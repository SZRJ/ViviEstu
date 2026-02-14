package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.response.NotificacionResponse;
import com.viviestu.viviestu_api.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET /api/notificaciones/{usuarioId}
 */
@RestController
@RequestMapping("/api")
public class NotificacionesController {

    @Autowired
    private RecomendacionService recomendacionService;

    @GetMapping("/notificaciones/{usuarioId}")
    public ResponseEntity<?> obtenerNotificaciones(@PathVariable("usuarioId") Long usuarioId,
                                                   @RequestParam(value = "top", required = false, defaultValue = "5") Integer top) {
        List<NotificacionResponse> lista = recomendacionService.generarRecomendacionesParaUsuario(usuarioId, top);
        if (lista.isEmpty()) {
            return ResponseEntity.ok().body("{\"notificaciones\": [], \"mensaje\": \"No hay recomendaciones (comprueba si tu perfil está completo)\"}");
        }
        return ResponseEntity.ok(lista);
    }
}
