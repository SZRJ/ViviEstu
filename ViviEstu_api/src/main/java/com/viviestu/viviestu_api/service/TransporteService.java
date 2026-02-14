// Archivo: service/TransporteService.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.TiempoTransporteResponse;
import org.springframework.stereotype.Service;
// En un caso real, importarías el SDK de Google Maps
import java.util.Random;

@Service
public class TransporteService {

    /**
     * Implementación MOCK (Simulada) de RN-09.
     * En un proyecto real, este método llamaría a la API de Google Directions.
     */
    public TiempoTransporteResponse calcularTiempo(Integer idZona, String destino, String modo) {

        // Simulación
        Random rand = new Random();
        String tiempo = (rand.nextInt(40) + 10) + " min"; // 10-50 min
        String dist = (rand.nextInt(15) + 2) + " km"; // 2-17 km
        String nombreZona = "Zona ID " + idZona; // (Buscarías el nombre en ZonaRepository)

        return new TiempoTransporteResponse(nombreZona, destino, modo, tiempo, dist);
    }
}