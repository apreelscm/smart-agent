export type MoneyCurrency = 'PLN' | 'EUR' | 'USD';

export interface Money {
  amount: number;
  currency: MoneyCurrency;
}
