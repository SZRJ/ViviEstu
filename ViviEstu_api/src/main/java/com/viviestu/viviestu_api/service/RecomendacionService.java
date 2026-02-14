package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.NotificacionResponse;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RecomendacionService {

    @Autowired
    private ZonaRepository zonaRepository;

    /**
     * Genera recomendaciones simples: convierte las zonas a NotificacionDTO y devuelve las topN por precio.
     */
    public List<NotificacionResponse> generarRecomendacionesParaUsuario(Long usuarioId, int topN) {
        List<Zona> zonas = zonaRepository.findAll();
        List<NotificacionResponse> recomendaciones = new ArrayList<>();

        for (Zona z : zonas) {
            // NotificacionResponse is an immutable record; create instances directly
            NotificacionResponse dto = new NotificacionResponse(
                    z.getIdZona(),
                    z.getNombre(),
                    "Recomendado",
                    z.getPrecioPromedio()
            );
            recomendaciones.add(dto);
        }

        recomendaciones.sort(Comparator.comparing(n -> n.precioPromedio() == null ? Double.MAX_VALUE : n.precioPromedio()));
        if (topN <= 0) topN = 5;
        return recomendaciones.stream().limit(topN).toList();
    }
}
