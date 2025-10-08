package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.NotificacionDTO;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zonas;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonasRepository zonasRepository;

    /**
     * Genera una lista simple de "notificaciones" / recomendaciones basada en universidad, presupuesto y transporte.
     * Algoritmo simple:
     *  - Buscar zonas con precio <= presupuesto
     *  - Priorizar zonas que contengan el transporte del usuario
     *  - Retornar top N (por defecto 5)
     */
    public List<NotificacionDTO> generarRecomendacionesParaUsuario(Integer usuarioId, int topN) {
        Optional<Usuario> uOpt = usuarioRepository.findById(usuarioId);
        if (!uOpt.isPresent()) return Collections.emptyList();

        Usuario u = uOpt.get();
        // Requisitos mínimos: universidad y presupuesto (RN-18)
        if (u.getUniversidad() == null || u.getUniversidad().trim().isEmpty() || u.getPresupuesto() == null) {
            return Collections.emptyList();
        }

        Double presupuesto = u.getPresupuesto();
        String transporte = u.getTransporte() == null ? "" : u.getTransporte().toLowerCase().trim();

        // 1) Zonas con precio <= presupuesto
        List<Zonas> porPrecio = zonasRepository.findByPrecioPromedioLessThanEqual(presupuesto);

        // 2) Si no hay resultados, tomar zonas cercanas por seguridad o transporte (fallback)
        if (porPrecio.isEmpty()) {
            porPrecio = zonasRepository.findAll();
        }

        // 3) Construir lista con prioridad: contiene transporte -> motivo "coincide transporte", else "ajusta a presupuesto" o "recomendado"
        List<NotificacionDTO> result = new ArrayList<>();
        for (Zonas z : porPrecio) {
            NotificacionDTO n = new NotificacionDTO();
            n.setZonaId(z.getIdZonas());
            n.setZonaNombre(z.getNombre());
            n.setPrecioPromedio(z.getPrecioPromedio());
            String motivo = "Ajuste a tu presupuesto";

            if (!transporte.isEmpty() && z.getTransporteDisponible() != null && z.getTransporteDisponible().toLowerCase().contains(transporte)) {
                motivo = "Buena conexión con tu medio de transporte: " + transporte;
            } else if (z.getSeguridad() != null && z.getSeguridad().toLowerCase().contains("alta")) {
                motivo = "Zona con seguridad alta";
            }
            n.setMotivo(motivo);
            result.add(n);
        }

        // Orden simple: priorizar coincidencias por transporte, luego precio ascendente
        List<NotificacionDTO> sorted = result.stream()
                .sorted(Comparator.comparing((NotificacionDTO nd) -> !nd.getMotivo().toLowerCase().contains("conexión"))
                        .thenComparing(NotificacionDTO::getPrecioPromedio))
                .collect(Collectors.toList());

        if (topN <= 0) topN = 5;
        return sorted.stream().limit(topN).collect(Collectors.toList());
    }
}
