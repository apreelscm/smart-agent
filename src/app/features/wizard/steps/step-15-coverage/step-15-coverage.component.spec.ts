import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import {
  ExchangeRateResult,
  ExchangeRateService,
  PresentationCurrency,
} from '../../../../core/services/exchange-rate.service';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { Step15CoverageComponent } from './step-15-coverage.component';

describe('Step15CoverageComponent', () => {
  let fixture: ComponentFixture<Step15CoverageComponent>;
  let component: Step15CoverageComponent;
  let draftService: PolicyDraftService;
  let router: jasmine.SpyObj<Router>;
  let exchangeRateService: jasmine.SpyObj<ExchangeRateService>;

  beforeEach(async () => {
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);
    exchangeRateService = jasmine.createSpyObj<ExchangeRateService>('ExchangeRateService', [
      'getRate',
    ]);

    exchangeRateService.getRate.and.callFake((currency: PresentationCurrency) =>
      of(mockRate(currency))
    );

    await TestBed.configureTestingModule({
      imports: [Step15CoverageComponent],
      providers: [
        PolicyDraftService,
        { provide: Router, useValue: router },
        { provide: ExchangeRateService, useValue: exchangeRateService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Step15CoverageComponent);
    component = fixture.componentInstance;
    draftService = TestBed.inject(PolicyDraftService);
    fixture.detectChanges();
  });

  it('should default to PLN and render PLN prices on initial load', () => {
    const host = fixture.nativeElement as HTMLElement;
    const selector = host.querySelector('#coverage-currency') as HTMLSelectElement;
    const firstPrice = host.querySelector('.cov-price') as HTMLElement;
    const totalPrice = host.querySelector('.price-total') as HTMLElement;

    expect(selector.value).toBe('PLN');
    expect(exchangeRateService.getRate).not.toHaveBeenCalled();
    expect(firstPrice.textContent?.trim()).toBe('289.00 PLN');
    expect(totalPrice.textContent?.trim()).toBe('1046.00 PLN');
  });

  it('should convert all visible prices to USD and round to 2 decimals', () => {
    component.onCurrencyChange('USD');
    fixture.detectChanges();

    const host = fixture.nativeElement as HTMLElement;
    const allPrices = Array.from(host.querySelectorAll('.cov-price')).map(element =>
      element.textContent?.trim() ?? ''
    );
    const totalPrice = host.querySelector('.price-total') as HTMLElement;
    const rateInfo = host.querySelector('.rate-info') as HTMLElement;

    expect(exchangeRateService.getRate).toHaveBeenCalledWith('USD');
    expect(allPrices.length).toBe(7);
    expect(allPrices.every(price => price.endsWith('USD'))).toBeTrue();
    expect(allPrices[0]).toBe('70.08 USD');
    expect(totalPrice.textContent?.trim()).toBe('253.67 USD');
    expect(rateInfo.textContent?.trim()).toContain('2026-04-17');
  });

  it('should persist totalPremium in PLN when moving to the next step', () => {
    const patchCoveragesSpy = spyOn(draftService, 'patchCoverages').and.callThrough();

    component.onCurrencyChange('USD');
    fixture.detectChanges();
    component.next();

    expect(patchCoveragesSpy).toHaveBeenCalledWith(
      jasmine.objectContaining({
        totalPremium: 1046,
      })
    );
    expect(patchCoveragesSpy.calls.mostRecent().args[0].totalPremium).not.toBe(
      component.convertAmount(component.total)
    );
    expect(router.navigate).toHaveBeenCalledWith(['/kalkulator/dane-polisowe']);
  });
});

function mockRate(currency: PresentationCurrency): ExchangeRateResult {
  if (currency === 'USD') {
    return {
      currency: 'USD',
      rate: 4.1234,
      effectiveDate: '2026-04-17',
      sourceTable: 'A',
    };
  }

  if (currency === 'EUR') {
    return {
      currency: 'EUR',
      rate: 4.2941,
      effectiveDate: '2026-04-17',
      sourceTable: 'A',
    };
  }

  return {
    currency: 'PLN',
    rate: 1,
    effectiveDate: null,
    sourceTable: null,
  };
}
