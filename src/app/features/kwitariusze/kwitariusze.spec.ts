import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

import { ExchangeRate } from '../../core/models/exchange-rate.model';
import { NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';
import { KwitariuszeComponent } from './kwitariusze';

describe('KwitariuszeComponent', () => {
  let fixture: ComponentFixture<KwitariuszeComponent>;
  let nbpExchangeRateService: jasmine.SpyObj<NbpExchangeRateService>;

  const eurRate: ExchangeRate = {
    tableType: 'A',
    currencyCode: 'EUR',
    currencyName: 'euro',
    effectiveDate: '2025-04-17',
    midRate: 4.2941,
    sourceTableNo: '074/A/NBP/2025',
    source: 'NBP',
  };

  beforeEach(async () => {
    nbpExchangeRateService = jasmine.createSpyObj<NbpExchangeRateService>('NbpExchangeRateService', ['getTodayRate']);

    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: NbpExchangeRateService, useValue: nbpExchangeRateService },
      ],
    }).compileComponents();
  });

  function createComponent(): ComponentFixture<KwitariuszeComponent> {
    const componentFixture = TestBed.createComponent(KwitariuszeComponent);
    componentFixture.detectChanges();
    return componentFixture;
  }

  function getText(selector: string): string {
    return ((fixture.nativeElement as HTMLElement).querySelector(selector)?.textContent ?? '')
      .replace(/\s+/g, ' ')
      .trim();
  }

  it('should start with PLN selected and without exchange-rate info', async () => {
    fixture = createComponent();
    await fixture.whenStable();
    fixture.detectChanges();

    const select = fixture.nativeElement.querySelector('.currency-switcher__select') as HTMLSelectElement;

    expect(fixture.componentInstance.selectedCurrency()).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(getText('.cell-amount__total')).toBe('328,50 zł');
    expect((fixture.nativeElement as HTMLElement).querySelector('.exchange-rate')).toBeNull();
  });

  it('should render converted EUR amounts and applied NBP rate info', async () => {
    nbpExchangeRateService.getTodayRate.and.returnValue(of(eurRate));

    fixture = createComponent();
    fixture.componentInstance.changeCurrency('EUR');
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const select = fixture.nativeElement.querySelector('.currency-switcher__select') as HTMLSelectElement;

    expect(nbpExchangeRateService.getTodayRate).toHaveBeenCalledOnceWith('EUR');
    expect(fixture.componentInstance.selectedCurrency()).toBe('EUR');
    expect(select.value).toBe('EUR');
    expect(getText('.cell-amount__total')).toBe('76,48 EUR');
    expect(getText('.cell-amount__interest')).toBe('odsetki: 4,31 EUR');

    const rateInfo = getText('.exchange-rate');

    expect(rateInfo).toContain('Kurs EUR');
    expect(rateInfo).toContain('4,2941');
    expect(rateInfo).toContain('2025-04-17');
    expect(rateInfo).toContain('NBP');
  });

  it('should reset currency state to PLN on fresh component creation', async () => {
    nbpExchangeRateService.getTodayRate.and.returnValue(of(eurRate));

    fixture = createComponent();
    fixture.componentInstance.changeCurrency('EUR');
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    fixture.destroy();

    fixture = createComponent();
    await fixture.whenStable();
    fixture.detectChanges();

    const select = fixture.nativeElement.querySelector('.currency-switcher__select') as HTMLSelectElement;

    expect(fixture.componentInstance.selectedCurrency()).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(getText('.cell-amount__total')).toBe('328,50 zł');
    expect((fixture.nativeElement as HTMLElement).querySelector('.exchange-rate')).toBeNull();
  });

  it('should fall back to PLN and show inline error when NBP request fails', async () => {
    nbpExchangeRateService.getTodayRate.and.returnValue(
      throwError(() => new Error('Brak dzisiejszego kursu NBP dla EUR.'))
    );

    fixture = createComponent();
    fixture.componentInstance.changeCurrency('EUR');
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(fixture.componentInstance.selectedCurrency()).toBe('PLN');
    expect(getText('.cell-amount__total')).toBe('328,50 zł');
    expect(getText('.exchange-rate--error')).toContain('Nie udało się pobrać kursu EUR z NBP. Wyświetlono kwoty w PLN.');
    expect((fixture.nativeElement as HTMLElement).textContent).not.toContain('76,48 EUR');
  });
});
