package com.viviestu.viviestu_api.dto.response;

/// DTO para devolver datos de una zona marcada como favorita
public record FavoritoResponse(
        Integer idFavorito,
        Long idUsuario,
        Integer idZona,
        String nombreZona,
        Double precioPromedio,
        String seguridad
) {}
