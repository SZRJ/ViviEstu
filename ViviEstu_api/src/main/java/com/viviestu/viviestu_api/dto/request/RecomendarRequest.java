package com.viviestu.viviestu_api.dto.request;

/// DTO para marcar o desmarcar una zona como recomendada
public record RecomendarRequest(
        Integer idZona,
        Boolean recomendado
) {}
