package com.viviestu.viviestu_api.dto.request;

/// DTO para actualizar perfil (incluye campos de RN-03 y RN-04)
public record PerfilRequest(
        String universidad,
        Float presupuesto,
        String transporte,
        String fechaNacimiento,
        String nombre,
        String nombreUsuario
) {}
