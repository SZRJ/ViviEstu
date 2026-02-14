package com.viviestu.viviestu_api.dto.request;

public record TravelTimeRequest(
        Integer zonaId, // ID de la Zona de Origen


        Double destLat, // Latitud del Destino
        Double destLon  //Longitud del Destino
) { }
