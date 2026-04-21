import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { CurrencyService } from '../../../core/services/currency.service';
import { CurrencyViewStore } from '../../../core/services/currency-view.store';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';
import { VariantsStepPageComponent } from './variants-step-page.component';

describe('VariantsStepPageComponent', () => {
  let setDiscountAmount: jasmine.Spy;

  beforeEach(async () => {
    setDiscountAmount = jasmine.createSpy('setDiscountAmount');

    await TestBed.configureTestingModule({
      imports: [VariantsStepPageComponent],
      providers: [
        CurrencyService,
        CurrencyViewStore,
        {
          provide: OfferWizardStateService,
          useValue: {
            draftOffer: signal({
              variants: [
                {
                  id: 'variant-1',
                  name: 'Standard',
                  rank: 1,
                  totalPremium: { amount: 430, currency: 'PLN' },
                  paymentPlans: [
                    {
                      frequency: 'ANNUAL',
                      totalPremium: { amount: 430, currency: 'PLN' },
                      installments: [{ sequence: 1, amount: { amount: 430, currency: 'PLN' } }]
                    }
                  ],
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
              selectedVariantId: 'variant-1',
              selectedPaymentPlan: {
                frequency: 'ANNUAL',
                totalPremium: { amount: 430, currency: 'PLN' },
                installments: [{ sequence: 1, amount: { amount: 430, currency: 'PLN' } }]
              }
            }),
            selectedVariant: signal({
              id: 'variant-1',
              name: 'Standard',
              rank: 1,
              totalPremium: { amount: 430, currency: 'PLN' },
              paymentPlans: [
                {
                  frequency: 'ANNUAL',
                  totalPremium: { amount: 430, currency: 'PLN' },
                  installments: [{ sequence: 1, amount: { amount: 430, currency: 'PLN' } }]
                }
              ],
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
            }),
            discountAmount: signal(0),
            ensureDefaultVariantSelection: () => undefined,
            updateSelectedVariant: () => undefined,
            updateSelectedPaymentPlan: () => undefined,
            setDiscountAmount,
            setCoverSelection: () => undefined,
            updateCoverTermValue: () => undefined
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

  it('converts EUR discount input to persisted PLN amount', async () => {
    const fixture = TestBed.createComponent(VariantsStepPageComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const eurButton = Array.from(fixture.nativeElement.querySelectorAll('.currency-amount-input__currency')).find(
      (button: HTMLButtonElement) => button.textContent?.trim() === 'EUR'
    ) as HTMLButtonElement;
    eurButton.click();
    fixture.detectChanges();

    const input = fixture.nativeElement.querySelector('.currency-amount-input__field input') as HTMLInputElement;
    input.value = '10';
    input.dispatchEvent(new Event('input'));
    input.dispatchEvent(new Event('blur'));
    fixture.detectChanges();

    expect(setDiscountAmount).toHaveBeenCalledWith(43);
    expect(fixture.nativeElement.textContent).toContain('≈ 43');
  });
});
