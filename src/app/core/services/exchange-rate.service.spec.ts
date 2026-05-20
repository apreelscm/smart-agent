import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ExchangeRateQuote } from '../models/exchange-rate.model';
import { ExchangeRateService } from './exchange-rate.service';

describe('ExchangeRateService', () => {
  let service: ExchangeRateService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExchangeRateService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ExchangeRateService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('fetches the current quote when the current NBP endpoint succeeds', () => {
    let receivedQuote: ExchangeRateQuote | undefined;

    service.getQuote('USD').subscribe(quote => {
      receivedQuote = quote;
    });

    const request = httpController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );

    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '101/A/NBP/2026',
          effectiveDate: '2026-05-20',
          mid: 3.91,
        },
      ],
    });

    expect(receivedQuote).toEqual({
      code: 'USD',
      mid: 3.91,
      tableNo: '101/A/NBP/2026',
      effectiveDate: '2026-05-20',
    });
  });

  it('falls back to the previous day when the current quote request fails', () => {
    let receivedQuote: ExchangeRateQuote | undefined;

    service.getQuote('EUR').subscribe(quote => {
      receivedQuote = quote;
    });

    const currentRequest = httpController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json'
    );

    currentRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    const fallbackRequest = httpController.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${getPreviousDayDate()}/?format=json`
    );

    expect(fallbackRequest.request.method).toBe('GET');

    fallbackRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '100/A/NBP/2026',
          effectiveDate: '2026-05-19',
          mid: 4.28,
        },
      ],
    });

    expect(receivedQuote).toEqual({
      code: 'EUR',
      mid: 4.28,
      tableNo: '100/A/NBP/2026',
      effectiveDate: '2026-05-19',
    });
  });

  it('returns an error when both current and previous-day requests fail', () => {
    let receivedError: HttpErrorResponse | undefined;

    service.getQuote('USD').subscribe({
      next: () => fail('expected request to fail'),
      error: error => {
        receivedError = error;
      },
    });

    const currentRequest = httpController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );

    currentRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    const fallbackRequest = httpController.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/USD/${getPreviousDayDate()}/?format=json`
    );

    fallbackRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    expect(receivedError).toBeTruthy();
    expect(receivedError?.status).toBe(404);
  });

  function getPreviousDayDate(referenceDate: Date = new Date()): string {
    const utcDate = new Date(
      Date.UTC(referenceDate.getFullYear(), referenceDate.getMonth(), referenceDate.getDate())
    );

    utcDate.setUTCDate(utcDate.getUTCDate() - 1);

    return utcDate.toISOString().slice(0, 10);
  }
});
