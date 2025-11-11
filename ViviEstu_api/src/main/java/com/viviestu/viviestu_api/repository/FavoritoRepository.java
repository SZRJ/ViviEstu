package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Favorito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    /// Verifica si un usuario ya marcó una zona como favorita
    boolean existsByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    /// Elimina una zona favorita específica
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorito f WHERE f.usuario.idUsuario = :usuarioId AND f.zona.idZona = :zonaId")
    void deleteByUsuarioIdUsuarioAndZonaIdZona(@Param("usuarioId") Long usuarioId, @Param("zonaId") Integer zonaId);

    /// Lista todas las zonas favoritas de un usuario
    List<Favorito> findByUsuarioIdUsuario(Long idUsuario);

    /// Busca un favorito específico
    Optional<Favorito> findByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);
}
