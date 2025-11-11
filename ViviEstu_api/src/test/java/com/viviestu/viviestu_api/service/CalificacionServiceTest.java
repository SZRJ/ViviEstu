package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.CalificacionRequest;
import com.viviestu.viviestu_api.model.Calificacion;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.model.Zona;
import com.viviestu.viviestu_api.repository.CalificacionRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import com.viviestu.viviestu_api.repository.ZonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock private CalificacionRepository calificacionRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ZonaRepository zonaRepository;
    @InjectMocks private CalificacionService calificacionService;

    @Test
    void testRegistrarCalificacion_Fail_PuntuacionInvalida_RN13() {
        // Arrange
        CalificacionRequest req = new CalificacionRequest(1L, 6); // Puntuación > 5

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> calificacionService.registrarCalificacion(1, req)
        );
        assertEquals("La puntuación debe estar entre 1 y 5", ex.getMessage());
    }

    @Test
    void testRegistrarCalificacion_ActualizaExistente_RN13() {
        // Arrange
        Integer idZona = 1;
        Long idUsuario = 1L;
        CalificacionRequest req = new CalificacionRequest(idUsuario, 4);

        // Crear entidades simuladas
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);

        Zona zona = new Zona();
        zona.setIdZona(idZona);

        // Configurar mocks
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(zonaRepository.findById(idZona)).thenReturn(Optional.of(zona));

        // Simular que ya existe una calificación
        Calificacion calificacionExistente = new Calificacion();
        calificacionExistente.setUsuario(usuario);
        calificacionExistente.setZona(zona);
        calificacionExistente.setPuntuacion(2); // Puntuación anterior

        when(calificacionRepository.findByUsuarioIdUsuarioAndZonaIdZona(idUsuario, idZona))
                .thenReturn(calificacionExistente);

        // Mockear save
        when(calificacionRepository.save(any(Calificacion.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        calificacionService.registrarCalificacion(idZona, req);

        // Assert
        assertEquals(4, calificacionExistente.getPuntuacion());
        verify(calificacionRepository, times(1)).save(calificacionExistente);
    }
}
