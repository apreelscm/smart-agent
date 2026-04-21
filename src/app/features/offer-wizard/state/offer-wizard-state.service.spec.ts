import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { OfferWizardStateService } from './offer-wizard-state.service';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { Offer } from '../../../core/models';

describe('OfferWizardStateService', () => {
  let service: OfferWizardStateService;

  const eurOffer: Offer = {
    id: 'offer-eur',
    product: 'MOTOR',
    offerNumber: 'OF/EUR/1',
    status: 'DRAFT',
    createdAt: '2026-03-08T09:14:00Z',
    updatedAt: '2026-03-09T08:32:00Z',
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
        paymentPlans: [
          {
            frequency: 'QUARTERLY',
            totalPremium: {
              amount: 900,
              currency: 'EUR'
            },
            installments: [
              {
                sequence: 1,
                dueDate: '2026-03-21',
                amount: {
                  amount: 225,
                  currency: 'EUR'
                }
              },
              {
                sequence: 2,
                dueDate: '2026-06-21',
                amount: {
                  amount: 225,
                  currency: 'EUR'
                }
              },
              {
                sequence: 3,
                dueDate: '2026-09-21',
                amount: {
                  amount: 225,
                  currency: 'EUR'
                }
              },
              {
                sequence: 4,
                dueDate: '2026-12-21',
                amount: {
                  amount: 225,
                  currency: 'EUR'
                }
              }
            ],
            firstInstallment: 225,
            nextInstallment: 225
          }
        ],
        policyLines: [
          {
            id: 'line-oc',
            code: 'OC',
            label: 'OC',
            included: true,
            basePremium: {
              amount: 0,
              currency: 'EUR'
            },
            premium: {
              amount: 400,
              currency: 'EUR'
            },
            covers: [
              {
                id: 'cover-oc',
                code: 'OC',
                label: 'OC',
                enabled: true,
                selectable: true,
                premiumDelta: {
                  amount: 400,
                  currency: 'EUR'
                },
                terms: []
              }
            ]
          }
        ]
      }
    ],
    selectedVariantId: 'variant-2',
    selectedPaymentPlan: {
      frequency: 'QUARTERLY',
      totalPremium: {
        amount: 900,
        currency: 'EUR'
      },
      installments: [
        {
          sequence: 1,
          dueDate: '2026-03-21',
          amount: {
            amount: 225,
            currency: 'EUR'
          }
        }
      ],
      firstInstallment: 225,
      nextInstallment: 225
    },
    contractData: {
      paymentMethod: 'PRZELEW',
      attachments: []
    },
    notes: []
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        OfferWizardStateService,
        {
          provide: OffersRepository,
          useValue: {
            getTemplateOffer: () => of(eurOffer),
            getOfferById: () => of(eurOffer)
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: () => []
          }
        }
      ]
    });

    service = TestBed.inject(OfferWizardStateService);
  });

  it('keeps offer currency when recalculating installments for foreign currency offers', async () => {
    await service.initializeFromOffer('offer-eur');
    service.updateProductCode('GREEN_CARD');

    const offer = service.draftOffer();
    const selectedVariant = service.selectedVariant();
    const installments = selectedVariant?.paymentPlans?.[0]?.installments ?? [];
    const greenCardLine = selectedVariant?.policyLines.find((line) => line.code === 'GREEN_CARD');

    expect(offer?.selectedPaymentPlan?.totalPremium.currency).toBe('EUR');
    expect(installments.every((installment) => installment.amount.currency === 'EUR')).toBeTrue();
    expect(greenCardLine?.premium.currency).toBe('EUR');
    expect(greenCardLine?.covers[0].premiumDelta?.currency).toBe('EUR');
  });
});
