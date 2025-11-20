import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { Zona } from '../models/zona.model'; // Asegúrate de tener tu modelo Zona creado

@Injectable({
  providedIn: 'root'
})
export class ZonaService {
  private http = inject(HttpClient);
  private API_URL = 'http://localhost:8080/api/zonas';

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

 

  // 2. OBTENER POR ID (GET /api/zonas/{id})
  obtenerPorId(id: number): Observable<Zona> {
    return this.http.get<ApiResponse<Zona>>(`${this.API_URL}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(map(resp => resp.data));
  }

  listarTodas(): Observable<Zona[]> {
    return this.http.get<any>(this.API_URL, { headers: this.getAuthHeaders() })
      .pipe(map(response => {
        // Intenta sacar .data, si no existe, usa la respuesta directa
        const lista = response.data || response;
        return Array.isArray(lista) ? lista : [];
      }));
  }
  
  // Haz lo mismo con listarRecomendadas si puedes
  listarRecomendadas(idUsuario: number): Observable<Zona[]> {
    const url = `${this.API_URL}/recomendadas?idUsuario=${idUsuario}`;
    return this.http.get<any>(url, { headers: this.getAuthHeaders() })
      .pipe(map(response => {
         const lista = response.data || response;
         return Array.isArray(lista) ? lista : [];
      }));
  }
}