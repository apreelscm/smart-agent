import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { CurrencyService } from '../../../core/services/currency.service';
import { CurrencyViewStore } from '../../../core/services/currency-view.store';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';
import { CropVariantsStepPageComponent } from './crop-variants-step-page.component';

describe('CropVariantsStepPageComponent', () => {
  let setCropDiscountAmount: jasmine.Spy;

  beforeEach(async () => {
    setCropDiscountAmount = jasmine.createSpy('setCropDiscountAmount');

    await TestBed.configureTestingModule({
      imports: [CropVariantsStepPageComponent],
      providers: [
        CurrencyService,
        CurrencyViewStore,
        {
          provide: OfferWizardStateService,
          useValue: {
            cropDraft: signal([
              {
                id: 'crop-1',
                species: 'PSZENICA',
                parcels: [
                  {
                    id: 'parcel-1',
                    cropAreaHa: 10,
                    soilClassCode: 'III',
                    county: 'Powiat',
                    locality: 'Miejscowość',
                    precinctNumber: '1',
                    fieldNumber: '2'
                  }
                ]
              }
            ]),
            cropSelectedVariantId: signal('RECOMMENDED'),
            cropSelectedPaymentFrequency: signal('ANNUAL'),
            cropTransportMainPlanEnabled: signal(false),
            cropDiscountAmount: signal(0),
            cropVariantConfigs: () => [],
            setCropSelectedVariantId: () => undefined,
            setCropTransportMainPlanEnabled: () => undefined,
            setCropDiscountAmount,
            setCropSelectedPaymentFrequency: () => undefined,
            setCropVariantConfigs: () => undefined
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

  it('disables foreign discount entry when rates are unavailable', async () => {
    TestBed.resetTestingModule();

    await TestBed.configureTestingModule({
      imports: [CropVariantsStepPageComponent],
      providers: [
        CurrencyService,
        CurrencyViewStore,
        {
          provide: OfferWizardStateService,
          useValue: {
            cropDraft: signal([
              {
                id: 'crop-1',
                species: 'PSZENICA',
                parcels: [{ id: 'parcel-1', cropAreaHa: 10, soilClassCode: 'III' }]
              }
            ]),
            cropSelectedVariantId: signal('RECOMMENDED'),
            cropSelectedPaymentFrequency: signal('ANNUAL'),
            cropTransportMainPlanEnabled: signal(false),
            cropDiscountAmount: signal(0),
            cropVariantConfigs: () => [],
            setCropSelectedVariantId: () => undefined,
            setCropTransportMainPlanEnabled: () => undefined,
            setCropDiscountAmount: () => undefined,
            setCropSelectedPaymentFrequency: () => undefined,
            setCropVariantConfigs: () => undefined
          }
        },
        {
          provide: ExchangeRatesRepository,
          useValue: {
            getExchangeRates: () => {
              throw new Error('NBP unavailable');
            }
          }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(CropVariantsStepPageComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const eurButton = Array.from(fixture.nativeElement.querySelectorAll('.currency-amount-input__currency')).find(
      (button: HTMLButtonElement) => button.textContent?.trim() === 'EUR'
    ) as HTMLButtonElement;

    expect(eurButton.disabled).toBeTrue();
  });

  it('converts USD discount input to PLN for crop flow', async () => {
    const fixture = TestBed.createComponent(CropVariantsStepPageComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const usdButton = Array.from(fixture.nativeElement.querySelectorAll('.currency-amount-input__currency')).find(
      (button: HTMLButtonElement) => button.textContent?.trim() === 'USD'
    ) as HTMLButtonElement;
    usdButton.click();
    fixture.detectChanges();

    const input = fixture.nativeElement.querySelector('.currency-amount-input__field input') as HTMLInputElement;
    input.value = '10';
    input.dispatchEvent(new Event('input'));
    input.dispatchEvent(new Event('blur'));
    fixture.detectChanges();

    expect(setCropDiscountAmount).toHaveBeenCalledWith(39);
  });
});
