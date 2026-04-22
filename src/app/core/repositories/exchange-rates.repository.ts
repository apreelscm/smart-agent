import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, shareReplay, throwError } from 'rxjs';
import { ExchangeRateQuote, ForeignCurrencyCode, NbpExchangeRateDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ExchangeRatesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly requestsCache = new Map<ForeignCurrencyCode, Observable<ExchangeRateQuote>>();
  private readonly ratesApiBaseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getExchangeRate(code: ForeignCurrencyCode): Observable<ExchangeRateQuote> {
    const cachedRequest = this.requestsCache.get(code);

    if (cachedRequest) {
      return cachedRequest;
    }

    const request$ = this.httpClient.get<NbpExchangeRateDto>(`${this.ratesApiBaseUrl}/${code}/?format=json`).pipe(
      map((response) => this.mapQuote(response)),
      shareReplay({ bufferSize: 1, refCount: false }),
      catchError((error) => {
        this.requestsCache.delete(code);
        return throwError(() => error);
      })
    );

    this.requestsCache.set(code, request$);
    return request$;
  }

  private mapQuote(response: NbpExchangeRateDto): ExchangeRateQuote {
    const currentRate = response.rates[0];

    if (!currentRate || currentRate.mid == null) {
      throw new Error(`NBP response for ${response.code} does not contain rates[0].mid.`);
    }

    return {
      code: response.code,
      mid: currentRate.mid,
      effectiveDate: currentRate.effectiveDate
    };
  }
}
