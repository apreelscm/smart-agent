export type CurrencyCode = 'PLN' | 'EUR' | 'USD';

export type ForeignCurrencyCode = Exclude<CurrencyCode, 'PLN'>;
