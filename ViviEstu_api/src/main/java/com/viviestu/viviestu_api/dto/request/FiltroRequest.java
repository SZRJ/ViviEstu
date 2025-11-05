// Archivo: dto/request/FiltroRequest.java
// (Para RN-10 / US08: Filtros de búsqueda)
package com.viviestu.viviestu_api.dto.request;

public record FiltroRequest(
        Double precioMax,
        String seguridad, // "alta", "media", "baja"
        String transporte,
        String nombre // Filtro por nombre
) {}