import { signal } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { PoliciesHomePageComponent } from './policies-home-page.component';

describe('PoliciesHomePageComponent', () => {
  const policiesRepositoryStub = {
    getPolicies: () =>
      of([
        {
          id: 'policy-1',
          policyNumber: 'POL/1',
          customerName: 'Jan Kowalski',
          vehicleLabel: 'Toyota Corolla',
          registrationNumber: 'WX12345',
          productName: 'Polisa komunikacyjna',
          status: 'ACTIVE',
          paymentStatus: 'PAID',
          annualPremium: 4800,
          monthlyPremium: 400,
          issueDate: '2026-01-01',
          conclusionDate: '2026-01-01',
          coverageEndDate: '2026-12-31',
          updatedAt: '2026-01-01T10:00:00Z',
          agentName: 'Agent Test'
        }
      ] as any),
    getPolicyById: () => of(undefined)
  };

  const runtimeRepositoryStub = {
    runtimeOffers: signal([]),
    promotedPolicies: signal([]),
    getPromotedPolicyById: jasmine.createSpy('getPromotedPolicyById')
  };

  it('renders policy premiums in EUR after currency switch', async () => {
    await TestBed.configureTestingModule({
      imports: [PoliciesHomePageComponent],
      providers: [
        provideRouter([]),
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: PoliciesRepository, useValue: policiesRepositoryStub },
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

    const fixture = TestBed.createComponent(PoliciesHomePageComponent);
    fixture.detectChanges();

    const localService = fixture.debugElement.injector.get((fixture.componentInstance as any).currencyPresentation.constructor);
    localService.selectCurrency('EUR');
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('€');
    expect(fixture.nativeElement.textContent).toContain('Kurs EUR/PLN');
  });

  it('forces PLN when exchange repository errors', async () => {
    await TestBed.configureTestingModule({
      imports: [PoliciesHomePageComponent],
      providers: [
        provideRouter([]),
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: PoliciesRepository, useValue: policiesRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: runtimeRepositoryStub },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () => throwError(() => new Error('nbp error'))
          }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(PoliciesHomePageComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('wyłącznie w PLN');
  });
});
