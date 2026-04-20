import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VariantsStepPageComponent } from './variants-step-page.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';
import { CurrencyService } from '../../../core/services/currency.service';

describe('VariantsStepPageComponent', () => {
  let fixture: ComponentFixture<VariantsStepPageComponent>;
  let component: VariantsStepPageComponent;
  let wizardState: jasmine.SpyObj<OfferWizardStateService>;

  beforeEach(async () => {
    wizardState = jasmine.createSpyObj<OfferWizardStateService>('OfferWizardStateService', [
      'ensureDefaultVariantSelection',
      'setDiscountAmount',
      'updateSelectedVariant',
      'setCoverSelection',
      'updateSelectedPaymentPlan',
      'updateCoverTermValue'
    ], {
      draftOffer: () => ({
        variants: [
          {
            id: 'v1',
            name: 'Standard',
            rank: 1,
            totalPremium: { amount: 1000, currency: 'PLN' },
            paymentPlans: [{ frequency: 'ANNUAL', totalPremium: { amount: 1000, currency: 'PLN' }, installments: [{ sequence: 1, amount: { amount: 1000, currency: 'PLN' } }] }],
            policyLines: [{ id: 'l1', code: 'OC', label: 'OC', included: true, premium: { amount: 500, currency: 'PLN' }, covers: [] }]
          }
        ],
        selectedVariantId: 'v1',
        selectedPaymentPlan: { frequency: 'ANNUAL', totalPremium: { amount: 1000, currency: 'PLN' }, installments: [{ sequence: 1, amount: { amount: 1000, currency: 'PLN' } }] }
      }),
      discountAmount: () => 0
    });

    await TestBed.configureTestingModule({
      imports: [VariantsStepPageComponent],
      providers: [
        { provide: OfferWizardStateService, useValue: wizardState },
        {
          provide: CurrencyService,
          useValue: {
            isForeignCurrencyAvailable: () => true,
            availability: () => ({ available: true, message: null }),
            rateLabel: () => 'Kurs testowy',
            convertFromPln: (amount: number) => ({ convertedAmount: amount / 4, targetCurrency: 'EUR' }),
            convertToPln: (amount: number) => ({ convertedAmount: Math.round(amount * 4), rate: 4, publicationDate: '2026-04-17' }),
            helperLabelForForeignInput: () => 'Około 40 zł wg kursu NBP z 17.04.2026.',
            formatAmount: (amount: number, currency: string) => `${amount} ${currency}`
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VariantsStepPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should default input currency to PLN', () => {
    expect(component['selectedCurrency']()).toBe('PLN');
  });

  it('should convert foreign discount input to PLN on blur', () => {
    component['setSelectedCurrency']('EUR');
    component['onDiscountInput']('10');
    component['onDiscountBlur']();

    expect(wizardState.setDiscountAmount).toHaveBeenCalledWith(40);
  });
}
