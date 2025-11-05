package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.CalificacionRequest;
import com.viviestu.viviestu_api.dto.response.CalificacionResponse;
import com.viviestu.viviestu_api.model.Calificacion;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// Servicio para registrar y gestionar calificaciones
@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    /**
     * Registra una calificación (1-5). Si ya existe una del mismo usuario en la zona, se actualiza.
     */
    public CalificacionResponse registrarCalificacion(Integer idZona, CalificacionRequest req) {
        if (req == null || req.idUsuario() == null || req.puntuacion() == null)
            throw new IllegalArgumentException("Datos incompletos para registrar calificación");

        if (req.puntuacion() < 1 || req.puntuacion() > 5)
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");

        Optional<Usuario> uOpt = usuarioRepository.findById(req.idUsuario());
        if (uOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Zona> zOpt = zonaRepository.findById(idZona);
        if (zOpt.isEmpty()) throw new IllegalArgumentException("Zona no encontrada");

        Calificacion existente = calificacionRepository.findByUsuarioIdUsuarioAndZonaIdZona(req.idUsuario(), idZona);

        Calificacion calificacion;
        if (existente != null) {
            existente.setPuntuacion(req.puntuacion());
            calificacion = calificacionRepository.save(existente);
        } else {
            calificacion = new Calificacion();
            calificacion.setUsuario(uOpt.get());
            calificacion.setZona(zOpt.get());
            calificacion.setPuntuacion(req.puntuacion());
            calificacion = calificacionRepository.save(calificacion);
        }

        return new CalificacionResponse(
                calificacion.getIdCalificacion(),
                calificacion.getUsuario().getIdUsuario(),
                calificacion.getZona().getIdZona(),
                calificacion.getPuntuacion()
        );
    }

    /// Devuelve el promedio de calificaciones de una zona
    public Double obtenerPromedioPorZona(Integer idZona) {
        Double promedio = calificacionRepository.findPromedioPorZona(idZona);
        return promedio != null ? promedio : 0.0;
    }
}
