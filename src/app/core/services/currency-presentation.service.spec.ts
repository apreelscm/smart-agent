import { TestBed } from '@angular/core/testing';
import { Observable, of, throwError } from 'rxjs';
import { ExchangeRatesSnapshot } from '../models';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';
import { CurrencyPresentationService } from './currency-presentation.service';

class ExchangeRatesRepositoryStub {
  constructor(private readonly source$: Observable<ExchangeRatesSnapshot>) {}

  getExchangeRates(): Observable<ExchangeRatesSnapshot> {
    return this.source$;
  }
}

describe('CurrencyPresentationService', () => {
  it('converts and formats amounts for selected currency', () => {
    TestBed.configureTestingModule({
      providers: [
        CurrencyPresentationService,
        {
          provide: ExchangeRatesRepository,
          useValue: new ExchangeRatesRepositoryStub(
            of({
              effectiveDate: '2026-04-17',
              rates: {
                EUR: 4,
                USD: 3.5
              }
            })
          )
        }
      ]
    });

    const service = TestBed.inject(CurrencyPresentationService);

    expect(service.selectedCurrency()).toBe('PLN');
    service.selectCurrency('EUR');

    expect(service.selectedCurrency()).toBe('EUR');
    expect(service.convertFromPln(400, 'EUR')).toBe(100);
    expect(service.convertToPln(100, 'EUR')).toBe(400);
    expect(service.formatAmount(400)).toContain('€');
    expect(service.activeRateInfo()?.effectiveDate).toBe('2026-04-17');
  });

  it('forces PLN and disables foreign currencies on repository error', () => {
    TestBed.configureTestingModule({
      providers: [
        CurrencyPresentationService,
        {
          provide: ExchangeRatesRepository,
          useValue: new ExchangeRatesRepositoryStub(throwError(() => new Error('NBP unavailable')))
        }
      ]
    });

    const service = TestBed.inject(CurrencyPresentationService);

    service.selectCurrency('USD');

    expect(service.selectedCurrency()).toBe('PLN');
    expect(service.canUseCurrency('EUR')).toBeFalse();
    expect(service.degradationMessage()).toContain('wyłącznie w PLN');
    expect(service.formatAmount(100)).toContain('zł');
  });
});
