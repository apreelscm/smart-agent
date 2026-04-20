import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ReportsPageComponent } from './reports-page.component';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyService } from '../../../core/services/currency.service';

describe('ReportsPageComponent', () => {
  let fixture: ComponentFixture<ReportsPageComponent>;
  let component: ReportsPageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportsPageComponent],
      providers: [
        {
          provide: OffersRepository,
          useValue: { getOffers: () => of([]) }
        },
        {
          provide: PoliciesRepository,
          useValue: { getPolicies: () => of([]) }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: () => [],
            promotedPolicies: () => []
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
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReportsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should default to PLN', () => {
    expect(component['selectedCurrency']()).toBe('PLN');
  });

  it('should allow selecting EUR for whole page', () => {
    component['setSelectedCurrency']('EUR');
    expect(component['selectedCurrency']()).toBe('EUR');
  });
}
