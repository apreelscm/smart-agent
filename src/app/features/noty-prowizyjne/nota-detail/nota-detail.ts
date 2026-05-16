import { Component, inject, computed, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NotaProwizyjnaService } from '../../../core/services/nota-prowizyjna.service';

type ViewMode = 'domyslny' | 'na-zadanie' | 'uproszczony';

const ALL_OPTIONAL_COLS = [
  { key:'risk',        label:'Ryzyko'          },
  { key:'commRate',    label:'Stawka %'         },
  { key:'riskCode',    label:'Kod ryzyka'       },
  { key:'issueDate',   label:'Data wystawienia' },
  { key:'insuredName', label:'Ubezpieczający'   },
  { key:'baseAmount',  label:'Podstawa (PLN)'   },
  { key:'ofwcName',    label:'OFWC – imię/nazwisko' },
  { key:'ofwcNumber',  label:'Numer OFWC'       },
];

@Component({
  selector: 'app-nota-detail',
  standalone: true,
  imports: [RouterLink, FormsModule, MatIconModule, MatTooltipModule],
  templateUrl: './nota-detail.html',
  styleUrl: './nota-detail.scss',
})
export class NotaDetailComponent {
  readonly service = inject(NotaProwizyjnaService);
  private readonly route = inject(ActivatedRoute);

  readonly nota = computed(() => this.service.getById(this.route.snapshot.paramMap.get('id') ?? ''));
  readonly viewMode   = signal<ViewMode>('domyslny');
  readonly showComm   = signal(true);
  readonly selectedCols = signal<string[]>(['issueDate','insuredName','baseAmount','risk','commRate']);
  readonly previewDay = signal(new Date().toISOString().slice(0,10));
  readonly newNote    = signal('');
  readonly showNoteForm = signal(false);
  readonly optionalCols = ALL_OPTIONAL_COLS;

  get agencies(): string[] {
    const nota = this.nota();
    if (!nota) return [];
    return [...new Set(nota.items.map(i => i.agencyCode))];
  }

  itemsByAgency(agency: string) {
    return this.nota()?.items.filter(i => i.agencyCode === agency) ?? [];
  }

  agencyTotal(agency: string): number {
    return +(this.itemsByAgency(agency).reduce((s, i) => s + i.commissionAmount, 0)).toFixed(2);
  }

  toggleCol(key: string): void {
    this.selectedCols.update(cols =>
      cols.includes(key) ? cols.filter(c => c !== key) : [...cols, key]
    );
  }

  hasCol(key: string): boolean { return this.selectedCols().includes(key); }
}
