package com.viviestu.viviestu_api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class DirectionsController {

    @Value("${google.directions.api.key:}")
    private String googleApiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Proxy endpoint to Google Directions API. Requires server-side key configured in application.properties
     * Query parameters:
     *  - origin: "lat,lng"
     *  - destination: "lat,lng"
     *  - mode: walking|bicycling|driving (default: driving)
     *  - departure: optional; if 'now' and mode=driving will request traffic-aware duration_in_traffic
     *
     * Response: JSON with distanceMeters, distanceKm, durationSeconds, durationMinutes, durationHours, mode, source
     */
    @GetMapping("/directions")
    public ResponseEntity<?> directions(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false, defaultValue = "driving") String mode,
            @RequestParam(required = false) String departure
    ) {
        if (googleApiKey == null || googleApiKey.isBlank()) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Server-side Google Directions API key is not configured");
            err.put("hint", "Set google.directions.api.key in application.properties or use frontend fallback");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }

        try {
            String base = "https://maps.googleapis.com/maps/api/directions/json";
            StringBuilder sb = new StringBuilder(base).append("?")
                    .append("origin=").append(URLEncoder.encode(origin, StandardCharsets.UTF_8))
                    .append("&destination=").append(URLEncoder.encode(destination, StandardCharsets.UTF_8))
                    .append("&mode=").append(URLEncoder.encode(mode, StandardCharsets.UTF_8))
                    .append("&key=").append(URLEncoder.encode(googleApiKey, StandardCharsets.UTF_8));

            // Optionally request traffic-aware durations (duration_in_traffic) when departure=now
            if ("now".equalsIgnoreCase(departure) && "driving".equalsIgnoreCase(mode)) {
                sb.append("&departure_time=now");
            }

            String url = sb.toString();
            RestTemplate rt = new RestTemplate();
            String raw = rt.getForObject(url, String.class);
            JsonNode root = mapper.readTree(raw);

            if (root.has("status") && !"OK".equals(root.get("status").asText())) {
                Map<String, Object> err = new HashMap<>();
                err.put("error", "Google Directions API returned status: " + root.get("status").asText());
                if (root.has("error_message")) err.put("message", root.get("error_message").asText());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(err);
            }

            JsonNode routes = root.path("routes");
            if (!routes.isArray() || routes.size() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", "No routes returned by Google Directions"));
            }

            JsonNode leg = routes.get(0).path("legs").get(0);
            long distanceMeters = leg.path("distance").path("value").asLong(0);
            long durationSeconds = 0;
            // prefer duration_in_traffic when available
            if (leg.has("duration_in_traffic") && leg.path("duration_in_traffic").has("value")) {
                durationSeconds = leg.path("duration_in_traffic").path("value").asLong(0);
            } else if (leg.has("duration") && leg.path("duration").has("value")) {
                durationSeconds = leg.path("duration").path("value").asLong(0);
            }

            double distanceKm = distanceMeters / 1000.0;
            double durationMinutes = Math.round((durationSeconds / 60.0));
            double durationHours = Math.round((durationSeconds / 3600.0) * 100.0) / 100.0; // 2 decimals

            Map<String, Object> out = new HashMap<>();
            out.put("source", "google");
            out.put("mode", mode);
            out.put("distanceMeters", distanceMeters);
            out.put("distanceKm", distanceKm);
            out.put("durationSeconds", durationSeconds);
            out.put("durationMinutes", (long) durationMinutes);
            out.put("durationHours", durationHours);

            return ResponseEntity.ok(out);

        } catch (Exception ex) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "Failed to call Google Directions API");
            err.put("exception", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(err);
        }
    }
}
