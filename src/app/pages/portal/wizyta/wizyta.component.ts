import { Component, computed, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';
import { findVisit } from '../visits.data';

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

  readonly visit = computed(() => findVisit(this.id()));

  readonly isTelemedicine = computed(() => this.visit()?.type.includes('telemedyczna') ?? false);
}
