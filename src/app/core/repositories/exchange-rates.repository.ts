import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map } from 'rxjs';
import { ExchangeRateTable } from '../models/common/exchange-rate.model';

type NbpTableResponse = Array<{
  table: string;
  no: string;
  effectiveDate: string;
  rates: Array<{
    currency: string;
    code: string;
    mid: number;
  }>;
}>;

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly http = inject(HttpClient);
  private readonly endpoint = 'https://api.nbp.pl/api/exchangerates/tables/A/?format=json';

  getCurrentRates() {
    return this.http.get<NbpTableResponse>(this.endpoint).pipe(map((response) => this.mapResponse(response)));
  }

  private mapResponse(response: NbpTableResponse): ExchangeRateTable {
    const table = response[0];

    if (!table) {
      throw new Error('NBP exchange-rate table response is empty.');
    }

    const eurRate = table.rates.find((rate) => rate.code === 'EUR')?.mid;
    const usdRate = table.rates.find((rate) => rate.code === 'USD')?.mid;

    if (typeof eurRate !== 'number' || typeof usdRate !== 'number') {
      throw new Error('NBP response does not contain expected EUR/USD average rates.');
    }

    return {
      tableType: 'A',
      sourceTableNo: table.no,
      publicationDate: table.effectiveDate,
      rates: {
        EUR: eurRate,
        USD: usdRate
      }
    };
  }
}
