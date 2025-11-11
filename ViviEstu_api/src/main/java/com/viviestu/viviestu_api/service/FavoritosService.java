package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Favoritos;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.FavoritosRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoritosService {

	@Autowired
	private FavoritosRepository favoritosRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ZonaRepository zonaRepository;

	@Transactional
	public Favoritos agregarFavorito(Long usuarioId, Integer zonaId) {
		// validar usuario y zona
		Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
		if (u == null) return null;
		Zona z = zonaRepository.findById(zonaId).orElse(null);
		if (z == null) return null;

		// si ya existe, devolver existente
		if (favoritosRepository.existsByUsuarioIdUsuarioAndZonaIdZona(usuarioId, zonaId)) {
			// devolver el primer favorito que coincida
			List<Favoritos> list = favoritosRepository.findByUsuarioIdUsuario(usuarioId);
			for (Favoritos f : list) {
				if (f.getZona() != null && f.getZona().getIdZona().equals(zonaId)) return f;
			}
		}

		Favoritos fav = new Favoritos();
		fav.setUsuario(u);
		fav.setZona(z);
		fav.setFechaCreacion(LocalDateTime.now());
		return favoritosRepository.save(fav);
	}

	public List<Favoritos> listarFavoritos(Long usuarioId) {
		return favoritosRepository.findByUsuarioIdUsuario(usuarioId);
	}

	@Transactional
	public void eliminarFavorito(Long usuarioId, Integer zonaId) {
		favoritosRepository.deleteByUsuarioIdAndZonaId(usuarioId, zonaId);
	}

}
