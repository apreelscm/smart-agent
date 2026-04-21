export type ForeignDisplayCurrency = 'EUR' | 'USD';
export type DisplayCurrency = 'PLN' | ForeignDisplayCurrency;

export const DISPLAY_CURRENCIES: DisplayCurrency[] = ['PLN', 'EUR', 'USD'];
export const FOREIGN_DISPLAY_CURRENCIES: ForeignDisplayCurrency[] = ['EUR', 'USD'];
