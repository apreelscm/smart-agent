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
      ],
    });

    service = TestBed.inject(NbpExchangeRateService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should request today EUR rate and map NBP response', () => {
    let actualRate: unknown;

    service.getTodayRate('EUR').subscribe(rate => {
      actualRate = rate;
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/today/?format=json');

    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2025',
          effectiveDate: '2025-04-17',
          mid: 4.2941,
        },
      ],
    });

    expect(actualRate).toEqual({
      tableType: 'A',
      currencyCode: 'EUR',
      currencyName: 'euro',
      effectiveDate: '2025-04-17',
      midRate: 4.2941,
      sourceTableNo: '074/A/NBP/2025',
      source: 'NBP',
    });
  });

  it('should surface an error when NBP response has no usable rate', () => {
    let actualError: Error | undefined;

    service.getTodayRate('USD').subscribe({
      error: error => {
        actualError = error;
      },
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/today/?format=json');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [],
    });

    expect(actualError?.message).toBe('Brak poprawnego kursu NBP dla USD.');
  });

  it('should map 404 response to missing today rate error', () => {
    let actualError: Error | undefined;

    service.getTodayRate('EUR').subscribe({
      error: error => {
        actualError = error;
      },
    });

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/today/?format=json');

    request.flush('', {
      status: 404,
      statusText: 'Not Found',
    });

    expect(actualError?.message).toBe('Brak dzisiejszego kursu NBP dla EUR.');
  });
});
