import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { OffersHomePageComponent } from './offers-home-page.component';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { Router } from '@angular/router';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () =>
              of([
                {
                  id: '1',
                  offerNumber: 'OF/1',
                  status: 'ISSUED',
                  createdAt: '2026-04-01T10:00:00Z',
                  updatedAt: '2026-04-01T10:00:00Z',
                  salesChannel: 'AGENCY',
                  agent: { id: 'a1', fullName: 'Jan Agent', salesChannel: 'AGENCY' },
                  customer: {
                    id: 'c1',
                    identity: { type: 'NATURAL_PERSON', personName: { firstName: 'Jan', lastName: 'Kowalski' }, pesel: '', birthDate: '' },
                    contact: { email: '', phoneNumber: '' },
                    residenceAddress: { street: '', buildingNumber: '', postalCode: '', city: 'Warszawa', countryCode: 'PL' },
                    isVatPayer: false,
                    isForeignClient: false
                  },
                  vehicle: {
                    make: 'Toyota',
                    model: 'Corolla',
                    version: '',
                    vin: '',
                    productionYear: 2020,
                    specialUsages: [],
                    registration: { registrationNumber: 'WX1', firstRegistrationDate: '', countryCode: 'PL' },
                    engine: { fuelType: 'PETROL' }
                  },
                  insuredObject: { id: 'i1', kind: 'VEHICLE', label: 'Toyota Corolla', vehicleId: 'v1', ownerCustomerId: 'c1', primaryDriverCustomerId: 'c1' },
                  variants: [
                    {
                      id: 'v1',
                      name: 'Standard',
                      rank: 1,
                      selected: true,
                      recommended: false,
                      totalPremium: { amount: 430, currency: 'PLN' },
                      policyLines: [],
                      paymentPlans: [{ frequency: 'ANNUAL', totalPremium: { amount: 430, currency: 'PLN' }, installments: [] }]
                    }
                  ],
                  selectedVariantId: 'v1',
                  selectedPaymentPlan: { frequency: 'ANNUAL', totalPremium: { amount: 430, currency: 'PLN' }, installments: [] }
                }
              ]),
            getTemplateOffer: () => of()
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () =>
              of({ offerStatuses: [], policyLines: [], salesChannels: [], vehicleUsages: [], vehicleFinancing: [] })
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: () => [],
            saveDraftOffer: jasmine.createSpy(),
            promoteOfferToPolicy: jasmine.createSpy()
          }
        },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getCurrentRates: () =>
              of({
                table: 'A',
                sourceTableNo: '074/A/NBP/2026',
                publishedAt: '2026-04-17',
                rates: { EUR: 4.3, USD: 3.85 }
              })
          }
        },
        {
          provide: Router,
          useValue: { navigate: jasmine.createSpy().and.resolveTo(true) }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();
  });

  it('defaults to PLN currency', () => {
    expect(fixture.componentInstance['selectedCurrency']()).toBe('PLN');
  });

  it('changes currency to EUR and exposes rate note', () => {
    fixture.componentInstance.changeCurrency('EUR');
    fixture.detectChanges();

    expect(fixture.componentInstance['selectedCurrency']()).toBe('EUR');
    expect(fixture.nativeElement.textContent).toContain('Kurs NBP EUR');
  });
});
