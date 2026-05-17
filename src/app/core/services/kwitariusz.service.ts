import { Injectable, computed, effect, signal } from '@angular/core';
import {
  Kwitariusz,
  KwitariuszStatus,
  KwitariuszType,
  MockPolicy,
  PolicyStatus,
} from '../models/kwitariusz.model';

const MOCK_KWITARIUSZE: Kwitariusz[] = [
  {
    id:'1', number:'KW/2025/001', type:'rata-odsetki', policyNumber:'KM-I/P0647405',
    insuredName:'STANISŁAW PAWŁOWSKI', issueDate:'2025-04-01',
    baseAmount:310.00, interest:18.50, status:'wystawiony', source:'SITU',
    payerName:'STANISŁAW PAWŁOWSKI', payerPeselNip:'62010112345',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Gdańska 12, 80-001 Gdańsk',
    payerEmail:'s.pawlowski@email.pl', payerPhone:'601 234 567',
    finCurrency:'PLN', finPaymentDeadline:'2025-07-15', finInterestDate:'2025-04-01',
    finInterestRate:7, finDaysOverdue:29,
    installmentPlan:[
      { number:1, dueDate:'2025-01-15', amount:310.00, paid:true, paidDate:'2025-01-10' },
      { number:2, dueDate:'2025-04-15', amount:310.00, paid:true, paidDate:'2025-04-12' },
      { number:3, dueDate:'2025-07-15', amount:310.00, paid:false },
      { number:4, dueDate:'2025-10-15', amount:310.00, paid:false },
    ],
  },
  {
    id:'2', number:'KW/2025/002', type:'rekalkulacja-odsetki', policyNumber:'KM-I/P0631516',
    insuredName:'JURAND GRABOWSKI', issueDate:'2025-04-03',
    baseAmount:876.00, interest:12.30, status:'oplacony', source:'VERSIS',
    payerName:'JURAND GRABOWSKI', payerPeselNip:'7705031234',
    payerClientType:'firma', payerAddress:'ul. Sopocka 8, 81-001 Sopot',
    payerEmail:'grabowski@firma.pl', payerPhone:'602 345 678',
    finCurrency:'PLN', finPaymentDeadline:'2025-04-30', finInterestDate:'2025-04-03',
    finInterestRate:7, finDaysOverdue:16,
    rekalkReason:'Zmiana sumy ubezpieczenia',
    rekalkDate:'2025-04-03',
    rekalkAmountBefore:876.00, rekalkAmountAfter:924.50,
  },
  {
    id:'3', number:'KW/2025/003', type:'rata-odsetki', policyNumber:'KM-I/P0645576',
    insuredName:'AGNIESZKA GÓRA', issueDate:'2025-04-05',
    baseAmount:700.00, interest:31.40, status:'wystawiony', source:'SITU',
    payerName:'AGNIESZKA GÓRA', payerPeselNip:'85091598765',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Morska 21, 81-225 Gdynia',
    payerEmail:'a.gora@mail.com', payerPhone:'603 456 789',
    finCurrency:'PLN', finPaymentDeadline:'2025-09-20', finInterestDate:'2025-04-05',
    finInterestRate:7, finDaysOverdue:46,
    installmentPlan:[
      { number:1, dueDate:'2025-01-20', amount:700.00, paid:true, paidDate:'2025-01-18' },
      { number:2, dueDate:'2025-05-20', amount:700.00, paid:true, paidDate:'2025-05-19' },
      { number:3, dueDate:'2025-09-20', amount:700.00, paid:false },
    ],
  },
  {
    id:'4', number:'KW/2025/004', type:'rata-odsetki', policyNumber:'KM-H/P1149860-K3',
    insuredName:'KRZYSZTOF LITWIN', issueDate:'2025-04-07',
    baseAmount:560.00, interest:8.20, status:'anulowany', source:'SITU',
    payerName:'KRZYSZTOF LITWIN', payerClientType:'osoba-fizyczna',
    finCurrency:'PLN', finInterestRate:7, finDaysOverdue:5,
  },
  {
    id:'5', number:'KW/2025/005', type:'rekalkulacja-odsetki', policyNumber:'VKPA-A/S0032336',
    insuredName:'TADEUSZ PRZESTRZELSKI', issueDate:'2025-04-09',
    baseAmount:3200.00, interest:47.80, status:'oplacony', source:'VERSIS',
    payerName:'TADEUSZ PRZESTRZELSKI', payerPeselNip:'NIP: 7261234567',
    payerClientType:'firma', payerAddress:'al. Niepodległości 100, 00-950 Warszawa',
    payerEmail:'t.przestrzelski@corp.pl', payerPhone:'605 678 901',
    finCurrency:'PLN', finPaymentDeadline:'2025-04-30', finInterestDate:'2025-04-09',
    finInterestRate:7, finDaysOverdue:20,
    rekalkReason:'Zmiana zakresu ubezpieczenia',
    rekalkDate:'2025-04-09',
    rekalkAmountBefore:3200.00, rekalkAmountAfter:2840.00,
  },
  {
    id:'6', number:'KW/2025/006', type:'rata-odsetki', policyNumber:'KM-I/P0635780',
    insuredName:'MAŁGORZATA DĄBROWSKA', issueDate:'2025-04-11',
    baseAmount:330.00, interest:14.60, status:'wystawiony', source:'SITU',
    payerName:'MAŁGORZATA DĄBROWSKA', payerPeselNip:'78032056789',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Długa 5, 80-827 Gdańsk',
    payerEmail:'m.dabrowska@email.pl', payerPhone:'606 789 012',
    finCurrency:'PLN', finPaymentDeadline:'2025-05-10', finInterestDate:'2025-04-11',
    finInterestRate:7, finDaysOverdue:16,
    installmentPlan:[
      { number:1, dueDate:'2024-11-10', amount:330.00, paid:true, paidDate:'2024-11-08' },
      { number:2, dueDate:'2025-02-10', amount:330.00, paid:true, paidDate:'2025-02-09' },
      { number:3, dueDate:'2025-05-10', amount:330.00, paid:false },
    ],
  },
  {
    id:'7', number:'KW/2025/007', type:'rata-odsetki', policyNumber:'KE-F/P0055142',
    insuredName:'ANNA BEDNAREK', issueDate:'2025-04-14',
    baseAmount:362.50, interest:21.70, status:'oczekujacy', source:'VERSIS',
    payerName:'ANNA BEDNAREK', payerPeselNip:'91040345678',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Oliwska 34, 80-563 Gdańsk',
    payerEmail:'anna.bednarek@gmail.com', payerPhone:'607 890 123',
    finCurrency:'PLN', finPaymentDeadline:'2025-06-01', finInterestDate:'2025-04-14',
    finInterestRate:7, finDaysOverdue:22,
    installmentPlan:[
      { number:1, dueDate:'2025-03-01', amount:362.50, paid:true, paidDate:'2025-02-28' },
      { number:2, dueDate:'2025-06-01', amount:362.50, paid:false },
      { number:3, dueDate:'2025-09-01', amount:362.50, paid:false },
      { number:4, dueDate:'2025-12-01', amount:362.50, paid:false },
    ],
  },
  {
    id:'8', number:'KW/2025/008', type:'rekalkulacja-odsetki', policyNumber:'KM-I/P0636745',
    insuredName:'BOGUSŁAWA WRĘBIAK', issueDate:'2025-04-16',
    baseAmount:720.00, interest:10.80, status:'wystawiony', source:'SITU',
    payerName:'BOGUSŁAWA WRĘBIAK', payerPeselNip:'69071267890',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Leśna 7, 83-000 Pruszcz Gdański',
    finCurrency:'PLN', finInterestRate:7, finDaysOverdue:5,
    rekalkReason:'Korekta błędu w polisie',
    rekalkDate:'2025-04-16',
    rekalkAmountBefore:720.00, rekalkAmountAfter:756.00,
  },
  {
    id:'9', number:'KW/2025/009', type:'rata-odsetki', policyNumber:'KM-I/P0638901',
    insuredName:'PIOTR WIŚNIEWSKI', issueDate:'2025-04-18',
    baseAmount:1890.00, interest:28.30, status:'oplacony', source:'SITU',
    payerName:'PIOTR WIŚNIEWSKI', payerPeselNip:'83112378901',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Kościuszki 22, 81-701 Sopot',
    payerEmail:'p.wisniewski@wp.pl', payerPhone:'609 012 345',
    finCurrency:'PLN', finPaymentDeadline:'2025-05-18', finInterestDate:'2025-04-18',
    finInterestRate:7, finDaysOverdue:20,
  },
  {
    id:'10', number:'KW/2025/010', type:'rekalkulacja-odsetki', policyNumber:'KE-I/P0041237',
    insuredName:'MARTA KOWALSKA', issueDate:'2025-04-20',
    baseAmount:430.00, interest:6.40, status:'wystawiony', source:'VERSIS',
    payerName:'MARTA KOWALSKA', payerPeselNip:'90032489012',
    payerClientType:'osoba-fizyczna', payerAddress:'ul. Różana 3, 80-251 Gdańsk',
    payerEmail:'marta.kowalska@email.com', payerPhone:'610 123 456',
    finCurrency:'PLN', finInterestRate:7, finDaysOverdue:5,
    rekalkReason:'Zmiana danych pojazdu',
    rekalkDate:'2025-04-20',
    rekalkAmountBefore:430.00, rekalkAmountAfter:398.50,
  },
];

const MOCK_POLICIES: MockPolicy[] = [
  {
    series:'KM-I', number:'P0647405', insuredName:'STANISŁAW PAWŁOWSKI', status:'aktywna', baseAmount:1240.00,
    installmentPlan: [
      { number:1, dueDate:'2025-01-15', amount:310.00, paid:true, paidDate:'2025-01-10' },
      { number:2, dueDate:'2025-04-15', amount:310.00, paid:true, paidDate:'2025-04-12' },
      { number:3, dueDate:'2025-07-15', amount:310.00, paid:false },
      { number:4, dueDate:'2025-10-15', amount:310.00, paid:false },
    ],
  },
  {
    series:'KM-I', number:'P0631516', insuredName:'JURAND GRABOWSKI', status:'oplacona', baseAmount:876.00,
    installmentPlan: [
      { number:1, dueDate:'2025-02-01', amount:438.00, paid:true, paidDate:'2025-01-28' },
      { number:2, dueDate:'2025-08-01', amount:438.00, paid:true, paidDate:'2025-07-30' },
    ],
  },
  {
    series:'KM-I', number:'P0645576', insuredName:'AGNIESZKA GÓRA', status:'rozliczona', baseAmount:2100.00,
    installmentPlan: [
      { number:1, dueDate:'2025-01-20', amount:700.00, paid:true, paidDate:'2025-01-18' },
      { number:2, dueDate:'2025-05-20', amount:700.00, paid:true, paidDate:'2025-05-19' },
      { number:3, dueDate:'2025-09-20', amount:700.00, paid:false },
    ],
  },
  { series:'KM-H', number:'P1149860-K3', insuredName:'KRZYSZTOF LITWIN', status:'anulowana', baseAmount:560.00 },
  { series:'VKPA-A', number:'S0032336', insuredName:'TADEUSZ PRZESTRZELSKI', status:'rozwiazana', baseAmount:3200.00 },
  {
    series:'KM-I', number:'P0635780', insuredName:'MAŁGORZATA DĄBROWSKA', status:'ochrona-skonczona', baseAmount:990.00,
    installmentPlan: [
      { number:1, dueDate:'2024-11-10', amount:330.00, paid:true, paidDate:'2024-11-08' },
      { number:2, dueDate:'2025-02-10', amount:330.00, paid:true, paidDate:'2025-02-09' },
      { number:3, dueDate:'2025-05-10', amount:330.00, paid:false },
    ],
  },
  {
    series:'KE-F', number:'P0055142', insuredName:'ANNA BEDNAREK', status:'aktywna', baseAmount:1450.00,
    installmentPlan: [
      { number:1, dueDate:'2025-03-01', amount:362.50, paid:true, paidDate:'2025-02-28' },
      { number:2, dueDate:'2025-06-01', amount:362.50, paid:false },
      { number:3, dueDate:'2025-09-01', amount:362.50, paid:false },
      { number:4, dueDate:'2025-12-01', amount:362.50, paid:false },
    ],
  },
];

export const POLICY_STATUS_WARNINGS: Partial<Record<PolicyStatus, { type: 'warning' | 'error'; message: string }>> = {
  rozliczona: { type:'warning', message:'Uwaga: polisa jest już rozliczona. Rejestracja kwitariusza jest możliwa, ale wymaga potwierdzenia.' },
  rozwiazana: { type:'warning', message:'Uwaga: polisa jest rozwiązana. Rejestracja kwitariusza jest możliwa, ale wymaga potwierdzenia.' },
  oplacona: { type:'warning', message:'Uwaga: polisa jest już opłacona. Sprawdź poprawność danych przed wystawieniem kwitariusza.' },
  'ochrona-skonczona': { type:'warning', message:'Uwaga: okres ochrony tej polisy już się skończył. Rejestracja kwitariusza jest możliwa, ale wymaga potwierdzenia.' },
  anulowana: { type:'error', message:'Polisa została anulowana. Rejestracja nowego kwitariusza dla tej polisy jest niemożliwa.' },
};

@Injectable({ providedIn: 'root' })
export class KwitariuszService {
  readonly kwitariusze = signal<Kwitariusz[]>(MOCK_KWITARIUSZE);

  readonly filterType = signal('');
  readonly filterLast30Days = signal(false);
  readonly filterPolicySearch = signal('');
  readonly filterInsuredSearch = signal('');
  readonly filterStatuses = signal<KwitariuszStatus[]>([]);

  readonly baseFiltered = computed(() => {
    const t = this.filterType();
    const d30 = this.filterLast30Days();
    const pol = this.filterPolicySearch().toLowerCase();
    const ins = this.filterInsuredSearch().toLowerCase();
    const cutoff = new Date();
    cutoff.setDate(cutoff.getDate() - 30);

    return this.kwitariusze().filter(k => {
      if (t && k.type !== t) return false;
      if (d30 && new Date(k.issueDate) < cutoff) return false;
      if (pol && !k.policyNumber.toLowerCase().includes(pol)) return false;
      if (ins && !k.insuredName.toLowerCase().includes(ins)) return false;
      return true;
    });
  });

  readonly availableStatuses = computed(() => {
    const statuses = this.baseFiltered().map(kwitariusz => kwitariusz.status);
    return Array.from(new Set(statuses));
  });

  readonly filtered = computed(() => {
    const selectedStatuses = this.filterStatuses();

    if (!selectedStatuses.length) {
      return this.baseFiltered();
    }

    const selectedSet = new Set(selectedStatuses);
    return this.baseFiltered().filter(kwitariusz => selectedSet.has(kwitariusz.status));
  });

  readonly resultCount = computed(() => this.filtered().length);

  constructor() {
    effect(() => {
      const selectedStatuses = this.filterStatuses();
      const availableStatuses = new Set(this.availableStatuses());
      const reconciledStatuses = selectedStatuses.filter(status => availableStatuses.has(status));

      if (reconciledStatuses.length !== selectedStatuses.length) {
        this.filterStatuses.set(reconciledStatuses);
      }
    });
  }

  searchPolicy(series: string, number: string): MockPolicy | null {
    const s = series.trim().toUpperCase();
    const n = number.trim().toUpperCase();
    return MOCK_POLICIES.find(p =>
      p.series.toUpperCase() === s && p.number.toUpperCase() === n,
    ) ?? null;
  }

  getById(id: string): Kwitariusz | null {
    return this.kwitariusze().find(k => k.id === id) ?? null;
  }

  addKwitariusz(k: Omit<Kwitariusz, 'id'>): void {
    const id = String(this.kwitariusze().length + 1);
    this.kwitariusze.update(list => [{ id, ...k }, ...list]);
  }

  updateKwitariusz(id: string, changes: Partial<Omit<Kwitariusz, 'id'>>): void {
    this.kwitariusze.update(list =>
      list.map(k => k.id === id ? { ...k, ...changes } : k),
    );
  }

  toggleStatus(status: KwitariuszStatus): void {
    this.filterStatuses.update(selectedStatuses =>
      selectedStatuses.includes(status)
        ? selectedStatuses.filter(selectedStatus => selectedStatus !== status)
        : [...selectedStatuses, status],
    );
  }

  clearStatusFilter(): void {
    this.filterStatuses.set([]);
  }

  clearFilters(): void {
    this.filterType.set('');
    this.filterLast30Days.set(false);
    this.filterPolicySearch.set('');
    this.filterInsuredSearch.set('');
    this.clearStatusFilter();
  }
}
