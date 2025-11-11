package com.viviestu.viviestu_api.dto;

import java.time.LocalDate;

/**
 * DTO para recibir parámetros de filtrado desde el cliente.
 * Campos:
 * - zona: nombre o fragmento de nombre de la zona (opcional)
 * - minPrecio / maxPrecio: rango de precio promedio (opcional)
 * - maxDistancia: distancia máxima (opcional). Nota: solo se usa si la entidad Zona dispone del campo/ getter correspondiente.
 * - fechaInicio / fechaFin: rango de fechas para disponibilidad (opcional). Requiere modelo de disponibilidad para filtrar realmente.
 * - ordenarPorDistancia: si true intentará ordenar por distancia cuando esté disponible; en caso contrario ordena por precio asc.
 */
public class FiltroDTO {
    private String zona;
    private Double minPrecio;
    private Double maxPrecio;
    private Double maxDistancia; // metros o km según convención del proyecto
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean ordenarPorDistancia;

    public FiltroDTO() {}

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Double getMinPrecio() {
        return minPrecio;
    }

    public void setMinPrecio(Double minPrecio) {
        this.minPrecio = minPrecio;
    }

    public Double getMaxPrecio() {
        return maxPrecio;
    }

    public void setMaxPrecio(Double maxPrecio) {
        this.maxPrecio = maxPrecio;
    }

    public Double getMaxDistancia() {
        return maxDistancia;
    }

    public void setMaxDistancia(Double maxDistancia) {
        this.maxDistancia = maxDistancia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getOrdenarPorDistancia() {
        return ordenarPorDistancia;
    }

    public void setOrdenarPorDistancia(Boolean ordenarPorDistancia) {
        this.ordenarPorDistancia = ordenarPorDistancia;
    }
}

