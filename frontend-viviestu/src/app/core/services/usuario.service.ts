import { inject, Injectable, signal, computed } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap, map } from 'rxjs';

// Imports de tus modelos
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import { UserInfo } from '../models/user-info.model';
import { Preference } from '../models/preference.model';
import { ApiResponse } from '../models/api-response.model';
import { RegistroRequest } from '../models/registro-request.model';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);

  // --- DEFINICIÓN DE URLs ---
  private API_USUARIOS = `${environment.apiUrl}/usuarios`;
  private API_PREFERENCIAS = `${environment.apiUrl}/preferencias`;

  // --- ESTADO DEL USUARIO (SEÑALES) ---
  token = signal<string | null>(localStorage.getItem('auth_token'));
  userInfo = signal<UserInfo | null>(this.loadUserInfo());

  // Nombre computado para mostrar en el Home
  userName = computed(() =>
  this.userInfo()?.nombreUsuario
  || this.userInfo()?.nombre
  || this.userInfo()?.email
  || localStorage.getItem('usuarioNombre')
  || 'Invitado'
);


  constructor() {
    // Sincroniza el estado al recargar la página
    if (this.token() && !this.userInfo()) {
      this.userInfo.set(this.loadUserInfo());
    }
  }
  
  isLoggedIn(): boolean {
    return !!this.token();
  }

  // --- HELPER: CABECERAS CON TOKEN ---
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    if (!token) {
      console.warn('⚠️ No hay token en localStorage para la petición');
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // ==========================================
  // 1. AUTENTICACIÓN (LOGIN / REGISTRO / LOGOUT)
  // ==========================================

  login(credentials: LoginRequest): Observable<LoginResponse> {
    const endpoint = `${this.API_USUARIOS}/login`;
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    
    return this.http.post<any>(endpoint, credentials, httpOptions).pipe(
      tap(response => {
        
        // 1. Desempaquetar
        const datosReales = (response as any).data || response; 
        console.log('📦 DATOS CRUDOS (data):', datosReales); 

        // 2. Buscar Token
        const tokenRecibido = datosReales.jwt || datosReales.token;
        if (tokenRecibido) {
            localStorage.setItem('auth_token', tokenRecibido);
            this.token.set(tokenRecibido);
        }

        // 3. BUSCAR LA INFO DEL USUARIO (CORRECCIÓN AQUÍ)
        // Tu backend manda 'usuario', nosotros buscábamos 'userInfo'.
        // Ahora buscamos ambos por si acaso.
        let info = datosReales.userInfo || datosReales.usuario;

        // Si no está en ninguna subcarpeta, quizás está suelto
        if (!info) {
            info = datosReales;
        }

        // 4. Guardar Datos
        if (info) {
            console.log('👤 Objeto Usuario encontrado:', info);

            localStorage.setItem('user_info', JSON.stringify(info));
            this.userInfo.set(info);

            // 5. BUSCAR EL ID
            const idParaGuardar = info.idUsuario || info.id;

            if (idParaGuardar) {
                console.log('✅ GUARDANDO ID:', idParaGuardar);
                localStorage.setItem('usuarioId', idParaGuardar.toString());
            } else {
                console.error('❌ ERROR FATAL: No encuentro el ID dentro de:', info);
            }

            // 6. Guardar Nombre
            const nombreParaGuardar = info.nombreUsuario || info.nombre || info.correo || credentials.correo;
            if (nombreParaGuardar) {
                localStorage.setItem('usuarioNombre', nombreParaGuardar);
            }
        }
      })
    );
  }
  registrar(datos: any): Observable<any> {
    const endpoint = `${this.API_USUARIOS}/registro`;
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    return this.http.post<any>(endpoint, datos, httpOptions);
  }

  actualizarUsuario(idUsuario: number, datos: Partial<RegistroRequest>): Observable<any> {
    const endpoint = `${this.API_USUARIOS}/${idUsuario}/perfil`;

    return this.http.put<any>(endpoint, datos, { headers: this.getAuthHeaders() }).pipe(
      tap(resp => {
        const data = (resp as any).data || resp;

        if (data) {
          // Actualiza info en memoria y localStorage
          localStorage.setItem('user_info', JSON.stringify(data));
          this.userInfo.set(data);

          const nombreParaGuardar =
            data.nombreUsuario || data.nombre || data.correo || data.email;

          if (nombreParaGuardar) {
            localStorage.setItem('usuarioNombre', nombreParaGuardar);
          }
        }
      })
    );
  }

  logout(): void {
    // Limpiamos TODO
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user_info');
    localStorage.removeItem('usuarioNombre');
    localStorage.removeItem('authToken');
    localStorage.removeItem('usuarioId');

    this.token.set(null);
    this.userInfo.set(null);
    console.log('Sesión cerrada.');
  }

  // ==========================================
  // 2. GESTIÓN DE CUENTA
  // ==========================================

  desactivarCuenta(idUsuario: number): Observable<any> {
    const url = `${this.API_USUARIOS}/${idUsuario}/desactivar`;
    const body = { confirmacion: true };
    return this.http.patch(url, body, { headers: this.getAuthHeaders() });
  }

  // ==========================================
  // 3. PREFERENCIAS (Tus nuevos métodos)
  // ==========================================

  // OBTENER (GET /api/preferencias/{idUsuario}) -> Devuelve lista
  obtenerPreferencias(idUsuario: number): Observable<Preference[]> {
    const url = `${this.API_PREFERENCIAS}/${idUsuario}`;
    
    return this.http.get<ApiResponse<Preference[]>>(url, { headers: this.getAuthHeaders() })
      .pipe(
        // Extraemos solo la lista que viene dentro de 'data'
        map(response => response.data) 
      );
  }

  // CREAR (POST /api/preferencias)
  crearPreferencia(preferencia: Preference): Observable<any> {
    return this.http.post<ApiResponse<Preference>>(this.API_PREFERENCIAS, preferencia, { 
        headers: this.getAuthHeaders() 
    });
  }

  // ACTUALIZAR (PUT /api/preferencias/{idPreferencia})
  actualizarPreferencia(idPreferencia: number, preferencia: Preference): Observable<any> {
    const url = `${this.API_PREFERENCIAS}/${idPreferencia}`;
    return this.http.put<ApiResponse<Preference>>(url, preferencia, { 
        headers: this.getAuthHeaders() 
    });
  }

  // ==========================================
  // PRIVADOS
  // ==========================================
  private loadUserInfo(): UserInfo | null {
    const infoJson = localStorage.getItem('user_info');
    if (infoJson) {
      try {
        return JSON.parse(infoJson) as UserInfo;
      } catch (e) {
        console.error('Error al parsear UserInfo', e);
        return null;
      }
    }
    return null;
  }
}
