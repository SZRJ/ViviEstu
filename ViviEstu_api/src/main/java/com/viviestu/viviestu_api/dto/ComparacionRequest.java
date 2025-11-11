package com.viviestu.viviestu_api.dto;

import java.util.List;

public class ComparacionRequest {
    private Long usuarioId; // opcional: si se pasa, se usarán los favoritos del usuario
    private List<Integer> zonaIds; // opcional: lista de zonas a comparar

    public ComparacionRequest() {}

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public List<Integer> getZonaIds() { return zonaIds; }
    public void setZonaIds(List<Integer> zonaIds) { this.zonaIds = zonaIds; }
}
