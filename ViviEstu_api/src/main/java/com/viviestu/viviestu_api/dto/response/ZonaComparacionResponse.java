package com.viviestu.viviestu_api.dto.response;

/// DTO usado para el endpoint de comparación. Contiene todos los detalles de una zona,
/// incluyendo métricas de calificación y tiempos de viaje (si se proporcionó destino).
public record ZonaComparacionResponse(
        // Identificación y datos principales de la zona
        Integer idZona,
        String nombre,
        Double precioPromedio,
        String seguridad,
        String transporteDisponible,
        Boolean recomendado, // Indica si la zona es recomendada para el usuario (opcional)

        // Datos geográficos
        Double latitud,
        Double longitud,

        // Métricas de calificación
        Double promedioCalificacion,
        Long cantidadCalificaciones,

        // Tiempos de viaje (pueden ser nulos si no se calculan)
        Double distanceKm,
        Double taxiMinutes,
        Double bicycleMinutes,
        Double walkingMinutes
) {}