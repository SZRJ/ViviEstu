package com.viviestu.viviestu_api.dto;

public class ComentarioRequest {
    private Long usuarioId;
    private String comentario;

    public ComentarioRequest() {}

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
