package com.viviestu.viviestu_api.dto;

public class NotificacionDTO {
    private Integer zonaId;
    private String zonaNombre;
    private String motivo; // por qué se recomienda (ej "Ajuste a tu presupuesto")
    private Double precioPromedio;

    public NotificacionDTO() {}

    public Integer getZonaId() { return zonaId; }
    public void setZonaId(Integer zonaId) { this.zonaId = zonaId; }

    public String getZonaNombre() { return zonaNombre; }
    public void setZonaNombre(String zonaNombre) { this.zonaNombre = zonaNombre; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Double getPrecioPromedio() { return precioPromedio; }
    public void setPrecioPromedio(Double precioPromedio) { this.precioPromedio = precioPromedio; }
}
