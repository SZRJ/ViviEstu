// Archivo: controller/ResumenController.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.response.ResumenResponse;
import com.viviestu.viviestu_api.service.ResumenService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumen")
@CrossOrigin(origins = "*")
public class ResumenController {

    @Autowired
    private ResumenService resumenService;

    /// GET /api/resumen/{idUsuario}
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<ResumenResponse>> obtenerResumen(@PathVariable Long idUsuario) {
        ResumenResponse res = resumenService.obtenerResumen(idUsuario);
        return ResponseEntity.ok(new ApiResponse<>(200, "Resumen de actividad", res));
    }
}