package com.viviestu.viviestu_api.dto.request;

/// DTO para registrar nuevas zonas
public record ZonaInsertRequest(
        String nombre,
        Double precioPromedio,
        String seguridad,
        String transporteDisponible
) {}
