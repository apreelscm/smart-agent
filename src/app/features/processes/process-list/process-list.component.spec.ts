import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { ProcessListComponent } from './process-list.component';
import { ProcessListService } from './process-list.service';

describe('ProcessListComponent', () => {
  let fixture: ComponentFixture<ProcessListComponent>;
  let component: ProcessListComponent;
  let processListService: ProcessListService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessListComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ProcessListComponent);
    component = fixture.componentInstance;
    processListService = TestBed.inject(ProcessListService);
  });

  async function renderComponent(): Promise<void> {
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  async function submitFilters(): Promise<void> {
    const form: HTMLFormElement = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  }

  function queryRows(): HTMLTableRowElement[] {
    return Array.from(fixture.nativeElement.querySelectorAll('tbody tr'));
  }

  function queryHeaderTexts(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('thead th')).map((header) =>
      header.textContent?.replace(/\s+/g, ' ').trim() ?? '',
    );
  }

  function setInputValue(selector: string, value: string): void {
    const input: HTMLInputElement = fixture.nativeElement.querySelector(selector);
    input.value = value;
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  function setCheckboxValue(selector: string, checked: boolean): void {
    const input: HTMLInputElement = fixture.nativeElement.querySelector(selector);
    input.checked = checked;
    input.dispatchEvent(new Event('change'));
    fixture.detectChanges();
  }

  function setSelectValue(selector: string, value: string): void {
    const select: HTMLSelectElement = fixture.nativeElement.querySelector(selector);
    select.value = value;
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();
  }

  it('loads the default process list on init', async () => {
    await renderComponent();

    expect(component.items.length).toBe(10);
    expect(component.total).toBeGreaterThan(10);
    expect(component.query.page).toBe(1);
    expect(component.query.size).toBe(10);
    expect(component.query.sort).toBe('id');
    expect(component.query.order).toBe('desc');
    expect(queryRows().length).toBe(10);
    expect(fixture.nativeElement.textContent).toContain('Lista procesów');
  });

  it('renders the PH header immediately before the Email PH header', async () => {
    await renderComponent();

    const headers = queryHeaderTexts();
    const phHeaderIndex = headers.indexOf('PH');
    const phEmailHeaderIndex = headers.indexOf('Email PH');

    expect(phHeaderIndex).toBeGreaterThan(-1);
    expect(phEmailHeaderIndex).toBeGreaterThan(-1);
    expect(phEmailHeaderIndex).toBe(phHeaderIndex + 1);
  });

  it('renders phName immediately before phEmail in each process row', async () => {
    await renderComponent();

    const firstRow = queryRows()[0];
    const firstItem = component.items[0];
    const rowValues = Array.from(firstRow.cells).map((cell) => cell.textContent?.trim() ?? '');
    const phNameIndex = rowValues.indexOf(firstItem.phName);
    const phEmailIndex = rowValues.indexOf(firstItem.phEmail);

    expect(phNameIndex).toBeGreaterThan(-1);
    expect(phEmailIndex).toBeGreaterThan(-1);
    expect(phEmailIndex).toBe(phNameIndex + 1);
    expect(rowValues[phNameIndex]).toBe(firstItem.phName);
    expect(rowValues[phEmailIndex]).toBe(firstItem.phEmail);
  });

  it('applies filters and narrows results with AND logic', async () => {
    await renderComponent();

    setInputValue('[data-testid="nip-filter"]', '1234567890');
    setCheckboxValue('[data-testid="observed-filter"]', true);

    await submitFilters();

    expect(component.total).toBe(1);
    expect(queryRows().length).toBe(1);
    expect(fixture.nativeElement.textContent).toContain('Firma Helios Sp. z o.o.');
  });

  it('clears filters and restores the default list state', async () => {
    await renderComponent();

    setSelectValue('[data-testid="status-filter"]', 'NEW');
    await submitFilters();

    expect(component.total).toBe(1);

    const clearButton: HTMLButtonElement = fixture.nativeElement.querySelector(
      '[data-testid="clear-filters-button"]',
    );
    clearButton.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const statusSelect: HTMLSelectElement = fixture.nativeElement.querySelector(
      '[data-testid="status-filter"]',
    );

    expect(component.query.page).toBe(1);
    expect(component.query.sort).toBe('id');
    expect(component.query.order).toBe('desc');
    expect(statusSelect.value).toBe('WAITING');
    expect(queryRows().length).toBe(10);
  });

  it('changes sorting when a sortable header is clicked', async () => {
    await renderComponent();

    const initialFirstRowId = Number(queryRows()[0].cells[0].textContent?.trim());

    const sortButton: HTMLButtonElement = fixture.nativeElement.querySelector('[data-testid="sort-id"]');
    sortButton.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const updatedFirstRowId = Number(queryRows()[0].cells[0].textContent?.trim());

    expect(component.query.sort).toBe('id');
    expect(component.query.order).toBe('asc');
    expect(updatedFirstRowId).toBeLessThan(initialFirstRowId);
  });

  it('moves to another page while keeping the current query context', async () => {
    await renderComponent();

    const pageButton: HTMLButtonElement = fixture.nativeElement.querySelector('[data-testid="page-2"]');
    pageButton.click();
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    expect(component.query.page).toBe(2);
    expect(queryRows().length).toBe(4);
    expect(queryRows()[0].cells[0].textContent?.trim()).toBe('1008');
  });

  it('shows an empty state when filters produce no matches', async () => {
    await renderComponent();

    setInputValue('[data-testid="ph-number-filter"]', 'PH/2099/99999');

    await submitFilters();

    const emptyState: HTMLElement | null = fixture.nativeElement.querySelector(
      '[data-testid="empty-state"]',
    );

    expect(component.total).toBe(0);
    expect(queryRows().length).toBe(0);
    expect(emptyState?.textContent).toContain('Nie znaleziono procesów');
  });

  it('shows an error state and keeps filter values for retry', async () => {
    await renderComponent();

    setInputValue('[data-testid="nip-filter"]', '1234567890');
    processListService.failNextRequest();

    await submitFilters();

    const errorAlert: HTMLElement | null = fixture.nativeElement.querySelector(
      '[data-testid="error-alert"]',
    );
    const nipInput: HTMLInputElement = fixture.nativeElement.querySelector('[data-testid="nip-filter"]');

    expect(errorAlert?.textContent).toContain('Nie udało się wczytać listy procesów');
    expect(nipInput.value).toBe('1234567890');
  });

  it('renders detail links to the existing process detail route', async () => {
    await renderComponent();

    const detailLink: HTMLAnchorElement = fixture.nativeElement.querySelector(
      '[data-testid="detail-link-1018"]',
    );

    expect(detailLink.getAttribute('href')).toContain('/processes/1018');
    expect(detailLink.textContent).toContain('Szczegóły');
  });
});
