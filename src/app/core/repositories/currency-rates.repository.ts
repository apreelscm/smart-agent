import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, defer, map, shareReplay } from 'rxjs';

export const SUPPORTED_PRESENTATION_CURRENCIES = ['EUR', 'USD'] as const;

export type SupportedPresentationCurrency = (typeof SUPPORTED_PRESENTATION_CURRENCIES)[number];

export interface CurrencyRatesTable {
  tableType: string;
  sourceTableNo: string;
  publicationDate: string;
  rates: Partial<Record<SupportedPresentationCurrency, number>>;
}

interface NbpExchangeRateDto {
  currency: string;
  code: string;
  mid: number;
}

interface NbpExchangeRatesTableDto {
  table: string;
  no: string;
  effectiveDate: string;
  rates: NbpExchangeRateDto[];
}

@Injectable({
  providedIn: 'root'
})
export class CurrencyRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly exchangeRatesTableUrl = 'https://api.nbp.pl/api/exchangerates/tables/A/?format=json';

  private readonly exchangeRates$ = defer(() => this.httpClient.get<NbpExchangeRatesTableDto[]>(this.exchangeRatesTableUrl)).pipe(
    map((response) => this.mapExchangeRates(response)),
    shareReplay({ bufferSize: 1, refCount: false })
  );

  getRates(): Observable<CurrencyRatesTable> {
    return this.exchangeRates$;
  }

  private mapExchangeRates(response: NbpExchangeRatesTableDto[]): CurrencyRatesTable {
    const table = response[0];

    if (!table) {
      throw new Error('NBP response did not contain exchange rates table data.');
    }

    return {
      tableType: table.table,
      sourceTableNo: table.no,
      publicationDate: table.effectiveDate,
      rates: this.mapSupportedRates(table.rates)
    };
  }

  private mapSupportedRates(rates: NbpExchangeRateDto[]): Partial<Record<SupportedPresentationCurrency, number>> {
    return SUPPORTED_PRESENTATION_CURRENCIES.reduce(
      (result, currencyCode) => {
        const rate = rates.find((item) => item.code === currencyCode)?.mid;

        if (typeof rate === 'number') {
          result[currencyCode] = rate;
        }

        return result;
      },
      {} as Partial<Record<SupportedPresentationCurrency, number>>
    );
  }
}
