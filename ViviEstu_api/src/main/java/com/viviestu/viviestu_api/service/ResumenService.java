package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.ResumenResponse;
import com.viviestu.viviestu_api.model.Favorito;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.ComentarioRepository;
import com.viviestu.viviestu_api.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <--- 1. IMPORTAR

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumenService {

    @Autowired
    private FavoritoRepository favoritoRepository;
    @Autowired
    private CalificacionRepository calificacionRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;

    // 2. AGREGAR @Transactional AQUÍ
    // Esto es necesario porque favoritoRepository usa JOIN FETCH
    @Transactional(readOnly = true)
    public ResumenResponse obtenerResumen(Long idUsuario) {

        // Obtener listas y conteos
        List<Favorito> favoritos = favoritoRepository.findByUsuarioIdUsuario(idUsuario);
        long totalCalificaciones = calificacionRepository.countByUsuarioIdUsuario(idUsuario);
        long totalComentarios = comentarioRepository.countByUsuarioIdUsuario(idUsuario);

        List<String> nombresZonas = favoritos.stream()
                .map(f -> f.getZona().getNombre())
                .collect(Collectors.toList());

        return new ResumenResponse(
                favoritos.size(),
                (int) totalCalificaciones,
                (int) totalComentarios,
                nombresZonas
        );
    }
}