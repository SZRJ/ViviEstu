package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Preferencias;
import com.viviestu.viviestu_api.repository.PreferenciasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenciasService {

    @Autowired
    private PreferenciasRepository preferenciasRepository;

    // Obtener todas las preferencias
    public List<Preferencias> listarPreferencias() {
        return preferenciasRepository.findAll();
    }

    // Obtener una preferencia por ID
    public ResponseEntity<Preferencias> obtenerPorId(Long id) {
        return preferenciasRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nueva preferencia
    public Preferencias crearPreferencia(Preferencias preferencias) {
        return preferenciasRepository.save(preferencias);
    }

    // Actualizar preferencia existente
    public ResponseEntity<Preferencias> actualizarPreferencias(Long id, Preferencias nuevasPreferencias) {
        return preferenciasRepository.findById(id)
                .map(existentes -> {
                    existentes.setUniversidad(nuevasPreferencias.getUniversidad());
                    existentes.setPresupuesto(nuevasPreferencias.getPresupuesto());
                    existentes.setTransporte(nuevasPreferencias.getTransporte());
                    existentes.setTiempoMax(nuevasPreferencias.getTiempoMax());
                    existentes.setSeguridad(nuevasPreferencias.getSeguridad());
                    Preferencias actualizadas = preferenciasRepository.save(existentes);
                    return ResponseEntity.ok(actualizadas);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar preferencia
    public ResponseEntity<?> eliminarPreferencia(Long id) {
        return preferenciasRepository.findById(id)
                .map(preferencia -> {
                    preferenciasRepository.delete(preferencia);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

