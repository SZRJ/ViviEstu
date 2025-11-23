import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  mensajeOk: string | null = null;
  mensajeError: string | null = null;
  isSaving = false;

  usuarioId = 0;

  // Objeto para el formulario
  datos: any = {
    nombre: '',
    nombreUsuario: '',
    correo: '',
    fechaNacimiento: '', // <--- IMPORTANTE: Agregamos esto
    contrasena: ''
  };

  ngOnInit(): void {
    this.cargarDatosUsuario();
  }

  cargarDatosUsuario() {
    const idStr = localStorage.getItem('usuarioId');
    if (!idStr) {
      this.router.navigate(['/login']);
      return;
    }
    this.usuarioId = parseInt(idStr, 10);

    // Intentar obtener datos
    let info = this.usuarioService.userInfo();

    // Si la señal está vacía, buscamos en localStorage
    if (!info) {
      const infoStr = localStorage.getItem('user_info');
      if (infoStr) {
        try {
          info = JSON.parse(infoStr);
        } catch (e) { console.error('Error parseando info', e); }
      }
    }

    if (info) {
      console.log('📝 Datos recuperados:', info);
      this.datos.nombre = info.nombre || '';
      this.datos.nombreUsuario = info.nombreUsuario || '';
      // El backend suele devolver 'email' o 'correo', aseguramos ambos
      this.datos.correo = info.correo || info.email || ''; 
      // Recuperamos la fecha para reenviarla
      this.datos.fechaNacimiento = info.fechaNacimiento || ''; 
    }
  }

  guardarCambios() {
    this.isSaving = true;
    this.mensajeError = null;
    this.mensajeOk = null;

    // PREPARAMOS EL PAQUETE PARA EL BACKEND
    // Corrección: Usamos 'correo' en vez de 'email' y enviamos fechaNacimiento
    const payload: any = {
      idUsuario: this.usuarioId, // A veces el backend pide el ID dentro del body
      nombre: this.datos.nombre,
      nombreUsuario: this.datos.nombreUsuario,
      correo: this.datos.correo, 
      fechaNacimiento: this.datos.fechaNacimiento 
    };

    // Solo mandamos contraseña si escribieron algo
    if (this.datos.contrasena && this.datos.contrasena.trim().length >= 6) {
      payload.contrasena = this.datos.contrasena.trim();
    }

    console.log('🚀 Enviando al Backend:', payload);

    this.usuarioService.actualizarUsuario(this.usuarioId, payload).subscribe({
      next: (resp) => {
        console.log('✅ Éxito:', resp);
        this.isSaving = false;
        this.mensajeOk = '¡Datos actualizados correctamente!';
        
        // Actualizamos el nombre en el navbar para que se vea el cambio
        if (payload.nombreUsuario) {
            localStorage.setItem('usuarioNombre', payload.nombreUsuario);
            // Opcional: Actualizar la señal en el servicio si quieres
        }
        
        setTimeout(() => this.router.navigate(['/dashboard']), 1500);
      },
      error: (err) => {
        console.error('❌ Error Backend:', err);
        this.isSaving = false;
        
        // Manejo específico del error 500
        if (err.status === 500) {
           this.mensajeError = 'Error interno del servidor. Revisa los datos enviados.';
        } else if (err.status === 403) {
           this.mensajeError = 'Tu sesión ha expirado. Vuelve a iniciar sesión.';
        } else {
           this.mensajeError = err.error?.message || 'No se pudieron guardar los cambios.';
        }
      }
    });
  }

  cancelar() {
    this.router.navigate(['/dashboard']);
  }
}