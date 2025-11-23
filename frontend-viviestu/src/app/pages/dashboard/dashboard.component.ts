import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  usuarioService = inject(UsuarioService);
  router = inject(Router);

  showUserMenu = false;

  toggleUserMenu(event: MouseEvent) {
    event.stopPropagation();
    this.showUserMenu = !this.showUserMenu;
  }

  irAlPerfil(event?: MouseEvent) {
    if (event) event.stopPropagation();
    this.showUserMenu = false;
    this.router.navigate(['/editar-perfil']);
  }

  irADesactivarCuenta(event?: MouseEvent) {
    if (event) event.stopPropagation();
    this.showUserMenu = false;
    this.router.navigate(['/profile']);
  }

  logout(event?: MouseEvent) {
    if (event) event.stopPropagation();
    this.usuarioService.logout();
    this.showUserMenu = false;
    this.router.navigate(['/']);
  }
}
