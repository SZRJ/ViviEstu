package com.viviestu.viviestu_api.dto.response;

import java.time.LocalDate; // <-- AÑADIR ESTA IMPORTACIÓN

/// Respuesta simplificada de usuario que se devuelve al cliente (oculta contraseña)
public record UsuarioResponse(
        Long idUsuario,
        String nombre,
        String nombreUsuario,
        LocalDate fechaNacimiento, // <-- CAMBIADO DE STRING A LOCALDATE
        String correo,
        boolean verificado,
        boolean activo
) {}