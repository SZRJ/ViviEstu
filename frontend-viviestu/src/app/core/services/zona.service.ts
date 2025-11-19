import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Preference } from '../models/preference.model';
import { Zona } from '../models/zona.model';

@Injectable({
  providedIn: 'root'
})
export class ZonaService {
  private http = inject(HttpClient);
  // Reemplaza con la URL base de tu backend Spring Boot
  private API_URL = 'http://localhost:8080/api/zonas'; 

  /**
   * Busca zonas enviando los parámetros de preferencia en el cuerpo de la solicitud (POST).
   * * @param preference - Objeto Preference con los filtros del usuario.
   * @returns Observable de un arreglo de objetos Zona.
   */
  getZonasByFilters(preference: Preference): Observable<Zona[]> {
    // El endpoint que usa un objeto Preference para filtrar
    const endpoint = `${this.API_URL}/filtrar`; 
    return this.http.post<Zona[]>(endpoint, preference);
  }

  // Puedes añadir otros métodos como obtener por ID, etc.
}