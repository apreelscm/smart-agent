import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

import { CurrencyRate } from '../../core/models/currency-rate.model';
import { CurrencyRateService } from '../../core/services/currency-rate.service';
import { KwitariuszeComponent } from './kwitariusze';

describe('KwitariuszeComponent', () => {
  let currencyRateService: jasmine.SpyObj<CurrencyRateService>;

  beforeEach(async () => {
    currencyRateService = jasmine.createSpyObj<CurrencyRateService>('CurrencyRateService', ['getLatestRate']);
    currencyRateService.getLatestRate.and.callFake((currency: 'USD' | 'EUR') => of(createRate(currency)));

    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: CurrencyRateService, useValue: currencyRateService },
      ],
    }).compileComponents();
  });

  it('should start with PLN and render source amounts without rate metadata', async () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    const component = fixture.componentInstance;

    await detectChanges(fixture);

    const element = fixture.nativeElement as HTMLElement;
    const currencySelect = element.querySelector('#presentationCurrency') as HTMLSelectElement;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(currencySelect.value).toBe('PLN');
    expect(element.querySelector('.currency-panel')).toBeNull();
    expect(textContent(element, '.cell-amount__total')).toBe('328,50 zł');
    expect(textContent(element, '.cell-amount__interest')).toBe('odsetki: 18,50 zł');
    expect(currencyRateService.getLatestRate).not.toHaveBeenCalled();
  });

  it('should fetch USD rate and render converted amounts with metadata', async () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    const component = fixture.componentInstance;

    await detectChanges(fixture);

    component.onCurrencyChange('USD');
    await detectChanges(fixture);

    const element = fixture.nativeElement as HTMLElement;

    expect(currencyRateService.getLatestRate).toHaveBeenCalledWith('USD');
    expect(component.selectedCurrency()).toBe('USD');
    expect(textContent(element, '.cell-amount__total')).toBe('82,13 USD');
    expect(textContent(element, '.cell-amount__interest')).toBe('odsetki: 4,63 USD');
    expect(textContent(element, '.currency-panel')).toContain('1 USD = 4,0000 PLN');
    expect(textContent(element, '.currency-panel')).toContain('074/A/NBP/2026');
    expect(textContent(element, '.currency-panel')).toContain('2026-04-17');
  });

  it('should fetch EUR rate and restore PLN values after switching back', async () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    const component = fixture.componentInstance;

    await detectChanges(fixture);

    component.onCurrencyChange('EUR');
    await detectChanges(fixture);

    let element = fixture.nativeElement as HTMLElement;

    expect(currencyRateService.getLatestRate).toHaveBeenCalledWith('EUR');
    expect(textContent(element, '.cell-amount__total')).toBe('74,66 EUR');
    expect(textContent(element, '.currency-panel')).toContain('1 EUR = 4,4000 PLN');

    component.onCurrencyChange('PLN');
    await detectChanges(fixture);

    element = fixture.nativeElement as HTMLElement;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(element.querySelector('.currency-panel')).toBeNull();
    expect(textContent(element, '.cell-amount__total')).toBe('328,50 zł');
    expect(textContent(element, '.cell-amount__interest')).toBe('odsetki: 18,50 zł');
  });

  it('should start a new component instance with PLN and without cached metadata', async () => {
    const firstFixture = TestBed.createComponent(KwitariuszeComponent);
    const firstComponent = firstFixture.componentInstance;

    await detectChanges(firstFixture);

    firstComponent.onCurrencyChange('USD');
    await detectChanges(firstFixture);

    expect(firstComponent.selectedCurrency()).toBe('USD');

    firstFixture.destroy();
    currencyRateService.getLatestRate.calls.reset();

    const secondFixture = TestBed.createComponent(KwitariuszeComponent);
    const secondComponent = secondFixture.componentInstance;

    await detectChanges(secondFixture);

    const secondElement = secondFixture.nativeElement as HTMLElement;
    const currencySelect = secondElement.querySelector('#presentationCurrency') as HTMLSelectElement;

    expect(secondComponent.selectedCurrency()).toBe('PLN');
    expect(currencySelect.value).toBe('PLN');
    expect(secondElement.querySelector('.currency-panel')).toBeNull();
    expect(currencyRateService.getLatestRate).not.toHaveBeenCalled();
  });

  it('should show error state and avoid displaying converted values when rate load fails', async () => {
    currencyRateService.getLatestRate.and.returnValue(
      throwError(() => new Error('NBP unavailable')),
    );

    const fixture = TestBed.createComponent(KwitariuszeComponent);
    const component = fixture.componentInstance;

    await detectChanges(fixture);

    component.onCurrencyChange('USD');
    await detectChanges(fixture);

    const element = fixture.nativeElement as HTMLElement;

    expect(component.selectedCurrency()).toBe('USD');
    expect(textContent(element, '.currency-panel')).toContain('Nie udało się pobrać kursu NBP dla wybranej waluty.');
    expect(textContent(element, '.cell-amount__total')).toBe('—');
    expect(textContent(element, '.cell-amount__interest')).toBe('odsetki: —');
  });

  function createRate(currency: 'USD' | 'EUR'): CurrencyRate {
    if (currency === 'USD') {
      return {
        table: 'A',
        tableNo: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        currencyCode: 'USD',
        currencyName: 'dolar amerykański',
        mid: 4,
      };
    }

    return {
      table: 'A',
      tableNo: '074/A/NBP/2026',
      effectiveDate: '2026-04-17',
      currencyCode: 'EUR',
      currencyName: 'euro',
      mid: 4.4,
    };
  }

  async function detectChanges(fixture: { detectChanges: () => void; whenStable: () => Promise<unknown> }): Promise<void> {
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  function textContent(root: HTMLElement, selector: string): string {
    return root.querySelector(selector)?.textContent?.trim() ?? '';
  }
});
