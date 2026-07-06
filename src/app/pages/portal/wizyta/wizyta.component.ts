import { Component, computed, inject, input, OnDestroy, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';
import { findVisit } from '../visits.data';
import { VisitLocationService } from './visit-location.service';

@Component({
  selector: 'app-wizyta',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './wizyta.component.html',
  styleUrl: './wizyta.component.scss',
})
export class WizytaComponent implements OnDestroy {
  /** Id wizyty z parametru trasy (withComponentInputBinding). */
  readonly id = input<string>('');

  private readonly sanitizer = inject(DomSanitizer);
  private readonly visitLocationService = inject(VisitLocationService);
  private mapRequest: Subscription | null = null;

  readonly visit = computed(() => findVisit(this.id()));

  readonly isTelemedicine = computed(() => this.visit()?.type.toLowerCase().includes('telemedyczna') ?? false);
  readonly canShowMapAction = computed(
    () => !this.isTelemedicine() && this.visitLocationService.canRenderMap(this.visit()),
  );

  readonly isMapOpen = signal(false);
  readonly isMapLoading = signal(false);
  readonly mapEmbedUrl = signal<SafeResourceUrl | null>(null);
  readonly mapError = signal<string | null>(null);

  openMap(): void {
    const visit = this.visit();

    if (!visit || !this.canShowMapAction()) {
      return;
    }

    this.mapRequest?.unsubscribe();
    this.isMapOpen.set(true);
    this.isMapLoading.set(true);
    this.mapEmbedUrl.set(null);
    this.mapError.set(null);

    this.mapRequest = this.visitLocationService.resolveLocation(visit).subscribe((location) => {
      if (!this.isMapOpen()) {
        return;
      }

      this.isMapLoading.set(false);

      if (!location) {
        this.mapError.set('Nie udało się ustalić lokalizacji tej placówki.');
        return;
      }

      this.mapEmbedUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(location.embedUrl));
    });
  }

  closeMap(): void {
    this.mapRequest?.unsubscribe();
    this.mapRequest = null;
    this.isMapOpen.set(false);
    this.isMapLoading.set(false);
    this.mapEmbedUrl.set(null);
    this.mapError.set(null);
  }

  ngOnDestroy(): void {
    this.mapRequest?.unsubscribe();
  }
}
