import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { NbpExchangeRateError, NbpExchangeRateService } from './nbp-exchange-rate.service';

describe('NbpExchangeRateService', () => {
  let service: NbpExchangeRateService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    jasmine.clock().install();
    jasmine.clock().mockDate(new Date('2026-04-17T12:00:00'));

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(NbpExchangeRateService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
    jasmine.clock().uninstall();
  });

  it('returns the latest quote when the primary endpoint succeeds', () => {
    let actualQuote: ReturnType<NbpExchangeRateService['getLatestOrPreviousRate']> extends infer _ ? unknown : never;

    service.getLatestOrPreviousRate('USD').subscribe(quote => {
      actualQuote = quote;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
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

    expect(actualQuote).toEqual({
      table: 'A',
      currencyCode: 'USD',
      tableNo: '074/A/NBP/2026',
      effectiveDate: '2026-04-17',
      mid: 3.85,
    });
  });

  it('falls back to the previous-day endpoint when the latest quote is unavailable', () => {
    let actualMid = 0;
    let actualEffectiveDate = '';

    service.getLatestOrPreviousRate('EUR').subscribe(quote => {
      actualMid = quote.mid;
      actualEffectiveDate = quote.effectiveDate;
    });

    const latestRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    latestRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    const previousDayRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/2026-04-16/?format=json');
    expect(previousDayRequest.request.method).toBe('GET');

    previousDayRequest.flush({
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

    expect(actualMid).toBe(4.2941);
    expect(actualEffectiveDate).toBe('2026-04-16');
  });

  it('treats an invalid payload as a failure and retries with the previous-day quote', () => {
    let actualTableNo = '';

    service.getLatestOrPreviousRate('USD').subscribe(quote => {
      actualTableNo = quote.tableNo;
    });

    const latestRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    latestRequest.flush({
      table: 'A',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
        },
      ],
    });

    const previousDayRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/2026-04-16/?format=json');
    previousDayRequest.flush({
      table: 'A',
      code: 'USD',
      rates: [
        {
          no: '073/A/NBP/2026',
          effectiveDate: '2026-04-16',
          mid: 3.81,
        },
      ],
    });

    expect(actualTableNo).toBe('073/A/NBP/2026');
  });

  it('surfaces a typed error when both attempts fail', () => {
    let actualError: unknown = null;

    service.getLatestOrPreviousRate('EUR').subscribe({
      next: () => fail('expected request to fail'),
      error: error => {
        actualError = error;
      },
    });

    const latestRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    latestRequest.flush('Server error', { status: 500, statusText: 'Server Error' });

    const previousDayRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/2026-04-16/?format=json');
    previousDayRequest.flush('Not Found', { status: 404, statusText: 'Not Found' });

    expect(actualError instanceof NbpExchangeRateError).toBeTrue();
    expect((actualError as NbpExchangeRateError).currency).toBe('EUR');
    expect((actualError as NbpExchangeRateError).reason).toBe('lookup-failed');
  });
});
