import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import {
  ExchangeRateService,
  PresentationCurrency,
} from '../../../../core/services/exchange-rate.service';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

interface CoverageItem {
  key: string;
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

  private currencyRequestId = 0;

  readonly currencies: PresentationCurrency[] = ['PLN', 'USD', 'EUR'];

  cov = { ...this.draft.draft().coverages };

  mainCoverages: CoverageItem[] = [
    { key: 'oc',         label: 'OC',         desc: 'Obowiązkowe ubezpieczenie odpowiedzialności cywilnej', price: 289, route: null, required: true },
    { key: 'ac',         label: 'AC',         desc: 'Pełny zakres casco',                                   price: 389, route: '/kalkulator/casco',      required: false },
    { key: 'assistance', label: 'Assistance', desc: 'Wsparcie na terenie Europy',                           price:  89, route: '/kalkulator/assistance', required: false },
    { key: 'nnwKip',     label: 'NNW KiP',    desc: 'Suma ubezpieczenia do 50 000 zł',                     price: 129, route: '/kalkulator/nnw',        required: false },
  ];

  addons: CoverageItem[] = [
    { key: 'szyby',  label: 'Szyby',  desc: 'Uszkodzenie, połknięcie lub wymiana szyb',               price: 150, route: '/kalkulator/szyby' },
    { key: 'bagaz',  label: 'Bagaż',  desc: 'Uszkodzenie lub kradzież przewożonego bagażu',            price:  89, route: null },
    { key: 'opony',  label: 'Opony',  desc: 'Pokrycie kosztów naprawy lub wymiany opon',               price:  89, route: null },
  ];

  promoCode = this.cov.promoCode;

  selectedCurrency: PresentationCurrency = 'PLN';
  currentRate = 1;
  rateEffectiveDate: string | null = null;
  isRateLoading = false;
  rateError: string | null = null;

  get total(): number {
    let sum = 289; // OC always
    if (this.cov.ac)         sum += 389;
    if (this.cov.assistance) sum += 89;
    if (this.cov.nnwKip)     sum += 129;
    if (this.cov.szyby)      sum += 150;
    if (this.cov.bagaz)      sum += 89;
    if (this.cov.opony)      sum += 89;
    return sum;
  }

  get displayCurrency(): PresentationCurrency {
    if (this.selectedCurrency === 'PLN') {
      return 'PLN';
    }

    return this.rateEffectiveDate && !this.rateError ? this.selectedCurrency : 'PLN';
  }

  get showRateDate(): boolean {
    return this.displayCurrency !== 'PLN' && !!this.rateEffectiveDate && !this.isRateLoading;
  }

  toggle(key: string) {
    (this.cov as any)[key] = !(this.cov as any)[key];
  }

  isOn(key: string): boolean {
    return !!(this.cov as any)[key];
  }

  onCurrencySelectChange(event: Event) {
    const value = (event.target as HTMLSelectElement | null)?.value;

    if (!value || !this.isPresentationCurrency(value)) {
      return;
    }

    this.onCurrencyChange(value);
  }

  onCurrencyChange(currency: PresentationCurrency) {
    this.selectedCurrency = currency;
    this.rateError = null;

    const requestId = ++this.currencyRequestId;

    if (currency === 'PLN') {
      this.resetRateState();
      return;
    }

    this.currentRate = 1;
    this.rateEffectiveDate = null;
    this.isRateLoading = true;

    this.exchangeRateService.getRate(currency).subscribe({
      next: result => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.currentRate = result.rate;
        this.rateEffectiveDate = result.effectiveDate;
        this.isRateLoading = false;
        this.rateError = null;
      },
      error: () => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.resetRateState();
        this.selectedCurrency = 'PLN';
        this.rateError = 'Nie udało się pobrać kursu waluty. Wyświetlamy kwoty w PLN.';
      },
    });
  }

  convertAmount(amountInPln: number): number {
    const amount =
      this.displayCurrency === 'PLN' ? amountInPln : amountInPln / this.currentRate;

    return this.roundToTwoDecimals(amount);
  }

  formatPrice(amountInPln: number): string {
    return `${this.convertAmount(amountInPln).toFixed(2)} ${this.displayCurrency}`;
  }

  openDetail(route: string | null) {
    if (route) this.router.navigate([route]);
  }

  next() {
    this.draft.patchCoverages({
      ...this.cov,
      promoCode: this.promoCode,
      totalPremium: this.total,
    });
    this.router.navigate(['/kalkulator/dane-polisowe']);
  }

  private resetRateState() {
    this.currentRate = 1;
    this.rateEffectiveDate = null;
    this.isRateLoading = false;
  }

  private roundToTwoDecimals(value: number): number {
    return Math.round((value + Number.EPSILON) * 100) / 100;
  }

  private isPresentationCurrency(value: string): value is PresentationCurrency {
    return this.currencies.includes(value as PresentationCurrency);
  }
}
