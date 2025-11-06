package com.viviestu.viviestu_api.security;


import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // Para los roles, si los tuvieras

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // 1. Buscamos al usuario por correo (nuestro 'username')
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        // 2. Aplicamos las reglas de negocio del Login (RN-02 y RN-11)
        if (!usuario.isVerificado()) {
            throw new UsernameNotFoundException("Usuario no verificado: " + correo);
        }
        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario desactivado: " + correo);
        }

        // 3. Creamos el UserDetails de Spring
        // (Usamos el correo como username y la contraseña HASHADA de la BD)
        return new User(usuario.getCorreo(), usuario.getContrasena(),
                new ArrayList<>()); // Lista de roles/autoridades (vacía por ahora)
    }
}