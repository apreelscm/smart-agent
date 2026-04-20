import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ExchangeRatesRepository } from './exchange-rates.repository';

describe('ExchangeRatesRepository', () => {
  let repository: ExchangeRatesRepository;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExchangeRatesRepository, provideHttpClient(), provideHttpClientTesting()]
    });

    repository = TestBed.inject(ExchangeRatesRepository);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should map EUR and USD rates from NBP table response', () => {
    let result: unknown;

    repository.getCurrentRates().subscribe((value) => {
      result = value;
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush([
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

  it('should throw when one expected currency is missing', () => {
    let error: unknown;

    repository.getCurrentRates().subscribe({
      error: (value) => {
        error = value;
      }
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [{ currency: 'euro', code: 'EUR', mid: 4.2941 }]
      }
    ]);

    expect(error).toEqual(jasmine.any(Error));
  });
}
