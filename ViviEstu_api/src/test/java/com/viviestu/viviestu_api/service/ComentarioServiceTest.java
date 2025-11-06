package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.ComentarioRequest;
import com.viviestu.viviestu_api.dto.response.ComentarioResponse;
import com.viviestu.viviestu_api.model.Comentario;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.ComentarioRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock private ComentarioRepository comentarioRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ZonaRepository zonaRepository;
    @InjectMocks private ComentarioService comentarioService;

    @Test
    void testAgregarComentario_Falla_SiComentarioVacio_RN14() {
        // Arrange
        ComentarioRequest reqVacio = new ComentarioRequest(1L, "   "); // Comentario vacío
        ComentarioRequest reqNull = new ComentarioRequest(1L, null); // Comentario null

        // Act & Assert
        IllegalArgumentException exVacio = assertThrows(
                IllegalArgumentException.class,
                () -> comentarioService.agregarComentario(1, reqVacio)
        );
        IllegalArgumentException exNull = assertThrows(
                IllegalArgumentException.class,
                () -> comentarioService.agregarComentario(1, reqNull)
        );

        assertEquals("Datos insuficientes para agregar comentario", exVacio.getMessage());
        assertEquals("Datos insuficientes para agregar comentario", exNull.getMessage());
    }

    @Test
    void testAgregarComentario_Exitoso_RN14() {
        // Arrange
        Integer idZona = 1;
        Long idUsuario = 1L;
        ComentarioRequest req = new ComentarioRequest(idUsuario, "¡Gran zona!");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreUsuario("testuser"); // RN-14: El DTO debe incluir el nombre

        Zona zona = new Zona();
        zona.setIdZona(idZona);

        // Mockear las búsquedas
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(zonaRepository.findById(idZona)).thenReturn(Optional.of(zona));

        // Mockear el guardado
        // Cuando se guarde, simulamos la respuesta de la BD
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(invocation -> {
            Comentario c = invocation.getArgument(0);
            c.setIdExperiencia(99); // Simular ID de BD
            c.setFecha(LocalDateTime.now()); // Simular fecha de BD
            return c;
        });

        // Act
        ComentarioResponse respuesta = comentarioService.agregarComentario(idZona, req);

        // Assert
        assertNotNull(respuesta);
        assertEquals(99, respuesta.idExperiencia());
        assertEquals(idUsuario, respuesta.idUsuario());
        assertEquals("testuser", respuesta.nombreUsuario()); // RN-14 verificado
        assertEquals("¡Gran zona!", respuesta.comentario());
        verify(comentarioRepository, times(1)).save(any(Comentario.class)); // Verificar que se guardó
    }
}