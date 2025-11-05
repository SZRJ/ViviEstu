// Archivo: dto/response/TiempoTransporteResponse.java
// (Para RN-09 / US07: Tipo de transporte)
package com.viviestu.viviestu_api.dto.response;

public record TiempoTransporteResponse(
        String zonaOrigen,
        String destino,
        String modo,
        String tiempoEstimado,
        String distancia
) {}