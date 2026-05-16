import { Component, inject, ViewChild, AfterViewInit, effect } from '@angular/core';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { PolicyPhotoService } from '../../core/services/policy-photo.service';
import { PolicyPhoto } from '../../core/models/policy-photo.model';
import { StatusBadgeComponent } from '../../shared/components/status-badge/status-badge';
import { ProductIconComponent } from '../../shared/components/product-icon/product-icon';

@Component({
  selector: 'app-policy-photos',
  standalone: true,
  imports: [
    MatTableModule, MatSortModule, MatIconModule, MatButtonModule, MatMenuModule,
    StatusBadgeComponent, ProductIconComponent,
  ],
  templateUrl: './policy-photos.html',
  styleUrl: './policy-photos.scss',
})
export class PolicyPhotosComponent implements AfterViewInit {
  readonly service = inject(PolicyPhotoService);
  readonly dataSource = new MatTableDataSource<PolicyPhoto>();
  readonly displayedColumns = ['product', 'policyNumber', 'status', 'branchNumber', 'insuredName', 'expiryDate', 'actions'];

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      this.dataSource.data = this.service.filteredPhotos();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  get activeStatusFilter() { return this.service.filter().status; }
  get activeProductFilter() { return this.service.filter().product; }

  setStatusFilter(s: string) { this.service.setStatusFilter(s); }
  setProductFilter(p: string) { this.service.setProductFilter(p); }
  clearFilters() { this.service.clearFilters(); }
}
