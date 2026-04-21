import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { SplitButton } from 'primeng/splitbutton';
import { Tag } from 'primeng/tag';
import { Offer, OfferStatus, ReferenceData } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyPresentationService } from '../../../core/services/currency-presentation.service';
import { PresentAmountPipe } from '../../../shared/pipes/present-amount.pipe';
import { CurrencySwitcherComponent } from '../../../shared/ui/currency-switcher/currency-switcher.component';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { StatTileComponent } from '../../../shared/ui/stat-tile/stat-tile.component';

type FilterOption = {
  code: string;
  label: string;
};

type SortDirection = 'ASC' | 'DESC';

type OfferSortField = 'ISSUE_DATE' | 'VALID_TO';

type OfferProductFilter = 'ALL' | 'MOTOR' | 'CROP';

type StatusPresentation = {
  label: string;
  severity: 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';
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

type CropOfferPayload = {
  cropData?: {
    crops?: Array<{
      parcels?: unknown[];
    }>;
  };
};

@Component({
  selector: 'app-offers-home-page',
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    PageHeaderComponent,
    SectionCardComponent,
    StatTileComponent,
    ButtonDirective,
    Dialog,
    InputText,
    Select,
    SplitButton,
    Tag,
    PresentAmountPipe,
    CurrencySwitcherComponent
  ],
  templateUrl: './offers-home-page.component.html',
  styleUrl: './offers-home-page.component.scss',
  providers: [CurrencyPresentationService]
})
export class OffersHomePageComponent {
  private readonly offersRepository = inject(OffersRepository);
  private readonly referenceDataRepository = inject(ReferenceDataRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly router = inject(Router);
  protected readonly currencyPresentation = inject(CurrencyPresentationService);

  protected readonly searchTerm = signal('');
  protected readonly selectedStatus = signal<string | null>(null);
  protected readonly selectedProduct = signal<OfferProductFilter>('ALL');
  protected readonly selectedSortField = signal<OfferSortField>('ISSUE_DATE');
  protected readonly selectedSortDirection = signal<SortDirection>('DESC');
  protected readonly statusOverrides = signal<Record<string, OfferStatus>>({});
  protected readonly pendingTransition = signal<PendingTransition | null>(null);
  protected readonly transitionDialogVisible = signal(false);

  protected readonly offers = toSignal(this.offersRepository.getOffers(), { initialValue: [] as Offer[] });
  protected readonly referenceData = toSignal(this.referenceDataRepository.getReferenceData(), {
    initialValue: {
      offerStatuses: [],
      policyLines: [],
      salesChannels: [],
      vehicleUsages: [],
      vehicleFinancing: []
    } as ReferenceData
  });

  protected readonly statusOptions = computed<FilterOption[]>(() => [
    { code: 'ALL', label: 'Wszystkie statusy' },
    ...this.referenceData().offerStatuses
  ]);
  protected readonly allOffers = computed<Offer[]>(() => {
    const byId = new Map<string, Offer>();

    [...this.offers(), ...this.salesFlowRuntimeRepository.runtimeOffers()].forEach((offer) => byId.set(offer.id, offer));
    return Array.from(byId.values());
  });
  protected readonly offersWithRuntimeStatus = computed(() => {
    const overrides = this.statusOverrides();

    return this.allOffers().map((offer) => ({
      ...offer,
      status: overrides[offer.id] ?? offer.status
    }));
  });

  protected readonly sortFieldOptions: FilterOption[] = [
    { code: 'ISSUE_DATE', label: 'Data wystawienia' },
    { code: 'VALID_TO', label: 'Data ważności' }
  ];
  protected readonly productOptions: FilterOption[] = [
    { code: 'ALL', label: 'Wszystkie produkty' },
    { code: 'MOTOR', label: 'Komunikacyjne' },
    { code: 'CROP', label: 'Uprawy' }
  ];

  protected readonly filteredOffers = computed(() => {
    const normalizedSearch = this.searchTerm().trim().toLowerCase();
    const selectedStatus = this.selectedStatus();
    const selectedProduct = this.selectedProduct();

    const filtered = this.offersWithRuntimeStatus().filter((offer) => {
      const matchesStatus = !selectedStatus || selectedStatus === 'ALL' || offer.status === selectedStatus;
      const offerProduct = offer.product ?? 'MOTOR';
      const matchesProduct = selectedProduct === 'ALL' || offerProduct === selectedProduct;
      const matchesSearch =
        normalizedSearch.length === 0 ||
        offer.offerNumber.toLowerCase().includes(normalizedSearch) ||
        this.getCustomerDisplayName(offer).toLowerCase().includes(normalizedSearch) ||
        this.getOfferHeadlineSubject(offer).toLowerCase().includes(normalizedSearch) ||
        `${offer.vehicle.make} ${offer.vehicle.model}`.toLowerCase().includes(normalizedSearch) ||
        (offer.vehicle.registration?.registrationNumber ?? '').toLowerCase().includes(normalizedSearch);

      return matchesStatus && matchesProduct && matchesSearch;
    });

    return [...filtered].sort((left, right) => this.compareOffers(left, right));
  });
  protected readonly offerMenuModels = computed<Record<string, MenuItem[]>>(() => {
    const result: Record<string, MenuItem[]> = {};

    this.filteredOffers().forEach((offer) => {
      const transitions = this.getAvailableTransitions(offer.status).map((transition) => ({
        label: transition.actionLabel,
        icon: this.transitionIcon(transition.targetStatus),
        command: () => this.openTransitionDialog(offer, transition)
      }));

      const utilityItems: MenuItem[] = [];

      if (this.supportsPrint(offer.status)) {
        utilityItems.push({
          label: 'Wydruk',
          icon: 'pi pi-print',
          command: () => this.printPlaceholder(offer)
        });
      }

      utilityItems.push({
        label: 'Kopiuj',
        icon: 'pi pi-copy',
        command: () => this.copyOffer(offer.id)
      });

      result[offer.id] = [...transitions, ...(transitions.length > 0 ? [{ separator: true } as MenuItem] : []), ...utilityItems];
    });

    return result;
  });

  protected readonly summaryTiles = computed(() => {
    const offers = this.filteredOffers();
    const issued = offers.filter((offer) => offer.status === 'ISSUED').length;
    const inProgress = offers.filter((offer) => ['DRAFT', 'CALCULATION'].includes(offer.status)).length;
    const averageMonthlyPremium =
      offers.length > 0
        ? Math.round(
            offers.reduce((sum, offer) => sum + (offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0), 0) /
              offers.length /
              12
          )
        : 0;

    return [
      { label: 'Oferta wystawiona', value: `${issued}`, note: 'gotowe do decyzji klienta' },
      { label: 'Draft / Kalkulacja', value: `${inProgress}`, note: 'oferty w przygotowaniu' },
      {
        label: 'Średnia składka',
        value: this.currencyPresentation.formatAmount(averageMonthlyPremium),
        note: 'w ujęciu miesięcznym'
      }
    ];
  });

  protected readonly totalVisibleOffers = computed(() => this.filteredOffers().length);
  protected readonly filtersChanged = computed(
    () =>
      this.searchTerm() !== '' ||
      this.selectedStatus() !== 'ALL' ||
      this.selectedProduct() !== 'ALL' ||
      this.selectedSortField() !== 'ISSUE_DATE' ||
      this.selectedSortDirection() !== 'DESC'
  );

  protected getCustomerDisplayName(offer: Offer): string {
    const identity = offer.customer.identity;

    if (identity.type === 'LEGAL_ENTITY') {
      return identity.companyName;
    }

    if (identity.type === 'SOLE_PROPRIETOR') {
      return identity.companyName;
    }

    return `${identity.personName.firstName} ${identity.personName.lastName}`;
  }

  protected getStatusPresentation(status: OfferStatus): StatusPresentation {
    const statusMap: Record<OfferStatus, StatusPresentation> = {
      DRAFT: { label: 'Draft', severity: 'secondary' },
      CALCULATION: { label: 'Kalkulacja', severity: 'info' },
      ISSUED: { label: 'Oferta wystawiona', severity: 'success' },
      ACCEPTED: { label: 'Oferta zaakceptowana', severity: 'contrast' },
      POLICY: { label: 'Polisa', severity: 'success' },
      REJECTED: { label: 'Oferta odrzucona', severity: 'danger' },
      CANCELED: { label: 'Oferta anulowana', severity: 'warn' }
    };

    return statusMap[status];
  }

  protected getPrimaryPremium(offer: Offer): number {
    return offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0;
  }

  protected getSelectedVariantName(offer: Offer): string {
    const selected = offer.variants.find((variant) => variant.id === offer.selectedVariantId);
    return selected?.name ?? 'Brak wyboru';
  }

  protected isCropOffer(offer: Offer): boolean {
    return offer.product === 'CROP';
  }

  protected getOfferHeadlineSubject(offer: Offer): string {
    if (!this.isCropOffer(offer)) {
      return `${offer.vehicle.make} ${offer.vehicle.model}`.trim();
    }

    const { cropsCount, parcelsCount } = this.getCropCounts(offer);
    return `${cropsCount} upraw${cropsCount === 1 ? 'a' : ''} · ${parcelsCount} dział${parcelsCount === 1 ? 'ka' : 'ki'}`;
  }

  protected getCropMetaPrimaryLine(offer: Offer): string {
    const { cropsCount, parcelsCount } = this.getCropCounts(offer);
    return `${cropsCount} upraw${cropsCount === 1 ? 'a' : ''} · ${parcelsCount} dział${parcelsCount === 1 ? 'ka' : 'ki'}`;
  }

  protected getCropMetaSecondaryLine(offer: Offer): string {
    const city = offer.customer.residenceAddress?.city ?? '—';
    const identity = offer.customer.identity;
    const owner =
      identity.type === 'NATURAL_PERSON'
        ? identity.personName.lastName
        : identity.type === 'SOLE_PROPRIETOR' || identity.type === 'LEGAL_ENTITY'
          ? identity.companyName
          : 'gospodarstwo';

    return `${city} · ${owner}`;
  }

  protected isRenewalOffer(offer: Offer): boolean {
    return !!offer.renewalContext && offer.renewalContext.mode === 'RENEW';
  }

  protected openTransitionDialog(offer: Offer, transition: TransitionDefinition): void {
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

    this.statusOverrides.update((overrides) => ({
      ...overrides,
      [pending.offer.id]: pending.transition.targetStatus
    }));

    const transitionedOffer: Offer = {
      ...pending.offer,
      status: pending.transition.targetStatus,
      updatedAt: new Date().toISOString()
    };
    this.salesFlowRuntimeRepository.saveDraftOffer(transitionedOffer);

    if (pending.transition.targetStatus === 'POLICY') {
      this.salesFlowRuntimeRepository.promoteOfferToPolicy(transitionedOffer);
      this.closeTransitionDialog();
      void this.router.navigate(['/policies']);
      return;
    }

    this.closeTransitionDialog();
  }

  protected clearAllFilters(): void {
    this.searchTerm.set('');
    this.selectedStatus.set('ALL');
    this.selectedProduct.set('ALL');
    this.selectedSortField.set('ISSUE_DATE');
    this.selectedSortDirection.set('DESC');
  }

  protected toggleSortDirection(): void {
    this.selectedSortDirection.update((direction) => (direction === 'DESC' ? 'ASC' : 'DESC'));
  }

  protected goToOffer(offerId: string): void {
    const offer = this.offersWithRuntimeStatus().find((item) => item.id === offerId);

    if (offer?.product === 'CROP') {
      void this.router.navigate(['/offers', offerId, 'crop', 'crop']);
      return;
    }

    void this.router.navigate(['/offers', offerId, 'vehicle']);
  }

  protected sortDirectionLabel(): string {
    return this.selectedSortDirection() === 'DESC' ? 'Malejąco' : 'Rosnąco';
  }

  protected sortDirectionIcon(): string {
    return this.selectedSortDirection() === 'DESC' ? 'pi pi-sort-amount-down' : 'pi pi-sort-amount-up-alt';
  }

  protected transitionTitle(): string {
    return this.pendingTransition()?.transition.title ?? '';
  }

  protected transitionDescription(): string {
    return this.pendingTransition()?.transition.description ?? '';
  }

  protected transitionFromStatusLabel(): string {
    const status = this.pendingTransition()?.offer.status;
    return status ? this.getStatusPresentation(status).label : '';
  }

  protected transitionToStatusLabel(): string {
    const status = this.pendingTransition()?.transition.targetStatus;
    return status ? this.getStatusPresentation(status).label : '';
  }

  protected transitionSubjectLabel(offer: Offer): string {
    return this.isCropOffer(offer) ? 'Przedmiot' : 'Pojazd';
  }

  protected transitionSubjectValue(offer: Offer): string {
    return this.isCropOffer(offer) ? this.getCropMetaPrimaryLine(offer) : `${offer.vehicle.make} ${offer.vehicle.model}`.trim();
  }

  protected offerProductIcon(offer: Offer): string {
    return this.isCropOffer(offer) ? 'pi pi-leaf' : 'pi pi-car';
  }

  protected offerProductLabel(offer: Offer): string {
    return this.isCropOffer(offer) ? 'Oferta upraw' : 'Oferta komunikacyjna';
  }

  private copyOffer(offerId: string): void {
    void this.router.navigate(['/offers/new/motor/vehicle'], { queryParams: { copyFrom: offerId } });
  }

  private getAvailableTransitions(status: OfferStatus): TransitionDefinition[] {
    const transitions: TransitionDefinition[] = [];

    switch (status) {
      case 'CALCULATION':
        transitions.push({
          actionLabel: 'Wystaw',
          title: 'Wystawienie oferty',
          description: 'Czy na pewno chcesz wystawić ofertę na podstawie tej kalkulacji?',
          targetStatus: 'ISSUED'
        });
        break;
      case 'ISSUED':
        transitions.push({
          actionLabel: 'Akceptuj',
          title: 'Akceptacja oferty',
          description: 'Czy potwierdzasz akceptację tej oferty?',
          targetStatus: 'ACCEPTED'
        });
        transitions.push({
          actionLabel: 'Odrzuć',
          title: 'Odrzucenie oferty',
          description: 'Czy na pewno chcesz oznaczyć ofertę jako odrzuconą?',
          targetStatus: 'REJECTED'
        });
        break;
      case 'ACCEPTED':
        transitions.push({
          actionLabel: 'Polisuj',
          title: 'Polisowanie oferty',
          description: 'Czy chcesz przejść z zaakceptowanej oferty do statusu polisa?',
          targetStatus: 'POLICY'
        });
        break;
      default:
        break;
    }

    if (status !== 'CANCELED' && status !== 'POLICY') {
      transitions.push({
        actionLabel: 'Anuluj',
        title: 'Anulowanie oferty',
        description: 'Czy na pewno chcesz anulować tę ofertę?',
        targetStatus: 'CANCELED'
      });
    }

    return transitions;
  }

  private transitionIcon(targetStatus: OfferStatus): string {
    switch (targetStatus) {
      case 'ISSUED':
        return 'pi pi-send';
      case 'ACCEPTED':
        return 'pi pi-check';
      case 'REJECTED':
        return 'pi pi-times';
      case 'POLICY':
        return 'pi pi-file';
      case 'CANCELED':
        return 'pi pi-ban';
      default:
        return 'pi pi-angle-right';
    }
  }

  private supportsPrint(status: OfferStatus): boolean {
    return status === 'ISSUED' || status === 'ACCEPTED';
  }

  private printPlaceholder(offer: Offer): void {
    console.log('[Offers] Print placeholder action triggered for offer', offer.id);
  }

  private compareOffers(left: Offer, right: Offer): number {
    const sortField = this.selectedSortField();

    if (sortField === 'VALID_TO') {
      return this.compareNullableDates(left.validTo, right.validTo);
    }

    return this.compareNullableDates(left.createdAt, right.createdAt);
  }

  private compareNullableDates(left?: string, right?: string): number {
    if (!left && !right) {
      return 0;
    }

    if (!left) {
      return 1;
    }

    if (!right) {
      return -1;
    }

    const leftTime = new Date(left).getTime();
    const rightTime = new Date(right).getTime();

    if (leftTime === rightTime) {
      return 0;
    }

    const naturalOrder = leftTime > rightTime ? 1 : -1;
    return this.selectedSortDirection() === 'ASC' ? naturalOrder : -naturalOrder;
  }

  private getCropCounts(offer: Offer): { cropsCount: number; parcelsCount: number } {
    const payload = offer as Offer & CropOfferPayload;
    const crops = payload.cropData?.crops ?? [];
    const parcelsCount = crops.reduce((sum, crop) => sum + (crop.parcels?.length ?? 0), 0);

    return {
      cropsCount: crops.length,
      parcelsCount
    };
  }
}
