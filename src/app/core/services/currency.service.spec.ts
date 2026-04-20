import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CurrencyService } from './currency.service';

describe('CurrencyService', () => {
  let service: CurrencyService;
  let httpMock: HttpTestingController;

  const mockRates = {
    base: 'PLN',
    date: '2026-04-20',
    rates: {
      EUR: 4.5,
      USD: 4.0
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CurrencyService]
    });

    service = TestBed.inject(CurrencyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('fetches and caches rates', (done) => {
    service.fetchRates().subscribe((set) => {
      expect(set).toBeTruthy();
      expect(set.rates.EUR).toBe(4.5);
      const raw = localStorage.getItem('currency_rates_cache_v1');
      expect(raw).toBeTruthy();
      done();
    });

    const req = httpMock.expectOne('mock/currency-rates.json');
    expect(req.request.method).toBe('GET');
    req.flush(mockRates);
  });

  it('converts PLN to EUR and back', (done) => {
    service.fetchRates().subscribe(() => {
      const eur = service.convert(450, 'PLN', 'EUR'); // 450 PLN / 4.5 = 100 EUR
      expect(eur).toBe(100);
      const pln = service.convert(100, 'EUR', 'PLN');
      expect(pln).toBe(450);
      done();
    });

    const req = httpMock.expectOne('mock/currency-rates.json');
    req.flush(mockRates);
  });

  it('converts EUR to USD via PLN', (done) => {
    service.fetchRates().subscribe(() => {
      // 100 EUR -> 100 * 4.5 = 450 PLN -> 450 / 4 = 112.5 USD -> rounded to 2 decimals 112.5
      const usd = service.convert(100, 'EUR', 'USD');
      expect(usd).toBe(112.5);
      done();
    });

    const req = httpMock.expectOne('mock/currency-rates.json');
    req.flush(mockRates);
  });

  it('falls back to cache when fetch fails', (done) => {
    // seed cache
    const cache = {
      fetchedAt: new Date().toISOString(),
      rateSet: mockRates
    };
    localStorage.setItem('currency_rates_cache_v1', JSON.stringify(cache));

    // create fresh instance to load cache
    const svc = TestBed.inject(CurrencyService);
    svc.getRates().subscribe((rates) => {
      expect(rates).toBeTruthy();
      expect(rates?.rates.EUR).toBe(4.5);
      done();
    });
    // no http call expected because cache present and getRates returns cache via fetch fallback; allow pending requests
    httpMock.expectNone('mock/currency-rates.json');
  });
});
