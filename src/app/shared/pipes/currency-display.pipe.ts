import { Pipe, PipeTransform } from '@angular/core';

type Currency = 'PLN' | 'EUR' | 'USD';

@Pipe({
  name: 'currencyDisplay',
  standalone: true,
  pure: true
})
export class CurrencyDisplayPipe implements PipeTransform {
  transform(value: number | null | undefined, currency: Currency = 'PLN'): string {
    const numeric = Number(value ?? 0);
    switch (currency) {
      case 'PLN': {
        const nf = new Intl.NumberFormat('pl-PL', {
          style: 'currency',
          currency: 'PLN',
          minimumFractionDigits: 0,
          maximumFractionDigits: 0,
          currencyDisplay: 'narrowSymbol'
        });
        return nf.format(Math.round(numeric));
      }
      case 'EUR': {
        const nf = new Intl.NumberFormat('pl-PL', {
          style: 'currency',
          currency: 'EUR',
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
          currencyDisplay: 'symbol'
        });
        return nf.format(Math.round(numeric * 100) / 100);
      }
      case 'USD': {
        const nf = new Intl.NumberFormat('en-US', {
          style: 'currency',
          currency: 'USD',
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
          currencyDisplay: 'symbol'
        });
        return nf.format(Math.round(numeric * 100) / 100);
      }
      default:
        return String(numeric);
    }
  }
}
