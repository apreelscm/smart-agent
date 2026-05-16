import { Component, inject, ViewChild, AfterViewInit, effect } from '@angular/core';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { PolicyRenewalService } from '../../core/services/policy-renewal.service';
import { PolicyRenewal } from '../../core/models/policy-renewal.model';
import { ProductIconComponent } from '../../shared/components/product-icon/product-icon';

@Component({
  selector: 'app-policy-renewals',
  standalone: true,
  imports: [MatTableModule, MatSortModule, MatIconModule, MatButtonModule, MatMenuModule, ProductIconComponent],
  templateUrl: './policy-renewals.html',
  styleUrl: './policy-renewals.scss',
})
export class PolicyRenewalsComponent implements AfterViewInit {
  readonly service = inject(PolicyRenewalService);
  readonly dataSource = new MatTableDataSource<PolicyRenewal>();
  readonly displayedColumns = ['product', 'policyNumber', 'insuredName', 'daysToEnd', 'identifier', 'actions'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      this.dataSource.data = this.service.filteredRenewals();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  setProductFilter(p: string) { this.service.setProductFilter(p); }
  clearFilters() { this.service.clearFilters(); }
}
