export type SupportedCurrencyCode = 'PLN' | 'USD' | 'EUR';
export type ForeignCurrencyCode = Exclude<SupportedCurrencyCode, 'PLN'>;

export interface NbpRateEntry {
  no: string;
  effectiveDate: string;
  mid: number;
}

export interface NbpSingleRateResponse {
  table: string;
  currency: string;
  code: ForeignCurrencyCode;
  rates: NbpRateEntry[];
}

export interface ExchangeRateQuote {
  code: ForeignCurrencyCode;
  mid: number;
  tableNo: string;
  effectiveDate: string;
}
