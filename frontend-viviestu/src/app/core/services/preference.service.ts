import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { Preference } from '../models/preference.model';
import { UsuarioService } from './usuario.service'; // Necesario para obtener el ID de usuario
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PreferenceService {
  private http = inject(HttpClient);
  private usuarioService = inject(UsuarioService);
  private API_URL = `${environment.apiUrl}/preferencias`; 

  /**
   * Proporciona un objeto de preferencia inicial con valores por defecto.
   */
  getInitialPreference(): Preference {
    return {
        presupuesto: 750,
        tiempoMax: 45,
        seguridad: 'MEDIA',
        transporte: 'AUTOBUS',
        universidad: 'UNMSM',
        idUsuario: this.usuarioService.userInfo()?.idUsuario || -1 // Usar -1 si no hay usuario, aunque el guard lo evitará
    };
  }

  /**
   * Obtiene la preferencia del usuario actualmente logueado.
   * Spring Boot debe mapear la URL con el token para saber qué usuario es.
   */
  getPreferenceByUserId(): Observable<Preference> {
    const endpoint = `${this.API_URL}/buscar`;
    
    // Spring Boot debe manejar la autenticación y obtener el ID del usuario
    // del token JWT para buscar su preferencia.
    return this.http.get<Preference>(endpoint);
  }

  /**
   * Guarda o actualiza la preferencia del usuario.
   * @param preference El objeto Preference a guardar.
   */
  savePreference(preference: Preference): Observable<Preference> {
    return this.http.post<Preference>(this.API_URL, preference);
  }
}