import { Injectable, computed, signal } from '@angular/core';
import { Wykaz, WykazFilter, WykazNote } from '../models/wykaz.model';

const MOCK: Wykaz[] = [
  {
    id:'1', number:'WYK/2025/0041', agentNumber:'AGT/001', agentName:'PIOTR KOWALSKI',
    issueDate:'2025-04-01', modifiedDate:'2025-04-03', modifiedBy:'p.kowalski',
    totalAmount:12450.00, inkasAmount:11200.00,
    paymentStatus:'oplacony', statusCR:'zatwierdzony', statusOddzial:'zatwierdzony',
    messages:[], notes:[], source:'SITU', blocked:false,
    items:[
      { id:'1a', type:'polisa',     number:'KM-I/P0647405',    insuredName:'STANISŁAW PAWŁOWSKI',  amount:1240.00, inkasAmount:1240.00, date:'2025-03-15', canDelete:false },
      { id:'1b', type:'kwitariusz', number:'KW/2025/001',      insuredName:'STANISŁAW PAWŁOWSKI',  amount:1258.50, inkasAmount:1258.50, date:'2025-04-01', canDelete:false },
      { id:'1c', type:'polisa',     number:'KM-I/P0631516',    insuredName:'JURAND GRABOWSKI',     amount:876.00,  inkasAmount:876.00,  date:'2025-03-20', canDelete:false },
    ],
  },
  {
    id:'2', number:'WYK/2025/0042', agentNumber:'AGT/002', agentName:'ANNA NOWAK',
    issueDate:'2025-04-05', modifiedDate:'2025-04-07', modifiedBy:'a.nowak',
    totalAmount:8760.00, inkasAmount:4380.00,
    paymentStatus:'oplacony-czesciowo', statusCR:'oczekujacy', statusOddzial:'oczekujacy',
    messages:['Brak potwierdzenia przelewu'], notes:[], source:'VERSIS', blocked:false,
    items:[
      { id:'2a', type:'polisa',  number:'KM-I/P0645576', insuredName:'AGNIESZKA GÓRA',  amount:2100.00, inkasAmount:1050.00, date:'2025-03-18', canDelete:false },
      { id:'2b', type:'aneks',   number:'ANK/2025/0032', insuredName:'AGNIESZKA GÓRA',  amount:450.00,  inkasAmount:225.00,  date:'2025-03-25', canDelete:false },
    ],
  },
  {
    id:'3', number:'WYK/2025/0043', agentNumber:'AGT/003', agentName:'MAREK WIŚNIEWSKI',
    issueDate:'2025-04-08', modifiedDate:'2025-04-08', modifiedBy:'m.wisniewski',
    totalAmount:5600.00, inkasAmount:0,
    paymentStatus:'nieoplacony', statusCR:'oczekujacy', statusOddzial:'oczekujacy',
    messages:['Oczekiwanie na płatność'], notes:[
      { id:'n1', author:'admin', date:'2025-04-09', content:'Pośrednik poinformowany o zaległości. Termin płatności do 15.04.' }
    ], source:'SITU', blocked:false,
    items:[
      { id:'3a', type:'polisa',     number:'KM-H/P1149860-K3', insuredName:'KRZYSZTOF LITWIN',     amount:560.00, inkasAmount:0, date:'2025-04-01', canDelete:true  },
      { id:'3b', type:'kwitariusz', number:'KW/2025/004',      insuredName:'KRZYSZTOF LITWIN',     amount:568.20, inkasAmount:0, date:'2025-04-07', canDelete:true  },
    ],
  },
  {
    id:'4', number:'WYK/2025/0044', agentNumber:'AGT/001', agentName:'PIOTR KOWALSKI',
    issueDate:'2025-04-12', modifiedDate:'2025-04-14', modifiedBy:'p.kowalski',
    totalAmount:19800.00, inkasAmount:19800.00,
    paymentStatus:'oplacony', statusCR:'zatwierdzony', statusOddzial:'zatwierdzony',
    messages:[], notes:[], source:'SITU', blocked:false,
    items:[
      { id:'4a', type:'polisa', number:'KM-I/P0635780', insuredName:'MAŁGORZATA DĄBROWSKA', amount:990.00,  inkasAmount:990.00,  date:'2025-04-05', canDelete:false },
      { id:'4b', type:'polisa', number:'KE-F/P0055142', insuredName:'ANNA BEDNAREK',         amount:1450.00, inkasAmount:1450.00, date:'2025-04-05', canDelete:false },
    ],
  },
  {
    id:'5', number:'WYK/2025/0045', agentNumber:'AGT/004', agentName:'ZOFIA KAMIŃSKA',
    issueDate:'2025-04-15', modifiedDate:'2025-04-15', modifiedBy:'z.kaminska',
    totalAmount:3200.00, inkasAmount:3200.00,
    paymentStatus:'oplacony', statusCR:'zatwierdzony', statusOddzial:'oczekujacy',
    messages:['Weryfikacja oddziału w toku'], notes:[], source:'VERSIS', blocked:false,
    items:[
      { id:'5a', type:'polisa', number:'VKPA-A/S0032336', insuredName:'TADEUSZ PRZESTRZELSKI', amount:3200.00, inkasAmount:3200.00, date:'2025-04-10', canDelete:false },
    ],
  },
  {
    id:'6', number:'WYK/2025/0046', agentNumber:'AGT/002', agentName:'ANNA NOWAK',
    issueDate:'2025-04-18', modifiedDate:'2025-04-19', modifiedBy:'supervisor',
    totalAmount:7250.00, inkasAmount:7250.00,
    paymentStatus:'oplacony', statusCR:'zatwierdzony', statusOddzial:'zatwierdzony',
    messages:[], notes:[], source:'SITU', blocked:true,
    items:[
      { id:'6a', type:'polisa', number:'KM-I/P0638901', insuredName:'PIOTR WIŚNIEWSKI', amount:1890.00, inkasAmount:1890.00, date:'2025-04-12', canDelete:false },
    ],
  },
];

@Injectable({ providedIn: 'root' })
export class WykazService {
  readonly wykazy = signal<Wykaz[]>(MOCK);

  readonly filter = signal<WykazFilter>({
    agentNumber:'', wykazNumber:'', paymentStatus:'',
    policyNumber:'', dateFrom:'', dateTo:'',
    amountFrom:'', amountTo:'', statusCR:'', statusOddzial:'',
    last30Days: false,
  });

  readonly filtered = computed(() => {
    const f = this.filter();
    const cutoff = new Date(); cutoff.setDate(cutoff.getDate() - 30);
    return this.wykazy().filter(w => {
      if (f.wykazNumber  && !w.number.toLowerCase().includes(f.wykazNumber.toLowerCase()))  return false;
      if (f.agentNumber  && !w.agentNumber.toLowerCase().includes(f.agentNumber.toLowerCase())) return false;
      if (f.paymentStatus && w.paymentStatus !== f.paymentStatus) return false;
      if (f.statusCR     && w.statusCR !== f.statusCR)    return false;
      if (f.statusOddzial && w.statusOddzial !== f.statusOddzial) return false;
      if (f.policyNumber) {
        const match = w.items.some(i => i.number.toLowerCase().includes(f.policyNumber.toLowerCase()));
        if (!match) return false;
      }
      if (f.last30Days && new Date(w.issueDate) < cutoff) return false;
      if (f.amountFrom && w.totalAmount < +f.amountFrom) return false;
      if (f.amountTo   && w.totalAmount > +f.amountTo)   return false;
      return true;
    });
  });

  readonly resultCount = computed(() => this.filtered().length);

  getById(id: string): Wykaz | null {
    return this.wykazy().find(w => w.id === id) ?? null;
  }

  addNote(wykazId: string, content: string, author: string): void {
    const note: WykazNote = {
      id: Date.now().toString(),
      author, content,
      date: new Date().toISOString().slice(0, 10),
    };
    this.wykazy.update(list =>
      list.map(w => w.id === wykazId ? { ...w, notes: [...w.notes, note] } : w)
    );
  }

  removeItem(wykazId: string, itemId: string): void {
    this.wykazy.update(list =>
      list.map(w => w.id === wykazId
        ? { ...w, items: w.items.filter(i => i.id !== itemId) }
        : w)
    );
  }

  clearFilters(): void {
    this.filter.set({
      agentNumber:'', wykazNumber:'', paymentStatus:'',
      policyNumber:'', dateFrom:'', dateTo:'',
      amountFrom:'', amountTo:'', statusCR:'', statusOddzial:'',
      last30Days: false,
    });
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
