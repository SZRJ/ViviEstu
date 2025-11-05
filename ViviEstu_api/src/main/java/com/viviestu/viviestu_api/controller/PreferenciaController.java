package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.PreferenciaRequest;
import com.viviestu.viviestu_api.dto.response.PreferenciaResponse;
import com.viviestu.viviestu_api.service.PreferenciaService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controlador para gestionar las preferencias del usuario
@RestController
@RequestMapping("/api/preferencias")
@CrossOrigin(origins = "*")
public class PreferenciaController {

    @Autowired
    private PreferenciaService preferenciaService;

    /// POST /api/preferencias
    @PostMapping
    public ResponseEntity<ApiResponse<PreferenciaResponse>> crear(@RequestBody PreferenciaRequest req) {
        PreferenciaResponse nueva = preferenciaService.crear(req);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Preferencia registrada correctamente", nueva));
    }

    /// GET /api/preferencias/{idUsuario}
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<List<PreferenciaResponse>>> listar(@PathVariable Long idUsuario) {
        List<PreferenciaResponse> lista = preferenciaService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(new ApiResponse<>(200, "Preferencias obtenidas correctamente", lista));
    }

    /// PUT /api/preferencias/{idPreferencia}
    @PutMapping("/{idPreferencia}")
    public ResponseEntity<ApiResponse<PreferenciaResponse>> actualizar(@PathVariable Long idPreferencia, @RequestBody PreferenciaRequest req) {
        PreferenciaResponse actualizada = preferenciaService.actualizar(idPreferencia, req);
        return ResponseEntity.ok(new ApiResponse<>(200, "Preferencia actualizada", actualizada));
    }

    /// DELETE /api/preferencias/{idPreferencia}
    @DeleteMapping("/{idPreferencia}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long idPreferencia) {
        preferenciaService.eliminar(idPreferencia);
        return ResponseEntity.ok(new ApiResponse<>(200, "Preferencia eliminada correctamente", null));
    }
}
