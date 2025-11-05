package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenciaRepository extends JpaRepository<Preferencia, Long> {

    /// Lista las preferencias de un usuario específico
    List<Preferencia> findByUsuarioIdUsuario(Long idUsuario);
}
