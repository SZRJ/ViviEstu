package com.viviestu.viviestu_api.dto.response;

public record TravelTimeResponse(
        double distanceKm,      // Distancia entre origen y destino en kilómetros
        double taxiMinutes,     // Tiempo de viaje estimado en taxi (minutos)
        double bicycleMinutes,  // Tiempo de viaje estimado en bicicleta (minutos)
        double walkingMinutes   // Tiempo de viaje estimado a pie (minutos)
) {}
