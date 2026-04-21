import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

type SupportedExchangeRateCode = 'EUR' | 'USD';

type NbpExchangeRate = {
  code: string;
  mid: number;
};

type NbpExchangeRatesTable = {
  effectiveDate: string;
  rates: NbpExchangeRate[];
};

export interface ExchangeRates {
  effectiveDate: string;
  EUR?: number;
  USD?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly exchangeRatesUrl = 'https://api.nbp.pl/api/exchangerates/tables/A?format=json';

  getExchangeRates(): Observable<ExchangeRates> {
    return this.httpClient.get<NbpExchangeRatesTable[]>(this.exchangeRatesUrl).pipe(
      map((tables) => {
        const currentTable = tables[0];

        if (!currentTable) {
          throw new Error('NBP response does not contain exchange rates table data.');
        }

        const rates = currentTable.rates.reduce<Partial<Record<SupportedExchangeRateCode, number>>>((accumulator, rate) => {
          if ((rate.code === 'EUR' || rate.code === 'USD') && typeof rate.mid === 'number') {
            accumulator[rate.code] = rate.mid;
          }

          return accumulator;
        }, {});

        return {
          effectiveDate: currentTable.effectiveDate,
          EUR: rates.EUR,
          USD: rates.USD
        };
      })
    );
  }
}
