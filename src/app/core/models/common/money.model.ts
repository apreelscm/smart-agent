import { SupportedCurrency } from './supported-currency.model';

export interface Money {
  amount: number;
  currency: SupportedCurrency;
}
