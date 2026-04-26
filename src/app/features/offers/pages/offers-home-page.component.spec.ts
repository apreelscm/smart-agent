import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer } from '../../../core/models/offer/offer.model';
import { ReferenceData } from '../../../core/models/reference-data/reference-data.model';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  const referenceData = {
    offerStatuses: [
      { code: 'DRAFT', label: 'Draft' },
      { code: 'CALCULATION', label: 'Kalkulacja' },
      { code: 'ISSUED', label: 'Oferta wystawiona' }
    ],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  } as ReferenceData;

  const baseOffer = {
    id: 'offer-1',
    offerNumber: 'OF/2025/05/000001',
    status: 'ISSUED',
    createdAt: '2025-05-08T09:00:00Z',
    updatedAt: '2025-05-10T08:30:00Z',
    validTo: '2025-05-20T22:59:59Z',
    salesChannel: 'AGENCY',
    agent: {
      id: 'agent-1',
      fullName: 'Anna Kowalska',
      agencyCode: 'KRK-1204',
      salesChannel: 'Agencja własna'
    },
    customer: {
      id: 'customer-1',
      kind: 'NATURAL_PERSON',
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Jan',
          lastName: 'Nowak'
        },
        pesel: '90010112345',
        birthDate: '1990-01-01'
      },
      contact: {
        email: 'jan.nowak@example.pl',
        phoneNumber: '+48 600 100 100'
      },
      residenceAddress: {
        street: 'Polna',
        buildingNumber: '10',
        postalCode: '00-001',
        city: 'Warszawa',
        countryCode: 'PL'
      },
      isVatPayer: false,
      isForeignClient: false
    },
    vehicle: {
      id: 'vehicle-1',
      make: 'Skoda',
      model: 'Octavia',
      version: 'Style',
      productionYear: 2024,
      vin: 'VIN00000000000001',
      bodyType: 'Kombi',
      usage: 'PRIVATE',
      financing: 'OWNED',
      seats: 5,
      annualMileageKm: 15000,
      marketValue: 120000,
      registration: {
        registrationNumber: 'WA12345',
        firstRegistrationDate: '2024-04-01',
        countryCode: 'PL'
      },
      engine: {
        fuelType: 'PETROL',
        displacementCc: 1498,
        powerKw: 110,
        powerHp: 150
      },
      grossVehicleWeightKg: 1900,
      specialUsages: []
    },
    insuredObject: {
      id: 'insured-object-1',
      type: 'VEHICLE',
      label: 'Skoda Octavia WA12345',
      vehicleId: 'vehicle-1',
      ownerCustomerId: 'customer-1',
      primaryDriverCustomerId: 'customer-1'
    },
    variants: [
      {
        id: 'variant-1',
        name: 'Optimum',
        rank: 1,
        selected: true,
        recommended: true,
        totalPremium: {
          amount: 6244,
          currency: 'PLN'
        },
        monthlyPremium: {
          amount: 520.33,
          currency: 'PLN'
        },
        paymentPlans: [],
        policyLines: []
      }
    ],
    selectedVariantId: 'variant-1',
    selectedPaymentPlan: {
      frequency: 'ANNUAL',
      totalPremium: {
        amount: 6244,
        currency: 'PLN'
      },
      installments: [
        {
          sequence: 1,
          dueDate: '2025-05-20',
          amount: {
            amount: 6244,
            currency: 'PLN'
          }
        }
      ],
      firstInstallment: 6244,
      nextInstallment: 6244
    }
  } as Offer;

  const createOffer = (overrides: Partial<Offer> = {}): Offer =>
    ({
      ...baseOffer,
      ...overrides
    }) as Offer;

  it('renders the Okres ochrony field with a non-empty value for each visible offer', async () => {
    const runtimeOffersState = signal<Offer[]>([]);

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () =>
              of([
                createOffer(),
                createOffer({
                  id: 'offer-2',
                  offerNumber: 'OF/2025/05/000002',
                  status: 'CALCULATION',
                  updatedAt: '2025-05-10T09:15:00Z'
                })
              ])
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
          useValue: {
            runtimeOffers: runtimeOffersState.asReadonly(),
            saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
            promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
          }
        }
      ]
    }).compileComponents();

    jasmine.clock().install();
    jasmine.clock().mockDate(new Date('2025-05-10T08:15:00Z'));

    try {
      const fixture = TestBed.createComponent(OffersHomePageComponent);
      fixture.detectChanges();

      const host = fixture.nativeElement as HTMLElement;
      const protectionItems = Array.from(host.querySelectorAll('.offer-row__meta-item--protection'));
      const labels = protectionItems.map((item) => item.querySelector('.offer-row__meta-label')?.textContent?.trim());
      const values = protectionItems.map((item) => item.querySelector('strong')?.textContent?.trim());
      const firstRow = host.querySelector('.offer-row');
      const firstRowMetaLabels = Array.from(
        firstRow?.querySelectorAll('.offer-row__meta-grid .offer-row__meta-label') ?? []
      ).map((label) => label.textContent?.trim() ?? '');
      const protectionLabelIndex = firstRowMetaLabels.indexOf('Okres ochrony');
      const updatedAtLabelIndex = firstRowMetaLabels.indexOf('Aktualizacja');

      expect(protectionItems.length).toBe(2);
      expect(labels).toEqual(['Okres ochrony', 'Okres ochrony']);
      expect(values).toEqual(['2025/05/10 00:00 - 2026/05/09 23:59', '2025/05/10 00:00 - 2026/05/09 23:59']);
      expect(values.every((value) => !!value)).toBeTrue();
      expect(protectionLabelIndex).toBeGreaterThan(-1);
      expect(updatedAtLabelIndex).toBeGreaterThan(-1);
      expect(protectionLabelIndex).toBeLessThan(updatedAtLabelIndex);
    } finally {
      jasmine.clock().uninstall();
    }
  });
});
