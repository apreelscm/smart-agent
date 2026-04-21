import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ExchangeRatesRepository } from './exchange-rates.repository';

describe('ExchangeRatesRepository', () => {
  let repository: ExchangeRatesRepository;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), ExchangeRatesRepository]
    });

    repository = TestBed.inject(ExchangeRatesRepository);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('maps NBP table A response for EUR and USD', () => {
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
          { code: 'EUR', mid: 4.2941 },
          { code: 'USD', mid: 3.85 }
        ]
      }
    ]);

    expect(result).toEqual({
      table: 'A',
      sourceTableNo: '074/A/NBP/2026',
      publishedAt: '2026-04-17',
      rates: {
        EUR: 4.2941,
        USD: 3.85
      }
    });
  });

  it('keeps missing currency as undefined', () => {
    let result: any;

    repository.getCurrentRates().subscribe((value) => {
      result = value;
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    request.flush([
      {
        table: 'A',
        no: '075/A/NBP/2026',
        effectiveDate: '2026-04-18',
        rates: [{ code: 'EUR', mid: 4.21 }]
      }
    ]);

    expect(result.rates.EUR).toBe(4.21);
    expect(result.rates.USD).toBeUndefined();
  });
});
