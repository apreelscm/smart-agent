import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { PoliciesHomePageComponent } from './policies-home-page.component';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyService } from '../../../core/services/currency.service';
import { ActivatedRoute, Router } from '@angular/router';

describe('PoliciesHomePageComponent', () => {
  let fixture: ComponentFixture<PoliciesHomePageComponent>;
  let component: PoliciesHomePageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PoliciesHomePageComponent],
      providers: [
        {
          provide: PoliciesRepository,
          useValue: {
            getPolicies: () => of([])
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            promotedPolicies: () => []
          }
        },
        {
          provide: CurrencyService,
          useValue: {
            isForeignCurrencyAvailable: () => false,
            availability: () => ({ available: false, message: 'Brak kursów' }),
            rateLabel: () => null
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            queryParamMap: of(new Map())
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

    fixture = TestBed.createComponent(PoliciesHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should default to PLN', () => {
    expect(component['selectedCurrency']()).toBe('PLN');
  });

  it('should keep PLN when foreign currency is unavailable', () => {
    component['setSelectedCurrency']('EUR');
    expect(component['selectedCurrency']()).toBe('PLN');
  });
}
