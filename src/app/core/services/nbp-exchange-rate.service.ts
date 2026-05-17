import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';

import {
  CurrencyRateSnapshot,
  ForeignCurrency,
  NbpSingleCurrencyRateResponse,
} from '../models/currency.model';

@Injectable({ providedIn: 'root' })
export class NbpExchangeRateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getRate(code: ForeignCurrency): Observable<CurrencyRateSnapshot> {
    const fallbackDate = this.getPreviousCalendarDay(new Date());

    return this.fetchLatestRate(code).pipe(
      catchError(() => this.fetchRateForDate(code, fallbackDate)),
    );
  }

  private fetchLatestRate(code: ForeignCurrency): Observable<CurrencyRateSnapshot> {
    return this.http
      .get<NbpSingleCurrencyRateResponse>(`${this.baseUrl}/${code}/?format=json`)
      .pipe(map(response => this.normalizeResponse(code, response)));
  }

  private fetchRateForDate(code: ForeignCurrency, date: string): Observable<CurrencyRateSnapshot> {
    return this.http
      .get<NbpSingleCurrencyRateResponse>(`${this.baseUrl}/${code}/${date}/?format=json`)
      .pipe(map(response => this.normalizeResponse(code, response)));
  }

  private normalizeResponse(
    requestedCode: ForeignCurrency,
    response: NbpSingleCurrencyRateResponse | null | undefined,
  ): CurrencyRateSnapshot {
    const firstRate = response?.rates?.[0];

    if (
      response?.table !== 'A' ||
      response.code !== requestedCode ||
      typeof response.currency !== 'string' ||
      typeof firstRate?.effectiveDate !== 'string' ||
      typeof firstRate?.mid !== 'number' ||
      !Number.isFinite(firstRate.mid) ||
      firstRate.mid <= 0
    ) {
      throw new Error('Malformed NBP response');
    }

    return {
      code: requestedCode,
      rate: firstRate.mid,
      effectiveDate: firstRate.effectiveDate,
    };
  }

  private getPreviousCalendarDay(referenceDate: Date): string {
    const previousDay = new Date(referenceDate);
    previousDay.setDate(previousDay.getDate() - 1);

    const year = previousDay.getFullYear();
    const month = String(previousDay.getMonth() + 1).padStart(2, '0');
    const day = String(previousDay.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
