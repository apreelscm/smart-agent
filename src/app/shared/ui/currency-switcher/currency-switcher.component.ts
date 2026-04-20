import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { SupportedCurrency, supportedCurrencies } from '../../../core/models';

@Component({
  selector: 'app-currency-switcher',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './currency-switcher.component.html',
  styleUrl: './currency-switcher.component.scss'
})
export class CurrencySwitcherComponent {
  readonly selectedCurrency = input.required<SupportedCurrency>();
  readonly foreignCurrencyAvailable = input<boolean>(false);
  readonly availabilityMessage = input<string | null>(null);
  readonly rateLabel = input<string | null>(null);
  readonly disabled = input<boolean>(false);
  readonly currencyChange = output<SupportedCurrency>();

  protected readonly currencies = supportedCurrencies;

  protected isDisabled(currency: SupportedCurrency): boolean {
    if (this.disabled()) {
      return true;
    }

    return currency !== 'PLN' && !this.foreignCurrencyAvailable();
  }

  protected select(currency: SupportedCurrency): void {
    if (this.isDisabled(currency)) {
      return;
    }

    this.currencyChange.emit(currency);
  }
}
