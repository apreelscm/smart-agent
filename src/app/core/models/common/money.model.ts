export type CurrencyCode = 'PLN' | 'EUR' | 'USD';

export type SupportedForeignCurrencyCode = Exclude<CurrencyCode, 'PLN'>;

export interface Money {
  amount: number;
  currency: CurrencyCode;
}

export interface ExchangeRateEntry {
  currencyCode: SupportedForeignCurrencyCode;
  rate: number;
}

export interface ExchangeRates {
  EUR?: number;
  USD?: number;
}

export interface ExchangeRateSnapshot {
  table: 'A';
  sourceTableNo: string;
  publishedAt: string;
  rates: ExchangeRates;
}

export interface ExchangeRateAvailability {
  availableCurrencies: CurrencyCode[];
  unavailableCurrencies: SupportedForeignCurrencyCode[];
  hasForeignRates: boolean;
  fallbackMessage: string | null;
}
