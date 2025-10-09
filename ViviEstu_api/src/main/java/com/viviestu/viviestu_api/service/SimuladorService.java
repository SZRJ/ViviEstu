package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.SimulacionGastoRequest;
import com.viviestu.viviestu_api.model.MedioTransporte;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class SimuladorService {

    private static final Map<MedioTransporte, BigDecimal> COSTO_TRANSPORTE_DEFAULT = new EnumMap<>(MedioTransporte.class);
    static {
        COSTO_TRANSPORTE_DEFAULT.put(MedioTransporte.BUS,  new BigDecimal("120.00"));
        COSTO_TRANSPORTE_DEFAULT.put(MedioTransporte.AUTO, new BigDecimal("350.00"));
        COSTO_TRANSPORTE_DEFAULT.put(MedioTransporte.PIE,  BigDecimal.ZERO);
    }

    public Map<String, Object> simularGasto(SimulacionGastoRequest request) {
        BigDecimal alquiler = request.getAlquilerMensual().setScale(2, RoundingMode.HALF_UP);
        BigDecimal costoTransporte = request.getCostoTransporteOverride() != null
                ? request.getCostoTransporteOverride()
                : COSTO_TRANSPORTE_DEFAULT.getOrDefault(request.getMedioTransporte(), BigDecimal.ZERO);
        costoTransporte = costoTransporte.setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = alquiler.add(costoTransporte).setScale(2, RoundingMode.HALF_UP);

        Map<String, Object> resp = new HashMap<>();
        resp.put("alquilerMensual", alquiler);
        resp.put("costoTransporteMensual", costoTransporte);
        resp.put("totalMensual", total);
        resp.put("detalle", String.format(
                "Total = Alquiler (S/ %s) + Transporte (%s, S/ %s)",
                alquiler.toPlainString(), request.getMedioTransporte().name(), costoTransporte.toPlainString()
        ));
        return resp;
    }
}