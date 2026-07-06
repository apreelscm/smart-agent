import { Component, OnDestroy, computed, inject, input, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subscription } from 'rxjs';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';
import { Visit, findVisit } from '../visits.data';
import { VisitMapService } from './visit-map.service';

const MAP_LOOKUP_ERROR_MESSAGE =
  'Nie udało się pokazać lokalizacji tej placówki. Spróbuj ponownie później.';

@Component({
  selector: 'app-wizyta',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './wizyta.component.html',
  styleUrl: './wizyta.component.scss',
})
export class WizytaComponent implements OnDestroy {
  private readonly sanitizer = inject(DomSanitizer);
  private readonly visitMapService = inject(VisitMapService);

  private mapLookupSubscription?: Subscription;

  /** Id wizyty z parametru trasy (withComponentInputBinding). */
  readonly id = input<string>('');

  readonly visit = computed(() => findVisit(this.id()));

  readonly isTelemedicine = computed(() =>
    this.visit()?.type.toLowerCase().includes('telemedyczna') ?? false,
  );

  readonly canShowMap = computed(() => {
    const visit = this.visit();

    return !this.isTelemedicine() && this.hasMappableFacilityContext(visit);
  });

  readonly isMapModalOpen = signal(false);
  readonly isMapLoading = signal(false);
  readonly mapError = signal('');
  readonly mapUrl = signal<SafeResourceUrl | null>(null);

  ngOnDestroy(): void {
    this.mapLookupSubscription?.unsubscribe();
  }

  openMapModal(): void {
    if (this.isMapModalOpen()) {
      return;
    }

    this.isMapModalOpen.set(true);
    this.isMapLoading.set(false);
    this.mapError.set('');
    this.mapUrl.set(null);

    const visit = this.visit();

    if (!visit || !this.hasMappableFacilityContext(visit)) {
      this.mapError.set(MAP_LOOKUP_ERROR_MESSAGE);
      return;
    }

    this.isMapLoading.set(true);
    this.mapLookupSubscription?.unsubscribe();
    this.mapLookupSubscription = this.visitMapService.resolveAddress(visit.address).subscribe({
      next: (location) => {
        this.mapUrl.set(
          this.sanitizer.bypassSecurityTrustResourceUrl(this.visitMapService.buildEmbedUrl(location)),
        );
        this.isMapLoading.set(false);
      },
      error: () => {
        this.mapError.set(MAP_LOOKUP_ERROR_MESSAGE);
        this.isMapLoading.set(false);
      },
    });
  }

  closeMapModal(): void {
    this.mapLookupSubscription?.unsubscribe();
    this.mapLookupSubscription = undefined;
    this.isMapModalOpen.set(false);
    this.isMapLoading.set(false);
    this.mapError.set('');
    this.mapUrl.set(null);
  }

  private hasMappableFacilityContext(visit: Visit | undefined): boolean {
    return !!visit?.facility.trim() && !!visit?.address.trim();
  }
}
