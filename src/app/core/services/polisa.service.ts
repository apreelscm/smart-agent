import { Injectable, computed, signal } from '@angular/core';
import { Polisa } from '../models/polisa.model';

const MOCK: Polisa[] = [
  { id:'1',  policyNumber:'KM-I/P0647405',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'STANISŁAW PAWŁOWSKI',   insurantName:'STANISŁAW PAWŁOWSKI',   issueDate:'2024-04-01', startDate:'2024-04-01', endDate:'2025-04-01', premium:1240.00, inkaso:1240.00, status:'aktywna',   agentName:'PIOTR KOWALSKI',    agentCode:'AGT/001', source:'SITU',   vehicleReg:'GD 12345', risks:[{name:'OC',premium:640,sum:0},{name:'AC',premium:600,sum:80000}] },
  { id:'2',  policyNumber:'KM-I/P0631516',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'JURAND GRABOWSKI',      insurantName:'JURAND GRABOWSKI',      issueDate:'2024-04-03', startDate:'2024-04-03', endDate:'2025-04-03', premium:876.00,  inkaso:876.00,  status:'aktywna',   agentName:'PIOTR KOWALSKI',    agentCode:'AGT/001', source:'SITU',   vehicleReg:'GD 67890', risks:[{name:'OC',premium:876,sum:0}] },
  { id:'3',  policyNumber:'KM-I/P0645576',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'AGNIESZKA GÓRA',        insurantName:'AGNIESZKA GÓRA',        issueDate:'2024-04-05', startDate:'2024-04-05', endDate:'2025-04-05', premium:2100.00, inkaso:2100.00, status:'w-trakcie', agentName:'ANNA NOWAK',        agentCode:'AGT/002', source:'VERSIS', vehicleReg:'PO 11111', risks:[{name:'OC',premium:900,sum:0},{name:'AC',premium:1200,sum:120000}] },
  { id:'4',  policyNumber:'KM-H/P1149860-K3', productType:'home',  productLabel:'Pakiet Mieszkanie+',insuredName:'KRZYSZTOF LITWIN',      insurantName:'KRZYSZTOF LITWIN',      issueDate:'2024-04-07', startDate:'2024-04-07', endDate:'2025-04-07', premium:560.00,  inkaso:0,       status:'zawieszona', agentName:'ANNA NOWAK',        agentCode:'AGT/002', source:'SITU',   address:'ul. Sopocka 12, Gdańsk', risks:[{name:'Mienie',premium:360,sum:500000},{name:'OC',premium:200,sum:100000}] },
  { id:'5',  policyNumber:'VKPA-A/S0032336',  productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'TADEUSZ PRZESTRZELSKI', insurantName:'TADEUSZ PRZESTRZELSKI', issueDate:'2024-04-09', startDate:'2024-04-09', endDate:'2025-04-09', premium:3200.00, inkaso:3200.00, status:'aktywna',   agentName:'MAREK WIŚNIEWSKI',  agentCode:'AGT/003', source:'VERSIS', vehicleReg:'WA 22222', risks:[{name:'OC',premium:1100,sum:0},{name:'AC',premium:2100,sum:200000}] },
  { id:'6',  policyNumber:'KM-I/P0635780',    productType:'home',  productLabel:'Pakiet Mieszkanie+',insuredName:'MAŁGORZATA DĄBROWSKA',  insurantName:'MAŁGORZATA DĄBROWSKA',  issueDate:'2024-04-11', startDate:'2024-04-11', endDate:'2025-04-11', premium:990.00,  inkaso:990.00,  status:'aktywna',   agentName:'MAREK WIŚNIEWSKI',  agentCode:'AGT/003', source:'SITU',   address:'ul. Długa 5, Gdańsk', risks:[{name:'Mienie',premium:590,sum:300000},{name:'OC',premium:400,sum:100000}] },
  { id:'7',  policyNumber:'KE-F/P0055142',    productType:'health',productLabel:'Pakiet Zdrowie',    insuredName:'ANNA BEDNAREK',         insurantName:'ANNA BEDNAREK',         issueDate:'2024-04-14', startDate:'2024-04-14', endDate:'2025-04-14', premium:1450.00, inkaso:1450.00, status:'aktywna',   agentName:'ZOFIA KAMIŃSKA',    agentCode:'AGT/004', source:'VERSIS', risks:[{name:'Hospitalizacja',premium:800,sum:50000},{name:'Ambulatorium',premium:650,sum:0}] },
  { id:'8',  policyNumber:'KM-I/P0636745',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'BOGUSŁAWA WRĘBIAK',     insurantName:'BOGUSŁAWA WRĘBIAK',     issueDate:'2023-05-01', startDate:'2023-05-01', endDate:'2024-05-01', premium:720.00,  inkaso:720.00,  status:'wygasla',   agentName:'ZOFIA KAMIŃSKA',    agentCode:'AGT/004', source:'SITU',   vehicleReg:'KR 33333', risks:[{name:'OC',premium:720,sum:0}] },
  { id:'9',  policyNumber:'KM-I/P0638901',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'PIOTR WIŚNIEWSKI',      insurantName:'PIOTR WIŚNIEWSKI',      issueDate:'2024-05-18', startDate:'2024-05-18', endDate:'2025-05-18', premium:1890.00, inkaso:1890.00, status:'aktywna',   agentName:'PIOTR KOWALSKI',    agentCode:'AGT/001', source:'SITU',   vehicleReg:'GD 44444', risks:[{name:'OC',premium:800,sum:0},{name:'AC',premium:1090,sum:150000}] },
  { id:'10', policyNumber:'KE-I/P0041237',    productType:'home',  productLabel:'Pakiet Mieszkanie+',insuredName:'MARTA KOWALSKA',        insurantName:'MARTA KOWALSKA',        issueDate:'2024-05-20', startDate:'2024-05-20', endDate:'2025-05-20', premium:430.00,  inkaso:430.00,  status:'aktywna',   agentName:'PIOTR KOWALSKI',    agentCode:'AGT/001', source:'VERSIS', address:'ul. Morska 21, Sopot', risks:[{name:'Mienie',premium:430,sum:200000}] },
  { id:'11', policyNumber:'KM-H/P1203456',    productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'ADAM NOWAK',            insurantName:'ADAM NOWAK',            issueDate:'2024-06-01', startDate:'2024-06-01', endDate:'2025-06-01', premium:1120.00, inkaso:560.00,  status:'w-trakcie', agentName:'ANNA NOWAK',        agentCode:'AGT/002', source:'SITU',   vehicleReg:'GD 55555', risks:[{name:'OC',premium:1120,sum:0}] },
  { id:'12', policyNumber:'VKPA-A/S0045678',  productType:'car',   productLabel:'Pakiet Auto+',      insuredName:'ZOFIA KAMIŃSKA SR.',    insurantName:'ZOFIA KAMIŃSKA SR.',    issueDate:'2023-06-15', startDate:'2023-06-15', endDate:'2024-06-15', premium:2350.00, inkaso:2350.00, status:'anulowana', agentName:'MAREK WIŚNIEWSKI',  agentCode:'AGT/003', source:'VERSIS', vehicleReg:'PO 66666', risks:[{name:'OC',premium:950,sum:0},{name:'AC',premium:1400,sum:180000}] },
];

@Injectable({ providedIn: 'root' })
export class PolisaService {
  readonly polisy = signal<Polisa[]>(MOCK);

  readonly filterProduct = signal('');
  readonly filterStatus  = signal('');
  readonly filterSearch  = signal('');
  readonly filterLast30  = signal(false);

  readonly filtered = computed(() => {
    const p   = this.filterProduct();
    const s   = this.filterStatus();
    const q   = this.filterSearch().toLowerCase();
    const d30 = this.filterLast30();
    const cutoff = new Date(); cutoff.setDate(cutoff.getDate() - 30);

    return this.polisy().filter(pol => {
      if (p  && pol.productType !== p) return false;
      if (s  && pol.status !== s) return false;
      if (q  && !pol.policyNumber.toLowerCase().includes(q) &&
                !pol.insuredName.toLowerCase().includes(q)) return false;
      if (d30 && new Date(pol.issueDate) < cutoff) return false;
      return true;
    });
  });

  readonly resultCount = computed(() => this.filtered().length);

  getById(id: string): Polisa | null {
    return this.polisy().find(p => p.id === id) ?? null;
  }

  clearFilters(): void {
    this.filterProduct.set('');
    this.filterStatus.set('');
    this.filterSearch.set('');
    this.filterLast30.set(false);
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
