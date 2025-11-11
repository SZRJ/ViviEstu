package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.dto.request.*;
import com.viviestu.viviestu_api.dto.response.UsuarioResponse;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.service.UsuarioService;
import com.viviestu.viviestu_api.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    /// POST /api/usuarios/login  (RN-02: sólo usuarios verificados pueden miniciar sesión)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioResponse>> login(@RequestBody LoginRequest req) {
        Usuario u = usuarioService.loginPorCorreo(req.correo(), req.contrasena());
        UsuarioResponse resp = new UsuarioResponse(u.getIdUsuario(), u.getNombre(), u.getNombreUsuario(),
                u.getFechaNacimiento(), u.getCorreo(), u.isVerificado(), u.isActivo());
        return ResponseEntity.ok(new ApiResponse<>(200, "Login exitoso", resp));
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
