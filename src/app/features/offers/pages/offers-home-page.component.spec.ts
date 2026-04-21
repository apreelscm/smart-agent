import { signal } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  const offersRepositoryStub = {
    getOffers: () =>
      of([
        {
          id: 'offer-1',
          offerNumber: 'OFR/1',
          status: 'ISSUED',
          product: 'MOTOR',
          createdAt: '2026-01-01T10:00:00Z',
          updatedAt: '2026-01-01T10:00:00Z',
          validTo: '2026-02-01',
          customer: {
            id: 'customer-1',
            identity: {
              type: 'NATURAL_PERSON',
              personName: { firstName: 'Jan', lastName: 'Kowalski' },
              pesel: '80010112345',
              birthDate: '1980-01-01'
            },
            contact: {},
            residenceAddress: { city: 'Warszawa', postalCode: '00-001', street: 'Testowa', buildingNumber: '1', countryCode: 'PL' }
          },
          vehicle: {
            make: 'Toyota',
            model: 'Corolla',
            productionYear: 2022,
            engine: { fuelType: 'PETROL' },
            registration: { registrationNumber: 'WX12345' }
          },
          variants: [
            {
              id: 'variant-1',
              name: 'Standard',
              rank: 1,
              recommended: true,
              selected: true,
              totalPremium: { amount: 4000, currency: 'PLN' },
              policyLines: [],
              paymentPlans: []
            }
          ],
          selectedVariantId: 'variant-1',
          selectedPaymentPlan: {
            frequency: 'ANNUAL',
            installments: [{ sequence: 1, dueDate: '2026-01-01', amount: { amount: 4000, currency: 'PLN' } }],
            totalPremium: { amount: 4000, currency: 'PLN' }
          },
          agent: { fullName: 'Agent Test', salesChannel: 'Oddział' }
        }
      ] as any)
  };

  const referenceDataRepositoryStub = {
    getReferenceData: () =>
      of({
        offerStatuses: [],
        policyLines: [],
        salesChannels: [],
        vehicleUsages: [],
        vehicleFinancing: []
      } as any)
  };

  const runtimeRepositoryStub = {
    runtimeOffers: signal([]),
    promotedPolicies: signal([]),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  };

  it('switches visible premiums to EUR', async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: runtimeRepositoryStub },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () =>
              of({
                effectiveDate: '2026-04-17',
                rates: {
                  EUR: 4,
                  USD: 3.5
                }
              })
          }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const currencyPresentation = fixture.debugElement.injector.get<any>(Object.getPrototypeOf(fixture.componentInstance).constructor.ɵcmp.providers[0] ?? null) as any;
    const localService = fixture.debugElement.injector.get((fixture.componentInstance as any).currencyPresentation.constructor);
    localService.selectCurrency('EUR');
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('€');
    expect(fixture.nativeElement.textContent).toContain('Kurs EUR/PLN');
  });

  it('shows degradation message and keeps PLN when exchange service fails', async () => {
    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: runtimeRepositoryStub },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () => throwError(() => new Error('nbp error'))
          }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('wyłącznie w PLN');
    expect(fixture.nativeElement.textContent).toContain('zł');
  });
});
