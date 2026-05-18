import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TitleCasePipe } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { Kwitariusz, KwitariuszType } from '../../core/models/kwitariusz.model';

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
  readonly displayedColumns = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'status', 'amount', 'actions'];

  readonly expandedFilter = signal<'policy' | 'insured' | null>(null);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => { this.dataSource.data = this.service.filtered(); });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get activeType()     { return this.service.filterType(); }
  get last30Active()   { return this.service.filterLast30Days(); }
  get policySearch()   { return this.service.filterPolicySearch(); }
  get insuredSearch()  { return this.service.filterInsuredSearch(); }
  get hasAnyFilter()   {
    return !!this.activeType || this.last30Active || !!this.policySearch || !!this.insuredSearch;
  }

  setType(t: string)   { this.service.filterType.update(v => v === t ? '' : t); }
  toggle30Days()       { this.service.filterLast30Days.update(v => !v); }

  toggleFilter(f: 'policy' | 'insured'): void {
    this.expandedFilter.update(v => v === f ? null : f);
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
