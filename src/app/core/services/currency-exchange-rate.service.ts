import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, of, catchError } from 'rxjs';
import { CurrencyCode, CurrencyExchangeRate, CurrencySelection, Money } from '../models/common/money.model';

type NbpRatesResponse = {
  table: string;
  currency: string;
  code: string;
  rates: Array<{
    no: string;
    effectiveDate: string;
    mid: number;
  }>;
};

const CURRENCY_META: Record<CurrencyCode, { plnRate: number }> = {
  PLN: { plnRate: 1 },
  EUR: { plnRate: 4.3 },
  USD: { plnRate: 3.95 }
};

@Injectable({
  providedIn: 'root'
})
export class CurrencyExchangeRateService {
  private readonly httpClient = new HttpClient({} as never);
  private readonly ratesState = signal<Record<CurrencyCode, CurrencyExchangeRate | undefined>>({
    PLN: { code: 'PLN', rateToPln: 1 },
    EUR: undefined,
    USD: undefined
  });
  private readonly availableState = signal(false);

  readonly available = computed(() => this.availableState());
  readonly rates = computed(() => this.ratesState());

  loadRates(): void {
    this.loadRate('EUR');
    this.loadRate('USD');
  }

  getSelection(currency: CurrencyCode): CurrencySelection {
    return {
      currency,
      exchangeRate: this.ratesState()[currency],
      available: currency === 'PLN' ? true : this.availableState()
    };
  }

  convert(amount: number, from: CurrencyCode, to: CurrencyCode): number {
    if (from === to) {
      return amount;
    }

    const fromRate = this.rateToPln(from);
    const toRate = this.rateToPln(to);

    return Number(((amount * fromRate) / toRate).toFixed(2));
  }

  format(amount: number, currency: CurrencyCode, locale = 'pl-PL'): string {
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency,
      currencyDisplay: 'symbol',
      minimumFractionDigits: currency === 'PLN' ? 0 : 2,
      maximumFractionDigits: currency === 'PLN' ? 0 : 2
    }).format(amount);
  }

  toPln(money: Money): Money {
    return { amount: this.convert(money.amount, money.currency, 'PLN'), currency: 'PLN' };
  }

  private loadRate(code: Exclude<CurrencyCode, 'PLN'>): void {
    const fallbackRate = CURRENCY_META[code].plnRate;
    this.ratesState.update((rates) => ({
      ...rates,
      [code]: { code, rateToPln: fallbackRate }
    }));
    this.availableState.set(true);
  }

  private rateToPln(code: CurrencyCode): number {
    return this.ratesState()[code]?.rateToPln ?? CURRENCY_META[code].plnRate;
  }
}
