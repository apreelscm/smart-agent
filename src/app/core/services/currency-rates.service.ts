import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map } from 'rxjs';

export type SupportedCurrencyCode = 'PLN' | 'USD' | 'EUR';
export type ForeignCurrencyCode = Exclude<SupportedCurrencyCode, 'PLN'>;

export interface NbpRateEntry {
  currency: string;
  code: string;
  mid: number;
}

export interface NbpExchangeRatesTable {
  table: string;
  no: string;
  effectiveDate: string;
  rates: NbpRateEntry[];
}

export interface CurrencyRatesData {
  table: string;
  tableNumber: string;
  effectiveDate: string;
  rates: Record<ForeignCurrencyCode, number>;
}

@Injectable({ providedIn: 'root' })
export class CurrencyRatesService {
  private http = inject(HttpClient);

  private readonly baseUrl = 'https://api.nbp.pl/api/exchangerates/tables/A';

  getRates(): Observable<CurrencyRatesData> {
    const previousDay = this.formatDate(this.getPreviousDay());

    return this.fetchTable(`${this.baseUrl}/today/?format=json`).pipe(
      catchError(() => this.fetchTable(`${this.baseUrl}/${previousDay}/?format=json`))
    );
  }

  private fetchTable(url: string): Observable<CurrencyRatesData> {
    return this.http.get<NbpExchangeRatesTable[]>(url).pipe(
      map(response => this.normalizeResponse(response))
    );
  }

  private normalizeResponse(response: NbpExchangeRatesTable[]): CurrencyRatesData {
    if (!Array.isArray(response) || response.length === 0) {
      throw new Error('NBP table response is empty.');
    }

    const table = response[0];

    if (
      !table ||
      !table.table ||
      !table.no ||
      !table.effectiveDate ||
      !Array.isArray(table.rates)
    ) {
      throw new Error('NBP table response is invalid.');
    }

    const usdRate = table.rates.find(rate => rate.code === 'USD');
    const eurRate = table.rates.find(rate => rate.code === 'EUR');

    if (!this.isValidRate(usdRate) || !this.isValidRate(eurRate)) {
      throw new Error('NBP table response does not contain required USD and EUR rates.');
    }

    return {
      table: table.table,
      tableNumber: table.no,
      effectiveDate: table.effectiveDate,
      rates: {
        USD: usdRate.mid,
        EUR: eurRate.mid,
      },
    };
  }

  private isValidRate(rate: NbpRateEntry | undefined): rate is NbpRateEntry {
    return !!rate && Number.isFinite(rate.mid) && rate.mid > 0;
  }

  private getPreviousDay(): Date {
    const date = new Date();
    date.setDate(date.getDate() - 1);
    return date;
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = `${date.getMonth() + 1}`.padStart(2, '0');
    const day = `${date.getDate()}`.padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
