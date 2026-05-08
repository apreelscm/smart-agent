import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';

export type NbpSupportedCurrency = 'EUR' | 'USD';

export interface NbpExchangeRateResponse {
  table: 'A';
  currency: string;
  code: NbpSupportedCurrency;
  rates: Array<{
    no: string;
    effectiveDate: string;
    mid: number;
  }>;
}

export interface NbpExchangeRateQuote {
  table: 'A';
  currencyCode: NbpSupportedCurrency;
  currencyName: string;
  effectiveDate: string;
  midRate: number;
  sourceTableNo: string;
}

@Injectable({
  providedIn: 'root'
})
export class NbpExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getCurrentRate(currencyCode: NbpSupportedCurrency): Observable<NbpExchangeRateQuote> {
    return this.httpClient
      .get<NbpExchangeRateResponse>(`${this.apiBaseUrl}/${currencyCode}/?format=json`)
      .pipe(map((response) => this.mapRateResponse(currencyCode, response)));
  }

  private mapRateResponse(expectedCurrency: NbpSupportedCurrency, response: NbpExchangeRateResponse): NbpExchangeRateQuote {
    const latestRate = response.rates?.[0];

    if (
      response.table !== 'A' ||
      response.code !== expectedCurrency ||
      !latestRate ||
      typeof latestRate.mid !== 'number' ||
      !Number.isFinite(latestRate.mid) ||
      latestRate.mid <= 0 ||
      !latestRate.no ||
      !latestRate.effectiveDate
    ) {
      throw new Error('Invalid NBP exchange rate response.');
    }

    return {
      table: response.table,
      currencyCode: response.code,
      currencyName: response.currency,
      effectiveDate: latestRate.effectiveDate,
      midRate: latestRate.mid,
      sourceTableNo: latestRate.no
    };
  }
}
