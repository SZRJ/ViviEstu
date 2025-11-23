package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    // Comprueba si un usuario ya calificó una zona
    boolean existsByUsuario_IdUsuarioAndZona_IdZona(Long usuarioId, Integer zonaId);

    // Busca una calificación existente por usuario y zona
    Calificacion findByUsuario_IdUsuarioAndZona_IdZona(Long usuarioId, Integer zonaId);
    // Compatibilidad con nombres usados en tests y otros servicios
    Calificacion findByUsuarioIdUsuarioAndZonaIdZona(Long usuarioId, Integer zonaId);

    // Promedio y cuenta por zona
    @Query("SELECT AVG(c.puntuacion) FROM Calificacion c WHERE c.zona.idZona = ?1")
    Double findPromedioPorZona(Integer idZona);

    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.zona.idZona = ?1")
    Long countByZonaId(Integer idZona);

    // Cuenta calificaciones por usuario (consistente con otros repositorios del proyecto)
    long countByUsuarioIdUsuario(Long idUsuario);
}
