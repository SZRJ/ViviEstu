// Archivo: service/ResumenService.java
// (ARCHIVO NUEVO)
package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.response.ResumenResponse;
import com.viviestu.viviestu_api.model.Favorito;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.ComentarioRepository;
import com.viviestu.viviestu_api.repository.FavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public ResumenResponse obtenerResumen(Long idUsuario) {
        List<Favorito> favoritos = favoritoRepository.findByUsuarioIdUsuario(idUsuario);

        // NOTA: Estas son simplificaciones. Deberías filtrar por idUsuario en tus repositorios.
        long totalCalificaciones = calificacionRepository.count(); // (Mejorar para contar por usuario)
        long totalComentarios = comentarioRepository.count(); // (Mejorar para contar por usuario)

        // RN-20: Validar si hay acciones
        if (favoritos.isEmpty() && totalCalificaciones == 0 && totalComentarios == 0) {
            throw new IllegalArgumentException("El usuario no tiene acciones registradas. ¡Empieza a explorar!");
        }

        List<String> nombresZonas = favoritos.stream()
                .map(f -> f.getZona().getNombre())
                .collect(Collectors.toList());

        return new ResumenResponse(
                favoritos.size(),
                (int) totalCalificaciones, // Simplificación
                (int) totalComentarios, // Simplificación
                nombresZonas
        );
    }
}