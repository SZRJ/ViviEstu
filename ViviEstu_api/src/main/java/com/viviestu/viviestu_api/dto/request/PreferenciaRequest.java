package com.viviestu.viviestu_api.dto.request;

/// DTO de entrada para crear o actualizar una preferencia
public record PreferenciaRequest(
        Long idUsuario,
        String universidad,
        Float presupuesto,
        String transporte,
        Float tiempoMax,
        String seguridad
) {}
