package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {
    Optional<Recomendacion> findByIdUsuarioAndIdZona(Integer idUsuario, Integer idZona);
    int countByIdZona(Integer idZona);
}
