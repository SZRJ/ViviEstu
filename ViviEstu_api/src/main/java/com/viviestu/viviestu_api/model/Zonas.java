package com.viviestu.viviestu_api.model;

import javax.persistence.*;

@Entity
@Table(name = "zonas")

public class Zonas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idZona;

    @Column(nullable = false)
    private String nombre;

    private Double precioPromedio;
    private String seguridad; // ej "alta", "media", "baja"
    private String transporteDisponible; // ej "bus, carro, caminar"

    public Zonas() {}

    // Getters / Setters
    public Integer getIdZonas() { return idZona; }
    public void setIdZona(Integer idZona) { this.idZona = idZona; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecioPromedio() { return precioPromedio; }
    public void setPrecioPromedio(Double precioPromedio) { this.precioPromedio = precioPromedio; }

    public String getSeguridad() { return seguridad; }
    public void setSeguridad(String seguridad) { this.seguridad = seguridad; }

    public String getTransporteDisponible() { return transporteDisponible; }
    public void setTransporteDisponible(String transporteDisponible) { this.transporteDisponible = transporteDisponible; }
}
