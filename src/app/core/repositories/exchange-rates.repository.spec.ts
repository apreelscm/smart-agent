import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import {
  EXCHANGE_RATES_API_URL,
  EXCHANGE_RATES_UNAVAILABLE_STATE,
  ExchangeRatesRepository,
  ExchangeRatesState
} from './exchange-rates.repository';

describe('ExchangeRatesRepository', () => {
  let repository: ExchangeRatesRepository;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), ExchangeRatesRepository]
    });

    repository = TestBed.inject(ExchangeRatesRepository);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should map NBP table A response to normalized EUR and USD rates', () => {
    let result: ExchangeRatesState | undefined;

    repository.getExchangeRates().subscribe((state) => {
      result = state;
    });

    const request = httpTestingController.expectOne(EXCHANGE_RATES_API_URL);

    expect(request.request.method).toBe('GET');

    request.flush([
      {
        table: 'A',
        no: '078/A/NBP/2026',
        effectiveDate: '2026-04-20',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.35 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.92 },
          { currency: 'frank szwajcarski', code: 'CHF', mid: 4.61 }
        ]
      }
    ]);

    expect(result).toEqual({
      status: 'available',
      available: true,
      effectiveDate: '2026-04-20',
      rates: {
        EUR: 4.35,
        USD: 3.92
      }
    });
  });

  it('should return unavailable fallback when the NBP request fails', () => {
    let result: ExchangeRatesState | undefined;

    repository.getExchangeRates().subscribe((state) => {
      result = state;
    });

    const request = httpTestingController.expectOne(EXCHANGE_RATES_API_URL);

    request.flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    expect(result).toEqual(EXCHANGE_RATES_UNAVAILABLE_STATE);
  });
});
