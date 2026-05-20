import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import {
  ExchangeRateQuote,
  ForeignCurrencyCode,
  SupportedCurrencyCode,
} from '../../../../core/models/exchange-rate.model';
import { ExchangeRateService } from '../../../../core/services/exchange-rate.service';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

type CoverageKey = 'oc' | 'ac' | 'assistance' | 'nnwKip' | 'szyby' | 'bagaz' | 'opony';

interface CoverageOption {
  key: CoverageKey;
  label: string;
  desc: string;
  price: number;
  route: string | null;
  required?: boolean;
}

@Component({
  selector: 'app-step-15-coverage',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-15-coverage.component.html',
  styleUrl: './step-15-coverage.component.scss',
})
export class Step15CoverageComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);
  private exchangeRateService = inject(ExchangeRateService);

  readonly currencyOptions: SupportedCurrencyCode[] = ['PLN', 'USD', 'EUR'];

  private readonly quoteCache = new Map<ForeignCurrencyCode, ExchangeRateQuote>();
  private readonly blockedCurrencies = new Set<ForeignCurrencyCode>();

  private selectionRequestId = 0;

  cov = { ...this.draft.draft().coverages };

  mainCoverages: CoverageOption[] = [
    {
      key: 'oc',
      label: 'OC',
      desc: 'Obowiązkowe ubezpieczenie odpowiedzialności cywilnej',
      price: 289,
      route: null,
      required: true,
    },
    {
      key: 'ac',
      label: 'AC',
      desc: 'Pełny zakres casco',
      price: 389,
      route: '/kalkulator/casco',
      required: false,
    },
    {
      key: 'assistance',
      label: 'Assistance',
      desc: 'Pomoc na terenie Europy',
      price: 89,
      route: '/kalkulator/assistance',
      required: false,
    },
    {
      key: 'nnwKip',
      label: 'NNW KiP',
      desc: 'Suma ubezpieczenia do 50 000 zł',
      price: 129,
      route: '/kalkulator/nnw',
      required: false,
    },
  ];

  addons: CoverageOption[] = [
    {
      key: 'szyby',
      label: 'Szyby',
      desc: 'Uszkodzenie, połknięcie lub wymiana szyb',
      price: 150,
      route: '/kalkulator/szyby',
    },
    {
      key: 'bagaz',
      label: 'Bagaż',
      desc: 'Uszkodzenie lub kradzież przewożonego bagażu',
      price: 89,
      route: null,
    },
    {
      key: 'opony',
      label: 'Opony',
      desc: 'Pokrycie kosztów naprawy lub wymiany opon',
      price: 89,
      route: null,
    },
  ];

  promoCode = this.cov.promoCode;

  selectedCurrency: SupportedCurrencyCode = 'PLN';
  pendingCurrency: ForeignCurrencyCode | null = null;
  appliedQuote: ExchangeRateQuote | null = null;
  currencyErrorMessage = '';

  get total() {
    let sum = 289; // OC always
    if (this.cov.ac) sum += 389;
    if (this.cov.assistance) sum += 89;
    if (this.cov.nnwKip) sum += 129;
    if (this.cov.szyby) sum += 150;
    if (this.cov.bagaz) sum += 89;
    if (this.cov.opony) sum += 89;
    return sum;
  }

  async selectCurrency(code: SupportedCurrencyCode): Promise<void> {
    if (code === 'PLN') {
      this.resetCurrencyToPln();
      return;
    }

    if (this.blockedCurrencies.has(code) || this.pendingCurrency === code) {
      return;
    }

    const cachedQuote = this.quoteCache.get(code);

    if (cachedQuote) {
      this.applyQuote(cachedQuote);
      return;
    }

    const requestId = ++this.selectionRequestId;
    this.pendingCurrency = code;
    this.currencyErrorMessage = '';

    try {
      const quote = await firstValueFrom(this.exchangeRateService.getQuote(code));
      this.quoteCache.set(code, quote);

      if (requestId !== this.selectionRequestId) {
        return;
      }

      this.applyQuote(quote);
    } catch {
      if (requestId !== this.selectionRequestId) {
        return;
      }

      this.blockedCurrencies.add(code);
      this.currencyErrorMessage = `Waluta ${code} jest chwilowo niedostępna. Nie udało się pobrać bieżącego ani poprzedniego kursu NBP.`;
    } finally {
      if (requestId === this.selectionRequestId && this.pendingCurrency === code) {
        this.pendingCurrency = null;
      }
    }
  }

  toggle(key: CoverageKey) {
    (this.cov as any)[key] = !(this.cov as any)[key];
  }

  isOn(key: CoverageKey): boolean {
    return !!(this.cov as any)[key];
  }

  isCurrencyActive(code: SupportedCurrencyCode): boolean {
    return this.selectedCurrency === code;
  }

  isCurrencyDisabled(code: SupportedCurrencyCode): boolean {
    return code !== 'PLN' && this.blockedCurrencies.has(code);
  }

  isCurrencyLoading(code: SupportedCurrencyCode): boolean {
    return code !== 'PLN' && this.pendingCurrency === code;
  }

  getDisplayPrice(amountPln: number): string {
    if (this.selectedCurrency === 'PLN') {
      return `${amountPln.toFixed(0)} zł`;
    }

    const convertedAmount = this.convertFromPln(amountPln);

    return `${convertedAmount.toFixed(2)} ${this.selectedCurrency}`;
  }

  getDisplayTotal(): string {
    return this.getDisplayPrice(this.total);
  }

  formatRate(rate: number): string {
    return rate.toFixed(4);
  }

  openDetail(route: string | null) {
    if (route) this.router.navigate([route]);
  }

  next() {
    this.draft.patchCoverages({ ...this.cov, promoCode: this.promoCode, totalPremium: this.total });
    this.router.navigate(['/kalkulator/dane-polisowe']);
  }

  private resetCurrencyToPln(): void {
    this.selectionRequestId++;
    this.pendingCurrency = null;
    this.selectedCurrency = 'PLN';
    this.appliedQuote = null;
    this.currencyErrorMessage = '';
  }

  private applyQuote(quote: ExchangeRateQuote): void {
    this.selectedCurrency = quote.code;
    this.appliedQuote = quote;
    this.currencyErrorMessage = '';
  }

  private convertFromPln(amountPln: number): number {
    if (!this.appliedQuote) {
      return amountPln;
    }

    return Math.round((amountPln / this.appliedQuote.mid) * 100) / 100;
  }
}
