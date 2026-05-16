import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PolisaService } from '../../core/services/polisa.service';
import { Polisa } from '../../core/models/polisa.model';
import { ProductIconComponent } from '../../shared/components/product-icon/product-icon';

@Component({
  selector: 'app-polisy',
  standalone: true,
  imports: [RouterLink, FormsModule, MatTableModule, MatSortModule,
            MatIconModule, MatButtonModule, MatMenuModule, MatTooltipModule,
            ProductIconComponent],
  templateUrl: './polisy.html',
  styleUrl: './polisy.scss',
})
export class PolisyComponent implements AfterViewInit {
  readonly service = inject(PolisaService);
  readonly dataSource = new MatTableDataSource<Polisa>();
  readonly displayedColumns = ['product','policyNumber','insured','dates','premium','status','agent','actions'];
  readonly expandedFilter = signal<string | null>(null);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => { this.dataSource.data = this.service.filtered(); });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get fp() { return this.service.filterProduct(); }
  get fs() { return this.service.filterStatus(); }
  get fq() { return this.service.filterSearch(); }
  get hasFilter() { return !!(this.fp || this.fs || this.fq || this.service.filterLast30()); }

  setProduct(p: string) { this.service.filterProduct.update(v => v === p ? '' : p); }
  setStatus(s: string)  { this.service.filterStatus.update(v => v === s ? '' : s); }
  toggleLast30()        { this.service.filterLast30.update(v => !v); }
  toggleFilter(f: string) { this.expandedFilter.update(v => v === f ? null : f); }
  clearFilters()        { this.service.clearFilters(); this.expandedFilter.set(null); }

  statusLabel(s: string): string {
    return ({ aktywna:'Aktywna', wygasla:'Wygasła', anulowana:'Anulowana',
              zawieszona:'Zawieszona', 'w-trakcie':'W trakcie' } as Record<string,string>)[s] ?? s;
  }
}
