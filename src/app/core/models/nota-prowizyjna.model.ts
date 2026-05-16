export type NotaType   = 'podstawowa' | 'dodatkowa';
export type NotaStatus = 'wyplacona' | 'oczekujaca' | 'zablokowana';

export interface CommissionCategory {
  name: 'dobrowolne' | 'obowiazkowe';
  damages: number;
  inkasAmount: number;
  lossRatio: number;
  lossRatioThreshold: number;
  commissionRate: number;
  commissionBase: number;
  commissionAmount: number;
}

export interface NotaItem {
  id: string;
  policyNumber: string;
  insuredName: string;
  issueDate: string;
  baseAmount: number;
  inkasAmount: number;
  risk: string;
  riskCode: string;
  commissionRate: number;
  commissionAmount: number;
  ofwcName: string;
  ofwcNumber: string;
  agentNumber: string;
  agencyCode: string;
}

export interface NotaProwizyjna {
  id: string;
  number: string;
  issueDate: string;
  type: NotaType;
  agencyOwner: string;
  agentNumber: string;
  agentName: string;
  branchCodes: string[];
  totalCommission: number;
  status: NotaStatus;
  isDebtor: boolean;
  showCommission: boolean;
  categories: CommissionCategory[];
  items: NotaItem[];
  rateSettings: string[];
}

export interface NotaFilter {
  agencyOwner: string;
  dateFrom: string;
  dateTo: string;
  type: string;
  amountFrom: string;
  amountTo: string;
  notaNumber: string;
  policyNumber: string;
  showCommission: boolean;
  last30Days: boolean;
}
