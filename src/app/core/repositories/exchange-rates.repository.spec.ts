import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { firstValueFrom } from 'rxjs';
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

  it('maps NBP table A response to exchange snapshot', async () => {
    const snapshotPromise = firstValueFrom(repository.getExchangeRates());

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    expect(request.request.method).toBe('GET');
    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [
          { currency: 'euro', code: 'EUR', mid: 4.2941 },
          { currency: 'dolar amerykański', code: 'USD', mid: 3.85 }
        ]
      }
    ]);

    await expectAsync(snapshotPromise).toBeResolvedTo({
      effectiveDate: '2026-04-17',
      rates: {
        EUR: 4.2941,
        USD: 3.85
      }
    });
  });

  it('errors when EUR or USD are missing', async () => {
    const snapshotPromise = firstValueFrom(repository.getExchangeRates());

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/tables/A/?format=json');
    request.flush([
      {
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        rates: [{ currency: 'euro', code: 'EUR', mid: 4.2941 }]
      }
    ]);

    await expectAsync(snapshotPromise).toBeRejected();
  });
});
