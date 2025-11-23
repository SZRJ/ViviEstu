import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
// Imports necesarios para formularios y directivas
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

// Servicios y Modelos
import { UsuarioService } from '../../core/services/usuario.service';
import { LoginRequest } from '../../core/models/login-request.model';
// Nota: Aunque importamos LoginResponse, usaremos 'any' abajo para evitar errores si el backend cambia
import { LoginResponse } from '../../core/models/login-response.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], 
  templateUrl: './login.component.html', 
  styleUrls: ['./login.component.css'] 
})
export class LoginComponent implements OnInit {
  
  // Inyección de dependencias
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  // Estado de la UI
  isSubmitting = false; 
  errorMessage: string | null = null; 

  // Modelo de datos del formulario
  loginData: LoginRequest = {
    correo: '', 
    contrasena: '' 
  };

  ngOnInit(): void {
    // Si el usuario ya está logueado, lo mandamos al Home ('/')
    if (this.usuarioService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  login(): void {
    // Validación simple
    if (!this.loginData.correo || !this.loginData.contrasena) {
      this.errorMessage = 'Por favor, ingrese su correo y contraseña.';
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = null;

    this.usuarioService.login(this.loginData).subscribe({
      // USO DE 'any': Esto evita que la app se rompa si 'userInfo' no viene en la respuesta
      next: (response: any) => {
        
        // DEBUG: Mira esto en la consola (F12) para ver qué manda realmente tu backend
        console.log('✅ RESPUESTA RECIBIDA DEL BACKEND:', response);
    
        // 1. EXTRACCIÓN SEGURA DE DATOS (BLINDAJE)
        // Intenta leer userInfo.nombre, si falla, busca response.nombre, si falla, usa el correo.
        const nombreUsuario = response.userInfo?.nombre || response.nombre || this.loginData.correo;
        
        // Intenta leer el token (jwt o token)
        const token = response.jwt || response.token;

        // 2. GUARDAR EN LOCALSTORAGE
        if (nombreUsuario) {
            localStorage.setItem('usuarioNombre', nombreUsuario);
        }
        
        if (token) {
            localStorage.setItem('authToken', token);
        }
        
        // Guardamos el ID solo si existe, usando ?. para que no de error si userInfo es null
        if (response.userInfo?.idUsuario) {
           localStorage.setItem('usuarioId', response.userInfo.idUsuario.toString());
        }

        // 3. REDIRIGIR AL HOME
        console.log('Redirigiendo al Home...');
        this.router.navigate(['/']); 
      },
      error: (error) => {
        this.isSubmitting = false;
        // Manejo de errores del backend
        const errorMsg = error.error?.message || 'Credenciales incorrectas o error en el servidor.';
        this.errorMessage = errorMsg;
        console.error('❌ Error en el login:', error);
      },
      complete: () => {
        this.isSubmitting = false; 
      }
    });
  }
}