import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer, Policy } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { CurrencyService } from '../../../core/services/currency.service';
import { ReportsPageComponent } from './reports-page.component';

describe('ReportsPageComponent', () => {
  const offer: Offer = {
    id: 'offer-1',
    offerNumber: 'OFR/1',
    status: 'POLICY',
    createdAt: '2026-01-01T10:00:00Z',
    updatedAt: '2026-01-02T10:00:00Z',
    salesChannel: 'AGENCY',
    agent: {
      id: 'agent-1',
      fullName: 'Jan Agent',
      salesChannel: 'Agency'
    },
    customer: {
      id: 'customer-1',
      kind: 'NATURAL_PERSON',
      identity: {
        type: 'NATURAL_PERSON',
        pesel: '90010112345',
        birthDate: '1990-01-01',
        citizenshipCountryCode: 'PL',
        personName: {
          firstName: 'Jan',
          lastName: 'Kowalski'
        }
      },
      contact: {
        email: 'jan@example.com',
        phoneNumber: '123'
      },
      residenceAddress: {
        street: 'Polna',
        buildingNumber: '1',
        postalCode: '00-001',
        city: 'Warszawa',
        countryCode: 'PL'
      },
      isVatPayer: false,
      isForeignClient: false
    },
    vehicle: {
      make: 'Skoda',
      model: 'Octavia',
      version: 'Style',
      vin: 'VIN',
      productionYear: 2024,
      usage: 'PRIVATE',
      financing: 'OWNED',
      registration: {
        registrationNumber: 'WX1',
        firstRegistrationDate: '2024-01-01',
        countryCode: 'PL'
      },
      specialUsages: [],
      engine: {
        fuelType: 'PETROL'
      }
    },
    insuredObject: {
      id: 'insured-1',
      kind: 'VEHICLE',
      label: 'Skoda Octavia',
      vehicleId: 'vehicle-1',
      ownerCustomerId: 'customer-1',
      primaryDriverCustomerId: 'customer-1'
    },
    variants: [
      {
        id: 'variant-1',
        name: 'Standard',
        rank: 1,
        totalPremium: { amount: 430, currency: 'PLN' },
        policyLines: [
          {
            id: 'line-1',
            code: 'OC',
            label: 'OC',
            included: true,
            premium: { amount: 430, currency: 'PLN' },
            covers: []
          }
        ]
      }
    ],
    selectedVariantId: 'variant-1'
  };

  const policy: Policy = {
    id: 'policy-1',
    sourceOfferId: 'offer-1',
    policyNumber: 'POL/1',
    customerName: 'Jan Kowalski',
    vehicleLabel: 'Skoda Octavia',
    registrationNumber: 'WX1',
    productName: 'OC',
    status: 'ACTIVE',
    paymentStatus: 'PAID',
    annualPremium: 430,
    monthlyPremium: 36,
    issueDate: '2026-01-02',
    conclusionDate: '2026-01-02',
    coverageEndDate: '2027-01-01',
    updatedAt: '2026-01-02T10:00:00Z',
    agentName: 'Jan Agent'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsPageComponent],
      providers: [
        provideRouter([]),
        CurrencyService,
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () => of([offer])
          }
        },
        {
          provide: PoliciesRepository,
          useValue: {
            getPolicies: () => of([policy])
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: signal([]),
            promotedPolicies: signal([])
          }
        },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () => of({ source: 'NBP' as const, table: 'A' as const, effectiveDate: '2026-04-17', rates: { EUR: 4.3, USD: 3.9 } })
          }
        }
      ]
    }).compileComponents();
  });

  it('renders report KPIs in one selected currency', async () => {
    const fixture = TestBed.createComponent(ReportsPageComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const eurButton = Array.from(fixture.nativeElement.querySelectorAll('button')).find(
      (button: HTMLButtonElement) => button.textContent?.trim() === 'EUR'
    ) as HTMLButtonElement;

    eurButton.click();
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('100,00');
    expect(fixture.nativeElement.textContent).toContain('€');
  });
});
