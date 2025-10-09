package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.RecomendarRequest;
import com.viviestu.viviestu_api.service.RecomendacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/zonas")
public class RecomendacionController {

    private final RecomendacionService service;

    public RecomendacionController(RecomendacionService service) {
        this.service = service;
    }

    @PostMapping("/{id}/recomendar")
    public ResponseEntity<Map<String, Object>> recomendar(@PathVariable("id") Integer idZona,
                                                          @Valid @RequestBody RecomendarRequest request) {
        return ResponseEntity.ok(service.toggle(idZona, request));
    }
}
