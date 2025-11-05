// Archivo: controller/SimuladorController.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.SimuladorRequest;
import com.viviestu.viviestu_api.dto.response.SimuladorResponse;
import com.viviestu.viviestu_api.service.SimuladorService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulador")
@CrossOrigin(origins = "*")
public class SimuladorController {

    @Autowired
    private SimuladorService simuladorService;

    /// POST /api/simulador/gasto
    @PostMapping("/gasto")
    public ResponseEntity<ApiResponse<SimuladorResponse>> simular(@RequestBody SimuladorRequest req) {
        SimuladorResponse res = simuladorService.simularGasto(req);
        return ResponseEntity.ok(new ApiResponse<>(200, "Simulación exitosa", res));
    }
}