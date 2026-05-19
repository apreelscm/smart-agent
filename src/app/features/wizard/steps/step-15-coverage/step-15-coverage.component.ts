import { DestroyRef, Component, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, catchError, map, of, switchMap } from 'rxjs';
import {
  ExchangeRateInfo,
  ForeignCurrency,
  SupportedCurrency,
} from '../../../../core/models/exchange-rate.model';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';
import { ExchangeRateService } from '../../../../core/services/exchange-rate.service';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';

type CoverageToggleKey = 'oc' | 'ac' | 'assistance' | 'nnwKip' | 'szyby' | 'bagaz' | 'opony';

interface CoverageOption {
  key: CoverageToggleKey;
  label: string;
  desc: string;
  basePricePln: number;
  route: string | null;
  required?: boolean;
}

type CurrencySelectionResult =
  | { kind: 'pln' }
  | { kind: 'rate'; currency: ForeignCurrency; rateInfo: ExchangeRateInfo }
  | { kind: 'error' };

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
  private destroyRef = inject(DestroyRef);

  private currencySelection$ = new Subject<SupportedCurrency>();

  readonly supportedCurrencies: SupportedCurrency[] = ['PLN', 'USD', 'EUR'];

  cov = { ...this.draft.draft().coverages };

  mainCoverages: CoverageOption[] = [
    {
      key: 'oc',
      label: 'OC',
      desc: 'Obowiązkowe ubezpieczenie odpowiedzialności cywilnej',
      basePricePln: 289,
      route: null,
      required: true,
    },
    {
      key: 'ac',
      label: 'AC',
      desc: 'Pełny zakres casco',
      basePricePln: 389,
      route: '/kalkulator/casco',
      required: false,
    },
    {
      key: 'assistance',
      label: 'Assistance',
      desc: 'Pomoc na terenie Europy',
      basePricePln: 89,
      route: '/kalkulator/assistance',
      required: false,
    },
    {
      key: 'nnwKip',
      label: 'NNW KiP',
      desc: 'Suma ubezpieczenia do 50 000 zł',
      basePricePln: 129,
      route: '/kalkulator/nnw',
      required: false,
    },
  ];

  addons: CoverageOption[] = [
    {
      key: 'szyby',
      label: 'Szyby',
      desc: 'Uszkodzenie, połknięcie lub wymiana szyb',
      basePricePln: 150,
      route: '/kalkulator/szyby',
    },
    {
      key: 'bagaz',
      label: 'Bagaż',
      desc: 'Uszkodzenie lub kradzież przewożonego bagażu',
      basePricePln: 89,
      route: null,
    },
    {
      key: 'opony',
      label: 'Opony',
      desc: 'Pokrycie kosztów naprawy lub wymiany opon',
      basePricePln: 89,
      route: null,
    },
  ];

  promoCode = this.cov.promoCode;

  appliedCurrency: SupportedCurrency = 'PLN';
  pendingCurrency: SupportedCurrency = 'PLN';
  rateInfo: ExchangeRateInfo | null = null;
  currencyError: string | null = null;
  isCurrencyLoading = false;

  constructor() {
    this.currencySelection$
      .pipe(
        switchMap(currency => {
          if (currency === 'PLN') {
            return of<CurrencySelectionResult>({ kind: 'pln' });
          }

          return this.exchangeRateService.getRate(currency).pipe(
            map(rateInfo => ({ kind: 'rate', currency, rateInfo }) as CurrencySelectionResult),
            catchError(() => of<CurrencySelectionResult>({ kind: 'error' }))
          );
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(result => {
        this.isCurrencyLoading = false;

        if (result.kind === 'pln') {
          this.appliedCurrency = 'PLN';
          this.pendingCurrency = 'PLN';
          this.rateInfo = null;
          this.currencyError = null;
          return;
        }

        if (result.kind === 'rate') {
          this.appliedCurrency = result.currency;
          this.pendingCurrency = result.currency;
          this.rateInfo = result.rateInfo;
          this.currencyError = null;
          return;
        }

        this.pendingCurrency = this.appliedCurrency;
        this.currencyError =
          'Nie udało się pobrać kursu waluty. Pozostawiliśmy ostatnio wyświetlone wartości.';
      });
  }

  get totalPln(): number {
    return [...this.mainCoverages, ...this.addons]
      .filter(item => item.required || this.isOn(item.key))
      .reduce((sum, item) => sum + item.basePricePln, 0);
  }

  get showRateMetadata(): boolean {
    return this.appliedCurrency !== 'PLN' && this.rateInfo !== null;
  }

  onCurrencySelected(currency: SupportedCurrency): void {
    if (currency === this.appliedCurrency && !this.isCurrencyLoading) {
      return;
    }

    this.pendingCurrency = currency;
    this.currencyError = null;
    this.isCurrencyLoading = currency !== 'PLN';

    this.currencySelection$.next(currency);
  }

  toggle(key: CoverageToggleKey): void {
    (this.cov as Record<string, boolean>)[key] = !this.isOn(key);
  }

  isOn(key: CoverageToggleKey): boolean {
    return !!(this.cov as Record<string, boolean>)[key];
  }

  getDisplayAmount(basePricePln: number): number {
    if (this.appliedCurrency === 'PLN' || !this.rateInfo) {
      return basePricePln;
    }

    return basePricePln / this.rateInfo.mid;
  }

  formatAmount(basePricePln: number): string {
    return `${this.getDisplayAmount(basePricePln).toFixed(2)} ${this.appliedCurrency}`;
  }

  formatRate(value: number): string {
    return value.toFixed(4);
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
      totalPremium: this.totalPln,
    });

    void this.router.navigate(['/kalkulator/dane-polisowe']);
  }
}
