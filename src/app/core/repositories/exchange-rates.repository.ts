import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, shareReplay } from 'rxjs';
import { ExchangeRateSnapshot, SupportedForeignCurrencyCode } from '../models';

type NbpTableRateDto = {
  code: string;
  mid?: number;
};

type NbpTableDto = {
  table: 'A';
  no: string;
  effectiveDate: string;
  rates: NbpTableRateDto[];
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly http = inject(HttpClient);
  private ratesRequest$?: Observable<ExchangeRateSnapshot>;

  getCurrentRates(): Observable<ExchangeRateSnapshot> {
    if (!this.ratesRequest$) {
      this.ratesRequest$ = this.http
        .get<NbpTableDto[]>('https://api.nbp.pl/api/exchangerates/tables/A/?format=json')
        .pipe(
          map((response) => this.mapTableResponse(response)),
          shareReplay(1)
        );
    }

    return this.ratesRequest$;
  }

  private mapTableResponse(response: NbpTableDto[]): ExchangeRateSnapshot {
    const table = response[0];

    if (!table) {
      throw new Error('NBP response does not contain exchange table data.');
    }

    return {
      table: 'A',
      sourceTableNo: table.no,
      publishedAt: table.effectiveDate,
      rates: {
        EUR: this.readMidRate(table.rates, 'EUR'),
        USD: this.readMidRate(table.rates, 'USD')
      }
    };
  }

  private readMidRate(rates: NbpTableRateDto[], code: SupportedForeignCurrencyCode): number | undefined {
    const rate = rates.find((entry) => entry.code === code)?.mid;
    return typeof rate === 'number' ? rate : undefined;
  }
}
