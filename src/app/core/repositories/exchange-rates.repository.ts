import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map, throwError } from 'rxjs';

export type ExchangeRateCode = 'EUR' | 'USD';

export interface ExchangeRateResult {
  table: string;
  currency: string;
  code: ExchangeRateCode;
  effectiveDate: string;
  mid: number;
}

type NbpExchangeRateResponse = {
  table: string;
  currency: string;
  code: string;
  rates?: Array<{
    effectiveDate?: string;
    mid?: number;
  }>;
};

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly supportedCodes: readonly ExchangeRateCode[] = ['EUR', 'USD'];
  private readonly nbpApiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getExchangeRate(code: ExchangeRateCode): Observable<ExchangeRateResult> {
    const normalizedCode = code.toUpperCase() as ExchangeRateCode;

    if (!this.supportedCodes.includes(normalizedCode)) {
      return throwError(() => new Error(`Unsupported exchange-rate currency code: ${code}`));
    }

    return this.httpClient
      .get<NbpExchangeRateResponse>(`${this.nbpApiBaseUrl}/${normalizedCode}/?format=json`)
      .pipe(
        map((response) => {
          const firstRate = response.rates?.[0];
          const mid = firstRate?.mid;
          const effectiveDate = firstRate?.effectiveDate;

          if (typeof mid !== 'number' || !Number.isFinite(mid) || !effectiveDate) {
            throw new Error(`NBP response for ${normalizedCode} does not contain a usable mid rate.`);
          }

          return {
            table: response.table,
            currency: response.currency,
            code: response.code.toUpperCase() as ExchangeRateCode,
            effectiveDate,
            mid
          };
        })
      );
  }
}
