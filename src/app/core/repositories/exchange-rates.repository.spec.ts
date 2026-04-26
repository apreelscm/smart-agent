import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ExchangeRateResult, ExchangeRatesRepository } from './exchange-rates.repository';

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

  it('should request and map the EUR rate from the NBP API', () => {
    let actualResult: ExchangeRateResult | undefined;

    repository.getExchangeRate('EUR').subscribe((result) => {
      actualResult = result;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 4.2941
        }
      ]
    });

    expect(actualResult).toEqual({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      effectiveDate: '2026-04-17',
      mid: 4.2941
    });
  });

  it('should request the USD rate from the documented endpoint', () => {
    repository.getExchangeRate('USD').subscribe();

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 3.85
        }
      ]
    });
  });

  it('should error when the payload does not contain a usable mid rate', () => {
    let actualError: Error | undefined;

    repository.getExchangeRate('EUR').subscribe({
      next: () => fail('Expected invalid NBP payload to fail'),
      error: (error) => {
        actualError = error;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          effectiveDate: '2026-04-17'
        }
      ]
    });

    expect(actualError).toEqual(jasmine.any(Error));
    expect(actualError?.message).toContain('usable mid rate');
  });

  it('should propagate API errors', () => {
    let actualError: { status?: number } | undefined;

    repository.getExchangeRate('USD').subscribe({
      next: () => fail('Expected NBP API error to be propagated'),
      error: (error) => {
        actualError = error;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    request.flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    expect(actualError?.status).toBe(503);
  });
});
