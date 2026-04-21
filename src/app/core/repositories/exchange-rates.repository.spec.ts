import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ExchangeRatesRepository } from './exchange-rates.repository';

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

  it('should request current NBP table A and map EUR and USD rates', () => {
    let actualResponse:
      | {
          effectiveDate: string;
          EUR?: number;
          USD?: number;
        }
      | undefined;

    repository.getExchangeRates().subscribe((response) => {
      actualResponse = response;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A?format=json');

    expect(request.request.method).toBe('GET');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 },
          { currency: 'funt szterling', code: 'GBP', mid: 5.0123 }
        ]
      }
    ]);

    expect(actualResponse).toEqual({
      effectiveDate: '2026-04-17',
      EUR: 4.2941,
      USD: 3.85
    });
  });

  it('should keep available rates when one expected currency is missing from payload', () => {
    let actualResponse:
      | {
          effectiveDate: string;
          EUR?: number;
          USD?: number;
        }
      | undefined;

    repository.getExchangeRates().subscribe((response) => {
      actualResponse = response;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A?format=json');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [{ currency: 'euro', code: 'EUR', mid: 4.2941 }]
      }
    ]);

    expect(actualResponse?.effectiveDate).toBe('2026-04-17');
    expect(actualResponse?.EUR).toBe(4.2941);
    expect(actualResponse?.USD).toBeUndefined();
  });
});
