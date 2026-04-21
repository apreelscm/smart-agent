import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ReportsPageComponent } from './reports-page.component';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';

describe('ReportsPageComponent', () => {
  let fixture: ComponentFixture<ReportsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsPageComponent],
      providers: [
        { provide: OffersRepository, useValue: { getOffers: () => of([]) } },
        {
          provide: PoliciesRepository,
          useValue: {
            getPolicies: () =>
              of([
                {
                  id: 'p1',
                  policyNumber: 'POL/1',
                  customerName: 'Jan Kowalski',
                  vehicleLabel: 'Toyota Corolla',
                  registrationNumber: 'WX1',
                  productName: 'OC',
                  status: 'ACTIVE',
                  paymentStatus: 'PAID',
                  annualPremium: 0,
                  monthlyPremium: 0,
                  issueDate: '2026-04-01',
                  conclusionDate: '2026-04-01',
                  coverageEndDate: '2027-03-31',
                  updatedAt: '2026-04-01T10:00:00Z',
                  agentName: 'Jan Agent'
                }
              ])
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: { runtimeOffers: () => [], promotedPolicies: () => [] }
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
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReportsPageComponent);
    fixture.detectChanges();
  });

  it('defaults to PLN and renders KPIs', () => {
    expect(fixture.componentInstance['selectedCurrency']()).toBe('PLN');
    expect(fixture.componentInstance.kpis().length).toBeGreaterThan(0);
  });

  it('keeps report money values convertible for zero data', () => {
    fixture.componentInstance.changeCurrency('USD');
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Kurs NBP USD');
  });
});
