import { Injectable } from '@angular/core';
import {
  CurrencyCode,
  ExchangeRateAvailability,
  ExchangeRateSnapshot,
  ExchangeRates,
  SupportedForeignCurrencyCode
} from '../models';

@Injectable({
  providedIn: 'root'
})
export class CurrencyConversionService {
  readonly currencyOptions: CurrencyCode[] = ['PLN', 'EUR', 'USD'];

  toDisplayAmount(amountInPln: number, currency: CurrencyCode, rates?: ExchangeRates): number {
    if (currency === 'PLN') {
      return amountInPln;
    }

    const rate = rates?.[currency];

    if (!rate) {
      return amountInPln;
    }

    return amountInPln / rate;
  }

  toPersistedPln(amount: number, currency: CurrencyCode, rates?: ExchangeRates): number {
    if (currency === 'PLN') {
      return this.roundPln(amount);
    }

    const rate = rates?.[currency];

    if (!rate) {
      return this.roundPln(amount);
    }

    return this.roundPln(amount * rate);
  }

  roundPln(amount: number): number {
    return Math.round(amount);
  }

  formatAmount(amountInPln: number, currency: CurrencyCode, rates?: ExchangeRates): string {
    const displayAmount = this.toDisplayAmount(amountInPln, currency, rates);

    if (currency === 'PLN') {
      return new Intl.NumberFormat('pl-PL', {
        style: 'currency',
        currency: 'PLN',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(displayAmount);
    }

    return new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(displayAmount);
  }

  formatEnteredAmount(amount: number, currency: CurrencyCode): string {
    if (currency === 'PLN') {
      return new Intl.NumberFormat('pl-PL', {
        style: 'currency',
        currency: 'PLN',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(this.roundPln(amount));
    }

    return new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency,
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(amount);
  }

  getAvailability(snapshot: ExchangeRateSnapshot | null, error: boolean): ExchangeRateAvailability {
    const availableForeignCurrencies = this.currencyOptions.filter(
      (currency): currency is SupportedForeignCurrencyCode => currency !== 'PLN' && !!snapshot?.rates[currency]
    );
    const unavailableCurrencies = this.currencyOptions.filter(
      (currency): currency is SupportedForeignCurrencyCode => currency !== 'PLN' && !availableForeignCurrencies.includes(currency)
    );

    return {
      availableCurrencies: ['PLN', ...availableForeignCurrencies],
      unavailableCurrencies,
      hasForeignRates: availableForeignCurrencies.length > 0,
      fallbackMessage:
        error || unavailableCurrencies.length > 0
          ? 'Kursy NBP są chwilowo niedostępne. Widok pozostaje w PLN, a waluty obce są wyłączone.'
          : null
    };
  }

  rateNote(currency: CurrencyCode, snapshot: ExchangeRateSnapshot | null): string | null {
    if (currency === 'PLN' || !snapshot?.rates[currency]) {
      return null;
    }

    return `Kurs NBP ${currency}: ${snapshot.rates[currency]?.toFixed(4)} z dnia ${snapshot.publishedAt}`;
  }

  helperPlnNote(amount: number | null, currency: CurrencyCode, snapshot: ExchangeRateSnapshot | null): string | null {
    if (amount === null || currency === 'PLN' || !snapshot?.rates[currency]) {
      return null;
    }

    const plnAmount = this.toPersistedPln(amount, currency, snapshot.rates);
    return `≈ ${this.formatEnteredAmount(plnAmount, 'PLN')} po kursie ${snapshot.rates[currency]?.toFixed(4)} z dnia ${snapshot.publishedAt}`;
  }
}
