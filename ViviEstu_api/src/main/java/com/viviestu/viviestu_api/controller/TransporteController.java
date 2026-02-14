// Archivo: controller/TransporteController.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.response.TiempoTransporteResponse;
import com.viviestu.viviestu_api.service.TransporteService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transporte")
@CrossOrigin(origins = "*")
public class TransporteController {

    @Autowired
    private TransporteService transporteService;

    /// GET /api/transporte/tiempo?zonaId=X&destino=Y&modo=Z
    @GetMapping("/tiempo")
    public ResponseEntity<ApiResponse<TiempoTransporteResponse>> obtenerTiempo(
            @RequestParam Integer zonaId,
            @RequestParam String destino,
            @RequestParam String modo) {

        TiempoTransporteResponse res = transporteService.calcularTiempo(zonaId, destino, modo);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cálculo de tiempo simulado", res));
    }
}