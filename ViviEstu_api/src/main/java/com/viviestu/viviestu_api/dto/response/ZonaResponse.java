package com.viviestu.viviestu_api.dto.response;

/// DTO de respuesta general para zonas
public record ZonaResponse(
        Integer idZona,
        String nombre,
        Double precioPromedio,
        String seguridad,
        String transporteDisponible,
        Boolean recomendado
) {}
