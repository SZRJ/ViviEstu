// Archivo: dto/request/SimuladorRequest.java
// (Para RN-17 / US17: Simulador de gasto)
package com.viviestu.viviestu_api.dto.request;

public record SimuladorRequest(
        Long idUsuario,
        Double alquiler, // RN-17
        Double costoTransporte // Costo mensual aprox.
) {}