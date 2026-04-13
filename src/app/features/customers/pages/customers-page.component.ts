import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ButtonDirective } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Tag } from 'primeng/tag';
import { Offer, OfferStatus, Policy } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';

type CustomerTypeLabel = 'Osoba fizyczna' | 'Firma' | 'Klient';

type CustomerRecord = {
  key: string;
  displayName: string;
  typeLabel: CustomerTypeLabel;
  phone?: string;
  email?: string;
  lastActivity?: string;
  offers: Offer[];
  policies: Policy[];
};

@Component({
  selector: 'app-customers-page',
  imports: [CommonModule, FormsModule, RouterLink, PageHeaderComponent, SectionCardComponent, InputText, Tag, ButtonDirective],
  templateUrl: './customers-page.component.html',
  styleUrl: './customers-page.component.scss'
})
export class CustomersPageComponent {
  private readonly offersRepository = inject(OffersRepository);
  private readonly policiesRepository = inject(PoliciesRepository);
  private readonly runtimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly searchTerm = signal('');
  private readonly offersFromMock = toSignal(this.offersRepository.getOffers(), { initialValue: [] as Offer[] });
  private readonly policiesFromMock = toSignal(this.policiesRepository.getPolicies(), { initialValue: [] as Policy[] });
  private readonly routeCustomerKey = toSignal(this.route.paramMap, {
    initialValue: this.route.snapshot.paramMap
  });

  protected readonly offers = computed<Offer[]>(() => {
    const byId = new Map<string, Offer>();
    [...this.offersFromMock(), ...this.runtimeRepository.runtimeOffers()].forEach((offer) => byId.set(offer.id, offer));
    return Array.from(byId.values());
  });

  protected readonly policies = computed<Policy[]>(() => {
    const byId = new Map<string, Policy>();
    [...this.policiesFromMock(), ...this.runtimeRepository.promotedPolicies()].forEach((policy) => byId.set(policy.id, policy));
    return Array.from(byId.values());
  });

  protected readonly customerRecords = computed<CustomerRecord[]>(() => {
    const records = new Map<string, CustomerRecord>();

    this.offers().forEach((offer) => {
      const key = this.offerCustomerKey(offer);
      const existing = records.get(key) ?? {
        key,
        displayName: this.offerCustomerDisplayName(offer),
        typeLabel: this.offerCustomerType(offer),
        phone: offer.customer.contact?.phoneNumber,
        email: offer.customer.contact?.email,
        offers: [],
        policies: [],
        lastActivity: offer.updatedAt
      };

      existing.offers.push(offer);
      existing.lastActivity = this.maxDate(existing.lastActivity, offer.updatedAt);
      if (!existing.phone) {
        existing.phone = offer.customer.contact?.phoneNumber;
      }
      if (!existing.email) {
        existing.email = offer.customer.contact?.email;
      }

      records.set(key, existing);
    });

    this.policies().forEach((policy) => {
      const normalizedPolicyName = this.normalizeName(policy.customerName);
      const matched = Array.from(records.values()).find((record) => this.normalizeName(record.displayName) === normalizedPolicyName);
      const key = matched?.key ?? `POLICY:${normalizedPolicyName}`;
      const existing = records.get(key) ?? {
        key,
        displayName: policy.customerName,
        typeLabel: this.inferTypeFromPolicyName(policy.customerName),
        offers: [],
        policies: [],
        lastActivity: policy.updatedAt
      };

      existing.policies.push(policy);
      existing.lastActivity = this.maxDate(existing.lastActivity, policy.updatedAt);
      records.set(key, existing);
    });

    return Array.from(records.values())
      .map((record) => ({
        ...record,
        offers: [...record.offers].sort((a, b) => this.compareDatesDesc(a.updatedAt, b.updatedAt)),
        policies: [...record.policies].sort((a, b) => this.compareDatesDesc(a.updatedAt, b.updatedAt))
      }))
      .sort((a, b) => this.compareDatesDesc(a.lastActivity, b.lastActivity));
  });

  protected readonly filteredCustomers = computed(() => {
    const term = this.searchTerm().trim().toLocaleLowerCase('pl');

    if (!term) {
      return this.customerRecords();
    }

    return this.customerRecords().filter((record) =>
      [record.displayName, record.phone ?? '', record.email ?? ''].some((value) => value.toLocaleLowerCase('pl').includes(term))
    );
  });

  protected readonly selectedCustomer = computed(() => {
    const encodedKey = this.routeCustomerKey().get('customerKey');
    const key = encodedKey ? decodeURIComponent(encodedKey) : null;
    return key ? this.customerRecords().find((record) => record.key === key) : undefined;
  });

  protected readonly totalCustomers = computed(() => this.filteredCustomers().length);
  protected readonly selectedCustomerProducts = computed(() => {
    const customer = this.selectedCustomer();
    if (!customer) {
      return [];
    }

    const labels = new Set<string>();
    customer.offers.forEach((offer) => {
      const selected = offer.variants.find((variant) => variant.id === offer.selectedVariantId) ?? offer.variants[0];
      selected?.policyLines.forEach((line) => labels.add(line.label));
    });
    customer.policies.forEach((policy) => labels.add(policy.productName));
    return Array.from(labels);
  });
  protected readonly selectedCustomerAssignedPremium = computed(() => {
    const customer = this.selectedCustomer();
    if (!customer) {
      return 0;
    }

    const policyPremium = customer.policies.reduce((sum, policy) => sum + policy.annualPremium, 0);
    const offersWithoutPolicy = customer.offers.filter((offer) => !customer.policies.some((policy) => policy.sourceOfferId === offer.id));
    const offerPremium = offersWithoutPolicy.reduce(
      (sum, offer) => sum + (offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0),
      0
    );
    return policyPremium + offerPremium;
  });
  protected readonly selectedCustomerItems = computed(() => {
    const customer = this.selectedCustomer();
    if (!customer) {
      return [];
    }

    const offerItems = customer.offers.map((offer) => ({
      type: 'Oferta' as const,
      number: offer.offerNumber,
      product: (offer.variants.find((variant) => variant.id === offer.selectedVariantId) ?? offer.variants[0])?.name ?? '—',
      vehicle: `${offer.vehicle.make} ${offer.vehicle.model}`.trim(),
      statusLabel: this.getOfferStatusLabel(offer.status),
      statusSeverity: this.offerStatusSeverity(offer.status),
      premium: offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0,
      date: offer.updatedAt,
      open: () => this.goToOffer(offer.id)
    }));

    const policyItems = customer.policies.map((policy) => ({
      type: 'Polisa' as const,
      number: policy.policyNumber,
      product: policy.productName,
      vehicle: policy.vehicleLabel,
      statusLabel: this.policyStatusLabel(policy),
      statusSeverity: 'info' as const,
      premium: policy.annualPremium,
      date: policy.updatedAt,
      open: () => this.goToPolicy(policy)
    }));

    return [...offerItems, ...policyItems].sort((a, b) => this.compareDatesDesc(a.date, b.date));
  });

  protected goToOffer(offerId: string): void {
    void this.router.navigate(['/offers', offerId, 'vehicle'], { queryParams: { readonly: '1' } });
  }

  protected goToPolicy(policy: Policy): void {
    if (policy.sourceOfferId) {
      void this.router.navigate(['/offers', policy.sourceOfferId, 'vehicle'], { queryParams: { readonly: '1' } });
      return;
    }

    void this.router.navigate(['/offers/new/vehicle'], {
      queryParams: { readonly: '1', sourcePolicyId: policy.id, mode: 'preview' }
    });
  }

  protected getOfferStatusLabel(status: OfferStatus): string {
    const map: Record<OfferStatus, string> = {
      DRAFT: 'Draft',
      CALCULATION: 'Kalkulacja',
      ISSUED: 'Oferta wystawiona',
      ACCEPTED: 'Oferta zaakceptowana',
      POLICY: 'Polisa',
      REJECTED: 'Oferta odrzucona',
      CANCELED: 'Oferta anulowana'
    };

    return map[status];
  }

  protected offerStatusSeverity(status: OfferStatus): 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast' {
    const map: Record<OfferStatus, 'success' | 'secondary' | 'info' | 'warn' | 'danger' | 'contrast'> = {
      DRAFT: 'secondary',
      CALCULATION: 'info',
      ISSUED: 'success',
      ACCEPTED: 'contrast',
      POLICY: 'success',
      REJECTED: 'danger',
      CANCELED: 'warn'
    };

    return map[status];
  }

  protected policyStatusLabel(policy: Policy): string {
    if (policy.status === 'CANCELED') {
      return 'Anulowana';
    }
    if (policy.status === 'RENEWAL') {
      return 'Wznowienie';
    }
    return policy.paymentStatus === 'PAID' ? 'Aktywna (opłacona)' : 'Aktywna (do opłaty)';
  }

  private offerCustomerDisplayName(offer: Offer): string {
    const identity = offer.customer.identity;

    if (identity.type === 'LEGAL_ENTITY' || identity.type === 'SOLE_PROPRIETOR') {
      return identity.companyName;
    }

    return `${identity.personName.firstName} ${identity.personName.lastName}`.trim();
  }

  private offerCustomerType(offer: Offer): CustomerTypeLabel {
    const identity = offer.customer.identity;
    if (identity.type === 'LEGAL_ENTITY' || identity.type === 'SOLE_PROPRIETOR') {
      return 'Firma';
    }
    return 'Osoba fizyczna';
  }

  private offerCustomerKey(offer: Offer): string {
    const identity = offer.customer.identity;

    if (identity.type === 'NATURAL_PERSON') {
      return `PERSON:${identity.pesel}`;
    }

    if (identity.type === 'SOLE_PROPRIETOR' || identity.type === 'LEGAL_ENTITY') {
      return `COMPANY:${this.normalizeName(identity.companyName)}`;
    }

    return `CUSTOMER:${offer.customer.id}`;
  }

  private inferTypeFromPolicyName(name: string): CustomerTypeLabel {
    return /sp\. z o\.o\.|s\.a\.|spółka|s\.k\./i.test(name) ? 'Firma' : 'Klient';
  }

  private normalizeName(value: string): string {
    return value.trim().toLocaleLowerCase('pl').replace(/\s+/g, ' ');
  }

  private maxDate(left?: string, right?: string): string | undefined {
    if (!left) {
      return right;
    }
    if (!right) {
      return left;
    }

    return new Date(left).getTime() >= new Date(right).getTime() ? left : right;
  }

  private compareDatesDesc(left?: string, right?: string): number {
    const leftTime = left ? new Date(left).getTime() : 0;
    const rightTime = right ? new Date(right).getTime() : 0;
    return rightTime - leftTime;
  }

  protected customerAssignedPremium(customer: CustomerRecord): number {
    const policyPremium = customer.policies.reduce((sum, policy) => sum + policy.annualPremium, 0);
    const offersWithoutPolicy = customer.offers.filter((offer) => !customer.policies.some((policy) => policy.sourceOfferId === offer.id));
    const offerPremium = offersWithoutPolicy.reduce(
      (sum, offer) => sum + (offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0),
      0
    );
    return policyPremium + offerPremium;
  }

  protected customerProducts(customer: CustomerRecord): string {
    const labels = new Set<string>();
    customer.offers.forEach((offer) => {
      const selected = offer.variants.find((variant) => variant.id === offer.selectedVariantId) ?? offer.variants[0];
      selected?.policyLines.forEach((line) => labels.add(line.label));
    });
    customer.policies.forEach((policy) => labels.add(policy.productName));
    return Array.from(labels).slice(0, 4).join(', ') || '—';
  }

  protected openCustomerDetails(customer: CustomerRecord): void {
    void this.router.navigate(['/customers', encodeURIComponent(customer.key)]);
  }

  protected backToCustomersList(): void {
    void this.router.navigate(['/customers']);
  }
}
