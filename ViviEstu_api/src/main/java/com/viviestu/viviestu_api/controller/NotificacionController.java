package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.response.NotificacionResponse;
import com.viviestu.viviestu_api.service.RecomendacionService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controlador para obtener las notificaciones / recomendaciones del usuario
@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    /// GET /api/notificaciones/{idUsuario}?top=N
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> obtenerRecomendaciones(
            @PathVariable Long idUsuario,
            @RequestParam(value = "top", defaultValue = "5") int top) {

        List<NotificacionResponse> lista = recomendacionService.generarRecomendacionesParaUsuario(idUsuario, top);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Recomendaciones generadas correctamente", lista)
        );
    }
}
