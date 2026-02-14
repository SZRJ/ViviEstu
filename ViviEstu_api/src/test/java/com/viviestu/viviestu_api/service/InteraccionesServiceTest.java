package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.CalificacionRequest;
import com.viviestu.viviestu_api.dto.request.ComentarioRequest;
import com.viviestu.viviestu_api.dto.response.ComentarioResponse;
import com.viviestu.viviestu_api.dto.response.FavoritoResponse;
import com.viviestu.viviestu_api.model.*;
import com.viviestu.viviestu_api.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable; // Importante

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InteraccionesServiceTest {

    @Mock private FavoritoRepository favoritoRepository;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private ComentarioRepository comentarioRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ZonaRepository zonaRepository;

    @InjectMocks private FavoritoService favoritoService;
    @InjectMocks private CalificacionService calificacionService;
    @InjectMocks private ComentarioService comentarioService;

    // US10: Favoritos
    @Test
    void testAgregarFavorito_US10() {
        Usuario u = new Usuario(); u.setIdUsuario(1L);
        Zona z = new Zona(); z.setIdZona(10); z.setNombre("Zona Fav");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(zonaRepository.findById(10)).thenReturn(Optional.of(z));
        when(favoritoRepository.save(any(Favorito.class))).thenAnswer(i -> {
            Favorito f = i.getArgument(0); f.setIdFavorito(1); return f;
        });

        FavoritoResponse res = favoritoService.agregarFavorito(1L, 10);
        assertNotNull(res);
        assertEquals("Zona Fav", res.nombreZona());
    }

    // US13: Calificar
    @Test
    void testRegistrarCalificacion_US13() {
        CalificacionRequest req = new CalificacionRequest(1L, 5);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(zonaRepository.findById(1)).thenReturn(Optional.of(new Zona()));
        when(calificacionRepository.save(any(Calificacion.class))).thenAnswer(i -> i.getArgument(0));

        var res = calificacionService.registrarCalificacion(1, req);
        assertEquals(5, res.puntuacion());
    }

    // US14: Comentar
    @Test
    void testAgregarComentario_US14() {
        ComentarioRequest req = new ComentarioRequest(1L, "Excelente zona");
        Usuario u = new Usuario(); u.setIdUsuario(1L); u.setNombreUsuario("Pepe");
        Zona z = new Zona(); z.setIdZona(1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(zonaRepository.findById(1)).thenReturn(Optional.of(z));
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(i -> {
            Comentario c = i.getArgument(0); c.setIdExperiencia(1); c.setFecha(LocalDateTime.now()); return c;
        });

        ComentarioResponse res = comentarioService.agregarComentario(1, req);
        assertEquals("Excelente zona", res.comentario());
    }

    // --- US16: VER RESEÑAS (CORREGIDO) ---
    @Test
    void testListarComentarios_US16() {
        Comentario c = new Comentario();
        c.setComentario("Info útil");
        c.setUsuario(new Usuario());
        c.setZona(new Zona());

        // CORRECCIÓN: Mockeamos el método que recibe Pageable, porque el servicio usa paginación
        when(comentarioRepository.findByZonaIdZonaOrderByFechaDesc(anyInt(), any(Pageable.class)))
                .thenReturn(List.of(c));

        List<ComentarioResponse> lista = comentarioService.listarPorZona(1, 10);

        assertEquals(1, lista.size());
        assertEquals("Info útil", lista.get(0).comentario());
    }
}