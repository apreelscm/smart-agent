export type PolisaStatus = 'aktywna' | 'wygasla' | 'anulowana' | 'zawieszona' | 'w-trakcie';
export type PolisaProductType = 'car' | 'home' | 'health' | 'other';

export interface PolisaRisk {
  name: string;
  sum: number;
  premium: number;
}

export interface Polisa {
  id: string;
  policyNumber: string;
  productType: PolisaProductType;
  productLabel: string;
  insuredName: string;
  insurantName: string;
  issueDate: string;
  startDate: string;
  endDate: string;
  premium: number;
  inkaso: number;
  status: PolisaStatus;
  agentName: string;
  agentCode: string;
  source: 'SITU' | 'VERSIS';
  risks: PolisaRisk[];
  vehicleReg?: string;
  address?: string;
}
