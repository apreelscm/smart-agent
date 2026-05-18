import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import {
  ExchangeRateQuote,
  ForeignCurrency,
  NbpSingleRateApiResponse,
  NbpTableType,
} from '../models/exchange-rate.model';

const NBP_API_BASE_URL = 'https://api.nbp.pl/api';
const NBP_TABLE: NbpTableType = 'A';

export class NbpExchangeRateError extends Error {
  constructor(
    readonly currency: ForeignCurrency,
    readonly reason: 'lookup-failed' | 'invalid-payload',
    message: string,
    cause?: unknown,
  ) {
    super(message, { cause });
    this.name = 'NbpExchangeRateError';
  }
}

@Injectable({ providedIn: 'root' })
export class NbpExchangeRateService {
  private readonly http = inject(HttpClient);

  getLatestOrPreviousRate(currency: ForeignCurrency): Observable<ExchangeRateQuote> {
    return this.fetchLatestRate(currency).pipe(
      catchError(() => this.fetchPreviousDayRate(currency)),
      catchError(error => throwError(() => this.normalizeError(currency, error))),
    );
  }

  private fetchLatestRate(currency: ForeignCurrency): Observable<ExchangeRateQuote> {
    return this.fetchRate(
      `${NBP_API_BASE_URL}/exchangerates/rates/${NBP_TABLE}/${currency}/?format=json`,
      currency,
    );
  }

  private fetchPreviousDayRate(currency: ForeignCurrency): Observable<ExchangeRateQuote> {
    return this.fetchRate(
      `${NBP_API_BASE_URL}/exchangerates/rates/${NBP_TABLE}/${currency}/${this.getPreviousDayDate()}/?format=json`,
      currency,
    );
  }

  private fetchRate(url: string, currency: ForeignCurrency): Observable<ExchangeRateQuote> {
    return this.http.get<NbpSingleRateApiResponse>(url).pipe(
      map(response => this.mapQuote(currency, response)),
    );
  }

  private mapQuote(currency: ForeignCurrency, response: NbpSingleRateApiResponse): ExchangeRateQuote {
    const rate = Array.isArray(response.rates) ? response.rates[0] : null;

    if (
      !this.isTableType(response.table) ||
      response.code !== currency ||
      !rate ||
      typeof rate.no !== 'string' ||
      typeof rate.effectiveDate !== 'string' ||
      typeof rate.mid !== 'number' ||
      !Number.isFinite(rate.mid)
    ) {
      throw new NbpExchangeRateError(
        currency,
        'invalid-payload',
        `NBP response for ${currency} is missing required rate metadata.`,
      );
    }

    return {
      table: response.table,
      currencyCode: currency,
      tableNo: rate.no,
      effectiveDate: rate.effectiveDate,
      mid: rate.mid,
    };
  }

  private normalizeError(currency: ForeignCurrency, error: unknown): NbpExchangeRateError {
    if (error instanceof NbpExchangeRateError) {
      return error;
    }

    return new NbpExchangeRateError(
      currency,
      'lookup-failed',
      `NBP exchange rate lookup failed for ${currency}.`,
      error,
    );
  }

  private isTableType(value: unknown): value is NbpTableType {
    return value === 'A' || value === 'B' || value === 'C';
  }

  private getPreviousDayDate(referenceDate: Date = new Date()): string {
    const previousDay = new Date(referenceDate);
    previousDay.setDate(previousDay.getDate() - 1);

    const year = previousDay.getFullYear();
    const month = String(previousDay.getMonth() + 1).padStart(2, '0');
    const day = String(previousDay.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
