import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ExchangeRatesRepository } from './exchange-rates.repository';

describe('ExchangeRatesRepository', () => {
  let repository: ExchangeRatesRepository;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExchangeRatesRepository, provideHttpClient(), provideHttpClientTesting()]
    });

    repository = TestBed.inject(ExchangeRatesRepository);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should request the exact NBP EUR endpoint and map the response', () => {
    let receivedQuote: unknown;

    repository.getCurrentRate('EUR').subscribe((quote) => {
      receivedQuote = quote;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');

    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 4.2941
        }
      ]
    });

    expect(receivedQuote).toEqual({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      effectiveDate: '2026-04-17',
      mid: 4.2941
    });
  });

  it('should fail when the NBP payload does not include rates[0].mid', () => {
    let receivedError: unknown;

    repository.getCurrentRate('USD').subscribe({
      error: (error) => {
        receivedError = error;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17'
        }
      ]
    });

    expect(receivedError).toEqual(jasmine.any(Error));
    expect((receivedError as Error).message).toBe('Invalid NBP exchange-rate response payload');
  });

  it('should pass through HTTP errors from the NBP service', () => {
    let receivedError: unknown;

    repository.getCurrentRate('EUR').subscribe({
      error: (error) => {
        receivedError = error;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');

    request.flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    expect(receivedError).toEqual(jasmine.objectContaining({ status: 503 }));
  });
});
