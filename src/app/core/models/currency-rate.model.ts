export type PresentationCurrency = 'PLN' | 'USD' | 'EUR';
export type ForeignPresentationCurrency = Exclude<PresentationCurrency, 'PLN'>;

export interface NbpTableRate {
  currency: string;
  code: string;
  mid: number;
}

export interface NbpTable {
  table: 'A';
  no: string;
  effectiveDate: string;
  rates: NbpTableRate[];
}

export type NbpTableResponse = NbpTable[];

export interface CurrencyRate {
  table: 'A';
  tableNo: string;
  effectiveDate: string;
  currencyCode: ForeignPresentationCurrency;
  currencyName: string;
  mid: number;
}
