import { Component, signal, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { WykazService } from '../../../core/services/wykaz.service';

interface DraftItem {
  id: string;
  type: string;
  number: string;
  insuredName: string;
  amount: number;
  inkasAmount: number;
  date: string;
  isCustom: boolean;
}

const DEFAULT_ITEMS: DraftItem[] = [
  { id:'d1', type:'polisa',     number:'KM-I/P0638901',    insuredName:'PIOTR WIŚNIEWSKI',     amount:1890.00, inkasAmount:1890.00, date:'2025-05-01', isCustom:false },
  { id:'d2', type:'polisa',     number:'KE-I/P0041237',    insuredName:'MARTA KOWALSKA',        amount:430.00,  inkasAmount:430.00,  date:'2025-05-02', isCustom:false },
  { id:'d3', type:'kwitariusz', number:'KW/2025/009',      insuredName:'PIOTR WIŚNIEWSKI',      amount:1918.30, inkasAmount:1918.30, date:'2025-05-03', isCustom:false },
  { id:'d4', type:'polisa',     number:'KM-H/P1203456',    insuredName:'ADAM NOWAK',            amount:1120.00, inkasAmount:1120.00, date:'2025-05-04', isCustom:false },
  { id:'d5', type:'aneks',      number:'ANK/2025/0041',    insuredName:'ADAM NOWAK',            amount:180.00,  inkasAmount:180.00,  date:'2025-05-05', isCustom:false },
];

const AVAILABLE_AGENTS = [
  { number:'AGT/001', name:'PIOTR KOWALSKI' },
  { number:'AGT/002', name:'ANNA NOWAK' },
  { number:'AGT/003', name:'MAREK WIŚNIEWSKI' },
  { number:'AGT/004', name:'ZOFIA KAMIŃSKA' },
];

@Component({
  selector: 'app-new-wykaz',
  standalone: true,
  imports: [RouterLink, FormsModule, MatIconModule, MatTooltipModule],
  templateUrl: './new-wykaz.html',
  styleUrl: './new-wykaz.scss',
})
export class NewWykazComponent {
  readonly service = inject(WykazService);
  readonly router  = inject(Router);

  readonly agents = AVAILABLE_AGENTS;
  readonly selectedAgents = signal<string[]>(['AGT/001']);
  readonly items = signal<DraftItem[]>([...DEFAULT_ITEMS]);

  // Add policy form
  readonly showAddForm  = signal<'policy' | 'csv' | null>(null);
  readonly newItemType  = signal('polisa');
  readonly newItemNum   = signal('');
  readonly newItemName  = signal('');
  readonly newItemAmt   = signal(0);
  readonly addError     = signal('');
  readonly csvFile      = signal('');

  readonly docTypes = ['polisa','kwitariusz','aneks','uks','wypowiedzenie','zawiadomienie o zbyciu',
    'cesja','akt własności','oświadczenie','zaświadczenie o przebiegu ubezpieczenia',
    'nota prowizyjna','faktura','lista ubezpieczonych','dowód rejestracyjny'];

  get totalAmount(): number {
    return +this.items().reduce((s, i) => s + i.amount, 0).toFixed(2);
  }

  get totalInkas(): number {
    return +this.items().reduce((s, i) => s + i.inkasAmount, 0).toFixed(2);
  }

  toggleAgent(num: string): void {
    this.selectedAgents.update(sel =>
      sel.includes(num) ? sel.filter(s => s !== num) : [...sel, num]
    );
  }

  removeItem(id: string): void {
    this.items.update(list => list.filter(i => i.id !== id));
  }

  addItem(): void {
    const num = this.newItemNum().trim();
    if (!num) { this.addError.set('Podaj numer dokumentu.'); return; }
    if (this.items().some(i => i.number === num)) {
      this.addError.set('Dokument o tym numerze już istnieje na liście.');
      return;
    }
    this.addError.set('');
    this.items.update(list => [...list, {
      id: Date.now().toString(),
      type: this.newItemType(),
      number: num,
      insuredName: this.newItemName() || 'NIEZNANY',
      amount: this.newItemAmt(),
      inkasAmount: this.newItemAmt(),
      date: new Date().toISOString().slice(0, 10),
      isCustom: true,
    }]);
    this.newItemNum.set(''); this.newItemName.set(''); this.newItemAmt.set(0);
    this.showAddForm.set(null);
  }

  submit(): void {
    const agents = this.agents.filter(a => this.selectedAgents().includes(a.number));
    const agentMain = agents[0];
    this.service.wykazy.update(list => [{
      id: Date.now().toString(),
      number: `WYK/2025/${String(list.length + 1).padStart(4, '0')}`,
      agentNumber: agentMain?.number ?? 'AGT/001',
      agentName:   agentMain?.name   ?? 'NIEZNANY',
      issueDate:    new Date().toISOString().slice(0,10),
      modifiedDate: new Date().toISOString().slice(0,10),
      modifiedBy:   'p.kowalski',
      totalAmount: this.totalAmount,
      inkasAmount: this.totalInkas,
      paymentStatus: 'nieoplacony' as const,
      statusCR:     'oczekujacy' as const,
      statusOddzial:'oczekujacy' as const,
      messages: [], notes: [],
      source: 'SITU' as const,
      blocked: false,
      items: this.items().map(i => ({
        id: i.id, type: i.type as any, number: i.number,
        insuredName: i.insuredName, amount: i.amount, inkasAmount: i.inkasAmount,
        date: i.date, canDelete: i.isCustom,
      })),
    }, ...list]);
    this.router.navigate(['/rozliczenia/wykazy']);
  }
}
