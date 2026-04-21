import { Injectable, computed, effect, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, map, of, startWith } from 'rxjs';
import { CurrencyCode, ExchangeRatesSnapshot } from '../models';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';

type CurrencyExchangeState = {
  snapshot: ExchangeRatesSnapshot | null;
  loading: boolean;
  error: boolean;
};

@Injectable()
export class CurrencyPresentationService {
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly selectedCurrencyState = signal<CurrencyCode>('PLN');

  private readonly exchangeState = toSignal(
    this.exchangeRatesRepository.getExchangeRates().pipe(
      map(
        (snapshot): CurrencyExchangeState => ({
          snapshot,
          loading: false,
          error: false
        })
      ),
      startWith({
        snapshot: null,
        loading: true,
        error: false
      } satisfies CurrencyExchangeState),
      catchError(() =>
        of({
          snapshot: null,
          loading: false,
          error: true
        } satisfies CurrencyExchangeState)
      )
    ),
    {
      initialValue: {
        snapshot: null,
        loading: true,
        error: false
      } satisfies CurrencyExchangeState
    }
  );

  readonly selectedCurrency = this.selectedCurrencyState.asReadonly();
  readonly snapshot = computed(() => this.exchangeState().snapshot);
  readonly loading = computed(() => this.exchangeState().loading);
  readonly error = computed(() => this.exchangeState().error);
  readonly foreignCurrenciesAvailable = computed(() => !this.loading() && !this.error() && !!this.snapshot());
  readonly availableCurrencies = computed<CurrencyCode[]>(() =>
    this.foreignCurrenciesAvailable() ? ['PLN', 'EUR', 'USD'] : ['PLN']
  );
  readonly degradationMessage = computed(() =>
    this.loading() || this.foreignCurrenciesAvailable()
      ? null
      : 'Kursy walut NBP są chwilowo niedostępne. Aplikacja działa wyłącznie w PLN.'
  );
  readonly activeRateInfo = computed(() => {
    const currency = this.selectedCurrency();
    const snapshot = this.snapshot();

    if (currency === 'PLN' || !snapshot) {
      return null;
    }

    const rate = snapshot.rates[currency];

    if (typeof rate !== 'number') {
      return null;
    }

    return {
      currency,
      rate,
      effectiveDate: snapshot.effectiveDate
    };
  });

  constructor() {
    effect(() => {
      if (!this.canUseCurrency(this.selectedCurrency())) {
        this.selectedCurrencyState.set('PLN');
      }
    });
  }

  selectCurrency(currency: CurrencyCode): void {
    this.selectedCurrencyState.set(this.resolveCurrency(currency));
  }

  canUseCurrency(currency: CurrencyCode): boolean {
    return currency === 'PLN' || this.foreignCurrenciesAvailable();
  }

  resolveCurrency(currency: CurrencyCode): CurrencyCode {
    return currency === 'PLN' ? 'PLN' : this.foreignCurrenciesAvailable() ? currency : 'PLN';
  }

  rateFor(currency: CurrencyCode): number | null {
    if (currency === 'PLN') {
      return 1;
    }

    return this.snapshot()?.rates[currency] ?? null;
  }

  convertFromPln(amount: number | null | undefined, currency: CurrencyCode = this.selectedCurrency()): number {
    const normalizedAmount = Number(amount ?? 0);
    const resolvedCurrency = this.resolveCurrency(currency);

    if (resolvedCurrency === 'PLN') {
      return normalizedAmount;
    }

    const rate = this.rateFor(resolvedCurrency);
    return rate ? normalizedAmount / rate : normalizedAmount;
  }

  convertToPln(amount: number | null | undefined, currency: CurrencyCode = this.selectedCurrency()): number {
    const normalizedAmount = Number(amount ?? 0);
    const resolvedCurrency = this.resolveCurrency(currency);

    if (resolvedCurrency === 'PLN') {
      return normalizedAmount;
    }

    const rate = this.rateFor(resolvedCurrency);
    return rate ? normalizedAmount * rate : normalizedAmount;
  }

  formatAmount(amount: number | null | undefined, currency: CurrencyCode = this.selectedCurrency()): string {
    const resolvedCurrency = this.resolveCurrency(currency);
    const displayAmount = resolvedCurrency === 'PLN' ? Number(amount ?? 0) : this.convertFromPln(amount, resolvedCurrency);
    const digits = resolvedCurrency === 'PLN' ? 0 : 2;

    return new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: resolvedCurrency,
      minimumFractionDigits: digits,
      maximumFractionDigits: digits
    }).format(displayAmount);
  }

  formatRate(rate: number): string {
    return new Intl.NumberFormat('pl-PL', {
      minimumFractionDigits: 4,
      maximumFractionDigits: 4
    }).format(rate);
  }
}
