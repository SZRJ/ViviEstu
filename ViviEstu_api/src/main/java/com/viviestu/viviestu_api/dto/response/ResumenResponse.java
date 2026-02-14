// Archivo: dto/response/ResumenResponse.java
// (Para RN-20 / US20: Resumen de búsqueda)
package com.viviestu.viviestu_api.dto.response;

import java.util.List;

public record ResumenResponse(
        int totalFavoritos,
        int totalCalificaciones,
        int totalComentarios,
        List<String> zonasFavoritas // Nombres de las zonas
) {}