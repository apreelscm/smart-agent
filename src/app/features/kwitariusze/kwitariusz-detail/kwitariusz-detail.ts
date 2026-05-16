import { Component, inject, computed } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { KwitariuszService } from '../../../core/services/kwitariusz.service';
import { Kwitariusz, KwitariuszType } from '../../../core/models/kwitariusz.model';

@Component({
  selector: 'app-kwitariusz-detail',
  standalone: true,
  imports: [RouterLink, MatIconModule, MatButtonModule],
  templateUrl: './kwitariusz-detail.html',
  styleUrl: './kwitariusz-detail.scss',
})
export class KwitariuszDetailComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(KwitariuszService);
  readonly Math = Math;
  readonly today = new Date().toISOString().slice(0, 10);

  readonly kwitariusz = computed(() =>
    this.service.getById(this.route.snapshot.paramMap.get('id') ?? '')
  );

  get total(): number {
    const k = this.kwitariusz();
    return k ? +(k.baseAmount + k.interest).toFixed(2) : 0;
  }

  rekalkDiff(k: Kwitariusz): number {
    return +((k.rekalkAmountAfter ?? 0) - (k.rekalkAmountBefore ?? 0)).toFixed(2);
  }

  typeLabel(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'Rata składki + odsetki' : 'Rekalkulacja + odsetki';
  }

  typeIcon(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'receipt' : 'sync_alt';
  }

  clientTypeLabel(v: string): string {
    return ({ 'osoba-fizyczna':'Osoba fizyczna', 'firma':'Firma', 'rolnik':'Rolnik', 'inny':'Inny' } as Record<string,string>)[v] ?? v;
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
