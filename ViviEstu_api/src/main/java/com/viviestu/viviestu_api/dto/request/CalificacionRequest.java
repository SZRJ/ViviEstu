package com.viviestu.viviestu_api.dto.request;

/// DTO para registrar o actualizar calificación
public record CalificacionRequest(
        Long idUsuario,
        Integer puntuacion
) {}
