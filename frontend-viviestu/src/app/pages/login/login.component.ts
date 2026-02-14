import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core'; // <--- 1. Importar ChangeDetectorRef
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

import { UsuarioService } from '../../core/services/usuario.service';
import { LoginRequest } from '../../core/models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef); // <--- 2. Inyectar el detector

  isSubmitting = false; 
  errorMessage: string | null = null; 

  loginData: LoginRequest = {
    correo: '', 
    contrasena: '' 
  };

  ngOnInit(): void {
    if (this.usuarioService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  login(): void {
    if (!this.loginData.correo || !this.loginData.contrasena) {
      this.errorMessage = 'Por favor, ingrese su correo y contraseña.';
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = null;

    this.usuarioService.login(this.loginData).subscribe({
      next: (response: any) => {
        console.log('✅ LOGIN EXITOSO:', response);
    
        // Lógica de extracción de datos (Blindada)
        const datosReales = response.data || response;
        const token = datosReales.jwt || datosReales.token;
        
        if (token) localStorage.setItem('auth_token', token);

        const info = datosReales.userInfo || datosReales.usuario || datosReales;
        localStorage.setItem('user_info', JSON.stringify(info));
        
        const idUsuario = info.idUsuario || info.id;
        if (idUsuario) localStorage.setItem('usuarioId', idUsuario.toString());
        
        const nombreUsuario = info.nombre || info.nombreUsuario || this.loginData.correo;
        localStorage.setItem('usuarioNombre', nombreUsuario);

        this.router.navigate(['/dashboard']); 
      },
      error: (err) => {
        console.error('❌ Error detectado:', err);

        // 3. LOGICA DE RECUPERACIÓN
        this.isSubmitting = false; // Desbloquear botón
        this.loginData.contrasena = ''; // Limpiar contraseña
        
        // Extraer mensaje del backend (Soporta message o mensaje)
        const msgBackend = err.error?.message || err.error?.mensaje;
        this.errorMessage = msgBackend || 'Credenciales incorrectas. Inténtalo de nuevo.';
        
        // 4. ¡DESPERTAR A ANGULAR!
        this.cd.detectChanges(); 
      }
    });
  }
}