import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { KwitariuszeComponent } from './kwitariusze';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';

describe('KwitariuszeComponent', () => {
  let fixture: ComponentFixture<KwitariuszeComponent>;
  let component: KwitariuszeComponent;
  let kwitariuszService: KwitariuszService;
  let nbpExchangeRateService: jasmine.SpyObj<NbpExchangeRateService>;

  beforeEach(async () => {
    nbpExchangeRateService = jasmine.createSpyObj<NbpExchangeRateService>('NbpExchangeRateService', ['getRate']);

    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        KwitariuszService,
        { provide: NbpExchangeRateService, useValue: nbpExchangeRateService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(KwitariuszeComponent);
    component = fixture.componentInstance;
    kwitariuszService = TestBed.inject(KwitariuszService);

    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  });

  it('should default to PLN on first load', () => {
    const firstRow = kwitariuszService.filtered()[0];
    const select = fixture.nativeElement.querySelector('#displayCurrency') as HTMLSelectElement;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(getFirstRowTotal()).toBe(component.formatAmount(component.totalAmount(firstRow)));
    expect(getFirstRowInterest()).toBe(`odsetki: ${component.formatAmount(firstRow.interest)}`);
  });

  it('should convert amounts to USD and EUR and restore PLN values', async () => {
    const firstRow = kwitariuszService.filtered()[0];

    nbpExchangeRateService.getRate.and.callFake(currency => {
      if (currency === 'USD') {
        return of({
          code: 'USD',
          rate: 4,
          effectiveDate: '2026-04-17',
        });
      }

      return of({
        code: 'EUR',
        rate: 4.5,
        effectiveDate: '2026-04-17',
      });
    });

    await component.changeCurrency('USD');
    fixture.detectChanges();

    expect(component.selectedCurrency()).toBe('USD');
    expect(getFirstRowTotal()).toBe(component.formatAmount(component.totalAmount(firstRow)));
    expect(getFirstRowInterest()).toBe(`odsetki: ${component.formatAmount(firstRow.interest)}`);

    await component.changeCurrency('EUR');
    fixture.detectChanges();

    expect(component.selectedCurrency()).toBe('EUR');
    expect(getFirstRowTotal()).toBe(component.formatAmount(component.totalAmount(firstRow)));
    expect(getFirstRowInterest()).toBe(`odsetki: ${component.formatAmount(firstRow.interest)}`);

    await component.changeCurrency('PLN');
    fixture.detectChanges();

    expect(component.selectedCurrency()).toBe('PLN');
    expect(getFirstRowTotal()).toBe(component.formatAmount(component.totalAmount(firstRow)));
    expect(getFirstRowInterest()).toBe(`odsetki: ${component.formatAmount(firstRow.interest)}`);
    expect(nbpExchangeRateService.getRate.calls.count()).toBe(2);
  });

  it('should keep the previous currency and amounts when rate loading fails', async () => {
    const usdRate = {
      code: 'USD' as const,
      rate: 4,
      effectiveDate: '2026-04-17',
    };

    nbpExchangeRateService.getRate.and.callFake(currency => {
      if (currency === 'USD') {
        return of(usdRate);
      }

      return throwError(() => new Error('NBP unavailable'));
    });

    await component.changeCurrency('USD');
    fixture.detectChanges();

    const previousTotal = getFirstRowTotal();
    const previousInterest = getFirstRowInterest();

    await component.changeCurrency('EUR');
    fixture.detectChanges();

    const errorBanner = fixture.nativeElement.querySelector('.alert--error') as HTMLElement;

    expect(component.selectedCurrency()).toBe('USD');
    expect(getFirstRowTotal()).toBe(previousTotal);
    expect(getFirstRowInterest()).toBe(previousInterest);
    expect(component.currencyError()).toBe('Nie udało się pobrać kursu NBP. Wyświetlane kwoty pozostały bez zmian.');
    expect(errorBanner.textContent).toContain('Nie udało się pobrać kursu NBP');
  });

  it('should keep existing filter toggles and result counts working', () => {
    expect(kwitariuszService.resultCount()).toBe(10);

    component.setType('rata-odsetki');
    fixture.detectChanges();

    expect(kwitariuszService.resultCount()).toBe(6);

    component.toggleFilter('policy');
    fixture.detectChanges();

    expect(component.expandedFilter()).toBe('policy');

    kwitariuszService.filterPolicySearch.set('P064');
    fixture.detectChanges();

    expect(kwitariuszService.resultCount()).toBe(2);

    component.clearFilters();
    fixture.detectChanges();

    expect(component.expandedFilter()).toBeNull();
    expect(kwitariuszService.resultCount()).toBe(10);
    expect((fixture.nativeElement.querySelector('.results-count') as HTMLElement).textContent).toContain('10');
  });

  function getFirstRowTotal(): string {
    return ((fixture.nativeElement.querySelector('.cell-amount__total') as HTMLElement)?.textContent ?? '').trim();
  }

  function getFirstRowInterest(): string {
    return ((fixture.nativeElement.querySelector('.cell-amount__interest') as HTMLElement)?.textContent ?? '')
      .replace(/\s+/g, ' ')
      .trim();
  }
});
