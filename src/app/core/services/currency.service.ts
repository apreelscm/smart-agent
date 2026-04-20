import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { RateSet, RatesCache } from '../models/currency.model';

const CACHE_KEY = 'currency_rates_cache_v1';
const TTL_MS = 24 * 60 * 60 * 1000; // 24 hours

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private readonly http = inject(HttpClient);
  // Development default points to public/mock
  private readonly ratesUrl = 'mock/currency-rates.json';

  private readonly availability$ = new BehaviorSubject<boolean>(true);
  private cachedRateSet: RateSet | null = null;

  constructor() {
    this.loadCache();
    // If cache stale, trigger background refresh
    if (this.cachedRateSet) {
      const cache = this.readCacheFromStorage();
      if (cache) {
        const age = Date.now() - new Date(cache.fetchedAt).getTime();
        if (age > TTL_MS) {
          // attempt refresh but don't block init
          void this.fetchRates().subscribe({
            next: () => {},
            error: () => {}
          });
        }
      }
    } else {
      // no cache, attempt initial fetch
      void this.fetchRates().subscribe({
        next: () => {},
        error: () => {
          // if fetch fails and no cache -> mark unavailable
          this.availability$.next(false);
        }
      });
    }
  }

  /**
   * Force fetch the rates from backend (or mock). Returns observable of RateSet.
   */
  fetchRates(): Observable<RateSet> {
    return this.http.get<RateSet>(this.ratesUrl).pipe(
      tap((set) => {
        this.cachedRateSet = set;
        this.writeCacheToStorage({ fetchedAt: new Date().toISOString(), rateSet: set });
        this.availability$.next(true);
      }),
      catchError((err) => {
        // If fetch fails, fallback to cache if available
        const cache = this.readCacheFromStorage();
        if (cache) {
          this.cachedRateSet = cache.rateSet;
          // still available (stale but usable)
          this.availability$.next(true);
          return of(cache.rateSet);
        }
        this.availability$.next(false);
        return throwError(() => err);
      })
    );
  }

  /**
   * Returns currently loaded rates as an observable. If no rates loaded,
   * attempts to fetch them.
   */
  getRates(): Observable<RateSet | null> {
    if (this.cachedRateSet) {
      return of(this.cachedRateSet);
    }
    return this.fetchRates().pipe(
      map((r) => r),
      catchError(() => of(null))
    );
  }

  /**
   * Convert numeric amount between PLN, EUR, USD using loaded rates.
   * If rates for requested conversion missing, throws.
   */
  convert(amount: number, from: 'PLN' | 'EUR' | 'USD', to: 'PLN' | 'EUR' | 'USD'): number {
    if (from === to) {
      return this.roundForCurrency(amount, to);
    }

    const rates = this.cachedRateSet?.rates ?? {};
    // Helper: convert any -> PLN then PLN -> target
    const toPln = (value: number, currency: 'PLN' | 'EUR' | 'USD'): number => {
      if (currency === 'PLN') return value;
      const rate = rates[currency];
      if (rate == null) {
        throw new Error(`Rate for ${currency} unavailable`);
      }
      return value * rate;
    };

    const fromPln = (valuePln: number, currency: 'PLN' | 'EUR' | 'USD'): number => {
      if (currency === 'PLN') return valuePln;
      const rate = rates[currency];
      if (rate == null) {
        throw new Error(`Rate for ${currency} unavailable`);
      }
      return valuePln / rate;
    };

    // Convert input to PLN
    const plnValue = toPln(amount, from);
    // Convert PLN to target
    const converted = fromPln(plnValue, to);
    return this.roundForCurrency(converted, to);
  }

  /**
   * Helper to round according to currency rules:
   * - PLN: 0 decimals (integer)
   * - EUR, USD: 2 decimals
   */
  roundForCurrency(value: number, currency: 'PLN' | 'EUR' | 'USD'): number {
    switch (currency) {
      case 'PLN':
        return Math.round(value);
      case 'EUR':
      case 'USD':
        return Math.round(value * 100) / 100;
      default:
        return value;
    }
  }

  isAvailable(): Observable<boolean> {
    return this.availability$.asObservable();
  }

  /**
   * Returns numeric rate for 1 <currency> in PLN, or null when unknown.
   * Example: rateFor('EUR') -> 4.65 (1 EUR = 4.65 PLN)
   */
  rateFor(currency: 'EUR' | 'USD'): number | null {
    return this.cachedRateSet?.rates[currency] ?? null;
  }

  ratesDate(): string | null {
    return this.cachedRateSet?.date ?? null;
  }

  /**
   * Returns a small info object: { available, date, rates }
   */
  rateInfo(): { available: boolean; date?: string | null; rates?: RateSet['rates'] } {
    return {
      available: this.availability$.value,
      date: this.cachedRateSet?.date ?? null,
      rates: this.cachedRateSet?.rates
    };
  }

  private writeCacheToStorage(cache: RatesCache) {
    try {
      localStorage.setItem(CACHE_KEY, JSON.stringify(cache));
    } catch {
      // ignore
    }
  }

  private readCacheFromStorage(): RatesCache | null {
    try {
      const raw = localStorage.getItem(CACHE_KEY);
      if (!raw) return null;
      return JSON.parse(raw) as RatesCache;
    } catch {
      return null;
    }
  }

  private loadCache() {
    const cache = this.readCacheFromStorage();
    if (!cache) {
      this.cachedRateSet = null;
      return;
    }
    this.cachedRateSet = cache.rateSet;
    // If cache too old we still keep it loaded but signal staleness via availability (true).
    const age = Date.now() - new Date(cache.fetchedAt).getTime();
    if (age > TTL_MS) {
      // stale but usable; availability true
      this.availability$.next(true);
    } else {
      this.availability$.next(true);
    }
  }
}
