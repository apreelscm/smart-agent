import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, throwError } from 'rxjs';
import { ExchangeRateCurrency, ExchangeRateQuote } from '../models';

type NbpExchangeRateResponse = {
  table?: unknown;
  currency?: unknown;
  code?: unknown;
  rates?: Array<{
    no?: unknown;
    effectiveDate?: unknown;
    mid?: unknown;
  }>;
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly exchangeRatesUrl = 'https://api.nbp.pl/api/exchangerates/rates/a';

  getCurrentRate(code: ExchangeRateCurrency): Observable<ExchangeRateQuote> {
    if (!this.isSupportedCurrency(code)) {
      return throwError(() => new Error(`Unsupported exchange-rate currency: ${code}`));
    }

    return this.httpClient
      .get<NbpExchangeRateResponse>(`${this.exchangeRatesUrl}/${code}/?format=json`)
      .pipe(map((response) => this.mapExchangeRateQuote(response, code)));
  }

  private mapExchangeRateQuote(response: NbpExchangeRateResponse, expectedCode: ExchangeRateCurrency): ExchangeRateQuote {
    const rate = Array.isArray(response.rates) ? response.rates[0] : undefined;

    if (
      typeof response.table !== 'string' ||
      typeof response.currency !== 'string' ||
      response.code !== expectedCode ||
      !rate ||
      typeof rate.effectiveDate !== 'string' ||
      typeof rate.mid !== 'number'
    ) {
      throw new Error('Invalid NBP exchange-rate response payload');
    }

    return {
      table: response.table,
      currency: response.currency,
      code: expectedCode,
      effectiveDate: rate.effectiveDate,
      mid: rate.mid
    };
  }

  private isSupportedCurrency(code: string): code is ExchangeRateCurrency {
    return code === 'EUR' || code === 'USD';
  }
}
