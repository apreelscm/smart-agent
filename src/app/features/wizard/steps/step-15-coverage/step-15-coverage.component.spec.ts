import { of, throwError } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { ExchangeRateService } from '../../../../core/services/exchange-rate.service';
import { Step15CoverageComponent } from './step-15-coverage.component';

describe('Step15CoverageComponent', () => {
  let fixture: ComponentFixture<Step15CoverageComponent>;
  let component: Step15CoverageComponent;
  let exchangeRateService: jasmine.SpyObj<ExchangeRateService>;

  beforeEach(async () => {
    exchangeRateService = jasmine.createSpyObj<ExchangeRateService>('ExchangeRateService', ['getRate']);

    await TestBed.configureTestingModule({
      imports: [Step15CoverageComponent],
      providers: [
        PolicyDraftService,
        { provide: ExchangeRateService, useValue: exchangeRateService },
        { provide: Router, useValue: jasmine.createSpyObj('Router', ['navigate']) },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Step15CoverageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should default to PLN without loading exchange-rate metadata', () => {
    const nativeElement: HTMLElement = fixture.nativeElement;
    const select = nativeElement.querySelector('#coverage-currency') as HTMLSelectElement;

    expect(component.appliedCurrency).toBe('PLN');
    expect(select.value).toBe('PLN');
    expect(exchangeRateService.getRate).not.toHaveBeenCalled();
    expect(nativeElement.querySelector('.currency-meta')).toBeNull();
    expect(nativeElement.textContent).toContain('289.00 PLN');
    expect(nativeElement.textContent).toContain('1046.00 PLN');
  });

  it('should apply a fetched USD rate to all displayed values and show metadata', () => {
    exchangeRateService.getRate.and.returnValue(
      of({
        currency: 'USD',
        code: 'USD',
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        mid: 4,
      })
    );

    component.onCurrencySelected('USD');
    fixture.detectChanges();

    const nativeElement: HTMLElement = fixture.nativeElement;
    const select = nativeElement.querySelector('#coverage-currency') as HTMLSelectElement;

    expect(exchangeRateService.getRate).toHaveBeenCalledWith('USD');
    expect(component.appliedCurrency).toBe('USD');
    expect(select.value).toBe('USD');
    expect(nativeElement.textContent).toContain('72.25 USD');
    expect(nativeElement.textContent).toContain('261.50 USD');
    expect(nativeElement.textContent).toContain('1 USD = 4.0000 PLN');
    expect(nativeElement.textContent).toContain('A / 074/A/NBP/2026');
    expect(nativeElement.textContent).toContain('2026-04-17');
  });

  it('should keep the last successful currency view when a later lookup fails', () => {
    exchangeRateService.getRate.and.returnValue(
      of({
        currency: 'USD',
        code: 'USD',
        table: 'A',
        no: '074/A/NBP/2026',
        effectiveDate: '2026-04-17',
        mid: 4,
      })
    );

    component.onCurrencySelected('USD');
    fixture.detectChanges();

    exchangeRateService.getRate.and.returnValue(
      throwError(() => new Error('NBP unavailable'))
    );

    component.onCurrencySelected('EUR');
    fixture.detectChanges();

    const nativeElement: HTMLElement = fixture.nativeElement;
    const select = nativeElement.querySelector('#coverage-currency') as HTMLSelectElement;

    expect(component.appliedCurrency).toBe('USD');
    expect(select.value).toBe('USD');
    expect(nativeElement.textContent).toContain('72.25 USD');
    expect(nativeElement.textContent).toContain('261.50 USD');
    expect(nativeElement.textContent).toContain(
      'Nie udało się pobrać kursu waluty. Pozostawiliśmy ostatnio wyświetlone wartości.'
    );
  });
});
