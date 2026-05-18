import { TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { AgentService } from '../../core/services/agent.service';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { KwitariuszeComponent } from './kwitariusze';

describe('KwitariuszeComponent', () => {
  const storageKey = (userKey: string) => `kwitariusze.status-filter.${userKey}`;

  const setAuthCurrentUser = (user: unknown): void => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  };

  const createComponent = () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    fixture.detectChanges();
    return fixture;
  };

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent, NoopAnimationsModule],
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
});
