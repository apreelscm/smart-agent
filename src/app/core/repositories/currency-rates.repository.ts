import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { AppliedExchangeRate, ExchangeRateCurrencyCode, NbpExchangeRateResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CurrencyRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly nbpApiUrl = 'https://api.nbp.pl/api/exchangerates/rates/a';

  getCurrentRate(currencyCode: ExchangeRateCurrencyCode): Observable<AppliedExchangeRate> {
    const normalizedCurrencyCode = currencyCode.toUpperCase() as ExchangeRateCurrencyCode;

    return this.httpClient
      .get<NbpExchangeRateResponse>(`${this.nbpApiUrl}/${normalizedCurrencyCode}/?format=json`)
      .pipe(map((response) => this.mapResponse(response, normalizedCurrencyCode)));
  }

  private mapResponse(response: NbpExchangeRateResponse, requestedCurrencyCode: ExchangeRateCurrencyCode): AppliedExchangeRate {
    const responseCode = typeof response.code === 'string' ? response.code.toUpperCase() : '';
    const firstRate = response.rates?.[0];

    if (
      responseCode !== requestedCurrencyCode ||
      typeof response.currency !== 'string' ||
      response.currency.trim().length === 0 ||
      !firstRate ||
      typeof firstRate.mid !== 'number' ||
      !Number.isFinite(firstRate.mid) ||
      firstRate.mid <= 0 ||
      typeof firstRate.effectiveDate !== 'string' ||
      firstRate.effectiveDate.trim().length === 0
    ) {
      throw new Error('Invalid NBP exchange-rate response.');
    }

    return {
      code: responseCode as ExchangeRateCurrencyCode,
      currency: response.currency,
      effectiveDate: firstRate.effectiveDate,
      midRate: firstRate.mid,
      sourceTableNo: typeof firstRate.no === 'string' ? firstRate.no : undefined
    };
  }
}
