import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UsuarioService } from './core/services/usuario.service'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  template: `
    <header class="bg-indigo-600 text-white p-4 shadow-xl">
      <nav class="flex justify-between items-center max-w-6xl mx-auto">
        <a routerLink="/dashboard" class="text-2xl font-extrabold tracking-wider hover:text-indigo-200 transition">
          ViviEstu
        </a>
        <div class="flex items-center space-x-4">
          @if (usuarioService.isLoggedIn()) {
            <span class="text-sm font-light hidden sm:inline">
              Hola, {{ usuarioService.userInfo()?.nombre }}
            </span>
            <button (click)="logout()" 
                    class="px-4 py-2 bg-red-500 rounded-lg font-semibold hover:bg-red-600 transition shadow">
              Cerrar Sesión
            </button>
          } @else {
            <a routerLink="/login" 
               class="px-4 py-2 bg-indigo-500 rounded-lg font-semibold hover:bg-indigo-700 transition shadow">
              Login
            </a>
          }
        </div>
      </nav>
    </header>
    <main class="container mx-auto p-4">
      <!-- Aquí se cargan los componentes de ruta (Login o Dashboard) -->
      <router-outlet></router-outlet>
    </main>
  `,
})
export class AppComponent {
  // Inyección del servicio de autenticación para usar su estado
  usuarioService = inject(UsuarioService);
  private router = inject(Router);

  logout() {
    this.usuarioService.logout();
    this.router.navigate(['/login']);
  }
}