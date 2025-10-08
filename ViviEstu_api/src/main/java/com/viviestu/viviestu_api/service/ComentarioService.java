package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Comentario;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ComentarioRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    public Comentario agregarComentario(Integer zonaId, Integer usuarioId, String texto) throws IllegalArgumentException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }
        Optional<Usuario> uOpt = usuarioRepository.findById(usuarioId);
        if (!uOpt.isPresent()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Zona> zOpt = zonaRepository.findById(zonaId);
        if (!zOpt.isPresent()) throw new IllegalArgumentException("Zona no encontrada");

        Comentario c = new Comentario();
        c.setUsuario(uOpt.get());
        c.setZona(zOpt.get());
        c.setComentario(texto.trim());
        c.setFecha(LocalDateTime.now());

        return comentarioRepository.save(c);
    }

    public List<Comentario> listarComentariosDeZona(Integer zonaId, int max) {
        // si max <= 0 devolvemos todos
        if (max <= 0) {
            return comentarioRepository.findByZonaIdZonaOrderByFechaDesc(zonaId);
        } else {
            return comentarioRepository.findByZonaIdZonaOrderByFechaDesc(zonaId, org.springframework.data.domain.PageRequest.of(0, max));
        }
    }
}
