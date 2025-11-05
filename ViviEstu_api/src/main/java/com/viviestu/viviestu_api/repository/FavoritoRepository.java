package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    /// Verifica si un usuario ya marcó una zona como favorita
    boolean existsByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    /// Elimina una zona favorita específica
    void deleteByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    /// Lista todas las zonas favoritas de un usuario
    List<Favorito> findByUsuarioIdUsuario(Long idUsuario);

    /// Busca un favorito específico
    Optional<Favorito> findByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);
}
