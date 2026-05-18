import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { KwitariuszeComponent } from './kwitariusze';

describe('KwitariuszeComponent', () => {
  let service: KwitariuszService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KwitariuszeComponent],
      providers: [provideRouter([]), provideNoopAnimations()],
    }).compileComponents();

    service = TestBed.inject(KwitariuszService);
  });

  async function createComponent() {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
    return fixture;
  }

  function getHeaderTexts(element: HTMLElement): string[] {
    return Array.from(element.querySelectorAll('th.mat-mdc-header-cell'))
      .map((cell) => cell.textContent?.replace(/\s+/g, ' ').trim() ?? '');
  }

  it('should place status directly before amount in displayedColumns', () => {
    const fixture = TestBed.createComponent(KwitariuszeComponent);
    const component = fixture.componentInstance;

    expect(component.displayedColumns).toEqual([
      'type',
      'number',
      'policyNumber',
      'insuredName',
      'issueDate',
      'status',
      'amount',
      'actions',
    ]);
    expect(component.displayedColumns.indexOf('status')).toBe(component.displayedColumns.indexOf('amount') - 1);
    expect(component.displayedColumns.filter((column) => column === 'status').length).toBe(1);
    expect(component.displayedColumns.filter((column) => column === 'amount').length).toBe(1);
  });

  it('should render Status header before Kwota (z odsetkami) without duplicates', async () => {
    service.kwitariusze.set([]);

    const fixture = await createComponent();
    const headers = getHeaderTexts(fixture.nativeElement as HTMLElement);

    const statusHeaders = headers.filter((header) => header.includes('Status'));
    const amountHeaders = headers.filter((header) => header.includes('Kwota (z odsetkami)'));
    const statusIndex = headers.findIndex((header) => header.includes('Status'));
    const amountIndex = headers.findIndex((header) => header.includes('Kwota (z odsetkami)'));

    expect(statusHeaders.length).toBe(1);
    expect(amountHeaders.length).toBe(1);
    expect(statusIndex).toBeGreaterThanOrEqual(0);
    expect(amountIndex).toBeGreaterThanOrEqual(0);
    expect(statusIndex).toBe(amountIndex - 1);
  });
});
