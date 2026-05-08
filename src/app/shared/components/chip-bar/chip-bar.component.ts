import { Component, inject, computed } from '@angular/core';
import { Router } from '@angular/router';
import { PolicyDraftService } from '../../../core/services/policy-draft.service';

interface Chip { label: string; route: string; }

@Component({
  selector: 'app-chip-bar',
  standalone: true,
  imports: [],
  templateUrl: './chip-bar.component.html',
})
export class ChipBarComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  chips = computed<Chip[]>(() => {
    const v = this.draft.vehicle();
    const result: Chip[] = [];
    if (v.year)      result.push({ label: String(v.year),  route: '/kalkulator/rok' });
    if (v.make)      result.push({ label: v.make,           route: '/kalkulator/marka' });
    if (v.model)     result.push({ label: v.model,          route: '/kalkulator/model' });
    if (v.fuelType)  result.push({ label: v.fuelType,       route: '/kalkulator/paliwo' });
    if (v.engineCC)  result.push({ label: `${v.engineCC} cm³`, route: '/kalkulator/pojemnosc' });
    if (v.engineKM)  result.push({ label: `${v.engineKM} KM`,  route: '/kalkulator/moc' });
    if (v.version)   result.push({ label: v.version,        route: '/kalkulator/wersja' });
    return result;
  });

  navigate(route: string): void {
    this.router.navigate([route]);
  }
}
