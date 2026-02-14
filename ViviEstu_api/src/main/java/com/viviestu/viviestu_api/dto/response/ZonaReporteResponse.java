package com.viviestu.viviestu_api.dto.response;

/// DTO con reporte de zonas recomendadas según preferencias
public record ZonaReporteResponse(
        Integer idZona,
        String nombre,
        Double precioPromedio,
        String seguridad
) {}
