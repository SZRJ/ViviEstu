package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.ZonaInsertRequest;
import com.viviestu.viviestu_api.dto.response.ZonaComentarioResponse;
import com.viviestu.viviestu_api.dto.response.ZonaReporteResponse;
import com.viviestu.viviestu_api.dto.response.ZonaResponse;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.viviestu.viviestu_api.dto.request.FiltroRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/// Servicio para la lógica de negocio del módulo Zona
@Service
public class ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    /// Registrar nueva zona (RN-07: el precio promedio debe ser > 0)
    public Zona crearZona(ZonaInsertRequest dto) {
        if (dto == null || dto.nombre() == null || dto.precioPromedio() == null)
            throw new IllegalArgumentException("Datos incompletos para registrar la zona");

        if (dto.precioPromedio() <= 0)
            throw new IllegalArgumentException("El precio promedio debe ser mayor que 0");

        Zona z = new Zona();
        z.setNombre(dto.nombre());
        z.setPrecioPromedio(dto.precioPromedio());
        z.setSeguridad(dto.seguridad());
        z.setTransporteDisponible(dto.transporteDisponible());
        z.setRecomendado(false);

        return zonaRepository.save(z);
    }

    /// Listar todas las zonas registradas
    public List<ZonaResponse> listarZonas() {
        return zonaRepository.findAll().stream()
                .map(z -> new ZonaResponse(z.getIdZona(), z.getNombre(), z.getPrecioPromedio(),
                        z.getSeguridad(), z.getTransporteDisponible(), z.getRecomendado()))
                .toList();
    }

    /// Marcar una zona como recomendada o no recomendada
    public ZonaResponse marcarRecomendacion(Integer idZona, Boolean recomendado) {
        Zona z = zonaRepository.findById(idZona)
                .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada"));
        z.setRecomendado(recomendado);
        Zona guardada = zonaRepository.save(z);
        return new ZonaResponse(guardada.getIdZona(), guardada.getNombre(),
                guardada.getPrecioPromedio(), guardada.getSeguridad(),
                guardada.getTransporteDisponible(), guardada.getRecomendado());
    }

    /// Listar promedios de calificaciones y comentarios recientes
    public List<ZonaComentarioResponse> obtenerPromediosYComentarios() {
        List<Object[]> resultados = zonaRepository.obtenerPromediosYComentarios();
        List<ZonaComentarioResponse> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            lista.add(new ZonaComentarioResponse(
                    ((Number) fila[0]).intValue(),
                    (String) fila[1],
                    fila[2] != null ? ((Number) fila[2]).doubleValue() : null,
                    (String) fila[3],
                    fila[4] != null ? ((Timestamp) fila[4]).toLocalDateTime() : null
            ));
        }
        return lista;
    }

    /// Listar zonas que cumplen las preferencias del usuario
    public List<ZonaReporteResponse> listarZonasQueCumplen(Long idUsuario) {
        List<Object[]> filas = zonaRepository.listarZonasQueCumplen(idUsuario);
        List<ZonaReporteResponse> lista = new ArrayList<>();
        for (Object[] f : filas) {
            lista.add(new ZonaReporteResponse(
                    ((Number) f[0]).intValue(),
                    (String) f[1],
                    f[2] != null ? ((Number) f[2]).doubleValue() : null,
                    (String) f[3]
            ));
        }
        return lista;
    }

    public Optional<Zona> obtenerPorId(Integer idZona) {
        return zonaRepository.findById(idZona);
    }
    /// Implementación de RN-10 (Filtro manual)
    public List<ZonaResponse> filtrarZonas(FiltroRequest req) {

        Specification<Zona> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.precioMax() != null && req.precioMax() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precioPromedio"), req.precioMax()));
            }
            if (req.seguridad() != null && !req.seguridad().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("seguridad"), req.seguridad()));
            }
            if (req.transporte() != null && !req.transporte().trim().isEmpty()) {
                predicates.add(cb.like(root.get("transporteDisponible"), "%" + req.transporte() + "%"));
            }
            if (req.nombre() != null && !req.nombre().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + req.nombre().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return zonaRepository.findAll(spec).stream()
                .map(z -> new ZonaResponse(z.getIdZona(), z.getNombre(), z.getPrecioPromedio(),
                        z.getSeguridad(), z.getTransporteDisponible(), z.getRecomendado()))
                .toList();
    }

    // NUEVO MÉTODO (para US09)
    public List<ZonaResponse> listarZonasPorIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        // Limitar a 3 IDs como dice la US09 (opcional, pero buena validación)
        if (ids.size() > 3) {
            throw new IllegalArgumentException("Solo puedes comparar hasta 3 zonas a la vez.");
        }

        return zonaRepository.findByIdZonaIn(ids).stream()
                .map(z -> new ZonaResponse(z.getIdZona(), z.getNombre(), z.getPrecioPromedio(),
                        z.getSeguridad(), z.getTransporteDisponible(), z.getRecomendado()))
                .toList();
    }
    /**
     * Obtiene una Zona y la convierte a ZonaResponse
     * (NECESARIO PARA US05)
     */
    public ZonaResponse obtenerZonaPorId(Integer idZona) {
        Zona z = zonaRepository.findById(idZona)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Zona no encontrada con ID: " + idZona));

        return new ZonaResponse(
                z.getIdZona(),
                z.getNombre(),
                z.getPrecioPromedio(),
                z.getSeguridad(),
                z.getTransporteDisponible(),
                z.getRecomendado()
        );
    }
}
