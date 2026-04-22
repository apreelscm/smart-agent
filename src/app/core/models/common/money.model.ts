import { CurrencyCode } from './currency-code.model';

export interface Money {
  amount: number;
  currency: CurrencyCode;
}
