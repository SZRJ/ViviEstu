package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    boolean existsByUsuarioIdUsuarioAndZonaIdZona(Integer usuarioId, Integer zonaId);

    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.zona.idZona = ?1")
    Double findPromedioPorZona(Integer idZona);

    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.zona.idZona = ?1")
    Long countByZonaId(Integer idZona);

    Calificacion findByUsuarioIdUsuarioAndZonaIdZona(Integer usuarioId, Integer zonaId);
}
