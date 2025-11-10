package com.viviestu.viviestu_api.dto;

public class CalificacionRequest {
    private Integer usuarioId;
    private Integer puntuacion; // 1-5

    public CalificacionRequest() {}

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Integer puntuacion) { this.puntuacion = puntuacion; }
}
