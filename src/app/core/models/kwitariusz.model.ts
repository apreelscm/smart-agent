export type KwitariuszType = 'rata-odsetki' | 'rekalkulacja-odsetki';
export type KwitariuszStatus = 'wystawiony' | 'oplacony' | 'anulowany' | 'oczekujacy';
export type PolicyStatus = 'aktywna' | 'rozliczona' | 'rozwiazana' | 'oplacona' | 'ochrona-skonczona' | 'anulowana';
export type BeSystem = 'SITU' | 'VERSIS';

export interface Installment {
  number: number;
  dueDate: string;
  amount: number;
  paid: boolean;
  paidDate?: string;
}

export interface Kwitariusz {
  id: string;
  number: string;
  type: KwitariuszType;
  policyNumber: string;
  insuredName: string;
  issueDate: string;
  baseAmount: number;
  interest: number;
  status: KwitariuszStatus;
  source: BeSystem;

  // Dane płatnika
  payerName?: string;
  payerPeselNip?: string;
  payerClientType?: string;
  payerAddress?: string;
  payerEmail?: string;
  payerPhone?: string;

  // Dane finansowe
  finCurrency?: string;
  finPaymentDeadline?: string;
  finInterestDate?: string;
  finInterestRate?: number;
  finDaysOverdue?: number;

  // Rekalkulacja (tylko dla rekalkulacja-odsetki)
  rekalkReason?: string;
  rekalkDate?: string;
  rekalkAmountBefore?: number;
  rekalkAmountAfter?: number;

  // Plan rat (tylko dla rata-odsetki)
  installmentPlan?: Installment[];
}

export interface MockPolicy {
  series: string;
  number: string;
  insuredName: string;
  status: PolicyStatus;
  baseAmount: number;
  installmentPlan?: Installment[];
}
