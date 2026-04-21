import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ExchangeRateSnapshot } from '../models';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';
import { CurrencyService } from './currency.service';

describe('CurrencyService', () => {
  const snapshot: ExchangeRateSnapshot = {
    source: 'NBP',
    table: 'A',
    effectiveDate: '2026-04-17',
    rates: {
      EUR: 4.3,
      USD: 3.9
    }
  };

  let repository: jasmine.SpyObj<ExchangeRatesRepository>;
  let service: CurrencyService;

  beforeEach(() => {
    repository = jasmine.createSpyObj<ExchangeRatesRepository>('ExchangeRatesRepository', ['getExchangeRates']);

    TestBed.configureTestingModule({
      providers: [
        CurrencyService,
        {
          provide: ExchangeRatesRepository,
          useValue: repository
        }
      ]
    });

    service = TestBed.inject(CurrencyService);
  });

  it('converts PLN to selected foreign currency and back', () => {
    expect(service.convertFromPln(430, 'EUR', snapshot)).toBeCloseTo(100, 6);
    expect(service.convertToPln(100, 'EUR', snapshot)).toBe(430);
    expect(service.convertToPln(100, 'USD', snapshot)).toBe(390);
  });

  it('returns formatting digits for supported currencies', () => {
    expect(service.formatDigits('PLN')).toBe('1.0-0');
    expect(service.formatDigits('EUR')).toBe('1.2-2');
    expect(service.formatDigits('USD')).toBe('1.2-2');
  });

  it('loads snapshot and enables foreign currencies', (done) => {
    repository.getExchangeRates.and.returnValue(of(snapshot));

    service.ensureRatesLoaded().subscribe((state) => {
      expect(state.snapshot).toEqual(snapshot);
      expect(state.error).toBeNull();
      expect(service.hasForeignCurrencyRates(state)).toBeTrue();
      done();
    });
  });

  it('falls back to PLN when NBP request fails', (done) => {
    repository.getExchangeRates.and.returnValue(throwError(() => new Error('boom')));

    service.ensureRatesLoaded().subscribe((state) => {
      expect(state.snapshot).toBeNull();
      expect(state.error).toBe(service.fallbackMessage());
      expect(service.hasForeignCurrencyRates(state)).toBeFalse();
      expect(service.formatAmount(250, 'EUR', null)).toContain('zł');
      done();
    });
  });
});
