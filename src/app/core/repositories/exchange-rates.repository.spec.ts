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

  it('should request EUR from the documented NBP endpoint and map rates[0].mid/effectiveDate', () => {
    let receivedQuote: unknown;

    repository.getExchangeRate('EUR').subscribe((quote) => {
      receivedQuote = quote;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');

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
      code: 'EUR',
      mid: 4.2941,
      effectiveDate: '2026-04-17'
    });
  });

  it('should request USD from the documented NBP endpoint and map rates[0].mid/effectiveDate', () => {
    let receivedQuote: unknown;

    repository.getExchangeRate('USD').subscribe((quote) => {
      receivedQuote = quote;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');

    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 3.85
        }
      ]
    });

    expect(receivedQuote).toEqual({
      code: 'USD',
      mid: 3.85,
      effectiveDate: '2026-04-17'
    });
  });
});
