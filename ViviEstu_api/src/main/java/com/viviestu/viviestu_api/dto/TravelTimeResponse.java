package com.viviestu.viviestu_api.dto;

public class TravelTimeResponse {
    private double distanceKm;
    private double taxiMinutes;
    private double bicycleMinutes;
    private double walkingMinutes;

    public TravelTimeResponse() {}

    public TravelTimeResponse(double distanceKm, double taxiMinutes, double bicycleMinutes, double walkingMinutes) {
        this.distanceKm = distanceKm;
        this.taxiMinutes = taxiMinutes;
        this.bicycleMinutes = bicycleMinutes;
        this.walkingMinutes = walkingMinutes;
    }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getTaxiMinutes() { return taxiMinutes; }
    public void setTaxiMinutes(double taxiMinutes) { this.taxiMinutes = taxiMinutes; }

    public double getBicycleMinutes() { return bicycleMinutes; }
    public void setBicycleMinutes(double bicycleMinutes) { this.bicycleMinutes = bicycleMinutes; }

    public double getWalkingMinutes() { return walkingMinutes; }
    public void setWalkingMinutes(double walkingMinutes) { this.walkingMinutes = walkingMinutes; }
}
