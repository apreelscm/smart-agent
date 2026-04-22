import { signal } from '@angular/core';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer, ReferenceData } from '../../../core/models';
import { CurrencyRateQuote, CurrencyRatesRepository } from '../../../core/repositories/currency-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

const mockReferenceData: ReferenceData = {
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

const motorOffer = createOffer({
  id: 'motor-offer',
  offerNumber: 'OFR/MOTOR/001',
  createdAt: '2026-04-17T10:00:00.000Z',
  updatedAt: '2026-04-17T10:00:00.000Z',
  validTo: '2026-07-01T00:00:00.000Z',
  premiumAmount: 1200,
  selectedPaymentPlanAmount: 1200,
  product: 'MOTOR'
});

const cropOffer = createOffer({
  id: 'crop-offer',
  offerNumber: 'OFR/CROP/002',
  createdAt: '2026-04-16T10:00:00.000Z',
  updatedAt: '2026-04-16T10:00:00.000Z',
  validTo: '2026-06-15T00:00:00.000Z',
  premiumAmount: 2400,
  product: 'CROP',
  selectedPaymentPlanAmount: undefined,
  cropData: {
    crops: [
      { parcels: [{ id: 'parcel-1' }, { id: 'parcel-2' }] },
      { parcels: [{ id: 'parcel-3' }] }
    ]
  }
});

const runtimeMotorOffer = createOffer({
  id: 'runtime-motor-offer',
  offerNumber: 'OFR/RUNTIME/003',
  createdAt: '2026-04-15T10:00:00.000Z',
  updatedAt: '2026-04-15T10:00:00.000Z',
  validTo: '2026-05-01T00:00:00.000Z',
  premiumAmount: 600,
  selectedPaymentPlanAmount: 600,
  product: 'MOTOR'
});

class OffersRepositoryStub {
  getOffers() {
    return of([motorOffer, cropOffer]);
  }
}

class ReferenceDataRepositoryStub {
  getReferenceData() {
    return of(mockReferenceData);
  }
}

class SalesFlowRuntimeRepositoryStub {
  readonly runtimeOffers = signal<Offer[]>([runtimeMotorOffer]);
  saveDraftOffer = jasmine.createSpy('saveDraftOffer');
  promoteOfferToPolicy = jasmine.createSpy('promoteOfferToPolicy');
}

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;
  let currencyRatesRepositorySpy: jasmine.SpyObj<CurrencyRatesRepository>;

  beforeEach(async () => {
    currencyRatesRepositorySpy = jasmine.createSpyObj<CurrencyRatesRepository>('CurrencyRatesRepository', ['getQuote']);

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: OffersRepository, useClass: OffersRepositoryStub },
        { provide: ReferenceDataRepository, useClass: ReferenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useClass: SalesFlowRuntimeRepositoryStub },
        { provide: CurrencyRatesRepository, useValue: currencyRatesRepositorySpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('defaults to PLN and exposes only PLN, EUR and USD', () => {
    expect(component['selectedCurrency']()).toBe('PLN');
    expect(component['currencyOptions'].map((option) => option.code)).toEqual(['PLN', 'EUR', 'USD']);
    expect(currencyRatesRepositorySpy.getQuote).not.toHaveBeenCalled();
    expect(queryRateInfo()).toBeNull();
    expect(readPremiumValues().every((value) => value.includes('zł'))).toBeTrue();
  });

  it('converts visible offers to EUR and shows the service quote metadata', fakeAsync(() => {
    currencyRatesRepositorySpy.getQuote.and.returnValue(of(createServiceQuote('EUR', 4, '2026-04-17')));

    component['selectedCurrency'].set('EUR');
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    expect(currencyRatesRepositorySpy.getQuote).toHaveBeenCalledOnceWith('EUR');
    expect(readPremiumValues()).toEqual(['300,00 €', '600,00 €', '150,00 €']);
    expect(readStatValues()[2]).toBe('29,17 €');

    const rateInfo = normalizeText(queryRateInfo()?.textContent ?? '');
    expect(rateInfo).toContain('Kurs EUR');
    expect(rateInfo).toContain('1 EUR = 4,0 PLN');
    expect(rateInfo).toContain('source: service');
    expect(rateInfo).toContain('data: 2026-04-17');
  }));

  it('hides rate info and restores PLN premiums after switching back to PLN', fakeAsync(() => {
    currencyRatesRepositorySpy.getQuote.and.returnValue(of(createServiceQuote('EUR', 4, '2026-04-17')));

    component['selectedCurrency'].set('EUR');
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    component['selectedCurrency'].set('PLN');
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    expect(queryRateInfo()).toBeNull();
    expect(readPremiumValues()).toEqual(['1 200 zł', '2 400 zł', '600 zł']);
    expect(readStatValues()[2]).toBe('117 zł');
  }));

  it('shows fallback metadata and converted USD premiums when the rate service falls back', fakeAsync(() => {
    currencyRatesRepositorySpy.getQuote.and.returnValue(of(createFallbackQuote('USD', 3.6)));

    component['selectedCurrency'].set('USD');
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    expect(currencyRatesRepositorySpy.getQuote).toHaveBeenCalledOnceWith('USD');
    expect(readPremiumValues()).toEqual(['300,00 $', '600,00 $', '150,00 $']);

    const rateInfo = normalizeText(queryRateInfo()?.textContent ?? '');
    expect(rateInfo).toContain('Kurs USD');
    expect(rateInfo).toContain('1 USD = 3,6 PLN');
    expect(rateInfo).toContain('source: fallback');
    expect(rateInfo).not.toContain('data:');
  }));

  it('keeps filtering and sorting behavior while converting only the visible offers', fakeAsync(() => {
    currencyRatesRepositorySpy.getQuote.and.returnValue(of(createServiceQuote('EUR', 4, '2026-04-17')));

    component['selectedCurrency'].set('EUR');
    component['selectedProduct'].set('MOTOR');
    component['selectedSortField'].set('VALID_TO');
    component['selectedSortDirection'].set('ASC');
    fixture.detectChanges();
    tick();
    fixture.detectChanges();

    expect(component['filteredOffers']().map((offer) => offer.id)).toEqual(['runtime-motor-offer', 'motor-offer']);
    expect(component['totalVisibleOffers']()).toBe(2);
    expect(readPremiumValues()).toEqual(['150,00 €', '300,00 €']);
    expect(readStatValues()[2]).toBe('18,75 €');
    expect(fixture.nativeElement.querySelector('.empty-state')).toBeNull();
  }));

  function queryRateInfo(): HTMLElement | null {
    return fixture.nativeElement.querySelector('.rate-info');
  }

  function readPremiumValues(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('.premium-box strong')).map((element) =>
      normalizeText((element as HTMLElement).textContent ?? '')
    );
  }

  function readStatValues(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('.stat-tile__value')).map((element) =>
      normalizeText((element as HTMLElement).textContent ?? '')
    );
  }
});

function createServiceQuote(currency: 'EUR' | 'USD', rate: number, effectiveDate?: string): CurrencyRateQuote {
  return {
    currency,
    rate,
    source: 'service',
    effectiveDate
  };
}

function createFallbackQuote(currency: 'EUR' | 'USD', rate: number): CurrencyRateQuote {
  return {
    currency,
    rate,
    source: 'fallback'
  };
}

function normalizeText(value: string): string {
  return value.replace(/\s+/g, ' ').trim();
}

function createOffer(config: {
  id: string;
  offerNumber: string;
  createdAt: string;
  updatedAt: string;
  validTo: string;
  premiumAmount: number;
  selectedPaymentPlanAmount?: number;
  product: 'MOTOR' | 'CROP';
  cropData?: {
    crops: Array<{
      parcels: Array<{ id: string }>;
    }>;
  };
}): Offer {
  return {
    id: config.id,
    product: config.product,
    offerNumber: config.offerNumber,
    status: 'ISSUED',
    createdAt: config.createdAt,
    updatedAt: config.updatedAt,
    validTo: config.validTo,
    salesChannel: 'AGENT' as never,
    agent: {
      salesChannel: 'Agent',
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
      make: config.product === 'CROP' ? 'Gospodarstwo' : 'Toyota',
      model: config.product === 'CROP' ? 'Uprawy' : 'Corolla',
      productionYear: 2020,
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
        id: `${config.id}-variant`,
        name: config.product === 'CROP' ? 'Pakiet upraw' : 'Standard',
        totalPremium: {
          amount: config.premiumAmount,
          currency: 'PLN'
        }
      }
    ] as never,
    selectedVariantId: `${config.id}-variant`,
    selectedPaymentPlan:
      config.selectedPaymentPlanAmount === undefined
        ? undefined
        : ({
            totalPremium: {
              amount: config.selectedPaymentPlanAmount,
              currency: 'PLN'
            }
          } as never),
    ...(config.cropData ? ({ cropData: config.cropData } as object) : {})
  } as Offer;
}
