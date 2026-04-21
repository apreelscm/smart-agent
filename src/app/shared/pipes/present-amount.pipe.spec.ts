import { TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { ExchangeRatesSnapshot } from '../../core/models';
import { ExchangeRatesRepository } from '../../core/repositories/exchange-rates.repository';
import { CurrencyPresentationService } from '../../core/services/currency-presentation.service';
import { PresentAmountPipe } from './present-amount.pipe';

class ExchangeRatesRepositoryStub {
  getExchangeRates(): Observable<ExchangeRatesSnapshot> {
    return of({
      effectiveDate: '2026-04-17',
      rates: {
        EUR: 4,
        USD: 3.5
      }
    });
  }
}

describe('PresentAmountPipe', () => {
  it('formats PLN with zero decimals and EUR/USD with two decimals', () => {
    TestBed.configureTestingModule({
      providers: [
        CurrencyPresentationService,
        {
          provide: ExchangeRatesRepository,
          useClass: ExchangeRatesRepositoryStub
        }
      ]
    });

    const service = TestBed.inject(CurrencyPresentationService);
    const pipe = TestBed.runInInjectionContext(() => new PresentAmountPipe());

    expect(pipe.transform(1234, 'PLN')).toContain('1 234');
    expect(pipe.transform(1234, 'PLN')).toContain('zł');

    service.selectCurrency('EUR');
    expect(pipe.transform(400)).toContain('100,00');
    expect(pipe.transform(400)).toContain('€');

    service.selectCurrency('USD');
    expect(pipe.transform(350)).toContain('100,00');
    expect(pipe.transform(350)).toContain('USD');
  });
});
