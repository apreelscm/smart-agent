import { ForeignDisplayCurrency } from '../common/display-currency.model';

export interface ExchangeRateSnapshot {
  source: 'NBP';
  table: 'A';
  tableNumber?: string;
  effectiveDate: string;
  rates: Record<ForeignDisplayCurrency, number>;
}

export interface ExchangeRateState {
  loading: boolean;
  snapshot: ExchangeRateSnapshot | null;
  error: string | null;
}
