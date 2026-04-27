import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer, ReferenceData } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let offersRepositoryStub: Pick<OffersRepository, 'getOffers'>;
  let referenceDataRepositoryStub: Pick<ReferenceDataRepository, 'getReferenceData'>;
  let salesFlowRuntimeRepositoryStub: Pick<SalesFlowRuntimeRepository, 'runtimeOffers' | 'saveDraftOffer' | 'promoteOfferToPolicy'>;
  let runtimeOffersState: ReturnType<typeof signal<Offer[]>>;

  const mockReferenceData: ReferenceData = {
    offerStatuses: [{ code: 'ISSUED', label: 'Oferta wystawiona' }],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  beforeEach(async () => {
    runtimeOffersState = signal<Offer[]>([]);

    offersRepositoryStub = {
      getOffers: jasmine.createSpy('getOffers').and.returnValue(of([buildOffer()]))
    };

    referenceDataRepositoryStub = {
      getReferenceData: jasmine.createSpy('getReferenceData').and.returnValue(of(mockReferenceData))
    };

    salesFlowRuntimeRepositoryStub = {
      runtimeOffers: runtimeOffersState.asReadonly(),
      saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
      promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
    };

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: salesFlowRuntimeRepositoryStub }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    jasmine.clock().install();
  });

  afterEach(() => {
    jasmine.clock().uninstall();
  });

  it('renders the protection period for the current system date', () => {
    jasmine.clock().mockDate(new Date(2025, 0, 15, 10, 0, 0));

    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const metaGrid = compiled.querySelector('.offer-row__meta-grid');

    expect(metaGrid?.textContent).toContain('Okres ochrony');
    expect(metaGrid?.textContent).toContain('2025/01/15 - 2026/01/15');
  });

  it('renders a valid one-calendar-year protection period for leap day', () => {
    jasmine.clock().mockDate(new Date(2024, 1, 29, 8, 30, 0));

    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const metaGrid = compiled.querySelector('.offer-row__meta-grid');

    expect(metaGrid?.textContent).toContain('Okres ochrony');
    expect(metaGrid?.textContent).toContain('2024/02/29 - 2025/02/28');
  });
});

function buildOffer(): Offer {
  return {
    id: 'offer-1',
    product: 'MOTOR',
    offerNumber: 'OFR/2025/0001',
    status: 'ISSUED',
    createdAt: '2025-01-10T09:00:00.000Z',
    updatedAt: '2025-01-15T12:30:00.000Z',
    validTo: '2025-02-15T00:00:00.000Z',
    salesChannel: 'AGENT',
    agent: {
      id: 'agent-1',
      fullName: 'Jan Kowalski',
      salesChannel: 'AGENT'
    },
    customer: {
      id: 'customer-1',
      kind: 'PERSON',
      identity: {
        type: 'NATURAL_PERSON',
        pesel: '90010112345',
        personName: {
          firstName: 'Anna',
          lastName: 'Nowak'
        }
      },
      residenceAddress: {
        street: 'Leśna',
        houseNumber: '10',
        postalCode: '00-001',
        city: 'Warszawa',
        country: 'PL'
      }
    },
    vehicle: {
      type: 'PASSENGER_CAR',
      make: 'Toyota',
      model: 'Corolla',
      registration: {
        registrationNumber: 'WA12345'
      },
      productionYear: 2022,
      engine: {
        fuelType: 'PETROL',
        horsepower: 132,
        capacity: 1598
      }
    },
    insuredObject: {
      type: 'VEHICLE'
    },
    variants: [
      {
        id: 'variant-1',
        name: 'Komfort',
        totalPremium: {
          amount: 2400,
          currency: 'PLN'
        },
        covers: []
      }
    ],
    selectedVariantId: 'variant-1'
  } as Offer;
}
