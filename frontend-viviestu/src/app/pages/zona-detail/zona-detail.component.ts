import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { ZonaService } from '../../core/services/zona.service';
import { UsuarioService } from '../../core/services/usuario.service'; // <--- Importamos UsuarioService
import { Zona } from '../../core/models/zona.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-zona-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './zona-detail.component.html',
  styleUrls: ['./zona-detail.component.css']
})
export class ZonaDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router); // <--- Router para navegar
  private zonaService = inject(ZonaService);
  public usuarioService = inject(UsuarioService); // <--- Público para el HTML
  private cd = inject(ChangeDetectorRef); // <--- EL DETECTOR DE CAMBIOS

  zona: Zona | null = null;
  loading = true;
  showUserMenu = false; // <--- Para el menú desplegable
  
  // VARIABLES PARA COMENTARIOS
  comentarios: any[] = [];
  nuevoComentario: string = '';
  enviandoComentario = false;

  puntuacionSeleccionada = 0;
  mensajeCalificacion = '';
  enviandoCalificacion = false;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    
    if (id) {
      this.zonaService.obtenerPorId(parseInt(id)).subscribe({
        next: (data) => {
          this.zona = data;
          this.loading = false;
          // 👇 ESTO ARREGLA QUE SE QUEDE CARGANDO 👇
          this.cd.detectChanges(); 
        },
        error: (err) => {
          console.error(err);
          this.loading = false;
          this.cd.detectChanges();
        }
      });
      this.cargarComentarios(parseInt(id));
    }
  }

  getImagenRandom(id: number, variant: number) {
    return `https://picsum.photos/seed/${id + variant}/800/600`;
  }

  // --- LÓGICA DEL NAVBAR (Idéntica al Dashboard) ---
  toggleUserMenu(event: Event) {
    event.stopPropagation();
    this.showUserMenu = !this.showUserMenu;
  }

  irAlPerfil() {
    this.router.navigate(['/editar-perfil']);
  }
  
  irADesactivarCuenta() {
    this.router.navigate(['/profile']);
  }

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }

  cargarComentarios(idZona: number) {
    this.zonaService.obtenerComentarios(idZona).subscribe({
      next: (lista) => {
        this.comentarios = lista;
        this.cd.detectChanges();
      }
    });
  }

  publicarComentario() {
    if (!this.nuevoComentario.trim()) return; // No enviar vacíos
    if (!this.zona) return;

    // Verificar si está logueado
    if (!this.usuarioService.isLoggedIn()) {
      alert('Debes iniciar sesión para comentar.');
      this.router.navigate(['/login']);
      return;
    }

    const idUsuario = parseInt(localStorage.getItem('usuarioId') || '0');
    this.enviandoComentario = true;

    this.zonaService.enviarComentario(this.zona.idZona, idUsuario, this.nuevoComentario)
      .subscribe({
        next: (resp) => {
          // Limpiamos el input
          this.nuevoComentario = '';
          this.enviandoComentario = false;
          
          // Recargamos la lista para que aparezca el nuevo
          this.cargarComentarios(this.zona!.idZona);
        },
        error: (err) => {
          console.error(err);
          alert('Error al publicar comentario');
          this.enviandoComentario = false;
        }
      });
  }

  calificar(puntos: number) {
    // 1. Efecto visual inmediato
    this.puntuacionSeleccionada = puntos;

    // 2. Verificar sesión
    if (!this.usuarioService.isLoggedIn()) {
      alert('Inicia sesión para calificar.');
      return;
    }

    // 3. Obtener ID del usuario
    const idUsuarioStr = localStorage.getItem('usuarioId');
    const idUsuario = idUsuarioStr ? parseInt(idUsuarioStr) : 0;

    if (this.zona && idUsuario > 0) {
      this.enviandoCalificacion = true;

      // 4. LLAMADA REAL AL BACKEND
      this.zonaService.calificarZona(this.zona.idZona, idUsuario, puntos).subscribe({
        next: (resp) => {
          console.log('Calificación guardada:', resp);
          this.mensajeCalificacion = '¡Gracias por tu calificación!';
          this.enviandoCalificacion = false;
        },
        error: (err) => {
          console.error('Error al calificar:', err);
          this.mensajeCalificacion = 'Error al guardar la calificación.';
          this.enviandoCalificacion = false;
        }
      });
    }
  }
}
