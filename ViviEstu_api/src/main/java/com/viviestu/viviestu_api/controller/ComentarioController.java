package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.response.ComentarioResponse;
import com.viviestu.viviestu_api.model.Comentario;
import com.viviestu.viviestu_api.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viviestu.viviestu_api.dto.request.ComentarioRequest;


@RestController
@RequestMapping("/api/zonas")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<?> listarComentarios(
            @PathVariable("id") Integer zonaId,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        return ResponseEntity.ok(comentarioService.listarPorZona(zonaId, limit));
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable("id") Integer zonaId, @RequestBody ComentarioRequest request) {
        try {
            // Esta validación está bien, usa los métodos de acceso del record
            if (request == null || request.idUsuario() == null || request.comentario() == null) {
                return ResponseEntity.badRequest().body("{\"mensaje\":\"Datos insuficientes\"}");
            }

            // Cambia el tipo de la variable "creado" de "Comentario" a "ComentarioResponse"
            ComentarioResponse creado = comentarioService.agregarComentario(zonaId, request);
            // ===================================

            return ResponseEntity.status(201).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("{\"mensaje\":\"Error interno\"}");
        }
    }
}
