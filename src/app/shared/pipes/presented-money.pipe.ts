import { Pipe, PipeTransform, inject } from '@angular/core';
import { CurrencyCode, ExchangeRates } from '../../core/models';
import { CurrencyConversionService } from '../../core/services/currency-conversion.service';

@Pipe({
  name: 'presentedMoney',
  standalone: true
})
export class PresentedMoneyPipe implements PipeTransform {
  private readonly currencyConversionService = inject(CurrencyConversionService);

  transform(amountInPln: number, currency: CurrencyCode, rates?: ExchangeRates): string {
    return this.currencyConversionService.formatAmount(amountInPln, currency, rates);
  }
}
