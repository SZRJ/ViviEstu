package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.TravelTimeResponse;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelTimeService {

    @Autowired
    private ZonaRepository zonaRepository;

    // Average speeds (km/h)
    private static final double TAXI_SPEED_KMH = 40.0; // ciudad
    private static final double BICYCLE_SPEED_KMH = 15.0;
    private static final double WALK_SPEED_KMH = 5.0;

    public TravelTimeResponse calcularTiemposDesdeZona(Integer zonaId, Double destLat, Double destLon) {
        if (zonaId == null || destLat == null || destLon == null) return null;
        Zona z = zonaRepository.findById(zonaId).orElse(null);
        if (z == null) return null;
        if (z.getLatitud() == null || z.getLongitud() == null) return null;

        double distKm = haversineDistanceKm(z.getLatitud(), z.getLongitud(), destLat, destLon);

        double taxiMinutes = toMinutes(distKm, TAXI_SPEED_KMH);
        double bicycleMinutes = toMinutes(distKm, BICYCLE_SPEED_KMH);
        double walkingMinutes = toMinutes(distKm, WALK_SPEED_KMH);

        return new TravelTimeResponse(distKm, taxiMinutes, bicycleMinutes, walkingMinutes);
    }

    private double toMinutes(double distanceKm, double speedKmh) {
        if (speedKmh <= 0) return -1;
        double hours = distanceKm / speedKmh;
        return Math.round(hours * 60.0 * 100.0) / 100.0; // rounded to 2 decimals
    }

    // Haversine formula to compute distance in kilometers between two lat/lon points
    private double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
