import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';
import { VisitCoordinates, findVisit } from '../visits.data';

@Component({
  selector: 'app-wizyta',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './wizyta.component.html',
  styleUrl: './wizyta.component.scss',
})
export class WizytaComponent {
  /** Id wizyty z parametru trasy (withComponentInputBinding). */
  readonly id = input<string>('');

  private readonly sanitizer = inject(DomSanitizer);

  readonly visit = computed(() => findVisit(this.id()));
  readonly coordinates = computed(() => this.getValidCoordinates(this.visit()?.coordinates));
  readonly hasCoordinates = computed(() => this.coordinates() !== null);
  readonly isMapOpen = signal(false);

  readonly mapUrl = computed<SafeResourceUrl | null>(() => {
    const coordinates = this.coordinates();

    if (!coordinates) {
      return null;
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(
      this.createOpenStreetMapEmbedUrl(coordinates),
    );
  });

  readonly isTelemedicine = computed(() => this.visit()?.type.includes('telemedyczna') ?? false);

  constructor() {
    effect(() => {
      if (!this.hasCoordinates()) {
        this.isMapOpen.set(false);
      }
    });
  }

  openMap(): void {
    if (!this.hasCoordinates()) {
      return;
    }

    this.isMapOpen.set(true);
  }

  closeMap(): void {
    this.isMapOpen.set(false);
  }

  private getValidCoordinates(coordinates?: VisitCoordinates): VisitCoordinates | null {
    if (!coordinates) {
      return null;
    }

    const { lat, lon } = coordinates;

    if (
      !Number.isFinite(lat) ||
      !Number.isFinite(lon) ||
      lat < -90 ||
      lat > 90 ||
      lon < -180 ||
      lon > 180
    ) {
      return null;
    }

    return coordinates;
  }

  private createOpenStreetMapEmbedUrl({ lat, lon }: VisitCoordinates): string {
    const latDelta = 0.006;
    const lonDelta = 0.01;
    const bbox = [
      this.clamp(lon - lonDelta, -180, 180).toFixed(6),
      this.clamp(lat - latDelta, -90, 90).toFixed(6),
      this.clamp(lon + lonDelta, -180, 180).toFixed(6),
      this.clamp(lat + latDelta, -90, 90).toFixed(6),
    ].join(',');

    const params = new URLSearchParams({
      bbox,
      layer: 'mapnik',
      marker: `${lat.toFixed(6)},${lon.toFixed(6)}`,
    });

    return `https://www.openstreetmap.org/export/embed.html?${params.toString()}`;
  }

  private clamp(value: number, min: number, max: number): number {
    return Math.min(Math.max(value, min), max);
  }
}
