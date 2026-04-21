import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { Offer } from '../../../core/models';
import { EXCHANGE_RATES_API_URL } from '../../../core/repositories/exchange-rates.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let httpTestingController: HttpTestingController;
  let salesFlowRuntimeRepositoryStub: {
    runtimeOffers: ReturnType<typeof signal<Offer[]>>;
    saveDraftOffer: jasmine.Spy;
    promoteOfferToPolicy: jasmine.Spy;
  };

  const offersFixture: Offer[] = [
    createOffer({
      id: 'offer-1',
      offerNumber: 'OFR/2026/0001',
      status: 'ISSUED',
      createdAt: '2026-04-12T09:00:00.000Z',
      updatedAt: '2026-04-12T10:30:00.000Z',
      validTo: '2026-04-30T00:00:00.000Z',
      selectedPaymentPlan: { totalPremium: { amount: 1200, currency: 'PLN' } } as never,
      variants: [{ id: 'variant-1', name: 'Standard', totalPremium: { amount: 1200, currency: 'PLN' } }] as never
    }),
    createOffer({
      id: 'offer-2',
      offerNumber: 'OFR/2026/0002',
      status: 'DRAFT',
      createdAt: '2026-04-10T08:00:00.000Z',
      updatedAt: '2026-04-10T09:15:00.000Z',
      validTo: undefined,
      selectedPaymentPlan: undefined,
      variants: [{ id: 'variant-2', name: 'Premium', totalPremium: { amount: 2400, currency: 'PLN' } }] as never
    })
  ];

  const referenceDataFixture = {
    offerStatuses: [
      { code: 'DRAFT', label: 'Draft' },
      { code: 'CALCULATION', label: 'Kalkulacja' },
      { code: 'ISSUED', label: 'Oferta wystawiona' },
      { code: 'ACCEPTED', label: 'Oferta zaakceptowana' },
      { code: 'POLICY', label: 'Polisa' },
      { code: 'REJECTED', label: 'Oferta odrzucona' },
      { code: 'CANCELED', label: 'Oferta anulowana' }
    ],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  const exchangeRatesFixture = [
    {
      table: 'A',
      no: '079/A/NBP/2026',
      effectiveDate: '2026-04-20',
      rates: [
        { currency: 'euro', code: 'EUR', mid: 4.0 },
        { currency: 'dolar amerykański', code: 'USD', mid: 3.5 }
      ]
    }
  ];

  beforeEach(async () => {
    salesFlowRuntimeRepositoryStub = {
      runtimeOffers: signal<Offer[]>([]),
      saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
      promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
    };

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        {
          provide: SalesFlowRuntimeRepository,
          useValue: salesFlowRuntimeRepositoryStub
        }
      ]
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(OffersHomePageComponent);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should render the offers list in PLN by default and keep existing list content', () => {
    renderWithSuccessfulRates();

    const pageText = normalizeText(fixture.nativeElement as HTMLElement);
    const premiumValues = getPremiumValues();

    expect(pageText).toContain('OFR/2026/0001');
    expect(pageText).toContain('Jan Kowalski');
    expect(pageText).toContain('Toyota Corolla');
    expect(premiumValues).toContain('1 200 zł');
    expect(premiumValues).toContain('2 400 zł');
    expect(pageText).toContain('150 zł');
    expect(getCurrencyButton('PLN').getAttribute('aria-pressed')).toBe('true');
    expect(fixture.nativeElement.querySelector('.currency-feedback--rate')).toBeNull();
  });

  it('should switch row and summary amounts to EUR and show the applied rate without write operations', () => {
    renderWithSuccessfulRates();

    getCurrencyButton('EUR').click();
    fixture.detectChanges();

    const pageText = normalizeText(fixture.nativeElement as HTMLElement);
    const premiumValues = getPremiumValues();
    const rateLabel = fixture.nativeElement.querySelector('.currency-feedback--rate') as HTMLElement;

    expect(premiumValues).toContain('300,00 €');
    expect(premiumValues).toContain('600,00 €');
    expect(pageText).toContain('37,50 €');
    expect(normalizeText(rateLabel)).toContain('kurs 4,00 PLN/EUR z 20.04.2026');
    expect(salesFlowRuntimeRepositoryStub.saveDraftOffer).not.toHaveBeenCalled();
    expect(salesFlowRuntimeRepositoryStub.promoteOfferToPolicy).not.toHaveBeenCalled();
  });

  it('should switch row and summary amounts to USD and show the applied rate', () => {
    renderWithSuccessfulRates();

    getCurrencyButton('USD').click();
    fixture.detectChanges();

    const pageText = normalizeText(fixture.nativeElement as HTMLElement);
    const premiumValues = getPremiumValues();
    const rateLabel = fixture.nativeElement.querySelector('.currency-feedback--rate') as HTMLElement;

    expect(premiumValues).toContain('342,86 $');
    expect(premiumValues).toContain('685,71 $');
    expect(pageText).toContain('42,86 $');
    expect(normalizeText(rateLabel)).toContain('kurs 3,50 PLN/USD z 20.04.2026');
  });

  it('should keep PLN usable, disable EUR and USD, and show an explanatory message when NBP is unavailable', () => {
    renderWithRateFailure();

    const eurButton = getCurrencyButton('EUR');
    const usdButton = getCurrencyButton('USD');
    const warningMessage = fixture.nativeElement.querySelector('.currency-feedback--warning') as HTMLElement;
    const premiumValues = getPremiumValues();

    expect(eurButton.disabled).toBeTrue();
    expect(usdButton.disabled).toBeTrue();
    expect(premiumValues).toContain('1 200 zł');
    expect(premiumValues).toContain('2 400 zł');
    expect(normalizeText(warningMessage)).toContain('Nie udało się pobrać kursów NBP. Lista pozostaje w PLN');
    expect(getCurrencyButton('PLN').getAttribute('aria-pressed')).toBe('true');
  });

  function renderWithSuccessfulRates(): void {
    fixture.detectChanges();

    httpTestingController.expectOne('mock/offers.json').flush(offersFixture);
    httpTestingController.expectOne('mock/reference-data.json').flush(referenceDataFixture);
    httpTestingController.expectOne(EXCHANGE_RATES_API_URL).flush(exchangeRatesFixture);

    fixture.detectChanges();
  }

  function renderWithRateFailure(): void {
    fixture.detectChanges();

    httpTestingController.expectOne('mock/offers.json').flush(offersFixture);
    httpTestingController.expectOne('mock/reference-data.json').flush(referenceDataFixture);
    httpTestingController.expectOne(EXCHANGE_RATES_API_URL).flush('NBP unavailable', {
      status: 503,
      statusText: 'Service Unavailable'
    });

    fixture.detectChanges();
  }

  function getCurrencyButton(currency: 'PLN' | 'EUR' | 'USD'): HTMLButtonElement {
    const buttons = Array.from(fixture.nativeElement.querySelectorAll<HTMLButtonElement>('.currency-switcher__button'));
    const button = buttons.find((candidate) => normalizeText(candidate) === currency);

    if (!button) {
      throw new Error(`Currency button ${currency} not found.`);
    }

    return button;
  }

  function getPremiumValues(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll<HTMLElement>('.premium-box strong')).map((element) => normalizeText(element));
  }

  function normalizeText(element: HTMLElement): string {
    return (element.textContent ?? '').replace(/\s+/g, ' ').trim();
  }
});

function createOffer(overrides: Partial<Offer> = {}): Offer {
  return {
    id: 'offer-base',
    product: 'MOTOR',
    offerNumber: 'OFR/BASE/0000',
    status: 'ISSUED',
    createdAt: '2026-04-01T08:00:00.000Z',
    updatedAt: '2026-04-01T09:00:00.000Z',
    validTo: '2026-04-30T00:00:00.000Z',
    salesChannel: 'AGENCY' as never,
    agent: {
      salesChannel: 'AGENCY',
      fullName: 'Jan Agent'
    } as never,
    customer: {
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Jan',
          lastName: 'Kowalski'
        }
      },
      residenceAddress: {
        city: 'Warszawa'
      }
    } as never,
    vehicle: {
      make: 'Toyota',
      model: 'Corolla',
      productionYear: 2023,
      engine: {
        fuelType: 'Benzyna'
      },
      registration: {
        registrationNumber: 'WX 12345'
      }
    } as never,
    insuredObject: {} as never,
    variants: [
      {
        id: 'variant-base',
        name: 'Standard',
        totalPremium: {
          amount: 1200,
          currency: 'PLN'
        }
      }
    ] as never,
    selectedVariantId: 'variant-base',
    selectedPaymentPlan: {
      totalPremium: {
        amount: 1200,
        currency: 'PLN'
      }
    } as never,
    ...overrides
  };
}
