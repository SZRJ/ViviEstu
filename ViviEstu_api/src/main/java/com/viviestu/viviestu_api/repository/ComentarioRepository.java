package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

/// Repositorio para gestionar comentarios
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    // Añade esta línea
    long countByUsuarioIdUsuario(Long idUsuario);

    /// Devuelve los comentarios de una zona ordenados por fecha descendente (más recientes primero)
    List<Comentario> findByZonaIdZonaOrderByFechaDesc(Integer idZona);

    /// Devuelve los comentarios con límite (para paginación)
    List<Comentario> findByZonaIdZonaOrderByFechaDesc(Integer idZona, Pageable pageable);
}
