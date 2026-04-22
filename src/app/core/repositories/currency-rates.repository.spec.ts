import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CurrencyRatesRepository } from './currency-rates.repository';

describe('CurrencyRatesRepository', () => {
  let repository: CurrencyRatesRepository;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });

    repository = TestBed.inject(CurrencyRatesRepository);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should request the documented NBP table A endpoint and map EUR/USD rates', () => {
    let result: unknown;

    repository.getRates().subscribe((value) => {
      result = value;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');

    expect(request.request.method).toBe('GET');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 },
          { currency: 'frank szwajcarski', code: 'CHF', mid: 4.5 }
        ]
      }
    ]);

    expect(result).toEqual({
      tableType: 'A',
      sourceTableNo: '074/A/NBP/2026',
      publicationDate: '2026-04-17',
      rates: {
        EUR: 4.2941,
        USD: 3.85
      }
    });
  });

  it('should share one fetched response across multiple subscriptions', () => {
    const received: unknown[] = [];

    repository.getRates().subscribe((value) => received.push(value));
    repository.getRates().subscribe((value) => received.push(value));

    const requests = httpTestingController.match('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');

    expect(requests.length).toBe(1);

    requests[0].flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 }
        ]
      }
    ]);

    expect(received).toEqual([
      {
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: {
          EUR: 4.2941,
          USD: 3.85
        }
      },
      {
        tableType: 'A',
        sourceTableNo: '074/A/NBP/2026',
        publicationDate: '2026-04-17',
        rates: {
          EUR: 4.2941,
          USD: 3.85
        }
      }
    ]);
  });

  it('should keep only supported rates that are present in the response', () => {
    let result: unknown;

    repository.getRates().subscribe((value) => {
      result = value;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [{ currency: 'euro', code: 'EUR', mid: 4.2941 }]
      }
    ]);

    expect(result).toEqual({
      tableType: 'A',
      sourceTableNo: '074/A/NBP/2026',
      publicationDate: '2026-04-17',
      rates: {
        EUR: 4.2941
      }
    });
  });

  it('should propagate http errors to the caller', () => {
    const errorSpy = jasmine.createSpy('error');

    repository.getRates().subscribe({
      next: () => fail('expected an error'),
      error: errorSpy
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');

    request.flush('NBP unavailable', {
      status: 500,
      statusText: 'Server Error'
    });

    expect(errorSpy).toHaveBeenCalled();
  });
});
