package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.FavoritoRequest;
import com.viviestu.viviestu_api.dto.response.FavoritoResponse;
import com.viviestu.viviestu_api.service.FavoritoService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // AHORA: @PostMapping("/{usuarioId}")
    @PostMapping("/{usuarioId}")
    public ResponseEntity<ApiResponse<FavoritoResponse>> agregar(
            @PathVariable("usuarioId") Long usuarioId, // 🔑 Nuevo parámetro de la URL
            @RequestBody FavoritoRequest request) {

        // Lógica de Validación (si el ID de la zona no viene en el cuerpo)
        if (request == null || request.idZona() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "El ID de la Zona es obligatorio.", null));
        }

        // 🔑 Llamar al servicio con ambos IDs separados
        FavoritoResponse favorito = favoritoService.agregarFavorito(usuarioId, request.idZona());

        if (favorito == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "Usuario o Zona no encontrados.", null));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
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
