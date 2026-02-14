package com.viviestu.viviestu_api.dto.response;

/// DTO para devolver información de una calificación
public record CalificacionResponse(
        Integer idCalificacion,
        Long idUsuario,
        Integer idZona,
        Integer puntuacion
) {}
