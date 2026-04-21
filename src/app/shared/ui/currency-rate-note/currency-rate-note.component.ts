import { CommonModule, DatePipe } from '@angular/common';
import { Component, computed, inject, input } from '@angular/core';
import { DisplayCurrency, ExchangeRateSnapshot, ForeignDisplayCurrency } from '../../../core/models';
import { CurrencyService } from '../../../core/services/currency.service';

@Component({
  selector: 'app-currency-rate-note',
  imports: [CommonModule, DatePipe],
  templateUrl: './currency-rate-note.component.html',
  styleUrl: './currency-rate-note.component.scss'
})
export class CurrencyRateNoteComponent {
  private readonly currencyService = inject(CurrencyService);

  readonly selectedCurrency = input<DisplayCurrency>('PLN');
  readonly snapshot = input<ExchangeRateSnapshot | null>(null);
  readonly message = input<string | null>(null);

  protected readonly foreignCurrency = computed<ForeignDisplayCurrency | null>(() => {
    const currency = this.selectedCurrency();
    return currency === 'PLN' ? null : currency;
  });

  protected rateLabel(): string {
    const currency = this.foreignCurrency();
    const snapshot = this.snapshot();

    if (!currency || !snapshot) {
      return '';
    }

    return this.currencyService.formatExchangeRate(currency, snapshot);
  }
}
