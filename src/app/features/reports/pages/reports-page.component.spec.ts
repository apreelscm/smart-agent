import { signal } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { ReportsPageComponent } from './reports-page.component';

describe('ReportsPageComponent', () => {
  const offersRepositoryStub = {
    getOffers: () =>
      of([
        {
          id: 'offer-1',
          status: 'POLICY',
          createdAt: '2026-01-01T10:00:00Z',
          updatedAt: '2026-01-01T10:00:00Z',
          variants: [
            {
              id: 'variant-1',
              selected: true,
              rank: 1,
              name: 'Standard',
              totalPremium: { amount: 4000, currency: 'PLN' },
              policyLines: [{ id: 'line-1', label: 'OC', premium: { amount: 4000, currency: 'PLN' } }]
            }
          ],
          selectedVariantId: 'variant-1'
        }
      ] as any)
  };

  const policiesRepositoryStub = {
    getPolicies: () =>
      of([
        {
          id: 'policy-1',
          issueDate: '2026-01-01',
          paymentStatus: 'PAID',
          status: 'ACTIVE',
          annualPremium: 4800,
          sourceOfferId: 'offer-1'
        }
      ] as any)
  };

  const runtimeRepositoryStub = {
    runtimeOffers: signal([]),
    promotedPolicies: signal([])
  };

  it('renders report KPI and trend amounts in EUR', async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsPageComponent],
      providers: [
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: OffersRepository, useValue: offersRepositoryStub },
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

    const fixture = TestBed.createComponent(ReportsPageComponent);
    fixture.detectChanges();

    const localService = fixture.debugElement.injector.get((fixture.componentInstance as any).constructor.ɵcmp.providers[0] ?? (fixture.componentInstance as any).currencyPresentation.constructor);
    const currencyService = fixture.debugElement.injector.get((fixture.componentInstance as any).currencyPresentation.constructor);
    currencyService.selectCurrency('EUR');
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('€');
    expect(fixture.nativeElement.textContent).toContain('Kurs EUR/PLN');
  });

  it('shows degradation note and stays in PLN on exchange error', async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsPageComponent],
      providers: [
        { provide: LOCALE_ID, useValue: 'pl-PL' },
        { provide: OffersRepository, useValue: offersRepositoryStub },
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

    const fixture = TestBed.createComponent(ReportsPageComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('wyłącznie w PLN');
  });
});
