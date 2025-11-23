import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <header class="topbar">
      <div class="brand">ViviEstu</div>

      <nav class="main-nav">
        <a routerLink="/">Inicio</a>
        <a routerLink="/favoritos">Favoritos</a>
        <a routerLink="/simulador">Simulador</a>
        <a routerLink="/mas">Más</a>
        <a routerLink="/nosotros">Nosotros</a>
      </nav>

      <div class="auth">
        <a class="link" href="#login">Iniciar Sesión</a>
        <a class="btn" href="#register">Registrarse</a>
      </div>
    </header>

    <router-outlet></router-outlet>
  `
})
export class AppComponent {}
