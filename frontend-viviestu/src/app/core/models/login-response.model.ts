import { UserInfo } from './user-info.model';

// Define la estructura de la respuesta que el servidor Spring Boot devuelve
// después de que un usuario se autentica correctamente.
export interface LoginResponse {
  // El JSON Web Token (JWT) necesario para futuras peticiones autenticadas
  jwt: string;
  
  // La información del usuario asociada al token
  userInfo: UserInfo;
}