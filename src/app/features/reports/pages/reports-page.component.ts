import { CommonModule, PercentPipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { Component, computed, inject, signal } from '@angular/core';
import { CurrencyCode, ExchangeRateSnapshot, Offer, OfferStatus, Policy } from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { PoliciesRepository } from '../../../core/repositories/policies.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CurrencyConversionService } from '../../../core/services/currency-conversion.service';
import { PresentedMoneyPipe } from '../../../shared/pipes/presented-money.pipe';
import { CurrencySwitcherComponent } from '../../../shared/ui/currency-switcher/currency-switcher.component';
import { PageHeaderComponent } from '../../../shared/ui/page-header/page-header.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { StatTileComponent } from '../../../shared/ui/stat-tile/stat-tile.component';

type FunnelRow = {
  label: string;
  count: number;
  ratio: number;
};

type MonthlyPoint = {
  label: string;
  amount: number;
};

type BusinessLineRow = {
  label: string;
  amount: number;
  ratio: number;
};

@Component({
  selector: 'app-reports-page',
  imports: [
    CommonModule,
    PercentPipe,
    PageHeaderComponent,
    SectionCardComponent,
    StatTileComponent,
    CurrencySwitcherComponent,
    PresentedMoneyPipe
  ],
  templateUrl: './reports-page.component.html',
  styleUrl: './reports-page.component.scss'
})
export class ReportsPageComponent {
  private readonly offersRepository = inject(OffersRepository);
  private readonly policiesRepository = inject(PoliciesRepository);
  private readonly runtimeRepository = inject(SalesFlowRuntimeRepository);
  private readonly exchangeRatesRepository = inject(ExchangeRatesRepository);
  private readonly currencyConversionService = inject(CurrencyConversionService);

  private readonly offersFromMock = toSignal(this.offersRepository.getOffers(), { initialValue: [] as Offer[] });
  private readonly policiesFromMock = toSignal(this.policiesRepository.getPolicies(), { initialValue: [] as Policy[] });
  protected readonly dateFrom = signal(this.defaultDateFrom());
  protected readonly dateTo = signal(this.defaultDateTo());
  protected readonly selectedCurrency = signal<CurrencyCode>('PLN');
  protected readonly ratesError = signal(false);
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

  protected readonly filteredOffers = computed<Offer[]>(() =>
    this.offers().filter((offer) => this.isInSelectedRange(offer.createdAt))
  );

  protected readonly filteredPolicies = computed<Policy[]>(() =>
    this.policies().filter((policy) => this.isInSelectedRange(policy.issueDate))
  );

  protected readonly kpis = computed(() => {
    const offers = this.filteredOffers();
    const policies = this.filteredPolicies();
    const paidPolicies = policies.filter((policy) => policy.paymentStatus === 'PAID');
    const activePolicies = policies.filter((policy) => policy.status !== 'CANCELED');
    const conversion = offers.length > 0 ? policies.filter((policy) => !!policy.sourceOfferId).length / offers.length : 0;
    const assignedPremium = paidPolicies.reduce((sum, policy) => sum + policy.annualPremium, 0);
    const monthlyAveragePremium = activePolicies.length > 0 ? Math.round(assignedPremium / activePolicies.length / 12) : 0;
    const renewalsRatio = policies.length > 0 ? policies.filter((policy) => policy.status === 'RENEWAL').length / policies.length : 0;

    return [
      {
        label: 'Składka przypisana',
        value: this.currencyConversionService.formatAmount(assignedPremium, this.selectedCurrency(), this.exchangeRates()?.rates),
        note: 'polisy opłacone'
      },
      {
        label: 'Konwersja oferta -> polisa',
        value: Math.round(conversion * 100).toLocaleString('pl-PL') + '%',
        note: 'oferty zmapowane do polis'
      },
      {
        label: 'Aktywne polisy',
        value: activePolicies.length.toLocaleString('pl-PL'),
        note: 'status różny od anulowanej'
      },
      {
        label: 'Śr. składka miesięczna',
        value: this.currencyConversionService.formatAmount(monthlyAveragePremium, this.selectedCurrency(), this.exchangeRates()?.rates),
        note: 'portfel aktywnych polis'
      },
      {
        label: 'Wznowienia',
        value: Math.round(renewalsRatio * 100).toLocaleString('pl-PL') + '%',
        note: 'udział wznowień w polisach'
      },
      {
        label: 'Oferty łącznie',
        value: offers.length.toLocaleString('pl-PL'),
        note: 'wszystkie źródła danych'
      }
    ];
  });

  protected readonly funnel = computed<FunnelRow[]>(() => {
    const offers = this.filteredOffers();
    const max = Math.max(offers.length, 1);
    const byStatus = (status: OfferStatus) => offers.filter((offer) => offer.status === status).length;

    const rows: Array<{ label: string; count: number }> = [
      { label: 'Draft', count: byStatus('DRAFT') },
      { label: 'Kalkulacja', count: byStatus('CALCULATION') },
      { label: 'Oferta wystawiona', count: byStatus('ISSUED') },
      { label: 'Oferta zaakceptowana', count: byStatus('ACCEPTED') },
      { label: 'Polisa', count: byStatus('POLICY') }
    ];

    return rows.map((row) => ({
      ...row,
      ratio: row.count / max
    }));
  });

  protected readonly monthlyTrend = computed<MonthlyPoint[]>(() => {
    const now = new Date();
    const monthKeys: string[] = [];

    for (let shift = 5; shift >= 0; shift--) {
      const date = new Date(now.getFullYear(), now.getMonth() - shift, 1);
      monthKeys.push(`${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`);
    }

    const paidPremium = this.filteredPolicies()
      .filter((policy) => policy.paymentStatus === 'PAID')
      .reduce((sum, policy) => sum + policy.annualPremium, 0);
    const fallbackBase = 14500;
    const base = Math.max(fallbackBase, Math.round((paidPremium || fallbackBase) / 8));
    const profile = [0.84, 0.92, 0.98, 1.07, 1.02, 1.12];

    return monthKeys.map((key, index) => ({
      label: key.slice(5),
      amount: Math.round(base * profile[index])
    }));
  });

  protected readonly trendPolyline = computed(() => {
    const points = this.monthlyTrend();
    const maxAmount = Math.max(...points.map((point) => point.amount), 1);
    const width = 100;
    const height = 42;
    const topPadding = 4;
    const bottomPadding = 3;
    const drawableHeight = height - topPadding - bottomPadding;
    const scaledMax = maxAmount * 1.15;

    if (points.length === 1) {
      return `0,${height} ${width},${height}`;
    }

    return points
      .map((point, index) => {
        const x = (index / (points.length - 1)) * width;
        const y = topPadding + (1 - point.amount / scaledMax) * drawableHeight;
        return `${x.toFixed(2)},${y.toFixed(2)}`;
      })
      .join(' ');
  });

  protected readonly businessLines = computed<BusinessLineRow[]>(() => {
    const totals = new Map<string, number>();

    this.filteredOffers().forEach((offer) => {
      const selected = offer.variants.find((variant) => variant.id === offer.selectedVariantId) ?? offer.variants[0];
      selected?.policyLines.forEach((line) => {
        totals.set(line.label, (totals.get(line.label) ?? 0) + line.premium.amount);
      });
    });

    const totalSum = Array.from(totals.values()).reduce((sum, amount) => sum + amount, 0);
    return Array.from(totals.entries())
      .map(([label, amount]) => ({
        label,
        amount,
        ratio: totalSum > 0 ? amount / totalSum : 0
      }))
      .sort((a, b) => b.amount - a.amount);
  });

  protected readonly paymentStats = computed(() => {
    const policies = this.filteredPolicies();
    const paid = policies.filter((policy) => policy.paymentStatus === 'PAID').length;
    const toPay = policies.filter((policy) => policy.paymentStatus === 'TO_PAY').length;
    const total = Math.max(policies.length, 1);
    return {
      paid,
      toPay,
      paidRatio: paid / total
    };
  });

  protected readonly paymentRingBackground = computed(() => {
    const paidPercent = Math.round(this.paymentStats().paidRatio * 100);
    return `conic-gradient(#0f766e ${paidPercent}%, #dce5f2 ${paidPercent}% 100%)`;
  });

  constructor() {
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

  private isInSelectedRange(rawDate: string | undefined): boolean {
    if (!rawDate) {
      return false;
    }

    const from = this.dateFrom();
    const to = this.dateTo();

    if (!from || !to) {
      return true;
    }

    const normalized = rawDate.slice(0, 10);
    return normalized >= from && normalized <= to;
  }

  private defaultDateTo(): string {
    return new Date().toISOString().slice(0, 10);
  }

  private defaultDateFrom(): string {
    const date = new Date();
    date.setFullYear(date.getFullYear() - 1);
    return date.toISOString().slice(0, 10);
  }
}
