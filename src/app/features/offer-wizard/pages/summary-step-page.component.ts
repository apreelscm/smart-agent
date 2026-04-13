import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { Tag } from 'primeng/tag';
import { OfferStatus } from '../../../core/models';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

@Component({
  selector: 'app-summary-step-page',
  imports: [CommonModule, CurrencyPipe, SectionCardComponent, Tag],
  templateUrl: './summary-step-page.component.html',
  styleUrl: './summary-step-page.component.scss'
})
export class SummaryStepPageComponent {
  private readonly wizardState = inject(OfferWizardStateService);

  protected readonly offer = computed(() => this.wizardState.draftOffer());
  protected readonly selectedVariant = computed(() => this.wizardState.selectedVariant());
  protected readonly discountAmount = computed(() => this.wizardState.discountAmount());

  protected customerDisplayName(): string {
    const customer = this.offer()?.customer;

    if (!customer) {
      return 'Brak danych klienta';
    }

    if (customer.identity.type === 'NATURAL_PERSON') {
      return `${customer.identity.personName.firstName} ${customer.identity.personName.lastName}`.trim() || 'Brak danych klienta';
    }

    if (customer.identity.type === 'SOLE_PROPRIETOR' || customer.identity.type === 'LEGAL_ENTITY') {
      return customer.identity.companyName || 'Brak danych klienta';
    }

    return 'Brak danych klienta';
  }

  protected correspondenceAddressLabel(): string {
    const offer = this.offer();
    const address = offer?.contractData?.correspondenceAddress ?? offer?.customer.residenceAddress;

    if (!address) {
      return 'Brak adresu korespondencyjnego';
    }

    const streetPart = [address.street, address.buildingNumber].filter(Boolean).join(' ');
    return `${streetPart}, ${address.postalCode} ${address.city}`.trim();
  }

  protected discountedPremiumAmount(): number {
    const variant = this.selectedVariant();

    if (!variant) {
      return 0;
    }

    const ocPremium = variant.policyLines.find((line) => line.code === 'OC')?.premium.amount ?? 0;
    const appliedDiscount = Math.min(this.discountAmount(), ocPremium);
    return Math.max(0, Math.round(variant.totalPremium.amount - appliedDiscount));
  }

  protected offerStatusLabel(status: OfferStatus): string {
    const labels: Record<OfferStatus, string> = {
      DRAFT: 'Draft',
      CALCULATION: 'Kalkulacja',
      ISSUED: 'Oferta wystawiona',
      ACCEPTED: 'Oferta zaakceptowana',
      POLICY: 'Polisa',
      REJECTED: 'Oferta odrzucona',
      CANCELED: 'Oferta anulowana'
    };

    return labels[status];
  }
}
