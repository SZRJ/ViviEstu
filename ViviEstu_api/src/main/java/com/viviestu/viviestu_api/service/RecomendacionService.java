package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.NotificacionResponse;
import com.viviestu.viviestu_api.model.Preferencia;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/// Servicio que genera recomendaciones de zonas según las preferencias del usuario
@Service
public class RecomendacionService {

    @Autowired
    private PreferenciaRepository preferenciaRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    /// Genera una lista de recomendaciones personalizadas
    public List<NotificacionResponse> generarRecomendacionesParaUsuario(Long idUsuario, int topN) {

        // RN-17: Verificar que el usuario tenga preferencias registradas
        List<Preferencia> preferencias = preferenciaRepository.findByUsuarioIdUsuario(idUsuario);
        if (preferencias.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene preferencias registradas.");
        }

        // Tomamos la primera preferencia activa (simplificación)
        Preferencia pref = preferencias.get(0);

        Float presupuesto = pref.getPresupuesto();
        String transporte = pref.getTransporte().toLowerCase();
        String seguridad = pref.getSeguridad().toLowerCase();

        // RN-18: Buscar zonas que cumplan presupuesto
        List<Zona> zonas = zonaRepository.findByPrecioPromedioLessThanEqual(Double.valueOf(presupuesto));

        if (zonas.isEmpty()) {
            zonas = zonaRepository.findAll();
        }

        // Filtrado adicional (seguridad o transporte)
        List<Zona> filtradas = zonas.stream()
                .filter(z -> z.getSeguridad() != null && z.getSeguridad().toLowerCase().contains(seguridad)
                        || (z.getTransporteDisponible() != null
                        && z.getTransporteDisponible().toLowerCase().contains(transporte)))
                .collect(Collectors.toList());

        if (filtradas.isEmpty()) filtradas = zonas; // RN-20 fallback

        // Construir lista de recomendaciones
        List<NotificacionResponse> notificaciones = filtradas.stream()
                .map(z -> {
                    String motivo;
                    if (z.getTransporteDisponible() != null &&
                            z.getTransporteDisponible().toLowerCase().contains(transporte)) {
                        motivo = "Buena conexión con tu medio de transporte: " + transporte;
                    } else if (z.getSeguridad() != null && z.getSeguridad().toLowerCase().contains("alta")) {
                        motivo = "Zona con alta seguridad";
                    } else {
                        motivo = "Ajuste a tu presupuesto y preferencias";
                    }

                    return new NotificacionResponse(
                            z.getIdZona(),
                            z.getNombre(),
                            motivo,
                            z.getPrecioPromedio()
                    );
                }).collect(Collectors.toList());

        // RN-19: Priorizar seguridad y transporte
        notificaciones = notificaciones.stream()
                .sorted(Comparator
                        .comparing((NotificacionResponse n) -> !n.motivo().toLowerCase().contains("transporte"))
                        .thenComparing(NotificacionResponse::precioPromedio))
                .limit(topN > 0 ? topN : 5)
                .collect(Collectors.toList());

        return notificaciones;
    }
}
