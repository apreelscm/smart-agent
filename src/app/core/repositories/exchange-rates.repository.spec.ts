import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
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

  it('maps EUR and USD rates from NBP table A response', () => {
    let result: unknown;

    repository.getExchangeRates().subscribe((snapshot) => {
      result = snapshot;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');

    expect(request.request.method).toBe('GET');
    expect(request.request.headers.has('Authorization')).toBeFalse();

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { code: 'EUR', mid: 4.2941 },
          { code: 'USD', mid: 3.85 }
        ]
      }
    ]);

    expect(result).toEqual({
      source: 'NBP',
      table: 'A',
      tableNumber: '074/A/NBP/2026',
      effectiveDate: '2026-04-17',
      rates: {
        EUR: 4.2941,
        USD: 3.85
      }
    });
  });

  it('fails when required foreign currency rates are missing', () => {
    let error: unknown;

    repository.getExchangeRates().subscribe({
      error: (receivedError) => {
        error = receivedError;
      }
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [{ code: 'EUR', mid: 4.2941 }]
      }
    ]);

    expect(error).toEqual(jasmine.any(Error));
  });
});
