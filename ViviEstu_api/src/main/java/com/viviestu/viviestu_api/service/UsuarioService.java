package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public ResponseEntity<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public ResponseEntity<Usuario> actualizarUsuario(Long id, Usuario datosActualizados) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(datosActualizados.getNombre());
                    usuario.setNombreUsuario(datosActualizados.getNombreUsuario());
                    usuario.setFechaNacimiento(datosActualizados.getFechaNacimiento());
                    usuario.setCorreo(datosActualizados.getCorreo());
                    usuario.setContrasena(datosActualizados.getContrasena());
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok(usuario);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> eliminarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    /**
     * Desactiva la cuenta del usuario (marca activo = false) solo si confirmacion == true.
     */
    public boolean desactivarCuenta(Long idUsuario, boolean confirmacion) {
        if (!confirmacion) return false;
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (!opt.isPresent()) return false;
        Usuario u = opt.get();
        if (!u.isActivo()) return false; // ya inactivo
        u.setActivo(false);
        usuarioRepository.save(u);
        return true;
    }
}
