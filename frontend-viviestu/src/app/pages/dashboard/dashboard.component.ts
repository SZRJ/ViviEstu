import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service'; // RUTA CORREGIDA: ../../core/services/usuario.service
import { UserInfo } from '../../core/models/user-info.model';
import { ZonaService } from '../../core/services/zona.service';
import { Zona } from '../../core/models/zona.model';
import { FormsModule } from '@angular/forms';
import { PreferenceService } from '../../core/services/preference.service';
import { Preference } from '../../core/models/preference.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  
})
export class DashboardComponent implements OnInit {
  // --- Servicios Inyectados ---
  usuarioService = inject(UsuarioService);
  zonaService = inject(ZonaService);
  preferenceService = inject(PreferenceService);

  // --- Propiedades de Estado ---
  zonas: Zona[] = [];
  isLoading: boolean = false;
  
  // Preferencia actual para los filtros
  currentPreference: Preference = this.preferenceService.getInitialPreference(); 

  ngOnInit(): void {
    // 1. Cargar Preferencia Guardada (si existe)
    this.loadUserPreference();
    
    // 2. Ejecutar la búsqueda inicial si hay preferencia
    // Se podría ejecutar aquí, pero se omite para que el usuario pueda ver los filtros primero.
    // this.getZonasByPreferences();
  }

  // --- Lógica de Preferencias ---
  loadUserPreference() {
    this.isLoading = true;
    this.preferenceService.getPreferenceByUserId().subscribe({
        next: (pref: Preference) => {
            if (pref.idPreferencia) {
                this.currentPreference = pref;
                console.log('Preferencia cargada:', pref);
            }
            this.isLoading = false;
        },
        error: (err) => {
            console.warn('No se encontró preferencia guardada para el usuario.', err);
            // Usar la preferencia inicial por defecto
            this.currentPreference = this.preferenceService.getInitialPreference();
            this.isLoading = false;
        }
    });
  }

  savePreference() {
    const userId = this.usuarioService.userInfo()?.idUsuario;
    if (!userId) {
        console.error('No hay ID de usuario. No se puede guardar la preferencia.');
        return;
    }

    // Actualizar el ID de usuario en la preferencia antes de guardar
    this.currentPreference.idUsuario = userId;

    this.preferenceService.savePreference(this.currentPreference).subscribe({
        next: (savedPref: Preference) => {
            this.currentPreference = savedPref;
            alert('¡Preferencia guardada exitosamente!');
        },
        error: (err) => {
            console.error('Error al guardar preferencia:', err);
            alert('Error al guardar preferencia. Revisa la consola.');
        }
    });
  }

  // --- Lógica de Zonas ---
  getZonasByPreferences() {
    this.isLoading = true;
    console.log('Buscando zonas con preferencia:', this.currentPreference);

    this.zonaService.getZonasByFilters(this.currentPreference).subscribe({
        next: (zonasResponse: Zona[]) => {
            this.zonas = zonasResponse;
            this.isLoading = false;
            console.log('Zonas encontradas:', this.zonas);
        },
        error: (err) => {
            console.error('Error al buscar zonas:', err);
            this.zonas = [];
            this.isLoading = false;
            // Usar alerta de UI si es necesario, pero por ahora solo consola
            // alert('Hubo un error al buscar las zonas. Inténtalo de nuevo.');
        }
    });
  }
}