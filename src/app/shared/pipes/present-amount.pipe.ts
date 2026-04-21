import { Pipe, PipeTransform, inject } from '@angular/core';
import { CurrencyCode } from '../../core/models';
import { CurrencyPresentationService } from '../../core/services/currency-presentation.service';

@Pipe({
  name: 'presentAmount',
  standalone: true,
  pure: false
})
export class PresentAmountPipe implements PipeTransform {
  private readonly currencyPresentation = inject(CurrencyPresentationService);

  transform(amountInPln: number | null | undefined, currency?: CurrencyCode): string {
    return this.currencyPresentation.formatAmount(amountInPln, currency);
  }
}
