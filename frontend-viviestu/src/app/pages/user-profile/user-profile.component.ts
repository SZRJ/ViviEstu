import { Component, inject, OnInit } from '@angular/core';
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

  usuarioNombre: string = '';
  usuarioId: number = 0;

  // Inicializamos vacío. Si no carga nada del backend, usaremos esto para crear.
  misPreferencias: Preference = {
    idUsuario: 0,
    presupuesto: 500,
    seguridad: 'Alta',
    tiempoMax: 30,
    transporte: 'Bus',
    universidad: 'UPC'
  };

  showDeleteModal = false;
  isSaving = false;
  mensaje: string | null = null;

  ngOnInit(): void {
    console.log('🔄 Cargando perfil...');
    this.recuperarSesion();
  }

  recuperarSesion() {
    // Lógica blindada para obtener el ID
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
      this.usuarioId = parseInt(idStr);
      this.usuarioNombre = localStorage.getItem('usuarioNombre') || 'Usuario';
      this.misPreferencias.idUsuario = this.usuarioId;

      // >>> AQUÍ ESTÁ LA CLAVE: BUSCAR SI YA EXISTEN <<<
      this.cargarPreferenciasExistentes();
      
    } else {
      this.router.navigate(['/login']);
    }
  }

  cargarPreferenciasExistentes() {
    console.log('🔎 Buscando preferencias para usuario:', this.usuarioId);

    this.usuarioService.obtenerPreferencias(this.usuarioId).subscribe({
      next: (lista) => {
        // Tu backend devuelve una lista.
        // Si la lista tiene elementos, significa que YA EXISTE una preferencia.
        if (lista && lista.length > 0) {
          const preferenciaEncontrada = lista[0];
          
          console.log('✅ Preferencia encontrada (ID):', preferenciaEncontrada.idPreferencia);
          
          // Llenamos el formulario con los datos que vinieron de la BD
          this.misPreferencias = preferenciaEncontrada;
          
          // Aseguramos que el idUsuario sea correcto (a veces el backend no lo devuelve en el objeto anidado)
          this.misPreferencias.idUsuario = this.usuarioId;
        } else {
          console.log('ℹ️ Este usuario no tiene preferencias guardadas. Se creará una nueva.');
        }
      },
      error: (err) => {
        console.log('⚠️ Error al buscar (o no existen):', err);
      }
    });
  }

  guardarCambios() {
    this.isSaving = true;
    this.misPreferencias.idUsuario = this.usuarioId;

    // Preparamos el objeto limpio para enviar
    const datosLimpios = {
        idUsuario: this.misPreferencias.idUsuario,
        universidad: this.misPreferencias.universidad,
        presupuesto: this.misPreferencias.presupuesto,
        transporte: this.misPreferencias.transporte,
        tiempoMax: this.misPreferencias.tiempoMax,
        seguridad: this.misPreferencias.seguridad
    };

    // >>> LÓGICA AUTOMÁTICA: PUT o POST <<<
    if (this.misPreferencias.idPreferencia) {
      
      // YA TIENE ID -> ES UNA ACTUALIZACIÓN (PUT)
      console.log('🔄 Actualizando (PUT) preferencia ID:', this.misPreferencias.idPreferencia);
      
      this.usuarioService.actualizarPreferencia(this.misPreferencias.idPreferencia, datosLimpios as any)
        .subscribe({
          next: () => this.manejarExito('¡Preferencias actualizadas correctamente!'),
          error: (err) => this.manejarError(err)
        });

    } else {
      
      // NO TIENE ID -> ES NUEVO (POST)
      console.log('✨ Creando (POST) nueva preferencia...');
      
      this.usuarioService.crearPreferencia(datosLimpios as any)
        .subscribe({
          next: (resp) => {
            // Capturamos el ID nuevo para que el próximo clic sea PUT
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
    this.isSaving = false;
    this.mensaje = texto;
    setTimeout(() => this.mensaje = null, 3000);
  }

  manejarError(err: any) {
    this.isSaving = false;
    console.error('Error backend:', err);
    alert('Ocurrió un error al guardar. Revisa que el servidor esté funcionando.');
  }

  cerrarSesion() {
    this.usuarioService.logout();
    this.router.navigate(['/']);
  }
  clickLogo() {
    // Como ya estás en el perfil, sabemos que estás logueado.
    // Así que te llevamos directo al Dashboard.
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