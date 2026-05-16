import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';

export type OfertaStatus = 'aktywna' | 'wygasla' | 'zaakceptowana' | 'odrzucona' | 'w-trakcie';
export type OfertaProduct = 'auto' | 'dom' | 'zdrowie' | 'oc-komunikacyjne' | 'inne';

export interface Oferta {
  id: string;
  number: string;
  product: OfertaProduct;
  productLabel: string;
  clientName: string;
  clientPhone: string;
  issueDate: string;
  expiryDate: string;
  premium: number;
  status: OfertaStatus;
  agentName: string;
  source: string;
  notes: string;
}

const MOCK_OFERTY: Oferta[] = [
  { id:'1',  number:'OFF/2025/0141', product:'auto',             productLabel:'Pakiet Auto+',       clientName:'STANISŁAW PAWŁOWSKI',    clientPhone:'601 234 567', issueDate:'2025-04-01', expiryDate:'2025-05-01', premium:1240.00, status:'w-trakcie',    agentName:'PIOTR KOWALSKI', source:'Portal', notes:'' },
  { id:'2',  number:'OFF/2025/0142', product:'dom',              productLabel:'Pakiet Mieszkanie+',  clientName:'JURAND GRABOWSKI',       clientPhone:'602 345 678', issueDate:'2025-04-02', expiryDate:'2025-05-02', premium:876.00,  status:'zaakceptowana', agentName:'PIOTR KOWALSKI', source:'Telefon', notes:'Klient zainteresowany OC w pakiecie' },
  { id:'3',  number:'OFF/2025/0143', product:'zdrowie',          productLabel:'Pakiet Zdrowie',      clientName:'AGNIESZKA GÓRA',         clientPhone:'603 456 789', issueDate:'2025-04-03', expiryDate:'2025-05-03', premium:2100.00, status:'aktywna',      agentName:'ANNA NOWAK',     source:'Aplikacja', notes:'' },
  { id:'4',  number:'OFF/2025/0144', product:'oc-komunikacyjne', productLabel:'OC Komunikacyjne',    clientName:'KRZYSZTOF LITWIN',       clientPhone:'604 567 890', issueDate:'2025-04-05', expiryDate:'2025-04-20', premium:560.00,  status:'wygasla',      agentName:'ANNA NOWAK',     source:'Portal', notes:'' },
  { id:'5',  number:'OFF/2025/0145', product:'auto',             productLabel:'Pakiet Auto+',       clientName:'TADEUSZ PRZESTRZELSKI',  clientPhone:'605 678 901', issueDate:'2025-04-07', expiryDate:'2025-05-07', premium:3200.00, status:'w-trakcie',    agentName:'MAREK WIŚNIEWSKI', source:'Polecenie', notes:'Flota 3 pojazdów' },
  { id:'6',  number:'OFF/2025/0146', product:'dom',              productLabel:'Pakiet Mieszkanie+',  clientName:'MAŁGORZATA DĄBROWSKA',   clientPhone:'606 789 012', issueDate:'2025-04-09', expiryDate:'2025-05-09', premium:990.00,  status:'zaakceptowana', agentName:'MAREK WIŚNIEWSKI', source:'Telefon', notes:'' },
  { id:'7',  number:'OFF/2025/0147', product:'zdrowie',          productLabel:'Pakiet Zdrowie',      clientName:'ANNA BEDNAREK',          clientPhone:'607 890 123', issueDate:'2025-04-10', expiryDate:'2025-05-10', premium:1450.00, status:'odrzucona',    agentName:'ZOFIA KAMIŃSKA',  source:'Portal', notes:'Klient wybrał konkurencję' },
  { id:'8',  number:'OFF/2025/0148', product:'oc-komunikacyjne', productLabel:'OC Komunikacyjne',    clientName:'BOGUSŁAWA WRĘBIAK',      clientPhone:'608 901 234', issueDate:'2025-04-12', expiryDate:'2025-05-12', premium:720.00,  status:'aktywna',      agentName:'ZOFIA KAMIŃSKA',  source:'Aplikacja', notes:'' },
  { id:'9',  number:'OFF/2025/0149', product:'auto',             productLabel:'Pakiet Auto+',       clientName:'PIOTR WIŚNIEWSKI',       clientPhone:'609 012 345', issueDate:'2025-04-14', expiryDate:'2025-05-14', premium:1890.00, status:'w-trakcie',    agentName:'PIOTR KOWALSKI', source:'Portal', notes:'' },
  { id:'10', number:'OFF/2025/0150', product:'dom',              productLabel:'Pakiet Mieszkanie+',  clientName:'MARTA KOWALSKA',         clientPhone:'610 123 456', issueDate:'2025-04-15', expiryDate:'2025-05-15', premium:430.00,  status:'aktywna',      agentName:'PIOTR KOWALSKI', source:'Polecenie', notes:'' },
  { id:'11', number:'OFF/2025/0151', product:'inne',             productLabel:'Inne',                clientName:'ADAM NOWAK',             clientPhone:'611 234 567', issueDate:'2025-04-16', expiryDate:'2025-05-16', premium:1120.00, status:'w-trakcie',    agentName:'ANNA NOWAK',     source:'Telefon', notes:'' },
  { id:'12', number:'OFF/2025/0152', product:'auto',             productLabel:'Pakiet Auto+',       clientName:'ZOFIA KAMIŃSKA',         clientPhone:'612 345 678', issueDate:'2025-04-18', expiryDate:'2025-05-18', premium:2340.00, status:'zaakceptowana', agentName:'MAREK WIŚNIEWSKI', source:'Portal', notes:'Wznowienie' },
];

@Component({
  selector: 'app-oferty',
  standalone: true,
  imports: [FormsModule, MatTableModule, MatSortModule,
            MatIconModule, MatButtonModule, MatMenuModule, MatTooltipModule],
  templateUrl: './oferty.html',
  styleUrl: './oferty.scss',
})
export class OfertyComponent implements AfterViewInit {
  readonly dataSource = new MatTableDataSource<Oferta>(MOCK_OFERTY);
  readonly displayedColumns = ['product','number','client','dates','premium','source','status','actions'];

  readonly filterStatus  = signal('');
  readonly filterProduct = signal('');
  readonly filterClient  = signal('');
  readonly filterLast30  = signal(false);
  readonly expandedFilter = signal<string | null>(null);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => {
      const s   = this.filterStatus();
      const p   = this.filterProduct();
      const c   = this.filterClient().toLowerCase();
      const d30 = this.filterLast30();
      const cutoff = new Date(); cutoff.setDate(cutoff.getDate() - 30);

      this.dataSource.data = MOCK_OFERTY.filter(o => {
        if (s  && o.status  !== s) return false;
        if (p  && o.product !== p) return false;
        if (c  && !o.clientName.toLowerCase().includes(c)) return false;
        if (d30 && new Date(o.issueDate) < cutoff) return false;
        return true;
      });
    });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get resultCount(): number { return this.dataSource.data.length; }
  get hasFilter(): boolean { return !!(this.filterStatus() || this.filterProduct() || this.filterClient() || this.filterLast30()); }

  setStatus(s: string)  { this.filterStatus.update(v => v === s ? '' : s); }
  setProduct(p: string) { this.filterProduct.update(v => v === p ? '' : p); }
  toggleLast30()        { this.filterLast30.update(v => !v); }
  toggleFilter(f: string) { this.expandedFilter.update(v => v === f ? null : f); }
  clearFilters() { this.filterStatus.set(''); this.filterProduct.set(''); this.filterClient.set(''); this.filterLast30.set(false); this.expandedFilter.set(null); }

  productIcon(p: OfertaProduct): string {
    return { auto:'directions_car', dom:'home', zdrowie:'monitor_heart', 'oc-komunikacyjne':'car_crash', inne:'description' }[p] ?? 'description';
  }

  statusLabel(s: OfertaStatus): string {
    return { aktywna:'Aktywna', wygasla:'Wygasła', zaakceptowana:'Zaakceptowana', odrzucona:'Odrzucona', 'w-trakcie':'W trakcie' }[s] ?? s;
  }

  formatAmount(n: number): string { return n.toFixed(2).replace('.', ',') + ' zł'; }
}
