import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { PoliciesHomePageComponent } from './policies-home-page.component';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { ActivatedRoute, Router } from '@angular/router';

describe('PoliciesHomePageComponent', () => {
  let fixture: ComponentFixture<PoliciesHomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PoliciesHomePageComponent],
      providers: [
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
                  annualPremium: 430,
                  monthlyPremium: 36,
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
          useValue: { promotedPolicies: () => [] }
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
          provide: ActivatedRoute,
          useValue: { queryParamMap: of(new Map() as any) }
        },
        {
          provide: Router,
          useValue: { navigate: jasmine.createSpy().and.resolveTo(true) }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PoliciesHomePageComponent);
    fixture.detectChanges();
  });

  it('defaults to PLN currency', () => {
    expect(fixture.componentInstance['selectedCurrency']()).toBe('PLN');
  });

  it('switches annual and monthly premiums to EUR presentation', () => {
    fixture.componentInstance.changeCurrency('EUR');
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Kurs NBP EUR');
  });
});
