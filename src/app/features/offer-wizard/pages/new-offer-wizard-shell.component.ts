import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { filter } from 'rxjs';
import { ButtonDirective } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { Tag } from 'primeng/tag';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { Offer, OfferStatus } from '../../../core/models';
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
    CurrencyPipe,
    SectionCardComponent
  ],
  templateUrl: './new-offer-wizard-shell.component.html',
  styleUrl: './new-offer-wizard-shell.component.scss'
})
export class NewOfferWizardShellComponent {
  private readonly wizardState = inject(OfferWizardStateService);
  private readonly policiesRepository = inject(PoliciesRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly currentUrl = signal(this.router.url);

  protected readonly steps: WizardStep[] = [
    { label: 'Dane do kalkulacji', route: 'vehicle' },
    { label: 'Konfiguracja wariantu', route: 'variants' },
    { label: 'Dane do umowy', route: 'customer' },
    { label: 'Podsumowanie', route: 'summary' }
  ];

  protected readonly selectedOffer = computed(() => this.wizardState.draftOffer());
  protected readonly selectedVariant = computed(() => this.wizardState.selectedVariant());
  protected readonly discountAmount = computed(() => this.wizardState.discountAmount());
  protected readonly readonlyMode = computed(() => this.wizardState.readonlyMode());
  protected readonly pendingTransition = signal<PendingTransition | null>(null);
  protected readonly transitionDialogVisible = signal(false);
  protected readonly stepLinks = computed(() => {
    const sourceOfferId = this.wizardState.sourceOfferId();
    const basePath = sourceOfferId ? ['/offers', sourceOfferId] : ['/offers', 'new'];

    return this.steps.map((step) => ({
      ...step,
      commands: [...basePath, step.route]
    }));
  });

  protected readonly currentStepIndex = computed(() => {
    const tree = this.router.parseUrl(this.currentUrl());
    const primarySegments = tree.root.children['primary']?.segments.map((segment) => segment.path) ?? [];
    const currentRoute = primarySegments[primarySegments.length - 1] ?? '';
    const foundIndex = this.steps.findIndex((step) => step.route === currentRoute);

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
  protected readonly isSummaryStep = computed(() => this.currentStepIndex() === this.steps.length - 1);

  constructor() {
    this.wizardState.setReadonlyMode(this.resolveReadonlyMode(this.router.url));

    this.route.paramMap.pipe(takeUntilDestroyed()).subscribe(async (params) => {
      const offerId = params.get('offerId');

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

      await this.wizardState.initializeNewDraft();

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

  protected discountedAmount(): number {
    const variant = this.selectedVariant();

    if (!variant) {
      return 0;
    }

    const ocPremium = variant.policyLines.find((line) => line.code === 'OC')?.premium.amount ?? 0;
    const appliedDiscount = Math.min(this.discountAmount(), ocPremium);
    return Math.max(0, Math.round(variant.totalPremium.amount - appliedDiscount));
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
}
