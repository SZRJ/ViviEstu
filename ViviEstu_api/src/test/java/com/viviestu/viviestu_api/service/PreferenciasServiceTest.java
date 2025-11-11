package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreferenciaServiceTest {

    @Mock PreferenciaRepository preferenciaRepository;
    @InjectMocks PreferenciaService preferenciaService;

    @Test
    void eliminar_cuandoExiste_llamaDeleteById() {
        when(preferenciaRepository.existsById(5L)).thenReturn(true);

        preferenciaService.eliminar(5L);

        verify(preferenciaRepository).deleteById(5L);
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(preferenciaRepository.existsById(99L)).thenReturn(false);

        var ex = assertThrows(IllegalArgumentException.class,
                () -> preferenciaService.eliminar(99L));

        assertTrue(ex.getMessage().toLowerCase().contains("no existe"));
        verify(preferenciaRepository, never()).deleteById(any());
    }
}