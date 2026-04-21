import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, shareReplay } from 'rxjs';
import { ExchangeRatesSnapshot, ForeignCurrencyCode } from '../models';

type NbpRateResponse = {
  currency: string;
  code: string;
  mid: number;
};

type NbpTableResponse = {
  table: string;
  no: string;
  effectiveDate: string;
  rates: NbpRateResponse[];
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly endpointUrl = 'https://api.nbp.pl/api/exchangerates/tables/A/?format=json';

  private readonly exchangeRatesSnapshot$ = this.httpClient.get<NbpTableResponse[]>(this.endpointUrl).pipe(
    map((response) => this.mapSnapshot(response)),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  getExchangeRates(): Observable<ExchangeRatesSnapshot> {
    return this.exchangeRatesSnapshot$;
  }

  private mapSnapshot(response: NbpTableResponse[]): ExchangeRatesSnapshot {
    const table = response[0];

    if (!table?.effectiveDate || !Array.isArray(table.rates)) {
      throw new Error('NBP table A response is invalid.');
    }

    const eurRate = this.pickRate(table.rates, 'EUR');
    const usdRate = this.pickRate(table.rates, 'USD');

    if (eurRate === null || usdRate === null) {
      throw new Error('NBP response does not contain EUR and USD rates.');
    }

    return {
      effectiveDate: table.effectiveDate,
      rates: {
        EUR: eurRate,
        USD: usdRate
      }
    };
  }

  private pickRate(rates: NbpRateResponse[], code: ForeignCurrencyCode): number | null {
    const rate = rates.find((item) => item.code === code)?.mid;
    return typeof rate === 'number' ? rate : null;
  }
}
