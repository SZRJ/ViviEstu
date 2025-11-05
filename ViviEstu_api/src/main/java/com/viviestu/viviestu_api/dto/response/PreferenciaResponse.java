package com.viviestu.viviestu_api.dto.response;

/// DTO de salida con los datos de la preferencia registrada
public record PreferenciaResponse(
        Long idPreferencia,
        Long idUsuario,
        String universidad,
        Float presupuesto,
        String transporte,
        Float tiempoMax,
        String seguridad
) {}
