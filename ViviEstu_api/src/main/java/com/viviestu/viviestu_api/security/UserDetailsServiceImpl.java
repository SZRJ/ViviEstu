package com.viviestu.viviestu_api.security;


import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.DisabledException;

import java.util.ArrayList; // Para los roles, si los tuvieras

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        if (!usuario.isVerificado()) {
            throw new DisabledException("Debe verificar su cuenta antes de iniciar sesión.");
        }
        if (!usuario.isActivo()) {
            throw new DisabledException("La cuenta se encuentra desactivada.");
        }

        return new User(usuario.getCorreo(), usuario.getContrasena(),
                new ArrayList<>());
    }
}