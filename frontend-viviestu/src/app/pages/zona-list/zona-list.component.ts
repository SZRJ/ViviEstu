import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core'; // Importamos ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ZonaService } from '../../core/services/zona.service';
import { Zona } from '../../core/models/zona.model';

@Component({
  selector: 'app-zona-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './zona-list.component.html',
  styleUrls: ['./zona-list.component.css']
})
export class ZonaListComponent implements OnInit {
  
  private zonaService = inject(ZonaService);
  private cd = inject(ChangeDetectorRef); // Inyectamos el detector
  
  zonas: Zona[] = [];
  loading = true;
  mensaje = 'Cargando zonas...';

  ngOnInit(): void {
    this.decidirQueCargar();
  }

  decidirQueCargar() {
    const idUsuario = localStorage.getItem('usuarioId');
    
    if (idUsuario) {
      console.log('👤 Usuario detectado (ID ' + idUsuario + '), buscando recomendaciones...');
      this.cargarRecomendadas(parseInt(idUsuario));
    } else {
      console.log('👤 Invitado: cargando todas las zonas.');
      this.cargarTodas();
    }
  }

  cargarRecomendadas(id: number) {
    this.zonaService.listarRecomendadas(id).subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          console.log('✅ Recomendaciones encontradas:', data.length);
          this.zonas = data;
          this.loading = false;
          
          this.cd.detectChanges(); // <--- ¡DESPIERTA A ANGULAR!
        } else {
          console.warn('⚠️ Sin recomendaciones, cargando generales...');
          this.cargarTodas();
        }
      },
      error: (err) => {
        console.error('❌ Error recomendaciones:', err);
        this.cargarTodas();
      }
    });
  }

  cargarTodas() {
    this.mensaje = 'Cargando listado general...';
    this.zonaService.listarTodas().subscribe({
      next: (data) => { 
        console.log('✅ Listado general cargado:', data);
        this.zonas = data; 
        this.loading = false; 
        
        this.cd.detectChanges(); // <--- ¡DESPIERTA A ANGULAR!
      },
      error: (err) => {
        console.error('❌ Error fatal:', err);
        this.loading = false;
        this.mensaje = 'Error al conectar con el servidor.';
        
        this.cd.detectChanges(); // Actualizar incluso en error
      }
    });
  }
  
  getImagenRandom(id: number) {
    return `https://picsum.photos/seed/${id}/400/250`;
  }
}