package com.viviestu.viviestu_api.dto.request;

/// DTO para agregar una zona a favoritos
public record FavoritoRequest(
        Long idUsuario,
        Integer idZona
) {}
