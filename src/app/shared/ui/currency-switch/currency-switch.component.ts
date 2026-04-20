import { Component, EventEmitter, Output, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDirective } from 'primeng/button';
import { CurrencyService } from '../../../core/services/currency.service';
import { CurrencyDisplayPipe } from '../../pipes/currency-display.pipe';

@Component({
  selector: 'app-currency-switch',
  standalone: true,
  imports: [CommonModule, ButtonDirective, CurrencyDisplayPipe],
  template: `
    <div class="currency-switch">
      <button
        pButton
        type="button"
        class="currency-button"
        [class.currency-button--active]="selected() === 'PLN'"
        (click)="select('PLN')"
      >
        PLN
      </button>
      <button
        pButton
        type="button"
        class="currency-button"
        [class.currency-button--active]="selected() === 'EUR'"
        [disabled]="!isCurrencyEnabled('EUR')"
        (click)="select('EUR')"
        [attr.title]="currencyTooltip('EUR')"
      >
        EUR
      </button>
      <button
        pButton
        type="button"
        class="currency-button"
        [class.currency-button--active]="selected() === 'USD'"
        [disabled]="!isCurrencyEnabled('USD')"
        (click)="select('USD')"
        [attr.title]="currencyTooltip('USD')"
      >
        USD
      </button>
    </div>
  `,
  styles: [
    `
      .currency-switch {
        display: flex;
        gap: 0.5rem;
        align-items: center;
      }
      .currency-button {
        min-width: 3.25rem;
      }
      .currency-button--active {
        font-weight: 700;
      }
    `
  ]
})
export class CurrencySwitchComponent {
  private readonly currencyService = inject(CurrencyService);
  readonly selected = signal<'PLN' | 'EUR' | 'USD'>('PLN');
  @Output() readonly selectionChange = new EventEmitter<'PLN' | 'EUR' | 'USD'>();

  select(currency: 'PLN' | 'EUR' | 'USD') {
    if (!this.isCurrencyEnabled(currency)) {
      return;
    }
    this.selected.set(currency);
    this.selectionChange.emit(currency);
  }

  isCurrencyEnabled(currency: 'PLN' | 'EUR' | 'USD'): boolean {
    if (currency === 'PLN') return true;
    const rate = this.currencyService.rateFor(currency as 'EUR' | 'USD');
    return rate != null && this.currencyService.rateInfo().available;
  }

  currencyTooltip(currency: 'EUR' | 'USD'): string {
    const rate = this.currencyService.rateFor(currency);
    if (!rate) {
      return 'Kurs niedostępny';
    }
    const date = this.currencyService.ratesDate();
    return `1 ${currency} = ${rate} PLN${date ? ' (na ' + date + ')' : ''}`;
  }
}
