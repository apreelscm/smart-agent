import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { CurrencyCode } from '../../../core/models';

@Component({
  selector: 'app-currency-switcher',
  standalone: true,
  imports: [CommonModule, ButtonDirective],
  templateUrl: './currency-switcher.component.html',
  styleUrl: './currency-switcher.component.scss'
})
export class CurrencySwitcherComponent {
  readonly selectedCurrency = input.required<CurrencyCode>();
  readonly disabledCurrencies = input<CurrencyCode[]>([]);
  readonly rateNote = input<string | null>(null);
  readonly fallbackMessage = input<string | null>(null);
  readonly currencySelected = output<CurrencyCode>();

  protected readonly currencies: CurrencyCode[] = ['PLN', 'EUR', 'USD'];

  protected isDisabled(currency: CurrencyCode): boolean {
    return this.disabledCurrencies().includes(currency);
  }

  protected selectCurrency(currency: CurrencyCode): void {
    if (this.isDisabled(currency)) {
      return;
    }

    this.currencySelected.emit(currency);
  }
}
