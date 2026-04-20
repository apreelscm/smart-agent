import { TestBed } from '@angular/core/testing';
import { CurrencyPipe } from '@angular/common';
import { of, throwError } from 'rxjs';
import { CurrencyService } from './currency.service';
import { ExchangeRatesRepository } from '../repositories/exchange-rates.repository';

describe('CurrencyService', () => {
  function createServiceWithResponse(getCurrentRates: ExchangeRatesRepository['getCurrentRates']) {
    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      providers: [
        CurrencyService,
        CurrencyPipe,
        {
          provide: ExchangeRatesRepository,
          useValue: { getCurrentRates }
        }
      ]
    });

    return TestBed.inject(CurrencyService);
  }

  it('should convert from PLN to EUR using current rate', () => {
    const service = createServiceWithResponse(() =>
      of({
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: { EUR: 4, USD: 3.5 }
      })
    );

    expect(service.convertFromPln(400, 'EUR')).toEqual(
      jasmine.objectContaining({
        convertedAmount: 100,
        targetCurrency: 'EUR',
        rate: 4
      })
    );
  });

  it('should convert from USD to PLN using current rate', () => {
    const service = createServiceWithResponse(() =>
      of({
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: { EUR: 4, USD: 3.5 }
      })
    );

    expect(service.convertToPln(10, 'USD')).toEqual(
      jasmine.objectContaining({
        convertedAmount: 35,
        targetCurrency: 'PLN',
        rate: 3.5
      })
    );
  });

  it('should format PLN with zero decimals and EUR with two decimals', () => {
    const service = createServiceWithResponse(() =>
      of({
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: { EUR: 4, USD: 3.5 }
      })
    );

    expect(service.formatAmount(1234, 'PLN')).toContain('1');
    expect(service.formatAmount(1234.5, 'EUR')).toContain('1');
  });

  it('should mark foreign currency unavailable on repository failure', () => {
    const service = createServiceWithResponse(() => throwError(() => new Error('network')));

    expect(service.isForeignCurrencyAvailable()).toBeFalse();
    expect(service.availability().message).toContain('Kursy');
  });

  it('should provide rate label for foreign currency', () => {
    const service = createServiceWithResponse(() =>
      of({
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: { EUR: 4.2941, USD: 3.85 }
      })
    );

    expect(service.rateLabel('EUR')).toContain('1 EUR = 4.2941 PLN');
  });
}
