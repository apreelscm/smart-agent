import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import {
  ForeignCurrencyCode,
  NbpExchangeRate,
  NbpExchangeRateService,
} from '../../../../core/services/nbp-exchange-rate.service';

type SupportedCurrency = 'PLN' | ForeignCurrencyCode;
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
export class Step15CoverageComponent {
  private readonly draft = inject(PolicyDraftService);
  private readonly router = inject(Router);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly nbpExchangeRateService = inject(NbpExchangeRateService);

  private readonly amountFormatter = new Intl.NumberFormat('pl-PL', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

  private readonly rateFormatter = new Intl.NumberFormat('pl-PL', {
    minimumFractionDigits: 4,
    maximumFractionDigits: 4,
  });

  private currencyRequestId = 0;

  readonly currencies: SupportedCurrency[] = ['PLN', 'USD', 'EUR'];
  readonly conversionErrorMessage =
    'Nie udało się pobrać kursu waluty. Pozostawiono poprzednio wyświetlane kwoty.';

  cov = { ...this.draft.draft().coverages, oc: true };

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

  promoCode = this.cov.promoCode;

  activeCurrency: SupportedCurrency = 'PLN';
  selectedCurrency: SupportedCurrency = 'PLN';
  loading = false;
  exchangeRateInfo: NbpExchangeRate | null = null;
  conversionError = '';

  get totalPln(): number {
    return this.getSelectedItems().reduce((sum, item) => sum + item.pricePln, 0);
  }

  get totalDisplayed(): number {
    const totalMinor = this.getSelectedItems().reduce(
      (sum, item) => sum + this.toMinorUnits(this.getDisplayedAmount(item.pricePln)),
      0
    );

    return this.fromMinorUnits(totalMinor);
  }

  toggle(key: CoverageKey) {
    if (key === 'oc') {
      return;
    }

    this.cov[key] = !this.cov[key];
  }

  isOn(key: CoverageKey): boolean {
    return this.cov[key];
  }

  onCurrencyChange(currency: SupportedCurrency): void {
    if (currency === this.activeCurrency) {
      this.selectedCurrency = this.activeCurrency;
      return;
    }

    this.conversionError = '';

    if (currency === 'PLN') {
      this.currencyRequestId++;
      this.loading = false;
      this.activeCurrency = 'PLN';
      this.selectedCurrency = 'PLN';
      this.exchangeRateInfo = null;
      return;
    }

    this.selectedCurrency = currency;
    this.loading = true;

    const requestId = ++this.currencyRequestId;

    this.nbpExchangeRateService.getRate(currency).subscribe({
      next: rate => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.activeCurrency = currency;
        this.selectedCurrency = currency;
        this.exchangeRateInfo = rate;
        this.conversionError = '';
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.selectedCurrency = this.activeCurrency;
        this.conversionError = this.conversionErrorMessage;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  formatAmount(amount: number, currency: SupportedCurrency = this.activeCurrency): string {
    return `${this.amountFormatter.format(amount)} ${currency}`;
  }

  formatRate(mid: number): string {
    return this.rateFormatter.format(mid);
  }

  getDisplayedAmount(amountPln: number): number {
    if (this.activeCurrency === 'PLN' || !this.exchangeRateInfo) {
      return this.fromMinorUnits(this.toMinorUnits(amountPln));
    }

    const convertedAmount = this.nbpExchangeRateService.convertFromPln(
      amountPln,
      this.exchangeRateInfo.mid
    );

    return this.fromMinorUnits(this.toMinorUnits(convertedAmount));
  }

  openDetail(route: string | null) {
    if (route) {
      this.router.navigate([route]);
    }
  }

  next() {
    this.draft.patchCoverages({
      ...this.cov,
      oc: true,
      promoCode: this.promoCode,
      totalPremium: this.totalPln,
    });
    this.router.navigate(['/kalkulator/dane-polisowe']);
  }

  private getSelectedItems(): CoverageOption[] {
    return [...this.mainCoverages, ...this.addons].filter(item => this.isOn(item.key));
  }

  private toMinorUnits(amount: number): number {
    return Math.round((amount + Number.EPSILON) * 100);
  }

  private fromMinorUnits(amount: number): number {
    return amount / 100;
  }
}
