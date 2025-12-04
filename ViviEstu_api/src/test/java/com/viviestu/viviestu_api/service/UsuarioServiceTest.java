package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.LoginRequest;
import com.viviestu.viviestu_api.dto.request.PerfilRequest;
import com.viviestu.viviestu_api.dto.request.RegistroRequest;
import com.viviestu.viviestu_api.model.Preferencia;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private PreferenciaRepository preferenciaRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UsuarioService usuarioService;

    // --- US01: REGISTRO E INICIO DE SESIÓN ---
    @Test
    void testCrearUsuario_US01() {
        RegistroRequest req = new RegistroRequest("Pepe", "pepe123", LocalDate.now(), "pepe@mail.com", "123456");
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario res = usuarioService.crearUsuario(req);
        assertNotNull(res);
        assertEquals("pepe@mail.com", res.getCorreo());
    }

    @Test
    void testLogin_US01() {
        // Nota: El login real usa AuthenticationManager, pero aquí probamos la lógica de validación del servicio si existiera
        // o simulamos la búsqueda de usuario para login
        Usuario u = new Usuario();
        u.setCorreo("test@mail.com");
        u.setContrasena("encoded_pass");
        u.setVerificado(true);
        u.setActivo(true);

        when(usuarioRepository.findByCorreo("test@mail.com")).thenReturn(u);
        when(passwordEncoder.matches("raw_pass", "encoded_pass")).thenReturn(true);

        Usuario logged = usuarioService.loginPorCorreo("test@mail.com", "raw_pass");
        assertNotNull(logged);
    }

    // --- US02: PERSONALIZACIÓN DE PERFIL ---
    @Test
    void testActualizarPerfil_US02() {
        Long id = 1L;
        PerfilRequest req = new PerfilRequest("UPC", 1500f, "Bus", LocalDate.now(), "Nuevo Nom", "user_new");
        Usuario u = new Usuario(); u.setIdUsuario(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(u));
        when(preferenciaRepository.findByUsuarioIdUsuario(id)).thenReturn(new ArrayList<>()); // Sin preferencias previas
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario res = usuarioService.actualizarPerfil(id, req);
        assertEquals("Nuevo Nom", res.getNombre());
    }

    // --- US11: DESACTIVAR CUENTA ---
    @Test
    void testDesactivarCuenta_US11() {
        Long id = 1L;
        Usuario u = new Usuario(); u.setActivo(true);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(u));

        boolean exito = usuarioService.desactivarCuenta(id, true);
        assertTrue(exito);
        assertFalse(u.isActivo());
    }
}