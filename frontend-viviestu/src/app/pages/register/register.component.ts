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
  idRegistrado: number | null = null;

  registroData: RegistroRequest = {
    nombre: '',
    nombreUsuario: '',
    fechaNacimiento: '',
    correo: '',
    contrasena: ''
  };

  repetirContrasena: string = '';
  isVerifying = false;

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
      next: (response: any) => {
        this.isSubmitting = false;
        
        // 1. CAPTURAMOS EL ID DEL BACKEND
        // Dependiendo de cómo responda tu backend, el ID puede venir en 'data' o directo.
        const usuarioCreado = response.data || response; 
        
        if (usuarioCreado && (usuarioCreado.idUsuario || usuarioCreado.id)) {
             this.idRegistrado = usuarioCreado.idUsuario || usuarioCreado.id;
             
             // 2. MOSTRAMOS EL MODAL (Ahora será el de "Verificación pendiente")
             this.showSuccessModal = true;
             this.cd.detectChanges();
        } else {
             this.errorMessage = "Usuario creado, pero no se recibió el ID para verificar.";
        }
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || 'Error al registrar.';
        this.cd.detectChanges();
      }
    });
  }

  simularVerificacion() {
    // Verificamos que tengamos un ID guardado
    if (!this.idRegistrado) {
      console.error('No hay ID registrado para verificar');
      return;
    }

    this.isVerifying = true;

    // Llamamos al servicio para activar la cuenta
    this.usuarioService.verificarCuenta(this.idRegistrado).subscribe({
      next: () => {
        // Si todo sale bien:
        alert('¡Cuenta verificada con éxito! Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        // Si falla:
        console.error(err);
        alert('Error al verificar. Intenta de nuevo.');
        this.isVerifying = false;
      }
    });
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }

}
