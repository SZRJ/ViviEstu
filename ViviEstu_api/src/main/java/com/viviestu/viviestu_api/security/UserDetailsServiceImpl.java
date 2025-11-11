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
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// 🟢 Añadir este import para el rol
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// 🟢 Añadir este import para crear la lista
import java.util.Collections;
import java.util.List; // Este ya lo puedes teners

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

        // 🟢 CÓDIGO CORREGIDO: Declaramos y llenamos la lista DE AUTORIDADES
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 🔑 LÓGICA TEMPORAL PARA ASIGNAR ROLE_ADMIN:
        if (usuario.getIdUsuario() == 1) { // Suponemos que ID 1 es el Admin (Reze)
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // 🟢 CÓDIGO CORREGIDO: Usamos el constructor de User con la lista de autoridades
        return new User(usuario.getCorreo(), usuario.getContrasena(), authorities);
    }
}