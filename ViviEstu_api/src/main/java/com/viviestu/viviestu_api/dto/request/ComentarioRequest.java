package com.viviestu.viviestu_api.dto.request;

/// DTO para registrar un nuevo comentario
public record ComentarioRequest(
        Long idUsuario,
        String comentario
) {}
