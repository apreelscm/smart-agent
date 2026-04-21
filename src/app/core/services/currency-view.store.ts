import { Injectable, signal } from '@angular/core';
import { DisplayCurrency } from '../models';

@Injectable()
export class CurrencyViewStore {
  private readonly currencyState = signal<DisplayCurrency>('PLN');

  readonly currency = this.currencyState.asReadonly();

  select(currency: DisplayCurrency): void {
    this.currencyState.set(currency);
  }

  forcePln(): void {
    this.currencyState.set('PLN');
  }

  reset(): void {
    this.currencyState.set('PLN');
  }
}
