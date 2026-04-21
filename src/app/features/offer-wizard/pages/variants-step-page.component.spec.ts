import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { VariantsStepPageComponent } from './variants-step-page.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';

describe('VariantsStepPageComponent', () => {
  let fixture: ComponentFixture<VariantsStepPageComponent>;
  let stateService: jasmine.SpyObj<OfferWizardStateService>;

  beforeEach(async () => {
    stateService = jasmine.createSpyObj<OfferWizardStateService>('OfferWizardStateService', [
      'ensureDefaultVariantSelection',
      'updateSelectedVariant',
      'setCoverSelection',
      'updateSelectedPaymentPlan',
      'setDiscountAmount',
      'setDiscountAmountFromCurrency',
      'updateCoverTermValue'
    ], {
      draftOffer: () => ({
        variants: [
          {
            id: 'v1',
            name: 'Standard',
            rank: 1,
            selected: true,
            recommended: false,
            totalPremium: { amount: 430, currency: 'PLN' },
            policyLines: [{ code: 'OC', label: 'OC', premium: { amount: 430, currency: 'PLN' }, covers: [] }],
            paymentPlans: [{ frequency: 'ANNUAL', totalPremium: { amount: 430, currency: 'PLN' }, installments: [] }]
          }
        ],
        selectedVariantId: 'v1',
        selectedPaymentPlan: { frequency: 'ANNUAL', totalPremium: { amount: 430, currency: 'PLN' }, installments: [] }
      }),
      discountAmount: () => 0
    } as any);

    await TestBed.configureTestingModule({
      imports: [VariantsStepPageComponent],
      providers: [
        { provide: OfferWizardStateService, useValue: stateService },
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

    fixture = TestBed.createComponent(VariantsStepPageComponent);
    fixture.detectChanges();
  });

  it('shows PLN as default input currency', () => {
    expect(fixture.componentInstance['selectedInputCurrency']()).toBe('PLN');
  });

  it('converts foreign discount input to PLN before persistence', () => {
    fixture.componentInstance.changeInputCurrency('EUR');
    fixture.componentInstance.onDiscountInput('100');
    fixture.componentInstance.onDiscountBlur();

    expect(stateService.setDiscountAmountFromCurrency).toHaveBeenCalledWith('100' as any, jasmine.anything(), jasmine.anything());
  });
});
