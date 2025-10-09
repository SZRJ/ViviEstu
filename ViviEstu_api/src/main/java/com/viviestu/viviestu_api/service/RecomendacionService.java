package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.NotificacionDTO;
import com.viviestu.viviestu_api.dto.RecomendarRequest;
import com.viviestu.viviestu_api.model.Recomendacion;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.RecomendacionRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ZonaRepository zonaRepository;
    @Autowired
    private RecomendacionRepository recomendacionRepository;

    public List<NotificacionDTO> generarRecomendacionesParaUsuario(Integer usuarioId, int topN) {
        Optional<Usuario> uOpt = usuarioRepository.findById(usuarioId);
        if (uOpt.isEmpty()) return Collections.emptyList();

        Usuario u = uOpt.get();
        if (u.getUniversidad() == null || u.getUniversidad().trim().isEmpty() || u.getPresupuesto() == null) {
            return Collections.emptyList();
        }

        Double presupuesto = u.getPresupuesto();
        String transporte = u.getTransporte() == null ? "" : u.getTransporte().toLowerCase().trim();

        List<Zona> porPrecio = zonaRepository.findByPrecioPromedioLessThanEqual(presupuesto);

        if (porPrecio.isEmpty()) porPrecio = zonaRepository.findAll();

        List<NotificacionDTO> result = new ArrayList<>();
        for (Zona z : porPrecio) {
            NotificacionDTO n = new NotificacionDTO();
            n.setZonaId(z.getIdZona());
            n.setZonaNombre(z.getNombre());
            n.setPrecioPromedio(z.getPrecioPromedio());

            String motivo = "Ajuste a tu presupuesto";
            if (!transporte.isEmpty()
                    && z.getTransporteDisponible() != null
                    && z.getTransporteDisponible().toLowerCase().contains(transporte)) {
                motivo = "Buena conexión con tu medio de transporte: " + transporte;
            } else if (z.getSeguridad() != null && z.getSeguridad().toLowerCase().contains("alta")) {
                motivo = "Zona con seguridad alta";
            }
            n.setMotivo(motivo);
            result.add(n);
        }

        List<NotificacionDTO> sorted = result.stream()
                .sorted(Comparator
                        .comparing((NotificacionDTO nd) -> !nd.getMotivo().toLowerCase().contains("conexión"))
                        .thenComparing(NotificacionDTO::getPrecioPromedio))
                .collect(Collectors.toList());

        if (topN <= 0) topN = 5;
        return sorted.stream().limit(topN).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> toggle(Integer idZona, RecomendarRequest req) {
        var existente = recomendacionRepository.findByIdUsuarioAndIdZona(req.getIdUsuario(), idZona);

        boolean recomendado;
        if (existente.isPresent()) {
            recomendacionRepository.delete(existente.get()); // quitar
            recomendado = false;
        } else {
            recomendacionRepository.save(new Recomendacion(req.getIdUsuario(), idZona)); // agregar
            recomendado = true;
        }

        int total = recomendacionRepository.countByIdZona(idZona);

        Map<String, Object> resp = new HashMap<>();
        resp.put("zonaId", idZona);
        resp.put("usuarioId", req.getIdUsuario());
        resp.put("recomendado", recomendado);        // true = agregado, false = retirado
        resp.put("totalRecomendaciones", total);     // contador actualizado
        return resp;
    }

    @Transactional(readOnly = true)
    public boolean isRecomendadaPorUsuario(Integer idZona, Integer idUsuario) {
        return recomendacionRepository.findByIdUsuarioAndIdZona(idUsuario, idZona).isPresent();
    }

    @Transactional(readOnly = true)
    public int contarPorZona(Integer idZona) {
        return recomendacionRepository.countByIdZona(idZona);
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> listarPorUsuario(Integer idUsuario) {
        // si luego quieres performance/orden, crea métodos específicos en el repo
        return recomendacionRepository.findAll().stream()
                .filter(r -> Objects.equals(r.getIdUsuario(), idUsuario))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> listarPorZona(Integer idZona) {
        return recomendacionRepository.findAll().stream()
                .filter(r -> Objects.equals(r.getIdZona(), idZona))
                .toList();
    }

    @Transactional
    public void eliminarPorId(Integer idRecomendacion) {
        recomendacionRepository.deleteById(idRecomendacion);
    }

    @Transactional
    public boolean crearSiNoExiste(Integer idZona, Integer idUsuario) {
        var existente = recomendacionRepository.findByIdUsuarioAndIdZona(idUsuario, idZona);
        if (existente.isPresent()) return false;
        recomendacionRepository.save(new Recomendacion(idUsuario, idZona));
        return true;
    }

    @Transactional
    public boolean eliminarSiExiste(Integer idZona, Integer idUsuario) {
        var existente = recomendacionRepository.findByIdUsuarioAndIdZona(idUsuario, idZona);
        if (existente.isEmpty()) return false;
        recomendacionRepository.delete(existente.get());
        return true;
    }
}