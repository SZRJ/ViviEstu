package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.service.FavoritosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "*")
public class FavoritosController {

	/*private final FavoritosService service;

	@Autowired
	public FavoritosController(FavoritosService service) {
		this.service = service;
	}

	@GetMapping
	public List<Favoritos> listAll() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Favoritos> getById(@PathVariable Long id) {
		return service.getById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Favoritos> create(@RequestBody Favoritos fav) {
		Favoritos created = service.create(fav);
		return ResponseEntity.created(URI.create("/api/favoritos/" + created.getId()))
				.body(created);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (service.getById(id).isPresent()) {
			service.delete(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}*/
}
