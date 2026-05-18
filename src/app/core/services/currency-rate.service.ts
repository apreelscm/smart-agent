import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  CurrencyRate,
  ForeignPresentationCurrency,
  NbpTableResponse,
} from '../models/currency-rate.model';

@Injectable({ providedIn: 'root' })
export class CurrencyRateService {
  private readonly http = inject(HttpClient);
  private readonly latestTableAUrl = 'https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json';

  getLatestRate(currencyCode: ForeignPresentationCurrency): Observable<CurrencyRate> {
    return this.http.get<NbpTableResponse>(this.latestTableAUrl).pipe(
      map((response) => {
        const [table] = response;

        if (!table) {
          throw new Error('NBP latest table A response is empty.');
        }

        const rate = table.rates.find((entry) => entry.code === currencyCode);

        if (!rate) {
          throw new Error(`NBP latest table A does not contain rate for ${currencyCode}.`);
        }

        return {
          table: table.table,
          tableNo: table.no,
          effectiveDate: table.effectiveDate,
          currencyCode,
          currencyName: rate.currency,
          mid: rate.mid,
        };
      }),
    );
  }
}
