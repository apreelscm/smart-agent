export type CurrencyCode = 'PLN' | 'EUR' | 'USD';

export type ForeignCurrencyCode = Exclude<CurrencyCode, 'PLN'>;

export const SUPPORTED_CURRENCIES: CurrencyCode[] = ['PLN', 'EUR', 'USD'];
export const FOREIGN_CURRENCIES: ForeignCurrencyCode[] = ['EUR', 'USD'];
