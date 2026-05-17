export type DisplayCurrency = 'PLN' | 'USD' | 'EUR';
export type ForeignCurrency = Exclude<DisplayCurrency, 'PLN'>;
export type NbpTableType = 'A';

export interface NbpSingleCurrencyRateEntry {
  no?: string;
  effectiveDate?: string;
  mid?: number;
}

export interface NbpSingleCurrencyRateResponse {
  table?: NbpTableType | string;
  currency?: string;
  code?: string;
  rates?: NbpSingleCurrencyRateEntry[];
}

export interface CurrencyRateSnapshot {
  code: ForeignCurrency;
  rate: number;
  effectiveDate: string;
}
