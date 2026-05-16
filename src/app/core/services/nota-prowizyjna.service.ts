import { Injectable, computed, signal } from '@angular/core';
import { NotaFilter, NotaProwizyjna } from '../models/nota-prowizyjna.model';

const MOCK: NotaProwizyjna[] = [
  {
    id: '1', number: 'NP/2025/0041', issueDate: '2025-04-01', type: 'podstawowa',
    agencyOwner: 'KOWALSKI', agentNumber: 'AGT/001', agentName: 'PIOTR KOWALSKI',
    branchCodes: ['Gdańsk-03/2018', 'Gdańsk-03/3002'], isDebtor: false,
    totalCommission: 109.58, status: 'wyplacona', showCommission: true,
    rateSettings: ['Gdańsk(03/2018, 03/3002)', 'Sopot(30/002)', 'Pzm(30/002)'],
    categories: [
      { name:'dobrowolne',  damages: 26698.38, inkasAmount: 28527.03, lossRatio: 93.59, lossRatioThreshold: 50, commissionRate: 0,    commissionBase: 0,         commissionAmount: 0      },
      { name:'obowiazkowe', damages:-16574.76, inkasAmount: 78776.97, lossRatio:-21.04, lossRatioThreshold: 50, commissionRate: 1,    commissionBase: 10957.85,  commissionAmount: 109.58 },
    ],
    items: [
      { id:'1a', policyNumber:'KM-I/P0647405', insuredName:'STANISŁAW PAWŁOWSKI', issueDate:'2025-03-15', baseAmount:1240.00, inkasAmount:1240.00, risk:'OC',  riskCode:'1001', commissionRate:1.0, commissionAmount:12.40,  ofwcName:'JAN NOWAK',   ofwcNumber:'OFW/001', agentNumber:'AGT/001', agencyCode:'Gdańsk' },
      { id:'1b', policyNumber:'KM-I/P0631516', insuredName:'JURAND GRABOWSKI',    issueDate:'2025-03-20', baseAmount:876.00,  inkasAmount:876.00,  risk:'AC',  riskCode:'1002', commissionRate:0.0, commissionAmount:0,      ofwcName:'JAN NOWAK',   ofwcNumber:'OFW/001', agentNumber:'AGT/001', agencyCode:'Gdańsk' },
      { id:'1c', policyNumber:'KM-I/P0645576', insuredName:'AGNIESZKA GÓRA',      issueDate:'2025-03-22', baseAmount:2100.00, inkasAmount:2100.00, risk:'OC',  riskCode:'1001', commissionRate:1.0, commissionAmount:21.00,  ofwcName:'ANNA KOWAL',  ofwcNumber:'OFW/002', agentNumber:'AGT/001', agencyCode:'Sopot'  },
      { id:'1d', policyNumber:'KE-F/P0055142', insuredName:'ANNA BEDNAREK',       issueDate:'2025-03-28', baseAmount:1450.00, inkasAmount:1450.00, risk:'NNW', riskCode:'1005', commissionRate:1.0, commissionAmount:14.50,  ofwcName:'ANNA KOWAL',  ofwcNumber:'OFW/002', agentNumber:'AGT/001', agencyCode:'Sopot'  },
    ],
  },
  {
    id: '2', number: 'NP/2025/0042', issueDate: '2025-04-05', type: 'dodatkowa',
    agencyOwner: 'NOWAK', agentNumber: 'AGT/002', agentName: 'ANNA NOWAK',
    branchCodes: ['Sopot-30/002'], isDebtor: false,
    totalCommission: 1845.20, status: 'oczekujaca', showCommission: true,
    rateSettings: ['Sopot(30/002)', 'Trójmiasto(40/001)'],
    categories: [
      { name:'dobrowolne',  damages: 8200.00,  inkasAmount: 32000.00, lossRatio: 25.63, lossRatioThreshold: 50, commissionRate: 3,    commissionBase: 32000.00,  commissionAmount: 960.00 },
      { name:'obowiazkowe', damages: 4100.00,  inkasAmount: 29510.00, lossRatio: 13.89, lossRatioThreshold: 50, commissionRate: 3,    commissionBase: 29510.00,  commissionAmount: 885.20 },
    ],
    items: [
      { id:'2a', policyNumber:'VKPA-A/S0032336', insuredName:'TADEUSZ PRZESTRZELSKI', issueDate:'2025-04-01', baseAmount:3200.00, inkasAmount:3200.00, risk:'OC', riskCode:'1001', commissionRate:3.0, commissionAmount:96.00, ofwcName:'MAREK WIŚNIEWSKI', ofwcNumber:'OFW/003', agentNumber:'AGT/002', agencyCode:'Sopot' },
      { id:'2b', policyNumber:'KM-I/P0635780',   insuredName:'MAŁGORZATA DĄBROWSKA', issueDate:'2025-04-03', baseAmount:990.00,  inkasAmount:990.00,  risk:'OC', riskCode:'1001', commissionRate:3.0, commissionAmount:29.70, ofwcName:'MAREK WIŚNIEWSKI', ofwcNumber:'OFW/003', agentNumber:'AGT/002', agencyCode:'Sopot' },
    ],
  },
  {
    id: '3', number: 'NP/2025/0043', issueDate: '2025-04-10', type: 'podstawowa',
    agencyOwner: 'WIŚNIEWSKI', agentNumber: 'AGT/003', agentName: 'MAREK WIŚNIEWSKI',
    branchCodes: ['Pzm-30/002'], isDebtor: true,
    totalCommission: 0, status: 'zablokowana', showCommission: false,
    rateSettings: ['Pzm(30/002)'],
    categories: [
      { name:'dobrowolne',  damages: 45000.00, inkasAmount: 12000.00, lossRatio: 375.00, lossRatioThreshold: 50, commissionRate: 0, commissionBase: 0, commissionAmount: 0 },
      { name:'obowiazkowe', damages: 5200.00,  inkasAmount: 8100.00,  lossRatio: 64.20,  lossRatioThreshold: 50, commissionRate: 0, commissionBase: 0, commissionAmount: 0 },
    ],
    items: [
      { id:'3a', policyNumber:'KM-H/P1149860-K3', insuredName:'KRZYSZTOF LITWIN', issueDate:'2025-04-05', baseAmount:560.00, inkasAmount:0, risk:'AC', riskCode:'1002', commissionRate:0, commissionAmount:0, ofwcName:'ZOFIA KAMIŃSKA', ofwcNumber:'OFW/004', agentNumber:'AGT/003', agencyCode:'Pzm' },
    ],
  },
  {
    id: '4', number: 'NP/2025/0044', issueDate: '2025-04-15', type: 'podstawowa',
    agencyOwner: 'KAMIŃSKA', agentNumber: 'AGT/004', agentName: 'ZOFIA KAMIŃSKA',
    branchCodes: ['Gdańsk-03/2018', 'Pzm-30/002'], isDebtor: false,
    totalCommission: 456.90, status: 'oczekujaca', showCommission: true,
    rateSettings: ['Gdańsk(03/2018)', 'Pzm(30/002)'],
    categories: [
      { name:'dobrowolne',  damages: 3200.00, inkasAmount: 18000.00, lossRatio: 17.78, lossRatioThreshold: 50, commissionRate: 2.5, commissionBase: 18000.00, commissionAmount: 450.00 },
      { name:'obowiazkowe', damages: 140.00,  inkasAmount: 6900.00,  lossRatio:  2.03, lossRatioThreshold: 50, commissionRate: 0.1, commissionBase: 6900.00,  commissionAmount:   6.90 },
    ],
    items: [
      { id:'4a', policyNumber:'KM-I/P0638901', insuredName:'PIOTR WIŚNIEWSKI',    issueDate:'2025-04-10', baseAmount:1890.00, inkasAmount:1890.00, risk:'OC',  riskCode:'1001', commissionRate:2.5, commissionAmount:47.25, ofwcName:'PIOTR KOWALSKI', ofwcNumber:'OFW/001', agentNumber:'AGT/004', agencyCode:'Gdańsk' },
      { id:'4b', policyNumber:'KE-I/P0041237', insuredName:'MARTA KOWALSKA',      issueDate:'2025-04-12', baseAmount:430.00,  inkasAmount:430.00,  risk:'NNW', riskCode:'1005', commissionRate:2.5, commissionAmount:10.75, ofwcName:'PIOTR KOWALSKI', ofwcNumber:'OFW/001', agentNumber:'AGT/004', agencyCode:'Gdańsk' },
    ],
  },
  {
    id: '5', number: 'NP/2025/0045', issueDate: '2025-04-20', type: 'dodatkowa',
    agencyOwner: 'KOWALSKI', agentNumber: 'AGT/001', agentName: 'PIOTR KOWALSKI',
    branchCodes: ['Gdańsk-03/2018'], isDebtor: false,
    totalCommission: 3200.00, status: 'oczekujaca', showCommission: true,
    rateSettings: ['Gdańsk(03/2018)'],
    categories: [
      { name:'dobrowolne',  damages: 2100.00, inkasAmount: 85000.00, lossRatio:  2.47, lossRatioThreshold: 50, commissionRate: 3.5, commissionBase: 85000.00, commissionAmount: 2975.00 },
      { name:'obowiazkowe', damages:  800.00, inkasAmount: 16400.00, lossRatio:  4.88, lossRatioThreshold: 50, commissionRate: 1.4, commissionBase: 16400.00, commissionAmount:  225.00 },
    ],
    items: [],
  },
];

@Injectable({ providedIn: 'root' })
export class NotaProwizyjnaService {
  readonly noty = signal<NotaProwizyjna[]>(MOCK);

  readonly filter = signal<NotaFilter>({
    agencyOwner:'', dateFrom:'', dateTo:'',
    type:'', amountFrom:'', amountTo:'',
    notaNumber:'', policyNumber:'',
    showCommission: true, last30Days: false,
  });

  readonly filtered = computed(() => {
    const f = this.filter();
    const cutoff = new Date(); cutoff.setDate(cutoff.getDate() - 30);
    return this.noty().filter(n => {
      if (f.type         && n.type !== f.type) return false;
      if (f.agencyOwner  && !n.agencyOwner.toLowerCase().includes(f.agencyOwner.toLowerCase())) return false;
      if (f.notaNumber   && !n.number.toLowerCase().includes(f.notaNumber.toLowerCase())) return false;
      if (f.policyNumber) {
        const hit = n.items.some(i => i.policyNumber.toLowerCase().includes(f.policyNumber.toLowerCase()));
        if (!hit) return false;
      }
      if (f.amountFrom && n.totalCommission < +f.amountFrom) return false;
      if (f.amountTo   && n.totalCommission > +f.amountTo)   return false;
      if (f.last30Days && new Date(n.issueDate) < cutoff)     return false;
      return true;
    });
  });

  readonly resultCount = computed(() => this.filtered().length);

  getById(id: string): NotaProwizyjna | null {
    return this.noty().find(n => n.id === id) ?? null;
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
