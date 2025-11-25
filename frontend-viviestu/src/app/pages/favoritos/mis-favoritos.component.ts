import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core'; // <--- Importar ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
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
  private cd = inject(ChangeDetectorRef); // <--- Inyectamos el Despertador

  favoritos: any[] = [];
  loading = true;
  usuarioNombre = localStorage.getItem('usuarioNombre') || 'Usuario';

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
        
        this.cd.detectChanges(); // <--- ¡DESPIERTA A ANGULAR!
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.cd.detectChanges(); // También actualizar si hay error
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

  cerrarSesion() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }

  getImagenRandom(id: number) {
    return `https://picsum.photos/seed/${id}/400/250`;
  }
}