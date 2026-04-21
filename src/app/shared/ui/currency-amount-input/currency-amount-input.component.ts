import { CommonModule } from '@angular/common';
import { Component, computed, effect, input, output, signal } from '@angular/core';
import { DisplayCurrency, ExchangeRateSnapshot } from '../../../core/models';
import { CurrencyService } from '../../../core/services/currency.service';

@Component({
  selector: 'app-currency-amount-input',
  imports: [CommonModule],
  templateUrl: './currency-amount-input.component.html',
  styleUrl: './currency-amount-input.component.scss'
})
export class CurrencyAmountInputComponent {
  private readonly currencyService = new CurrencyService();

  readonly label = input<string>('Kwota');
  readonly basePlnAmount = input<number>(0);
  readonly maxPlnAmount = input<number | null>(null);
  readonly snapshot = input<ExchangeRateSnapshot | null>(null);
  readonly foreignCurrencyAvailable = input<boolean>(false);
  readonly disabled = input<boolean>(false);

  readonly plnAmountChange = output<number>();
  readonly validationMessageChange = output<string | null>();

  protected readonly selectedCurrency = signal<DisplayCurrency>('PLN');
  protected readonly rawValue = signal('0');
  protected readonly validationMessage = signal<string | null>(null);
  private readonly dirty = signal(false);

  protected readonly approximatePln = computed(() => {
    if (this.selectedCurrency() === 'PLN') {
      return null;
    }

    const parsed = this.parseAmount(this.rawValue());

    if (parsed === null) {
      return null;
    }

    const plnAmount = this.currencyService.convertToPln(parsed, this.selectedCurrency(), this.snapshot());
    return this.currencyService.formatAmount(plnAmount, 'PLN', this.snapshot());
  });

  protected readonly rateHint = computed(() => {
    const currency = this.selectedCurrency();

    if (currency === 'PLN' || !this.snapshot()) {
      return null;
    }

    return this.currencyService.formatExchangeRate(currency, this.snapshot());
  });

  constructor() {
    effect(() => {
      if (this.dirty()) {
        return;
      }

      const displayAmount = this.currencyService.convertFromPln(this.basePlnAmount(), this.selectedCurrency(), this.snapshot());
      this.rawValue.set(this.formatEditableAmount(displayAmount, this.selectedCurrency()));
    });
  }

  protected selectCurrency(currency: DisplayCurrency): void {
    if (currency !== 'PLN' && !this.foreignCurrencyAvailable()) {
      return;
    }

    this.selectedCurrency.set(currency);
    this.validationMessage.set(null);
    this.validationMessageChange.emit(null);
    this.dirty.set(false);
    const displayAmount = this.currencyService.convertFromPln(this.basePlnAmount(), currency, this.snapshot());
    this.rawValue.set(this.formatEditableAmount(displayAmount, currency));
  }

  protected onInput(rawValue: string): void {
    this.rawValue.set(rawValue);
    this.dirty.set(true);
    this.validationMessage.set(null);
    this.validationMessageChange.emit(null);
  }

  protected onBlur(): void {
    const parsed = this.parseAmount(this.rawValue());

    if (parsed === null) {
      this.setValidationMessage('Podaj poprawną wartość kwotową.');
      return;
    }

    const plnAmount = this.currencyService.convertToPln(parsed, this.selectedCurrency(), this.snapshot());

    if (this.maxPlnAmount() !== null && plnAmount > (this.maxPlnAmount() ?? 0)) {
      this.setValidationMessage(`Maksymalna dopuszczalna wartość to ${this.currencyService.formatAmount(this.maxPlnAmount(), 'PLN', this.snapshot())}.`);
      return;
    }

    this.validationMessage.set(null);
    this.validationMessageChange.emit(null);
    this.dirty.set(false);
    this.rawValue.set(this.formatEditableAmount(this.currencyService.convertFromPln(plnAmount, this.selectedCurrency(), this.snapshot()), this.selectedCurrency()));
    this.plnAmountChange.emit(plnAmount);
  }

  protected isDisabled(currency: DisplayCurrency): boolean {
    return this.disabled() || (currency !== 'PLN' && !this.foreignCurrencyAvailable());
  }

  private setValidationMessage(message: string): void {
    this.validationMessage.set(message);
    this.validationMessageChange.emit(message);
  }

  private parseAmount(value: string): number | null {
    const normalized = value.trim().replace(',', '.');

    if (!normalized) {
      return null;
    }

    const parsed = Number(normalized);

    if (!Number.isFinite(parsed) || parsed < 0) {
      return null;
    }

    return parsed;
  }

  private formatEditableAmount(amount: number, currency: DisplayCurrency): string {
    const digits = currency === 'PLN' ? 0 : 2;
    return amount.toFixed(digits);
  }
}
