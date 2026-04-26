import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { Offer, ReferenceData } from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';
import { of } from 'rxjs';

registerLocaleData(localePl);

const TEST_OFFERS: Offer[] = [
  {
    id: 'offer-motor-1',
    product: 'MOTOR',
    offerNumber: 'OFR/MOTOR/001',
    status: 'ISSUED',
    createdAt: '2026-04-10T08:00:00.000Z',
    updatedAt: '2026-04-10T10:00:00.000Z',
    validTo: '2026-05-10T00:00:00.000Z',
    salesChannel: 'AGENCY' as never,
    agent: {
      salesChannel: 'Agent',
      fullName: 'Jan Agent'
    } as never,
    customer: {
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Anna',
          lastName: 'Nowak'
        }
      },
      residenceAddress: {
        city: 'Warszawa'
      }
    } as never,
    vehicle: {
      make: 'Toyota',
      model: 'Corolla',
      productionYear: 2022,
      engine: {
        fuelType: 'BENZYNA'
      },
      registration: {
        registrationNumber: 'WA12345'
      }
    } as never,
    insuredObject: {} as never,
    variants: [
      {
        id: 'variant-motor',
        name: 'Standard',
        totalPremium: {
          amount: 1001,
          currency: 'PLN'
        }
      } as never
    ],
    selectedVariantId: 'variant-motor',
    selectedPaymentPlan: {
      totalPremium: {
        amount: 1001,
        currency: 'PLN'
      }
    } as never
  } as Offer,
  {
    id: 'offer-crop-1',
    product: 'CROP',
    offerNumber: 'OFR/CROP/001',
    status: 'CALCULATION',
    createdAt: '2026-04-11T08:00:00.000Z',
    updatedAt: '2026-04-11T10:00:00.000Z',
    validTo: '2026-05-11T00:00:00.000Z',
    salesChannel: 'AGENCY' as never,
    agent: {
      salesChannel: 'Agent',
      fullName: 'Maria Agent'
    } as never,
    customer: {
      identity: {
        type: 'LEGAL_ENTITY',
        companyName: 'Agro Sp. z o.o.'
      },
      residenceAddress: {
        city: 'Płock'
      }
    } as never,
    vehicle: {
      make: 'AGRO',
      model: 'CROP',
      productionYear: 2020,
      engine: {
        fuelType: 'DIESEL'
      },
      registration: {
        registrationNumber: 'PL12345'
      }
    } as never,
    insuredObject: {} as never,
    variants: [
      {
        id: 'variant-crop',
        name: 'Agro',
        totalPremium: {
          amount: 755,
          currency: 'PLN'
        }
      } as never
    ],
    selectedVariantId: 'variant-crop',
    cropData: {
      crops: [
        {
          parcels: [{}, {}]
        }
      ]
    }
  } as Offer
];

const TEST_REFERENCE_DATA: ReferenceData = {
  offerStatuses: [
    { code: 'ISSUED', label: 'Oferta wystawiona' },
    { code: 'CALCULATION', label: 'Kalkulacja' }
  ],
  policyLines: [],
  salesChannels: [],
  vehicleUsages: [],
  vehicleFinancing: []
};

describe('OffersHomePageComponent', () => {
  let fixture: ReturnType<typeof TestBed.createComponent<OffersHomePageComponent>>;
  let component: OffersHomePageComponent;
  let httpTestingController: HttpTestingController;

  const offersRepositoryStub = {
    getOffers: () => of(TEST_OFFERS)
  };

  const referenceDataRepositoryStub = {
    getReferenceData: () => of(TEST_REFERENCE_DATA)
  };

  const salesFlowRuntimeRepositoryStub = {
    runtimeOffers: signal<Offer[]>([]),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer').and.callFake((offer: Offer) => offer),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        ExchangeRatesRepository,
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: salesFlowRuntimeRepositoryStub }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should render premiums in PLN by default', () => {
    const premiumTexts = getPremiumTexts();

    expect((component as any).getDisplayedPremiumCurrency()).toBe('PLN');
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[0])).toBe(1001);
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[1])).toBe(755);
    expect(premiumTexts[0]).toContain('zł');
    expect(premiumTexts[1]).toContain('zł');
    expect(fixture.nativeElement.querySelector('.results-meta__rate')).toBeNull();
    expect(fixture.nativeElement.querySelector('.results-meta__error')).toBeNull();
  });

  it('should convert visible offers to EUR, round to whole numbers and display the applied rate', () => {
    (component as any).onPresentationCurrencyChange('EUR');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 4
        }
      ]
    });

    fixture.detectChanges();

    const premiumTexts = getPremiumTexts();
    const rateText = normalizeText(fixture.nativeElement.querySelector('.results-meta__rate'));

    expect((component as any).presentationCurrency()).toBe('EUR');
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[0])).toBe(250);
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[1])).toBe(189);
    expect(premiumTexts[0]).toContain('250');
    expect(premiumTexts[0]).toContain('€');
    expect(premiumTexts[1]).toContain('189');
    expect(premiumTexts[1]).toContain('€');
    expect(rateText).toContain('Kurs NBP EUR');
    expect(rateText).toContain('1 EUR = 4,0000 PLN');
  });

  it('should convert visible offers to USD with the current USD rate', () => {
    (component as any).onPresentationCurrencyChange('USD');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    expect(request.request.method).toBe('GET');

    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 5
        }
      ]
    });

    fixture.detectChanges();

    const premiumTexts = getPremiumTexts();
    const rateText = normalizeText(fixture.nativeElement.querySelector('.results-meta__rate'));

    expect((component as any).presentationCurrency()).toBe('USD');
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[0])).toBe(200);
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[1])).toBe(151);
    expect(premiumTexts[0]).toContain('200');
    expect(premiumTexts[0]).toContain('$');
    expect(premiumTexts[1]).toContain('151');
    expect(premiumTexts[1]).toContain('$');
    expect(rateText).toContain('Kurs NBP USD');
    expect(rateText).toContain('1 USD = 5,0000 PLN');
  });

  it('should keep the last valid currency and amounts when the rate request fails', () => {
    (component as any).onPresentationCurrencyChange('EUR');

    const eurRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json');
    eurRequest.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: [
        {
          effectiveDate: '2026-04-17',
          mid: 4
        }
      ]
    });

    fixture.detectChanges();

    (component as any).onPresentationCurrencyChange('USD');

    const usdRequest = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json');
    usdRequest.flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    fixture.detectChanges();

    const premiumTexts = getPremiumTexts();
    const rateText = normalizeText(fixture.nativeElement.querySelector('.results-meta__rate'));
    const errorText = normalizeText(fixture.nativeElement.querySelector('.results-meta__error'));

    expect((component as any).presentationCurrency()).toBe('EUR');
    expect((component as any).presentationCurrencySelection()).toBe('EUR');
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[0])).toBe(250);
    expect((component as any).getDisplayedPremiumAmount(TEST_OFFERS[1])).toBe(189);
    expect(premiumTexts[0]).toContain('€');
    expect(premiumTexts[1]).toContain('€');
    expect(rateText).toContain('Kurs NBP EUR');
    expect(errorText).toContain('Nie udało się pobrać kursu waluty');
  });

  function getPremiumTexts(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('.premium-box strong')).map((element) => normalizeText(element));
  }

  function normalizeText(element: Element | null): string {
    return element?.textContent?.replace(/\s+/g, ' ').trim() ?? '';
  }
});
