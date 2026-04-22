import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { firstValueFrom } from 'rxjs';
import { CurrencyRateQuote, CurrencyRatesRepository } from './currency-rates.repository';

describe('CurrencyRatesRepository', () => {
  let repository: CurrencyRatesRepository;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CurrencyRatesRepository, provideHttpClient(), provideHttpClientTesting()]
    });

    repository = TestBed.inject(CurrencyRatesRepository);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('maps a successful NBP response to a service quote', async () => {
    const quotePromise = firstValueFrom(repository.getQuote('EUR'));

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');
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

    await expectAsync(quotePromise).toBeResolvedTo({
      currency: 'EUR',
      rate: 4.2941,
      source: 'service',
      effectiveDate: '2026-04-17'
    } as CurrencyRateQuote);
  });

  it('falls back when the payload does not contain a usable mid value', async () => {
    const quotePromise = firstValueFrom(repository.getQuote('USD'));

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 0
        }
      ]
    });

    await expectAsync(quotePromise).toBeResolvedTo({
      currency: 'USD',
      rate: 3.6,
      source: 'fallback',
      effectiveDate: '2026-04-17'
    } as CurrencyRateQuote);
  });

  it('falls back when the HTTP call fails', async () => {
    const quotePromise = firstValueFrom(repository.getQuote('EUR'));

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    await expectAsync(quotePromise).toBeResolvedTo({
      currency: 'EUR',
      rate: 4.4,
      source: 'fallback'
    } as CurrencyRateQuote);
  });
});
