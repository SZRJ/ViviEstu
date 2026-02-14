import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ZonaService } from '../../core/services/zona.service';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-mis-favoritos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-favoritos.component.html',
  styleUrls: ['./mis-favoritos.component.css']
})
export class MisFavoritosComponent implements OnInit {
  
  private zonaService = inject(ZonaService);
  public usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef);

  favoritos: any[] = [];
  loading = true;
  
  // VARIABLES DEL MENÚ (Esto es lo que faltaba)
  showUserMenu = false; 

  ngOnInit() {
    if (!this.usuarioService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargarFavoritos();
  }

  cargarFavoritos() {
    const idUsuario = parseInt(localStorage.getItem('usuarioId')!);
    
    this.zonaService.listarFavoritos(idUsuario).subscribe({
      next: (data) => {
        console.log('Favoritos cargados:', data);
        this.favoritos = data; 
        this.loading = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cd.detectChanges();
      }
    });
  }

  quitarFavorito(event: Event, idZona: number) {
    event.stopPropagation();
    event.preventDefault();

    if(!confirm('¿Ya no te interesa esta zona? Se quitará de tus favoritos.')) return;

    const idUsuario = parseInt(localStorage.getItem('usuarioId')!);
    const backup = [...this.favoritos];
    this.favoritos = this.favoritos.filter(f => f.idZona !== idZona);

    this.zonaService.eliminarFavorito(idUsuario, idZona).subscribe({
        error: () => {
            alert('Error al eliminar favorito');
            this.favoritos = backup;
        }
    });
  }

  getImagenRandom(id: number) {
    return `https://picsum.photos/seed/${id}/400/250`;
  }

  // --- FUNCIONES DEL MENÚ NAVBAR (ESTO FALTABA) ---
  
  toggleUserMenu(event: Event) {
    event.stopPropagation();
    this.showUserMenu = !this.showUserMenu;
  }

  irAlPerfil() { 
    this.router.navigate(['/profile']); 
  }
  
  irADesactivarCuenta() { 
    this.router.navigate(['/profile']); 
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }
}