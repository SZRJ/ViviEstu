package com.viviestu.viviestu_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔓 Rutas públicas que no deben pasar por validación JWT
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/usuarios/registro",
            "/api/usuarios/login",
            "/api/usuarios/verificar",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        //testeo
        System.out.println(">>> EJECUTANDO FILTRO JWT: " + request.getRequestURI());

        // ✅ Si la ruta es pública, no validamos el token
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // 1️⃣ Extrae el token si existe
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("Error al parsear JWT: " + e.getMessage());
            }
        }

        // 2️⃣ Valida el token y configura el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                // La carga del usuario (y el chequeo de activo/verificado) ocurre aquí
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);

                // Si el usuario se carga, el log de roles debe aparecer
                System.out.println(">>> ROLES LEÍDOS del usuario: " + userDetails.getAuthorities());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ESTO DEBE FUNCIONAR
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // 🚨 CRÍTICO: Capturamos la excepción (DisabledException/UsernameNotFoundException)
                // Esto evita que el filtro falle y garantiza que la petición continúe SIN autenticación,
                // permitiendo a Spring Security devolver 403/401 limpio.
                logger.warn("Fallo al autenticar/cargar usuario: " + e.getMessage());
            }
        }

        // 3️⃣ Continúa con el resto de filtros
        filterChain.doFilter(request, response);
    }
}
