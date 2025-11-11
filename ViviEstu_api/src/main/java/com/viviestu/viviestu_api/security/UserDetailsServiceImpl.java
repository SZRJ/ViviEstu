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
            // ✅ OK: El usuario realmente no existe
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        // 2. Aplicamos las reglas de negocio del Login (RN-02 y RN-11)
        if (!usuario.isVerificado()) {
            // ✅ CORRECCIÓN: Lanza una excepción que Spring Security maneja mejor
            throw new DisabledException("Debe verificar su cuenta antes de iniciar sesión.");
        }
        if (!usuario.isActivo()) {
            // ✅ CORRECCIÓN
            throw new DisabledException("La cuenta se encuentra desactivada.");
        }

        // 3. Creamos el UserDetails de Spring
        return new User(usuario.getCorreo(), usuario.getContrasena(),
                new ArrayList<>());
    }
}