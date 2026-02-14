package com.viviestu.viviestu_api.security;

import com.viviestu.viviestu_api.security.UserDetailsServiceImpl;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 🚨 Permite todo para pruebas. Para PROD, sé más específico.
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Permitir todas las cabeceras
        configuration.setAllowCredentials(false); // No enviar cookies o certificados

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todas las rutas
        return source;
    }

    // 3. Define las reglas de seguridad de los endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 👈 Usar el bean aquí
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos (No requieren token)
                        .requestMatchers(
                                "/api/usuarios/verificar/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        //permitir logearse o registrase sin token
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()


                        // Permitir ver zonas públicas sin loguearse (GET)
                        .requestMatchers(HttpMethod.GET, "/api/zonas", "/api/zonas/{idZona}").permitAll()
                        // Otros GETs públicos relacionados con zonas/utilidades
                        .requestMatchers(HttpMethod.GET, "/api/zonas/promedios-comentarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/zonas/{idZona}/calificaciones/promedio").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/zonas/filtrar").permitAll() // Filtrado no requiere auth
                        .requestMatchers(HttpMethod.POST, "/api/zonas/comparar").permitAll() // Comparación no requiere auth
                        .requestMatchers(HttpMethod.GET, "/api/transporte/tiempo").permitAll() // Transporte/Tiempo no requiere auth





                        // Endpoints protegidos
                        .requestMatchers("/api/usuarios/{id}/desactivar").authenticated()
                        .requestMatchers("/api/notificaciones/{idUsuario}").authenticated()
                        .requestMatchers("/api/zonas/{idZona}/calificaciones").authenticated()
                        .requestMatchers("/api/zonas/{id}/comentarios").authenticated()
                        .requestMatchers("/api/favoritos/{idUsuario}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/favoritos/{usuarioId}").authenticated() // POST /api/favoritos/{usuarioId}
                        .requestMatchers(HttpMethod.DELETE, "/api/favoritos/{idUsuario}/{idZona}").authenticated() // DELETE /api/favoritos/{idUsuario}/{idZona}
                        .requestMatchers(HttpMethod.PUT, "/api/zonas/recomendacion").authenticated()
                        // Endpoints de Preferencia (CRUD)
                        .requestMatchers("/api/preferencias").authenticated() // POST /api/preferencias
                        .requestMatchers("/api/preferencias/{idUsuario}").authenticated() // GET /api/preferencias/{idUsuario}
                        .requestMatchers("/api/preferencias/{idPreferencia}").authenticated() // PUT, DELETE /api/preferencias/{idPreferencia}


                        // Endpoints de Usuario (perfil, get, etc.)
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").authenticated() // GET /api/usuarios/{id}
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/{id}/perfil").authenticated() // PUT /api/usuarios/{id}/perfil

                        // Reportes y Resumen
                        .requestMatchers(HttpMethod.GET, "/api/reportes/pdf/{idUsuario}").authenticated() // GET /api/reportes/pdf/{idUsuario}
                        .requestMatchers(HttpMethod.GET, "/api/resumen/{idUsuario}").authenticated() // GET /api/resumen/{idUsuario}

                        // Simulador
                        .requestMatchers(HttpMethod.POST, "/api/simulador/gasto").authenticated() // POST /api/simulador/gasto


                        //Admin endpoints
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/preferencias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/zonas").hasRole("ADMIN") // Registrar nueva zona
                        //.requestMatchers(HttpMethod.PUT, "/api/zonas/recomendacion").hasRole("ADMIN") // Marcar recomendación


                        // Asegurar el resto
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