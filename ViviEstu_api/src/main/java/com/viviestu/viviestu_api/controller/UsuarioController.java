package com.viviestu.viviestu_api.controller;

import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.dto.DesactivarRequest;
import com.viviestu.viviestu_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

/**
 * Endpoint:
 * PATCH /api/usuarios/{id}/desactivar
 */

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String nombreUsuario = loginData.get("nombreUsuario");
        String contrasena = loginData.get("contrasena");

        Usuario usuario = usuarioService.obtenerPorNombreUsuario(nombreUsuario);

        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        return usuarioService.eliminarUsuario(id);}

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarCuenta (@PathVariable("id") Long id, @RequestBody DesactivarRequest request){
        if (request == null || request.getConfirmacion() == null) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"Se requiere confirmación\"}");
        }

        boolean ok = usuarioService.desactivarCuenta(id, request.getConfirmacion());
        if (!ok) {
            return ResponseEntity.badRequest().body("{\"mensaje\":\"No se pudo desactivar la cuenta (usuario no encontrado, ya inactivo o confirmación false)\"}");
        }

        return ResponseEntity.ok().body("{\"mensaje\":\"Cuenta desactivada\"}");

        }
    }
