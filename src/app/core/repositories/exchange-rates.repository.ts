import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';

export type SupportedForeignCurrency = 'EUR' | 'USD';

export type ExchangeRatesState =
  | {
      status: 'loading';
      available: false;
      effectiveDate: null;
      rates: {};
    }
  | {
      status: 'available';
      available: true;
      effectiveDate: string;
      rates: Partial<Record<SupportedForeignCurrency, number>>;
    }
  | {
      status: 'unavailable';
      available: false;
      effectiveDate: null;
      rates: {};
    };

type NbpRateDto = {
  code?: unknown;
  mid?: unknown;
};

type NbpTableDto = {
  effectiveDate?: unknown;
  rates?: unknown;
};

export const EXCHANGE_RATES_API_URL = 'https://api.nbp.pl/api/exchangerates/tables/A/?format=json';

export const EXCHANGE_RATES_INITIAL_STATE: ExchangeRatesState = {
  status: 'loading',
  available: false,
  effectiveDate: null,
  rates: {}
};

export const EXCHANGE_RATES_UNAVAILABLE_STATE: ExchangeRatesState = {
  status: 'unavailable',
  available: false,
  effectiveDate: null,
  rates: {}
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);

  getExchangeRates(): Observable<ExchangeRatesState> {
    return this.httpClient.get<unknown>(EXCHANGE_RATES_API_URL).pipe(
      map((response) => this.normalizeResponse(response)),
      catchError(() => of(EXCHANGE_RATES_UNAVAILABLE_STATE))
    );
  }

  private normalizeResponse(response: unknown): ExchangeRatesState {
    const [table] = Array.isArray(response) ? response : [];

    if (!table || typeof table !== 'object') {
      throw new Error('NBP response does not contain table data.');
    }

    const { effectiveDate, rates } = table as NbpTableDto;

    if (typeof effectiveDate !== 'string' || !Array.isArray(rates)) {
      throw new Error('NBP response has invalid table shape.');
    }

    const normalizedRates: Partial<Record<SupportedForeignCurrency, number>> = {};
    const supportedCurrencies: SupportedForeignCurrency[] = ['EUR', 'USD'];

    supportedCurrencies.forEach((currencyCode) => {
      const rate = this.extractRate(rates, currencyCode);

      if (rate !== undefined) {
        normalizedRates[currencyCode] = rate;
      }
    });

    if (Object.keys(normalizedRates).length === 0) {
      throw new Error('NBP response does not contain supported foreign currencies.');
    }

    return {
      status: 'available',
      available: true,
      effectiveDate,
      rates: normalizedRates
    };
  }

  private extractRate(rates: unknown[], currencyCode: SupportedForeignCurrency): number | undefined {
    const matchingRate = rates.find((rate) => this.isValidRate(rate, currencyCode));

    return matchingRate ? matchingRate.mid : undefined;
  }

  private isValidRate(rate: unknown, currencyCode: SupportedForeignCurrency): rate is { code: SupportedForeignCurrency; mid: number } {
    if (!rate || typeof rate !== 'object') {
      return false;
    }

    const candidate = rate as NbpRateDto;

    return candidate.code === currencyCode && typeof candidate.mid === 'number' && Number.isFinite(candidate.mid) && candidate.mid > 0;
  }
}
