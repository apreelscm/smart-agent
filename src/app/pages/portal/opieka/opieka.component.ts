import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';
import { VISITS, VisitState } from '../visits.data';

@Component({
  selector: 'app-opieka',
  standalone: true,
  imports: [PortalLayoutComponent, RouterLink],
  templateUrl: './opieka.component.html',
  styleUrl: './opieka.component.scss',
})
export class OpiekaComponent {
  readonly activeTab = signal<VisitState>('planned');
  readonly filtersVisible = signal(true);

  readonly visibleVisits = computed(() => VISITS.filter((v) => v.group === this.activeTab()));

  setTab(tab: VisitState): void {
    this.activeTab.set(tab);
  }

  toggleFilters(): void {
    this.filtersVisible.update((v) => !v);
  }
}
