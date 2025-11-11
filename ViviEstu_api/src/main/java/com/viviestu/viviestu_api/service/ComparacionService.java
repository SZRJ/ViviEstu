package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.ZoneCompareDTO;
import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.FavoritosRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComparacionService {

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private FavoritosRepository favoritosRepository;
    
    @Autowired
    private TravelTimeService travelTimeService;

    /**
     * Devuelve una lista de ZoneCompareDTO para las zonas solicitadas. Si se pasa usuarioId y no zonaIds,
     * se usan los favoritos del usuario.
     */
    public List<ZoneCompareDTO> comparar(Long usuarioId, List<Integer> zonaIds, Double destLat, Double destLon) {
        List<Integer> ids = new ArrayList<>();
        if ((zonaIds == null || zonaIds.isEmpty()) && usuarioId != null) {
            List<Favoritos> favs = favoritosRepository.findByUsuarioIdUsuario(usuarioId);
            if (favs != null) {
                ids.addAll(favs.stream().map(f -> f.getZona().getIdZona()).collect(Collectors.toList()));
            }
        } else if (zonaIds != null) {
            ids.addAll(zonaIds);
        }

        if (ids.isEmpty()) return new ArrayList<>();

        List<ZoneCompareDTO> out = new ArrayList<>();
        Iterable<Zona> zonas = zonaRepository.findAllById(ids);
        for (Zona z : zonas) {
            ZoneCompareDTO dto = new ZoneCompareDTO();
            dto.setIdZona(z.getIdZona());
            dto.setNombre(z.getNombre());
            dto.setPrecioPromedio(z.getPrecioPromedio());
            dto.setSeguridad(z.getSeguridad());
            dto.setTransporteDisponible(z.getTransporteDisponible());
            dto.setRecomendado(z.getRecomendado());
            dto.setLatitud(z.getLatitud());
            dto.setLongitud(z.getLongitud());

            Double avg = calificacionRepository.findPromedioPorZona(z.getIdZona());
            Long cnt = calificacionRepository.countByZonaId(z.getIdZona());
            dto.setPromedioCalificacion(avg != null ? Math.round(avg * 100.0) / 100.0 : null);
            dto.setCantidadCalificaciones(cnt != null ? cnt : 0L);

            // Si se proporcionó destino (lat/lon) intentamos calcular tiempos usando TravelTimeService
            if (destLat != null && destLon != null) {
                var tt = travelTimeService.calcularTiemposDesdeZona(z.getIdZona(), destLat, destLon);
                if (tt != null) {
                    dto.setDistanceKm(tt.getDistanceKm());
                    dto.setTaxiMinutes(tt.getTaxiMinutes());
                    dto.setBicycleMinutes(tt.getBicycleMinutes());
                    dto.setWalkingMinutes(tt.getWalkingMinutes());
                }
            }

            out.add(dto);
        }

        // Mantener el orden de entrada de ids
        List<ZoneCompareDTO> ordered = new ArrayList<>();
        for (Integer id : ids) {
            Optional<ZoneCompareDTO> match = out.stream().filter(d -> d.getIdZona().equals(id)).findFirst();
            match.ifPresent(ordered::add);
        }

        return ordered;
    }
}
