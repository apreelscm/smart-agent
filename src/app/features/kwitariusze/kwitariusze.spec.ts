import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { ExchangeRateQuote } from '../../core/models/exchange-rate.model';
import { AgentService } from '../../core/services/agent.service';
import { NbpExchangeRateError, NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { KwitariuszeComponent } from './kwitariusze';

describe('KwitariuszeComponent', () => {
  const storageKey = (userKey: string) => `kwitariusze.status-filter.${userKey}`;
  const usdQuote: ExchangeRateQuote = {
    table: 'A',
    currencyCode: 'USD',
    tableNo: '074/A/NBP/2026',
    effectiveDate: '2026-04-17',
    mid: 4,
  };

  let nbpExchangeRateService: jasmine.SpyObj<NbpExchangeRateService>;

  const setAuthCurrentUser = (user: unknown): void => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  };

  const createComponent = () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    fixture.detectChanges();
    return fixture;
  };

  const textContent = (element: Element | null): string => (element?.textContent ?? '').replace(/\s+/g, ' ').trim();

  beforeEach(async () => {
    localStorage.clear();
    nbpExchangeRateService = jasmine.createSpyObj<NbpExchangeRateService>('NbpExchangeRateService', ['getLatestOrPreviousRate']);

    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent, NoopAnimationsModule],
      providers: [
        { provide: NbpExchangeRateService, useValue: nbpExchangeRateService },
      ],
    }).compileComponents();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('restores persisted statuses and applies them to the list on init', () => {
    setAuthCurrentUser({ auwId: 'agent-1', username: 'alpha' });
    localStorage.setItem(storageKey('agent-1'), JSON.stringify(['oplacony']));

    const fixture = createComponent();
    const service = TestBed.inject(KwitariuszService);

    expect(fixture.componentInstance.selectedStatuses).toEqual(['oplacony']);
    expect(service.resultCount()).toBe(3);
    expect(service.filtered().every(kwitariusz => kwitariusz.status === 'oplacony')).toBeTrue();
  });

  it('ignores malformed persisted storage data', () => {
    setAuthCurrentUser({ username: 'alpha' });
    localStorage.setItem(storageKey('alpha'), '{not-valid-json');

    createComponent();
    const service = TestBed.inject(KwitariuszService);

    expect(service.filterStatuses()).toEqual([]);
    expect(service.resultCount()).toBe(10);
    expect(localStorage.getItem(storageKey('alpha'))).toBeNull();
  });

  it('restores only valid statuses and rewrites cleaned storage', () => {
    setAuthCurrentUser({ username: 'alpha' });
    localStorage.setItem(storageKey('alpha'), JSON.stringify(['oplacony', 'nieznany', 'oplacony']));

    createComponent();
    const service = TestBed.inject(KwitariuszService);

    expect(service.filterStatuses()).toEqual(['oplacony']);
    expect(localStorage.getItem(storageKey('alpha'))).toBe(JSON.stringify(['oplacony']));
  });

  it('clears persisted statuses when the status filter or all filters are cleared', () => {
    setAuthCurrentUser({ username: 'alpha' });
    const fixture = createComponent();
    const component = fixture.componentInstance;
    const key = storageKey('alpha');

    component.toggleStatus('wystawiony');
    expect(localStorage.getItem(key)).toBe(JSON.stringify(['wystawiony']));

    component.clearStatusFilter();
    expect(component.selectedStatuses).toEqual([]);
    expect(localStorage.getItem(key)).toBeNull();

    component.toggleStatus('oplacony');
    expect(localStorage.getItem(key)).toBe(JSON.stringify(['oplacony']));

    component.clearFilters();
    expect(component.selectedStatuses).toEqual([]);
    expect(localStorage.getItem(key)).toBeNull();
  });

  it('reconciles unavailable statuses against current options and persists the cleaned value', async () => {
    setAuthCurrentUser({ username: 'alpha' });
    localStorage.setItem(storageKey('alpha'), JSON.stringify(['oczekujacy', 'oplacony']));

    const fixture = createComponent();
    const service = TestBed.inject(KwitariuszService);

    service.filterType.set('rekalkulacja-odsetki');
    fixture.detectChanges();
    await fixture.whenStable();

    expect(service.availableStatuses()).toEqual(['wystawiony', 'oplacony']);
    expect(service.filterStatuses()).toEqual(['oplacony']);
    expect(localStorage.getItem(storageKey('alpha'))).toBe(JSON.stringify(['oplacony']));
  });

  it('isolates persisted statuses per resolved user and falls back to agent username', () => {
    const agentService = TestBed.inject(AgentService);

    setAuthCurrentUser({ username: 'user-one' });
    let fixture = createComponent();
    fixture.componentInstance.toggleStatus('oplacony');

    expect(localStorage.getItem(storageKey('user-one'))).toBe(JSON.stringify(['oplacony']));
    fixture.destroy();

    localStorage.removeItem('auth.currentUser');
    agentService.currentAgent.update(agent => ({ ...agent, username: 'agent-two' }));

    fixture = createComponent();
    const service = TestBed.inject(KwitariuszService);

    expect(service.filterStatuses()).toEqual([]);

    fixture.componentInstance.toggleStatus('wystawiony');

    expect(localStorage.getItem(storageKey('agent-two'))).toBe(JSON.stringify(['wystawiony']));
    expect(localStorage.getItem(storageKey('user-one'))).toBe(JSON.stringify(['oplacony']));
  });

  it('starts in PLN, hides rate metadata, and does not fetch rates on init', () => {
    const fixture = createComponent();
    const component = fixture.componentInstance;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(component.rateInfo()).toBeNull();
    expect(nbpExchangeRateService.getLatestOrPreviousRate).not.toHaveBeenCalled();
    expect(fixture.nativeElement.querySelector('.currency-banner--info')).toBeNull();
  });

  it('renders Status immediately before Kwota (z odsetkami) in the standard table', () => {
    const fixture = createComponent();
    const nativeElement = fixture.nativeElement as HTMLElement;
    const headerCells = Array.from(nativeElement.querySelectorAll('tr.mat-mdc-header-row th.mat-mdc-header-cell'));

    const columnOrder = headerCells.map(cell => {
      const columnClass = Array.from(cell.classList).find(className => className.startsWith('mat-column-'));
      return columnClass?.replace('mat-column-', '') ?? '';
    });

    const statusHeader = nativeElement.querySelector('th.mat-column-status');
    const amountHeader = nativeElement.querySelector('th.mat-column-amount');
    const firstRowStatus = nativeElement.querySelector('td.mat-column-status .status-badge');
    const firstRowAmountTotal = nativeElement.querySelector('td.mat-column-amount .cell-amount__total');
    const firstRowAmountInterest = nativeElement.querySelector('td.mat-column-amount .cell-amount__interest');

    expect(columnOrder).toEqual(['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'status', 'amount', 'actions']);
    expect(columnOrder.indexOf('status')).toBe(columnOrder.indexOf('amount') - 1);
    expect(textContent(statusHeader)).toBe('Status');
    expect(textContent(amountHeader)).toContain('Kwota (z odsetkami)');
    expect(textContent(firstRowStatus)).not.toBe('');
    expect(textContent(firstRowAmountTotal)).toContain('PLN');
    expect(textContent(firstRowAmountInterest)).toContain('odsetki:');
  });

  it('converts visible totals and interest amounts after a successful USD selection', () => {
    nbpExchangeRateService.getLatestOrPreviousRate.and.returnValue(of(usdQuote));

    const fixture = createComponent();
    const component = fixture.componentInstance;
    const service = TestBed.inject(KwitariuszService);

    service.filterType.set('rata-odsetki');
    fixture.detectChanges();

    const resultCountBeforeChange = service.resultCount();

    component.changeCurrency('USD');
    fixture.detectChanges();

    const nativeElement = fixture.nativeElement as HTMLElement;

    expect(nbpExchangeRateService.getLatestOrPreviousRate).toHaveBeenCalledOnceWith('USD');
    expect(component.selectedCurrency()).toBe('USD');
    expect(component.rateInfo()).toEqual(usdQuote);
    expect(service.filterType()).toBe('rata-odsetki');
    expect(service.resultCount()).toBe(resultCountBeforeChange);
    expect(textContent(nativeElement.querySelector('.cell-amount__total'))).toContain('82,13 USD');
    expect(textContent(nativeElement.querySelector('.cell-amount__interest'))).toContain('odsetki: 4,63 USD');
    expect(textContent(nativeElement.querySelector('.currency-banner--info'))).toContain('1 USD = 4,0000 PLN');
    expect(textContent(nativeElement.querySelector('.currency-banner--info'))).toContain('074/A/NBP/2026');
    expect(textContent(nativeElement.querySelector('.currency-banner--info'))).toContain('2026-04-17');
  });

  it('keeps the last committed presentation and shows an error when a new quote lookup fails', () => {
    nbpExchangeRateService.getLatestOrPreviousRate.and.returnValues(
      of(usdQuote),
      throwError(() => new NbpExchangeRateError('EUR', 'lookup-failed', 'failed')),
    );

    const fixture = createComponent();
    const component = fixture.componentInstance;
    const nativeElement = fixture.nativeElement as HTMLElement;

    component.changeCurrency('USD');
    fixture.detectChanges();
    expect(textContent(nativeElement.querySelector('.cell-amount__total'))).toContain('82,13 USD');

    component.changeCurrency('EUR');
    fixture.detectChanges();

    expect(component.selectedCurrency()).toBe('USD');
    expect(component.rateInfo()).toEqual(usdQuote);
    expect(textContent(nativeElement.querySelector('.cell-amount__total'))).toContain('82,13 USD');
    expect(textContent(nativeElement.querySelector('.currency-banner--error'))).toContain('Nie udało się pobrać kursu NBP dla EUR');
  });

  it('switches back to PLN without another rate request and clears metadata', () => {
    nbpExchangeRateService.getLatestOrPreviousRate.and.returnValue(of(usdQuote));

    const fixture = createComponent();
    const component = fixture.componentInstance;
    const nativeElement = fixture.nativeElement as HTMLElement;

    component.changeCurrency('USD');
    fixture.detectChanges();

    component.changeCurrency('PLN');
    fixture.detectChanges();

    expect(nbpExchangeRateService.getLatestOrPreviousRate).toHaveBeenCalledTimes(1);
    expect(component.selectedCurrency()).toBe('PLN');
    expect(component.rateInfo()).toBeNull();
    expect(textContent(nativeElement.querySelector('.cell-amount__total'))).toContain('328,50 PLN');
    expect(nativeElement.querySelector('.currency-banner--info')).toBeNull();
  });
});
