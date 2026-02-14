package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {
	// Extiende JpaRepository para operaciones CRUD persistentes
}
