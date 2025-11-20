import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  public usuarioService = inject(UsuarioService);
  private router = inject(Router);

  usuarioNombre: string | null = null;

  ngOnInit(): void {
    // Verificar si hay usuario logueado recuperando el nombre
    this.usuarioNombre = localStorage.getItem('usuarioNombre');
  }

  logout(): void {
    this.usuarioService.logout();
    this.usuarioNombre = null;
    // Opcional: Recargar para limpiar estados
    // window.location.reload();
  }

  // >>> NUEVA FUNCIÓN 1: LOGO INTELIGENTE <<<
  clickLogo() {
    if (this.usuarioNombre) {
      // Si está logueado, el logo actúa como acceso al Dashboard
      this.router.navigate(['/dashboard']);
    } else {
      // Si no, te lleva al inicio (o recarga el home)
      this.router.navigate(['/']);
    }
  }

  // >>> NUEVA FUNCIÓN 2: BOTÓN EMPEZAR <<<
  clickEmpezar() {
    if (this.usuarioNombre) {
      // Si ya entró, va directo a la acción
      this.router.navigate(['/dashboard']);
    } else {
      // Si es un invitado, lo invitamos a loguearse primero
      this.router.navigate(['/login']);
    }
  }
}