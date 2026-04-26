import { ExchangeRateCurrency } from './presentation-currency.model';

export interface ExchangeRateQuote {
  table: string;
  currency: string;
  code: ExchangeRateCurrency;
  effectiveDate: string;
  mid: number;
}
