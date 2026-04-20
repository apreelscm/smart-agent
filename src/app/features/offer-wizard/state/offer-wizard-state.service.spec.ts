import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { OfferWizardStateService } from './offer-wizard-state.service';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';

describe('OfferWizardStateService', () => {
  let service: OfferWizardStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        OfferWizardStateService,
        {
          provide: OffersRepository,
          useValue: {
            getTemplateOffer: () =>
              of({
                id: 'template',
                offerNumber: 'TMP',
                status: 'DRAFT',
                createdAt: '2026-04-01T00:00:00Z',
                updatedAt: '2026-04-01T00:00:00Z',
                salesChannel: 'AGENCY',
                agent: { id: 'a1', fullName: 'Agent', salesChannel: 'AGENCY' },
                customer: {
                  id: 'c1',
                  identity: { type: 'NATURAL_PERSON', personName: { firstName: '', lastName: '' }, pesel: '', birthDate: '' },
                  contact: { email: '', phoneNumber: '' },
                  residenceAddress: { street: '', buildingNumber: '', postalCode: '', city: '', countryCode: 'PL' },
                  isVatPayer: false,
                  isForeignClient: false
                },
                vehicle: {
                  make: '',
                  model: '',
                  version: '',
                  vin: '',
                  productionYear: 2024,
                  specialUsages: [],
                  registration: { registrationNumber: '', firstRegistrationDate: '', countryCode: 'PL' },
                  engine: { fuelType: 'PETROL' }
                },
                insuredObject: { id: 'i1', label: 'obj', vehicleId: 'v1', ownerCustomerId: 'c1', primaryDriverCustomerId: 'c1' },
                variants: [
                  {
                    id: 'v1',
                    name: 'Standard',
                    rank: 1,
                    totalPremium: { amount: 1000, currency: 'PLN' },
                    paymentPlans: [{ frequency: 'ANNUAL', totalPremium: { amount: 1000, currency: 'PLN' }, installments: [{ sequence: 1, amount: { amount: 1000, currency: 'PLN' } }] }],
                    policyLines: [{ id: 'l1', code: 'OC', label: 'OC', included: true, premium: { amount: 500, currency: 'PLN' }, covers: [] }]
                  }
                ]
              }),
            getOfferById: () => of(undefined)
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

  it('should keep discount persisted in PLN', async () => {
    await service.initializeNewDraft();
    service.setDiscountAmount(42);

    expect(service.discountAmount()).toBe(42);
  });
}
