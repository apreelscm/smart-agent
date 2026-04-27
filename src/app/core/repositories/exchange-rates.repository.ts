import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { MoneyCurrency } from '../models/common/money.model';

export type PresentationCurrency = MoneyCurrency;
export type ExchangeRateTargetCurrency = Exclude<PresentationCurrency, 'PLN'>;

export interface ExchangeRate {
  tableType: 'A';
  currencyCode: ExchangeRateTargetCurrency;
  currencyName?: string;
  publicationDate: string;
  midRate: number;
  sourceTableNo?: string;
}

type NbpExchangeRateResponse = {
  table?: string;
  currency?: string;
  code?: string;
  rates?: Array<{
    no?: string;
    effectiveDate?: string;
    mid?: number | null;
  }>;
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getCurrentRate(targetCurrency: ExchangeRateTargetCurrency): Observable<ExchangeRate> {
    return this.httpClient
      .get<NbpExchangeRateResponse>(`${this.apiBaseUrl}/${targetCurrency}/?format=json`)
      .pipe(map((response) => this.mapCurrentRateResponse(targetCurrency, response)));
  }

  private mapCurrentRateResponse(
    targetCurrency: ExchangeRateTargetCurrency,
    response: NbpExchangeRateResponse
  ): ExchangeRate {
    const currentRate = response.rates?.[0];
    const responseCurrencyCode = response.code?.toUpperCase();

    if (response.table !== 'A') {
      throw new Error('NBP response missing table A metadata.');
    }

    if (responseCurrencyCode !== targetCurrency) {
      throw new Error('NBP response currency code does not match requested currency.');
    }

    if (!currentRate?.effectiveDate || typeof currentRate.mid !== 'number') {
      throw new Error('NBP response missing current mid rate.');
    }

    return {
      tableType: 'A',
      currencyCode: targetCurrency,
      currencyName: response.currency,
      publicationDate: currentRate.effectiveDate,
      midRate: currentRate.mid,
      sourceTableNo: currentRate.no
    };
  }
}
