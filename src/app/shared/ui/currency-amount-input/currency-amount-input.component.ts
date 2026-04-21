import { CommonModule } from '@angular/common';
import { Component, computed, effect, forwardRef, inject, input, signal } from '@angular/core';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { CurrencyPresentationService } from '../../../core/services/currency-presentation.service';

@Component({
  selector: 'app-currency-amount-input',
  imports: [CommonModule, FormsModule, ButtonDirective],
  templateUrl: './currency-amount-input.component.html',
  styleUrl: './currency-amount-input.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CurrencyAmountInputComponent),
      multi: true
    }
  ]
})
export class CurrencyAmountInputComponent implements ControlValueAccessor {
  private readonly currencyPresentation = inject(CurrencyPresentationService);

  readonly step = input<number>(1);
  readonly min = input<number | undefined>(undefined);
  readonly max = input<number | undefined>(undefined);
  readonly ariaLabel = input('Kwota');
  readonly placeholder = input('0');
  readonly showCurrencySelector = input(false);

  protected readonly displayValueState = signal('0');
  protected readonly valueInPlnState = signal(0);
  protected readonly focusedState = signal(false);
  protected readonly disabledState = signal(false);

  protected readonly selectedCurrency = computed(() => this.currencyPresentation.selectedCurrency());
  protected readonly helperText = computed(() => {
    const activeRateInfo = this.currencyPresentation.activeRateInfo();
    const displayAmount = this.parseLocalizedNumber(this.displayValueState());

    if (!activeRateInfo || displayAmount === null) {
      return null;
    }

    const plnAmount = this.currencyPresentation.convertToPln(displayAmount, activeRateInfo.currency);

    return `≈ ${this.currencyPresentation.formatAmount(plnAmount, 'PLN')} po kursie ${this.currencyPresentation.formatRate(activeRateInfo.rate)} z dnia ${this.formatDate(activeRateInfo.effectiveDate)}`;
  });
  protected readonly availabilityMessage = computed(() =>
    !this.currencyPresentation.loading() && !!this.currencyPresentation.degradationMessage()
      ? 'Waluty obce są chwilowo niedostępne. Wpisywanie odbywa się tylko w PLN.'
      : null
  );
  protected readonly displayMin = computed(() => {
    const min = this.min();
    return typeof min === 'number' ? this.currencyPresentation.convertFromPln(min, this.selectedCurrency()) : null;
  });
  protected readonly displayMax = computed(() => {
    const max = this.max();
    return typeof max === 'number' ? this.currencyPresentation.convertFromPln(max, this.selectedCurrency()) : null;
  });

  private onChange: (value: number) => void = () => undefined;
  private onTouched: () => void = () => undefined;

  constructor() {
    effect(() => {
      this.selectedCurrency();

      if (!this.focusedState()) {
        this.displayValueState.set(this.formatInputValue(this.valueInPlnState()));
      }
    });
  }

  writeValue(value: number | null): void {
    this.valueInPlnState.set(Number(value ?? 0));

    if (!this.focusedState()) {
      this.displayValueState.set(this.formatInputValue(this.valueInPlnState()));
    }
  }

  registerOnChange(fn: (value: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabledState.set(isDisabled);
  }

  protected selectCurrency(currency: 'PLN' | 'EUR' | 'USD'): void {
    this.currencyPresentation.selectCurrency(currency);
  }

  protected onFocus(): void {
    this.focusedState.set(true);
  }

  protected onInput(rawValue: string): void {
    this.displayValueState.set(rawValue);

    const parsedAmount = this.parseLocalizedNumber(rawValue);

    if (parsedAmount === null) {
      return;
    }

    this.valueInPlnState.set(this.currencyPresentation.convertToPln(parsedAmount, this.selectedCurrency()));
    this.onChange(this.valueInPlnState());
  }

  protected onBlur(): void {
    this.focusedState.set(false);
    this.onTouched();

    const parsedAmount = this.parseLocalizedNumber(this.displayValueState());

    if (parsedAmount === null) {
      this.valueInPlnState.set(0);
      this.onChange(0);
    }

    this.displayValueState.set(this.formatInputValue(this.valueInPlnState()));
  }

  private formatInputValue(valueInPln: number): string {
    const displayAmount = this.currencyPresentation.convertFromPln(valueInPln, this.selectedCurrency());
    const digits = this.selectedCurrency() === 'PLN' ? 0 : 2;

    return new Intl.NumberFormat('pl-PL', {
      minimumFractionDigits: digits,
      maximumFractionDigits: digits,
      useGrouping: false
    }).format(displayAmount);
  }

  private parseLocalizedNumber(rawValue: string): number | null {
    const normalized = rawValue.replace(/\s/g, '').replace(',', '.').trim();

    if (normalized === '') {
      return null;
    }

    const parsed = Number(normalized);
    return Number.isNaN(parsed) ? null : parsed;
  }

  private formatDate(rawDate: string): string {
    return new Intl.DateTimeFormat('pl-PL').format(new Date(rawDate));
  }
}
