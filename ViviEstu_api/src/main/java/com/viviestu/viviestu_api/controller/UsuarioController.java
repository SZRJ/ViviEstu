package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.DesactivarRequest;
import com.viviestu.viviestu_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint:
 * PATCH /api/usuarios/{id}/desactivar
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarCuenta(@PathVariable("id") Integer id, @RequestBody DesactivarRequest request) {
        if (request == null || request.getConfirmacion() == null) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"Se requiere confirmación\"}");
        }

        boolean ok = usuarioService.desactivarCuenta(id, request.getConfirmacion());
        if (!ok) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"No se pudo desactivar la cuenta (usuario no encontrado, ya inactivo o confirmación false)\"}");
        }

        return ResponseEntity.ok().body("{\"mensaje\":\"Cuenta desactivada\"}");
    }
}
