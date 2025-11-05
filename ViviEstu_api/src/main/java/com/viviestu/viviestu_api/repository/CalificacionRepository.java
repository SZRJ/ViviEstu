package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    /// Verifica si el usuario ya calificó la zona
    boolean existsByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    /// Busca una calificación existente por usuario y zona
    Calificacion findByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    /// Calcula el promedio de puntuaciones por zona
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.zona.idZona = ?1")
    Double findPromedioPorZona(Integer idZona);
}
