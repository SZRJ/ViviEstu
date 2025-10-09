package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByZonaIdZonaOrderByFechaDesc(Integer idZona, Pageable pageable);
    List<Comentario> findByZonaIdZonaOrderByFechaDesc(Integer idZona);
}
