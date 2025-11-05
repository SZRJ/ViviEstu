package com.viviestu.viviestu_api.model;

import jakarta.persistence.*;

/// Entidad que representa las zonas registradas en el sistema.
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "precio_promedio")
    private Double precioPromedio;

    @Column
    private String seguridad; // alta, media, baja

    @Column(name = "transporte_disponible")
    private String transporteDisponible; // bus, metro, etc.

    @Column
    private Boolean recomendado = false;

    public Zona() {}

    /// Getters y setters
    public Integer getIdZona() { return idZona; }
    public void setIdZona(Integer idZona) { this.idZona = idZona; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecioPromedio() { return precioPromedio; }
    public void setPrecioPromedio(Double precioPromedio) { this.precioPromedio = precioPromedio; }

    public String getSeguridad() { return seguridad; }
    public void setSeguridad(String seguridad) { this.seguridad = seguridad; }

    public String getTransporteDisponible() { return transporteDisponible; }
    public void setTransporteDisponible(String transporteDisponible) { this.transporteDisponible = transporteDisponible; }

    public Boolean getRecomendado() { return recomendado; }
    public void setRecomendado(Boolean recomendado) { this.recomendado = recomendado; }
}
