import { TestBed } from '@angular/core/testing';
import { ExchangeRatesRepository } from '../../core/repositories/exchange-rates.repository';
import { CurrencyService } from '../../core/services/currency.service';
import { DisplayMoneyPipe } from './display-money.pipe';

describe('DisplayMoneyPipe', () => {
  let pipe: DisplayMoneyPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DisplayMoneyPipe,
        CurrencyService,
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () => {
              throw new Error('not used');
            }
          }
        }
      ]
    });

    pipe = TestBed.inject(DisplayMoneyPipe);
  });

  it('formats PLN amounts without decimal digits', () => {
    expect(pipe.transform(1234, 'PLN', null)).toContain('1 234');
    expect(pipe.transform(1234, 'PLN', null)).toContain('zł');
  });

  it('formats EUR and USD with two decimal digits', () => {
    const snapshot = {
      source: 'NBP' as const,
      table: 'A' as const,
      effectiveDate: '2026-04-17',
      rates: {
        EUR: 4,
        USD: 4
      }
    };

    expect(pipe.transform(400, 'EUR', snapshot)).toContain('100,00');
    expect(pipe.transform(400, 'EUR', snapshot)).toContain('€');
    expect(pipe.transform(400, 'USD', snapshot)).toContain('100,00');
    expect(pipe.transform(400, 'USD', snapshot)).toContain('$');
  });
});
