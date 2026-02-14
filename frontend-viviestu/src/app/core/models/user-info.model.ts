// Este modelo representa la información del usuario que se guarda en el servicio
// después de un inicio de sesión exitoso.

export interface UserInfo {
    // Coincide con el campo 'id_usuario' de la tabla 'usuarios'
    idUsuario: number; 
    
    // Coincide con el campo 'nombre'
    nombre: string;
    
    // Coincide con el campo 'email'
    email: string;

    nombreUsuario: string;
    
    // El rol del usuario (Ej: ESTUDIANTE)
    rol: string; 
    
    fechaNacimiento?: string; 
    correo?: string; 


}
