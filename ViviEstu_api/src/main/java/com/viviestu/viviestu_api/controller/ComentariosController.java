package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.ComentarioRequest;
import com.viviestu.viviestu_api.model.Comentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/zonas/{id}/comentarios
 */
@RestController
@RequestMapping("/api/zonas")
public class ComentariosController {

    @Autowired
    private ComentariosService comentarioService;

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> agregarComentario(@PathVariable("id") Integer zonaId, @RequestBody ComentarioRequest request) {
        try {
            if (request == null || request.getUsuarioId() == null || request.getComentario() == null) {
                return ResponseEntity.badRequest().body("{\"mensaje\":\"Datos insuficientes\"}");
            }
            Comentario creado = comentarioService.agregarComentario(zonaId, request.getUsuarioId(), request.getComentario());
            return ResponseEntity.status(201).body(creado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("{\"mensaje\":\"Error interno\"}");
        }
    }
}
