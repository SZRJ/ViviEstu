package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    // Verifica si ya existe el favorito
    boolean existsByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);

    // Eliminar favorito
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorito f WHERE f.usuario.idUsuario = :usuarioId AND f.zona.idZona = :zonaId")
    void deleteByUsuarioIdUsuarioAndZonaIdZona(@Param("usuarioId") Long usuarioId, @Param("zonaId") Integer zonaId);

    // >>> SOLUCIÓN NUCLEAR (JOIN FETCH) <<<
    // Esto trae al Usuario y a la Zona JUNTOS. Imposible que de error de carga perezosa.
    @Query("SELECT f FROM Favorito f JOIN FETCH f.usuario JOIN FETCH f.zona WHERE f.usuario.idUsuario = :idUsuario")
    List<Favorito> findByUsuarioIdUsuario(@Param("idUsuario") Long idUsuario);

    Optional<Favorito> findByUsuarioIdUsuarioAndZonaIdZona(Long idUsuario, Integer idZona);
}