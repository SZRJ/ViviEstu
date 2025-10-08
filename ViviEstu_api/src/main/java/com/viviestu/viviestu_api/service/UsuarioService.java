package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Desactiva la cuenta del usuario (marca activo = false) solo si confirmacion == true.
     */
    public boolean desactivarCuenta(Integer idUsuario, boolean confirmacion) {
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
