package com.viviestu.viviestu_api.security;

import com.viviestu.viviestu_api.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // Esta es la clase que tu captura llama "JwtAuthenticationFilter"

    // 1. Define el encriptador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Define el manejador de autenticación
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    // 3. Define las reglas de seguridad de los endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
                .cors(cors -> {})
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos (No requieren token)
                        .requestMatchers("/api/usuarios/registro", "/api/usuarios/login", "/api/usuarios/verificar/**").permitAll()
                        // Permitir ver zonas públicas sin loguearse (GET)
                        .requestMatchers(HttpMethod.GET, "/api/zonas", "/api/zonas/{idZona}").permitAll()

                        // Endpoints Protegidos (Requieren token JWT)
                        // US11
                        .requestMatchers("/api/usuarios/{id}/desactivar").authenticated()
                        // US12
                        .requestMatchers("/api/notificaciones/{idUsuario}").authenticated()
                        // US13
                        .requestMatchers("/api/zonas/{idZona}/calificaciones").authenticated()
                        // US14
                        .requestMatchers("/api/zonas/{id}/comentarios").authenticated()

                        // Aseguramos el resto
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Le decimos a Spring que no cree sesiones, usaremos JWT
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // 4. Añadimos nuestro filtro JWT antes del filtro de autenticación de Spring
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}