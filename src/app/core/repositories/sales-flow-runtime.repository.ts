import { Injectable, signal } from '@angular/core';
import { Offer, Policy } from '../models';

const POLICIES_STORAGE_KEY = 'smart-agent-promoted-policies';
const OFFERS_STORAGE_KEY = 'smart-agent-runtime-offers';

@Injectable({
  providedIn: 'root'
})
export class SalesFlowRuntimeRepository {
  private readonly promotedPoliciesState = signal<Policy[]>(this.loadPromotedPolicies());
  private readonly runtimeOffersState = signal<Offer[]>(this.loadRuntimeOffers());

  readonly promotedPolicies = this.promotedPoliciesState.asReadonly();
  readonly runtimeOffers = this.runtimeOffersState.asReadonly();

  getPromotedPolicyById(policyId: string): Policy | undefined {
    return this.promotedPoliciesState().find((policy) => policy.id === policyId);
  }

  promoteOfferToPolicy(offer: Offer): Policy {
    const existing = this.promotedPoliciesState().find((policy) => policy.sourceOfferId === offer.id);

    if (existing) {
      return existing;
    }

    const now = new Date();
    const issueDate = offer.createdAt || now.toISOString();
    const conclusionDate = issueDate;
    const coverageEnd = new Date(conclusionDate);
    coverageEnd.setFullYear(coverageEnd.getFullYear() + 1);
    coverageEnd.setDate(coverageEnd.getDate() - 1);

    const annualPremium = offer.selectedPaymentPlan?.totalPremium.amount ?? offer.variants[0]?.totalPremium.amount ?? 0;
    const monthlyPremium = Math.round(annualPremium / 12);

    const policy: Policy = {
      id: `runtime-policy-${offer.id}`,
      sourceOfferId: offer.id,
      policyNumber: `POL/RUNTIME/${offer.offerNumber}`,
      customerName: this.customerDisplayName(offer),
      vehicleLabel: `${offer.vehicle.make} ${offer.vehicle.model}`.trim(),
      registrationNumber: offer.vehicle.registration?.registrationNumber ?? 'brak rej.',
      productName: 'Polisa komunikacyjna',
      status: 'ACTIVE',
      paymentStatus: 'TO_PAY',
      annualPremium,
      monthlyPremium,
      issueDate,
      conclusionDate,
      coverageEndDate: coverageEnd.toISOString(),
      dueDate: coverageEnd.toISOString(),
      renewalDate: coverageEnd.toISOString(),
      updatedAt: now.toISOString(),
      agentName: offer.agent.fullName,
      residenceAddress: offer.contractData?.correspondenceAddress ?? offer.customer.residenceAddress,
      marketingConsents: offer.contractData?.marketingConsents
    };

    this.promotedPoliciesState.update((items) => {
      const updated = [policy, ...items];
      this.persistPromotedPolicies(updated);
      return updated;
    });

    return policy;
  }

  saveDraftOffer(offer: Offer): Offer {
    const nowIso = new Date().toISOString();
    const nextId = offer.id.startsWith('draft-') ? this.generateOfferId() : offer.id;
    const nextOfferNumber = offer.offerNumber.startsWith('NOWA') ? this.generateOfferNumber() : offer.offerNumber;

    const normalized: Offer = {
      ...offer,
      id: nextId,
      offerNumber: nextOfferNumber,
      status: offer.status,
      createdAt: offer.createdAt || nowIso,
      updatedAt: nowIso
    };

    this.runtimeOffersState.update((items) => {
      const withoutCurrent = items.filter((item) => item.id !== normalized.id);
      const updated = [normalized, ...withoutCurrent];
      this.persistRuntimeOffers(updated);
      return updated;
    });

    return normalized;
  }

  private customerDisplayName(offer: Offer): string {
    const identity = offer.customer.identity;

    if (identity.type === 'LEGAL_ENTITY' || identity.type === 'SOLE_PROPRIETOR') {
      return identity.companyName;
    }

    return `${identity.personName.firstName} ${identity.personName.lastName}`.trim();
  }

  private loadPromotedPolicies(): Policy[] {
    if (typeof window === 'undefined') {
      return [];
    }

    const raw = window.localStorage.getItem(POLICIES_STORAGE_KEY);

    if (!raw) {
      return [];
    }

    try {
      return JSON.parse(raw) as Policy[];
    } catch {
      return [];
    }
  }

  private persistPromotedPolicies(policies: Policy[]): void {
    if (typeof window === 'undefined') {
      return;
    }

    window.localStorage.setItem(POLICIES_STORAGE_KEY, JSON.stringify(policies));
  }

  private loadRuntimeOffers(): Offer[] {
    if (typeof window === 'undefined') {
      return [];
    }

    const raw = window.localStorage.getItem(OFFERS_STORAGE_KEY);

    if (!raw) {
      return [];
    }

    try {
      return JSON.parse(raw) as Offer[];
    } catch {
      return [];
    }
  }

  private persistRuntimeOffers(offers: Offer[]): void {
    if (typeof window === 'undefined') {
      return;
    }

    window.localStorage.setItem(OFFERS_STORAGE_KEY, JSON.stringify(offers));
  }

  private generateOfferId(): string {
    return `runtime-offer-${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 7)}`;
  }

  private generateOfferNumber(): string {
    const timestamp = Date.now().toString().slice(-6);
    return `OFR/RUNTIME/${timestamp}`;
  }
}
