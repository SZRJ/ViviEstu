import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { Zona } from '../models/zona.model';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ZonaService {
  private http = inject(HttpClient);
  private API_URL = `${environment.apiUrl}/zonas`;

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }


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

  listarRecomendadas(idUsuario: number): Observable<Zona[]> {
    const url = `${this.API_URL}/recomendadas?idUsuario=${idUsuario}`;
    return this.http.get<any>(url, { headers: this.getAuthHeaders() })
      .pipe(map(response => {
         const lista = response.data || response;
         return Array.isArray(lista) ? lista : [];
      }));
  }

  // OBTENER COMENTARIOS DE UNA ZONA
  obtenerComentarios(idZona: number): Observable<any[]> {
    const url = `${this.API_URL}/${idZona}/comentarios`;
    return this.http.get<any>(url, { headers: this.getAuthHeaders() });
  }

  //ENVIAR UN NUEVO COMENTARIO
  enviarComentario(idZona: number, idUsuario: number, texto: string): Observable<any> {
    const url = `${this.API_URL}/${idZona}/comentarios`;
    const body = {
      idUsuario: idUsuario,
      comentario: texto
    };
    return this.http.post<any>(url, body, { headers: this.getAuthHeaders() });
  }

  calificarZona(idZona: number, idUsuario: number, puntuacion: number): Observable<any> {
    const url = `${this.API_URL}/${idZona}/calificaciones`;
    
    const body = { 
      idUsuario: idUsuario, 
      puntuacion: puntuacion 
    };
    
    return this.http.post<any>(url, body, { headers: this.getAuthHeaders() });
  }
}
