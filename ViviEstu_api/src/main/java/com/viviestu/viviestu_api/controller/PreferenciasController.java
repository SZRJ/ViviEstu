package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.model.Preferencias;
import com.viviestu.viviestu_api.service.PreferenciasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferencias")
@CrossOrigin(origins = "*") // permite llamadas desde frontend
public class PreferenciasController {

    @Autowired
    private PreferenciasService preferenciasService;

    @GetMapping
    public List<Preferencias> listarPreferencias() {
        return preferenciasService.listarPreferencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preferencias> obtenerPorId(@PathVariable Long id) {
        return preferenciasService.obtenerPorId(id);
    }

    @PostMapping
    public Preferencias crearPreferencia(@RequestBody Preferencias preferencias) {
        return preferenciasService.crearPreferencia(preferencias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Preferencias> actualizarPreferencias(@PathVariable Long id, @RequestBody Preferencias prefs) {
        return preferenciasService.actualizarPreferencias(id, prefs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPreferencia(@PathVariable Long id) {
        return preferenciasService.eliminarPreferencia(id);
    }

}
