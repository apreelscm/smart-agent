import { CommonModule } from '@angular/common';
import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { SplitButton } from 'primeng/splitbutton';
import { Tag } from 'primeng/tag';
import {
  CurrencyCode,
  ExchangeRateQuote,
  ForeignCurrencyCode,
  Offer,
  OfferStatus,
  ReferenceData
} from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { StatTileComponent } from '../../../shared/ui/stat-tile/stat-tile.component';

type FilterOption = {
  code: string;
  label: string;
};

type CurrencyOption = {
  code: CurrencyCode;
  label: string;
  disabled?: boolean;
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

type CurrencyStatusPresentation = {
  kind: 'info' | 'success' | 'loading' | 'error';
  icon: string;
  message: string;
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
    Tag
  ],
  templateUrl: './offers-home-page.component.html',
  styleUrl: './offers-home-page.component.scss'
})
export class OffersHomePageComponent {
  private readonly offersRepository = inject(OffersRepository);
  private readonly referenceDataRepository = inject(ReferenceDataRepository);
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly supportedForeignCurrencies: ForeignCurrencyCode[] = ['EUR', 'USD'];

  private exchangeRateRequestId = 0;

  protected readonly searchTerm = signal('');
  protected readonly selectedStatus = signal<string | null>(null);
  protected readonly selectedProduct = signal<OfferProductFilter>('ALL');
  protected readonly selectedSortField = signal<OfferSortField>('ISSUE_DATE');
  protected readonly selectedSortDirection = signal<SortDirection>('DESC');
  protected readonly statusOverrides = signal<Record<string, OfferStatus>>({});
  protected readonly pendingTransition = signal<PendingTransition | null>(null);
  protected readonly transitionDialogVisible = signal(false);
  protected readonly selectedDisplayCurrency = signal<CurrencyCode>('PLN');
  protected readonly pendingDisplayCurrency = signal<ForeignCurrencyCode | null>(null);
  protected readonly exchangeRateQuotes = signal<Partial<Record<ForeignCurrencyCode, ExchangeRateQuote>>>({});
  protected readonly unavailableDisplayCurrencies = signal<Partial<Record<ForeignCurrencyCode, true>>>({});
  protected readonly displayCurrencyMessage = signal<string | null>(null);

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
  protected readonly displayCurrencyOptions = computed<CurrencyOption[]>(() => {
    const unavailableCurrencies = this.unavailableDisplayCurrencies();
    const quotes = this.exchangeRateQuotes();

    return [
      { code: 'PLN', label: 'PLN' },
      ...this.supportedForeignCurrencies.map((code) => ({
        code,
        label: code,
        disabled: !!unavailableCurrencies[code] && !quotes[code]
      }))
    ];
  });
  protected readonly activeExchangeRate = computed<ExchangeRateQuote | null>(() => {
    const displayCurrency = this.selectedDisplayCurrency();

    if (displayCurrency === 'PLN') {
      return null;
    }

    return this.exchangeRateQuotes()[displayCurrency] ?? null;
  });
  protected readonly displayCurrencyStatus = computed<CurrencyStatusPresentation>(() => {
    const pendingCurrency = this.pendingDisplayCurrency();
    const activeCurrency = this.selectedDisplayCurrency();
    const message = this.displayCurrencyMessage();

    if (pendingCurrency) {
      return {
        kind: 'loading',
        icon: 'pi pi-spin pi-spinner',
        message: `Pobieranie kursu NBP dla ${pendingCurrency}. Aktywna waluta pozostaje ${activeCurrency}.`
      };
    }

    if (message) {
      return {
        kind: 'error',
        icon: 'pi pi-exclamation-triangle',
        message
      };
    }

    if (activeCurrency === 'PLN') {
      return {
        kind: 'info',
        icon: 'pi pi-money-bill',
        message: 'Aktywna waluta: PLN · kwoty źródłowe bez przeliczenia'
      };
    }

    const rate = this.activeExchangeRate();

    return {
      kind: 'success',
      icon: 'pi pi-check-circle',
      message: rate
        ? `Aktywna waluta: ${activeCurrency} · kurs NBP z ${this.formatDateLabel(rate.effectiveDate)}`
        : `Aktywna waluta: ${activeCurrency}`
    };
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
    const averageMonthlyPremiumInPln =
      offers.length > 0 ? offers.reduce((sum, offer) => sum + this.getBasePremiumAmount(offer), 0) / offers.length / 12 : 0;

    return [
      { label: 'Oferta wystawiona', value: `${issued}`, note: 'gotowe do decyzji klienta' },
      { label: 'Draft / Kalkulacja', value: `${inProgress}`, note: 'oferty w przygotowaniu' },
      { label: 'Średnia składka', value: this.formatDisplayAmount(this.convertAmountFromPln(averageMonthlyPremiumInPln)), note: 'w ujęciu miesięcznym' }
    ];
  });

  protected readonly totalVisibleOffers = computed(() => this.filteredOffers().length);

  protected readonly filtersChanged = computed(() => {
    return (
      this.searchTerm() !== '' ||
      this.selectedStatus() !== 'ALL' ||
      this.selectedProduct() !== 'ALL' ||
      this.selectedSortField() !== 'ISSUE_DATE' ||
      this.selectedSortDirection() !== 'DESC'
    );
  });

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

  protected getBasePremiumAmount(offer: Offer): number {
    return offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0;
  }

  protected getDisplayPremium(offer: Offer): string {
    return this.formatDisplayAmount(this.convertAmountFromPln(this.getBasePremiumAmount(offer)));
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

  protected selectDisplayCurrency(nextCurrency: CurrencyCode): void {
    if (nextCurrency === this.pendingDisplayCurrency()) {
      return;
    }

    if (nextCurrency === this.selectedDisplayCurrency()) {
      this.displayCurrencyMessage.set(null);
      return;
    }

    if (nextCurrency === 'PLN') {
      this.exchangeRateRequestId += 1;
      this.pendingDisplayCurrency.set(null);
      this.displayCurrencyMessage.set(null);
      this.selectedDisplayCurrency.set('PLN');
      return;
    }

    const cachedQuote = this.exchangeRateQuotes()[nextCurrency];

    if (cachedQuote) {
      this.exchangeRateRequestId += 1;
      this.pendingDisplayCurrency.set(null);
      this.displayCurrencyMessage.set(null);
      this.selectedDisplayCurrency.set(nextCurrency);
      this.markCurrencyAvailable(nextCurrency);
      return;
    }

    if (this.unavailableDisplayCurrencies()[nextCurrency]) {
      this.displayCurrencyMessage.set(`Waluta ${nextCurrency} jest obecnie niedostępna. Aktywna waluta pozostaje ${this.selectedDisplayCurrency()}.`);
      return;
    }

    this.displayCurrencyMessage.set(null);
    this.pendingDisplayCurrency.set(nextCurrency);

    const requestId = ++this.exchangeRateRequestId;

    this.exchangeRatesRepository
      .getExchangeRate(nextCurrency)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (quote) => {
          if (requestId !== this.exchangeRateRequestId) {
            return;
          }

          this.exchangeRateQuotes.update((quotes) => ({
            ...quotes,
            [nextCurrency]: quote
          }));
          this.markCurrencyAvailable(nextCurrency);
          this.selectedDisplayCurrency.set(nextCurrency);
          this.pendingDisplayCurrency.set(null);
          this.displayCurrencyMessage.set(null);
        },
        error: () => {
          if (requestId !== this.exchangeRateRequestId) {
            return;
          }

          this.unavailableDisplayCurrencies.update((unavailable) => ({
            ...unavailable,
            [nextCurrency]: true
          }));
          this.pendingDisplayCurrency.set(null);
          this.displayCurrencyMessage.set(
            `Nie udało się pobrać kursu NBP dla ${nextCurrency}. Pozostawiono ${this.selectedDisplayCurrency()} jako aktywną walutę.`
          );
        }
      });
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

  private convertAmountFromPln(amountInPln: number): number {
    const displayCurrency = this.selectedDisplayCurrency();

    if (displayCurrency === 'PLN') {
      return amountInPln;
    }

    const quote = this.exchangeRateQuotes()[displayCurrency];

    if (!quote) {
      return amountInPln;
    }

    return amountInPln / quote.mid;
  }

  private formatDisplayAmount(amount: number): string {
    const displayCurrency = this.selectedDisplayCurrency();
    const fractionDigits = this.getCurrencyFractionDigits(displayCurrency);

    return new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: displayCurrency,
      currencyDisplay: 'narrowSymbol',
      minimumFractionDigits: fractionDigits,
      maximumFractionDigits: fractionDigits
    }).format(amount);
  }

  private getCurrencyFractionDigits(currency: CurrencyCode): number {
    return currency === 'PLN' ? 0 : 2;
  }

  private formatDateLabel(date: string): string {
    const [year, month, day] = date.split('-');

    if (!year || !month || !day) {
      return date;
    }

    return `${day}.${month}.${year}`;
  }

  private markCurrencyAvailable(code: ForeignCurrencyCode): void {
    this.unavailableDisplayCurrencies.update((unavailable) => {
      const nextState = { ...unavailable };
      delete nextState[code];
      return nextState;
    });
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
