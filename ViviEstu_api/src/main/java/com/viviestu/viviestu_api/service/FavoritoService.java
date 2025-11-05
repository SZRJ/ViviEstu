package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.FavoritoRequest;
import com.viviestu.viviestu_api.dto.response.FavoritoResponse;
import com.viviestu.viviestu_api.model.Favorito;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.FavoritoRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// Servicio que gestiona el marcado y desmarcado de zonas favoritas
@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    /// Agrega una zona a favoritos
    public FavoritoResponse agregarFavorito(FavoritoRequest req) {
        if (req == null || req.idUsuario() == null || req.idZona() == null)
            throw new IllegalArgumentException("Datos incompletos para marcar favorito");

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(req.idUsuario());
        if (usuarioOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Zona> zonaOpt = zonaRepository.findById(req.idZona());
        if (zonaOpt.isEmpty()) throw new IllegalArgumentException("Zona no encontrada");

        if (favoritoRepository.existsByUsuarioIdUsuarioAndZonaIdZona(req.idUsuario(), req.idZona()))
            throw new IllegalArgumentException("La zona ya está marcada como favorita");

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuarioOpt.get());
        favorito.setZona(zonaOpt.get());

        Favorito guardado = favoritoRepository.save(favorito);

        return new FavoritoResponse(
                guardado.getIdFavorito(),
                guardado.getUsuario().getIdUsuario(),
                guardado.getZona().getIdZona(),
                guardado.getZona().getNombre(),
                guardado.getZona().getPrecioPromedio(),
                guardado.getZona().getSeguridad()
        );
    }

    /// Lista todas las zonas favoritas de un usuario
    public List<FavoritoResponse> listarFavoritos(Long idUsuario) {
        List<Favorito> favoritos = favoritoRepository.findByUsuarioIdUsuario(idUsuario);
        return favoritos.stream().map(f ->
                new FavoritoResponse(
                        f.getIdFavorito(),
                        f.getUsuario().getIdUsuario(),
                        f.getZona().getIdZona(),
                        f.getZona().getNombre(),
                        f.getZona().getPrecioPromedio(),
                        f.getZona().getSeguridad()
                )).collect(Collectors.toList());
    }

    /// Elimina una zona favorita
    public void eliminarFavorito(Long idUsuario, Integer idZona) {
        if (!favoritoRepository.existsByUsuarioIdUsuarioAndZonaIdZona(idUsuario, idZona))
            throw new IllegalArgumentException("El favorito no existe o ya fue eliminado");

        favoritoRepository.deleteByUsuarioIdUsuarioAndZonaIdZona(idUsuario, idZona);
    }
}
