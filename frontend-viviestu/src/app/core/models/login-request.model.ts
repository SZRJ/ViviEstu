// Define la estructura de los datos que se envían al servidor
// en la solicitud de inicio de sesión.
export interface LoginRequest {
  correo: string;
  contrasena: string;
}