import { AfterViewInit, Component, ViewChild, effect, inject, signal } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ExchangeRateQuote, ForeignCurrency, SupportedCurrency } from '../../core/models/exchange-rate.model';
import { Kwitariusz, KwitariuszStatus, KwitariuszType } from '../../core/models/kwitariusz.model';
import { NbpExchangeRateService } from '../../core/services/nbp-exchange-rate.service';
import { KwitariuszService } from '../../core/services/kwitariusz.service';

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
  readonly displayedColumns = ['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'status', 'amount', 'actions'];
  readonly expandedFilter = signal<'policy' | 'insured' | 'status' | null>(null);
  readonly currencyOptions: readonly SupportedCurrency[] = ['PLN', 'USD', 'EUR'];
  readonly selectedCurrency = signal<SupportedCurrency>('PLN');
  readonly rateInfo = signal<ExchangeRateQuote | null>(null);
  readonly currencyError = signal<string | null>(null);
  readonly pendingCurrency = signal<ForeignCurrency | null>(null);

  private currencyRequestId = 0;

  @ViewChild(MatSort) sort!: MatSort;

  constructor() {
    this.service.restorePersistedStatusFilter();

    effect(() => {
      this.dataSource.data = this.service.filtered();
    });

    this.dataSource.sortingDataAccessor = (row: Kwitariusz, column: string) => {
      switch (column) {
        case 'type':
          return row.type;
        case 'number':
          return row.number;
        case 'policyNumber':
          return row.policyNumber;
        case 'insuredName':
          return row.insuredName;
        case 'issueDate':
          return row.issueDate;
        case 'amount':
          return this.presentedTotalAmount(row);
        case 'status':
          return row.status;
        default:
          return (row as unknown as Record<string, string | number>)[column] ?? '';
      }
    };
  }

  ngAfterViewInit(): void { this.dataSource.sort = this.sort; }

  get activeType() { return this.service.filterType(); }
  get last30Active() { return this.service.filterLast30Days(); }
  get policySearch() { return this.service.filterPolicySearch(); }
  get insuredSearch() { return this.service.filterInsuredSearch(); }
  get availableStatuses() { return this.service.availableStatuses(); }
  get selectedStatuses() { return this.service.filterStatuses(); }
  get selectedStatusCount() { return this.selectedStatuses.length; }
  get statusFilterLabel() {
    return this.selectedStatusCount ? `Status (${this.selectedStatusCount})` : 'Status';
  }
  get hasAnyFilter() {
    return !!this.activeType || this.last30Active || !!this.policySearch || !!this.insuredSearch || this.selectedStatusCount > 0;
  }

  setType(t: string) { this.service.filterType.update(v => v === t ? '' : t); }
  toggle30Days() { this.service.filterLast30Days.update(v => !v); }

  toggleFilter(f: 'policy' | 'insured' | 'status'): void {
    this.expandedFilter.update(v => v === f ? null : f);
  }

  toggleStatus(status: KwitariuszStatus): void {
    this.service.toggleStatusFilter(status);
  }

  clearStatusFilter(): void {
    this.service.clearStatusFilter();
  }

  clearFilters(): void {
    this.service.clearFilters();
    this.expandedFilter.set(null);
  }

  changeCurrency(currency: SupportedCurrency): void {
    if (currency === this.selectedCurrency()) {
      return;
    }

    this.currencyError.set(null);
    const requestId = ++this.currencyRequestId;

    if (currency === 'PLN') {
      this.pendingCurrency.set(null);
      this.selectedCurrency.set('PLN');
      this.rateInfo.set(null);
      return;
    }

    this.pendingCurrency.set(currency);

    this.nbpExchangeRateService.getLatestOrPreviousRate(currency).subscribe({
      next: quote => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.selectedCurrency.set(currency);
        this.rateInfo.set(quote);
        this.currencyError.set(null);
        this.pendingCurrency.set(null);
      },
      error: () => {
        if (requestId !== this.currencyRequestId) {
          return;
        }

        this.pendingCurrency.set(null);
        this.currencyError.set(this.buildCurrencyErrorMessage(currency));
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

  presentedTotalAmount(k: Kwitariusz): number {
    return this.convertAmount(this.totalAmount(k));
  }

  presentedInterestAmount(k: Kwitariusz): number {
    return this.convertAmount(k.interest);
  }

  formatAmount(n: number): string {
    return `${n.toFixed(2).replace('.', ',')} ${this.selectedCurrency()}`;
  }

  formatMidRate(rate: number): string {
    return rate.toFixed(4).replace('.', ',');
  }

  private convertAmount(amountPln: number): number {
    const quote = this.rateInfo();

    if (this.selectedCurrency() === 'PLN' || !quote) {
      return this.roundAmount(amountPln);
    }

    return this.roundAmount(amountPln / quote.mid);
  }

  private roundAmount(value: number): number {
    return +value.toFixed(2);
  }

  private buildCurrencyErrorMessage(currency: ForeignCurrency): string {
    return `Nie udało się pobrać kursu NBP dla ${currency}. Zachowano poprzednią prezentację kwot.`;
  }
}
