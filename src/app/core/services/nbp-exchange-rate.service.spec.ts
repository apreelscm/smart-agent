import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { NbpExchangeRateService } from './nbp-exchange-rate.service';

describe('NbpExchangeRateService', () => {
  let service: NbpExchangeRateService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        NbpExchangeRateService,
      ],
    });

    service = TestBed.inject(NbpExchangeRateService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should return the latest rate when the latest endpoint succeeds', done => {
    service.getRate('USD').subscribe(rate => {
      expect(rate).toEqual({
        code: 'USD',
        rate: 3.85,
        effectiveDate: '2026-04-17',
      });
      done();
    });

    const request = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json',
    );
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 3.85,
        },
      ],
    });
  });

  it('should fall back to the previous calendar day when the latest request fails', done => {
    service.getRate('EUR').subscribe(rate => {
      expect(rate).toEqual({
        code: 'EUR',
        rate: 4.2941,
        effectiveDate: '2026-04-17',
      });
      done();
    });

    const latestRequest = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json',
    );
    latestRequest.flush('temporary error', { status: 500, statusText: 'Server Error' });

    const fallbackRequest = httpTestingController.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${getPreviousCalendarDay()}/?format=json`,
    );
    expect(fallbackRequest.request.method).toBe('GET');

    fallbackRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 4.2941,
        },
      ],
    });
  });

  it('should fall back to the previous calendar day when the latest payload is malformed', done => {
    service.getRate('USD').subscribe(rate => {
      expect(rate).toEqual({
        code: 'USD',
        rate: 3.91,
        effectiveDate: '2026-04-16',
      });
      done();
    });

    const latestRequest = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json',
    );
    latestRequest.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
        },
      ],
    });

    const fallbackRequest = httpTestingController.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/USD/${getPreviousCalendarDay()}/?format=json`,
    );
    fallbackRequest.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '073/A/NBP/2026',
          effectiveDate: '2026-04-16',
          mid: 3.91,
        },
      ],
    });
  });

  it('should propagate an error when both latest and fallback requests fail', done => {
    service.getRate('EUR').subscribe({
      next: () => fail('Expected an error when both requests fail'),
      error: error => {
        expect(error).toBeTruthy();
        done();
      },
    });

    const latestRequest = httpTestingController.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json',
    );
    latestRequest.flush('temporary error', { status: 500, statusText: 'Server Error' });

    const fallbackRequest = httpTestingController.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${getPreviousCalendarDay()}/?format=json`,
    );
    fallbackRequest.flush('not found', { status: 404, statusText: 'Not Found' });
  });

  function getPreviousCalendarDay(): string {
    const previousDay = new Date();
    previousDay.setDate(previousDay.getDate() - 1);

    const year = previousDay.getFullYear();
    const month = String(previousDay.getMonth() + 1).padStart(2, '0');
    const day = String(previousDay.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
});
