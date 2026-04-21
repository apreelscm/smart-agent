import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { Component, computed, inject, signal } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { Tag } from 'primeng/tag';
import { CoverTerm, CurrencyCode, ExchangeRateSnapshot, OfferVariant, PolicyLineCode } from '../../../core/models';
import { Cover } from '../../../core/models/cover/cover.model';
import { PaymentPlan } from '../../../core/models/payment/payment-plan.model';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { CurrencyConversionService } from '../../../core/services/currency-conversion.service';
import { PresentedMoneyPipe } from '../../../shared/pipes/presented-money.pipe';
import { CurrencySwitcherComponent } from '../../../shared/ui/currency-switcher/currency-switcher.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

type AddonDefinition = {
  lineCode: PolicyLineCode;
  coverCode: string;
  label: string;
};

type PaymentPlanView = {
  plan: PaymentPlan;
  installmentsCount: number;
  firstInstallmentAmount: number;
  remainingInstallmentAmount: number | null;
  totalAmount: number;
};

@Component({
  selector: 'app-variants-step-page',
  imports: [CommonModule, SectionCardComponent, Tag, ButtonDirective, CurrencySwitcherComponent, PresentedMoneyPipe],
  templateUrl: './variants-step-page.component.html',
  styleUrl: './variants-step-page.component.scss'
})
export class VariantsStepPageComponent {
  private readonly wizardState = inject(OfferWizardStateService);
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly currencyConversionService = inject(CurrencyConversionService);

  protected readonly customerDiscountBudget = 2540;
  protected readonly discountInput = signal<string>('0');
  protected readonly discountError = signal<string | null>(null);
  protected readonly discountBusinessLine = 'Komunikacyjne OC';
  protected readonly selectedInputCurrency = signal<CurrencyCode>('PLN');
  protected readonly ratesError = signal(false);
  protected readonly exchangeRates = toSignal(this.exchangeRatesRepository.getCurrentRates(), {
    initialValue: null as ExchangeRateSnapshot | null,
    rejectErrors: true
  });
  protected readonly availability = computed(() =>
    this.currencyConversionService.getAvailability(this.exchangeRates(), this.ratesError())
  );
  protected readonly disabledCurrencies = computed<CurrencyCode[]>(() => this.availability().unavailableCurrencies);
  protected readonly currentRateNote = computed(() =>
    this.currencyConversionService.rateNote(this.selectedInputCurrency(), this.exchangeRates())
  );
  protected readonly discountHelperNote = computed(() => {
    const parsed = Number(this.discountInput().replace(',', '.'));
    return Number.isNaN(parsed)
      ? null
      : this.currencyConversionService.helperPlnNote(parsed, this.selectedInputCurrency(), this.exchangeRates());
  });

  protected readonly addons: AddonDefinition[] = [
    { lineCode: 'NNW', coverCode: 'NNW', label: 'NNW' },
    { lineCode: 'NNW_FAMILY', coverCode: 'NNW_FAMILY', label: 'NNW Bezpieczna Rodzina' },
    { lineCode: 'ASSISTANCE', coverCode: 'ASSISTANCE', label: 'Assistance' },
    { lineCode: 'GLASS', coverCode: 'GLASS', label: 'Szyby' },
    { lineCode: 'GREEN_CARD', coverCode: 'GREEN_CARD', label: 'Zielona karta' },
    { lineCode: 'TYRE_ASSISTANCE', coverCode: 'TYRE_ASSISTANCE', label: 'Opony' }
  ];

  protected readonly variants = computed(() => this.wizardState.draftOffer()?.variants ?? []);
  protected readonly variantsByRank = computed(() => [...this.variants()].sort((left, right) => left.rank - right.rank));
  protected readonly selectedVariantId = computed(() => this.wizardState.draftOffer()?.selectedVariantId);
  protected readonly selectedVariant = computed(() =>
    this.variantsByRank().find((variant) => variant.id === this.selectedVariantId())
  );
  protected readonly selectedPaymentPlan = computed(() => this.wizardState.draftOffer()?.selectedPaymentPlan);
  protected readonly paymentPlans = computed(() =>
    [...(this.selectedVariant()?.paymentPlans ?? [])].sort((left, right) => left.installments.length - right.installments.length)
  );
  protected readonly maxDiscountAmount = computed(() => {
    const ocPremium = this.getLine(this.selectedVariant(), 'OC')?.premium.amount ?? 0;
    return Math.max(0, Math.min(this.customerDiscountBudget, ocPremium));
  });
  protected readonly appliedDiscountAmount = computed(() => Math.min(this.wizardState.discountAmount(), this.maxDiscountAmount()));
  protected readonly paymentPlanRows = computed<PaymentPlanView[]>(() => {
    const appliedDiscountAmount = this.appliedDiscountAmount();

    return this.paymentPlans().map((plan) => {
      const installmentsCount = Math.max(plan.installments.length, 1);
      const totalAmount = Math.max(0, Math.round(plan.totalPremium.amount - appliedDiscountAmount));

      if (installmentsCount === 1) {
        return {
          plan,
          installmentsCount,
          firstInstallmentAmount: totalAmount,
          remainingInstallmentAmount: null,
          totalAmount
        };
      }

      const remainingInstallmentAmount = Math.floor(totalAmount / installmentsCount);
      const firstInstallmentAmount = totalAmount - remainingInstallmentAmount * (installmentsCount - 1);

      return {
        plan,
        installmentsCount,
        firstInstallmentAmount,
        remainingInstallmentAmount,
        totalAmount
      };
    });
  });

  constructor() {
    this.wizardState.ensureDefaultVariantSelection();
    this.discountInput.set(String(this.wizardState.discountAmount()));

    try {
      this.exchangeRates();
    } catch {
      this.ratesError.set(true);
      this.selectedInputCurrency.set('PLN');
    }
  }

  protected readonly policyLineCodes = computed<PolicyLineCode[]>(() => {
    const lineCodes = new Set<PolicyLineCode>();

    this.variantsByRank().forEach((variant) => {
      variant.policyLines.forEach((line) => lineCodes.add(line.code));
    });

    return Array.from(lineCodes).filter((lineCode) => this.isLineVisible(lineCode));
  });

  protected changeInputCurrency(currency: CurrencyCode): void {
    if (this.disabledCurrencies().includes(currency)) {
      this.selectedInputCurrency.set('PLN');
      return;
    }

    this.selectedInputCurrency.set(currency);
  }

  protected getLine(variant: OfferVariant | undefined, lineCode: PolicyLineCode) {
    if (!variant) {
      return undefined;
    }
    return variant.policyLines.find((line) => line.code === lineCode);
  }

  protected getLineCoverCodes(lineCode: PolicyLineCode): string[] {
    const coverCodes = new Set<string>();

    this.variantsByRank().forEach((variant) => {
      const line = this.getLine(variant, lineCode);
      line?.covers.forEach((cover) => coverCodes.add(cover.code));
    });

    return Array.from(coverCodes).filter((coverCode) => this.isCoverVisible(lineCode, coverCode));
  }

  protected getCoverByCode(variant: OfferVariant, lineCode: PolicyLineCode, coverCode: string): Cover | undefined {
    return this.getLine(variant, lineCode)?.covers.find((cover) => cover.code === coverCode);
  }

  protected getCoverLabel(lineCode: PolicyLineCode, coverCode: string): string {
    return (
      this.variantsByRank()
        .flatMap((variant) => this.getLine(variant, lineCode)?.covers ?? [])
        .find((cover) => cover.code === coverCode)?.label ?? coverCode
    );
  }

  protected getLineLabel(lineCode: PolicyLineCode): string {
    return (
      this.variantsByRank()
        .flatMap((variant) => variant.policyLines)
        .find((line) => line.code === lineCode)?.label ?? lineCode
    );
  }

  protected selectVariant(variantId: string): void {
    this.wizardState.updateSelectedVariant(variantId);
  }

  protected setCoverSelection(variantId: string, lineCode: PolicyLineCode, coverCode: string, enabled: boolean): void {
    this.wizardState.setCoverSelection(variantId, lineCode, coverCode, enabled);
  }

  protected isAddonSelected(addon: AddonDefinition): boolean {
    return this.variantsByRank().some((variant) => this.getCoverByCode(variant, addon.lineCode, addon.coverCode)?.enabled === true);
  }

  protected setAddonSelection(addon: AddonDefinition, enabled: boolean): void {
    this.variantsByRank().forEach((variant) => {
      if (this.getCoverByCode(variant, addon.lineCode, addon.coverCode)) {
        this.setCoverSelection(variant.id, addon.lineCode, addon.coverCode, enabled);
      }
    });
  }

  protected selectPaymentPlan(frequency: PaymentPlan['frequency']): void {
    this.wizardState.updateSelectedPaymentPlan(frequency);
  }

  protected onDiscountInput(rawValue: string): void {
    this.discountInput.set(rawValue);

    if (rawValue.trim() === '') {
      this.discountError.set(null);
      return;
    }

    const parsed = Number(rawValue.replace(',', '.'));
    const maxDiscountAmount = this.maxDiscountAmount();

    if (Number.isNaN(parsed) || parsed < 0) {
      this.discountError.set(`Podaj poprawną wartość kwotową.`);
      return;
    }

    const normalizedLimit = this.selectedInputCurrency() === 'PLN'
      ? maxDiscountAmount
      : this.currencyConversionService.toDisplayAmount(maxDiscountAmount, this.selectedInputCurrency(), this.exchangeRates()?.rates);

    if (parsed > normalizedLimit) {
      this.discountError.set(`Maksymalna zniżka dla linii ${this.discountBusinessLine} została przekroczona.`);
      return;
    }

    this.discountError.set(null);
  }

  protected onDiscountBlur(): void {
    const rawValue = this.discountInput().trim();
    const maxDiscountAmount = this.maxDiscountAmount();

    if (rawValue === '') {
      this.discountInput.set('0');
      this.discountError.set(null);
      this.wizardState.setDiscountAmount(0);
      return;
    }

    const parsed = Number(rawValue.replace(',', '.'));

    if (Number.isNaN(parsed) || parsed < 0) {
      this.discountError.set(`Podaj poprawną wartość kwotową.`);
      return;
    }

    const persistedPln = this.currencyConversionService.toPersistedPln(parsed, this.selectedInputCurrency(), this.exchangeRates()?.rates);

    if (persistedPln > maxDiscountAmount) {
      this.discountError.set(`Maksymalna zniżka dla linii ${this.discountBusinessLine} to ${maxDiscountAmount} PLN.`);
      return;
    }

    this.discountError.set(null);
    this.discountInput.set(this.selectedInputCurrency() === 'PLN' ? String(persistedPln) : parsed.toFixed(2));
    this.wizardState.setDiscountAmountFromCurrency(parsed, this.selectedInputCurrency(), this.exchangeRates()?.rates);
  }

  protected isPaymentPlanSelected(plan: PaymentPlan): boolean {
    return this.selectedPaymentPlan()?.frequency === plan.frequency;
  }

  protected paymentPeriodLabel(plan: PaymentPlan): string {
    switch (plan.frequency) {
      case 'ANNUAL':
        return '1 rok';
      case 'SEMI_ANNUAL':
        return 'Półrocznie';
      case 'QUARTERLY':
        return 'Kwartalnie';
      case 'MONTHLY':
        return 'Miesięcznie';
      default:
        return plan.frequency;
    }
  }

  protected discountAmountForVariant(variant: OfferVariant): number {
    const ocPremium = this.getLine(variant, 'OC')?.premium.amount ?? 0;
    return Math.min(this.wizardState.discountAmount(), this.customerDiscountBudget, ocPremium);
  }

  protected discountedAmount(variant: OfferVariant): number {
    return Math.max(0, Math.round(variant.totalPremium.amount - this.discountAmountForVariant(variant)));
  }

  protected isEditableTerm(term: CoverTerm): boolean {
    return term.editable === true;
  }

  protected isSelectTerm(term: CoverTerm): boolean {
    return this.isEditableTerm(term) && (term.options?.length ?? 0) > 0;
  }

  protected updateCoverTermValue(
    variantId: string,
    lineCode: PolicyLineCode,
    coverCode: string,
    termId: string,
    rawValue: string
  ): void {
    const parsedValue = Number(rawValue);
    const value = Number.isNaN(parsedValue) ? rawValue : parsedValue;
    this.wizardState.updateCoverTermValue(variantId, lineCode, coverCode, termId, value);
  }

  protected localizedUnit(unit?: string): string | undefined {
    if (!unit) {
      return undefined;
    }

    switch (unit) {
      case 'KM':
        return 'km';
      case 'DAY':
        return 'd';
      case 'PERCENT':
        return '%';
      default:
        return unit;
    }
  }

  protected termValueLabel(term: CoverTerm): string {
    const unit = this.localizedUnit(term.unit);
    const raw = unit ? `${term.value} ${unit}` : `${term.value}`;
    return this.normalizeUnitSpacing(raw);
  }

  protected termOptionLabel(option: string | number, term: CoverTerm): string {
    const unit = this.localizedUnit(term.unit);
    const raw = unit ? `${option} ${unit}` : `${option}`;
    return this.normalizeUnitSpacing(raw);
  }

  private normalizeUnitSpacing(value: string): string {
    return value
      .replace(/\b(\d+)\s*(km)\b/gi, '$1 km')
      .replace(/\b(\d+)\s*(d)\b/gi, '$1 d')
      .replace(/\s{2,}/g, ' ')
      .trim();
  }

  private isLineVisible(lineCode: PolicyLineCode): boolean {
    if (lineCode === 'OC') {
      return true;
    }

    if (lineCode === 'AC') {
      return this.variantsByRank().some((variant) => this.getLine(variant, 'AC')?.covers.some((cover) => cover.enabled));
    }

    const addon = this.addons.find((item) => item.lineCode === lineCode);
    return addon ? this.isAddonSelected(addon) : true;
  }

  private isCoverVisible(lineCode: PolicyLineCode, coverCode: string): boolean {
    if ((lineCode === 'OC' && (coverCode === 'OC' || coverCode === 'OC_FAST_PAYOUT')) || (lineCode === 'AC' && coverCode === 'AC')) {
      return true;
    }

    const addon = this.addons.find((item) => item.lineCode === lineCode && item.coverCode === coverCode);
    return addon ? this.isAddonSelected(addon) : false;
  }
}
