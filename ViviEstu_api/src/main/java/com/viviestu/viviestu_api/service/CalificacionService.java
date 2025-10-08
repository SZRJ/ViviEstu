package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Calificacion;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    /**
     * Registra una calificación (1-5). Si el usuario ya calificó la zona, actualiza la puntuación.
     */
    public Calificacion registrarCalificacion(Integer zonaId, Integer usuarioId, Integer puntuacion) throws IllegalArgumentException {
        if (puntuacion == null || puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("Puntuación inválida. Debe ser entre 1 y 5.");
        }
        Optional<Usuario> uOpt = usuarioRepository.findById(usuarioId);
        if (!uOpt.isPresent()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Zona> zOpt = zonaRepository.findById(zonaId);
        if (!zOpt.isPresent()) throw new IllegalArgumentException("Zona no encontrada");

        // Si ya existe calificación del usuario -> actualizar
        Calificacion existente = calificacionRepository.findByUsuarioIdUsuarioAndZonaIdZona(usuarioId, zonaId);
        if (existente != null) {
            existente.setPuntuacion(puntuacion);
            return calificacionRepository.save(existente);
        } else {
            Calificacion c = new Calificacion();
            c.setUsuario(uOpt.get());
            c.setZona(zOpt.get());
            c.setPuntuacion(puntuacion);
            return calificacionRepository.save(c);
        }
    }

    public Double obtenerPromedioZona(Integer zonaId) {
        Double promedio = calificacionRepository.findPromedioPorZona(zonaId);
        if (promedio == null) return 0.0;
        return promedio;
    }
}
