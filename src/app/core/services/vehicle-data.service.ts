import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, shareReplay } from 'rxjs';

export interface VehicleVersion {
  name: string;
  eurotaxValue: number;
  engineCC: number[];
  engineKM: number[];
}

export interface VehicleModel {
  name: string;
  yearRange: string;
  fuelTypes: string[];
  versions: VehicleVersion[];
}

export interface VehicleBrand {
  name: string;
  popular: boolean;
  models: VehicleModel[];
}

@Injectable({ providedIn: 'root' })
export class VehicleDataService {
  private http = inject(HttpClient);

  private data$: Observable<VehicleBrand[]> = this.http
    .get<VehicleBrand[]>('/smart-agent/data/vehicles.json')
    .pipe(shareReplay(1));

  getBrands(): Observable<VehicleBrand[]> {
    return this.data$;
  }

  getPopularBrands(): Observable<VehicleBrand[]> {
    return this.data$.pipe(map(brands => brands.filter(b => b.popular)));
  }

  getModels(make: string): Observable<VehicleModel[]> {
    return this.data$.pipe(
      map(brands => brands.find(b => b.name === make)?.models ?? [])
    );
  }

  getEngineCCs(make: string, model: string, fuel: string): Observable<number[]> {
    return this.data$.pipe(
      map(brands => {
        const m = brands.find(b => b.name === make)?.models.find(m => m.name === model);
        if (!m) return [];
        const ccs = new Set<number>();
        m.versions.forEach(v => v.engineCC.forEach(cc => ccs.add(cc)));
        return [...ccs].sort((a, b) => a - b);
      })
    );
  }

  getEngineKMs(make: string, model: string, fuel: string, cc: number): Observable<number[]> {
    return this.data$.pipe(
      map(brands => {
        const m = brands.find(b => b.name === make)?.models.find(m => m.name === model);
        if (!m) return [];
        const kms = new Set<number>();
        m.versions
          .filter(v => v.engineCC.includes(cc))
          .forEach(v => v.engineKM.forEach(km => kms.add(km)));
        return [...kms].sort((a, b) => a - b);
      })
    );
  }

  getVersions(make: string, model: string, cc: number, km: number): Observable<VehicleVersion[]> {
    return this.data$.pipe(
      map(brands => {
        const m = brands.find(b => b.name === make)?.models.find(m => m.name === model);
        if (!m) return [];
        return m.versions.filter(v => v.engineCC.includes(cc) && v.engineKM.includes(km));
      })
    );
  }
}
