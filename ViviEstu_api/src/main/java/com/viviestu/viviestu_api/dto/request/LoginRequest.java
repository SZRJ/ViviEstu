package com.viviestu.viviestu_api.dto.request;

/// DTO para login
public record LoginRequest(
        String correo,
        String contrasena
) {}
