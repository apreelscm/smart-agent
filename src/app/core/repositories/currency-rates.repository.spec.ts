import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { CurrencyRatesRepository } from './currency-rates.repository';

describe('CurrencyRatesRepository', () => {
  let repository: CurrencyRatesRepository;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CurrencyRatesRepository, provideHttpClient(), provideHttpClientTesting()]
    });

    repository = TestBed.inject(CurrencyRatesRepository);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should call the documented NBP endpoint and map the response', () => {
    let actualRate:
      | {
          code: string;
          currency: string;
          effectiveDate: string;
          midRate: number;
          sourceTableNo?: string;
        }
      | undefined;

    repository.getCurrentRate('EUR').subscribe((rate) => {
      actualRate = rate;
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

    expect(actualRate).toEqual({
      code: 'EUR',
      currency: 'euro',
      effectiveDate: '2026-04-17',
      midRate: 4.2941,
      sourceTableNo: '074/A/NBP/2026'
    });
  });

  it('should fail when the response does not contain a usable rate', () => {
    let actualError: unknown;

    repository.getCurrentRate('USD').subscribe({
      next: () => fail('Expected repository to reject invalid payload'),
      error: (error) => {
        actualError = error;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: []
    });

    expect(actualError).toEqual(jasmine.any(Error));
    expect((actualError as Error).message).toBe('Invalid NBP exchange-rate response.');
  });

  it('should surface http errors from the NBP API', () => {
    let actualStatus: number | undefined;

    repository.getCurrentRate('EUR').subscribe({
      next: () => fail('Expected request to fail'),
      error: (error) => {
        actualStatus = error.status;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');

    request.flush('Service unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    expect(actualStatus).toBe(503);
  });
});
