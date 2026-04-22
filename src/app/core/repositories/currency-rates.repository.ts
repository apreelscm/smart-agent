import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';

export type DisplayCurrency = 'PLN' | 'EUR' | 'USD';
export type SupportedQuoteCurrency = Exclude<DisplayCurrency, 'PLN'>;
export type CurrencyRateSource = 'service' | 'fallback';

export interface CurrencyRateQuote {
  currency: SupportedQuoteCurrency;
  rate: number;
  source: CurrencyRateSource;
  effectiveDate?: string;
}

interface NbpExchangeRateResponse {
  code?: string;
  rates?: Array<{
    effectiveDate?: string;
    mid?: number;
  }>;
}

const FALLBACK_RATES: Readonly<Record<SupportedQuoteCurrency, number>> = {
  EUR: 4.4,
  USD: 3.6
};

@Injectable({
  providedIn: 'root'
})
export class CurrencyRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/a';

  getQuote(currency: SupportedQuoteCurrency): Observable<CurrencyRateQuote> {
    const url = `${this.apiBaseUrl}/${currency}/?format=json`;

    return this.httpClient.get<NbpExchangeRateResponse>(url).pipe(
      map((response) => this.mapResponseToQuote(response, currency)),
      catchError(() => of(this.createFallbackQuote(currency)))
    );
  }

  private mapResponseToQuote(response: NbpExchangeRateResponse | null | undefined, currency: SupportedQuoteCurrency): CurrencyRateQuote {
    const firstRate = response?.rates?.[0];
    const effectiveDate = typeof firstRate?.effectiveDate === 'string' ? firstRate.effectiveDate : undefined;
    const mid = firstRate?.mid;

    if (typeof mid === 'number' && Number.isFinite(mid) && mid > 0) {
      return {
        currency,
        rate: mid,
        source: 'service',
        effectiveDate
      };
    }

    return this.createFallbackQuote(currency, effectiveDate);
  }

  private createFallbackQuote(currency: SupportedQuoteCurrency, effectiveDate?: string): CurrencyRateQuote {
    return {
      currency,
      rate: FALLBACK_RATES[currency],
      source: 'fallback',
      effectiveDate
    };
  }
}
