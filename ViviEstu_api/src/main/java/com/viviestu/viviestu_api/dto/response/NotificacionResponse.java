package com.viviestu.viviestu_api.dto.response;

/// Representa una recomendación o notificación personalizada para el usuario
public record NotificacionResponse(
        Integer idZona,
        String nombreZona,
        String motivo,
        Double precioPromedio
) {}
