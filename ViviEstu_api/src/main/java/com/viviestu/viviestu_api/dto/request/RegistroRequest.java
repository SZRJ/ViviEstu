package com.viviestu.viviestu_api.dto.request;

import java.time.LocalDate; // <-- AÑADIR ESTA IMPORTACIÓN

/// DTO para crear usuario (registro)
public record RegistroRequest(
        String nombre,
        String nombreUsuario,
        LocalDate fechaNacimiento, // <-- CAMBIADO DE STRING A LOCALDATE
        String correo,
        String contrasena
) {}