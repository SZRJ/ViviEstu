import { Component } from '@angular/core';
import { TiempoService } from '../services/tiempo.service';

interface TravelTimes {
  source: 'google' | 'fallback';
  distanceKm: number;
  walking: number | null;
  bicycling: number | null;
  driving: number | null;
}

@Component({
  selector: 'app-simulador',
  templateUrl: './simulador.component.html',
  styleUrls: ['./simulador.component.css']
})
export class SimuladorComponent {
  // origin inputs
  originLat: number = -12.0464; // default Lima center
  originLng: number = -77.0428;

  // destination inputs
  destLat: number | null = null;
  destLng: number | null = null;

  // result
  distanceKm: number | null = null;
  estimatedMinutes: number | null = null;
  // per-mode results
  times: TravelTimes | null = null;
  loading: boolean = false;

  // presets for Lima
  presets = [
    { name: 'Miraflores', lat: -12.1219, lng: -77.0300 },
    { name: 'San Isidro', lat: -12.0916, lng: -77.0365 },
    { name: 'Santiago de Surco', lat: -12.1180, lng: -76.9860 },
    { name: 'Cercado de Lima', lat: -12.0464, lng: -77.0428 }
  ];

  selectedPreset = '';
  avgSpeed: number = 30;

  constructor(private tiempoService: TiempoService) {}

  usePreset() {
    const p = this.presets.find(x => x.name === this.selectedPreset);
    if (p) { this.destLat = p.lat; this.destLng = p.lng; }
  }

  calcular() {
    if (this.destLat == null || this.destLng == null) return;
    this.loading = true;
    this.times = null;
    this.tiempoService.getTravelTimes(this.originLat, this.originLng, this.destLat, this.destLng)
      .subscribe(res => {
        this.times = res;
        this.distanceKm = res.distanceKm;
        // legacy single estimate: driving fallback or average
        this.estimatedMinutes = res.driving ?? res.bicycling ?? res.walking ?? null;
        this.loading = false;
      }, _ => {
        // on error use fallback quickly
        this.times = {
          source: 'fallback',
          distanceKm: this.tiempoService.distanceKm(this.originLat, this.originLng, this.destLat!, this.destLng!),
          walking: null,
          bicycling: null,
          driving: null
        };
        this.loading = false;
      });
  }

  clear() {
    this.destLat = null;
    this.destLng = null;
    this.distanceKm = null;
    this.estimatedMinutes = null;
    this.selectedPreset = '';
  }
}
