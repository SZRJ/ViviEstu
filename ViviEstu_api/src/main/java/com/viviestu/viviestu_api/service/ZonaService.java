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
import java.util.stream.Collectors;
import java.util.Comparator;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

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

    /**
     * Implementación de RN-10 (Filtro).
     * Usa JPA Specification para filtros básicos y Stream/Reflection para filtros complejos (Distancia, Orden).
     */
    public List<ZonaResponse> filtrarZonas(FiltroRequest req) {

        // 1. Crear la especificación de JPA para filtros básicos (Nombre, Precio, Seguridad, Transporte)
        Specification<Zona> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por precio MÍNIMO (agregado de la versión antigua)
            if (req.minPrecio() != null && req.minPrecio() >= 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("precioPromedio"), req.minPrecio()));
            }
            // Filtro por precio MÁXIMO (ya existía)
            if (req.maxPrecio() != null && req.maxPrecio() > 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("precioPromedio"), req.maxPrecio()));
            }
            // Filtro por seguridad (ya existía)
            if (req.seguridad() != null && !req.seguridad().trim().isEmpty()) {
                predicates.add(cb.equal(root.get("seguridad"), req.seguridad()));
            }
            // Filtro por transporte (ya existía)
            if (req.transporte() != null && !req.transporte().trim().isEmpty()) {
                predicates.add(cb.like(root.get("transporteDisponible"), "%" + req.transporte() + "%"));
            }
            // Filtro por nombre (ya existía)
            if (req.nombre() != null && !req.nombre().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + req.nombre().toLowerCase() + "%"));
            }
            // Nota: Las fechas (fechaInicio, fechaFin) no se filtran aquí ya que requieren un modelo de disponibilidad/reserva

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 2. Ejecutar la consulta con la Specification
        List<Zona> zonasFiltradas = zonaRepository.findAll(spec);

        // 3. Filtrado y Ordenamiento Post-consulta (Lógica de Distancia)
        boolean ordenarPorDistancia = req.ordenarPorDistancia() != null && req.ordenarPorDistancia();
        boolean necesitaFiltroDistancia = req.maxDistancia() != null;

        // 3a. Filtrar por Distancia si es necesario (usa Reflection como la versión antigua)
        if (necesitaFiltroDistancia) {
            zonasFiltradas = zonasFiltradas.stream()
                    .filter(z -> {
                        try {
                            // Intentar obtener el método getDistancia()
                            Method m = z.getClass().getMethod("getDistancia");
                            Object val = m.invoke(z);
                            if (val instanceof Number) {
                                double dist = ((Number) val).doubleValue();
                                return dist <= req.maxDistancia(); // Mantener si está dentro del maxDistancia
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                            // Si no hay método o error, no filtramos por distancia para esta zona
                        }
                        // Si el filtro está activado pero no se pudo obtener la distancia, se excluye
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        // 3b. Ordenamiento
        if (ordenarPorDistancia) {
            try {
                // Intentar ordenar por getDistancia()
                zonasFiltradas.sort(Comparator.comparingDouble(z -> {
                    try {
                        Method m = z.getClass().getMethod("getDistancia");
                        Object val = m.invoke(z);
                        if (val instanceof Number) return ((Number) val).doubleValue();
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                        // Si no tiene getDistancia(), se le da un valor máximo para ir al final
                    }
                    return Double.MAX_VALUE;
                }));
            } catch (Exception e) {
                // Si falla el ordenamiento por distancia, se ordena por precio
                ordenarPorPrecio(zonasFiltradas);
            }
        } else {
            // Si no se pide ordenar por distancia, ordenar por precio ascendente por defecto
            ordenarPorPrecio(zonasFiltradas);
        }

        // 4. Mapear a ZonaResponse
        return zonasFiltradas.stream()
                .map(z -> new ZonaResponse(z.getIdZona(), z.getNombre(), z.getPrecioPromedio(),
                        z.getSeguridad(), z.getTransporteDisponible(), z.getRecomendado()))
                .toList();
    }

    /** Método auxiliar para ordenar por precio */
    private void ordenarPorPrecio(List<Zona> zonas) {
        zonas.sort(Comparator.comparing(z -> z.getPrecioPromedio() == null ? Double.MAX_VALUE : z.getPrecioPromedio()));
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