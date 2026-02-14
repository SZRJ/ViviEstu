import { Component, OnInit } from '@angular/core';
import { FavoritosService } from '../services/favoritos.service';

interface Favorito {
  id?: number;
  title: string;
  description?: string;
  createdAt?: string;
}

@Component({
  selector: 'app-favoritos',
  templateUrl: './favoritos.component.html',
  styleUrls: ['./favoritos.component.css']
})
export class FavoritosComponent implements OnInit {
  list: Favorito[] = [];
  title = '';
  description = '';

  constructor(private svc: FavoritosService) {}

  ngOnInit(): void {
    this.load();
  }

  async load() {
    this.list = await this.svc.list();
  }

  async add() {
    if (!this.title) return alert('Título requerido');
    await this.svc.create({ title: this.title, description: this.description });
    this.title = '';
    this.description = '';
    await this.load();
  }

  async remove(id?: number) {
    if (!id) return;
    if (!confirm('Eliminar favorito ' + id + '?')) return;
    await this.svc.delete(id);
    await this.load();
  }
}
