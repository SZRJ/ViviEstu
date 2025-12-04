import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { Preference } from '../../core/models/preference.model';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  
  public usuarioService = inject(UsuarioService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  usuarioNombre: string = '';
  usuarioId: number = 0;
  isLoadingPrefs = true;
  showUserMenu = false; // <--- PARA EL NAVBAR

  misPreferencias: Preference = {
    idPreferencia: undefined,
    idUsuario: 0,
    presupuesto: null as any,
    seguridad: '',
    tiempoMax: null as any,
    transporte: '',
    universidad: ''
  };

  showDeleteModal = false;
  isSaving = false;
  mensaje: string | null = null;

  ngOnInit(): void {
    this.recuperarSesion();
  }

  recuperarSesion() {
    let idStr = localStorage.getItem('usuarioId');
    if (!idStr) {
        // (Lógica de recuperación de respaldo...)
        const userInfoStr = localStorage.getItem('user_info');
        if (userInfoStr) {
             try {
                 const u = JSON.parse(userInfoStr);
                 if (u.idUsuario || u.id) idStr = (u.idUsuario || u.id).toString();
             } catch(e){}
        }
    }

    if (idStr) {
      this.usuarioId = parseInt(idStr, 10);
      this.usuarioNombre = localStorage.getItem('usuarioNombre') || 'Usuario';
      this.misPreferencias.idUsuario = this.usuarioId;
      this.cargarPreferenciasExistentes();
    } else {
      this.router.navigate(['/login']);
    }
  }

  cargarPreferenciasExistentes() {
    this.usuarioService.obtenerPreferencias(this.usuarioId).subscribe({
      next: (resp: any) => {
        const lista = resp.data || resp;
        const prefs = Array.isArray(lista) && lista.length ? lista[0] : null;

        if (prefs) {
          this.misPreferencias = { ...prefs, idUsuario: this.usuarioId };
        }
        this.isLoadingPrefs = false;
        this.cdr.detectChanges();
      },
      error: () => { this.isLoadingPrefs = false; }
    });
  }

  guardarCambios() {
    this.isSaving = true;
    this.misPreferencias.idUsuario = this.usuarioId;

    // Datos limpios para enviar
    const datosLimpios = {
      idUsuario: this.misPreferencias.idUsuario,
      universidad: this.misPreferencias.universidad,
      presupuesto: this.misPreferencias.presupuesto,
      transporte: this.misPreferencias.transporte,
      tiempoMax: this.misPreferencias.tiempoMax,
      seguridad: this.misPreferencias.seguridad
    };

    if (this.misPreferencias.idPreferencia) {
      // ACTUALIZAR
      this.usuarioService.actualizarPreferencia(this.misPreferencias.idPreferencia, datosLimpios as any)
        .subscribe({
          next: () => this.manejarExito('¡Preferencias actualizadas!'),
          error: (err) => this.manejarError(err)
        });
    } else {
      // CREAR
      this.usuarioService.crearPreferencia(datosLimpios as any)
        .subscribe({
          next: (resp: any) => {
            const data = resp.data || resp;
            if (data?.idPreferencia) this.misPreferencias.idPreferencia = data.idPreferencia;
            this.manejarExito('¡Preferencias creadas!');
          },
          error: (err) => this.manejarError(err)
        });
    }
  }

  manejarExito(texto: string) {
    this.isSaving = false;
    this.mensaje = `${texto} Volviendo al Dashboard...`; // Mensaje claro
    this.cdr.detectChanges();
    
    setTimeout(() => {
      this.mensaje = null;
      // CORRECCIÓN: Redirigir al Dashboard
      this.router.navigate(['/dashboard']); 
    }, 2000);
  }

  manejarError(err: any) {
    this.isSaving = false;
    alert('Error al guardar. Intenta nuevamente.');
  }

  // --- LÓGICA NAVBAR ---
  toggleUserMenu(event: Event) {
    event.stopPropagation();
    this.showUserMenu = !this.showUserMenu;
  }
  logout() { this.usuarioService.logout(); this.router.navigate(['/']); }
  irAlPerfil() { this.showUserMenu = false; } // Ya estás en perfil
  irADesactivarCuenta() { this.showDeleteModal = true; this.showUserMenu = false; }

  // --- ELIMINAR CUENTA ---
  confirmarEliminacion() { this.showDeleteModal = true; }
  cancelarEliminacion() { this.showDeleteModal = false; }
  eliminarCuentaDefinitiva() {
    this.usuarioService.desactivarCuenta(this.usuarioId).subscribe({
      next: () => { alert('Cuenta eliminada'); this.logout(); },
      error: () => { alert('Error al eliminar'); this.showDeleteModal = false; }
    });
  }
}