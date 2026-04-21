import { ForeignCurrencyCode } from './currency-code.model';

export interface ExchangeRatesSnapshot {
  effectiveDate: string;
  rates: Record<ForeignCurrencyCode, number>;
}
