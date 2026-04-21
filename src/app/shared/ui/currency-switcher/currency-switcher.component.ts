import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { SUPPORTED_CURRENCIES } from '../../../core/models';
import { CurrencyPresentationService } from '../../../core/services/currency-presentation.service';

@Component({
  selector: 'app-currency-switcher',
  imports: [CommonModule, ButtonDirective],
  templateUrl: './currency-switcher.component.html',
  styleUrl: './currency-switcher.component.scss'
})
export class CurrencySwitcherComponent {
  protected readonly currencyPresentation = inject(CurrencyPresentationService);
  protected readonly currencies = SUPPORTED_CURRENCIES;
}
