// Archivo: service/SimuladorService.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.SimuladorRequest;
import com.viviestu.viviestu_api.dto.response.SimuladorResponse;
import org.springframework.stereotype.Service;

@Service
public class SimuladorService {

    public SimuladorResponse simularGasto(SimuladorRequest req) {
        if (req.alquiler() == null || req.alquiler() <= 0) { // RN-17
            throw new IllegalArgumentException("El monto del alquiler debe ser mayor a 0");
        }

        double transporte = (req.costoTransporte() != null) ? req.costoTransporte() : 0.0;
        double total = req.alquiler() + transporte;

        return new SimuladorResponse(req.alquiler(), transporte, total);
    }
}