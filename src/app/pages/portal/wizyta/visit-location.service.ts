import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { Visit, VisitCoordinates } from '../visits.data';

export interface ResolvedVisitLocation {
  coordinates: VisitCoordinates;
  embedUrl: string;
}

interface NominatimSearchResult {
  lat: string;
  lon: string;
}

@Injectable({
  providedIn: 'root',
})
export class VisitLocationService {
  private readonly http = inject(HttpClient);
  private readonly geocodeCache = new Map<string, VisitCoordinates | null>();
  private readonly bboxOffset = 0.005;

  canRenderMap(visit: Visit | undefined): boolean {
    if (!visit) {
      return false;
    }

    return !!visit.coordinates || !!this.getRenderableAddress(visit);
  }

  resolveLocation(visit: Visit): Observable<ResolvedVisitLocation | null> {
    if (visit.coordinates) {
      return of(this.toResolvedLocation(visit.coordinates));
    }

    const address = this.getRenderableAddress(visit);

    if (!address) {
      return of(null);
    }

    if (this.geocodeCache.has(address)) {
      const cachedCoordinates = this.geocodeCache.get(address);

      return of(cachedCoordinates ? this.toResolvedLocation(cachedCoordinates) : null);
    }

    const params = new HttpParams().set('format', 'jsonv2').set('limit', '1').set('q', address);

    return this.http
      .get<NominatimSearchResult[]>('https://nominatim.openstreetmap.org/search', { params })
      .pipe(
        map((results) => {
          const firstResult = results[0];

          if (!firstResult) {
            this.geocodeCache.set(address, null);
            return null;
          }

          const lat = Number(firstResult.lat);
          const lon = Number(firstResult.lon);

          if (!Number.isFinite(lat) || !Number.isFinite(lon)) {
            this.geocodeCache.set(address, null);
            return null;
          }

          const coordinates: VisitCoordinates = { lat, lon };

          this.geocodeCache.set(address, coordinates);

          return this.toResolvedLocation(coordinates);
        }),
        catchError(() => of(null)),
      );
  }

  buildEmbedUrl(coordinates: VisitCoordinates): string {
    const minLon = (coordinates.lon - this.bboxOffset).toFixed(6);
    const minLat = (coordinates.lat - this.bboxOffset).toFixed(6);
    const maxLon = (coordinates.lon + this.bboxOffset).toFixed(6);
    const maxLat = (coordinates.lat + this.bboxOffset).toFixed(6);

    const params = new URLSearchParams({
      bbox: `${minLon},${minLat},${maxLon},${maxLat}`,
      layer: 'mapnik',
      marker: `${coordinates.lat},${coordinates.lon}`,
    });

    return `https://www.openstreetmap.org/export/embed.html?${params.toString()}`;
  }

  private toResolvedLocation(coordinates: VisitCoordinates): ResolvedVisitLocation {
    return {
      coordinates,
      embedUrl: this.buildEmbedUrl(coordinates),
    };
  }

  private getRenderableAddress(visit: Visit): string | null {
    const normalizedAddress = this.normalizeAddress(visit.address);

    if (!normalizedAddress) {
      return null;
    }

    const lowerCasedAddress = normalizedAddress.toLowerCase();

    if (lowerCasedAddress.includes('online') || lowerCasedAddress.includes('telemed')) {
      return null;
    }

    if (!/[a-ząćęłńóśźż]/i.test(normalizedAddress) || !/\d/.test(normalizedAddress)) {
      return null;
    }

    return normalizedAddress;
  }

  private normalizeAddress(address: string | null | undefined): string | null {
    const normalizedAddress = address?.trim().replace(/\s+/g, ' ');

    return normalizedAddress ? normalizedAddress : null;
  }
}
