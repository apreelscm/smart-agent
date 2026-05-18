export type SupportedCurrency = 'PLN' | 'USD' | 'EUR';
export type ForeignCurrency = Exclude<SupportedCurrency, 'PLN'>;
export type NbpTableType = 'A' | 'B' | 'C';

export interface NbpSingleRateEntry {
  no: string;
  effectiveDate: string;
  mid?: number;
}

export interface NbpSingleRateApiResponse {
  table: string;
  currency?: string;
  code: string;
  rates: NbpSingleRateEntry[];
}

export interface ExchangeRateQuote {
  table: NbpTableType;
  currencyCode: ForeignCurrency;
  tableNo: string;
  effectiveDate: string;
  mid: number;
}
