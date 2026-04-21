import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonDirective } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { SplitButton } from 'primeng/splitbutton';
import { Tag } from 'primeng/tag';
import { CurrencyCode, ExchangeRateSnapshot, Policy, PolicyPaymentStatus, PolicyStatus } from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyConversionService } from '../../../core/services/currency-conversion.service';
import { PresentedMoneyPipe } from '../../../shared/pipes/presented-money.pipe';
import { CurrencySwitcherComponent } from '../../../shared/ui/currency-switcher/currency-switcher.component';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';

type PolicyViewFilter = 'ALL' | 'RENEWALS' | 'TO_PAY';

type SortDirection = 'ASC' | 'DESC';

type PolicySortField = 'ISSUE_DATE' | 'CONCLUSION_DATE' | 'COVERAGE_END_DATE';

type SortFieldOption = {
  code: PolicySortField;
  label: string;
};

type PolicyStatusPresentation = {
  label: string;
  severity: 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast';
};

@Component({
  selector: 'app-policies-home-page',
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    PageHeaderComponent,
    SectionCardComponent,
    ButtonDirective,
    Dialog,
    InputText,
    Select,
    SplitButton,
    Tag,
    CurrencySwitcherComponent,
    PresentedMoneyPipe
  ],
  templateUrl: './policies-home-page.component.html',
  styleUrl: './policies-home-page.component.scss'
})
export class PoliciesHomePageComponent {
  private readonly policiesRepository = inject(PoliciesRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly currencyConversionService = inject(CurrencyConversionService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  protected readonly searchTerm = signal('');
  protected readonly selectedFilter = signal<PolicyViewFilter>('ALL');
  protected readonly selectedSortField = signal<PolicySortField>('ISSUE_DATE');
  protected readonly selectedSortDirection = signal<SortDirection>('DESC');
  protected readonly statusOverrides = signal<Record<string, PolicyStatus>>({});
  protected readonly paymentStatusOverrides = signal<Record<string, PolicyPaymentStatus>>({});
  protected readonly pendingCancellation = signal<Policy | null>(null);
  protected readonly cancellationDialogVisible = signal(false);
  protected readonly pendingRenewal = signal<Policy | null>(null);
  protected readonly renewalDialogVisible = signal(false);
  protected readonly pendingPayment = signal<Policy | null>(null);
  protected readonly paymentDialogVisible = signal(false);
  protected readonly selectedCurrency = signal<CurrencyCode>('PLN');
  protected readonly ratesError = signal(false);

  protected readonly basePolicies = toSignal(this.policiesRepository.getPolicies(), { initialValue: [] as Policy[] });
  protected readonly exchangeRates = toSignal(this.exchangeRatesRepository.getCurrentRates(), {
    initialValue: null as ExchangeRateSnapshot | null,
    rejectErrors: true
  });
  protected readonly availability = computed(() =>
    this.currencyConversionService.getAvailability(this.exchangeRates(), this.ratesError())
  );
  protected readonly disabledCurrencies = computed<CurrencyCode[]>(() => this.availability().unavailableCurrencies);
  protected readonly currentRateNote = computed(() =>
    this.currencyConversionService.rateNote(this.selectedCurrency(), this.exchangeRates())
  );

  protected readonly policies = computed<Policy[]>(() => {
    const runtimePolicies = this.salesFlowRuntimeRepository.promotedPolicies();
    const overrides = this.statusOverrides();
    const byId = new Map<string, Policy>();

    [...runtimePolicies, ...this.basePolicies()].forEach((policy) => byId.set(policy.id, policy));
    return Array.from(byId.values()).map((policy) => ({
      ...policy,
      status: overrides[policy.id] ?? policy.status,
      paymentStatus: this.paymentStatusOverrides()[policy.id] ?? policy.paymentStatus
    }));
  });
  protected readonly policyMenuModels = computed<Record<string, MenuItem[]>>(() => {
    const result: Record<string, MenuItem[]> = {};

    this.policies().forEach((policy) => {
      const items: MenuItem[] = [
        {
          label: 'Kopiuj',
          icon: 'pi pi-copy',
          command: () => {
            this.debug('menu: Kopiuj', { policyId: policy.id });
            this.copyPolicyToOffer(policy.id);
          }
        }
      ];

      if (policy.paymentStatus === 'PAID' && policy.status !== 'CANCELED') {
        items.push({
          label: 'Wznów',
          icon: 'pi pi-refresh',
          command: () => {
            this.debug('menu: Wznów', { policyId: policy.id });
            this.openRenewalDialog(policy);
          }
        });
      }

      if (policy.paymentStatus !== 'PAID' && policy.status !== 'CANCELED') {
        items.push({
          label: 'Opłacenie',
          icon: 'pi pi-wallet',
          command: () => {
            this.debug('menu: Opłacenie', { policyId: policy.id });
            this.openPaymentDialog(policy);
          }
        });
      }

      if (policy.status !== 'CANCELED') {
        items.push({
          label: 'Anuluj',
          icon: 'pi pi-ban',
          command: () => {
            this.debug('menu: Anuluj', { policyId: policy.id });
            this.openCancellationDialog(policy);
          }
        });
      }

      result[policy.id] = items;
    });

    return result;
  });

  protected readonly sortFieldOptions: SortFieldOption[] = [
    { code: 'ISSUE_DATE', label: 'Data wystawienia' },
    { code: 'CONCLUSION_DATE', label: 'Data zawarcia' },
    { code: 'COVERAGE_END_DATE', label: 'Data końca ubezpieczenia' }
  ];

  protected readonly filteredPolicies = computed(() => {
    const normalizedSearch = this.searchTerm().trim().toLowerCase();

    const filtered = this.policies().filter((policy) => {
      const matchesFilter = this.matchesSelectedFilter(policy);
      const matchesSearch =
        normalizedSearch.length === 0 ||
        policy.policyNumber.toLowerCase().includes(normalizedSearch) ||
        policy.customerName.toLowerCase().includes(normalizedSearch) ||
        policy.vehicleLabel.toLowerCase().includes(normalizedSearch) ||
        policy.registrationNumber.toLowerCase().includes(normalizedSearch);

      return matchesFilter && matchesSearch;
    });

    return [...filtered].sort((left, right) => this.comparePolicies(left, right));
  });

  protected readonly renewalsCount = computed(
    () => this.policies().filter((policy) => policy.status === 'RENEWAL').length
  );
  protected readonly toPayCount = computed(
    () => this.policies().filter((policy) => policy.status !== 'CANCELED' && policy.paymentStatus === 'TO_PAY').length
  );
  protected readonly paidPoliciesPremiumTotal = computed(() =>
    this.policies()
      .filter((policy) => policy.paymentStatus === 'PAID')
      .reduce((sum, policy) => sum + policy.annualPremium, 0)
  );

  protected readonly totalVisiblePolicies = computed(() => this.filteredPolicies().length);

  constructor() {
    this.route.queryParamMap.pipe(takeUntilDestroyed()).subscribe((params) => {
      const policyId = params.get('policy');

      if (!policyId) {
        return;
      }

      const policy = this.policies().find((item) => item.id === policyId);

      if (policy) {
        this.searchTerm.set(policy.policyNumber);
      }
    });

    try {
      this.exchangeRates();
    } catch {
      this.ratesError.set(true);
      this.selectedCurrency.set('PLN');
    }
  }

  protected changeCurrency(currency: CurrencyCode): void {
    if (this.disabledCurrencies().includes(currency)) {
      this.selectedCurrency.set('PLN');
      return;
    }

    this.selectedCurrency.set(currency);
  }

  protected setFilter(filter: PolicyViewFilter): void {
    this.selectedFilter.set(filter);
  }

  protected isFilterSelected(filter: PolicyViewFilter): boolean {
    return this.selectedFilter() === filter;
  }

  protected clearFilters(): void {
    this.searchTerm.set('');
    this.selectedFilter.set('ALL');
  }

  protected goToPolicyFlow(policy: Policy): void {
    this.debug('click: Szczegóły', {
      policyId: policy.id,
      sourceOfferId: policy.sourceOfferId ?? null
    });

    if (policy.sourceOfferId) {
      this.debug('navigate: existing offer readonly', { offerId: policy.sourceOfferId });
      void this.router.navigate(['/offers', policy.sourceOfferId, 'vehicle'], {
        queryParams: { readonly: '1' }
      });
      return;
    }

    this.debug('navigate: new offer readonly from policy', { policyId: policy.id });
    void this.router.navigate(['/offers/new/motor/vehicle'], {
      queryParams: { readonly: '1', sourcePolicyId: policy.id, mode: 'preview' }
    });
  }

  protected onPolicySplitDropdownClick(policy: Policy): void {
    this.debug('click: split dropdown', { policyId: policy.id });
  }

  protected openCancellationDialog(policy: Policy): void {
    this.debug('dialog: open cancellation', { policyId: policy.id });
    this.pendingCancellation.set(policy);
    this.cancellationDialogVisible.set(true);
  }

  protected closeCancellationDialog(): void {
    this.debug('dialog: close cancellation');
    this.cancellationDialogVisible.set(false);
    this.pendingCancellation.set(null);
  }

  protected openRenewalDialog(policy: Policy): void {
    this.debug('dialog: open renewal', { policyId: policy.id });
    this.pendingRenewal.set(policy);
    this.renewalDialogVisible.set(true);
  }

  protected closeRenewalDialog(): void {
    this.debug('dialog: close renewal');
    this.renewalDialogVisible.set(false);
    this.pendingRenewal.set(null);
  }

  protected confirmRenewal(): void {
    const policy = this.pendingRenewal();

    if (!policy) {
      return;
    }

    this.debug('dialog: confirm renewal', { policyId: policy.id });
    this.closeRenewalDialog();
    this.renewPolicyAsOffer(policy.id);
  }

  protected openPaymentDialog(policy: Policy): void {
    this.debug('dialog: open payment', { policyId: policy.id });
    this.pendingPayment.set(policy);
    this.paymentDialogVisible.set(true);
  }

  protected closePaymentDialog(): void {
    this.debug('dialog: close payment');
    this.paymentDialogVisible.set(false);
    this.pendingPayment.set(null);
  }

  protected confirmPayment(): void {
    const policy = this.pendingPayment();

    if (!policy) {
      return;
    }

    this.debug('dialog: confirm payment', { policyId: policy.id });
    this.paymentStatusOverrides.update((overrides) => ({
      ...overrides,
      [policy.id]: 'PAID'
    }));
    this.closePaymentDialog();
  }

  protected confirmCancellation(): void {
    const policy = this.pendingCancellation();

    if (!policy) {
      return;
    }

    this.debug('dialog: confirm cancellation', { policyId: policy.id });
    this.statusOverrides.update((overrides) => ({
      ...overrides,
      [policy.id]: 'CANCELED'
    }));

    this.closeCancellationDialog();
  }

  protected toggleSortDirection(): void {
    this.selectedSortDirection.update((direction) => (direction === 'DESC' ? 'ASC' : 'DESC'));
  }

  protected sortDirectionLabel(): string {
    return this.selectedSortDirection() === 'DESC' ? 'Malejąco' : 'Rosnąco';
  }

  protected sortDirectionIcon(): string {
    return this.selectedSortDirection() === 'DESC' ? 'pi pi-sort-amount-down' : 'pi pi-sort-amount-up-alt';
  }

  protected getPolicyStatusPresentation(status: PolicyStatus): PolicyStatusPresentation {
    if (status === 'CANCELED') {
      return { label: 'Anulowana', severity: 'danger' };
    }

    if (status === 'RENEWAL') {
      return { label: 'Wznowienie', severity: 'warn' };
    }

    return { label: 'Aktywna', severity: 'success' };
  }

  protected getPaymentStatusPresentation(status: PolicyPaymentStatus): PolicyStatusPresentation {
    if (status === 'TO_PAY') {
      return { label: 'Do opłaty', severity: 'danger' };
    }

    return { label: 'Opłacona', severity: 'info' };
  }

  private matchesSelectedFilter(policy: Policy): boolean {
    switch (this.selectedFilter()) {
      case 'RENEWALS':
        return policy.status === 'RENEWAL';
      case 'TO_PAY':
        return policy.paymentStatus === 'TO_PAY';
      case 'ALL':
      default:
        return true;
    }
  }

  private comparePolicies(left: Policy, right: Policy): number {
    switch (this.selectedSortField()) {
      case 'CONCLUSION_DATE':
        return this.compareDates(left.conclusionDate, right.conclusionDate);
      case 'COVERAGE_END_DATE':
        return this.compareDates(left.coverageEndDate, right.coverageEndDate);
      case 'ISSUE_DATE':
      default:
        return this.compareDates(left.issueDate, right.issueDate);
    }
  }

  private compareDates(left: string, right: string): number {
    const leftTime = new Date(left).getTime();
    const rightTime = new Date(right).getTime();

    if (leftTime === rightTime) {
      return 0;
    }

    const naturalOrder = leftTime > rightTime ? 1 : -1;
    return this.selectedSortDirection() === 'ASC' ? naturalOrder : -naturalOrder;
  }

  private copyPolicyToOffer(policyId: string): void {
    this.debug('navigate: copy policy -> offer', { policyId });
    void this.router.navigate(['/offers/new/motor/vehicle'], {
      queryParams: { sourcePolicyId: policyId, mode: 'copy' }
    });
  }

  private renewPolicyAsOffer(policyId: string): void {
    this.debug('navigate: renew policy -> offer', { policyId });
    void this.router.navigate(['/offers/new/motor/vehicle'], {
      queryParams: { sourcePolicyId: policyId, mode: 'renew' }
    });
  }

  private debug(message: string, payload?: Record<string, unknown>): void {
    console.debug(`[Policies] ${message}`, payload ?? {});
  }
}
