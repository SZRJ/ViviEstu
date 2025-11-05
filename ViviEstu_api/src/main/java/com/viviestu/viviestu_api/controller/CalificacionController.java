package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.CalificacionRequest;
import com.viviestu.viviestu_api.dto.response.CalificacionResponse;
import com.viviestu.viviestu_api.service.CalificacionService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/// Controller para calificaciones de zonas
@RestController
@RequestMapping("/api/zonas/{idZona}/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    /// POST /api/zonas/{idZona}/calificaciones
    @PostMapping
    public ResponseEntity<ApiResponse<CalificacionResponse>> registrar(
            @PathVariable Integer idZona,
            @RequestBody CalificacionRequest req) {

        CalificacionResponse calificacion = calificacionService.registrarCalificacion(idZona, req);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Calificación registrada correctamente", calificacion));
    }

    /// GET /api/zonas/{idZona}/calificaciones/promedio
    @GetMapping("/promedio")
    public ResponseEntity<ApiResponse<Double>> obtenerPromedio(@PathVariable Integer idZona) {
        Double promedio = calificacionService.obtenerPromedioPorZona(idZona);
        return ResponseEntity.ok(new ApiResponse<>(200, "Promedio de calificaciones", promedio));
    }
}
