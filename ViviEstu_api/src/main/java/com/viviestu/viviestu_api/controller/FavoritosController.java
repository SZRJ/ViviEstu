package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.FavoritoRequest;
import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.service.FavoritosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritosController {

	@Autowired
	private FavoritosService favoritosService;

	@PostMapping("/{usuarioId}")
	public ResponseEntity<Favoritos> agregarFavorito(@PathVariable("usuarioId") Long usuarioId,
													 @RequestBody FavoritoRequest request) {
		if (request == null || request.getZonaId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Favoritos creado = favoritosService.agregarFavorito(usuarioId, request.getZonaId());
		if (creado == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(HttpStatus.CREATED).body(creado);
	}

	@GetMapping("/{usuarioId}")
	public ResponseEntity<List<Favoritos>> listarFavoritos(@PathVariable("usuarioId") Long usuarioId) {
		List<Favoritos> list = favoritosService.listarFavoritos(usuarioId);
		return ResponseEntity.ok(list);
	}

	@DeleteMapping("/{usuarioId}/{zonaId}")
	public ResponseEntity<Void> eliminarFavorito(@PathVariable("usuarioId") Long usuarioId,
												 @PathVariable("zonaId") Integer zonaId) {
		favoritosService.eliminarFavorito(usuarioId, zonaId);
		return ResponseEntity.noContent().build();
	}

}
