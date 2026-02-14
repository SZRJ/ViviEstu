// Archivo: dto/response/SimuladorResponse.java
// (Para RN-17 / US17: Simulador de gasto)
package com.viviestu.viviestu_api.dto.response;

public record SimuladorResponse(
    Double alquiler,
    Double transporte,
    Double gastoTotal
) {}