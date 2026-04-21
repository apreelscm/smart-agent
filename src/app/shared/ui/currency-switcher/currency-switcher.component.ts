import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { DisplayCurrency } from '../../../core/models';

@Component({
  selector: 'app-currency-switcher',
  imports: [CommonModule],
  templateUrl: './currency-switcher.component.html',
  styleUrl: './currency-switcher.component.scss'
})
export class CurrencySwitcherComponent {
  readonly selectedCurrency = input<DisplayCurrency>('PLN');
  readonly foreignCurrencyAvailable = input<boolean>(false);
  readonly currencyChange = output<DisplayCurrency>();

  protected readonly currencies: DisplayCurrency[] = ['PLN', 'EUR', 'USD'];

  protected selectCurrency(currency: DisplayCurrency): void {
    if (this.isDisabled(currency)) {
      return;
    }

    this.currencyChange.emit(currency);
  }

  protected isDisabled(currency: DisplayCurrency): boolean {
    return currency !== 'PLN' && !this.foreignCurrencyAvailable();
  }
}
