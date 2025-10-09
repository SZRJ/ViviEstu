package com.viviestu.viviestu_api.dto;

public class ComentarioRequest {
    private Integer usuarioId;
    private String comentario;

    public ComentarioRequest() {}

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
