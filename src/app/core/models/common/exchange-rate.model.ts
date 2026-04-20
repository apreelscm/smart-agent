import { SupportedCurrency } from './supported-currency.model';

export interface ExchangeRateTable {
  tableType: 'A';
  sourceTableNo: string;
  publicationDate: string;
  rates: Record<Exclude<SupportedCurrency, 'PLN'>, number>;
}

export interface ExchangeRateAvailability {
  available: boolean;
  message: string | null;
}

export interface ConvertedMoneyView {
  originalAmount: number;
  originalCurrency: SupportedCurrency;
  convertedAmount: number;
  targetCurrency: SupportedCurrency;
  rate: number | null;
  publicationDate: string | null;
}

export interface RateMetadata {
  currency: Exclude<SupportedCurrency, 'PLN'>;
  rate: number;
  publicationDate: string;
  sourceTableNo: string;
}
