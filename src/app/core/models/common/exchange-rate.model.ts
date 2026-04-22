import { ForeignCurrencyCode } from './currency-code.model';

export type NbpExchangeRateTableType = 'A' | 'B' | 'C';

export interface NbpExchangeRateRateDto {
  no: string;
  effectiveDate: string;
  tradingDate?: string;
  mid?: number;
  bid?: number;
  ask?: number;
}

export interface NbpExchangeRateDto {
  table: NbpExchangeRateTableType;
  currency: string;
  code: ForeignCurrencyCode;
  rates: NbpExchangeRateRateDto[];
}

export interface ExchangeRateQuote {
  code: ForeignCurrencyCode;
  mid: number;
  effectiveDate: string;
}
