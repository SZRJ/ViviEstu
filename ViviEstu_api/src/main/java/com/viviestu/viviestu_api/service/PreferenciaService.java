package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.PreferenciaRequest;
import com.viviestu.viviestu_api.dto.response.PreferenciaResponse;
import com.viviestu.viviestu_api.model.Preferencia;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// Servicio encargado de la gestión de preferencias del usuario
@Service
public class PreferenciaService {

    @Autowired
    private PreferenciaRepository preferenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /// Crea una nueva preferencia para el usuario
    public PreferenciaResponse crear(PreferenciaRequest req) {
        if (req.idUsuario() == null)
            throw new IllegalArgumentException("Debe especificarse el id del usuario.");

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(req.idUsuario());
        if (usuarioOpt.isEmpty())
            throw new IllegalArgumentException("Usuario no encontrado.");

        Preferencia p = new Preferencia();
        p.setUsuario(usuarioOpt.get());
        p.setUniversidad(req.universidad());
        p.setPresupuesto(req.presupuesto());
        p.setTransporte(req.transporte());
        p.setTiempoMax(req.tiempoMax());
        p.setSeguridad(req.seguridad());

        Preferencia guardada = preferenciaRepository.save(p);

        return new PreferenciaResponse(
                guardada.getIdPreferencia(),
                guardada.getUsuario().getIdUsuario(),
                guardada.getUniversidad(),
                guardada.getPresupuesto(),
                guardada.getTransporte(),
                guardada.getTiempoMax(),
                guardada.getSeguridad()
        );
    }

    /// Lista todas las preferencias de un usuario
    public List<PreferenciaResponse> listarPorUsuario(Long idUsuario) {
        List<Preferencia> lista = preferenciaRepository.findByUsuarioIdUsuario(idUsuario);
        return lista.stream().map(p ->
                new PreferenciaResponse(
                        p.getIdPreferencia(),
                        p.getUsuario().getIdUsuario(),
                        p.getUniversidad(),
                        p.getPresupuesto(),
                        p.getTransporte(),
                        p.getTiempoMax(),
                        p.getSeguridad()
                )).collect(Collectors.toList());
    }

    /// Actualiza una preferencia existente
    public PreferenciaResponse actualizar(Long idPreferencia, PreferenciaRequest req) {
        Preferencia pref = preferenciaRepository.findById(idPreferencia)
                .orElseThrow(() -> new IllegalArgumentException("Preferencia no encontrada."));

        pref.setUniversidad(req.universidad());
        pref.setPresupuesto(req.presupuesto());
        pref.setTransporte(req.transporte());
        pref.setTiempoMax(req.tiempoMax());
        pref.setSeguridad(req.seguridad());

        Preferencia actualizada = preferenciaRepository.save(pref);

        return new PreferenciaResponse(
                actualizada.getIdPreferencia(),
                actualizada.getUsuario().getIdUsuario(),
                actualizada.getUniversidad(),
                actualizada.getPresupuesto(),
                actualizada.getTransporte(),
                actualizada.getTiempoMax(),
                actualizada.getSeguridad()
        );
    }

    /// Elimina una preferencia por ID
    public void eliminar(Long idPreferencia) {
        if (!preferenciaRepository.existsById(idPreferencia))
            throw new IllegalArgumentException("La preferencia no existe o ya fue eliminada.");

        preferenciaRepository.deleteById(idPreferencia);
    }
}
