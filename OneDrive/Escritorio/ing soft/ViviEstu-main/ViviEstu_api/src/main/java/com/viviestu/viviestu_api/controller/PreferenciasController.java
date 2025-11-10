package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.model.Preferencia;
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
    public List<Preferencia> listarPreferencias() {
        return preferenciasService.listarPreferencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preferencia> obtenerPorId(@PathVariable Long id) {
        return preferenciasService.obtenerPorId(id);
    }

    @PostMapping
    public Preferencia crearPreferencia(@RequestBody Preferencia preferencias) {
        return preferenciasService.crearPreferencia(preferencias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Preferencia> actualizarPreferencias(@PathVariable Long id, @RequestBody Preferencia prefs) {
        return preferenciasService.actualizarPreferencias(id, prefs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPreferencia(@PathVariable Long id) {
        return preferenciasService.eliminarPreferencia(id);
    }

}
