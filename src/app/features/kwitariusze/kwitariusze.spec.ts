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

  it('should default to PLN and empty status selection on first load', () => {
    const firstRow = kwitariuszService.filtered()[0];
    const select = fixture.nativeElement.querySelector('#displayCurrency') as HTMLSelectElement;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(component.activeStatusCount).toBe(0);
    expect(component.statusTriggerLabel).toBe('Status');
    expect(kwitariuszService.filterStatuses()).toEqual([]);
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

  it('should filter rows by a single selected status', () => {
    component.toggleStatus('oplacony');
    fixture.detectChanges();

    expect(component.activeStatusCount).toBe(1);
    expect(component.statusTriggerLabel).toBe('Status (1)');
    expect(kwitariuszService.resultCount()).toBe(3);
    expect(kwitariuszService.filtered().every(kwitariusz => kwitariusz.status === 'oplacony')).toBeTrue();
  });

  it('should filter rows by multiple selected statuses', () => {
    component.toggleStatus('oplacony');
    component.toggleStatus('oczekujacy');
    fixture.detectChanges();

    const statuses = new Set(kwitariuszService.filtered().map(kwitariusz => kwitariusz.status));

    expect(component.activeStatusCount).toBe(2);
    expect(component.statusTriggerLabel).toBe('Status (2)');
    expect(kwitariuszService.resultCount()).toBe(4);
    expect(Array.from(statuses)).toEqual(['oplacony', 'oczekujacy']);
  });

  it('should derive available statuses from the non-status filtered dataset', () => {
    component.setType('rekalkulacja-odsetki');
    fixture.detectChanges();

    expect(kwitariuszService.availableStatuses()).toEqual(['oplacony', 'wystawiony']);

    kwitariuszService.filterPolicySearch.set('S0032336');
    fixture.detectChanges();

    expect(kwitariuszService.availableStatuses()).toEqual(['oplacony']);
  });

  it('should reconcile selected statuses that disappear after other filters change', () => {
    component.toggleStatus('anulowany');
    fixture.detectChanges();

    expect(kwitariuszService.filterStatuses()).toEqual(['anulowany']);

    component.setType('rekalkulacja-odsetki');
    fixture.detectChanges();

    expect(kwitariuszService.availableStatuses()).toEqual(['oplacony', 'wystawiony']);
    expect(kwitariuszService.filterStatuses()).toEqual([]);
    expect(component.activeStatusCount).toBe(0);
  });

  it('should clear status selection with the global reset action', () => {
    component.toggleStatus('oplacony');
    component.toggleFilter('status');
    kwitariuszService.filterPolicySearch.set('P064');
    fixture.detectChanges();

    expect(component.hasAnyFilter).toBeTrue();

    component.clearFilters();
    fixture.detectChanges();

    expect(component.expandedFilter()).toBeNull();
    expect(component.activeStatusCount).toBe(0);
    expect(kwitariuszService.filterStatuses()).toEqual([]);
    expect(kwitariuszService.resultCount()).toBe(10);
    expect((fixture.nativeElement.querySelector('.results-count') as HTMLElement).textContent).toContain('10');
  });

  it('should clear the status filter when the component is created again', async () => {
    kwitariuszService.filterStatuses.set(['oplacony']);

    const secondFixture = TestBed.createComponent(KwitariuszeComponent);
    const secondComponent = secondFixture.componentInstance;

    secondFixture.detectChanges();
    await secondFixture.whenStable();
    secondFixture.detectChanges();

    expect(secondComponent.service.filterStatuses()).toEqual([]);
    expect(secondComponent.activeStatusCount).toBe(0);
    secondFixture.destroy();
  });

  it('should show a no-data row when filters remove all rows', () => {
    component.toggleStatus('oplacony');
    kwitariuszService.filterPolicySearch.set('BRAK-WYNIKOW');
    fixture.detectChanges();

    const noDataRow = fixture.nativeElement.querySelector('.no-data-row') as HTMLElement;

    expect(kwitariuszService.resultCount()).toBe(0);
    expect(noDataRow).not.toBeNull();
    expect(noDataRow.textContent).toContain('Brak kwitariuszy spełniających wybrane filtry.');
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
