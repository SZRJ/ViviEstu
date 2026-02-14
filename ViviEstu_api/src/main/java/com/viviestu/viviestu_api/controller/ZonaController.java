package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.ZonaInsertRequest;
import com.viviestu.viviestu_api.dto.request.RecomendarRequest;
import com.viviestu.viviestu_api.dto.response.*;
import com.viviestu.viviestu_api.service.ZonaService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viviestu.viviestu_api.dto.request.FiltroRequest;
import com.viviestu.viviestu_api.dto.request.CompararRequest;

import java.util.List;

/// Controller que maneja las zonas del sistema
@RestController
@RequestMapping("/api/zonas")
@CrossOrigin(origins = "*")
public class ZonaController {

    @Autowired
    private ZonaService zonaService;

    /// POST /api/zonas (registrar nueva zona)
    @PostMapping
    public ResponseEntity<ApiResponse<ZonaResponse>> registrarZona(@RequestBody ZonaInsertRequest dto) {
        ZonaResponse zona = new ZonaResponse(
                zonaService.crearZona(dto).getIdZona(),
                dto.nombre(),
                dto.precioPromedio(),
                dto.seguridad(),
                dto.transporteDisponible(),
                false
        );
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Zona registrada correctamente", zona));
    }

    /// GET /api/zonas (listar todas)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ZonaResponse>>> listarTodas() {
        List<ZonaResponse> zonas = zonaService.listarZonas();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lista de zonas", zonas));
    }
    /// GET /api/zonas/{id} (Implementación de US05)
    @GetMapping("/{idZona}")
    public ResponseEntity<ApiResponse<ZonaResponse>> obtenerPorId(@PathVariable Integer idZona) {
        ZonaResponse zona = zonaService.obtenerZonaPorId(idZona);
        return ResponseEntity.ok(new ApiResponse<>(200, "Zona obtenida", zona));
    }

    /// PUT /api/zonas/recomendacion (marcar o desmarcar)
    @PutMapping("/recomendacion")
    public ResponseEntity<ApiResponse<ZonaResponse>> recomendar(@RequestBody RecomendarRequest dto) {
        ZonaResponse actualizada = zonaService.marcarRecomendacion(dto.idZona(), dto.recomendado());
        String mensaje = actualizada.recomendado()
                ? "Zona marcada como recomendada"
                : "Zona marcada como no recomendada";
        return ResponseEntity.ok(new ApiResponse<>(200, mensaje, actualizada));
    }

    /// GET /api/zonas/promedios-comentarios
    @GetMapping("/promedios-comentarios")
    public ResponseEntity<ApiResponse<List<ZonaComentarioResponse>>> promediosYComentarios() {
        List<ZonaComentarioResponse> lista = zonaService.obtenerPromediosYComentarios();
        return ResponseEntity.ok(new ApiResponse<>(200, "Promedios y comentarios por zona", lista));
    }

    /// GET /api/zonas/recomendadas?idUsuario=#
    @GetMapping("/recomendadas")
    public ResponseEntity<ApiResponse<List<ZonaReporteResponse>>> recomendadas(@RequestParam Long idUsuario) {
        List<ZonaReporteResponse> zonas = zonaService.listarZonasQueCumplen(idUsuario);
        return ResponseEntity.ok(new ApiResponse<>(200, "Zonas recomendadas según tus preferencias", zonas));
    }
    /// POST /api/zonas/filtrar (Implementación de RN-10 / US08)
    @PostMapping("/filtrar")
    public ResponseEntity<ApiResponse<List<ZonaResponse>>> filtrar(@RequestBody FiltroRequest req) {
        List<ZonaResponse> zonas = zonaService.filtrarZonas(req);
        return ResponseEntity.ok(new ApiResponse<>(200, "Zonas filtradas", zonas));
    }

    /// POST /api/zonas/comparar (Implementación de US09)
    @PostMapping("/comparar")
    public ResponseEntity<ApiResponse<List<ZonaResponse>>> compararZonas(@RequestBody CompararRequest req) {
        List<ZonaResponse> zonas = zonaService.listarZonasPorIds(req.zonaIds());
        return ResponseEntity.ok(new ApiResponse<>(200, "Zonas para comparar", zonas));
    }
}
