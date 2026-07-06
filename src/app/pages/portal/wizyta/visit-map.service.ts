import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, timeout } from 'rxjs';

export interface VisitMapLocation {
  lat: number;
  lon: number;
}

interface NominatimSearchResult {
  lat: string;
  lon: string;
}

@Injectable({
  providedIn: 'root',
})
export class VisitMapService {
  private readonly http = inject(HttpClient);

  private readonly geocodingUrl = 'https://nominatim.openstreetmap.org/search';

  resolveAddress(address: string): Observable<VisitMapLocation> {
    const normalizedAddress = address.trim();

    return this.http
      .get<NominatimSearchResult[]>(
        this.geocodingUrl,
        {
          params: new HttpParams()
            .set('format', 'jsonv2')
            .set('limit', '1')
            .set('q', normalizedAddress),
        },
      )
      .pipe(
        timeout(10000),
        map((results) => {
          const firstResult = results[0];
          const lat = Number(firstResult?.lat);
          const lon = Number(firstResult?.lon);

          if (!firstResult || !Number.isFinite(lat) || !Number.isFinite(lon)) {
            throw new Error('VISIT_MAP_NO_RESULT');
          }

          return { lat, lon };
        }),
      );
  }

  buildEmbedUrl(location: VisitMapLocation): string {
    const bbox = this.buildBoundingBox(location);

    const params = new URLSearchParams({
      bbox: `${bbox.minLon},${bbox.minLat},${bbox.maxLon},${bbox.maxLat}`,
      layer: 'mapnik',
      marker: `${location.lat},${location.lon}`,
    });

    return `https://www.openstreetmap.org/export/embed.html?${params.toString()}`;
  }

  private buildBoundingBox(location: VisitMapLocation): {
    minLon: string;
    minLat: string;
    maxLon: string;
    maxLat: string;
  } {
    const latDelta = 0.005;
    const lonDelta = 0.005 / Math.max(Math.cos((location.lat * Math.PI) / 180), 0.2);

    return {
      minLon: (location.lon - lonDelta).toFixed(6),
      minLat: (location.lat - latDelta).toFixed(6),
      maxLon: (location.lon + lonDelta).toFixed(6),
      maxLat: (location.lat + latDelta).toFixed(6),
    };
  }
}
