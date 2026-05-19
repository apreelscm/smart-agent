export type SupportedCurrency = 'PLN' | 'USD' | 'EUR';
export type ForeignCurrency = Exclude<SupportedCurrency, 'PLN'>;

export interface NbpRateEntry {
  no: string;
  effectiveDate: string;
  mid: number;
}

export interface NbpSingleRateResponse {
  table: string;
  code: string;
  rates: NbpRateEntry[];
}

export interface ExchangeRateInfo {
  currency: ForeignCurrency;
  code: string;
  table: string;
  no: string;
  effectiveDate: string;
  mid: number;
}
