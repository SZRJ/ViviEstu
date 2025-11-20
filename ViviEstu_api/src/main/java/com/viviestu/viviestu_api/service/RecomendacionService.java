package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.NotificacionDTO;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    @Autowired
    private ZonaRepository zonaRepository;

    /**
     * Genera una lista simple de "notificaciones" / recomendaciones basada en universidad, presupuesto y transporte.
     * Algoritmo simple:
     *  - Buscar zonas con precio <= presupuesto
     *  - Priorizar zonas que contengan el transporte del usuario
     *  - Retornar top N (por defecto 5)
     */
    public List<NotificacionDTO> generarRecomendacionesParaUsuario(Long usuarioId, int topN) {
        // Implementación mínima: devolver las zonas disponibles como recomendaciones genéricas.
        List<Zona> zonas = zonaRepository.findAll();
        List<NotificacionDTO> result = new ArrayList<>();
        for (Zona z : zonas) {
            NotificacionDTO n = new NotificacionDTO();
            n.setZonaId(z.getIdZona());
            n.setZonaNombre(z.getNombre());
            n.setPrecioPromedio(z.getPrecioPromedio());
            n.setMotivo("Recomendado");
            result.add(n);
        }

        result.sort(Comparator.comparing(n -> n.getPrecioPromedio() == null ? Double.MAX_VALUE : n.getPrecioPromedio()));
        if (topN <= 0) topN = 5;
        return result.stream().limit(topN).toList();
    }
}
