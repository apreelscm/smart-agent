import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild,
  effect,
  inject,
  signal,
} from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';

import {
  CurrencyRateSnapshot,
  DisplayCurrency,
  ForeignCurrency,
} from '../../core/models/currency.model';
import { Kwitariusz, KwitariuszType } from '../../core/models/kwitariusz.model';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';

@Component({
  selector: 'app-kwitariusze',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule,
    TitleCasePipe,
    MatTableModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule,
  ],
  templateUrl: './kwitariusze.html',
  styleUrl: './kwitariusze.scss',
})
export class KwitariuszeComponent implements AfterViewInit {
  readonly service = inject(KwitariuszService);
  private readonly nbpExchangeRateService = inject(NbpExchangeRateService);

  readonly dataSource = new MatTableDataSource<Kwitariusz>();
  readonly displayedColumns = [
    'type',
    'number',
    'policyNumber',
    'insuredName',
    'issueDate',
    'amount',
    'status',
    'actions',
  ];

  readonly expandedFilter = signal<'policy' | 'insured' | null>(null);
  readonly selectedCurrency = signal<DisplayCurrency>('PLN');
  readonly isRateLoading = signal(false);
  readonly currencyError = signal<string | null>(null);
  readonly displayCurrencies = [
    { value: 'PLN', label: 'PLN' },
    { value: 'USD', label: 'USD' },
    { value: 'EUR', label: 'EUR' },
  ] as const satisfies ReadonlyArray<{ value: DisplayCurrency; label: string }>;

  private readonly cachedRates = new Map<ForeignCurrency, CurrencyRateSnapshot>();
  private readonly currencyFormatters: Record<DisplayCurrency, Intl.NumberFormat> = {
    PLN: new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: 'PLN',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }),
    USD: new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: 'USD',
      currencyDisplay: 'code',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }),
    EUR: new Intl.NumberFormat('pl-PL', {
      style: 'currency',
      currency: 'EUR',
      currencyDisplay: 'code',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }),
  };

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('displayCurrencySelect')
  private displayCurrencySelect?: ElementRef<HTMLSelectElement>;

  constructor() {
    effect(() => {
      this.dataSource.data = this.service.filtered();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  get activeType() {
    return this.service.filterType();
  }

  get last30Active() {
    return this.service.filterLast30Days();
  }

  get policySearch() {
    return this.service.filterPolicySearch();
  }

  get insuredSearch() {
    return this.service.filterInsuredSearch();
  }

  get hasAnyFilter() {
    return !!this.activeType || this.last30Active || !!this.policySearch || !!this.insuredSearch;
  }

  setType(t: string) {
    this.service.filterType.update((v) => (v === t ? '' : t));
  }

  toggle30Days() {
    this.service.filterLast30Days.update((v) => !v);
  }

  toggleFilter(f: 'policy' | 'insured'): void {
    this.expandedFilter.update((v) => (v === f ? null : f));
  }

  clearFilters(): void {
    this.service.clearFilters();
    this.expandedFilter.set(null);
  }

  async changeCurrency(nextCurrency: string): Promise<void> {
    if (!this.isDisplayCurrency(nextCurrency) || this.isRateLoading()) {
      this.syncCurrencySelectWithState();
      return;
    }

    if (nextCurrency === this.selectedCurrency()) {
      this.syncCurrencySelectWithState();
      return;
    }

    this.currencyError.set(null);

    if (nextCurrency === 'PLN') {
      this.selectedCurrency.set('PLN');
      this.syncCurrencySelectWithState();
      return;
    }

    const cachedRate = this.cachedRates.get(nextCurrency);
    if (cachedRate) {
      this.selectedCurrency.set(nextCurrency);
      this.syncCurrencySelectWithState();
      return;
    }

    this.isRateLoading.set(true);

    try {
      const rateSnapshot = await firstValueFrom(this.nbpExchangeRateService.getRate(nextCurrency));
      this.cachedRates.set(nextCurrency, rateSnapshot);
      this.selectedCurrency.set(nextCurrency);
    } catch {
      this.currencyError.set(
        'Nie udało się pobrać kursu NBP. Wyświetlane kwoty pozostały bez zmian.',
      );
    } finally {
      this.isRateLoading.set(false);
      this.syncCurrencySelectWithState();
    }
  }

  typeIcon(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'payments' : 'sync_alt';
  }

  typeLabel(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'Rata + odsetki' : 'Rekalkulacja + odsetki';
  }

  totalAmount(k: Kwitariusz): number {
    return +(k.baseAmount + k.interest).toFixed(2);
  }

  formatAmount(amountPln: number): string {
    const currency = this.selectedCurrency();
    const convertedAmount = this.convertFromPln(amountPln, currency);

    return this.currencyFormatters[currency].format(convertedAmount);
  }

  private convertFromPln(amountPln: number, currency: DisplayCurrency): number {
    if (currency === 'PLN') {
      return this.roundAmount(amountPln);
    }

    const rateSnapshot = this.cachedRates.get(currency);
    if (!rateSnapshot) {
      return this.roundAmount(amountPln);
    }

    return this.roundAmount(amountPln / rateSnapshot.rate);
  }

  private roundAmount(amount: number): number {
    return Number(amount.toFixed(2));
  }

  private isDisplayCurrency(value: string): value is DisplayCurrency {
    return value === 'PLN' || value === 'USD' || value === 'EUR';
  }

  private syncCurrencySelectWithState(): void {
    if (this.displayCurrencySelect) {
      this.displayCurrencySelect.nativeElement.value = this.selectedCurrency();
    }
  }
}
