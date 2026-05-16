export type RemoteDisplayCurrency = 'EUR' | 'USD';
export type DisplayCurrency = 'PLN' | RemoteDisplayCurrency;

export interface ExchangeRate {
  tableType: 'A';
  currencyCode: RemoteDisplayCurrency;
  currencyName: string;
  effectiveDate: string;
  midRate: number;
  sourceTableNo: string;
  source: 'NBP';
}

export interface NbpExchangeRateApiRate {
  no: string;
  effectiveDate: string;
  mid?: number;
}

export interface NbpExchangeRateApiResponse {
  table: 'A';
  currency: string;
  code: RemoteDisplayCurrency;
  rates: NbpExchangeRateApiRate[];
}
