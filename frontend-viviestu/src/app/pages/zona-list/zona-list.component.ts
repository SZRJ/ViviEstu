import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ZonaService } from '../../core/services/zona.service';
import { UsuarioService } from '../../core/services/usuario.service';
import { Zona } from '../../core/models/zona.model';

@Component({
  selector: 'app-zona-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './zona-list.component.html',
  styleUrls: ['./zona-list.component.css']
})
export class ZonaListComponent implements OnInit {
  
  private zonaService = inject(ZonaService);
  public usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef);
  
  zonas: Zona[] = [];
  loading = true;
  vistaActual: 'recomendadas' | 'todas' | 'filtradas' = 'todas';
  misFavoritosIds: number[] = [];
  showUserMenu = false; // Para el navbar

  // Filtros
  filtro = {
    nombre: '',
    minPrecio: null,
    maxPrecio: null,
    seguridad: '',
    transporte: ''
  };

  ngOnInit(): void {
    // 1. Cargar favoritos para saber qué corazones pintar
    const idUsuario = localStorage.getItem('usuarioId');
    if (idUsuario) {
        this.zonaService.listarFavoritos(parseInt(idUsuario)).subscribe({
            next: (favs) => {
                this.misFavoritosIds = favs.map((f: any) => f.idZona);
                this.decidirQueCargar();
            },
            error: () => this.decidirQueCargar()
        });
    } else {
        this.decidirQueCargar();
    }
  }

  decidirQueCargar() {
    const idUsuario = localStorage.getItem('usuarioId');
    if (idUsuario) {
      this.vistaActual = 'recomendadas';
      this.cargarRecomendadas(parseInt(idUsuario));
    } else {
      this.vistaActual = 'todas';
      this.cargarTodas();
    }
  }

  // --- CARGA DE DATOS ---
  cargarRecomendadas(id: number) {
    this.zonaService.listarRecomendadas(id).subscribe({
      next: (data) => {
        this.zonas = this.procesarDatos(data);
        this.loading = false;
        if (data.length === 0 && this.vistaActual === 'recomendadas') {
           this.verTodas();
        }
        this.cd.detectChanges();
      },
      error: () => this.verTodas()
    });
  }

  cargarTodas() {
    this.zonaService.listarTodas().subscribe({
      next: (data) => { 
        this.zonas = this.procesarDatos(data); 
        this.loading = false; 
        this.cd.detectChanges();
      },
      error: () => { this.loading = false; }
    });
  }

  // --- FILTROS ---
  aplicarFiltros() {
    this.loading = true;
    this.vistaActual = 'filtradas';
    this.zonaService.filtrarZonas(this.filtro).subscribe({
      next: (data) => {
        this.zonas = this.procesarDatos(data);
        this.loading = false;
        this.cd.detectChanges();
      },
      error: () => { this.loading = false; alert('Error al filtrar'); }
    });
  }

  limpiarFiltros() {
    this.filtro = { nombre: '', minPrecio: null, maxPrecio: null, seguridad: '', transporte: '' };
    this.verTodas();
  }

  verRecomendadas() {
    const id = parseInt(localStorage.getItem('usuarioId') || '0');
    if (!id) { alert('Inicia sesión'); return; }
    this.vistaActual = 'recomendadas';
    this.loading = true;
    this.cargarRecomendadas(id);
  }

  verTodas() {
    this.vistaActual = 'todas';
    this.loading = true;
    this.cargarTodas();
  }

  // --- AUXILIARES ---
  procesarDatos(lista: any[]): Zona[] {
    return lista.map(z => ({
        ...z,
        promedioCalificacion: z.promedioCalificacion || (Math.random() * 1.5 + 3.5).toFixed(1),
        esFavorito: this.misFavoritosIds.includes(z.idZona)
    }));
  }

  toggleFavorito(event: Event, zona: Zona) {
    event.stopPropagation();
    event.preventDefault();
    if (!this.usuarioService.isLoggedIn()) { alert('Inicia sesión'); return; }
    
    const idUsuario = parseInt(localStorage.getItem('usuarioId')!);
    zona.esFavorito = !zona.esFavorito;

    if (zona.esFavorito) {
        this.zonaService.agregarFavorito(idUsuario, zona.idZona).subscribe();
    } else {
        this.zonaService.eliminarFavorito(idUsuario, zona.idZona).subscribe();
    }
  }

  getImagenRandom(id: number) {
    return `https://picsum.photos/seed/${id}/400/250`;
  }

  // --- MENÚ NAVBAR ---
  toggleUserMenu(event: Event) {
    event.stopPropagation();
    this.showUserMenu = !this.showUserMenu;
  }
  logout() { this.usuarioService.logout(); this.router.navigate(['/']); }
  irAlPerfil() { this.router.navigate(['/profile']); }
  irADesactivarCuenta() { this.router.navigate(['/profile']); }
}