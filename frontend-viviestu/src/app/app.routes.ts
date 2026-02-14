import { Routes, Router, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';

// --- Importaciones de Componentes ---
// Correcto: Apunta directo a pages/login según tu estructura actual
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';

// Si aún no creas el registro, mantén esta línea comentada o bórrala
 import { RegisterComponent } from './pages/register/register.component'; 

import { UsuarioService } from './core/services/usuario.service'; 
import { UserProfileComponent } from './pages/user-profile/user-profile.component'; 
import { EditProfileComponent } from './pages/edit-profile/edit-profile.component';


import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { ZonaListComponent } from './pages/zona-list/zona-list.component';
import { ZonaDetailComponent } from './pages/zona-detail/zona-detail.component'; 

import { MisFavoritosComponent } from './pages/favoritos/mis-favoritos.component'; 
import { SimuladorComponent } from './pages/simulador/simulador.component';

const authGuard: CanActivateFn = () => {
    const usuarioService = inject(UsuarioService);
    const router = inject(Router);
    
    if (usuarioService.isLoggedIn()) {
        return true;
    } else {
        return router.navigate(['/login']);
    }
};

export const routes: Routes = [
    { 
      path: '', 
      component: HomeComponent,
      pathMatch: 'full'
    },
    { 
      path: 'login', 
      component: LoginComponent 
    },
    
    // NOTA: Como tienes comentado el registro, si das clic en "Registrarse" 
    // en el Home, el wildcard de abajo (**) te devolverá al Home.
    
    { 
      path: 'register', 
      component: RegisterComponent 
    },
    
    { path: 'profile', component: UserProfileComponent },

    /*
    { 
      path: 'dashboard', 
      component: DashboardComponent, 
      canActivate: [authGuard] 
    },
    */

    // NUEVAS RUTAS
    { path: 'dashboard', component: DashboardComponent }, // Panel principal
    { path: 'editar-perfil', component: EditProfileComponent },
    { path: 'zonas', component: ZonaListComponent },      // Listado general
    { path: 'zonas/:id', component: ZonaDetailComponent },
    { path: 'favoritos', component: MisFavoritosComponent },
    { path: 'simulador', component: SimuladorComponent },

    // Wildcard: Cualquier ruta desconocida devuelve al Home
    { path: '**', redirectTo: '' }
];
