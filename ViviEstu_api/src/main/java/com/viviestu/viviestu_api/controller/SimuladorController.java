package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.SimulacionGastoRequest;
import com.viviestu.viviestu_api.service.SimuladorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/simulador")
public class SimuladorController {

    private final SimuladorService simuladorService;

    public SimuladorController(SimuladorService simuladorService) {
        this.simuladorService = simuladorService;
    }

    @PostMapping("/gasto")
    public ResponseEntity<Map<String, Object>> simular(@Valid @RequestBody SimulacionGastoRequest request) {
        Map<String, Object> resp = simuladorService.simularGasto(request);
        return ResponseEntity.ok(resp);
    }
}