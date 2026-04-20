export type CurrencyCode = 'PLN' | 'EUR' | 'USD';

export interface Money {
  amount: number;
  currency: CurrencyCode;
}

export interface CurrencyExchangeRate {
  code: CurrencyCode;
  rateToPln: number;
}

export interface CurrencySelection {
  currency: CurrencyCode;
  exchangeRate?: CurrencyExchangeRate;
  available: boolean;
}
