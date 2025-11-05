package com.viviestu.viviestu_api.dto.request;

/// DTO para crear usuario (registro)
public record RegistroRequest(
        String nombre,
        String nombreUsuario,
        String fechaNacimiento,
        String correo,
        String contrasena
) {}
