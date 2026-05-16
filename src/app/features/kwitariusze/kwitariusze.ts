import { Component, inject, signal, ViewChild, AfterViewInit, effect } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TitleCasePipe } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DisplayCurrency, ExchangeRate, RemoteDisplayCurrency } from '../../core/models/exchange-rate.model';
import { Kwitariusz, KwitariuszType } from '../../core/models/kwitariusz.model';
import { KwitariuszService } from '../../core/services/kwitariusz.service';
import { NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';

@Component({
  selector: 'app-kwitariusze',
  standalone: true,
  imports: [RouterLink, FormsModule, TitleCasePipe,
            MatTableModule, MatSortModule, MatIconModule,
            MatButtonModule, MatMenuModule, MatTooltipModule],
  templateUrl: './kwitariusze.html',
  styleUrl: './kwitariusze.scss',
})
export class KwitariuszeComponent implements AfterViewInit {
  readonly service = inject(KwitariuszService);
  private readonly nbpExchangeRateService = inject(NbpExchangeRateService);

  readonly dataSource = new MatTableDataSource<Kwitariusz>();
  readonly displayedColumns = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'amount', 'status', 'actions'];
  readonly currencyOptions: DisplayCurrency[] = ['PLN', 'EUR', 'USD'];

  readonly expandedFilter = signal<'policy' | 'insured' | null>(null);
  readonly selectedCurrency = signal<DisplayCurrency>('PLN');
  readonly appliedRate = signal<ExchangeRate | null>(null);
  readonly isRateLoading = signal(false);
  readonly rateError = signal('');
  readonly rateCache = signal<Partial<Record<RemoteDisplayCurrency, ExchangeRate>>>({});
  readonly pendingCurrency = signal<RemoteDisplayCurrency | null>(null);

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    effect(() => { this.dataSource.data = this.service.filtered(); });
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get activeType()     { return this.service.filterType(); }
  get last30Active()   { return this.service.filterLast30Days(); }
  get policySearch()   { return this.service.filterPolicySearch(); }
  get insuredSearch()  { return this.service.filterInsuredSearch(); }
  get hasAnyFilter()   {
    return !!this.activeType || this.last30Active || !!this.policySearch || !!this.insuredSearch;
  }

  setType(t: string)   { this.service.filterType.update(v => v === t ? '' : t); }
  toggle30Days()       { this.service.filterLast30Days.update(v => !v); }

  toggleFilter(f: 'policy' | 'insured'): void {
    this.expandedFilter.update(v => v === f ? null : f);
  }

  clearFilters(): void {
    this.service.clearFilters();
    this.expandedFilter.set(null);
  }

  changeCurrency(currency: DisplayCurrency): void {
    if (currency === 'PLN') {
      this.resetToPlnPresentation(true);
      return;
    }

    this.selectedCurrency.set(currency);
    this.rateError.set('');

    const cachedRate = this.rateCache()[currency];
    if (cachedRate) {
      this.appliedRate.set(cachedRate);
      this.isRateLoading.set(false);
      this.pendingCurrency.set(null);
      return;
    }

    this.appliedRate.set(null);
    this.isRateLoading.set(true);
    this.pendingCurrency.set(currency);

    this.nbpExchangeRateService.getTodayRate(currency).subscribe({
      next: rate => {
        if (this.pendingCurrency() !== currency || this.selectedCurrency() !== currency) {
          return;
        }

        this.rateCache.update(cache => ({ ...cache, [currency]: rate }));
        this.appliedRate.set(rate);
        this.isRateLoading.set(false);
        this.pendingCurrency.set(null);
      },
      error: () => {
        if (this.pendingCurrency() !== currency || this.selectedCurrency() !== currency) {
          return;
        }

        this.resetToPlnPresentation(false);
        this.rateError.set(`Nie udało się pobrać kursu ${currency} z NBP. Wyświetlono kwoty w PLN.`);
      },
    });
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

  currentAppliedRate(): ExchangeRate | null {
    const rate = this.appliedRate();
    return rate && rate.currencyCode === this.selectedCurrency() ? rate : null;
  }

  displayAmount(amountPln: number): string {
    const currency = this.effectiveDisplayCurrency();
    const convertedAmount = this.convertAmount(amountPln);
    const suffix = currency === 'PLN' ? 'zł' : currency;

    return `${convertedAmount.toLocaleString('pl-PL', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })} ${suffix}`;
  }

  formatRate(rate: number): string {
    return rate.toLocaleString('pl-PL', {
      minimumFractionDigits: 4,
      maximumFractionDigits: 4,
    });
  }

  private effectiveDisplayCurrency(): DisplayCurrency {
    const selectedCurrency = this.selectedCurrency();
    const rate = this.appliedRate();

    if (selectedCurrency === 'PLN') {
      return 'PLN';
    }

    return rate && rate.currencyCode === selectedCurrency ? selectedCurrency : 'PLN';
  }

  private convertAmount(amountPln: number): number {
    const rate = this.currentAppliedRate();
    return rate ? amountPln / rate.midRate : amountPln;
  }

  private resetToPlnPresentation(clearError: boolean): void {
    this.selectedCurrency.set('PLN');
    this.appliedRate.set(null);
    this.isRateLoading.set(false);
    this.pendingCurrency.set(null);

    if (clearError) {
      this.rateError.set('');
    }
  }
}
