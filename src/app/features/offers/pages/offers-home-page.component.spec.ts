import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { OffersHomePageComponent } from './offers-home-page.component';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { Offer, ReferenceData } from '../../../core/models';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;

  const referenceData: ReferenceData = {
    offerStatuses: [],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  const offers: Offer[] = [
    {
      id: 'offer-pln',
      product: 'MOTOR',
      offerNumber: 'OF/PLN/1',
      status: 'ISSUED',
      createdAt: '2026-03-07T09:14:00Z',
      updatedAt: '2026-03-09T08:32:00Z',
      validTo: '2026-03-21T22:59:59Z',
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
            lastName: 'Kowalski'
          },
          pesel: '90021212345',
          birthDate: '1990-02-12'
        },
        contact: {
          email: 'jan@example.pl',
          phoneNumber: '+48 600 000 000'
        },
        residenceAddress: {
          street: 'Lipowa',
          buildingNumber: '1',
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
        vin: 'VIN1',
        usage: 'PRIVATE',
        financing: 'OWNED',
        annualMileageKm: 10000,
        marketValue: 100000,
        registration: {
          registrationNumber: 'WA12345',
          firstRegistrationDate: '2024-01-01',
          countryCode: 'PL'
        },
        engine: {
          fuelType: 'PETROL',
          displacementCc: 1498,
          powerKw: 110,
          powerHp: 150
        },
        grossVehicleWeightKg: 1800,
        specialUsages: []
      },
      insuredObject: {
        id: 'insured-1',
        type: 'VEHICLE',
        label: 'Skoda Octavia',
        vehicleId: 'vehicle-1',
        ownerCustomerId: 'customer-1',
        primaryDriverCustomerId: 'customer-1'
      },
      variants: [
        {
          id: 'variant-1',
          name: 'Optimum',
          rank: 1,
          recommended: true,
          selected: true,
          totalPremium: {
            amount: 1200,
            currency: 'PLN'
          },
          monthlyPremium: {
            amount: 100,
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
          amount: 1200,
          currency: 'PLN'
        },
        installments: [
          {
            sequence: 1,
            dueDate: '2026-03-21',
            amount: {
              amount: 1200,
              currency: 'PLN'
            }
          }
        ],
        firstInstallment: 1200,
        nextInstallment: 1200
      },
      contractData: {
        paymentMethod: 'PRZELEW',
        attachments: []
      },
      notes: []
    },
    {
      id: 'offer-eur',
      product: 'MOTOR',
      offerNumber: 'OF/EUR/1',
      status: 'CALCULATION',
      createdAt: '2026-03-08T09:14:00Z',
      updatedAt: '2026-03-09T08:32:00Z',
      validTo: '2026-03-22T22:59:59Z',
      salesChannel: 'AGENCY',
      agent: {
        id: 'agent-2',
        fullName: 'Ewa Nowak',
        agencyCode: 'KRK-1205',
        salesChannel: 'Agencja własna'
      },
      customer: {
        id: 'customer-2',
        kind: 'NATURAL_PERSON',
        identity: {
          type: 'NATURAL_PERSON',
          personName: {
            firstName: 'Eva',
            lastName: 'Muster'
          },
          pesel: '90021212346',
          birthDate: '1990-02-12'
        },
        contact: {
          email: 'eva@example.pl',
          phoneNumber: '+48 600 000 001'
        },
        residenceAddress: {
          street: 'Nowa',
          buildingNumber: '2',
          postalCode: '00-002',
          city: 'Kraków',
          countryCode: 'PL'
        },
        isVatPayer: false,
        isForeignClient: false
      },
      vehicle: {
        id: 'vehicle-2',
        make: 'Audi',
        model: 'A4',
        version: 'Business',
        productionYear: 2024,
        vin: 'VIN2',
        usage: 'PRIVATE',
        financing: 'OWNED',
        annualMileageKm: 10000,
        marketValue: 120000,
        registration: {
          registrationNumber: 'KR12345',
          firstRegistrationDate: '2024-01-01',
          countryCode: 'PL'
        },
        engine: {
          fuelType: 'PETROL',
          displacementCc: 1984,
          powerKw: 140,
          powerHp: 190
        },
        grossVehicleWeightKg: 1900,
        specialUsages: []
      },
      insuredObject: {
        id: 'insured-2',
        type: 'VEHICLE',
        label: 'Audi A4',
        vehicleId: 'vehicle-2',
        ownerCustomerId: 'customer-2',
        primaryDriverCustomerId: 'customer-2'
      },
      variants: [
        {
          id: 'variant-2',
          name: 'Start',
          rank: 1,
          recommended: true,
          selected: true,
          totalPremium: {
            amount: 900,
            currency: 'EUR'
          },
          monthlyPremium: {
            amount: 75,
            currency: 'EUR'
          },
          paymentPlans: [],
          policyLines: []
        }
      ],
      selectedVariantId: 'variant-2',
      selectedPaymentPlan: {
        frequency: 'ANNUAL',
        totalPremium: {
          amount: 900,
          currency: 'EUR'
        },
        installments: [
          {
            sequence: 1,
            dueDate: '2026-03-21',
            amount: {
              amount: 900,
              currency: 'EUR'
            }
          }
        ],
        firstInstallment: 900,
        nextInstallment: 900
      },
      contractData: {
        paymentMethod: 'PRZELEW',
        attachments: []
      },
      notes: []
    },
    {
      id: 'offer-usd',
      product: 'MOTOR',
      offerNumber: 'OF/USD/1',
      status: 'DRAFT',
      createdAt: '2026-03-09T09:14:00Z',
      updatedAt: '2026-03-09T08:32:00Z',
      salesChannel: 'AGENCY',
      agent: {
        id: 'agent-3',
        fullName: 'John Smith',
        agencyCode: 'KRK-1206',
        salesChannel: 'Agencja własna'
      },
      customer: {
        id: 'customer-3',
        kind: 'NATURAL_PERSON',
        identity: {
          type: 'NATURAL_PERSON',
          personName: {
            firstName: 'John',
            lastName: 'Smith'
          },
          pesel: '90021212347',
          birthDate: '1990-02-12'
        },
        contact: {
          email: 'john@example.pl',
          phoneNumber: '+48 600 000 002'
        },
        residenceAddress: {
          street: 'Polna',
          buildingNumber: '3',
          postalCode: '00-003',
          city: 'Gdańsk',
          countryCode: 'PL'
        },
        isVatPayer: false,
        isForeignClient: false
      },
      vehicle: {
        id: 'vehicle-3',
        make: 'Ford',
        model: 'Mustang',
        version: 'GT',
        productionYear: 2024,
        vin: 'VIN3',
        usage: 'PRIVATE',
        financing: 'OWNED',
        annualMileageKm: 10000,
        marketValue: 250000,
        registration: {
          registrationNumber: 'GD12345',
          firstRegistrationDate: '2024-01-01',
          countryCode: 'PL'
        },
        engine: {
          fuelType: 'PETROL',
          displacementCc: 5000,
          powerKw: 330,
          powerHp: 450
        },
        grossVehicleWeightKg: 2000,
        specialUsages: []
      },
      insuredObject: {
        id: 'insured-3',
        type: 'VEHICLE',
        label: 'Ford Mustang',
        vehicleId: 'vehicle-3',
        ownerCustomerId: 'customer-3',
        primaryDriverCustomerId: 'customer-3'
      },
      variants: [
        {
          id: 'variant-3',
          name: 'Premium',
          rank: 1,
          recommended: true,
          selected: true,
          totalPremium: {
            amount: 1500,
            currency: 'USD'
          },
          monthlyPremium: {
            amount: 125,
            currency: 'USD'
          },
          paymentPlans: [],
          policyLines: []
        }
      ],
      selectedVariantId: 'variant-3',
      contractData: {
        paymentMethod: 'PRZELEW',
        attachments: []
      },
      notes: []
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
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
          useValue: {
            runtimeOffers: () => [],
            saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
            promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();
  });

  it('renders offer premiums in their own currencies', () => {
    const text = fixture.nativeElement.textContent;

    expect(text).toContain('1 200 zł');
    expect(text).toContain('€900');
    expect(text).toContain('$1,500');
  });

  it('counts average premium only for PLN offers', () => {
    const text = fixture.nativeElement.textContent;

    expect(text).toContain('100 zł');
    expect(text).toContain('tylko dla ofert PLN');
  });
});
