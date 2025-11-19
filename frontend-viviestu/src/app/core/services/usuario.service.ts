import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; // ¡CRÍTICO: Importar HttpHeaders!
import { Observable, tap } from 'rxjs';
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import { UserInfo } from '../models/user-info.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);

  private API_URL = 'http://localhost:8080/api/usuarios';

  // Señales para manejar el estado de autenticación
  token = signal<string | null>(localStorage.getItem('auth_token'));
  userInfo = signal<UserInfo | null>(this.loadUserInfo());

  // >>>>> MODIFICACIÓN AQUÍ <<<<<
  // 1. Crea una señal computada para exponer el nombre del usuario
  userName = computed(() => this.userInfo()?.nombre || 'Invitado');
  // >>>>> FIN MODIFICACIÓN <<<<<

  constructor() {
    // Sincroniza el estado de logueo con el storage al inicio
    if (this.token() && !this.userInfo()) {
      this.userInfo.set(this.loadUserInfo());
    }
  }
  
  isLoggedIn(): boolean {
    return !!this.token();
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    const endpoint = `${this.API_URL}/login`;

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json' 
      })
    };
    
    return this.http.post<LoginResponse>(
        endpoint, 
        credentials, 
        httpOptions 
    ).pipe(
      tap(response => {
        // 1. Guardar el token
        localStorage.setItem('auth_token', response.jwt);
        this.token.set(response.jwt);
        
        // 2. Guardar la info del usuario
        const info: UserInfo = response.userInfo; 
        localStorage.setItem('user_info', JSON.stringify(info));
        this.userInfo.set(info);

        console.log('Login exitoso. Usuario:', info.nombre);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_info');
    this.token.set(null);
    this.userInfo.set(null);
    console.log('Sesión cerrada.');
  }

  private loadUserInfo(): UserInfo | null {
    const infoJson = localStorage.getItem('user_info');
    if (infoJson) {
      try {
        return JSON.parse(infoJson) as UserInfo;
      } catch (e) {
        console.error('Error al parsear UserInfo del storage', e);
        return null;
      }
    }
    return null;
  }
}