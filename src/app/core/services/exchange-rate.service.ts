import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of, shareReplay, throwError } from 'rxjs';

export type PresentationCurrency = 'PLN' | 'USD' | 'EUR';
type ForeignCurrency = Exclude<PresentationCurrency, 'PLN'>;

interface NbpExchangeRateResponse {
  table: 'A' | 'B' | 'C';
  currency: string;
  code: string;
  rates: Array<{
    no: string;
    effectiveDate: string;
    mid: number;
  }>;
}

export interface ExchangeRateResult {
  currency: PresentationCurrency;
  rate: number;
  effectiveDate: string | null;
  sourceTable: 'A' | 'B' | 'C' | null;
}

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  private readonly http = inject(HttpClient);
  private readonly cache = new Map<string, Observable<ExchangeRateResult>>();

  getRate(currency: PresentationCurrency): Observable<ExchangeRateResult> {
    if (currency === 'PLN') {
      return of({
        currency,
        rate: 1,
        effectiveDate: null,
        sourceTable: null,
      });
    }

    const cacheKey = `${currency}:current-or-yesterday`;
    const cached = this.cache.get(cacheKey);
    if (cached) {
      return cached;
    }

    const yesterday = this.getYesterdayIsoDate();

    const request$ = this.fetchCurrentRate(currency).pipe(
      catchError(() => this.fetchRateForDate(currency, yesterday)),
      catchError(error => {
        this.cache.delete(cacheKey);
        return throwError(() => error);
      }),
      shareReplay(1)
    );

    this.cache.set(cacheKey, request$);

    return request$;
  }

  private fetchCurrentRate(currency: ForeignCurrency): Observable<ExchangeRateResult> {
    return this.http
      .get<NbpExchangeRateResponse>(
        `https://api.nbp.pl/api/exchangerates/rates/A/${currency}/?format=json`
      )
      .pipe(map(response => this.mapResponse(currency, response)));
  }

  private fetchRateForDate(currency: ForeignCurrency, date: string): Observable<ExchangeRateResult> {
    return this.http
      .get<NbpExchangeRateResponse>(
        `https://api.nbp.pl/api/exchangerates/rates/A/${currency}/${date}/?format=json`
      )
      .pipe(map(response => this.mapResponse(currency, response)));
  }

  private mapResponse(
    currency: ForeignCurrency,
    response: NbpExchangeRateResponse
  ): ExchangeRateResult {
    const firstRate = response.rates[0];

    if (!firstRate || typeof firstRate.mid !== 'number' || !firstRate.effectiveDate) {
      throw new Error(`Invalid NBP response for ${currency}`);
    }

    return {
      currency,
      rate: firstRate.mid,
      effectiveDate: firstRate.effectiveDate,
      sourceTable: response.table,
    };
  }

  private getYesterdayIsoDate(): string {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    return yesterday.toISOString().slice(0, 10);
  }
}
