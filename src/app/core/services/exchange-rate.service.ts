import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';
import { ExchangeRateQuote, ForeignCurrencyCode, NbpSingleRateResponse } from '../models/exchange-rate.model';

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  private http = inject(HttpClient);

  private readonly baseUrl = 'https://api.nbp.pl/api';
  private readonly tableType = 'A';

  getQuote(code: ForeignCurrencyCode): Observable<ExchangeRateQuote> {
    return this.fetchQuote(code).pipe(
      catchError(() => this.fetchQuote(code, this.getPreviousDayDate()))
    );
  }

  private fetchQuote(code: ForeignCurrencyCode, date?: string): Observable<ExchangeRateQuote> {
    return this.http
      .get<NbpSingleRateResponse>(this.buildUrl(code, date))
      .pipe(map(response => this.mapQuote(response)));
  }

  private buildUrl(code: ForeignCurrencyCode, date?: string): string {
    const dateSegment = date ? `/${date}` : '';
    return `${this.baseUrl}/exchangerates/rates/${this.tableType}/${code}${dateSegment}/?format=json`;
  }

  private mapQuote(response: NbpSingleRateResponse): ExchangeRateQuote {
    const [rate] = response.rates;

    if (!rate) {
      throw new Error('NBP response does not contain rate data.');
    }

    return {
      code: response.code,
      mid: rate.mid,
      tableNo: rate.no,
      effectiveDate: rate.effectiveDate,
    };
  }

  private getPreviousDayDate(referenceDate: Date = new Date()): string {
    const utcDate = new Date(
      Date.UTC(referenceDate.getFullYear(), referenceDate.getMonth(), referenceDate.getDate())
    );

    utcDate.setUTCDate(utcDate.getUTCDate() - 1);

    return utcDate.toISOString().slice(0, 10);
  }
}
