import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
// ¡CRÍTICO! Estos imports corrigen los errores NG8003 y NG8002
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; // Necesario para *ngIf

// Importa tus servicios y modelos (Asegúrate de que estas rutas sean correctas)
// Si ves errores de compilación después de pegar esto, la causa es que falta uno de estos 3 archivos.
import { UsuarioService } from '../../core/services/usuario.service';
import { LoginRequest } from '../../core/models/login-request.model';
import { LoginResponse } from '../../core/models/login-response.model';

@Component({
  selector: 'app-login',
  standalone: true, 
  // ¡CRÍTICO! Aquí se declaran los módulos para que el template reconozca ngForm, ngModel y *ngIf
  imports: [CommonModule, FormsModule, RouterLink], 
  
  templateUrl: './login.component.html', 
  styleUrls: ['./login.component.css'] 
})
export class LoginComponent implements OnInit {
  // Inyección de servicios usando la función inject()
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  // Propiedades de estado de la UI
  isSubmitting = false; 
  errorMessage: string | null = null; 

  // Objeto de datos del formulario (se conecta con [(ngModel)] en el HTML)
  loginData: LoginRequest = {
    correo: '', 
    contrasena: '' 
  };

  ngOnInit(): void {
    // Redirige al dashboard si el usuario ya está logueado
    if (this.usuarioService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  /**
   * Maneja el envío del formulario de login.
   */
  login(): void {
    // Validación básica
    if (!this.loginData.correo || !this.loginData.contrasena) {
      this.errorMessage = 'Por favor, ingrese su correo y contraseña.';
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = null;

    // Llama a tu servicio de autenticación
    this.usuarioService.login(this.loginData).subscribe({
      next: (response: LoginResponse) => {
        console.log('Login exitoso, redirigiendo a /dashboard.');
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.isSubmitting = false;
        // Muestra un mensaje de error si la conexión falla o las credenciales son incorrectas
        const errorMsg = error.error?.message || 'Credenciales incorrectas. Inténtalo de nuevo.';
        this.errorMessage = errorMsg;
        console.error('Error en el login:', error);
      },
      complete: () => {
        this.isSubmitting = false; 
      }
    });
  }
}