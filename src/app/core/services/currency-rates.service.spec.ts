import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { CurrencyRatesService } from './currency-rates.service';

describe('CurrencyRatesService', () => {
  let service: CurrencyRatesService;
  let httpMock: HttpTestingController;

  const todayUrl = 'https://api.nbp.pl/api/exchangerates/tables/A/today/?format=json';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CurrencyRatesService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(CurrencyRatesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('loads rates from today table when it is available', () => {
    let result: unknown;

    service.getRates().subscribe(value => {
      result = value;
    });

    const request = httpMock.expectOne(todayUrl);
    expect(request.request.method).toBe('GET');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-05-20',
        rates: [
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 },
          { currency: 'euro', code: 'EUR', mid: 4.29 },
        ],
      },
    ]);

    expect(result).toEqual({
      table: 'A',
      tableNumber: '074/A/NBP/2026',
      effectiveDate: '2026-05-20',
      rates: {
        USD: 3.85,
        EUR: 4.29,
      },
    });
  });

  it('falls back to previous day when today table request fails', () => {
    let result: unknown;
    const previousDayUrl = `https://api.nbp.pl/api/exchangerates/tables/A/${getPreviousDayString()}/?format=json`;

    service.getRates().subscribe(value => {
      result = value;
    });

    const todayRequest = httpMock.expectOne(todayUrl);
    todayRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    const previousDayRequest = httpMock.expectOne(previousDayUrl);
    expect(previousDayRequest.request.method).toBe('GET');

    previousDayRequest.flush([
      {
        table: 'A',
        no: '073/A/NBP/2026',
        effectiveDate: getPreviousDayString(),
        rates: [
          { currency: 'dolar amerykański', code: 'USD', mid: 3.81 },
          { currency: 'euro', code: 'EUR', mid: 4.25 },
        ],
      },
    ]);

    expect(result).toEqual({
      table: 'A',
      tableNumber: '073/A/NBP/2026',
      effectiveDate: getPreviousDayString(),
      rates: {
        USD: 3.81,
        EUR: 4.25,
      },
    });
  });

  it('returns an error when required rates are unavailable in both responses', () => {
    let capturedError: unknown;
    const previousDayUrl = `https://api.nbp.pl/api/exchangerates/tables/A/${getPreviousDayString()}/?format=json`;

    service.getRates().subscribe({
      error: error => {
        capturedError = error;
      },
    });

    const todayRequest = httpMock.expectOne(todayUrl);
    todayRequest.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-05-20',
        rates: [{ currency: 'dolar amerykański', code: 'USD', mid: 3.85 }],
      },
    ]);

    const previousDayRequest = httpMock.expectOne(previousDayUrl);
    previousDayRequest.flush([
      {
        table: 'A',
        no: '073/A/NBP/2026',
        effectiveDate: getPreviousDayString(),
        rates: [{ currency: 'euro', code: 'EUR', mid: 4.25 }],
      },
    ]);

    expect(capturedError).toEqual(jasmine.any(Error));
  });

  function getPreviousDayString(): string {
    const date = new Date();
    date.setDate(date.getDate() - 1);

    const year = date.getFullYear();
    const month = `${date.getMonth() + 1}`.padStart(2, '0');
    const day = `${date.getDate()}`.padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
});
