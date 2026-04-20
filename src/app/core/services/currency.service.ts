import { CurrencyPipe, formatDate } from '@angular/common';
import { Injectable, computed, effect, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import {
  ConvertedMoneyView,
  ExchangeRateAvailability,
  ExchangeRateTable,
  RateMetadata,
  SupportedCurrency,
  foreignCurrencies,
  supportedCurrencies
} from '../models';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly currencyPipe = inject(CurrencyPipe);

  private readonly ratesState = signal<ExchangeRateTable | null>(null);
  private readonly loadingState = signal(true);
  private readonly errorState = signal<string | null>(null);

  readonly supportedCurrencies = supportedCurrencies;
  readonly rates = this.ratesState.asReadonly();
  readonly isLoading = this.loadingState.asReadonly();
  readonly availability = computed<ExchangeRateAvailability>(() => {
    const rates = this.ratesState();

    if (!rates) {
      return {
        available: false,
        message: this.errorState() ?? 'Kursy walut są chwilowo niedostępne. Prezentujemy kwoty w PLN.'
      };
    }

    const hasAllForeignRates = foreignCurrencies.every((currency) => typeof rates.rates[currency] === 'number');

    if (!hasAllForeignRates) {
      return {
        available: false,
        message: 'Nie udało się pobrać pełnych kursów EUR/USD. Prezentujemy kwoty w PLN.'
      };
    }

    return {
      available: true,
      message: null
    };
  });
  readonly isForeignCurrencyAvailable = computed(() => this.availability().available);
  readonly rateMetadata = computed<RateMetadata[]>(() => {
    const rates = this.ratesState();

    if (!rates) {
      return [];
    }

    return foreignCurrencies.map((currency) => ({
      currency,
      rate: rates.rates[currency],
      publicationDate: rates.publicationDate,
      sourceTableNo: rates.sourceTableNo
    }));
  });

  constructor() {
    this.exchangeRatesRepository
      .getCurrentRates()
      .pipe(
        takeUntilDestroyed(),
        catchError(() => {
          this.ratesState.set(null);
          this.errorState.set('Nie udało się pobrać aktualnych kursów NBP. Prezentujemy kwoty w PLN.');
          this.loadingState.set(false);
          return of(null);
        })
      )
      .subscribe((rates) => {
        if (!rates) {
          return;
        }

        this.ratesState.set(rates);
        this.errorState.set(null);
        this.loadingState.set(false);
      });

    effect(() => {
      if (this.ratesState()) {
        this.loadingState.set(false);
      }
    });
  }

  convertFromPln(amountPln: number, targetCurrency: SupportedCurrency): ConvertedMoneyView {
    if (targetCurrency === 'PLN') {
      return {
        originalAmount: amountPln,
        originalCurrency: 'PLN',
        convertedAmount: amountPln,
        targetCurrency,
        rate: null,
        publicationDate: this.ratesState()?.publicationDate ?? null
      };
    }

    const rate = this.ratesState()?.rates[targetCurrency];

    if (typeof rate !== 'number') {
      return {
        originalAmount: amountPln,
        originalCurrency: 'PLN',
        convertedAmount: amountPln,
        targetCurrency: 'PLN',
        rate: null,
        publicationDate: null
      };
    }

    return {
      originalAmount: amountPln,
      originalCurrency: 'PLN',
      convertedAmount: Number((amountPln / rate).toFixed(2)),
      targetCurrency,
      rate,
      publicationDate: this.ratesState()?.publicationDate ?? null
    };
  }

  convertToPln(amount: number, sourceCurrency: SupportedCurrency): ConvertedMoneyView {
    if (sourceCurrency === 'PLN') {
      return {
        originalAmount: amount,
        originalCurrency: 'PLN',
        convertedAmount: Math.round(amount),
        targetCurrency: 'PLN',
        rate: null,
        publicationDate: this.ratesState()?.publicationDate ?? null
      };
    }

    const rate = this.ratesState()?.rates[sourceCurrency];

    if (typeof rate !== 'number') {
      return {
        originalAmount: amount,
        originalCurrency: sourceCurrency,
        convertedAmount: 0,
        targetCurrency: 'PLN',
        rate: null,
        publicationDate: null
      };
    }

    return {
      originalAmount: amount,
      originalCurrency: sourceCurrency,
      convertedAmount: Math.round(amount * rate),
      targetCurrency: 'PLN',
      rate,
      publicationDate: this.ratesState()?.publicationDate ?? null
    };
  }

  formatAmount(amount: number, currency: SupportedCurrency): string {
    const digitsInfo = currency === 'PLN' ? '1.0-0' : '1.2-2';
    return this.currencyPipe.transform(amount, currency, 'symbol-narrow', digitsInfo, 'pl-PL') ?? `${amount}`;
  }

  formatPlnEquivalent(amountPln: number): string {
    return this.formatAmount(amountPln, 'PLN');
  }

  rateLabel(selectedCurrency: SupportedCurrency): string | null {
    if (selectedCurrency === 'PLN') {
      return null;
    }

    const rates = this.ratesState();

    if (!rates) {
      return null;
    }

    const rate = rates.rates[selectedCurrency];

    if (typeof rate !== 'number') {
      return null;
    }

    return `Kurs NBP (${rates.sourceTableNo}) z ${formatDate(rates.publicationDate, 'dd.MM.yyyy', 'pl-PL')}: 1 ${selectedCurrency} = ${rate.toFixed(4)} PLN`;
  }

  helperLabelForForeignInput(amount: number, sourceCurrency: SupportedCurrency): string | null {
    if (sourceCurrency === 'PLN') {
      return null;
    }

    const converted = this.convertToPln(amount, sourceCurrency);

    if (!converted.rate || !converted.publicationDate) {
      return null;
    }

    return `Około ${this.formatPlnEquivalent(converted.convertedAmount)} wg kursu NBP z ${formatDate(converted.publicationDate, 'dd.MM.yyyy', 'pl-PL')}.`;
  }
}
