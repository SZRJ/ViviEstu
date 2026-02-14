export interface RegistroRequest {
    nombre: string;          // Nombre completo
    nombreUsuario: string;   // Usuario
    fechaNacimiento: string; // Lo mandaremos como string (YYYY-MM-DD)
    correo: string;
    contrasena: string;
}