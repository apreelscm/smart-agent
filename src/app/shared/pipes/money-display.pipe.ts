import { Pipe, PipeTransform, inject } from '@angular/core';
import { CurrencyService } from '../../core/services/currency.service';
import { SupportedCurrency } from '../../core/models';

@Pipe({
  name: 'moneyDisplay',
  standalone: true
})
export class MoneyDisplayPipe implements PipeTransform {
  private readonly currencyService = inject(CurrencyService);

  transform(amountPln: number | null | undefined, targetCurrency: SupportedCurrency): string {
    const normalizedAmount = amountPln ?? 0;
    const converted =
      targetCurrency === 'PLN'
        ? { convertedAmount: normalizedAmount, targetCurrency }
        : this.currencyService.convertFromPln(normalizedAmount, targetCurrency);

    return this.currencyService.formatAmount(converted.convertedAmount, converted.targetCurrency);
  }
}
