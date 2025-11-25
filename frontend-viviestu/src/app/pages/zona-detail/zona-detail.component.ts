import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ZonaService } from '../../core/services/zona.service';
import { UsuarioService } from '../../core/services/usuario.service';
import { Zona } from '../../core/models/zona.model';

@Component({
  selector: 'app-zona-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './zona-detail.component.html',
  styleUrls: ['./zona-detail.component.css']
})
export class ZonaDetailComponent implements OnInit {
  
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private zonaService = inject(ZonaService);
  public usuarioService = inject(UsuarioService);
  private cd = inject(ChangeDetectorRef);

  zona: Zona | null = null;
  loading = true;
  showUserMenu = false;
  
  // COMENTARIOS
  comentarios: any[] = [];
  nuevoComentario: string = '';
  enviandoComentario = false;

  // CALIFICACIÓN
  puntuacionSeleccionada = 0;
  mensajeCalificacion = '';
  enviandoCalificacion = false;

  // RECOMENDACIÓN (US15)
  enviandoRecomendacion = false;

  // TRANSPORTE (US07) - ¡Esto es lo nuevo!
  destinoTransporte: string = 'Universidad';
  modoTransporte: string = 'Bus';
  resultadoTransporte: string | null = null;
  calculandoTransporte = false;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.zonaService.obtenerPorId(parseInt(id)).subscribe({
        next: (data) => {
          this.zona = data;
          this.loading = false;
          this.cd.detectChanges(); 
        },
        error: () => { this.loading = false; this.cd.detectChanges(); }
      });
      this.cargarComentarios(parseInt(id));
    }
  }

  // --- US07: TRANSPORTE ---
  calcularTiempo() {
    if (!this.zona) return;
    this.calculandoTransporte = true;
    this.resultadoTransporte = null;

    this.zonaService.calcularTransporte(this.zona.idZona, this.destinoTransporte, this.modoTransporte)
      .subscribe({
        next: (data) => {
          this.resultadoTransporte = `${data.tiempoEstimado} (${data.distancia})`;
          this.calculandoTransporte = false;
          this.cd.detectChanges();
        },
        error: () => {
            alert('Error al calcular tiempo');
            this.calculandoTransporte = false;
        }
      });
  }

  // --- RECOMENDAR (US15) ---
  toggleRecomendacion() {
    if (!this.zona) return;
    if (!this.usuarioService.isLoggedIn()) { alert('Inicia sesión'); return; }

    this.enviandoRecomendacion = true;
    const nuevoEstado = !this.zona.recomendado;

    this.zonaService.toggleRecomendacion(this.zona.idZona, nuevoEstado).subscribe({
      next: () => {
        if(this.zona) this.zona.recomendado = nuevoEstado;
        this.enviandoRecomendacion = false;
        this.cd.detectChanges();
      },
      error: () => { this.enviandoRecomendacion = false; }
    });
  }

  // ... (Resto de métodos: getImagenRandom, navbar, comentarios, calificar se mantienen igual) ...
  
  getImagenRandom(id: number, variant: number) { return `https://picsum.photos/seed/${id + variant}/800/600`; }
  toggleUserMenu(event: Event) { event.stopPropagation(); this.showUserMenu = !this.showUserMenu; }
  irAlPerfil() { this.router.navigate(['/profile']); }
  irADesactivarCuenta() { this.router.navigate(['/profile']); }
  logout() { this.usuarioService.logout(); this.router.navigate(['/']); }

  cargarComentarios(idZona: number) {
    this.zonaService.obtenerComentarios(idZona).subscribe({
      next: (lista) => { this.comentarios = lista; this.cd.detectChanges(); }
    });
  }

  publicarComentario() {
    if (!this.nuevoComentario.trim() || !this.zona) return;
    if (!this.usuarioService.isLoggedIn()) { alert('Inicia sesión'); return; }

    const idUsuario = parseInt(localStorage.getItem('usuarioId') || '0');
    this.enviandoComentario = true;

    this.zonaService.enviarComentario(this.zona.idZona, idUsuario, this.nuevoComentario).subscribe({
        next: () => {
          this.nuevoComentario = '';
          this.enviandoComentario = false;
          this.cargarComentarios(this.zona!.idZona);
        },
        error: () => { this.enviandoComentario = false; }
    });
  }

  calificar(puntos: number) {
    this.puntuacionSeleccionada = puntos;
    if (!this.usuarioService.isLoggedIn()) { alert('Inicia sesión'); return; }
    const idUsuario = parseInt(localStorage.getItem('usuarioId') || '0');

    if (this.zona) {
      this.enviandoCalificacion = true;
      this.zonaService.calificarZona(this.zona.idZona, idUsuario, puntos).subscribe({
        next: () => {
          this.mensajeCalificacion = '¡Gracias por tu calificación!';
          this.enviandoCalificacion = false;
          this.cd.detectChanges();
        },
        error: () => { this.enviandoCalificacion = false; }
      });
    }
  }
}