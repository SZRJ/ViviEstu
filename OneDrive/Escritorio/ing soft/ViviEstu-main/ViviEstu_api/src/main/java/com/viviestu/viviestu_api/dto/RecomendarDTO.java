package com.viviestu.viviestu_api.dto;

public class RecomendarDTO {
    private Integer idZona;
    private Boolean recomendado; // ej "true" = Recomendado, "false" = Recomendar

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public Boolean getRecomendado() {
        return recomendado;
    }

    public void setRecomendado(Boolean recomendado) {
        this.recomendado = recomendado;
    }
}
