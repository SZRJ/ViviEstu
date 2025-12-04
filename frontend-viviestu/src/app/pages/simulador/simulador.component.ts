import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { ZonaService } from '../../core/services/zona.service';

@Component({
  selector: 'app-simulador',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './simulador.component.html',
  styleUrls: ['./simulador.component.css']
})
export class SimuladorComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private zonaService = inject(ZonaService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef);

  // INPUTS
  alquiler: number | null = null;
  transporte: number | null = null;
  
  // NUEVOS CAMPOS
  comida: number | null = null;
  extras: number | null = null;
  meses: number = 1; // Ciclo académico (ej: 5 meses)

  misFavoritos: any[] = [];
  favoritoSeleccionado: number | null = null;

  resultado: any = null;
  granTotal: number = 0;
  loading = false;

  ngOnInit() {
    if (!this.usuarioService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargarFavoritos();
  }

  cargarFavoritos() {
    const idUsuario = parseInt(localStorage.getItem('usuarioId') || '0');
    this.zonaService.listarFavoritos(idUsuario).subscribe({
      next: (data) => {
        this.misFavoritos = data;
        this.cd.detectChanges();
      }
    });
  }

  onSeleccionarFavorito() {
    const zona = this.misFavoritos.find(f => f.idZona == this.favoritoSeleccionado);
    if (zona) this.alquiler = zona.precioPromedio;
  }

  calcular() {
    if (!this.alquiler || this.alquiler <= 0) {
      alert('Ingresa un monto de alquiler válido.');
      return;
    }
    this.loading = true;
    const costoTransporte = this.transporte || 0;

    // 1. Backend calcula lo básico
    this.usuarioService.simularGasto(this.alquiler, costoTransporte).subscribe({
      next: (data) => {
        this.resultado = data;
        
        // 2. Frontend suma los extras
        const gastoMensual = data.gastoTotal + (this.comida || 0) + (this.extras || 0);
        this.granTotal = gastoMensual * this.meses;

        this.loading = false;
        this.cd.detectChanges();
      },
      error: () => { this.loading = false; alert('Error al calcular'); }
    });
  }
}