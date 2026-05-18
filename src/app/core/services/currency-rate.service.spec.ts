import { HttpErrorResponse } from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CurrencyRateService } from './currency-rate.service';

describe('CurrencyRateService', () => {
  let service: CurrencyRateService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CurrencyRateService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(CurrencyRateService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should map latest table A response to currency rate metadata', () => {
    let actualRate: unknown;

    service.getLatestRate('USD').subscribe((rate) => {
      actualRate = rate;
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 },
        ],
      },
    ]);

    expect(actualRate).toEqual({
      table: 'A',
      tableNo: '074/A/NBP/2026',
      effectiveDate: '2026-04-17',
      currencyCode: 'USD',
      currencyName: 'dolar amerykański',
      mid: 3.85,
    });
  });

  it('should fail when requested currency is missing in latest table A response', () => {
    let actualError: Error | undefined;

    service.getLatestRate('USD').subscribe({
      next: () => fail('expected missing currency error'),
      error: (error: Error) => {
        actualError = error;
      },
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json');

    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
        ],
      },
    ]);

    expect(actualError).toEqual(jasmine.any(Error));
    expect(actualError?.message).toContain('USD');
  });

  it('should propagate http errors from NBP', () => {
    let actualError: HttpErrorResponse | undefined;

    service.getLatestRate('EUR').subscribe({
      next: () => fail('expected http error'),
      error: (error: HttpErrorResponse) => {
        actualError = error;
      },
    });

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json');

    request.flush('NBP unavailable', {
      status: 500,
      statusText: 'Server Error',
    });

    expect(actualError).toEqual(jasmine.any(HttpErrorResponse));
    expect(actualError?.status).toBe(500);
  });
});
