export type ExchangeRateCurrencyCode = 'EUR' | 'USD';

export interface NbpExchangeRateQuote {
  no?: string;
  effectiveDate: string;
  mid: number;
}

export interface NbpExchangeRateResponse {
  table?: string;
  currency: string;
  code: string;
  rates: NbpExchangeRateQuote[];
}

export interface AppliedExchangeRate {
  code: ExchangeRateCurrencyCode;
  currency: string;
  effectiveDate: string;
  midRate: number;
  sourceTableNo?: string;
}
