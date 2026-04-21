export type SupportedCurrency = 'PLN' | 'EUR' | 'USD';

export interface Money {
  amount: number;
  currency: SupportedCurrency;
}
