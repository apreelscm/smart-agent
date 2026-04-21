import { Injectable, computed, inject, signal } from '@angular/core';
import { catchError, map, Observable, of, shareReplay, tap } from 'rxjs';
import { DisplayCurrency, ExchangeRateSnapshot, ExchangeRateState, ForeignDisplayCurrency } from '../models';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';

const SESSION_STORAGE_KEY = 'smart-agent-exchange-rates';
const FALLBACK_MESSAGE = 'Kursy EUR i USD są chwilowo niedostępne. Widok działa wyłącznie w PLN.';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly stateSignal = signal<ExchangeRateState>(this.restoreState());
  private loadRequest$?: Observable<ExchangeRateState>;

  readonly state = this.stateSignal.asReadonly();
  readonly snapshot = computed(() => this.stateSignal().snapshot);
  readonly foreignCurrencyAvailable = computed(() => this.hasForeignCurrencyRates(this.stateSignal()));

  ensureRatesLoaded(): Observable<ExchangeRateState> {
    if (this.stateSignal().snapshot) {
      return of(this.stateSignal());
    }

    if (!this.loadRequest$) {
      this.stateSignal.update((state) => ({
        ...state,
        loading: true
      }));

      this.loadRequest$ = this.exchangeRatesRepository.getExchangeRates().pipe(
        map((snapshot) => ({
          loading: false,
          snapshot,
          error: null
        })),
        catchError(() =>
          of({
            loading: false,
            snapshot: null,
            error: FALLBACK_MESSAGE
          })
        ),
        tap((state) => {
          this.stateSignal.set(state);
          this.persistState(state);
        }),
        shareReplay({ bufferSize: 1, refCount: false })
      );
    }

    return this.loadRequest$;
  }

  convertFromPln(amountPln: number, currency: DisplayCurrency, snapshot: ExchangeRateSnapshot | null = this.snapshot()): number {
    if (currency === 'PLN') {
      return amountPln;
    }

    if (!snapshot) {
      return amountPln;
    }

    return amountPln / snapshot.rates[currency];
  }

  convertToPln(amount: number, currency: DisplayCurrency, snapshot: ExchangeRateSnapshot | null = this.snapshot()): number {
    if (currency === 'PLN') {
      return Math.round(amount);
    }

    if (!snapshot) {
      return Math.round(amount);
    }

    return Math.round(amount * snapshot.rates[currency]);
  }

  formatDigits(currency: DisplayCurrency): string {
    return currency === 'PLN' ? '1.0-0' : '1.2-2';
  }

  formatAmount(amountPln: number | null | undefined, currency: DisplayCurrency, snapshot: ExchangeRateSnapshot | null = this.snapshot()): string {
    const normalizedAmount = Number.isFinite(amountPln) ? Number(amountPln) : 0;
    const resolvedCurrency = this.resolveCurrency(currency, snapshot);
    const convertedAmount =
      resolvedCurrency === 'PLN' ? normalizedAmount : this.convertFromPln(normalizedAmount, resolvedCurrency, snapshot);

    return new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: resolvedCurrency,
      currencyDisplay: 'narrowSymbol',
      minimumFractionDigits: resolvedCurrency === 'PLN' ? 0 : 2,
      maximumFractionDigits: resolvedCurrency === 'PLN' ? 0 : 2
    }).format(convertedAmount);
  }

  formatExchangeRate(currency: ForeignDisplayCurrency, snapshot: ExchangeRateSnapshot | null = this.snapshot()): string {
    if (!snapshot) {
      return '';
    }

    return `1 ${currency} = ${new Intl.NumberFormat('pl-PL', {
      minimumFractionDigits: 4,
      maximumFractionDigits: 4
    }).format(snapshot.rates[currency])} PLN`;
  }

  hasForeignCurrencyRates(state: ExchangeRateState = this.stateSignal()): boolean {
    return !!state.snapshot?.rates.EUR && !!state.snapshot?.rates.USD;
  }

  fallbackMessage(): string {
    return FALLBACK_MESSAGE;
  }

  private resolveCurrency(currency: DisplayCurrency, snapshot: ExchangeRateSnapshot | null): DisplayCurrency {
    if (currency === 'PLN') {
      return 'PLN';
    }

    return snapshot ? currency : 'PLN';
  }

  private restoreState(): ExchangeRateState {
    if (typeof window === 'undefined') {
      return {
        loading: false,
        snapshot: null,
        error: null
      };
    }

    const raw = window.sessionStorage.getItem(SESSION_STORAGE_KEY);

    if (!raw) {
      return {
        loading: false,
        snapshot: null,
        error: null
      };
    }

    try {
      const snapshot = JSON.parse(raw) as ExchangeRateSnapshot;
      return {
        loading: false,
        snapshot,
        error: null
      };
    } catch {
      return {
        loading: false,
        snapshot: null,
        error: null
      };
    }
  }

  private persistState(state: ExchangeRateState): void {
    if (typeof window === 'undefined') {
      return;
    }

    if (!state.snapshot) {
      window.sessionStorage.removeItem(SESSION_STORAGE_KEY);
      return;
    }

    window.sessionStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(state.snapshot));
  }
}
