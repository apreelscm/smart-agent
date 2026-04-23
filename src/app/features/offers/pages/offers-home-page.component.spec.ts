import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { ComponentFixture } from '@angular/core/testing';
import { Offer, OfferStatus, ReferenceData } from '../../../core/models';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;
  let httpTestingController: HttpTestingController;

  const referenceDataFixture: ReferenceData = {
    offerStatuses: [
      { code: 'DRAFT', label: 'Draft' },
      { code: 'CALCULATION', label: 'Kalkulacja' },
      { code: 'ISSUED', label: 'Oferta wystawiona' }
    ],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  const offersFixture: Offer[] = [
    createOffer({
      id: 'offer-1001',
      offerNumber: 'OF/2026/03/000123',
      status: 'ISSUED',
      createdAt: '2026-03-07T09:14:00Z',
      updatedAt: '2026-03-09T08:32:00Z',
      validTo: '2026-03-21T22:59:59Z',
      selectedPaymentPlanAmount: 6244,
      variantAmount: 7181
    }),
    createOffer({
      id: 'offer-1002',
      offerNumber: 'OF/2026/03/000124',
      status: 'CALCULATION',
      createdAt: '2026-03-08T10:10:00Z',
      updatedAt: '2026-03-09T10:48:00Z',
      validTo: '2026-03-23T22:59:59Z',
      variantAmount: 7181
    })
  ];

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([]), provideNoopAnimations()]
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
    flushInitialRequests();
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
    httpTestingController.verify();
  });

  it('should convert visible premiums to EUR and show the applied rate', () => {
    component['onPresentationCurrencyChange']('EUR');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');
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

    fixture.detectChanges();

    const firstPremium = getOfferPremiumText('OF/2026/03/000123');
    const secondPremium = getOfferPremiumText('OF/2026/03/000124');
    const rateText = normalizeText(fixture.nativeElement.querySelector('.results-meta__rate')?.textContent ?? '');

    expect(firstPremium).toContain('€');
    expect(firstPremium).toMatch(/1(?:\s|\u00A0)454/);
    expect(secondPremium).toContain('€');
    expect(secondPremium).toMatch(/1(?:\s|\u00A0)672/);
    expect(rateText).toContain('1 EUR = 4,2941 PLN');
    expect(rateText).toContain('17.04.2026');
  });

  it('should convert visible premiums to USD and show the applied rate', () => {
    component['onPresentationCurrencyChange']('USD');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json');
    request.flush({
      table: 'A',
      currency: 'dolar amerykański',
      code: 'USD',
      rates: [
        {
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          mid: 3.85
        }
      ]
    });

    fixture.detectChanges();

    const premium = getOfferPremiumText('OF/2026/03/000123');
    const rateText = normalizeText(fixture.nativeElement.querySelector('.results-meta__rate')?.textContent ?? '');

    expect(premium).toMatch(/1(?:\s|\u00A0)622/);
    expect(premium).toMatch(/(\$|USD)/);
    expect(rateText).toContain('1 USD = 3,8500 PLN');
    expect(rateText).toContain('17.04.2026');
  });

  it('should keep PLN premiums and show an error when the rate response is invalid', () => {
    component['onPresentationCurrencyChange']('EUR');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');
    request.flush({
      table: 'A',
      currency: 'euro',
      code: 'EUR',
      rates: []
    });

    fixture.detectChanges();

    const premium = getOfferPremiumText('OF/2026/03/000123');
    const errorText = normalizeText(fixture.nativeElement.querySelector('.currency-error-banner')?.textContent ?? '');

    expect(premium).toContain('zł');
    expect(errorText).toContain('Nie udało się pobrać kursu EUR. Wyświetlamy składki w PLN.');
    expect(fixture.nativeElement.querySelector('.results-meta__rate')).toBeNull();
  });

  it('should not mutate source offers after successful conversion', () => {
    component['onPresentationCurrencyChange']('EUR');

    const request = httpTestingController.expectOne('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json');
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

    fixture.detectChanges();

    const sourceOffer = component['offers']().find((offer) => offer.id === 'offer-1001');

    expect(sourceOffer?.selectedPaymentPlan?.totalPremium.amount).toBe(6244);
    expect(sourceOffer?.selectedPaymentPlan?.totalPremium.currency).toBe('PLN');
    expect(sourceOffer?.variants[0]?.totalPremium.amount).toBe(7181);
    expect(sourceOffer?.variants[0]?.totalPremium.currency).toBe('PLN');
  });

  function flushInitialRequests(): void {
    const offersRequest = httpTestingController.expectOne('mock/offers.json');
    offersRequest.flush(offersFixture);

    const referenceDataRequest = httpTestingController.expectOne('mock/reference-data.json');
    referenceDataRequest.flush(referenceDataFixture);
  }

  function getOfferPremiumText(offerNumber: string): string {
    const rows = Array.from(fixture.nativeElement.querySelectorAll('.offer-row')) as HTMLElement[];
    const row = rows.find((element) => normalizeText(element.textContent ?? '').includes(offerNumber));

    expect(row).withContext(`Expected offer row for ${offerNumber}`).toBeDefined();

    const premium = row?.querySelector('.premium-box strong');
    expect(premium).withContext(`Expected premium box for ${offerNumber}`).not.toBeNull();

    return normalizeText(premium?.textContent ?? '');
  }

  function normalizeText(value: string): string {
    return value.replace(/\s+/g, ' ').trim();
  }

  function createOffer(input: {
    id: string;
    offerNumber: string;
    status: OfferStatus;
    createdAt: string;
    updatedAt: string;
    validTo: string;
    variantAmount: number;
    selectedPaymentPlanAmount?: number;
  }): Offer {
    return {
      id: input.id,
      offerNumber: input.offerNumber,
      status: input.status,
      createdAt: input.createdAt,
      updatedAt: input.updatedAt,
      validTo: input.validTo,
      salesChannel: 'AGENCY',
      agent: {
        id: 'agent-1',
        fullName: 'Anna Kowalska',
        agencyCode: 'KRK-1204',
        salesChannel: 'Agencja własna'
      },
      customer: {
        id: `customer-${input.id}`,
        kind: 'NATURAL_PERSON',
        identity: {
          type: 'NATURAL_PERSON',
          personName: {
            firstName: 'Jan',
            lastName: 'Kowalski'
          },
          pesel: '90021212345',
          birthDate: '1990-02-12'
        },
        contact: {
          email: 'jan.kowalski@example.pl',
          phoneNumber: '+48 600 100 100'
        },
        residenceAddress: {
          street: 'Zakopiańska',
          buildingNumber: '88A',
          postalCode: '30-418',
          city: 'Kraków',
          countryCode: 'PL'
        },
        isVatPayer: false,
        isForeignClient: false
      },
      vehicle: {
        id: `vehicle-${input.id}`,
        make: 'Skoda',
        model: 'Octavia',
        version: 'Style 1.5 TSI DSG',
        productionYear: 2024,
        vin: `VIN-${input.id}`,
        bodyType: 'Kombi',
        usage: 'PRIVATE',
        financing: 'OWNED',
        seats: 5,
        annualMileageKm: 18000,
        marketValue: 138000,
        registration: {
          registrationNumber: `KR-${input.id}`,
          firstRegistrationDate: '2024-06-18',
          countryCode: 'PL'
        },
        engine: {
          fuelType: 'PETROL',
          displacementCc: 1498,
          powerKw: 110,
          powerHp: 150
        },
        grossVehicleWeightKg: 1910,
        specialUsages: []
      },
      insuredObject: {
        id: `insured-${input.id}`,
        type: 'VEHICLE',
        label: `Skoda Octavia ${input.id}`,
        vehicleId: `vehicle-${input.id}`,
        ownerCustomerId: `customer-${input.id}`,
        primaryDriverCustomerId: `customer-${input.id}`
      },
      selectedVariantId: `${input.id}-variant-1`,
      selectedPaymentPlan: input.selectedPaymentPlanAmount
        ? {
            frequency: 'ANNUAL',
            totalPremium: {
              amount: input.selectedPaymentPlanAmount,
              currency: 'PLN'
            },
            installments: []
          }
        : undefined,
      variants: [
        {
          id: `${input.id}-variant-1`,
          name: 'Optimum',
          rank: 1,
          badge: 'Polecany',
          totalPremium: {
            amount: input.variantAmount,
            currency: 'PLN'
          },
          monthlyPremium: {
            amount: Math.round((input.variantAmount / 12) * 100) / 100,
            currency: 'PLN'
          },
          paymentPlans: [],
          policyLines: [],
          selected: true,
          recommended: true
        }
      ]
    } as Offer;
  }
});
