import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <header class="topbar">
      <div class="brand">ViviEstu</div>

      <nav class="main-nav">
        <a href="#inicio">Inicio</a>
        <a href="#favoritos">Favoritos</a>
        <a href="#mas">Más</a>
        <a href="#nosotros">Nosotros</a>
      </nav>

      <div class="auth">
        <a class="link" href="#login">Iniciar Sesión</a>
        <a class="btn" href="#register">Registrarse</a>
      </div>
    </header>

    <main class="app-shell">
      <section id="inicio" class="hero card">
        <h1>Encuentra tu lugar ideal cerca de la universidad</h1>
        <p>Porque estudiar también es elegir donde vivir</p>
      </section>

      <section id="favoritos" class="card">
        <h2>Favoritos</h2>
        <app-favoritos></app-favoritos>
      </section>

      <section id="mas" class="card">
        <h2>Más</h2>
        <p>Contenido adicional...</p>
      </section>

      <section id="nosotros" class="card">
        <h2>Nosotros</h2>
        <p>Información sobre el equipo.</p>
      </section>
    </main>
  `
})
export class AppComponent {}
