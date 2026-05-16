import { Injectable, computed, signal } from '@angular/core';
import { PolicyPhoto, PolicyPhotoFilter } from '../models/policy-photo.model';

const MOCK_PHOTOS: PolicyPhoto[] = [
  { id:'1',  productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/998',       status:'do-blisko',     branchNumber:'WSY/991',  insuredName:'KOWALSKI JAN',         expiryDate:'2025-06-30' },
  { id:'2',  productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581162',     status:'do-blisko',     branchNumber:'OZORKÓW',  insuredName:'CZORDAN ŁUKASZ',        expiryDate:'2025-07-14' },
  { id:'3',  productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/2232',      status:'do-blisko',     branchNumber:'WSY/2291', insuredName:'MACZGAŁA OLIWIA',       expiryDate:'2025-05-28' },
  { id:'4',  productType:'home',  productColor:'#1565c0', policyNumber:'KM-A60/01/283',       status:'do-blisko',     branchNumber:'ŁUBNA',    insuredName:'WIŚNIEWSKA ANNA',       expiryDate:'2025-08-01' },
  { id:'5',  productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/340128',    status:'do-blisko',     branchNumber:'POLAŃ',    insuredName:'MROWIEC PIOTR',         expiryDate:'2025-07-22' },
  { id:'6',  productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/2521',      status:'do-blisko',     branchNumber:'WSY/234',  insuredName:'MAZUR AGNIESZKA',       expiryDate:'2025-06-15' },
  { id:'7',  productType:'health',productColor:'#43a047', policyNumber:'KM-A60/01/789',       status:'w-werifikacja', branchNumber:'GDAŃSK',   insuredName:'NOWAK TOMASZ',          expiryDate:'2025-09-30' },
  { id:'8',  productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581200',     status:'do-blisko',     branchNumber:'POZNAŃ',   insuredName:'LEWANDOWSKI KRZYSZTOF', expiryDate:'2025-07-05' },
  { id:'9',  productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/991',       status:'w-werifikacja', branchNumber:'WARSZAWA', insuredName:'DĄBROWSKI MAREK',       expiryDate:'2025-08-18' },
  { id:'10', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581300',     status:'do-blisko',     branchNumber:'KRAKÓW',   insuredName:'WÓJCIK BARBARA',        expiryDate:'2025-06-25' },
  { id:'11', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/1105',      status:'do-blisko',     branchNumber:'ŁÓDŹ',     insuredName:'KAMIŃSKA EWA',          expiryDate:'2025-07-10' },
  { id:'12', productType:'health',productColor:'#43a047', policyNumber:'KM-A60/01/1250',      status:'w-werifikacja', branchNumber:'WROCŁAW',  insuredName:'SZYMAŃSKI RAFAŁ',       expiryDate:'2025-10-01' },
  { id:'13', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/1340',      status:'do-blisko',     branchNumber:'GDYNIA',   insuredName:'WOŹNIAK MONIKA',        expiryDate:'2025-05-31' },
  { id:'14', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581450',     status:'do-blisko',     branchNumber:'SZCZECIN', insuredName:'KOZŁOWSKI ADAM',        expiryDate:'2025-08-22' },
  { id:'15', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/1510',      status:'w-werifikacja', branchNumber:'KATOWICE', insuredName:'JANKOWSKA ZOFIA',       expiryDate:'2025-09-15' },
  { id:'16', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581600',     status:'do-blisko',     branchNumber:'BIAŁYSTOK',insuredName:'WIŚNIEWSKI PAWEŁ',      expiryDate:'2025-07-28' },
  { id:'17', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/1720',      status:'do-blisko',     branchNumber:'LUBLIN',   insuredName:'NOWAK MARTA',           expiryDate:'2025-06-08' },
  { id:'18', productType:'health',productColor:'#43a047', policyNumber:'KM-A60/01/1850',      status:'w-werifikacja', branchNumber:'RZESZÓW',  insuredName:'KOWALCZYK PIOTR',       expiryDate:'2025-11-30' },
  { id:'19', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/1990',      status:'do-blisko',     branchNumber:'OLSZTYN',  insuredName:'ZIELIŃSKA KATARZYNA',   expiryDate:'2025-06-20' },
  { id:'20', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581700',     status:'do-blisko',     branchNumber:'OPOLE',    insuredName:'BĄK STANISŁAW',         expiryDate:'2025-08-10' },
  { id:'21', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/2050',      status:'do-blisko',     branchNumber:'PŁOCK',    insuredName:'MICHALSKA ALEKSANDRA',  expiryDate:'2025-07-03' },
  { id:'22', productType:'health',productColor:'#43a047', policyNumber:'KM-A60/01/2100',      status:'w-werifikacja', branchNumber:'KIELCE',   insuredName:'GRABOWSKI ROBERT',      expiryDate:'2025-12-15' },
  { id:'23', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581800',     status:'do-blisko',     branchNumber:'GORZÓW',   insuredName:'PAWLAK IRENA',          expiryDate:'2025-09-01' },
  { id:'24', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/2200',      status:'do-blisko',     branchNumber:'BYDGOSZCZ',insuredName:'KRAWCZYK MICHAŁ',       expiryDate:'2025-05-25' },
  { id:'25', productType:'home',  productColor:'#1565c0', policyNumber:'KM-AJ0012581900',     status:'w-werifikacja', branchNumber:'TORUŃ',    insuredName:'KUBIAK DOROTA',         expiryDate:'2025-10-20' },
  { id:'26', productType:'car',   productColor:'#e53935', policyNumber:'KM-A60/01/2310',      status:'do-blisko',     branchNumber:'KOSZALIN', insuredName:'MAZUREK ANDRZEJ',       expiryDate:'2025-06-12' },
  { id:'27', productType:'health',productColor:'#43a047', policyNumber:'KM-A60/01/2400',      status:'w-werifikacja', branchNumber:'RADOM',    insuredName:'WYSOCKA NATALIA',       expiryDate:'2025-11-05' },
];

@Injectable({ providedIn: 'root' })
export class PolicyPhotoService {
  readonly filter = signal<PolicyPhotoFilter>({ days30: false, product: '', status: '' });

  readonly filteredPhotos = computed(() => {
    const f = this.filter();
    return MOCK_PHOTOS.filter(p => {
      if (f.status && p.status !== f.status) return false;
      if (f.product && p.productType !== f.product) return false;
      return true;
    });
  });

  readonly resultCount = computed(() => this.filteredPhotos().length);

  setStatusFilter(status: string): void {
    this.filter.update(f => ({ ...f, status: f.status === status ? '' : status }));
  }

  setProductFilter(product: string): void {
    this.filter.update(f => ({ ...f, product: f.product === product ? '' : product }));
  }

  clearFilters(): void {
    this.filter.set({ days30: false, product: '', status: '' });
  }
}
