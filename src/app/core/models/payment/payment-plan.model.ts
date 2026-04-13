import { Money } from '../common/money.model';

export interface PaymentInstallment {
  sequence: number;
  dueDate?: string;
  amount: Money;
}

export interface PaymentPlan {
  frequency: 'ANNUAL' | 'SEMI_ANNUAL' | 'QUARTERLY' | 'MONTHLY';
  totalPremium: Money;
  installments: PaymentInstallment[];
}
