package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.RecomendarDTO;
import com.viviestu.viviestu_api.dto.ZonaComentarioDTO;
import com.viviestu.viviestu_api.dto.ZonaInsertDTO;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.service.ZonaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/promedios-comentarios")
    public ResponseEntity<List<ZonaComentarioDTO>> listarPromediosYComentarios() {
        List<ZonaComentarioDTO> lista = zonaService.obtenerPromediosYComentarios();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/insertar")
    public ResponseEntity<String> insertar(@RequestBody ZonaInsertDTO dto) {
        ModelMapper m = new ModelMapper();
        Zona zona = m.map(dto, Zona.class);

        // Validar precio
        if (zona.getPrecioPromedio() == null || zona.getPrecioPromedio() <= 0) {
            return ResponseEntity.badRequest().body("El precio promedio debe ser mayor que 0.");
        }

        zonaService.insert(zona);
        return ResponseEntity.ok("Zona registrada correctamente.");
    }


}
