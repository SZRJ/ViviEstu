package com.viviestu.viviestu_api.dto;

public class ZonaInsertDTO {
    private String nombre;
    private Double precioPromedio;
    private String seguridad; // ej "alta", "media", "baja"
    private String transporteDisponible; // ej "bus, carro, caminar"

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioPromedio() {
        return precioPromedio;
    }

    public void setPrecioPromedio(Double precioPromedio) {
        this.precioPromedio = precioPromedio;
    }

    public String getSeguridad() {
        return seguridad;
    }

    public void setSeguridad(String seguridad) {
        this.seguridad = seguridad;
    }

    public String getTransporteDisponible() {
        return transporteDisponible;
    }

    public void setTransporteDisponible(String transporteDisponible) {
        this.transporteDisponible = transporteDisponible;
    }
}
