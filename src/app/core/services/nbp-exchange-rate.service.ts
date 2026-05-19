import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';

export type ForeignCurrencyCode = 'USD' | 'EUR';

export interface NbpExchangeRate {
  code: ForeignCurrencyCode;
  mid: number;
  tableNo: string;
  effectiveDate: string;
}

interface NbpRateResponse {
  table: string;
  currency: string;
  code: ForeignCurrencyCode;
  rates: Array<{
    no: string;
    effectiveDate: string;
    mid: number;
  }>;
}

@Injectable({ providedIn: 'root' })
export class NbpExchangeRateService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'https://api.nbp.pl/api/exchangerates/rates/A';

  getRate(code: ForeignCurrencyCode): Observable<NbpExchangeRate> {
    return this.http.get<NbpRateResponse>(this.buildCurrentRateUrl(code)).pipe(
      map(response => this.mapRateResponse(response)),
      catchError(() =>
        this.http.get<NbpRateResponse>(this.buildHistoricalRateUrl(code, this.getPreviousDay())).pipe(
          map(response => this.mapRateResponse(response)),
          catchError(() =>
            throwError(() => new Error(`NBP exchange rate is unavailable for ${code}`))
          )
        )
      )
    );
  }

  convertFromPln(amountPln: number, mid: number): number {
    return amountPln / mid;
  }

  private buildCurrentRateUrl(code: ForeignCurrencyCode): string {
    return `${this.baseUrl}/${code}/?format=json`;
  }

  private buildHistoricalRateUrl(code: ForeignCurrencyCode, date: string): string {
    return `${this.baseUrl}/${code}/${date}/?format=json`;
  }

  private mapRateResponse(response: NbpRateResponse): NbpExchangeRate {
    const latestRate = response.rates[0];

    if (!latestRate) {
      throw new Error(`NBP response does not contain rate data for ${response.code}`);
    }

    return {
      code: response.code,
      mid: latestRate.mid,
      tableNo: latestRate.no,
      effectiveDate: latestRate.effectiveDate,
    };
  }

  private getPreviousDay(): string {
    const date = new Date();
    date.setDate(date.getDate() - 1);

    const year = date.getFullYear();
    const month = `${date.getMonth() + 1}`.padStart(2, '0');
    const day = `${date.getDate()}`.padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
