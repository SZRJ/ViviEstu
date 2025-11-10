package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.CalificacionRequest;
import com.viviestu.viviestu_api.model.Calificacion;
import com.viviestu.viviestu_api.service.CalificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/zonas/{id}/calificaciones
 */
@RestController
@RequestMapping("/api/zonas")
public class CalificacionesController {

    @Autowired
    private CalificacionService calificacionService;

    @PostMapping("/{id}/calificaciones")
    public ResponseEntity<?> registrarCalificacion(@PathVariable("id") Integer zonaId, @RequestBody CalificacionRequest request) {
        try {
            if (request == null || request.getUsuarioId() == null || request.getPuntuacion() == null) {
                return ResponseEntity.badRequest().body("{\"mensaje\":\"Datos insuficientes\"}");
            }
            Calificacion c = calificacionService.registrarCalificacion(zonaId, request.getUsuarioId(), request.getPuntuacion());
            return ResponseEntity.status(201).body(c);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("{\"mensaje\":\"Error interno\"}");
        }
    }
}
