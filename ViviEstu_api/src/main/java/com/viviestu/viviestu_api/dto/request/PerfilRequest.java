package com.viviestu.viviestu_api.dto.request;

import java.time.LocalDate; // <-- AÑADIR ESTA IMPORTACIÓN

/// DTO para actualizar perfil (incluye campos de RN-03 y RN-04)
public record PerfilRequest(
        String universidad,
        Float presupuesto,
        String transporte,
        LocalDate fechaNacimiento, // <-- CAMBIADO DE STRING A LOCALDATE
        String nombre,
        String nombreUsuario
) {}