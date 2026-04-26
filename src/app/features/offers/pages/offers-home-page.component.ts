import { CommonModule } from '@angular/common'
import { Component, computed, inject, signal } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { FormsModule } from '@angular/forms'
import { Router, RouterLink } from '@angular/router'
import { MenuItem } from 'primeng/api'
import { ButtonDirective } from 'primeng/button'
import { Dialog } from 'primeng/dialog'
import { InputText } from 'primeng/inputtext'
import { Select } from 'primeng/select'
import { SplitButton } from 'primeng/splitbutton'
import { Tag } from 'primeng/tag'
import { Offer, OfferStatus, ReferenceData } from '../../../core/models'
import { OffersRepository } from '../../../core/repositories/offers.repository'
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository'
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository'
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component'
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component'
import { StatTileComponent } from '../../../shared/ui/stat-tile/stat-tile.component'
import { getCoveragePeriodLabel } from '../utils/coverage-period.util'

type FilterOption = {
  code: string
  label: string
}

type SortDirection = 'ASC' | 'DESC'

type OfferSortField = 'ISSUE_DATE' | 'VALID_TO'

type OfferProductFilter = 'ALL' | 'MOTOR' | 'CROP'

type OfferStatusFilter = 'ALL' | OfferStatus

type StatusPresentation = {
  label: string
  severity: 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast'
}

type TransitionDefinition = {
  actionLabel: string
  title: string
  description: string
  targetStatus: OfferStatus
}

type PendingTransition = {
  offer: Offer
  transition: TransitionDefinition
}

type CropOfferPayload = {
  cropData?: {
    crops?: Array<{
      parcels?: unknown[]
    }>
  }
}

@Component({
  selector: 'app-offers-home-page',
  standalone: true,
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
  styleUrls: ['./offers-home-page.component.scss']
})
export class OffersHomePageComponent {
  private readonly offersRepository = inject(OffersRepository)
  private readonly referenceDataRepository = inject(ReferenceDataRepository)
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository)
  private readonly router = inject(Router)

  readonly searchTerm = signal('')
  readonly selectedStatus = signal<OfferStatusFilter>('ALL')
  readonly selectedProduct = signal<OfferProductFilter>('ALL')
  readonly selectedSortField = signal<OfferSortField>('ISSUE_DATE')
  readonly selectedSortDirection = signal<SortDirection>('DESC')
  readonly statusOverrides = signal<Record<string, OfferStatus>>({})
  readonly pendingTransition = signal<PendingTransition | null>(null)
  readonly transitionDialogVisible = signal(false)

  readonly offers = toSignal(this.offersRepository.getOffers(), { initialValue: [] as Offer[] })
  readonly referenceData = toSignal(this.referenceDataRepository.getReferenceData(), {
    initialValue: {
      offerStatuses: [],
      policyLines: [],
      salesChannels: [],
      vehicleUsages: [],
      vehicleFinancing: []
    } as ReferenceData
  })
  readonly coveragePeriodLabel = computed(() => getCoveragePeriodLabel())

  readonly statusOptions = computed<FilterOption[]>(() => [
    { code: 'ALL', label: 'Wszystkie statusy' },
    ...this.referenceData().offerStatuses
  ])

  readonly sortFieldOptions: FilterOption[] = [
    { code: 'ISSUE_DATE', label: 'Data wystawienia' },
    { code: 'VALID_TO', label: 'Data ważności' }
  ]

  readonly productOptions: FilterOption[] = [
    { code: 'ALL', label: 'Wszystkie produkty' },
    { code: 'MOTOR', label: 'Komunikacyjne' },
    { code: 'CROP', label: 'Uprawy' }
  ]

  readonly allOffers = computed<Offer[]>(() => {
    const byId = new Map<string, Offer>()

    for (const offer of [...this.offers(), ...this.salesFlowRuntimeRepository.runtimeOffers()]) {
      byId.set(offer.id, offer)
    }

    return Array.from(byId.values())
  })

  readonly offersWithRuntimeStatus = computed<Offer[]>(() => {
    const overrides = this.statusOverrides()

    return this.allOffers().map((offer) => ({
      ...offer,
      status: overrides[offer.id] ?? offer.status
    }))
  })

  readonly filteredOffers = computed<Offer[]>(() => {
    const normalizedSearch = this.searchTerm().trim().toLowerCase()
    const selectedStatus = this.selectedStatus()
    const selectedProduct = this.selectedProduct()

    const filtered = this.offersWithRuntimeStatus().filter((offer) => {
      const matchesStatus = selectedStatus === 'ALL' || offer.status === selectedStatus
      const offerProduct = offer.product ?? 'MOTOR'
      const matchesProduct = selectedProduct === 'ALL' || offerProduct === selectedProduct
      const matchesSearch =
        normalizedSearch.length === 0 ||
        offer.offerNumber.toLowerCase().includes(normalizedSearch) ||
        this.getCustomerDisplayName(offer).toLowerCase().includes(normalizedSearch) ||
        this.getOfferHeadlineSubject(offer).toLowerCase().includes(normalizedSearch) ||
        `${offer.vehicle.make} ${offer.vehicle.model}`.toLowerCase().includes(normalizedSearch) ||
        (offer.vehicle.registration?.registrationNumber ?? '').toLowerCase().includes(normalizedSearch)

      return matchesStatus && matchesProduct && matchesSearch
    })

    return [...filtered].sort((left, right) => this.compareOffers(left, right))
  })

  readonly offerMenuModels = computed<Record<string, MenuItem[]>>(() => {
    const result: Record<string, MenuItem[]> = {}

    for (const offer of this.filteredOffers()) {
      const transitions = this.getAvailableTransitions(offer.status).map((transition) => ({
        label: transition.actionLabel,
        icon: this.transitionIcon(transition.targetStatus),
        command: () => this.openTransitionDialog(offer, transition)
      }))

      const utilityItems: MenuItem[] = []

      if (this.supportsPrint(offer.status)) {
        utilityItems.push({
          label: 'Wydruk',
          icon: 'pi pi-print',
          command: () => this.printPlaceholder(offer)
        })
      }

      utilityItems.push({
        label: 'Kopiuj',
        icon: 'pi pi-copy',
        command: () => this.copyOffer(offer.id)
      })

      result[offer.id] = [
        ...transitions,
        ...(transitions.length > 0 ? ([{ separator: true }] as MenuItem[]) : []),
        ...utilityItems
      ]
    }

    return result
  })

  readonly summaryTiles = computed(() => {
    const offers = this.filteredOffers()
    const issued = offers.filter((offer) => offer.status === 'ISSUED').length
    const inProgress = offers.filter((offer) => ['DRAFT', 'CALCULATION'].includes(offer.status)).length
    const totalPremium = offers.reduce(
      (sum, offer) => sum + (offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0),
      0
    )
    const averageMonthlyPremium = offers.length > 0 ? Math.round(totalPremium / offers.length / 12) : 0

    return [
      { label: 'Oferta wystawiona', value: `${issued}`, note: 'gotowe do decyzji klienta' },
      { label: 'Draft / Kalkulacja', value: `${inProgress}`, note: 'oferty w przygotowaniu' },
      { label: 'Średnia składka', value: `${averageMonthlyPremium.toLocaleString('pl-PL')} zł`, note: 'w ujęciu miesięcznym' }
    ]
  })

  readonly totalVisibleOffers = computed(() => this.filteredOffers().length)

  readonly filtersChanged = computed(() => {
    return (
      this.searchTerm() !== '' ||
      this.selectedStatus() !== 'ALL' ||
      this.selectedProduct() !== 'ALL' ||
      this.selectedSortField() !== 'ISSUE_DATE' ||
      this.selectedSortDirection() !== 'DESC'
    )
  })

  getCustomerDisplayName(offer: Offer): string {
    const identity = offer.customer.identity

    if (identity.type === 'LEGAL_ENTITY' || identity.type === 'SOLE_PROPRIETOR') {
      return identity.companyName
    }

    return `${identity.personName.firstName} ${identity.personName.lastName}`.trim()
  }

  getStatusPresentation(status: OfferStatus): StatusPresentation {
    const statusMap: Record<OfferStatus, StatusPresentation> = {
      DRAFT: { label: 'Draft', severity: 'secondary' },
      CALCULATION: { label: 'Kalkulacja', severity: 'info' },
      ISSUED: { label: 'Oferta wystawiona', severity: 'success' },
      ACCEPTED: { label: 'Oferta zaakceptowana', severity: 'contrast' },
      POLICY: { label: 'Polisa', severity: 'success' },
      REJECTED: { label: 'Oferta odrzucona', severity: 'danger' },
      CANCELED: { label: 'Oferta anulowana', severity: 'warn' }
    }

    return statusMap[status]
  }

  getPrimaryPremium(offer: Offer): number {
    return offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0
  }

  getSelectedVariantName(offer: Offer): string {
    const selected = offer.variants.find((variant) => variant.id === offer.selectedVariantId)
    return selected?.name ?? 'Brak wyboru'
  }

  isCropOffer(offer: Offer): boolean {
    return offer.product === 'CROP'
  }

  getOfferHeadlineSubject(offer: Offer): string {
    if (!this.isCropOffer(offer)) {
      return `${offer.vehicle.make} ${offer.vehicle.model}`.trim()
    }

    const { cropsCount, parcelsCount } = this.getCropCounts(offer)
    return `${cropsCount} upraw${cropsCount === 1 ? 'a' : ''} · ${parcelsCount} dział${parcelsCount === 1 ? 'ka' : 'ki'}`
  }

  getCropMetaPrimaryLine(offer: Offer): string {
    const { cropsCount, parcelsCount } = this.getCropCounts(offer)
    return `${cropsCount} upraw${cropsCount === 1 ? 'a' : ''} · ${parcelsCount} dział${parcelsCount === 1 ? 'ka' : 'ki'}`
  }

  getCropMetaSecondaryLine(offer: Offer): string {
    const city = offer.customer.residenceAddress?.city ?? '—'
    const identity = offer.customer.identity
    const owner =
      identity.type === 'NATURAL_PERSON'
        ? identity.personName.lastName
        : identity.companyName

    return `${city} · ${owner}`
  }

  isRenewalOffer(offer: Offer): boolean {
    return offer.renewalContext?.mode === 'RENEW'
  }

  openTransitionDialog(offer: Offer, transition: TransitionDefinition): void {
    this.pendingTransition.set({ offer, transition })
    this.transitionDialogVisible.set(true)
  }

  closeTransitionDialog(): void {
    this.transitionDialogVisible.set(false)
    this.pendingTransition.set(null)
  }

  confirmTransition(): void {
    const pending = this.pendingTransition()

    if (!pending) {
      return
    }

    this.statusOverrides.update((overrides) => ({
      ...overrides,
      [pending.offer.id]: pending.transition.targetStatus
    }))

    const transitionedOffer: Offer = {
      ...pending.offer,
      status: pending.transition.targetStatus,
      updatedAt: new Date().toISOString()
    }

    this.salesFlowRuntimeRepository.saveDraftOffer(transitionedOffer)

    if (pending.transition.targetStatus === 'POLICY') {
      this.salesFlowRuntimeRepository.promoteOfferToPolicy(transitionedOffer)
      this.closeTransitionDialog()
      void this.router.navigate(['/policies'])
      return
    }

    this.closeTransitionDialog()
  }

  clearAllFilters(): void {
    this.searchTerm.set('')
    this.selectedStatus.set('ALL')
    this.selectedProduct.set('ALL')
    this.selectedSortField.set('ISSUE_DATE')
    this.selectedSortDirection.set('DESC')
  }

  toggleSortDirection(): void {
    this.selectedSortDirection.update((direction) => (direction === 'DESC' ? 'ASC' : 'DESC'))
  }

  goToOffer(offerId: string): void {
    const offer = this.offersWithRuntimeStatus().find((item) => item.id === offerId)

    if (offer?.product === 'CROP') {
      void this.router.navigate(['/offers', offerId, 'crop', 'crop'])
      return
    }

    void this.router.navigate(['/offers', offerId, 'vehicle'])
  }

  sortDirectionLabel(): string {
    return this.selectedSortDirection() === 'DESC' ? 'Malejąco' : 'Rosnąco'
  }

  sortDirectionIcon(): string {
    return this.selectedSortDirection() === 'DESC' ? 'pi pi-sort-amount-down' : 'pi pi-sort-amount-up-alt'
  }

  transitionTitle(): string {
    return this.pendingTransition()?.transition.title ?? ''
  }

  transitionDescription(): string {
    return this.pendingTransition()?.transition.description ?? ''
  }

  transitionFromStatusLabel(): string {
    const status = this.pendingTransition()?.offer.status
    return status ? this.getStatusPresentation(status).label : ''
  }

  transitionToStatusLabel(): string {
    const status = this.pendingTransition()?.transition.targetStatus
    return status ? this.getStatusPresentation(status).label : ''
  }

  transitionSubjectLabel(offer: Offer): string {
    return this.isCropOffer(offer) ? 'Przedmiot' : 'Pojazd'
  }

  transitionSubjectValue(offer: Offer): string {
    return this.isCropOffer(offer) ? this.getCropMetaPrimaryLine(offer) : `${offer.vehicle.make} ${offer.vehicle.model}`.trim()
  }

  offerProductIcon(offer: Offer): string {
    return this.isCropOffer(offer) ? 'pi pi-leaf' : 'pi pi-car'
  }

  offerProductLabel(offer: Offer): string {
    return this.isCropOffer(offer) ? 'Oferta upraw' : 'Oferta komunikacyjna'
  }

  private copyOffer(offerId: string): void {
    void this.router.navigate(['/offers/new/motor/vehicle'], { queryParams: { copyFrom: offerId } })
  }

  private getAvailableTransitions(status: OfferStatus): TransitionDefinition[] {
    const transitions: TransitionDefinition[] = []

    switch (status) {
      case 'CALCULATION':
        transitions.push({
          actionLabel: 'Wystaw',
          title: 'Wystawienie oferty',
          description: 'Czy na pewno chcesz wystawić ofertę na podstawie tej kalkulacji?',
          targetStatus: 'ISSUED'
        })
        break
      case 'ISSUED':
        transitions.push({
          actionLabel: 'Akceptuj',
          title: 'Akceptacja oferty',
          description: 'Czy potwierdzasz akceptację tej oferty?',
          targetStatus: 'ACCEPTED'
        })
        transitions.push({
          actionLabel: 'Odrzuć',
          title: 'Odrzucenie oferty',
          description: 'Czy na pewno chcesz oznaczyć ofertę jako odrzuconą?',
          targetStatus: 'REJECTED'
        })
        break
      case 'ACCEPTED':
        transitions.push({
          actionLabel: 'Polisuj',
          title: 'Polisowanie oferty',
          description: 'Czy chcesz przejść z zaakceptowanej oferty do statusu polisa?',
          targetStatus: 'POLICY'
        })
        break
      default:
        break
    }

    if (status !== 'CANCELED' && status !== 'POLICY') {
      transitions.push({
        actionLabel: 'Anuluj',
        title: 'Anulowanie oferty',
        description: 'Czy na pewno chcesz anulować tę ofertę?',
        targetStatus: 'CANCELED'
      })
    }

    return transitions
  }

  private transitionIcon(targetStatus: OfferStatus): string {
    switch (targetStatus) {
      case 'ISSUED':
        return 'pi pi-send'
      case 'ACCEPTED':
        return 'pi pi-check'
      case 'REJECTED':
        return 'pi pi-times'
      case 'POLICY':
        return 'pi pi-file'
      case 'CANCELED':
        return 'pi pi-ban'
      default:
        return 'pi pi-angle-right'
    }
  }

  private supportsPrint(status: OfferStatus): boolean {
    return status === 'ISSUED' || status === 'ACCEPTED'
  }

  private printPlaceholder(offer: Offer): void {
    console.log('[Offers] Print placeholder action triggered for offer', offer.id)
  }

  private compareOffers(left: Offer, right: Offer): number {
    const naturalOrder =
      this.selectedSortField() === 'VALID_TO'
        ? this.compareNullableDates(left.validTo, right.validTo)
        : this.compareNullableDates(left.createdAt, right.createdAt)

    return this.selectedSortDirection() === 'ASC' ? naturalOrder : -naturalOrder
  }

  private compareNullableDates(left?: string, right?: string): number {
    if (!left && !right) {
      return 0
    }

    if (!left) {
      return 1
    }

    if (!right) {
      return -1
    }

    const leftTime = new Date(left).getTime()
    const rightTime = new Date(right).getTime()

    if (leftTime === rightTime) {
      return 0
    }

    return leftTime > rightTime ? 1 : -1
  }

  private getCropCounts(offer: Offer): { cropsCount: number; parcelsCount: number } {
    const payload = offer as Offer & CropOfferPayload
    const crops = payload.cropData?.crops ?? []
    const parcelsCount = crops.reduce((sum, crop) => sum + (crop.parcels?.length ?? 0), 0)

    return {
      cropsCount: crops.length,
      parcelsCount
    }
  }
}
