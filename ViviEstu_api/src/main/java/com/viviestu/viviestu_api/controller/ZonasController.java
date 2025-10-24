package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.RecomendarDTO;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.service.ZonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zonas")
public class ZonasController {
    @Autowired
    private ZonaService zonaService;

    @PutMapping("/recomendacion")
    public ResponseEntity<String> recomendar(@RequestBody RecomendarDTO dto) {
        if (dto.getIdZona() == null || dto.getRecomendado() == null) {
            return ResponseEntity.badRequest().body("idZona y recomendado son obligatorios.");
        }

        Zona existente = zonaService.listId(dto.getIdZona());
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una zona con el ID: " + dto.getIdZona());
        }

        // Cambiar SOLO el campo necesario
        existente.setRecomendado(dto.getRecomendado());
        zonaService.edit(existente);

        return ResponseEntity.ok("Zona " + dto.getIdZona() + " marcada como "
                + (dto.getRecomendado() ? "recomendada" : "no recomendada") + ".");
    }

}
