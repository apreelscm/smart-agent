import { AfterViewInit, Component, ViewChild, effect, inject, signal } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Kwitariusz, KwitariuszStatus, KwitariuszType } from '../../core/models/kwitariusz.model';
import { KwitariuszService } from '../../core/services/kwitariusz.service';

@Component({
  selector: 'app-kwitariusze',
  standalone: true,
  imports: [RouterLink, FormsModule, TitleCasePipe,
            MatTableModule, MatSortModule, MatIconModule,
            MatButtonModule, MatMenuModule, MatTooltipModule],
  templateUrl: './kwitariusze.html',
  styleUrl: './kwitariusze.scss',
})
export class KwitariuszeComponent implements AfterViewInit {
  readonly service = inject(KwitariuszService);
  readonly dataSource = new MatTableDataSource<Kwitariusz>();
  readonly displayedColumns = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'amount', 'status', 'actions'];

  readonly expandedFilter = signal<'policy' | 'insured' | 'status' | null>(null);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.service.restorePersistedStatusFilter();
    effect(() => { this.dataSource.data = this.service.filtered(); });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get activeType() { return this.service.filterType(); }
  get last30Active() { return this.service.filterLast30Days(); }
  get policySearch() { return this.service.filterPolicySearch(); }
  get insuredSearch() { return this.service.filterInsuredSearch(); }
  get availableStatuses() { return this.service.availableStatuses(); }
  get selectedStatuses() { return this.service.filterStatuses(); }
  get selectedStatusCount() { return this.selectedStatuses.length; }
  get statusFilterLabel() {
    return this.selectedStatusCount ? `Status (${this.selectedStatusCount})` : 'Status';
  }
  get hasAnyFilter() {
    return !!this.activeType || this.last30Active || !!this.policySearch || !!this.insuredSearch || this.selectedStatusCount > 0;
  }

  setType(t: string) { this.service.filterType.update(v => v === t ? '' : t); }
  toggle30Days() { this.service.filterLast30Days.update(v => !v); }

  toggleFilter(f: 'policy' | 'insured' | 'status'): void {
    this.expandedFilter.update(v => v === f ? null : f);
  }

  toggleStatus(status: KwitariuszStatus): void {
    this.service.toggleStatusFilter(status);
  }

  clearStatusFilter(): void {
    this.service.clearStatusFilter();
  }

  clearFilters(): void {
    this.service.clearFilters();
    this.expandedFilter.set(null);
  }

  typeIcon(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'payments' : 'sync_alt';
  }

  typeLabel(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'Rata + odsetki' : 'Rekalkulacja + odsetki';
  }

  totalAmount(k: Kwitariusz): number {
    return +(k.baseAmount + k.interest).toFixed(2);
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
