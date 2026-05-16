import { Component, inject, computed } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { PolisaService } from '../../../core/services/polisa.service';
import { ProductIconComponent } from '../../../shared/components/product-icon/product-icon';

@Component({
  selector: 'app-polisa-detail',
  standalone: true,
  imports: [RouterLink, MatIconModule, MatButtonModule, ProductIconComponent],
  templateUrl: './polisa-detail.html',
  styleUrl: './polisa-detail.scss',
})
export class PolisaDetailComponent {
  readonly service = inject(PolisaService);
  private readonly route = inject(ActivatedRoute);
  readonly polisa = computed(() => this.service.getById(this.route.snapshot.paramMap.get('id') ?? ''));

  statusLabel(s: string): string {
    return ({ aktywna:'Aktywna', wygasla:'Wygasła', anulowana:'Anulowana',
              zawieszona:'Zawieszona', 'w-trakcie':'W trakcie' } as Record<string,string>)[s] ?? s;
  }
}
