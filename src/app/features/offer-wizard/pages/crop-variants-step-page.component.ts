import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ButtonDirective } from 'primeng/button';
import { PaymentPlan } from '../../../core/models/payment/payment-plan.model';
import {
  CropCoverCode,
  CropCoverSelection,
  CropMaster,
  CropParcel,
  CropParcelVariantConfig,
  CropVariantConfig,
  CropVariantId
} from '../models/crop-offer.model';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

type LaneDefinition = {
  id: CropVariantId;
  title: string;
  subtitle: string;
  recommended: boolean;
};

type CoverDefinition = {
  code: CropCoverCode;
  label: string;
  ratePerHa: number;
};

type ParcelView = {
  cropId: string;
  cropSpecies: string;
  cropIndex: number;
  parcel: CropParcel;
  parcelIndex: number;
};

type CropPaymentRow = {
  frequency: PaymentPlan['frequency'];
  installmentsCount: number;
  firstInstallmentAmount: number;
  remainingInstallmentAmount: number | null;
  totalAmount: number;
};

@Component({
  selector: 'app-crop-variants-step-page',
  imports: [CommonModule, SectionCardComponent, CurrencyPipe, ButtonDirective],
  templateUrl: './crop-variants-step-page.component.html',
  styleUrl: './crop-variants-step-page.component.scss'
})
export class CropVariantsStepPageComponent {
  private readonly wizardState = inject(OfferWizardStateService);
  protected readonly customerDiscountBudget = 2540;
  protected readonly discountBusinessLine = 'Ubezpieczenie upraw';
  protected readonly transportMainPlanLabel = 'Plon główny w transporcie';
  protected readonly transportMainPlanPremium = 180;
  protected readonly transportMainPlanInsuranceSum = 100000;
  protected readonly discountInput = signal<string>('0');
  protected readonly discountError = signal<string | null>(null);

  protected readonly lanes: LaneDefinition[] = [
    {
      id: 'BASIC',
      title: 'Podstawowy wariant',
      subtitle: 'Zakres bazowy',
      recommended: false
    },
    {
      id: 'RECOMMENDED',
      title: 'Rekomendowany',
      subtitle: 'Najczęściej wybierany',
      recommended: true
    },
    {
      id: 'FULL',
      title: 'Pełny',
      subtitle: 'Najszersza ochrona',
      recommended: false
    }
  ];

  protected readonly covers: CoverDefinition[] = [
    { code: 'HAIL', label: 'Grad', ratePerHa: 32 },
    { code: 'HEAVY_RAIN_HURRICANE', label: 'Deszcz nawalny, huragan', ratePerHa: 24 },
    { code: 'SPRING_FROST', label: 'Przymrozki wiosenne', ratePerHa: 28 },
    { code: 'FLOOD', label: 'Powódź', ratePerHa: 20 },
    { code: 'WATER_STAGNATION', label: 'Zastoiska wodne', ratePerHa: 16 },
    { code: 'FIRE', label: 'Ogień', ratePerHa: 11 }
  ];

  protected readonly crops = computed<CropMaster[]>(() => this.wizardState.cropDraft());
  protected readonly selectedVariantId = computed<CropVariantId>(() => this.wizardState.cropSelectedVariantId());
  protected readonly selectedPaymentFrequency = computed<PaymentPlan['frequency']>(() => this.wizardState.cropSelectedPaymentFrequency());
  protected readonly transportMainPlanEnabled = computed(() => this.wizardState.cropTransportMainPlanEnabled());
  protected readonly parcelViews = computed<ParcelView[]>(() =>
    this.crops().flatMap((crop, cropIndex) =>
      crop.parcels.map((parcel, parcelIndex) => ({
        cropId: crop.id,
        cropSpecies: crop.species,
        cropIndex,
        parcel,
        parcelIndex
      }))
    )
  );
  protected readonly selectedVariantPrice = computed(() => this.lanePrice(this.selectedVariantId()));
  protected readonly maxDiscountAmount = computed(() => Math.max(0, Math.min(this.customerDiscountBudget, this.selectedVariantPrice())));
  protected readonly appliedDiscountAmount = computed(() => Math.min(this.wizardState.cropDiscountAmount(), this.maxDiscountAmount()));
  protected readonly paymentRows = computed<CropPaymentRow[]>(() => {
    const totalAmount = Math.max(0, Math.round(this.selectedVariantPrice() - this.appliedDiscountAmount()));
    const plans: Array<{ frequency: PaymentPlan['frequency']; installmentsCount: number }> = [
      { frequency: 'ANNUAL', installmentsCount: 1 },
      { frequency: 'SEMI_ANNUAL', installmentsCount: 2 },
      { frequency: 'QUARTERLY', installmentsCount: 4 }
    ];

    return plans.map((plan) => {
      if (plan.installmentsCount === 1) {
        return {
          frequency: plan.frequency,
          installmentsCount: 1,
          firstInstallmentAmount: totalAmount,
          remainingInstallmentAmount: null,
          totalAmount
        };
      }

      const remainingInstallmentAmount = Math.floor(totalAmount / plan.installmentsCount);
      const firstInstallmentAmount = totalAmount - remainingInstallmentAmount * (plan.installmentsCount - 1);

      return {
        frequency: plan.frequency,
        installmentsCount: plan.installmentsCount,
        firstInstallmentAmount,
        remainingInstallmentAmount,
        totalAmount
      };
    });
  });

  protected readonly variantConfigs = signal<CropVariantConfig[]>([]);

  constructor() {
    this.variantConfigs.set(this.buildInitialConfigs());
    this.persistConfigs();
    this.discountInput.set(String(this.wizardState.cropDiscountAmount()));
  }

  protected cropLabel(code: string): string {
    switch (code) {
      case 'PSZENICA':
        return 'Pszenica';
      case 'KUKURYDZA':
        return 'Kukurydza';
      case 'RZEPAK':
        return 'Rzepak';
      case 'BURAK_CUKROWY':
        return 'Burak cukrowy';
      default:
        return code;
    }
  }

  protected insuranceSumForParcel(parcel: CropParcel): number {
    const soilFactors: Record<string, number> = {
      I: 10,
      II: 9,
      III: 8,
      IV: 7,
      V: 6,
      VI: 5
    };
    const factor = soilFactors[parcel.soilClassCode] ?? 5;
    const area = Math.max(0, parcel.cropAreaHa || 0);
    return Math.round((345 * factor * area) / 10);
  }

  protected lanePrice(variantId: CropVariantId): number {
    const parcelsPremium = this.parcelViews().reduce((sum, parcelView) => sum + this.parcelPrice(variantId, parcelView.parcel.id), 0);
    return parcelsPremium + this.transportAddonPremium();
  }

  protected selectVariant(variantId: CropVariantId): void {
    this.wizardState.setCropSelectedVariantId(variantId);
    const maxDiscountAmount = Math.max(0, Math.min(this.customerDiscountBudget, this.lanePrice(variantId)));
    if (this.wizardState.cropDiscountAmount() > maxDiscountAmount) {
      this.wizardState.setCropDiscountAmount(maxDiscountAmount);
      this.discountInput.set(String(maxDiscountAmount));
    }
    this.discountError.set(null);
  }

  protected toggleTransportMainPlan(enabled: boolean): void {
    this.wizardState.setCropTransportMainPlanEnabled(enabled);
    const maxDiscountAmount = this.maxDiscountAmount();
    if (this.wizardState.cropDiscountAmount() > maxDiscountAmount) {
      this.wizardState.setCropDiscountAmount(maxDiscountAmount);
      this.discountInput.set(String(maxDiscountAmount));
    }
    this.discountError.set(null);
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
      this.discountError.set(`Podaj poprawną wartość kwotową (0-${maxDiscountAmount} PLN).`);
      return;
    }

    if (parsed > maxDiscountAmount) {
      this.discountError.set(`Maksymalna zniżka dla linii ${this.discountBusinessLine} to ${maxDiscountAmount} PLN.`);
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
      this.wizardState.setCropDiscountAmount(0);
      return;
    }

    const parsed = Number(rawValue.replace(',', '.'));

    if (Number.isNaN(parsed) || parsed < 0) {
      this.discountError.set(`Podaj poprawną wartość kwotową (0-${maxDiscountAmount} PLN).`);
      return;
    }

    if (parsed > maxDiscountAmount) {
      this.discountError.set(`Maksymalna zniżka dla linii ${this.discountBusinessLine} to ${maxDiscountAmount} PLN.`);
      return;
    }

    const normalized = Math.round(parsed);
    this.discountError.set(null);
    this.discountInput.set(String(normalized));
    this.wizardState.setCropDiscountAmount(normalized);
  }

  protected selectPaymentFrequency(frequency: PaymentPlan['frequency']): void {
    this.wizardState.setCropSelectedPaymentFrequency(frequency);
  }

  protected isPaymentRowSelected(row: CropPaymentRow): boolean {
    return this.selectedPaymentFrequency() === row.frequency;
  }

  protected paymentPeriodLabel(row: CropPaymentRow): string {
    switch (row.frequency) {
      case 'ANNUAL':
        return '1 rok';
      case 'SEMI_ANNUAL':
        return 'Półrocznie';
      case 'QUARTERLY':
        return 'Kwartalnie';
      default:
        return row.frequency;
    }
  }

  protected transportAddonPremium(): number {
    return this.transportMainPlanEnabled() ? this.transportMainPlanPremium : 0;
  }

  protected isCoverSelected(variantId: CropVariantId, parcelId: string, coverCode: CropCoverCode): boolean {
    return this.getParcelConfig(variantId, parcelId)?.selectedCovers[coverCode] === true;
  }

  protected toggleCover(variantId: CropVariantId, parcelId: string, coverCode: CropCoverCode): void {
    this.variantConfigs.update((configs) =>
      configs.map((config) => {
        if (config.variantId !== variantId) {
          return config;
        }

        return {
          ...config,
          parcelConfigurations: config.parcelConfigurations.map((parcelConfig) =>
            parcelConfig.parcelId !== parcelId
              ? parcelConfig
              : {
                  ...parcelConfig,
                  selectedCovers: {
                    ...parcelConfig.selectedCovers,
                    [coverCode]: !parcelConfig.selectedCovers[coverCode]
                  }
                }
          )
        };
      })
    );
    this.persistConfigs();
  }

  protected deductiblePercent(variantId: CropVariantId, parcelId: string): 5 | 10 | 20 {
    return this.getParcelConfig(variantId, parcelId)?.deductiblePercent ?? 10;
  }

  protected updateDeductible(variantId: CropVariantId, parcelId: string, rawValue: string): void {
    const parsed = Number(rawValue);
    const deductiblePercent: 5 | 10 | 20 = parsed === 5 ? 5 : parsed === 20 ? 20 : 10;

    this.variantConfigs.update((configs) =>
      configs.map((config) => {
        if (config.variantId !== variantId) {
          return config;
        }

        return {
          ...config,
          parcelConfigurations: config.parcelConfigurations.map((parcelConfig) =>
            parcelConfig.parcelId !== parcelId
              ? parcelConfig
              : {
                  ...parcelConfig,
                  deductiblePercent
                }
          )
        };
      })
    );
    this.persistConfigs();
  }

  protected parcelPrice(variantId: CropVariantId, parcelId: string): number {
    const parcel = this.parcelViews().find((item) => item.parcel.id === parcelId)?.parcel;

    if (!parcel) {
      return 0;
    }

    const config = this.getParcelConfig(variantId, parcelId);

    if (!config) {
      return 0;
    }

    const selectedRate = this.covers.reduce(
      (sum, cover) => sum + (config.selectedCovers[cover.code] ? cover.ratePerHa : 0),
      0
    );

    const deductibleFactor = this.deductibleFactor(config.deductiblePercent);
    return Math.round(Math.max(0, parcel.cropAreaHa) * selectedRate * deductibleFactor);
  }

  private buildInitialConfigs(): CropVariantConfig[] {
    const parcelIds = this.parcelViews().map((item) => item.parcel.id);
    const persisted = this.wizardState.cropVariantConfigs();

    if (persisted.length === 0) {
      return this.defaultConfigs(parcelIds);
    }

    return this.mergeWithParcelIds(persisted, parcelIds);
  }

  private mergeWithParcelIds(configs: CropVariantConfig[], parcelIds: string[]): CropVariantConfig[] {
    return this.lanes.map((lane) => {
      const persisted = configs.find((item) => item.variantId === lane.id);
      const defaultsByVariant = this.defaultConfigByVariant(lane.id);

      const parcelConfigurations: CropParcelVariantConfig[] = parcelIds.map((parcelId) => {
        const existing = persisted?.parcelConfigurations.find((item) => item.parcelId === parcelId);

        if (existing) {
          const deductiblePercent: 5 | 10 | 20 =
            existing.deductiblePercent === 5 || existing.deductiblePercent === 20 ? existing.deductiblePercent : 10;
          return {
            parcelId,
            selectedCovers: {
              ...defaultsByVariant.selectedCovers,
              ...existing.selectedCovers
            },
            deductiblePercent
          };
        }

        return {
          parcelId,
          selectedCovers: { ...defaultsByVariant.selectedCovers },
          deductiblePercent: defaultsByVariant.deductiblePercent
        };
      });

      return {
        variantId: lane.id,
        parcelConfigurations
      };
    });
  }

  private defaultConfigs(parcelIds: string[]): CropVariantConfig[] {
    return this.lanes.map((lane) => {
      const defaultsByVariant = this.defaultConfigByVariant(lane.id);

      return {
        variantId: lane.id,
        parcelConfigurations: parcelIds.map((parcelId) => ({
          parcelId,
          selectedCovers: { ...defaultsByVariant.selectedCovers },
          deductiblePercent: defaultsByVariant.deductiblePercent
        }))
      };
    });
  }

  private defaultConfigByVariant(variantId: CropVariantId): {
    selectedCovers: CropCoverSelection;
    deductiblePercent: 5 | 10 | 20;
  } {
    if (variantId === 'BASIC') {
      return {
        selectedCovers: {
          HAIL: true,
          HEAVY_RAIN_HURRICANE: true,
          SPRING_FROST: false,
          FLOOD: false,
          WATER_STAGNATION: false,
          FIRE: false
        },
        deductiblePercent: 20
      };
    }

    if (variantId === 'RECOMMENDED') {
      return {
        selectedCovers: {
          HAIL: true,
          HEAVY_RAIN_HURRICANE: true,
          SPRING_FROST: true,
          FLOOD: true,
          WATER_STAGNATION: false,
          FIRE: true
        },
        deductiblePercent: 10
      };
    }

    return {
      selectedCovers: {
        HAIL: true,
        HEAVY_RAIN_HURRICANE: true,
        SPRING_FROST: true,
        FLOOD: true,
        WATER_STAGNATION: true,
        FIRE: true
      },
      deductiblePercent: 5
    };
  }

  private deductibleFactor(percent: 5 | 10 | 20): number {
    switch (percent) {
      case 5:
        return 1.2;
      case 10:
        return 1;
      case 20:
        return 0.82;
      default:
        return 1;
    }
  }

  private getParcelConfig(variantId: CropVariantId, parcelId: string): CropParcelVariantConfig | undefined {
    return this.variantConfigs()
      .find((config) => config.variantId === variantId)
      ?.parcelConfigurations.find((parcelConfig) => parcelConfig.parcelId === parcelId);
  }

  private persistConfigs(): void {
    this.wizardState.setCropVariantConfigs(this.variantConfigs());
  }
}
