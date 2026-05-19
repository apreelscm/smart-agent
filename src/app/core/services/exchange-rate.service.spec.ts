import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ExchangeRateService } from './exchange-rate.service';

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
  });

  it('should fetch the current NBP rate and normalize the response', () => {
    let actual: any;

    service.getRate('USD').subscribe(rate => {
      actual = rate;
    });

    const req = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    expect(req.request.method).toBe('GET');

    req.flush({
      table: 'A',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 3.85,
        },
      ],
    });

    expect(actual).toEqual({
      currency: 'USD',
      code: 'USD',
      table: 'A',
      no: '074/A/NBP/2026',
      effectiveDate: '2026-04-17',
      mid: 3.85,
    });
  });

  it('should fall back to the previous calendar day when the current request fails', () => {
    let actual: any;

    service.getRate('EUR').subscribe(rate => {
      actual = rate;
    });

    const currentReq = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    currentReq.flush('NBP unavailable', { status: 500, statusText: 'Server Error' });

    const previousDate = new Date();
    previousDate.setDate(previousDate.getDate() - 1);

    const expectedDate = `${previousDate.getFullYear()}-${String(previousDate.getMonth() + 1).padStart(2, '0')}-${String(previousDate.getDate()).padStart(2, '0')}`;
    const fallbackReq = httpMock.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${expectedDate}/?format=json`
    );

    expect(fallbackReq.request.method).toBe('GET');

    fallbackReq.flush({
      table: 'A',
      code: 'EUR',
      rates: [
        {
          no: '073/A/NBP/2026',
          effectiveDate: '2026-04-16',
          mid: 4.2941,
        },
      ],
    });

    expect(actual).toEqual({
      currency: 'EUR',
      code: 'EUR',
      table: 'A',
      no: '073/A/NBP/2026',
      effectiveDate: '2026-04-16',
      mid: 4.2941,
    });
  });

  it('should surface an error when both current and fallback requests fail', () => {
    let receivedError: any;

    service.getRate('USD').subscribe({
      next: () => fail('Expected request to fail'),
      error: error => {
        receivedError = error;
      },
    });

    const currentReq = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    currentReq.flush('NBP unavailable', { status: 500, statusText: 'Server Error' });

    const previousDate = new Date();
    previousDate.setDate(previousDate.getDate() - 1);

    const expectedDate = `${previousDate.getFullYear()}-${String(previousDate.getMonth() + 1).padStart(2, '0')}-${String(previousDate.getDate()).padStart(2, '0')}`;
    const fallbackReq = httpMock.expectOne(
      `https://api.nbp.pl/api/exchangerates/rates/A/USD/${expectedDate}/?format=json`
    );

    fallbackReq.flush('NBP still unavailable', { status: 503, statusText: 'Service Unavailable' });

    expect(receivedError.status).toBe(503);
  });
});
