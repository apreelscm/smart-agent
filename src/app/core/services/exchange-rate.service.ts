import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';
import {
  ExchangeRateInfo,
  ForeignCurrency,
  NbpSingleRateResponse,
} from '../models/exchange-rate.model';

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  private http = inject(HttpClient);

  private readonly apiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getRate(currency: ForeignCurrency): Observable<ExchangeRateInfo> {
    const code = currency.toUpperCase() as ForeignCurrency;

    return this.http.get<NbpSingleRateResponse>(this.buildCurrentRateUrl(code)).pipe(
      map(response => this.normalizeRate(code, response)),
      catchError(() =>
        this.http.get<NbpSingleRateResponse>(this.buildFallbackRateUrl(code)).pipe(
          map(response => this.normalizeRate(code, response))
        )
      )
    );
  }

  private normalizeRate(currency: ForeignCurrency, response: NbpSingleRateResponse): ExchangeRateInfo {
    const [rate] = response.rates;

    if (!rate) {
      throw new Error(`NBP response for ${currency} does not contain rate data.`);
    }

    return {
      currency,
      code: response.code,
      table: response.table,
      no: rate.no,
      effectiveDate: rate.effectiveDate,
      mid: rate.mid,
    };
  }

  private buildCurrentRateUrl(currency: ForeignCurrency): string {
    return `${this.apiBaseUrl}/${currency}/?format=json`;
  }

  private buildFallbackRateUrl(currency: ForeignCurrency): string {
    return `${this.apiBaseUrl}/${currency}/${this.getPreviousCalendarDate()}/?format=json`;
  }

  private getPreviousCalendarDate(referenceDate = new Date()): string {
    const previousDate = new Date(referenceDate);
    previousDate.setDate(previousDate.getDate() - 1);

    return this.formatDate(previousDate);
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
