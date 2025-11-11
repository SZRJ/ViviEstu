package com.viviestu.viviestu_api.dto;

public class TravelTimeRequest {
    private Integer zonaId; // origen
    private Double destLat;
    private Double destLon;

    public TravelTimeRequest() {}

    public Integer getZonaId() { return zonaId; }
    public void setZonaId(Integer zonaId) { this.zonaId = zonaId; }

    public Double getDestLat() { return destLat; }
    public void setDestLat(Double destLat) { this.destLat = destLat; }

    public Double getDestLon() { return destLon; }
    public void setDestLon(Double destLon) { this.destLon = destLon; }
}
