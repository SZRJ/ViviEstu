package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.ComentarioRequest;
import com.viviestu.viviestu_api.dto.response.ComentarioResponse;
import com.viviestu.viviestu_api.model.Comentario;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ComentarioRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/// Servicio de lógica de negocio para los comentarios
@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    /// Agregar comentario a una zona
    public ComentarioResponse agregarComentario(Integer idZona, ComentarioRequest req) {
        if (req == null || req.idUsuario() == null || req.comentario() == null || req.comentario().trim().isEmpty()) {
            throw new IllegalArgumentException("Datos insuficientes para agregar comentario");
        }

        Optional<Usuario> uOpt = usuarioRepository.findById(req.idUsuario());
        if (uOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Zona> zOpt = zonaRepository.findById(idZona);
        if (zOpt.isEmpty()) throw new IllegalArgumentException("Zona no encontrada");

        Comentario c = new Comentario();
        c.setUsuario(uOpt.get());
        c.setZona(zOpt.get());
        c.setComentario(req.comentario().trim());
        c.setFecha(LocalDateTime.now());

        Comentario guardado = comentarioRepository.save(c);
        return new ComentarioResponse(
                guardado.getIdExperiencia(),
                guardado.getUsuario().getIdUsuario(),
                guardado.getUsuario().getNombreUsuario(),
                guardado.getZona().getIdZona(),
                guardado.getComentario(),
                guardado.getFecha()
        );
    }

    /// Listar comentarios de una zona (máximo opcional)
    public List<ComentarioResponse> listarPorZona(Integer idZona, Integer max) {
        List<Comentario> lista;
        if (max == null || max <= 0)
            lista = comentarioRepository.findByZonaIdZonaOrderByFechaDesc(idZona);
        else
            lista = comentarioRepository.findByZonaIdZonaOrderByFechaDesc(idZona, PageRequest.of(0, max));

        return lista.stream()
                .map(c -> new ComentarioResponse(
                        c.getIdExperiencia(),
                        c.getUsuario().getIdUsuario(),
                        c.getUsuario().getNombreUsuario(),
                        c.getZona().getIdZona(),
                        c.getComentario(),
                        c.getFecha()
                ))
                .collect(Collectors.toList());
    }
}
