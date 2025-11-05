package com.viviestu.viviestu_api.dto.response;

/// Respuesta simplificada de usuario que se devuelve al cliente (oculta contraseña)
public record UsuarioResponse(
        Long idUsuario,
        String nombre,
        String nombreUsuario,
        String fechaNacimiento,
        String correo,
        boolean verificado,
        boolean activo
) {}
