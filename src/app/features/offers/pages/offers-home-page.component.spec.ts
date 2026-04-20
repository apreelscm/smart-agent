import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { OffersHomePageComponent } from './offers-home-page.component';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyService } from '../../../core/services/currency.service';
import { Router } from '@angular/router';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;

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
                  offerNumber: 'O-1',
                  status: 'ISSUED',
                  createdAt: '2026-04-01T10:00:00Z',
                  updatedAt: '2026-04-01T10:00:00Z',
                  salesChannel: 'AGENCY',
                  agent: { id: 'a1', fullName: 'Agent Test', salesChannel: 'AGENCY' },
                  customer: {
                    id: 'c1',
                    identity: { type: 'NATURAL_PERSON', personName: { firstName: 'Jan', lastName: 'Nowak' }, pesel: '123', birthDate: '1990-01-01' },
                    contact: { email: 'a@a.pl', phoneNumber: '123' },
                    residenceAddress: { street: 'A', buildingNumber: '1', postalCode: '00-000', city: 'Warszawa', countryCode: 'PL' },
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
                    registration: { registrationNumber: 'WX123', firstRegistrationDate: '2020-01-01', countryCode: 'PL' },
                    engine: { fuelType: 'PETROL' }
                  },
                  insuredObject: { id: 'i1', label: 'Toyota Corolla', vehicleId: 'v1', ownerCustomerId: 'c1', primaryDriverCustomerId: 'c1' },
                  variants: [
                    {
                      id: 'v1',
                      name: 'Standard',
                      rank: 1,
                      totalPremium: { amount: 400, currency: 'PLN' },
                      policyLines: []
                    }
                  ]
                }
              ])
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () =>
              of({
                offerStatuses: [],
                policyLines: [],
                salesChannels: [],
                vehicleUsages: [],
                vehicleFinancing: []
              })
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
          provide: CurrencyService,
          useValue: {
            isForeignCurrencyAvailable: () => true,
            availability: () => ({ available: true, message: null }),
            rateLabel: () => 'Kurs testowy',
            convertFromPln: (amount: number) => ({ convertedAmount: amount / 4, targetCurrency: 'EUR' }),
            formatAmount: (amount: number, currency: string) => `${amount} ${currency}`
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jasmine.createSpy()
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should default to PLN', () => {
    expect(component['selectedCurrency']()).toBe('PLN');
  });

  it('should switch displayed currency when foreign currency is available', () => {
    component['setSelectedCurrency']('EUR');
    expect(component['selectedCurrency']()).toBe('EUR');
  });

  it('should render clear filters button with Wyczyść label and green styling', () => {
    const clearFiltersButton: HTMLButtonElement | null = fixture.nativeElement.querySelector('.clear-filters-button');

    expect(clearFiltersButton).not.toBeNull();
    expect(clearFiltersButton?.textContent?.trim()).toBe('Wyczyść');
    expect(clearFiltersButton?.getAttribute('style')).toContain('background-color: #d4edda');
    expect(clearFiltersButton?.getAttribute('style')).toContain('color: #155724');
    expect(clearFiltersButton?.getAttribute('style')).toContain('border-color: #c3e6cb');
    expect(clearFiltersButton?.getAttribute('style')).not.toContain('#cce5ff');
    expect(clearFiltersButton?.getAttribute('style')).not.toContain('#004085');
    expect(clearFiltersButton?.getAttribute('style')).not.toContain('#b8daff');
    expect(fixture.nativeElement.textContent).not.toContain('Reset');
  });
}
