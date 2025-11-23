package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.repository.FavoritosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritosService {

	private final FavoritosRepository repository;

	@Autowired
	public FavoritosService(FavoritosRepository repository) {
		this.repository = repository;
	}

	public List<Favoritos> getAll() {
		return repository.findAll();
	}

	public Optional<Favoritos> getById(Long id) {
		return repository.findById(id);
	}

	public Favoritos create(Favoritos fav) {
		return repository.save(fav);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}
