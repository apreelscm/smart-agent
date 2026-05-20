import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { CurrencyRatesData, CurrencyRatesService } from '../../../../core/services/currency-rates.service';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { Step15CoverageComponent } from './step-15-coverage.component';

describe('Step15CoverageComponent', () => {
  const mockRates: CurrencyRatesData = {
    table: 'A',
    tableNumber: '074/A/NBP/2026',
    effectiveDate: '2026-05-20',
    rates: {
      USD: 3,
      EUR: 4.5,
    },
  };

  it('defaults to PLN and recalculates visible prices with 2 decimals after currency change', () => {
    const { fixture, component } = setup(of(mockRates));
    const nativeElement = fixture.nativeElement as HTMLElement;
    const select = nativeElement.querySelector('#presentationCurrency') as HTMLSelectElement;

    expect(component.selectedCurrency).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(nativeElement.querySelector('.currency-meta')?.textContent).toContain('074/A/NBP/2026');
    expect(nativeElement.querySelector('.currency-meta')?.textContent).toContain('20.05.2026');

    component.onCurrencyChange('USD');
    fixture.detectChanges();

    const prices = Array.from(nativeElement.querySelectorAll('.cov-price')).map(element =>
      element.textContent?.trim()
    );

    expect(prices).toContain('96,33 USD');
    expect(prices).toContain('50,00 USD');
    expect(nativeElement.querySelector('.currency-meta')?.textContent).toContain('1 USD = 3,0000 PLN');
    expect((nativeElement.querySelector('.price-total') as HTMLElement).textContent?.trim()).toBe(
      '348,67 USD'
    );
  });

  it('keeps PLN selected and disables foreign options when rates cannot be loaded', () => {
    const { fixture, component } = setup(
      throwError(() => new Error('NBP unavailable'))
    );
    const nativeElement = fixture.nativeElement as HTMLElement;
    const usdOption = nativeElement.querySelector('option[value="USD"]') as HTMLOptionElement;
    const eurOption = nativeElement.querySelector('option[value="EUR"]') as HTMLOptionElement;

    expect(component.selectedCurrency).toBe('PLN');
    expect(usdOption.disabled).toBeTrue();
    expect(eurOption.disabled).toBeTrue();

    component.onCurrencyChange('USD');
    fixture.detectChanges();

    expect(component.selectedCurrency).toBe('PLN');
    expect(nativeElement.querySelector('.currency-error')?.textContent).toContain(
      'Kursy walut są obecnie niedostępne'
    );
  });

  it('persists total premium in PLN when proceeding from a foreign-currency view', () => {
    const { component, draft, routerSpy } = setup(of(mockRates));

    component.onCurrencyChange('USD');
    component.next();

    expect(draft.coverages().totalPremium).toBe(component.baseTotal);
    expect(draft.coverages().totalPremium).toBe(1046);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/kalkulator/dane-polisowe']);
  });

  function setup(rates$ = of(mockRates)) {
    TestBed.resetTestingModule();

    const routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);
    routerSpy.navigate.and.returnValue(Promise.resolve(true));

    TestBed.configureTestingModule({
      imports: [Step15CoverageComponent],
      providers: [
        PolicyDraftService,
        { provide: CurrencyRatesService, useValue: { getRates: () => rates$ } },
        { provide: Router, useValue: routerSpy },
      ],
    });

    const fixture = TestBed.createComponent(Step15CoverageComponent);
    fixture.detectChanges();

    return {
      fixture,
      component: fixture.componentInstance,
      draft: TestBed.inject(PolicyDraftService),
      routerSpy,
    };
  }
});
