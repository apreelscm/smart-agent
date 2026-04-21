import { Pipe, PipeTransform, inject } from '@angular/core';
import { DisplayCurrency, ExchangeRateSnapshot } from '../../core/models';
import { CurrencyService } from '../../core/services/currency.service';

@Pipe({
  name: 'displayMoney',
  standalone: true
})
export class DisplayMoneyPipe implements PipeTransform {
  private readonly currencyService = inject(CurrencyService);

  transform(amountPln: number | null | undefined, currency: DisplayCurrency, snapshot: ExchangeRateSnapshot | null = null): string {
    return this.currencyService.formatAmount(amountPln, currency, snapshot);
  }
}
