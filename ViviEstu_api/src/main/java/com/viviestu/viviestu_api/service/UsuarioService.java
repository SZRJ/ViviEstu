package com.viviestu.viviestu_api.service;

import com.viviestu.viviestu_api.dto.request.RegistroRequest;
import com.viviestu.viviestu_api.dto.request.PerfilRequest;
import com.viviestu.viviestu_api.model.Usuario;
import com.viviestu.viviestu_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.viviestu.viviestu_api.model.Preferencia;
import com.viviestu.viviestu_api.repository.PreferenciaRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- AÑADIR


import java.util.List;
import java.util.Optional;

/// Servicio que implementa reglas de negocio del módulo Usuario (RN-01 .. RN-04)
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PreferenciaRepository preferenciaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // <-- AÑADIR ESTO

    /// Obtiene todos los usuarios (podrías ocultar campos sensibles si se usa en producción)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorIdOptional(Long id) {
        return usuarioRepository.findById(id);
    }

    public ResponseEntity<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // MÉTODO MODIFICADO (RN-01)
    public Usuario crearUsuario(RegistroRequest req) {
        // ... (tus validaciones de req == null, correo, etc. se quedan igual) ...

        if (usuarioRepository.existsByCorreo(req.correo())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo");
        }
        if (req.nombreUsuario() != null && usuarioRepository.existsByNombreUsuario(req.nombreUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setNombreUsuario(req.nombreUsuario());
        u.setFechaNacimiento(req.fechaNacimiento());
        u.setCorreo(req.correo());

        // --- CAMBIO IMPORTANTE ---
        // Encriptamos la contraseña antes de guardarla
        u.setContrasena(passwordEncoder.encode(req.contrasena()));
        // -------------------------

        u.setVerificado(false); // RN-01
        u.setActivo(true);

        Usuario guardado = usuarioRepository.save(u);
        return guardado;
    }

    public Usuario loginPorCorreo(String correo, String contrasena) {
        if (correo == null || contrasena == null)
            throw new IllegalArgumentException("Correo y contraseña son requeridos");

        Usuario u = usuarioRepository.findByCorreo(correo);
        if (u == null) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        // --- CAMBIO IMPORTANTE ---
        // Comparamos la contraseña plana del request con la hasheada de la BD
        if (!passwordEncoder.matches(contrasena, u.getContrasena())) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }
        // -------------------------

        if (!u.isVerificado()) { // RN-02
            throw new IllegalArgumentException("Debe verificar su correo antes de iniciar sesión");
        }

        if (!u.isActivo()) { // RN-11
            throw new IllegalArgumentException("La cuenta está desactivada");
        }

        return u;
    }

    /**
     * Actualizar perfil: respeta RN-03 (universidad obligatoria para considerar el perfil "válido")
     * y RN-04 (presupuesto > 0) si se envía presupuesto.
     */

    /**
     * Actualizar perfil: CORREGIDO (RN-03 y RN-04)
     * Ahora guarda los datos de perfil (universidad, presupuesto)
     * en la entidad 'Preferencia' asociada.
     * (REEMPLAZA TU MÉTODO EXISTENTE CON ESTE)
     */
    public Usuario actualizarPerfil(Long id, PerfilRequest req) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 1. Actualizar campos del Usuario
        if (req.nombre() != null) u.setNombre(req.nombre());
        if (req.nombreUsuario() != null) u.setNombreUsuario(req.nombreUsuario());
        if (req.fechaNacimiento() != null) u.setFechaNacimiento(req.fechaNacimiento());

        // 2. Validar y Sincronizar Preferencias (RN-03 y RN-04)
        // Busca la primera preferencia del usuario o crea una nueva
        Preferencia pref = preferenciaRepository.findByUsuarioIdUsuario(id)
                .stream()
                .findFirst()
                .orElse(new Preferencia());

        pref.setUsuario(u); // Asegurar la asociación

        if (req.universidad() != null) {
            if (req.universidad().trim().isEmpty()) // RN-03
                throw new IllegalArgumentException("Universidad no puede estar vacía");
            pref.setUniversidad(req.universidad());
        }

        if (req.presupuesto() != null) {
            if (req.presupuesto() <= 0) // RN-04
                throw new IllegalArgumentException("El presupuesto debe ser mayor que 0");
            pref.setPresupuesto(req.presupuesto());
        }

        if (req.transporte() != null) {
            pref.setTransporte(req.transporte());
        }

        // Guardar la preferencia (nueva o actualizada)
        preferenciaRepository.save(pref);

        // Guardar el usuario (actualizado)
        Usuario actualizado = usuarioRepository.save(u);
        return actualizado;
    }

    /**
     * Desactiva la cuenta si confirmación == true (RN-11)
     */
    public boolean desactivarCuenta(Long idUsuario, boolean confirmacion) {
        if (!confirmacion) return false;
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        if (!opt.isPresent()) return false;
        Usuario u = opt.get();
        if (!u.isActivo()) return false;
        u.setActivo(false);
        usuarioRepository.save(u);
        return true;
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    public Usuario obtenerPorCorreo(String correo) {

        return usuarioRepository.findByCorreo(correo);
    }
    /**
     * Implementación Faltante de RN-01 (Verificación)
     * Simula la verificación de cuenta. En un caso real, buscaría por token.
     * (AÑADE ESTE MÉTODO NUEVO)
     */
    public Usuario verificarCuenta(Long idUsuario) {
        Usuario u = usuarioRepository.findById(idUsuario).orElseThrow(() ->
                new IllegalArgumentException("Usuario no encontrado para verificación"));

        if (u.isVerificado()) {
            throw new IllegalArgumentException("La cuenta ya se encuentra verificada.");
        }

        u.setVerificado(true);
        return usuarioRepository.save(u);
    }

}
