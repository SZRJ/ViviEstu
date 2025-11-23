import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, of, forkJoin } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable()
export class TiempoService {
  constructor(private http: HttpClient) {}

  // Haversine formula to compute distance in kilometers
  private toRad(value: number) {
    return (value * Math.PI) / 180;
  }

  public distanceKm(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const R = 6371; // Earth radius in km
    const dLat = this.toRad(lat2 - lat1);
    const dLon = this.toRad(lon2 - lon1);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(this.toRad(lat1)) * Math.cos(this.toRad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  /**
   * Fallback times (minutes) using straight-line distance and average speeds.
   */
  private fallbackTimes(lat1: number, lon1: number, lat2: number, lon2: number) {
    const km = this.distanceKm(lat1, lon1, lat2, lon2);
    const walkingKmh = 5; // avg walking
    const bicycleKmh = 12; // avg bicycle
    const drivingKmh = 30; // urban taxi average (congestion)
    const minutes = (km: number, kmh: number) => (kmh > 0 ? Math.max(0, Math.round((km / kmh) * 60)) : Infinity);
    return {
      source: 'fallback',
      distanceKm: km,
      walking: minutes(km, walkingKmh),
      bicycling: minutes(km, bicycleKmh),
      driving: minutes(km, drivingKmh)
    };
  }

  /**
   * Get travel times (in minutes) for walking, bicycling and driving.
   * Attempts Google Directions API when `environment.googleMapsApiKey` is set, otherwise uses fallback.
   * Returns an Observable resolving to an object: { source: 'google'|'fallback', distanceKm, walking, bicycling, driving }
   */
  public getTravelTimes(lat1: number, lon1: number, lat2: number, lon2: number): Observable<any> {
    const key = (environment as any).googleMapsApiKey;
    // If key provided, call Google Directions for each mode in parallel
    if (key && key.length > 0) {
      const origin = `${lat1},${lon1}`;
      const destination = `${lat2},${lon2}`;
      const modes = ['walking', 'bicycling', 'driving'];
      const requests = modes.map(mode => {
        const params = new HttpParams()
          .set('origin', origin)
          .set('destination', destination)
          .set('mode', mode)
          .set('key', key);
        return this.http.get('https://maps.googleapis.com/maps/api/directions/json', { params }).pipe(
          map((res: any) => {
            if (res && res.routes && res.routes.length > 0 && res.routes[0].legs && res.routes[0].legs.length > 0) {
              const leg = res.routes[0].legs[0];
              return { mode, minutes: Math.round((leg.duration.value) / 60), distanceMeters: leg.distance?.value ?? null };
            }
            return { mode, minutes: null, distanceMeters: null };
          }),
          catchError(_ => of({ mode, minutes: null, distanceMeters: null }))
        );
      });

      return forkJoin(requests).pipe(
        map((arr: any[]) => {
          const result: any = { source: 'google', distanceKm: this.distanceKm(lat1, lon1, lat2, lon2), walking: null, bicycling: null, driving: null };
          arr.forEach(r => {
            if (r && r.mode) result[r.mode] = r.minutes;
          });
          return result;
        }),
        catchError(() => of(this.fallbackTimes(lat1, lon1, lat2, lon2)))
      );
    }

    // No API key: return fallback
    return of(this.fallbackTimes(lat1, lon1, lat2, lon2));
  }
}
