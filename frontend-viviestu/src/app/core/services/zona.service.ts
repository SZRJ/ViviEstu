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

  // --- GET BASICO ---
  obtenerPorId(id: number): Observable<Zona> {
    return this.http.get<ApiResponse<Zona>>(`${this.API_URL}/${id}`, { headers: this.getAuthHeaders() })
      .pipe(map(resp => resp.data));
  }

  listarTodas(): Observable<Zona[]> {
    return this.http.get<any>(this.API_URL, { headers: this.getAuthHeaders() })
      .pipe(map(response => {
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

  // --- FAVORITOS ---
  listarFavoritos(idUsuario: number): Observable<any[]> {
    const url = `${environment.apiUrl}/favoritos/${idUsuario}`;
    return this.http.get<any>(url, { headers: this.getAuthHeaders() })
      .pipe(map(resp => resp.data || []));
  }

  agregarFavorito(idUsuario: number, idZona: number): Observable<any> {
    const url = `${environment.apiUrl}/favoritos/${idUsuario}`;
    return this.http.post(url, { idZona }, { headers: this.getAuthHeaders() });
  }

  eliminarFavorito(idUsuario: number, idZona: number): Observable<any> {
    const url = `${environment.apiUrl}/favoritos/${idUsuario}/${idZona}`;
    return this.http.delete(url, { headers: this.getAuthHeaders() });
  }

  // --- INTERACCIONES (Comentarios, Calificaciones, Recomendar) ---
  obtenerComentarios(idZona: number): Observable<any[]> {
    const url = `${this.API_URL}/${idZona}/comentarios`;
    return this.http.get<any>(url, { headers: this.getAuthHeaders() })
      .pipe(map(resp => {
          const lista = resp.data || resp; 
          return Array.isArray(lista) ? lista : [];
      }));
  }

  enviarComentario(idZona: number, idUsuario: number, texto: string): Observable<any> {
    const url = `${this.API_URL}/${idZona}/comentarios`;
    const body = { idUsuario: idUsuario, comentario: texto };
    return this.http.post<any>(url, body, { headers: this.getAuthHeaders() });
  }

  calificarZona(idZona: number, idUsuario: number, puntuacion: number): Observable<any> {
    const url = `${this.API_URL}/${idZona}/calificaciones`;
    const body = { idUsuario: idUsuario, puntuacion: puntuacion };
    return this.http.post<any>(url, body, { headers: this.getAuthHeaders() });
  }

  toggleRecomendacion(idZona: number, estado: boolean): Observable<any> {
    const url = `${this.API_URL}/recomendacion`;
    const body = { idZona: idZona, recomendado: estado };
    return this.http.put<any>(url, body, { headers: this.getAuthHeaders() });
  }

  // >>>>> EL MÉTODO QUE TE FALTABA (US08 - FILTROS) <<<<<
  filtrarZonas(filtros: any): Observable<Zona[]> {
    const url = `${this.API_URL}/filtrar`;
    return this.http.post<ApiResponse<Zona[]>>(url, filtros, { headers: this.getAuthHeaders() })
      .pipe(map(resp => {
          const lista = resp.data || resp;
          return Array.isArray(lista) ? lista : [];
      }));
  }
  // US07: CALCULAR TIEMPO DE TRANSPORTE
  // GET /api/transporte/tiempo?zonaId=1&destino=UPC&modo=bus
  calcularTransporte(idZona: number, destino: string, modo: string): Observable<any> {
    const url = `${environment.apiUrl}/transporte/tiempo?zonaId=${idZona}&destino=${destino}&modo=${modo}`;
    return this.http.get<ApiResponse<any>>(url, { headers: this.getAuthHeaders() })
      .pipe(map(resp => resp.data));
  }
}