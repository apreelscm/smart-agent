export type PresentationCurrency = 'PLN' | 'EUR' | 'USD';

export type ExchangeRateCurrency = Exclude<PresentationCurrency, 'PLN'>;
