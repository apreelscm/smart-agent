import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { Offer, ReferenceData } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

const referenceDataStub: ReferenceData = {
  offerStatuses: [],
  policyLines: [],
  salesChannels: [],
  vehicleUsages: [],
  vehicleFinancing: []
};

function buildOffer(overrides: Partial<Offer> = {}): Offer {
  return {
    id: 'offer-motor-1',
    product: 'MOTOR',
    offerNumber: 'OFR/2024/001',
    status: 'ISSUED',
    createdAt: '2024-02-20T08:00:00.000Z',
    updatedAt: '2024-02-29T10:15:00.000Z',
    validTo: '2024-03-31T00:00:00.000Z',
    salesChannel: 'AGENCY' as never,
    agent: {
      salesChannel: 'AGENCY',
      fullName: 'Jan Agent'
    } as never,
    customer: {
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Anna',
          lastName: 'Kowalska'
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
        fuelType: 'Hybrid'
      },
      registration: {
        registrationNumber: 'WI 1234A'
      }
    } as never,
    insuredObject: {} as never,
    variants: [
      {
        id: 'variant-basic',
        name: 'Podstawowy',
        totalPremium: {
          amount: 1200,
          currency: 'PLN'
        }
      } as never
    ],
    selectedVariantId: 'variant-basic',
    ...overrides
  } as Offer;
}

function buildCropOffer(): Offer {
  return {
    ...buildOffer({
      id: 'offer-crop-1',
      product: 'CROP',
      offerNumber: 'OFR/2024/002',
      customer: {
        identity: {
          type: 'LEGAL_ENTITY',
          companyName: 'Gospodarstwo Rolne Zielone Pole'
        },
        residenceAddress: {
          city: 'Płock'
        }
      } as never,
      vehicle: {
        make: 'Farm',
        model: 'Placeholder',
        productionYear: 2021,
        engine: {
          fuelType: 'Diesel'
        },
        registration: {
          registrationNumber: 'PL 00000'
        }
      } as never
    }),
    cropData: {
      crops: [{ parcels: [{}, {}] }, { parcels: [{}] }]
    }
  } as unknown as Offer;
}

describe('OffersHomePageComponent', () => {
  const offers = [buildOffer(), buildCropOffer()];

  beforeEach(() => {
    jasmine.clock().install();
    jasmine.clock().mockDate(new Date(2024, 1, 29, 10, 15, 0));
  });

  afterEach(() => {
    jasmine.clock().uninstall();
  });

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () => of(offers)
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () => of(referenceDataStub)
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: signal<Offer[]>([]),
            saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
            promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
          }
        }
      ]
    }).compileComponents();
  });

  it('should calculate protection period once using fixed yyyy/MM/dd format', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);

    expect(fixture.componentInstance['protectionPeriodLabel']).toBe('2024/02/29 - 2025/02/28');
  });

  it('should render protection period for every visible offer', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const protectionLabels = Array.from(compiled.querySelectorAll('.offer-row__meta-label')).filter(
      (element) => element.textContent?.trim() === 'Okres ochrony'
    );
    const protectionValues = compiled.querySelectorAll('.offer-row__protection-period');

    expect(protectionLabels.length).toBe(2);
    expect(protectionValues.length).toBe(2);
    expect(Array.from(protectionValues).every((element) => element.textContent?.trim() === '2024/02/29 - 2025/02/28')).toBeTrue();
  });

  it('should keep rendering existing offer content after adding protection period', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;

    expect(compiled.querySelectorAll('.offer-row').length).toBe(2);
    expect(compiled.textContent).toContain('OFR/2024/001');
    expect(compiled.textContent).toContain('OFR/2024/002');
    expect(compiled.textContent).toContain('WI 1234A');
    expect(compiled.textContent).toContain('2 uprawy · 3 działki');
  });
});
