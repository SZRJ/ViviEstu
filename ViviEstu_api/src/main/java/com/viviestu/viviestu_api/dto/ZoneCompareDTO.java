package com.viviestu.viviestu_api.dto;

public class ZoneCompareDTO {
    private Integer idZona;
    private String nombre;
    private Double precioPromedio;
    private String seguridad;
    private String transporteDisponible;
    private Boolean recomendado;
    private Double latitud;
    private Double longitud;
    private Double promedioCalificacion;
    private Long cantidadCalificaciones;
    // Travel time fields (nullable if destination not provided)
    private Double distanceKm;
    private Double taxiMinutes;
    private Double bicycleMinutes;
    private Double walkingMinutes;

    public ZoneCompareDTO() {}

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

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Double getPromedioCalificacion() { return promedioCalificacion; }
    public void setPromedioCalificacion(Double promedioCalificacion) { this.promedioCalificacion = promedioCalificacion; }

    public Long getCantidadCalificaciones() { return cantidadCalificaciones; }
    public void setCantidadCalificaciones(Long cantidadCalificaciones) { this.cantidadCalificaciones = cantidadCalificaciones; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Double getTaxiMinutes() { return taxiMinutes; }
    public void setTaxiMinutes(Double taxiMinutes) { this.taxiMinutes = taxiMinutes; }

    public Double getBicycleMinutes() { return bicycleMinutes; }
    public void setBicycleMinutes(Double bicycleMinutes) { this.bicycleMinutes = bicycleMinutes; }

    public Double getWalkingMinutes() { return walkingMinutes; }
    public void setWalkingMinutes(Double walkingMinutes) { this.walkingMinutes = walkingMinutes; }
}
