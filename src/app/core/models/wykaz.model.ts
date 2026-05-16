export type WykazPaymentStatus = 'oplacony' | 'nieoplacony' | 'oplacony-czesciowo';
export type WykazProcessStatus = 'zatwierdzony' | 'oczekujacy' | 'odrzucony';
export type WykazItemType = 'polisa' | 'kwitariusz' | 'aneks' | 'inny';
export type BeSystem = 'SITU' | 'VERSIS';

export interface WykazNote {
  id: string;
  author: string;
  date: string;
  content: string;
}

export interface WykazItem {
  id: string;
  type: WykazItemType;
  number: string;
  insuredName: string;
  amount: number;
  inkasAmount: number;
  date: string;
  canDelete: boolean;
}

export interface Wykaz {
  id: string;
  number: string;
  agentNumber: string;
  agentName: string;
  issueDate: string;
  modifiedDate: string;
  modifiedBy: string;
  totalAmount: number;
  inkasAmount: number;
  paymentStatus: WykazPaymentStatus;
  statusCR: WykazProcessStatus;
  statusOddzial: WykazProcessStatus;
  messages: string[];
  notes: WykazNote[];
  items: WykazItem[];
  source: BeSystem;
  blocked: boolean;
}

export interface WykazFilter {
  agentNumber: string;
  wykazNumber: string;
  paymentStatus: string;
  policyNumber: string;
  dateFrom: string;
  dateTo: string;
  amountFrom: string;
  amountTo: string;
  statusCR: string;
  statusOddzial: string;
  last30Days: boolean;
}
