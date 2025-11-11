package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks UsuarioService usuarioService;

    @Test
    void verificarCuenta_cuandoNoVerificada_cambiaAtrueYGuarda() {
        var u = new Usuario();
        u.setIdUsuario(10L);
        u.setVerificado(false);

        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        var actualizado = usuarioService.verificarCuenta(10L);

        assertTrue(actualizado.isVerificado());
        verify(usuarioRepository).save(u);
    }

    @Test
    void verificarCuenta_cuandoYaVerificada_lanzaExcepcion() {
        var u = new Usuario();
        u.setIdUsuario(11L);
        u.setVerificado(true);

        when(usuarioRepository.findById(11L)).thenReturn(Optional.of(u));

        var ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.verificarCuenta(11L));

        assertTrue(ex.getMessage().toLowerCase().contains("verificada"));
        verify(usuarioRepository, never()).save(any());
    }
}