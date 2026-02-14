package com.viviestu.viviestu_api.dto.response;

import java.time.LocalDateTime;

/// DTO con promedio y último comentario por zona
public record ZonaComentarioResponse(
        Integer idZona,
        String nombreZona,
        Double promedioCalificacion,
        String comentario,
        LocalDateTime fecha
) {}
