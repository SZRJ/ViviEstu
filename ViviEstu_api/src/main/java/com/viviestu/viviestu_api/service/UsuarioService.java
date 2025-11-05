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

import java.util.List;
import java.util.Optional;

/// Servicio que implementa reglas de negocio del módulo Usuario (RN-01 .. RN-04)
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PreferenciaRepository preferenciaRepository;

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

    /// Registro de usuario (RN-01: correo único). Marca verificado = false y devuelve mensaje de verificación.
    public Usuario crearUsuario(RegistroRequest req) {
        if (req == null) throw new IllegalArgumentException("Datos de registro incompletos");
        if (req.correo() == null || req.correo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (req.contrasena() == null || req.contrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        // Validar unicidad correo y nombreUsuario
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
        u.setContrasena(req.contrasena()); // Nota: recomiendo encriptar la contraseña con BCrypt en el futuro
        u.setVerificado(false); // RN-01: necesita verificación por correo
        u.setActivo(true);

        Usuario guardado = usuarioRepository.save(u);

        // TODO: Enviar correo de verificación (en producción implementar servicio de email que actualice verificado = true)
        return guardado;
    }

    /**
     * Login: sólo permite iniciar sesión si el correo/usuario existe, la contraseña coincide y verificado == true (RN-02).
     * Retorna el usuario si OK, sino lanza IllegalArgumentException.
     */
    public Usuario loginPorCorreo(String correo, String contrasena) {
        if (correo == null || contrasena == null)
            throw new IllegalArgumentException("Correo y contraseña son requeridos");

        Usuario u = usuarioRepository.findByCorreo(correo);
        if (u == null) throw new IllegalArgumentException("Credenciales incorrectas");

        if (!u.isVerificado()) {
            throw new IllegalArgumentException("Debe verificar su correo antes de iniciar sesión");
        }

        if (!u.getContrasena().equals(contrasena)) {
            // Si implementas hashing, acá debes usar passwordEncoder.matches(...)
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        if (!u.isActivo()) {
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
