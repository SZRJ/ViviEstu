package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.*;
import com.viviestu.viviestu_api.dto.response.UsuarioResponse;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.service.UsuarioService;
import com.viviestu.viviestu_api.util.ApiResponse;
import com.viviestu.viviestu_api.security.JwtUtil;
import com.viviestu.viviestu_api.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/// Controller para endpoints relacionados a usuarios
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /// GET /api/usuarios
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<UsuarioResponse> data = usuarios.stream().map(u ->
                        new UsuarioResponse(u.getIdUsuario(), u.getNombre(), u.getNombreUsuario(),
                                u.getFechaNacimiento(), u.getCorreo(), u.isVerificado(), u.isActivo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(200, "Lista de usuarios", data));
    }

    /// GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerPorId(@PathVariable Long id) {
        // Llama al método que devuelve un Optional
        return usuarioService.obtenerPorIdOptional(id)
                .map(u -> {
                    // Ahora 'u' es un Usuario, no un ResponseEntity
                    UsuarioResponse resp = new
                            UsuarioResponse(u.getIdUsuario(), u.getNombre(), u.getNombreUsuario(),
                            u.getFechaNacimiento(), u.getCorreo(), u.isVerificado(), u.isActivo());
                    return ResponseEntity.ok(new ApiResponse<>(200, "Usuario encontrado", resp));
                })
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Usuario no encontrado", null)));
    }

    /// POST /api/usuarios/registro  (RN-01: guardar verificado=false y notificar)
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrar(@RequestBody RegistroRequest req) {
        Usuario creado = usuarioService.crearUsuario(req);
        UsuarioResponse resp = new UsuarioResponse(creado.getIdUsuario(), creado.getNombre(), creado.getNombreUsuario(),
                creado.getFechaNacimiento(), creado.getCorreo(), creado.isVerificado(), creado.isActivo());
        return ResponseEntity.status(201).body(new ApiResponse<>(201, "Usuario registrado. Verifique su correo.", resp));
    }

    /// POST /api/usuarios/login  (RN-02: sólo usuarios verificados pueden iniciar sesión)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody LoginRequest req) {
        try {
            // 1. Autenticar con Spring Security (usará UserDetailsServiceImpl y PasswordEncoder)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.correo(), req.contrasena())
            );
        } catch (Exception e) {
            // Si falla (malas credenciales, no verificado, inactivo)
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Error de autenticación: " + e.getMessage(), null));
        }

        // 2. Si la autenticación fue exitosa, generar el token
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(req.correo());
        final String jwt = jwtUtil.generateToken(userDetails);

        // 3. (Opcional) Devolver también los datos del usuario
        Usuario u = usuarioService.obtenerPorNombreUsuario(userDetails.getUsername()); // O usa findByCorreo
        UsuarioResponse resp = new UsuarioResponse(u.getIdUsuario(), u.getNombre(), u.getNombreUsuario(),
                u.getFechaNacimiento(), u.getCorreo(), u.isVerificado(), u.isActivo());

        // DTO de respuesta anidado para el token
        record LoginResponse(String token, UsuarioResponse usuario) {}

        return ResponseEntity.ok(new ApiResponse<>(200, "Login exitoso",
                new LoginResponse(jwt, resp)
        ));
    }

    /// PUT /api/usuarios/{id}/perfil   (actualiza perfil, valida RN-03 y RN-04 donde aplique)
    @PutMapping("/{id}/perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarPerfil(@PathVariable Long id, @RequestBody PerfilRequest req) {
        Usuario actualizado = usuarioService.actualizarPerfil(id, req);
        UsuarioResponse resp = new UsuarioResponse(actualizado.getIdUsuario(), actualizado.getNombre(), actualizado.getNombreUsuario(),
                actualizado.getFechaNacimiento(), actualizado.getCorreo(), actualizado.isVerificado(), actualizado.isActivo());
        return ResponseEntity.ok(new ApiResponse<>(200, "Perfil actualizado", resp));
    }

    /// PATCH /api/usuarios/{id}/desactivar  (RN-11)
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<Void>> desactivarCuenta(@PathVariable Long id, @RequestBody DesactivarRequest req) {
        if (req == null || req.confirmacion() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Se requiere confirmación", null));
        }
        boolean ok = usuarioService.desactivarCuenta(id, req.confirmacion());
        if (!ok) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "No se pudo desactivar la cuenta (usuario no encontrado, ya inactivo o confirmación false)", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Cuenta desactivada", null));
    }
    /// GET /api/usuarios/verificar/{id} (Implementación simple de RN-01)
    @GetMapping("/verificar/{id}")
    public ResponseEntity<ApiResponse<String>> verificarCuenta(@PathVariable Long id) {
        usuarioService.verificarCuenta(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cuenta verificada exitosamente", "Usuario ID: " + id));
    }
}
