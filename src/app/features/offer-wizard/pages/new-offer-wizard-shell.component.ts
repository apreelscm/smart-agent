import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { filter } from 'rxjs';
import { ButtonDirective } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { Tag } from 'primeng/tag';
import { Offer, OfferStatus } from '../../../core/models';
import { PaymentPlan } from '../../../core/models/payment/payment-plan.model';
import { OfferProduct } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyPresentationService } from '../../../core/services/currency-presentation.service';
import { PresentAmountPipe } from '../../../shared/pipes/present-amount.pipe';
import { CurrencySwitcherComponent } from '../../../shared/ui/currency-switcher/currency-switcher.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { CropCoverCode, CropParcel, CropVariantId } from '../models/crop-offer.model';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

type WizardStep = {
  label: string;
  route: string;
};

type TransitionDefinition = {
  actionLabel: string;
  title: string;
  description: string;
  targetStatus: OfferStatus;
};

type PendingTransition = {
  offer: Offer;
  transition: TransitionDefinition;
};

@Component({
  selector: 'app-new-offer-wizard-shell',
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet,
    ButtonDirective,
    Dialog,
    Tag,
    SectionCardComponent,
    PresentAmountPipe,
    CurrencySwitcherComponent
  ],
  templateUrl: './new-offer-wizard-shell.component.html',
  styleUrl: './new-offer-wizard-shell.component.scss',
  providers: [CurrencyPresentationService]
})
export class NewOfferWizardShellComponent {
  private readonly wizardState = inject(OfferWizardStateService);
  private readonly policiesRepository = inject(PoliciesRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  protected readonly currencyPresentation = inject(CurrencyPresentationService);

  private readonly currentUrl = signal(this.router.url);
  private readonly productState = signal<OfferProduct>('MOTOR');

  protected readonly steps = computed<WizardStep[]>(() =>
    this.productState() === 'CROP'
      ? [
          { label: 'Dane do kalkulacji', route: 'crop' },
          { label: 'Konfiguracja wariantu', route: 'variants' },
          { label: 'Dane do umowy', route: 'customer' },
          { label: 'Podsumowanie', route: 'summary' }
        ]
      : [
          { label: 'Dane do kalkulacji', route: 'vehicle' },
          { label: 'Konfiguracja wariantu', route: 'variants' },
          { label: 'Dane do umowy', route: 'customer' },
          { label: 'Podsumowanie', route: 'summary' }
        ]
  );
  protected readonly product = computed(() => this.productState());

  protected readonly selectedOffer = computed(() => this.wizardState.draftOffer());
  protected readonly selectedVariant = computed(() => this.wizardState.selectedVariant());
  protected readonly discountAmount = computed(() => this.wizardState.discountAmount());
  protected readonly readonlyMode = computed(() => this.wizardState.readonlyMode());
  protected readonly cropDraft = computed(() => this.wizardState.cropDraft());
  protected readonly cropVariantConfigs = computed(() => this.wizardState.cropVariantConfigs());
  protected readonly cropSelectedVariantId = computed<CropVariantId>(() => this.wizardState.cropSelectedVariantId());
  protected readonly cropDiscountAmount = computed(() => this.wizardState.cropDiscountAmount());
  protected readonly cropSelectedPaymentFrequency = computed(() => this.wizardState.cropSelectedPaymentFrequency());
  protected readonly cropTransportMainPlanEnabled = computed(() => this.wizardState.cropTransportMainPlanEnabled());
  protected readonly cropTransportMainPlanPremium = 180;
  protected readonly cropCount = computed(() => this.cropDraft().length);
  protected readonly cropParcelsCount = computed(() => this.cropDraft().reduce((sum, crop) => sum + crop.parcels.length, 0));
  protected readonly cropTotalInsuranceSum = computed(() =>
    this.cropDraft()
      .flatMap((crop) => crop.parcels)
      .reduce((sum, parcel) => sum + this.cropInsuranceSumForParcel(parcel), 0)
  );
  protected readonly cropSelectedVariantBasePremium = computed(() => this.cropVariantPremium(this.cropSelectedVariantId()));
  protected readonly cropAppliedDiscount = computed(() => Math.min(this.cropDiscountAmount(), this.cropSelectedVariantBasePremium()));
  protected readonly cropDiscountedPremium = computed(() =>
    Math.max(0, this.cropSelectedVariantBasePremium() - this.cropAppliedDiscount())
  );
  protected readonly cropPaymentRows = computed(() => {
    const totalAmount = Math.max(0, Math.round(this.cropDiscountedPremium()));
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
          remainingInstallmentAmount: null as number | null,
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
  protected readonly cropSelectedPaymentRow = computed(
    () => this.cropPaymentRows().find((row) => row.frequency === this.cropSelectedPaymentFrequency()) ?? this.cropPaymentRows()[0]
  );
  protected readonly pendingTransition = signal<PendingTransition | null>(null);
  protected readonly transitionDialogVisible = signal(false);
  protected readonly stepLinks = computed(() => {
    const sourceOfferId = this.wizardState.sourceOfferId();
    const basePath = sourceOfferId
      ? this.productState() === 'CROP'
        ? ['/offers', sourceOfferId, 'crop']
        : ['/offers', sourceOfferId]
      : ['/offers', 'new', this.productState().toLowerCase()];

    return this.steps().map((step) => ({
      ...step,
      commands: [...basePath, step.route]
    }));
  });

  protected readonly currentStepIndex = computed(() => {
    const tree = this.router.parseUrl(this.currentUrl());
    const primarySegments = tree.root.children['primary']?.segments.map((segment) => segment.path) ?? [];
    const currentRoute = primarySegments[primarySegments.length - 1] ?? '';
    const foundIndex = this.steps().findIndex((step) => step.route === currentRoute);

    return foundIndex >= 0 ? foundIndex : 0;
  });

  protected readonly previousStepLink = computed(() => {
    const currentIndex = this.currentStepIndex();
    return currentIndex > 0 ? this.stepLinks()[currentIndex - 1].commands : null;
  });

  protected readonly nextStepLink = computed(() => {
    const currentIndex = this.currentStepIndex();
    return currentIndex < this.stepLinks().length - 1 ? this.stepLinks()[currentIndex + 1].commands : null;
  });
  protected readonly isSummaryStep = computed(() => this.currentStepIndex() === this.steps().length - 1);

  constructor() {
    this.productState.set(this.resolveProductFromRoute(this.route.snapshot.data['product']));
    this.wizardState.setReadonlyMode(this.resolveReadonlyMode(this.router.url));

    this.route.paramMap.pipe(takeUntilDestroyed()).subscribe(async (params) => {
      const offerId = params.get('offerId');
      const product = this.resolveProductFromRoute(this.route.snapshot.data['product']);
      this.productState.set(product);

      if (offerId) {
        await this.wizardState.initializeFromOffer(offerId);
        this.wizardState.setReadonlyMode(this.resolveReadonlyMode(this.router.url));
        return;
      }

      const copyFromOfferId = this.route.snapshot.queryParamMap.get('copyFrom');

      if (copyFromOfferId) {
        await this.wizardState.initializeCopyFromOffer(copyFromOfferId);
        this.wizardState.setReadonlyMode(this.resolveReadonlyMode(this.router.url));
        return;
      }

      await this.wizardState.initializeNewDraft(product);

      const sourcePolicyId = this.route.snapshot.queryParamMap.get('sourcePolicyId');
      const mode = this.route.snapshot.queryParamMap.get('mode');

      if (!sourcePolicyId) {
        return;
      }

      const policyFromMock = await firstValueFrom(this.policiesRepository.getPolicyById(sourcePolicyId));
      const policy = policyFromMock ?? this.salesFlowRuntimeRepository.getPromotedPolicyById(sourcePolicyId);

      if (policy) {
        this.wizardState.applyPolicyPrefill(policy, mode === 'renew' ? 'RENEW' : 'COPY');
      }
    });

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntilDestroyed()
      )
      .subscribe(() => {
        this.currentUrl.set(this.router.url);
        const readonly = this.resolveReadonlyMode(this.router.url);
        this.wizardState.setReadonlyMode(readonly);
      });
  }

  private resolveProductFromRoute(rawProduct: unknown): OfferProduct {
    return rawProduct === 'CROP' ? 'CROP' : 'MOTOR';
  }

  protected discountedAmount(): number {
    const variant = this.selectedVariant();

    if (!variant) {
      return 0;
    }

    const ocPremium = variant.policyLines.find((line) => line.code === 'OC')?.premium.amount ?? 0;
    const appliedDiscount = Math.min(this.discountAmount(), ocPremium);
    return Math.max(0, Math.round(variant.totalPremium.amount - appliedDiscount));
  }

  protected cropVariantLabel(variantId: CropVariantId): string {
    switch (variantId) {
      case 'BASIC':
        return 'Podstawowy wariant';
      case 'RECOMMENDED':
        return 'Rekomendowany';
      case 'FULL':
        return 'Pełny';
      default:
        return variantId;
    }
  }

  protected cropPaymentFrequencyLabel(frequency: PaymentPlan['frequency']): string {
    switch (frequency) {
      case 'ANNUAL':
        return '1 rata';
      case 'SEMI_ANNUAL':
        return '2 raty';
      case 'QUARTERLY':
        return '4 raty';
      default:
        return frequency;
    }
  }

  protected async saveDraft(): Promise<void> {
    const offer = this.selectedOffer();

    if (!offer || this.readonlyMode()) {
      return;
    }

    const saved = this.salesFlowRuntimeRepository.saveDraftOffer(offer as Offer);
    await this.router.navigate(['/offers'], { queryParams: { highlight: saved.id } });
  }

  protected openTransitionDialog(transition: TransitionDefinition): void {
    const offer = this.selectedOffer();

    if (!offer || !this.canExecuteStatusActions(offer)) {
      return;
    }

    this.pendingTransition.set({ offer, transition });
    this.transitionDialogVisible.set(true);
  }

  protected closeTransitionDialog(): void {
    this.transitionDialogVisible.set(false);
    this.pendingTransition.set(null);
  }

  protected confirmTransition(): void {
    const pending = this.pendingTransition();

    if (!pending) {
      return;
    }

    this.wizardState.updateOfferStatus(pending.transition.targetStatus);
    const updatedOffer = this.selectedOffer();

    if (updatedOffer) {
      this.salesFlowRuntimeRepository.saveDraftOffer(updatedOffer);
    }

    if (pending.transition.targetStatus === 'POLICY') {
      this.salesFlowRuntimeRepository.promoteOfferToPolicy(updatedOffer ?? { ...pending.offer, status: 'POLICY' });
      this.closeTransitionDialog();
      void this.router.navigate(['/policies']);
      return;
    }

    this.wizardState.setReadonlyMode(this.resolveReadonlyMode(this.router.url));
    this.closeTransitionDialog();
  }

  protected actionsForStatus(status?: OfferStatus): TransitionDefinition[] {
    switch (status) {
      case 'DRAFT':
        return [
          {
            actionLabel: 'Wystaw ofertę',
            title: 'Wystawienie oferty',
            description: 'Czy na pewno chcesz wystawić ofertę z wersji roboczej?',
            targetStatus: 'ISSUED'
          }
        ];
      case 'CALCULATION':
        return [
          {
            actionLabel: 'Wystaw ofertę',
            title: 'Wystawienie oferty',
            description: 'Czy na pewno chcesz wystawić ofertę na podstawie tej kalkulacji?',
            targetStatus: 'ISSUED'
          }
        ];
      case 'ISSUED':
        return [
          {
            actionLabel: 'Akceptuj',
            title: 'Akceptacja oferty',
            description: 'Czy potwierdzasz akceptację tej oferty?',
            targetStatus: 'ACCEPTED'
          },
          {
            actionLabel: 'Odrzuć',
            title: 'Odrzucenie oferty',
            description: 'Czy na pewno chcesz oznaczyć ofertę jako odrzuconą?',
            targetStatus: 'REJECTED'
          }
        ];
      case 'ACCEPTED':
        return [
          {
            actionLabel: 'Polisuj',
            title: 'Polisowanie oferty',
            description: 'Czy chcesz przejść z zaakceptowanej oferty do statusu polisa?',
            targetStatus: 'POLICY'
          }
        ];
      default:
        return [];
    }
  }

  protected showPrintButton(status?: OfferStatus): boolean {
    return status === 'ISSUED' || status === 'ACCEPTED';
  }

  protected printPlaceholder(): void {
    console.log('[OfferWizard] Print placeholder action triggered');
  }

  protected transitionTitle(): string {
    return this.pendingTransition()?.transition.title ?? '';
  }

  protected canExecuteStatusActions(offer?: Offer): boolean {
    if (!offer) {
      return false;
    }

    return !this.readonlyMode() || this.isStatusReadonly(offer.status);
  }

  private resolveReadonlyMode(url: string): boolean {
    const tree = this.router.parseUrl(url);
    const readonlyRequested = tree.queryParams['readonly'] === '1';
    const segments = tree.root.children['primary']?.segments.map((segment) => segment.path) ?? [];
    const isOfferEditionRoute = segments[0] === 'offers' && !!segments[1] && segments[1] !== 'new';

    if (isOfferEditionRoute) {
      return this.isStatusReadonly(this.wizardState.draftOffer()?.status);
    }

    return readonlyRequested;
  }

  private isStatusReadonly(status?: OfferStatus): boolean {
    return status === 'ISSUED' || status === 'ACCEPTED' || status === 'REJECTED';
  }

  private cropInsuranceSumForParcel(parcel: CropParcel): number {
    const soilFactors: Record<string, number> = { I: 10, II: 9, III: 8, IV: 7, V: 6, VI: 5 };
    const factor = soilFactors[parcel.soilClassCode] ?? 5;
    const area = Math.max(0, parcel.cropAreaHa || 0);
    return Math.round((345 * factor * area) / 10);
  }

  private cropVariantPremium(variantId: CropVariantId): number {
    const parcelsPremium = this.cropDraft()
      .flatMap((crop) => crop.parcels)
      .reduce((sum, parcel) => sum + this.cropParcelPremium(variantId, parcel), 0);
    return parcelsPremium + (this.cropTransportMainPlanEnabled() ? this.cropTransportMainPlanPremium : 0);
  }

  private cropParcelPremium(variantId: CropVariantId, parcel: CropParcel): number {
    const persistedConfig = this.cropVariantConfigs()
      .find((config) => config.variantId === variantId)
      ?.parcelConfigurations.find((item) => item.parcelId === parcel.id);

    const defaultSelection = this.defaultCropSelection(variantId);
    const selectedCovers = { ...defaultSelection.selectedCovers, ...(persistedConfig?.selectedCovers ?? {}) };
    const deductiblePercent = persistedConfig?.deductiblePercent ?? defaultSelection.deductiblePercent;

    const coverRates: Record<CropCoverCode, number> = {
      HAIL: 32,
      HEAVY_RAIN_HURRICANE: 24,
      SPRING_FROST: 28,
      FLOOD: 20,
      WATER_STAGNATION: 16,
      FIRE: 11
    };

    const selectedRate = (Object.keys(coverRates) as CropCoverCode[]).reduce(
      (rate, code) => rate + (selectedCovers[code] ? coverRates[code] : 0),
      0
    );

    return Math.round(Math.max(0, parcel.cropAreaHa) * selectedRate * this.cropDeductibleFactor(deductiblePercent));
  }

  private defaultCropSelection(variantId: CropVariantId): {
    selectedCovers: Record<CropCoverCode, boolean>;
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

  private cropDeductibleFactor(percent: 5 | 10 | 20): number {
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
}
