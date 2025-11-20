import { Component, inject, ChangeDetectorRef } from '@angular/core'; // <--- Importamos ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { RegistroRequest } from '../../core/models/registro-request.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private cd = inject(ChangeDetectorRef); // <--- Inyectamos el detector de cambios

  isSubmitting = false;
  errorMessage: string | null = null;
  showSuccessModal = false; 

  registroData: RegistroRequest = {
    nombre: '',
    nombreUsuario: '',
    fechaNacimiento: '',
    correo: '',
    contrasena: ''
  };

  repetirContrasena: string = '';

  onRegister(): void {
    // Validaciones
    if (this.registroData.contrasena !== this.repetirContrasena) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    if (!this.registroData.nombre || !this.registroData.correo) {
      this.errorMessage = 'Por favor completa todos los campos.';
      return;
    }
    
    this.isSubmitting = true;
    this.errorMessage = null;

    console.log('Enviando datos...', this.registroData);

    this.usuarioService.registrar(this.registroData).subscribe({
      next: (response) => {
        console.log('✅ COMPONENTE: Respuesta recibida', response);
        
        // 1. Detener la carga
        this.isSubmitting = false;
        
        // 2. Mostrar el modal
        this.showSuccessModal = true;

        // 3. FORZAR ACTUALIZACIÓN DE LA PANTALLA (El truco)
        this.cd.detectChanges(); 
      },
      error: (error) => {
        this.isSubmitting = false;
        console.error('❌ Error:', error);
        this.errorMessage = error.error?.message || 'Error al registrar.';
        this.cd.detectChanges(); // Forzar actualización en error también
      }
    });
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }
}