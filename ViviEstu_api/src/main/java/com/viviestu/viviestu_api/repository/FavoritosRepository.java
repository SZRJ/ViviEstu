package com.viviestu.viviestu_api.repository;

import com.viviestu.viviestu_api.model.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {

	List<Favoritos> findByUsuarioIdUsuario(Long usuarioId);

	boolean existsByUsuarioIdUsuarioAndZonaIdZona(Long usuarioId, Integer zonaId);

	@Modifying
	@Query("DELETE FROM Favoritos f WHERE f.usuario.idUsuario = :usuarioId AND f.zona.idZona = :zonaId")
	void deleteByUsuarioIdAndZonaId(@Param("usuarioId") Long usuarioId, @Param("zonaId") Integer zonaId);
}
