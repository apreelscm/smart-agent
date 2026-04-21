import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map, Observable } from 'rxjs';
import { EXCHANGE_RATES_CONFIG } from '../config/exchange-rates.token';
import { ExchangeRateSnapshot } from '../models';

type NbpRateDto = {
  code: string;
  mid: number;
};

type NbpTableDto = {
  table: string;
  no?: string;
  effectiveDate: string;
  rates: NbpRateDto[];
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly config = inject(EXCHANGE_RATES_CONFIG);

  getExchangeRates(): Observable<ExchangeRateSnapshot> {
    return this.httpClient.get<NbpTableDto[]>(this.buildUrl()).pipe(
      map((response) => {
        const table = response[0];

        if (!table) {
          throw new Error('NBP_EMPTY_RESPONSE');
        }

        const eur = table.rates.find((rate) => rate.code === 'EUR')?.mid;
        const usd = table.rates.find((rate) => rate.code === 'USD')?.mid;

        if (eur === undefined || usd === undefined) {
          throw new Error('NBP_REQUIRED_RATES_MISSING');
        }

        return {
          source: 'NBP',
          table: 'A',
          tableNumber: table.no,
          effectiveDate: table.effectiveDate,
          rates: {
            EUR: eur,
            USD: usd
          }
        } satisfies ExchangeRateSnapshot;
      })
    );
  }

  private buildUrl(): string {
    const normalizedBaseUrl = this.config.baseUrl.replace(/\/+$/, '');
    const normalizedEndpoint = this.config.tableEndpoint.startsWith('/') ? this.config.tableEndpoint : `/${this.config.tableEndpoint}`;
    return `${normalizedBaseUrl}${normalizedEndpoint}`;
  }
}
