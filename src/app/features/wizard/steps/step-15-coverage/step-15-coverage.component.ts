import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PolicyDraft } from '../../../../core/models/policy-draft.model';
import {
  CurrencyRatesData,
  CurrencyRatesService,
  SupportedCurrencyCode,
} from '../../../../core/services/currency-rates.service';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

type CoverageKey = 'oc' | 'ac' | 'assistance' | 'nnwKip' | 'szyby' | 'bagaz' | 'opony';

interface CoverageOption {
  key: CoverageKey;
  label: string;
  desc: string;
  pricePln: number;
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
export class Step15CoverageComponent implements OnInit {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);
  private currencyRatesService = inject(CurrencyRatesService);
  private changeDetectorRef = inject(ChangeDetectorRef);

  readonly supportedCurrencies: SupportedCurrencyCode[] = ['PLN', 'USD', 'EUR'];
  readonly technicalRatesMessage =
    'Kursy walut są obecnie niedostępne. Prezentujemy kwoty wyłącznie w PLN.';

  cov: PolicyDraft['coverages'] = { ...this.draft.draft().coverages };
  promoCode = this.cov.promoCode;

  selectedCurrency: SupportedCurrencyCode = 'PLN';
  rates: CurrencyRatesData | null = null;
  ratesLoading = true;
  ratesError = false;

  mainCoverages: CoverageOption[] = [
    {
      key: 'oc',
      label: 'OC',
      desc: 'Obowiązkowe ubezpieczenie odpowiedzialności cywilnej',
      pricePln: 289,
      route: null,
      required: true,
    },
    {
      key: 'ac',
      label: 'AC',
      desc: 'Pełny zakres casco',
      pricePln: 389,
      route: '/kalkulator/casco',
      required: false,
    },
    {
      key: 'assistance',
      label: 'Assistance',
      desc: 'Pomoc na terenie Europy',
      pricePln: 89,
      route: '/kalkulator/assistance',
      required: false,
    },
    {
      key: 'nnwKip',
      label: 'NNW KiP',
      desc: 'Suma ubezpieczenia do 50 000 zł',
      pricePln: 129,
      route: '/kalkulator/nnw',
      required: false,
    },
  ];

  addons: CoverageOption[] = [
    {
      key: 'szyby',
      label: 'Szyby',
      desc: 'Uszkodzenie, połknięcie lub wymiana szyb',
      pricePln: 150,
      route: '/kalkulator/szyby',
    },
    {
      key: 'bagaz',
      label: 'Bagaż',
      desc: 'Uszkodzenie lub kradzież przewożonego bagażu',
      pricePln: 89,
      route: null,
    },
    {
      key: 'opony',
      label: 'Opony',
      desc: 'Pokrycie kosztów naprawy lub wymiany opon',
      pricePln: 89,
      route: null,
    },
  ];

  ngOnInit(): void {
    this.currencyRatesService.getRates().subscribe({
      next: rates => {
        this.rates = rates;
        this.ratesLoading = false;
        this.ratesError = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.rates = null;
        this.selectedCurrency = 'PLN';
        this.ratesLoading = false;
        this.ratesError = true;
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  get hasRates(): boolean {
    return !!this.rates;
  }

  get showExchangeMeta(): boolean {
    return this.hasRates && this.selectedCurrency !== 'PLN';
  }

  get baseTotal(): number {
    let sum = 0;

    for (const item of this.mainCoverages) {
      if (this.isOn(item.key)) {
        sum += item.pricePln;
      }
    }

    for (const item of this.addons) {
      if (this.isOn(item.key)) {
        sum += item.pricePln;
      }
    }

    return sum;
  }

  get displayTotal(): number {
    return this.convertAmount(this.baseTotal);
  }

  toggle(key: CoverageKey): void {
    if (key === 'oc') return;
    this.cov[key] = !this.cov[key];
  }

  isOn(key: CoverageKey): boolean {
    return this.cov[key];
  }

  onCurrencyChange(currency: SupportedCurrencyCode): void {
    if (currency === 'PLN' || this.canUseForeignCurrency(currency)) {
      this.selectedCurrency = currency;
      return;
    }

    this.selectedCurrency = 'PLN';
  }

  canUseForeignCurrency(currency: SupportedCurrencyCode): boolean {
    return currency === 'PLN' || this.hasRates;
  }

  convertAmount(amountPln: number): number {
    if (this.selectedCurrency === 'PLN') {
      return amountPln;
    }

    const rate = this.rates?.rates[this.selectedCurrency];

    if (!rate) {
      return amountPln;
    }

    return amountPln / rate;
  }

  displayAmount(amountPln: number): number {
    return this.convertAmount(amountPln);
  }

  formatAmount(amount: number, currency: SupportedCurrencyCode): string {
    return `${this.formatDecimalNumber(amount, 2)} ${currency}`;
  }

  displayRate(currency: SupportedCurrencyCode = this.selectedCurrency): number {
    if (currency === 'PLN') {
      return 1;
    }

    return this.rates?.rates[currency] ?? 1;
  }

  formatRate(rate: number): string {
    return this.formatDecimalNumber(rate, 4);
  }

  formatEffectiveDate(date: string): string {
    const parsedDate = new Date(`${date}T00:00:00`);

    if (Number.isNaN(parsedDate.getTime())) {
      return date;
    }

    return new Intl.DateTimeFormat('pl-PL').format(parsedDate);
  }

  openDetail(route: string | null): void {
    if (route) {
      void this.router.navigate([route]);
    }
  }

  next(): void {
    this.draft.patchCoverages({
      ...this.cov,
      promoCode: this.promoCode,
      totalPremium: this.baseTotal,
    });

    void this.router.navigate(['/kalkulator/dane-polisowe']);
  }

  private formatDecimalNumber(value: number, fractionDigits: number): string {
    const normalizedValue = Number.isFinite(value) ? value : 0;
    const sign = normalizedValue < 0 ? '-' : '';
    const [integerPart, decimalPart] = Math.abs(normalizedValue).toFixed(fractionDigits).split('.');
    const groupedIntegerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, ' ');

    return `${sign}${groupedIntegerPart},${decimalPart}`;
  }
}
