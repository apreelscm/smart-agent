import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ExchangeRate, ExchangeRatesRepository } from './exchange-rates.repository';

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

  it('should request current exchange rate from NBP and map the response', () => {
    let actualRate: ExchangeRate | undefined;

    repository.getCurrentRate('EUR').subscribe((rate) => {
      actualRate = rate;
    });

    const request = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json'
    );

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
      tableType: 'A',
      currencyCode: 'EUR',
      currencyName: 'euro',
      publicationDate: '2026-04-17',
      midRate: 4.2941,
      sourceTableNo: '074/A/NBP/2026'
    });
  });

  it('should propagate HTTP errors from NBP', () => {
    let actualError: HttpErrorResponse | undefined;

    repository.getCurrentRate('USD').subscribe({
      next: () => fail('expected HTTP error'),
      error: (error: HttpErrorResponse) => {
        actualError = error;
      }
    });

    const request = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );

    request.flush('Not Found', {
      status: 404,
      statusText: 'Not Found'
    });

    expect(actualError).toEqual(jasmine.any(HttpErrorResponse));
    expect(actualError?.status).toBe(404);
  });

  it('should fail when NBP response does not contain mid rate', () => {
    let actualError: Error | undefined;

    repository.getCurrentRate('USD').subscribe({
      next: () => fail('expected business validation error'),
      error: (error: Error) => {
        actualError = error;
      }
    });

    const request = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: null
        }
      ]
    });

    expect(actualError).toEqual(jasmine.any(Error));
    expect(actualError?.message).toContain('mid rate');
  });
});
