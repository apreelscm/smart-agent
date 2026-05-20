import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import {
  ExchangeRateResult,
  ExchangeRateService,
  PresentationCurrency,
} from './exchange-rate.service';

describe('ExchangeRateService', () => {
  let service: ExchangeRateService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExchangeRateService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ExchangeRateService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    jasmine.clock().uninstall();
  });

  it('should parse current NBP rates for USD and EUR', () => {
    const received: Partial<Record<PresentationCurrency, ExchangeRateResult>> = {};

    service.getRate('USD').subscribe(result => (received.USD = result));
    service.getRate('EUR').subscribe(result => (received.EUR = result));

    const usdRequest = httpMock.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );
    usdRequest.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [{ no: '101/A/NBP/2026', effectiveDate: '2026-04-18', mid: 3.8765 }],
    });

    const eurRequest = httpMock.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json'
    );
    eurRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [{ no: '101/A/NBP/2026', effectiveDate: '2026-04-18', mid: 4.2941 }],
    });

    expect(received.USD).toEqual({
      currency: 'USD',
      rate: 3.8765,
      effectiveDate: '2026-04-18',
      sourceTable: 'A',
    });

    expect(received.EUR).toEqual({
      currency: 'EUR',
      rate: 4.2941,
      effectiveDate: '2026-04-18',
      sourceTable: 'A',
    });
  });

  it('should retry with yesterday rate when current request fails', () => {
    jasmine.clock().install();
    jasmine.clock().mockDate(new Date('2026-04-18T12:00:00.000Z'));

    let received: ExchangeRateResult | undefined;

    service.getRate('USD').subscribe(result => (received = result));

    const currentRequest = httpMock.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json'
    );
    currentRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    const fallbackRequest = httpMock.expectOne(
      'https://api.nbp.pl/api/exchangerates/rates/A/USD/2026-04-17/?format=json'
    );
    fallbackRequest.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [{ no: '100/A/NBP/2026', effectiveDate: '2026-04-17', mid: 3.8123 }],
    });

    expect(received).toEqual({
      currency: 'USD',
      rate: 3.8123,
      effectiveDate: '2026-04-17',
      sourceTable: 'A',
    });
  });

  it('should return PLN rate without any HTTP request', () => {
    let received: ExchangeRateResult | undefined;

    service.getRate('PLN').subscribe(result => (received = result));

    expect(received).toEqual({
      currency: 'PLN',
      rate: 1,
      effectiveDate: null,
      sourceTable: null,
    });
    expect(httpMock.match(() => true).length).toBe(0);
  });
});
