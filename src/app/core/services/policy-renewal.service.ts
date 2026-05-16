import { Injectable, computed, signal } from '@angular/core';
import { PolicyRenewal, PolicyRenewalFilter } from '../models/policy-renewal.model';

const MOCK_RENEWALS: PolicyRenewal[] = [
  { id:'1',  productType:'car', policyNumber:'KM-I/P0647405',     insuredName:'STANISŁAW PAWŁOWSKI',    daysToEnd:29, identifier:'WOSE449',  hasRenewAction:false },
  { id:'2',  productType:'car', policyNumber:'KM-I/P0631516',     insuredName:'JURAND GRABOWSKI',       daysToEnd:29, identifier:'SZA64EY',  hasRenewAction:false },
  { id:'3',  productType:'car', policyNumber:'KM-I/P0645576',     insuredName:'AGNIESZKA GÓRA',         daysToEnd:29, identifier:'LZA42041', hasRenewAction:false },
  { id:'4',  productType:'car', policyNumber:'KM-H/P1149860-K3',  insuredName:'KRZYSZTOF LITWIN',       daysToEnd:29, identifier:'SD0638K',  hasRenewAction:true  },
  { id:'5',  productType:'car', policyNumber:'VKPA-A/S0032336',   insuredName:'TADEUSZ PRZESTRZELSKI',  daysToEnd:29, identifier:'BIAML94',  hasRenewAction:false },
  { id:'6',  productType:'car', policyNumber:'KM-I/P0635780',     insuredName:'MAŁGORZATA DĄBROWSKA',   daysToEnd:29, identifier:'RJST787',  hasRenewAction:false },
  { id:'7',  productType:'car', policyNumber:'KE-F/P0055142',     insuredName:'ANNA BEDNAREK',          daysToEnd:29, identifier:'ZKO66M6',  hasRenewAction:false },
  { id:'8',  productType:'car', policyNumber:'KM-I/P0636745',     insuredName:'BOGUSŁAWA WRĘBIAK',      daysToEnd:29, identifier:'RLU84W2',  hasRenewAction:false },
  { id:'9',  productType:'car', policyNumber:'KM-I/P0638901',     insuredName:'PIOTR WIŚNIEWSKI',       daysToEnd:28, identifier:'TKL9823',  hasRenewAction:true  },
  { id:'10', productType:'car', policyNumber:'KE-I/P0041237',     insuredName:'MARTA KOWALSKA',         daysToEnd:28, identifier:'DFG4521',  hasRenewAction:false },
  { id:'11', productType:'car', policyNumber:'KM-H/P1203456',     insuredName:'ADAM NOWAK',             daysToEnd:27, identifier:'QWE7654',  hasRenewAction:false },
  { id:'12', productType:'car', policyNumber:'VKPA-A/S0045678',   insuredName:'ZOFIA KAMIŃSKA',         daysToEnd:27, identifier:'ASD3214',  hasRenewAction:true  },
  { id:'13', productType:'car', policyNumber:'KM-I/P0712345',     insuredName:'JAN LEWANDOWSKI',        daysToEnd:26, identifier:'ZXC8976',  hasRenewAction:false },
  { id:'14', productType:'car', policyNumber:'KE-F/P0067890',     insuredName:'BARBARA WÓJCIK',         daysToEnd:25, identifier:'POI1234',  hasRenewAction:false },
  { id:'15', productType:'car', policyNumber:'KM-I/P0698765',     insuredName:'TOMASZ SZYMAŃSKI',       daysToEnd:24, identifier:'LKJ5678',  hasRenewAction:true  },
  { id:'16', productType:'car', policyNumber:'KM-H/P1187654',     insuredName:'EWA WOŹNIAK',            daysToEnd:23, identifier:'MNB9012',  hasRenewAction:false },
  { id:'17', productType:'car', policyNumber:'KE-I/P0023456',     insuredName:'ROBERT KOZŁOWSKI',       daysToEnd:22, identifier:'VCX3456',  hasRenewAction:false },
  { id:'18', productType:'car', policyNumber:'KM-I/P0654321',     insuredName:'KATARZYNA JABŁOŃSKA',    daysToEnd:21, identifier:'BNM7890',  hasRenewAction:true  },
  { id:'19', productType:'car', policyNumber:'VKPA-A/S0089012',   insuredName:'MAREK WIŚNIEWSKA',       daysToEnd:20, identifier:'QAZ2345',  hasRenewAction:false },
  { id:'20', productType:'car', policyNumber:'KM-I/P0743210',     insuredName:'ANNA DĄBROWSKI',         daysToEnd:19, identifier:'WSX6789',  hasRenewAction:false },
];

@Injectable({ providedIn: 'root' })
export class PolicyRenewalService {
  readonly filter = signal<PolicyRenewalFilter>({ product: 'car', policyNumber: '', client: '', regNumber: '' });

  readonly filteredRenewals = computed(() => {
    const f = this.filter();
    return MOCK_RENEWALS.filter(r => {
      if (f.product && r.productType !== f.product) return false;
      if (f.client && !r.insuredName.toLowerCase().includes(f.client.toLowerCase())) return false;
      if (f.policyNumber && !r.policyNumber.toLowerCase().includes(f.policyNumber.toLowerCase())) return false;
      return true;
    });
  });

  readonly resultCount = computed(() => this.filteredRenewals().length);

  setProductFilter(product: string): void {
    this.filter.update(f => ({ ...f, product: f.product === product ? '' : product }));
  }

  clearFilters(): void {
    this.filter.set({ product: 'car', policyNumber: '', client: '', regNumber: '' });
  }
}
