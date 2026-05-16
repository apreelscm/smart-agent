import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotaProwizyjnaService } from '../../core/services/nota-prowizyjna.service';
import { NotaProwizyjna } from '../../core/models/nota-prowizyjna.model';

@Component({
  selector: 'app-noty-prowizyjne',
  standalone: true,
  imports: [RouterLink, FormsModule, MatTableModule, MatSortModule,
            MatIconModule, MatButtonModule, MatMenuModule, MatTooltipModule],
  templateUrl: './noty-prowizyjne.html',
  styleUrl: './noty-prowizyjne.scss',
})
export class NotyProwizyjneComponent implements AfterViewInit {
  readonly service = inject(NotaProwizyjnaService);
  readonly dataSource = new MatTableDataSource<NotaProwizyjna>();
  readonly expandedFilter = signal<string | null>(null);
  readonly showAdvanced   = signal(false);

  get cols() {
    const base = ['number', 'agent', 'issueDate', 'type', 'status'];
    if (this.f.showCommission) base.push('commission');
    base.push('actions');
    return base;
  }

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => { this.dataSource.data = this.service.filtered(); });

    this.dataSource.sortingDataAccessor = (row: NotaProwizyjna, col: string) => {
      switch (col) {
        case 'agent':      return row.agentName;
        case 'commission': return row.totalCommission;
        case 'status':     return row.status;
        case 'type':       return row.type;
        case 'issueDate':  return row.issueDate;
        case 'number':     return row.number;
        default:           return (row as unknown as Record<string, unknown>)[col] as string ?? '';
      }
    };
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get f() { return this.service.filter(); }
  get hasFilter(): boolean {
    const f = this.f;
    return !!(f.agencyOwner || f.notaNumber || f.policyNumber || f.type ||
              f.amountFrom || f.amountTo || f.dateFrom || f.dateTo || f.last30Days);
  }

  setFilter(key: keyof ReturnType<typeof this.service.filter>, val: unknown): void {
    this.service.filter.update(f => ({ ...f, [key]: val }));
  }
  toggleFilter(name: string) { this.expandedFilter.update(v => v === name ? null : name); }
  toggleLast30()             { this.service.filter.update(f => ({ ...f, last30Days: !f.last30Days })); }
  toggleShowCommission()     { this.service.filter.update(f => ({ ...f, showCommission: !f.showCommission })); }
  clearFilters()             { this.service.filter.set({ agencyOwner:'', dateFrom:'', dateTo:'', type:'', amountFrom:'', amountTo:'', notaNumber:'', policyNumber:'', showCommission:true, last30Days:false }); this.expandedFilter.set(null); }

  typeLabel(t: string)  { return t === 'podstawowa' ? 'Podstawowa' : 'Dodatkowa'; }
  statusLabel(s: string){ return ({ wyplacona:'Wypłacona', oczekujaca:'Oczekująca', zablokowana:'Zablokowana' } as Record<string,string>)[s] ?? s; }
}
