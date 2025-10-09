package com.viviestu.viviestu_api.dto;

import jakarta.validation.constraints.NotNull;

public class RecomendarRequest {

    @NotNull(message = "idUsuario es obligatorio")
    private Integer idUsuario;

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}
