import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { OffersHomePageComponent } from './offers-home-page.component';

const OFFERS_FIXTURE = [
  {
    id: 'offer-1001',
    offerNumber: 'OF/2026/03/000123',
    status: 'ISSUED',
    createdAt: '2026-03-07T09:14:00Z',
    updatedAt: '2026-03-09T08:32:00Z',
    validTo: '2026-03-21T22:59:59Z',
    agent: {
      fullName: 'Anna Kowalska',
      salesChannel: 'Agencja własna'
    },
    customer: {
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Michał',
          lastName: 'Wysocki'
        }
      },
      residenceAddress: {
        city: 'Kraków'
      }
    },
    vehicle: {
      make: 'Skoda',
      model: 'Octavia',
      productionYear: 2024,
      registration: {
        registrationNumber: 'KR7A921'
      },
      engine: {
        fuelType: 'PETROL'
      }
    },
    selectedVariantId: 'variant-optimum',
    selectedPaymentPlan: {
      totalPremium: {
        amount: 6244,
        currency: 'PLN'
      }
    },
    variants: [
      {
        id: 'variant-optimum',
        name: 'Optimum',
        totalPremium: {
          amount: 6244,
          currency: 'PLN'
        },
        paymentPlans: []
      }
    ]
  },
  {
    id: 'offer-1002',
    offerNumber: 'OF/2026/03/000124',
    status: 'CALCULATION',
    createdAt: '2026-03-08T10:10:00Z',
    updatedAt: '2026-03-09T10:48:00Z',
    validTo: '2026-03-23T22:59:59Z',
    agent: {
      fullName: 'Paweł Nowak',
      salesChannel: 'Dealer'
    },
    customer: {
      identity: {
        type: 'SOLE_PROPRIETOR',
        companyName: 'Różańska Detailing'
      },
      residenceAddress: {
        city: 'Warszawa'
      }
    },
    vehicle: {
      make: 'Toyota',
      model: 'Corolla Cross',
      productionYear: 2025,
      registration: {
        registrationNumber: 'WA8K331'
      },
      engine: {
        fuelType: 'HYBRID'
      }
    },
    selectedVariantId: 'variant-start',
    selectedPaymentPlan: {
      totalPremium: {
        amount: 5430,
        currency: 'PLN'
      }
    },
    variants: [
      {
        id: 'variant-start',
        name: 'Start',
        totalPremium: {
          amount: 5430,
          currency: 'PLN'
        },
        paymentPlans: []
      }
    ]
  }
];

const REFERENCE_DATA_FIXTURE = {
  offerStatuses: [
    { code: 'ISSUED', label: 'Oferta wystawiona' },
    { code: 'CALCULATION', label: 'Kalkulacja' },
    { code: 'DRAFT', label: 'Draft' }
  ],
  policyLines: [],
  salesChannels: [],
  vehicleUsages: [],
  vehicleFinancing: []
};

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: any;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance as any;

    flushInitialRequests();
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('defaults to PLN presentation without calling NBP', () => {
    expect(component.presentationCurrency()).toBe('PLN');
    expect(component.activeExchangeRate()).toBeNull();
    expect(component.getDisplayedPremium(OFFERS_FIXTURE[0])).toBe(6244);
    expect(component.summaryTiles()[2].value).toContain('486');
    expect(component.summaryTiles()[2].value).toContain('zł');
    expect(component.exchangeRateRowState()).toBeNull();

    component.onPresentationCurrencyChange('PLN');

    httpMock.expectNone((request) => request.url.startsWith('https://api.nbp.pl/api/exchangerates/rates/A/'));
  });

  it('converts visible premiums to EUR using the current NBP rate', () => {
    component.onPresentationCurrencyChange('EUR');

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    expect(request.request.method).toBe('GET');
    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 4
        }
      ]
    });
    fixture.detectChanges();

    expect(component.presentationCurrency()).toBe('EUR');
    expect(component.activeExchangeRate().currencyCode).toBe('EUR');
    expect(component.getDisplayedPremium(OFFERS_FIXTURE[0])).toBe(1561);
    expect(component.summaryTiles()[2].value).toContain('122');
    expect(component.summaryTiles()[2].value).toContain('€');
    expect(component.exchangeRateRowState().variant).toBe('info');
    expect(normalizeText(fixture.nativeElement.textContent)).toContain(
      'Kurs NBP: 1 EUR = 4,0000 PLN (2026-04-17, tabela A, nr 074/A/NBP/2026)'
    );

    component.onPresentationCurrencyChange('EUR');

    httpMock.expectNone('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
  });

  it('converts visible premiums to USD using the current NBP rate', () => {
    component.onPresentationCurrencyChange('USD');

    const request = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    expect(request.request.method).toBe('GET');
    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 5
        }
      ]
    });
    fixture.detectChanges();

    expect(component.presentationCurrency()).toBe('USD');
    expect(component.activeExchangeRate().currencyCode).toBe('USD');
    expect(component.getDisplayedPremium(OFFERS_FIXTURE[0])).toBe(1249);
    expect(component.summaryTiles()[2].value).toContain('97');
    expect(component.summaryTiles()[2].value).toContain('$');
    expect(normalizeText(fixture.nativeElement.textContent)).toContain(
      'Kurs NBP: 1 USD = 5,0000 PLN (2026-04-17, tabela A, nr 074/A/NBP/2026)'
    );
  });

  it('restores PLN amounts without sending an NBP request when PLN is selected', () => {
    component.onPresentationCurrencyChange('EUR');

    const eurRequest = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    eurRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 4
        }
      ]
    });
    fixture.detectChanges();

    component.onPresentationCurrencyChange('PLN');
    fixture.detectChanges();

    expect(component.presentationCurrency()).toBe('PLN');
    expect(component.activeExchangeRate()).toBeNull();
    expect(component.getDisplayedPremium(OFFERS_FIXTURE[0])).toBe(6244);
    expect(component.exchangeRateRowState()).toBeNull();

    httpMock.expectNone('https://api.nbp.pl/api/exchangerates/rates/A/PLN/?format=json');
  });

  it('shows an error and preserves the last valid currency presentation when NBP fails', () => {
    component.onPresentationCurrencyChange('EUR');

    const eurRequest = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    eurRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 4
        }
      ]
    });
    fixture.detectChanges();

    component.onPresentationCurrencyChange('USD');

    const usdRequest = httpMock.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    usdRequest.flush('NBP unavailable', { status: 503, statusText: 'Service Unavailable' });
    fixture.detectChanges();

    expect(component.presentationCurrency()).toBe('EUR');
    expect(component.activeExchangeRate().currencyCode).toBe('EUR');
    expect(component.getDisplayedPremium(OFFERS_FIXTURE[0])).toBe(1561);
    expect(component.currencyChangeError()).toBe('Nie udało się pobrać aktualnego kursu NBP. Wyświetlane wartości pozostają bez zmian.');
    expect(component.exchangeRateRowState().variant).toBe('error');
    expect(normalizeText(fixture.nativeElement.textContent)).toContain(
      'Nie udało się pobrać aktualnego kursu NBP. Wyświetlane wartości pozostają bez zmian.'
    );
  });

  function flushInitialRequests(): void {
    httpMock.expectOne('mock/offers.json').flush(OFFERS_FIXTURE);
    httpMock.expectOne('mock/reference-data.json').flush(REFERENCE_DATA_FIXTURE);
  }

  function normalizeText(text: string): string {
    return text.replace(/\s+/g, ' ').trim();
  }
});
