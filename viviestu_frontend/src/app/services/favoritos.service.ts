import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class FavoritosService {
  private API = '/api/favoritos';

  constructor(private http: HttpClient) {}

  list(): Promise<any[]> {
    return this.http.get<any[]>(this.API).toPromise();
  }

  create(payload: { title: string; description?: string }) {
    return this.http.post<any>(this.API, payload).toPromise();
  }

  delete(id: number) {
    return this.http.delete(this.API + '/' + id).toPromise();
  }
}
