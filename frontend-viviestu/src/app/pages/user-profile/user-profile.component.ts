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

  // ✅ Por defecto: VACÍO para usuarios nuevos
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
    console.log('🔄 Cargando perfil...');
    this.recuperarSesion();
  }

  recuperarSesion() {
    let idStr = localStorage.getItem('usuarioId');
    
    if (!idStr) {
      const userInfoStr = localStorage.getItem('user_info');
      if (userInfoStr) {
        try {
          const u = JSON.parse(userInfoStr);
          const idReal = u.idUsuario || u.id;
          if (idReal) {
            idStr = idReal.toString();
            localStorage.setItem('usuarioId', idStr!);
          }
        } catch(e) {}
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
    console.log('🔎 Buscando preferencias para usuario:', this.usuarioId);

    this.usuarioService.obtenerPreferencias(this.usuarioId).subscribe({
      next: (resp: any) => {
        const lista = resp.data || resp;
        const prefs = Array.isArray(lista) && lista.length ? lista[0] : null;

        if (!prefs) {
          console.log('ℹ️ Usuario sin preferencias, dejamos el formulario vacío.');
          this.misPreferencias = {
            idPreferencia: undefined,
            idUsuario: this.usuarioId,
            universidad: '',
            presupuesto: null as any,
            transporte: '',
            tiempoMax: null as any,
            seguridad: ''
          };
        } else {
          console.log('✅ Preferencia encontrada (ID):', prefs.idPreferencia);
          this.misPreferencias = {
            idPreferencia: prefs.idPreferencia,
            idUsuario: this.usuarioId,
            universidad: prefs.universidad,
            presupuesto: prefs.presupuesto,
            transporte: prefs.transporte,
            tiempoMax: prefs.tiempoMax,
            seguridad: prefs.seguridad
          };
        }

        this.isLoadingPrefs = false;   // ✅ terminamos de cargar
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log('⚠️ Error al buscar preferencias:', err);
        this.isLoadingPrefs = false;   // igual liberamos el form
      }
    });
  }

  guardarCambios() {
    this.isSaving = true;
    this.misPreferencias.idUsuario = this.usuarioId;

    const datosLimpios = {
      idUsuario: this.misPreferencias.idUsuario,
      universidad: this.misPreferencias.universidad,
      presupuesto: this.misPreferencias.presupuesto,
      transporte: this.misPreferencias.transporte,
      tiempoMax: this.misPreferencias.tiempoMax,
      seguridad: this.misPreferencias.seguridad
    };

    if (this.misPreferencias.idPreferencia) {
      console.log('🔄 Actualizando (PUT) preferencia ID:', this.misPreferencias.idPreferencia);
      this.usuarioService.actualizarPreferencia(this.misPreferencias.idPreferencia, datosLimpios as any)
        .subscribe({
          next: () => this.manejarExito('¡Preferencias actualizadas correctamente!'),
          error: (err) => this.manejarError(err)
        });

    } else {
      console.log('✨ Creando (POST) nueva preferencia...');
      this.usuarioService.crearPreferencia(datosLimpios as any)
        .subscribe({
          next: (resp: any) => {
            const data = resp.data || resp;
            if (data && data.idPreferencia) {
              this.misPreferencias.idPreferencia = data.idPreferencia;
            }
            this.manejarExito('¡Preferencias creadas correctamente!');
          },
          error: (err) => this.manejarError(err)
        });
    }
  }

  manejarExito(texto: string) {
    console.log('✅ manejarExito()', texto); 
    this.isSaving = false;
    this.mensaje = texto;
    setTimeout(() => {
      this.mensaje = null;
      this.router.navigate(['/']);  // home
    }, 1500);
  }

  manejarError(err: any) {
    console.error('❌ manejarError()', err);
    this.isSaving = false;
    alert('Ocurrió un error al guardar. Revisa que el servidor esté funcionando.');
  }

  cerrarSesion() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }

  clickLogo() {
    this.router.navigate(['/dashboard']);
  }

  confirmarEliminacion() { this.showDeleteModal = true; }
  cancelarEliminacion() { this.showDeleteModal = false; }

  eliminarCuentaDefinitiva() {
    this.usuarioService.desactivarCuenta(this.usuarioId).subscribe({
      next: () => { alert('Cuenta eliminada'); this.cerrarSesion(); },
      error: () => { alert('Error al eliminar'); this.showDeleteModal = false; }
    });
  }
}
