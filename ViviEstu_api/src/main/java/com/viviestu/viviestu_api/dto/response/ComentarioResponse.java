package com.viviestu.viviestu_api.dto.response;

import java.time.LocalDateTime;

/// DTO para devolver la información de un comentario
public record ComentarioResponse(
        Integer idExperiencia,
        Long idUsuario,
        String nombreUsuario,
        Integer idZona,
        String comentario,
        LocalDateTime fecha
) {}
