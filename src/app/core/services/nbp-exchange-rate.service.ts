import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';

import {
  ExchangeRate,
  NbpExchangeRateApiResponse,
  RemoteDisplayCurrency,
} from '../models/exchange-rate.model';

@Injectable({ providedIn: 'root' })
export class NbpExchangeRateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getTodayRate(code: RemoteDisplayCurrency): Observable<ExchangeRate> {
    return this.http
      .get<NbpExchangeRateApiResponse>(`${this.baseUrl}/${code}/today/?format=json`)
      .pipe(
        map(response => {
          const currentRate = response.rates[0];

          if (!currentRate || typeof currentRate.mid !== 'number' || !currentRate.effectiveDate || !currentRate.no) {
            throw new Error(`Brak poprawnego kursu NBP dla ${code}.`);
          }

          return {
            tableType: 'A',
            currencyCode: response.code,
            currencyName: response.currency,
            effectiveDate: currentRate.effectiveDate,
            midRate: currentRate.mid,
            sourceTableNo: currentRate.no,
            source: 'NBP',
          };
        }),
        catchError(error => {
          if (error instanceof Error) {
            return throwError(() => error);
          }

          if (error.status === 404) {
            return throwError(() => new Error(`Brak dzisiejszego kursu NBP dla ${code}.`));
          }

          return throwError(() => new Error(`Nie udało się pobrać kursu NBP dla ${code}.`));
        }),
      );
  }
}
