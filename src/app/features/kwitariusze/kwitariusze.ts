import { TitleCasePipe } from '@angular/common';
import { AfterViewInit, Component, effect, inject, OnDestroy, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';

import { CurrencyRate, ForeignPresentationCurrency, PresentationCurrency } from '../../core/models/currency-rate.model';
import { Kwitariusz, KwitariuszType } from '../../core/models/kwitariusz.model';
import { CurrencyRateService } from '../../core/services/currency-rate.service';
import { KwitariuszService } from '../../core/services/kwitariusz.service';

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
export class KwitariuszeComponent implements AfterViewInit, OnDestroy {
  readonly service = inject(KwitariuszService);
  private readonly currencyRateService = inject(CurrencyRateService);

  readonly dataSource = new MatTableDataSource<Kwitariusz>();
  readonly displayedColumns = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'amount', 'status', 'actions'];

  readonly expandedFilter = signal<'policy' | 'insured' | null>(null);
  readonly selectedCurrency = signal<PresentationCurrency>('PLN');
  readonly rateMeta = signal<CurrencyRate | null>(null);
  readonly isRateLoading = signal(false);
  readonly rateError = signal<string | null>(null);

  private readonly rateCache = new Map<ForeignPresentationCurrency, CurrencyRate>();
  private rateRequestId = 0;
  private rateSubscription?: Subscription;

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.dataSource.sortingDataAccessor = (item, property) => {
      if (property === 'amount') {
        return this.displayTotalAmountValue(item) ?? 0;
      }

      if (property === 'issueDate') {
        return new Date(item.issueDate).getTime();
      }

      const value = item[property as keyof Kwitariusz];

      if (typeof value === 'number') {
        return value;
      }

      if (typeof value === 'string') {
        return value.toLowerCase();
      }

      return '';
    };

    effect(() => {
      this.selectedCurrency();
      this.rateMeta();
      this.isRateLoading();
      this.rateError();
      this.dataSource.data = [...this.service.filtered()];
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  ngOnDestroy(): void {
    this.rateSubscription?.unsubscribe();
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
    this.service.filterType.update((value) => value === t ? '' : t);
  }

  toggle30Days() {
    this.service.filterLast30Days.update((value) => !value);
  }

  toggleFilter(f: 'policy' | 'insured'): void {
    this.expandedFilter.update((value) => value === f ? null : f);
  }

  clearFilters(): void {
    this.service.clearFilters();
    this.expandedFilter.set(null);
  }

  onCurrencyChange(currency: PresentationCurrency): void {
    if (currency === this.selectedCurrency()) {
      return;
    }

    this.selectedCurrency.set(currency);
    this.rateRequestId += 1;
    this.rateSubscription?.unsubscribe();

    if (currency === 'PLN') {
      this.clearRateState();
      return;
    }

    const cachedRate = this.rateCache.get(currency);

    if (cachedRate) {
      this.rateMeta.set(cachedRate);
      this.rateError.set(null);
      this.isRateLoading.set(false);
      return;
    }

    this.rateMeta.set(null);
    this.rateError.set(null);
    this.isRateLoading.set(true);

    const requestId = this.rateRequestId;

    this.rateSubscription = this.currencyRateService.getLatestRate(currency).subscribe({
      next: (rate) => {
        if (requestId !== this.rateRequestId || this.selectedCurrency() !== currency) {
          return;
        }

        this.rateCache.set(currency, rate);
        this.rateMeta.set(rate);
        this.rateError.set(null);
        this.isRateLoading.set(false);
      },
      error: () => {
        if (requestId !== this.rateRequestId || this.selectedCurrency() !== currency) {
          return;
        }

        this.rateMeta.set(null);
        this.rateError.set('Nie udało się pobrać kursu NBP dla wybranej waluty.');
        this.isRateLoading.set(false);
      },
    });
  }

  typeIcon(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'payments' : 'sync_alt';
  }

  typeLabel(t: KwitariuszType): string {
    return t === 'rata-odsetki' ? 'Rata + odsetki' : 'Rekalkulacja + odsetki';
  }

  displayTotalAmount(row: Kwitariusz): string {
    const value = this.displayTotalAmountValue(row);
    return value === null ? '—' : this.formatAmount(value);
  }

  displayInterestAmount(row: Kwitariusz): string {
    const value = this.convertAmount(row.interest);
    return value === null ? '—' : this.formatAmount(value);
  }

  formatRateMid(mid: number): string {
    return mid.toFixed(4).replace('.', ',');
  }

  private clearRateState(): void {
    this.rateMeta.set(null);
    this.rateError.set(null);
    this.isRateLoading.set(false);
  }

  private displayTotalAmountValue(row: Kwitariusz): number | null {
    return this.convertAmount(this.sourceTotalAmount(row));
  }

  private convertAmount(plnValue: number): number | null {
    if (this.selectedCurrency() === 'PLN') {
      return this.roundAmount(plnValue);
    }

    const rate = this.rateMeta();

    if (!rate) {
      return null;
    }

    return this.roundAmount(plnValue / rate.mid);
  }

  private sourceTotalAmount(k: Kwitariusz): number {
    return k.baseAmount + k.interest;
  }

  private roundAmount(value: number): number {
    return +value.toFixed(2);
  }

  private formatAmount(value: number): string {
    const currency = this.selectedCurrency();
    const suffix = currency === 'PLN' ? 'zł' : currency;
    return `${value.toFixed(2).replace('.', ',')} ${suffix}`;
  }
}
