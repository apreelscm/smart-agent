import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { WykazService } from '../../core/services/wykaz.service';
import { Wykaz } from '../../core/models/wykaz.model';

@Component({
  selector: 'app-wykazy',
  standalone: true,
  imports: [RouterLink, FormsModule, MatTableModule, MatSortModule,
            MatIconModule, MatButtonModule, MatMenuModule, MatTooltipModule],
  templateUrl: './wykazy.html',
  styleUrl: './wykazy.scss',
})
export class WykazyComponent implements AfterViewInit {
  readonly service = inject(WykazService);
  readonly dataSource = new MatTableDataSource<Wykaz>();
  readonly displayedColumns = ['number', 'agent', 'amounts', 'dates', 'statusCR', 'statusOddzial', 'paymentStatus', 'messages', 'actions'];

  readonly expandedFilter = signal<string | null>(null);
  readonly showAdvanced  = signal(false);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => { this.dataSource.data = this.service.filtered(); });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get f() { return this.service.filter(); }
  get hasFilter(): boolean {
    const f = this.f;
    return !!(f.wykazNumber || f.agentNumber || f.paymentStatus || f.policyNumber ||
              f.statusCR || f.statusOddzial || f.dateFrom || f.dateTo ||
              f.amountFrom || f.amountTo || f.last30Days);
  }

  setFilter(key: keyof typeof this.f, val: unknown): void {
    this.service.filter.update(f => ({ ...f, [key]: val }));
  }

  toggleFilter(name: string): void {
    this.expandedFilter.update(v => v === name ? null : name);
  }

  toggleLast30(): void { this.service.filter.update(f => ({ ...f, last30Days: !f.last30Days })); }
  clearFilters(): void { this.service.clearFilters(); this.expandedFilter.set(null); }

  paymentLabel(s: string): string {
    return { oplacony:'Opłacony', nieoplacony:'Nieopłacony', 'oplacony-czesciowo':'Opłacony częściowo' }[s] ?? s;
  }

  processLabel(s: string): string {
    return { zatwierdzony:'Zatwierdzony', oczekujacy:'Oczekujący', odrzucony:'Odrzucony' }[s] ?? s;
  }
}
