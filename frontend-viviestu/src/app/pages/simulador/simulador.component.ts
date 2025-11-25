import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core'; // <--- 1. IMPORTAR ESTO
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
  private cd = inject(ChangeDetectorRef); // <--- 2. INYECTAR EL DETECTOR

  alquiler: number | null = null;
  transporte: number | null = null;
  
  misFavoritos: any[] = [];
  favoritoSeleccionado: number | null = null;

  resultado: any = null;
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
        console.log('Favoritos cargados:', data);
        this.misFavoritos = data;
        
        // 3. ¡DESPERTAR A ANGULAR!
        // Esto obliga a que el *ngIf detecte los datos y muestre el selector YA.
        this.cd.detectChanges(); 
      },
      error: (err) => console.error(err)
    });
  }

  onSeleccionarFavorito() {
    const zonaEncontrada = this.misFavoritos.find(f => f.idZona == this.favoritoSeleccionado);
    if (zonaEncontrada) {
      this.alquiler = zonaEncontrada.precioPromedio;
    }
  }

  calcular() {
    if (!this.alquiler || this.alquiler <= 0) {
      alert('Ingresa un monto de alquiler válido.');
      return;
    }

    this.loading = true;
    const costoTransporte = this.transporte || 0;

    this.usuarioService.simularGasto(this.alquiler, costoTransporte).subscribe({
      next: (data) => {
        this.resultado = data;
        this.loading = false;
        this.cd.detectChanges(); // Actualizar también al mostrar el resultado
      },
      error: (err) => {
        console.error(err);
        alert('Error al calcular.');
        this.loading = false;
      }
    });
  }
}