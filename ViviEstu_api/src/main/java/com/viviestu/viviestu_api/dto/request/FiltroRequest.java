package com.viviestu.viviestu_api.dto.request;

import java.time.LocalDate;

/// DTO para recibir parámetros de filtrado desde el cliente.
/// Incluye filtros por precio, seguridad, transporte, nombre, distancia y disponibilidad.
public record FiltroRequest(
        // Filtros de zona
        String nombre, // Usado para 'zona' o fragmento de nombre

        // Filtros de precio
        Double minPrecio,
        Double maxPrecio,

        // Filtros de características de la zona
        String seguridad,   // e.g., "alta", "media", "baja"
        String transporte,

        // Filtro de distancia (opcional, requiere datos geográficos)
        Double maxDistancia, // metros o km según convención del proyecto

        // Filtro de disponibilidad (requiere lógica de fechas en el backend)
        LocalDate fechaInicio,
        LocalDate fechaFin,

        // Opción de ordenamiento
        Boolean ordenarPorDistancia // si true, ordena por distancia; si false/null, por precio
) {}