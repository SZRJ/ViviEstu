import { Routes, Router, CanActivateFn } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { inject } from '@angular/core';
import { UsuarioService } from './core/services/usuario.service'; 

// Guard para proteger las rutas: solo permite el acceso si el usuario está logueado
const authGuard: CanActivateFn = () => {
    const usuarioService = inject(UsuarioService);
    const router = inject(Router);
    
    // Si la señal de token es verdadera, permite el acceso.
    if (usuarioService.isLoggedIn()) {
        return true;
    } else {
        // Si no hay token, redirige al login
        return router.navigate(['/login']);
    }
};

export const routes: Routes = [
    { 
      path: 'login', 
      component: LoginComponent 
    },
    { 
      path: 'dashboard', 
      component: DashboardComponent, 
      canActivate: [authGuard] // Aplicar la protección aquí
    }, 
    // Redirigir la ruta raíz a dashboard
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    // Manejar cualquier otra ruta
    { path: '**', redirectTo: '/dashboard' }
];