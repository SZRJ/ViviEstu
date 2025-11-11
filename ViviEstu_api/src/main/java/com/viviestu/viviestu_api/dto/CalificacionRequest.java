package com.viviestu.viviestu_api.dto;

public class CalificacionRequest {
    private Long usuarioId;
    private Integer puntuacion; // 1-5

    public CalificacionRequest() {}

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Integer getPuntuacion() { return puntuacion; }
    public void setPuntuacion(Integer puntuacion) { this.puntuacion = puntuacion; }
}
