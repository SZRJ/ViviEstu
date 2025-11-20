import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ZonaService } from '../../core/services/zona.service';
import { Zona } from '../../core/models/zona.model';

@Component({
  selector: 'app-zona-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './zona-detail.component.html',
  styleUrls: ['./zona-detail.component.css']
})
export class ZonaDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private zonaService = inject(ZonaService);

  zona: Zona | null = null;

  ngOnInit() {
    // Obtenemos el ID de la URL (ej: /zonas/5)
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.zonaService.obtenerPorId(parseInt(id)).subscribe(data => {
        this.zona = data;
      });
    }
  }

  getImagenRandom(id: number, variant: number) {
    return `https://picsum.photos/seed/${id + variant}/600/400`;
  }
}