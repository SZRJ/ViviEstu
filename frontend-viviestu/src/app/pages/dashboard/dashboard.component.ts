import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { ZonaService } from '../../core/services/zona.service'; // <--- IMPORTANTE

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  
  private usuarioService = inject(UsuarioService);
  private zonaService = inject(ZonaService); // <--- Inyectamos ZonaService
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef);

  usuarioNombre: string = '';
  
  // MENÚS
  showUserMenu = false;
  showNotifMenu = false; // <--- Faltaba esto

  // DATOS
  resumen: any = null; 
  numNotificaciones = 0; // <--- Faltaba esto

  ngOnInit(): void {
    this.usuarioNombre = localStorage.getItem('usuarioNombre') || 'Estudiante';
    const idStr = localStorage.getItem('usuarioId');
    
    if (idStr) {
        const id = parseInt(idStr);
        this.cargarResumen(id);
        this.cargarNotificaciones(id);
    }
  }

  cargarResumen(id: number) {
    this.usuarioService.obtenerResumen(id).subscribe({
      next: (data) => {
        this.resumen = data;
        this.cd.detectChanges();
      },
      error: (err) => console.error('Error resumen:', err)
    });
  }

  cargarNotificaciones(id: number) {
    // Usamos listarRecomendadas para saber cuántas hay
    this.zonaService.listarRecomendadas(id).subscribe({
        next: (data) => {
            this.numNotificaciones = data ? data.length : 0;
            this.cd.detectChanges();
        }
    });
  }

  // --- FUNCIONES DEL HTML QUE FALTABAN ---

  toggleUserMenu(event: Event) {
    event.stopPropagation();
    this.showNotifMenu = false; // Cerramos notificaciones si abrimos usuario
    this.showUserMenu = !this.showUserMenu;
  }

  toggleNotifMenu(event: Event) {
    event.stopPropagation();
    this.showUserMenu = false; // Cerramos usuario si abrimos notificaciones
    this.showNotifMenu = !this.showNotifMenu;
  }

  closeMenu() { 
    this.showUserMenu = false; 
    this.showNotifMenu = false;
  }

  irANotificacion() {
    this.router.navigate(['/zonas']); // Te lleva a ver las zonas
  }

  irAlPerfil() { this.router.navigate(['/profile']); }
  irADesactivarCuenta() { this.router.navigate(['/profile']); }
  
  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }
}