import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer } from '../../../core/models/offer/offer.model';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { getCoveragePeriodLabel } from '../utils/coverage-period.util';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  const fixedNow = new Date('2025-01-02T10:15:00+01:00');

  const mockOffers: Offer[] = [
    buildOffer({
      id: 'offer-1',
      offerNumber: 'OFR/000001',
      updatedAt: '2025-01-02T08:10:00.000Z'
    }),
    buildOffer({
      id: 'offer-2',
      offerNumber: 'OFR/000002',
      updatedAt: '2025-01-02T09:40:00.000Z',
      customer: {
        identity: {
          type: 'NATURAL_PERSON',
          personName: {
            firstName: 'Piotr',
            lastName: 'Kowalski'
          }
        },
        residenceAddress: {
          city: 'Kraków'
        }
      } as never,
      vehicle: {
        make: 'Skoda',
        model: 'Octavia',
        productionYear: 2021,
        engine: {
          fuelType: 'Diesel'
        },
        registration: {
          registrationNumber: 'KR 54321'
        }
      } as never
    })
  ];

  const mockReferenceData = {
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

  const salesFlowRuntimeRepositoryStub = {
    runtimeOffers: signal<Offer[]>([]),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () => of(mockOffers)
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () => of(mockReferenceData)
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: salesFlowRuntimeRepositoryStub
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    jasmine.clock().install();
    jasmine.clock().mockDate(fixedNow);
  });

  afterEach(() => {
    jasmine.clock().uninstall();
  });

  it('renders the exact same coverage period label for every visible offer row', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const expectedLabel = getCoveragePeriodLabel(fixedNow);
    const coverageValues = Array.from(
      fixture.nativeElement.querySelectorAll('.offer-row__coverage-value'),
      (element: Element) => normalizeText(element.textContent)
    );

    expect(coverageValues).toEqual([expectedLabel, expectedLabel]);
  });

  it('renders Okres ochrony before Aktualizacja in the metadata grid', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const firstOfferRow = fixture.nativeElement.querySelector('.offer-row') as HTMLElement;
    const metaLabels = Array.from(
      firstOfferRow.querySelectorAll('.offer-row__meta-grid .offer-row__meta-label'),
      (element: Element) => normalizeText(element.textContent)
    );

    expect(metaLabels.indexOf('Okres ochrony')).toBeGreaterThan(-1);
    expect(metaLabels.indexOf('Aktualizacja')).toBeGreaterThan(-1);
    expect(metaLabels.indexOf('Okres ochrony')).toBeLessThan(metaLabels.indexOf('Aktualizacja'));
  });

  function normalizeText(value: string | null): string {
    return value?.replace(/\s+/g, ' ').trim() ?? '';
  }

  function buildOffer(overrides: Partial<Offer> = {}): Offer {
    return {
      id: 'offer-default',
      product: 'MOTOR',
      offerNumber: 'OFR/DEFAULT',
      status: 'ISSUED',
      createdAt: '2025-01-01T08:00:00.000Z',
      updatedAt: '2025-01-02T08:00:00.000Z',
      validTo: '2025-02-01T23:59:59.000Z',
      salesChannel: 'AGENCY' as never,
      agent: {
        fullName: 'Jan Agent',
        salesChannel: 'Agent'
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
          fuelType: 'Benzyna'
        },
        registration: {
          registrationNumber: 'WX 12345'
        }
      } as never,
      insuredObject: {} as never,
      variants: [
        {
          id: 'variant-1',
          name: 'Standard',
          totalPremium: {
            amount: 1200,
            currency: 'PLN'
          }
        } as never
      ],
      selectedVariantId: 'variant-1',
      ...overrides
    };
  }
});
