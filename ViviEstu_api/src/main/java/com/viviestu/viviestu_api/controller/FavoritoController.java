package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.FavoritoRequest;
import com.viviestu.viviestu_api.dto.response.FavoritoResponse;
import com.viviestu.viviestu_api.service.FavoritoService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controller para gestionar favoritos de los usuarios
@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    /// POST /api/favoritos → agrega un favorito
    @PostMapping
    public ResponseEntity<ApiResponse<FavoritoResponse>> agregar(@RequestBody FavoritoRequest req) {
        FavoritoResponse favorito = favoritoService.agregarFavorito(req);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(201, "Zona marcada como favorita", favorito));
    }

    /// GET /api/favoritos/{idUsuario} → lista favoritos de un usuario
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<List<FavoritoResponse>>> listar(@PathVariable Long idUsuario) {
        List<FavoritoResponse> lista = favoritoService.listarFavoritos(idUsuario);
        return ResponseEntity.ok(new ApiResponse<>(200, "Favoritos obtenidos correctamente", lista));
    }

    /// DELETE /api/favoritos/{idUsuario}/{idZona} → elimina un favorito
    @DeleteMapping("/{idUsuario}/{idZona}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long idUsuario, @PathVariable Integer idZona) {
        favoritoService.eliminarFavorito(idUsuario, idZona);
        return ResponseEntity.ok(new ApiResponse<>(200, "Zona eliminada de favoritos", null));
    }
}
