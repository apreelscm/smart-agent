import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer, OfferStatus, ReferenceData } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  const fixedNow = new Date('2025-05-10T08:15:00+02:00');

  const offers: Offer[] = [
    createOffer({
      id: 'offer-motor-1',
      product: 'MOTOR',
      offerNumber: 'OFR/MOT/001',
      status: 'ISSUED',
      createdAt: '2025-05-08T07:00:00Z',
      updatedAt: '2025-05-10T07:45:00Z',
      validTo: '2025-05-20T21:59:00Z',
      customerName: 'Jan Kowalski',
      vehicleMake: 'Toyota',
      vehicleModel: 'Corolla',
      registrationNumber: 'WI 1234A',
      premiumAmount: 1_200,
      variantName: 'Standard'
    }),
    createOffer({
      id: 'offer-crop-1',
      product: 'CROP',
      offerNumber: 'OFR/CROP/002',
      status: 'CALCULATION',
      createdAt: '2025-05-09T07:00:00Z',
      updatedAt: '2025-05-10T08:05:00Z',
      validTo: '2025-05-25T21:59:00Z',
      customerName: 'Agro Farma',
      vehicleMake: 'CROP',
      vehicleModel: 'Template',
      registrationNumber: 'brak',
      premiumAmount: 2_400,
      variantName: 'Rozszerzony',
      identityType: 'LEGAL_ENTITY',
      companyName: 'Agro Farma',
      cropData: {
        crops: [
          { parcels: [{ id: 'parcel-1' }, { id: 'parcel-2' }] },
          { parcels: [{ id: 'parcel-3' }] }
        ]
      }
    })
  ];

  const referenceData: ReferenceData = {
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

  const runtimeOffersState = signal<Offer[]>([]);
  const salesFlowRuntimeRepositoryStub = {
    runtimeOffers: runtimeOffersState.asReadonly(),
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
            getOffers: () => of(offers)
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () => of(referenceData)
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: salesFlowRuntimeRepositoryStub
        }
      ]
    }).compileComponents();
  });

  afterEach(() => {
    runtimeOffersState.set([]);
  });

  it('renders the Okres ochrony field for every visible offer using the CET label', () => {
    jasmine.clock().install();
    jasmine.clock().mockDate(fixedNow);

    try {
      const fixture = TestBed.createComponent(OffersHomePageComponent);
      fixture.detectChanges();

      const host = fixture.nativeElement as HTMLElement;
      const labels = Array.from(
        host.querySelectorAll('.offer-row__meta-item--protection-period .offer-row__meta-label')
      ).map((element) => element.textContent?.trim());
      const values = Array.from(host.querySelectorAll('.offer-row__meta-item--protection-period strong')).map((element) =>
        element.textContent?.trim()
      );

      expect(host.querySelectorAll('.offer-row').length).toBe(2);
      expect(labels).toEqual(['Okres ochrony', 'Okres ochrony']);
      expect(values).toEqual([
        '2025/05/10 00:00 - 2026/05/09 23:59',
        '2025/05/10 00:00 - 2026/05/09 23:59'
      ]);
    } finally {
      jasmine.clock().uninstall();
    }
  });

  it('keeps filtering and sorting behavior intact after adding the protection-period field', () => {
    jasmine.clock().install();
    jasmine.clock().mockDate(fixedNow);

    try {
      const fixture = TestBed.createComponent(OffersHomePageComponent);
      fixture.detectChanges();

      const component = fixture.componentInstance as any;
      const host = fixture.nativeElement as HTMLElement;

      component.searchTerm.set('toyota');
      fixture.detectChanges();

      let rows = Array.from(host.querySelectorAll('.offer-row'));
      expect(rows.length).toBe(1);
      expect(rows[0].textContent).toContain('OFR/MOT/001');
      expect(rows[0].querySelector('.offer-row__meta-item--protection-period strong')?.textContent?.trim()).toBe(
        '2025/05/10 00:00 - 2026/05/09 23:59'
      );

      component.searchTerm.set('');
      component.selectedSortField.set('VALID_TO');
      component.selectedSortDirection.set('ASC');
      fixture.detectChanges();

      rows = Array.from(host.querySelectorAll('.offer-row'));
      const offerNumbers = rows.map(
        (row) => row.querySelector('.offer-row__number span:last-child')?.textContent?.trim() ?? ''
      );

      expect(offerNumbers).toEqual(['OFR/MOT/001', 'OFR/CROP/002']);
      expect(host.querySelectorAll('.offer-row__meta-item--protection-period').length).toBe(2);
    } finally {
      jasmine.clock().uninstall();
    }
  });
});

function createOffer(params: {
  id: string;
  product: 'MOTOR' | 'CROP';
  offerNumber: string;
  status: OfferStatus;
  createdAt: string;
  updatedAt: string;
  validTo: string;
  customerName: string;
  vehicleMake: string;
  vehicleModel: string;
  registrationNumber: string;
  premiumAmount: number;
  variantName: string;
  identityType?: 'NATURAL_PERSON' | 'LEGAL_ENTITY';
  companyName?: string;
  cropData?: {
    crops: Array<{
      parcels: Array<{ id: string }>;
    }>;
  };
}): Offer {
  return {
    id: params.id,
    product: params.product,
    offerNumber: params.offerNumber,
    status: params.status,
    createdAt: params.createdAt,
    updatedAt: params.updatedAt,
    validTo: params.validTo,
    salesChannel: 'AGENCY' as Offer['salesChannel'],
    agent: {
      id: 'agent-1',
      fullName: 'Anna Agent',
      salesChannel: 'Oddział'
    } as Offer['agent'],
    customer: {
      identity:
        params.identityType === 'LEGAL_ENTITY'
          ? {
              type: 'LEGAL_ENTITY',
              companyName: params.companyName ?? params.customerName
            }
          : {
              type: 'NATURAL_PERSON',
              personName: {
                firstName: params.customerName.split(' ')[0] ?? 'Jan',
                lastName: params.customerName.split(' ').slice(1).join(' ') || 'Kowalski'
              }
            },
      residenceAddress: {
        city: 'Warszawa'
      }
    } as Offer['customer'],
    vehicle: {
      make: params.vehicleMake,
      model: params.vehicleModel,
      productionYear: 2024,
      engine: {
        fuelType: 'Benzyna'
      },
      registration: {
        registrationNumber: params.registrationNumber
      }
    } as Offer['vehicle'],
    insuredObject: {} as Offer['insuredObject'],
    variants: [
      {
        id: 'variant-1',
        name: params.variantName,
        totalPremium: {
          amount: params.premiumAmount,
          currency: 'PLN'
        }
      }
    ] as Offer['variants'],
    selectedVariantId: 'variant-1',
    ...(params.cropData ? { cropData: params.cropData } : {})
  } as Offer;
}
