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

  const renderedColumnName = (element: Element): string | null => {
    const className = Array.from(element.classList).find(name =>
      name.startsWith('mat-column-') || name.startsWith('cdk-column-'),
    );

    if (!className) {
      return null;
    }

    return className.replace(/^mat-column-/, '').replace(/^cdk-column-/, '');
  };

  const getHeaderCells = (fixture: ReturnType<typeof createComponent>): HTMLTableCellElement[] =>
    Array.from(fixture.nativeElement.querySelectorAll('tr.mat-mdc-header-row th'));

  const getHeaderColumnOrder = (fixture: ReturnType<typeof createComponent>): string[] =>
    getHeaderCells(fixture)
      .map(cell => renderedColumnName(cell))
      .filter((column): column is string => column !== null);

  const getFirstRowCells = (fixture: ReturnType<typeof createComponent>): HTMLTableCellElement[] => {
    const firstRow = fixture.nativeElement.querySelector('tr.mat-mdc-row');

    return firstRow ? Array.from(firstRow.querySelectorAll('td')) : [];
  };

  const getFirstRowColumnOrder = (fixture: ReturnType<typeof createComponent>): string[] =>
    getFirstRowCells(fixture)
      .map(cell => renderedColumnName(cell))
      .filter((column): column is string => column !== null);

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

  it('renders the header with status directly before amount', () => {
    const fixture = createComponent();
    const headerCells = getHeaderCells(fixture);
    const expectedOrder = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'status', 'amount', 'actions'];

    expect(getHeaderColumnOrder(fixture)).toEqual(expectedOrder);

    const statusHeader = headerCells.find(cell => renderedColumnName(cell) === 'status') ?? null;
    const amountHeader = headerCells.find(cell => renderedColumnName(cell) === 'amount') ?? null;

    expect(textContent(statusHeader)).toBe('Status');
    expect(textContent(amountHeader)).toBe('Kwota (z odsetkami)');
  });

  it('renders each row with the status cell directly before the amount cell', () => {
    const fixture = createComponent();
    const rowCells = getFirstRowCells(fixture);
    const expectedOrder = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'status', 'amount', 'actions'];

    expect(getFirstRowColumnOrder(fixture)).toEqual(expectedOrder);

    const statusCell = rowCells.find(cell => renderedColumnName(cell) === 'status') ?? null;
    const amountCell = rowCells.find(cell => renderedColumnName(cell) === 'amount') ?? null;

    expect(statusCell?.querySelector('.status-badge')).not.toBeNull();
    expect(amountCell?.querySelector('.cell-amount__total')).not.toBeNull();
    expect(amountCell?.querySelector('.cell-amount__interest')).not.toBeNull();
  });

  it('starts in PLN, hides rate metadata, and does not fetch rates on init', () => {
    const fixture = createComponent();
    const component = fixture.componentInstance;

    expect(component.selectedCurrency()).toBe('PLN');
    expect(component.rateInfo()).toBeNull();
    expect(nbpExchangeRateService.getLatestOrPreviousRate).not.toHaveBeenCalled();
    expect(fixture.nativeElement.querySelector('.currency-banner--info')).toBeNull();
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
