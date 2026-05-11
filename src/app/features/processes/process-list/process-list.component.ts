import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProcessListService } from './process-list.service';
import {
  PROCESS_PAGE_SIZE_OPTIONS,
  PROCESS_STATUS_LABELS,
  PROCESS_STATUS_OPTIONS,
  ProcessListFilters,
  ProcessListItem,
  ProcessListQuery,
  ProcessSortField,
  ProcessStatus,
  buildDefaultProcessListFilters,
  buildDefaultProcessListQuery,
} from './process-list.models';

@Component({
  selector: 'app-process-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="d-flex flex-column gap-4">
      <div>
        <h2 class="mb-1">Lista procesów</h2>
        <p class="text-muted mb-0">
          Przeglądaj procesy ZRD z filtrowaniem, sortowaniem i stronicowaniem.
        </p>
      </div>

      <div class="card shadow-sm">
        <div class="card-body">
          <form class="row g-3" (ngSubmit)="applyFilters()">
            <div class="col-12 col-md-6 col-xl-4">
              <label for="status" class="form-label">Status</label>
              <select
                id="status"
                data-testid="status-filter"
                class="form-select"
                [(ngModel)]="filters.status"
                name="status"
              >
                <option value="">Wszystkie</option>
                @for (option of statusOptions; track option.value) {
                  <option [value]="option.value">{{ option.label }}</option>
                }
              </select>
            </div>

            <div class="col-12 col-md-6 col-xl-4">
              <label for="nip" class="form-label">NIP</label>
              <input
                id="nip"
                data-testid="nip-filter"
                type="text"
                class="form-control"
                [(ngModel)]="filters.nip"
                name="nip"
                placeholder="np. 1234567890"
              />
              <div class="form-text">Filtrowanie działa tylko dla wartości numerycznej.</div>
            </div>

            <div class="col-12 col-md-6 col-xl-4">
              <label for="phNumber" class="form-label">Numer PH</label>
              <input
                id="phNumber"
                data-testid="ph-number-filter"
                type="text"
                class="form-control"
                [(ngModel)]="filters.phNumber"
                name="phNumber"
                placeholder="np. PH/2026/00112"
              />
            </div>

            <div class="col-12 col-md-6 col-xl-3">
              <label for="dateFrom" class="form-label">Data od</label>
              <input
                id="dateFrom"
                data-testid="date-from-filter"
                type="text"
                class="form-control"
                [(ngModel)]="filters.dateFrom"
                name="dateFrom"
                placeholder="DD.MM.RRRR"
              />
            </div>

            <div class="col-12 col-md-6 col-xl-3">
              <label for="dateTo" class="form-label">Data do</label>
              <input
                id="dateTo"
                data-testid="date-to-filter"
                type="text"
                class="form-control"
                [(ngModel)]="filters.dateTo"
                name="dateTo"
                placeholder="DD.MM.RRRR"
              />
            </div>

            <div class="col-12 col-md-6 col-xl-3">
              <label class="form-label d-block">Obserwowane</label>
              <div class="form-check pt-2">
                <input
                  id="observed"
                  data-testid="observed-filter"
                  type="checkbox"
                  class="form-check-input"
                  [checked]="filters.observed === 'isObserved'"
                  (change)="toggleObservedFilter($event)"
                />
                <label for="observed" class="form-check-label">Tylko obserwowane</label>
              </div>
            </div>

            <div class="col-12 col-md-6 col-xl-3">
              <label for="pageSize" class="form-label">Liczba wyników</label>
              <select
                id="pageSize"
                data-testid="page-size-select"
                class="form-select"
                [(ngModel)]="query.size"
                name="pageSize"
                (ngModelChange)="changePageSize($event)"
              >
                @for (size of pageSizeOptions; track size) {
                  <option [ngValue]="size">{{ size }}</option>
                }
              </select>
            </div>

            <div class="col-12 d-flex flex-wrap gap-2">
              <button
                data-testid="apply-filters-button"
                type="submit"
                class="btn btn-primary"
                [disabled]="isLoading"
              >
                {{ isLoading ? 'Wczytywanie...' : 'Szukaj' }}
              </button>
              <button
                data-testid="clear-filters-button"
                type="button"
                class="btn btn-outline-secondary"
                (click)="clearFilters()"
                [disabled]="isLoading"
              >
                Wyczyść filtry
              </button>
            </div>
          </form>
        </div>
      </div>

      @if (errorMessage) {
        <div class="alert alert-danger d-flex flex-column flex-md-row gap-3 align-items-md-center mb-0" role="alert" data-testid="error-alert">
          <div class="flex-grow-1">
            {{ errorMessage }}
          </div>
          <button type="button" class="btn btn-outline-danger btn-sm" (click)="reload()">
            Spróbuj ponownie
          </button>
        </div>
      }

      <div class="card shadow-sm">
        <div class="card-body">
          <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-2 mb-3">
            <div class="text-muted">
              @if (total > 0) {
                Wyświetlanie {{ resultStart }}–{{ resultEnd }} z {{ total }} procesów
              } @else if (!isLoading && !errorMessage) {
                Brak wyników do wyświetlenia
              } @else {
                Ładowanie danych listy procesów...
              }
            </div>

            <div class="small text-muted">
              Sortowanie: {{ sortLabel(query.sort) }} ({{ query.order === 'asc' ? 'rosnąco' : 'malejąco' }})
            </div>
          </div>

          @if (isLoading) {
            <div class="py-5 text-center text-muted" data-testid="loading-state">
              Wczytywanie listy procesów...
            </div>
          } @else if (!errorMessage && items.length === 0) {
            <div class="alert alert-info mb-0" role="status" data-testid="empty-state">
              Nie znaleziono procesów spełniających podane kryteria.
            </div>
          } @else if (!errorMessage) {
            <div class="table-responsive">
              <table class="table table-striped table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th scope="col">
                      <button
                        data-testid="sort-id"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('id')"
                      >
                        ID {{ sortIndicator('id') }}
                      </button>
                    </th>
                    <th scope="col">
                      <button
                        data-testid="sort-phNumber"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('phNumber')"
                      >
                        Numer PH {{ sortIndicator('phNumber') }}
                      </button>
                    </th>
                    <th scope="col">
                      <button
                        data-testid="sort-nip"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('nip')"
                      >
                        NIP {{ sortIndicator('nip') }}
                      </button>
                    </th>
                    <th scope="col">Nazwa klienta</th>
                    <th scope="col">Aktywność</th>
                    <th scope="col">Segment</th>
                    <th scope="col">PH</th>
                    <th scope="col">Email PH</th>
                    <th scope="col">
                      <button
                        data-testid="sort-status"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('status')"
                      >
                        Status {{ sortIndicator('status') }}
                      </button>
                    </th>
                    <th scope="col">
                      <button
                        data-testid="sort-lastUpdated"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('lastUpdated')"
                      >
                        Ostatnia zmiana {{ sortIndicator('lastUpdated') }}
                      </button>
                    </th>
                    <th scope="col">
                      <button
                        data-testid="sort-dateCreated"
                        type="button"
                        class="btn btn-link btn-sm p-0 text-decoration-none"
                        (click)="changeSort('dateCreated')"
                      >
                        Data utworzenia {{ sortIndicator('dateCreated') }}
                      </button>
                    </th>
                    <th scope="col">Format dokumentów</th>
                    <th scope="col">Uwagi COA</th>
                    <th scope="col">Uwagi ZRD</th>
                    <th scope="col">Obserwowane</th>
                    <th scope="col" class="text-end">Akcje</th>
                  </tr>
                </thead>
                <tbody>
                  @for (item of items; track item.id) {
                    <tr>
                      <td>{{ item.id }}</td>
                      <td>{{ item.phNumber }}</td>
                      <td>{{ item.nip }}</td>
                      <td>{{ item.clientName }}</td>
                      <td>{{ item.activity }}</td>
                      <td>{{ item.salesSegment }}</td>
                      <td>{{ item.phName }}</td>
                      <td>{{ item.phEmail }}</td>
                      <td>
                        <span class="badge" [class]="statusBadgeClass(item.status)">
                          {{ statusLabel(item.status) }}
                        </span>
                      </td>
                      <td>{{ item.lastUpdated }}</td>
                      <td>{{ item.dateCreated }}</td>
                      <td>{{ formatDocuments(item.documentsFormat) }}</td>
                      <td [title]="item.notesFromCoa">{{ truncate(item.notesFromCoa) }}</td>
                      <td [title]="item.notesFromZrd">{{ truncate(item.notesFromZrd) }}</td>
                      <td class="text-center">
                        <span
                          class="fw-bold"
                          [class.text-warning]="item.observed"
                          [class.text-muted]="!item.observed"
                          [attr.aria-label]="item.observed ? 'obserwowany' : 'nieobserwowany'"
                        >
                          {{ item.observed ? '★' : '—' }}
                        </span>
                      </td>
                      <td class="text-end">
                        <a
                          [routerLink]="['/processes', item.id]"
                          class="btn btn-outline-primary btn-sm"
                          [attr.data-testid]="'detail-link-' + item.id"
                        >
                          Szczegóły
                        </a>
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        </div>

        @if (!isLoading && !errorMessage && totalPages > 0) {
          <div class="card-footer bg-white border-top-0">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
              <div class="small text-muted">
                Strona {{ query.page }} z {{ totalPages }}
              </div>

              <nav aria-label="Paginacja listy procesów">
                <ul class="pagination pagination-sm mb-0">
                  <li class="page-item" [class.disabled]="query.page === 1">
                    <button
                      data-testid="previous-page"
                      type="button"
                      class="page-link"
                      (click)="changePage(query.page - 1)"
                      [disabled]="query.page === 1"
                    >
                      Poprzednia
                    </button>
                  </li>

                  @for (page of pageNumbers; track page) {
                    <li class="page-item" [class.active]="page === query.page">
                      <button
                        type="button"
                        class="page-link"
                        [attr.data-testid]="'page-' + page"
                        (click)="changePage(page)"
                      >
                        {{ page }}
                      </button>
                    </li>
                  }

                  <li class="page-item" [class.disabled]="query.page === totalPages">
                    <button
                      data-testid="next-page"
                      type="button"
                      class="page-link"
                      (click)="changePage(query.page + 1)"
                      [disabled]="query.page === totalPages"
                    >
                      Następna
                    </button>
                  </li>
                </ul>
              </nav>
            </div>
          </div>
        }
      </div>
    </div>
  `,
  styles: [
    `:host {
      overflow: visible;
      display: block;
    }

    /* Make the table horizontally scrollable if it overflows the viewport */
    :host .table-responsive {
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;
    }

    /* Ensure the table doesn't collapse columns - set reasonable min-width */
    :host .table-responsive .table {
      min-width: 1200px;
      table-layout: auto;
      white-space: nowrap;
    }

    /* Prevent automatic text wrapping in cells and show ellipsis for overflow */
    :host .table-responsive th,
    :host .table-responsive td {
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    /* Slightly smaller min-width for small screens */
    @media (max-width: 768px) {
      :host .table-responsive .table {
        min-width: 1000px;
      }
    }
    `,
  ],
})
export class ProcessListComponent implements OnInit {
  private readonly processListService = inject(ProcessListService);

  readonly statusOptions = PROCESS_STATUS_OPTIONS;
  readonly pageSizeOptions = PROCESS_PAGE_SIZE_OPTIONS;

  filters: ProcessListFilters = buildDefaultProcessListFilters();
  query: ProcessListQuery = buildDefaultProcessListQuery();
  items: ProcessListItem[] = [];
  total = 0;
  totalPages = 0;
  isLoading = false;
  errorMessage = '';

  ngOnInit(): void {
    void this.loadProcesses();
  }

  get pageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, index) => index + 1);
  }

  get resultStart(): number {
    return this.total === 0 ? 0 : (this.query.page - 1) * this.query.size + 1;
  }

  get resultEnd(): number {
    return Math.min(this.query.page * this.query.size, this.total);
  }

  async applyFilters(): Promise<void> {
    this.query = {
      ...this.query,
      ...this.filters,
      page: 1,
    };

    await this.loadProcesses();
  }

  async clearFilters(): Promise<void> {
    this.filters = buildDefaultProcessListFilters();
    this.query = buildDefaultProcessListQuery();

    await this.loadProcesses();
  }

  async changeSort(field: ProcessSortField): Promise<void> {
    const order =
      this.query.sort === field ? (this.query.order === 'asc' ? 'desc' : 'asc') : 'asc';

    this.query = {
      ...this.query,
      sort: field,
      order,
      page: 1,
    };

    await this.loadProcesses();
  }

  async changePage(page: number): Promise<void> {
    if (page < 1 || page > this.totalPages || page === this.query.page) {
      return;
    }

    this.query = {
      ...this.query,
      page,
    };

    await this.loadProcesses();
  }

  async changePageSize(size: number): Promise<void> {
    this.query = {
      ...this.query,
      size: Number(size),
      page: 1,
    };

    await this.loadProcesses();
  }

  toggleObservedFilter(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.filters.observed = checkbox.checked ? 'isObserved' : '';
  }

  async reload(): Promise<void> {
    await this.loadProcesses();
  }

  sortIndicator(field: ProcessSortField): string {
    if (this.query.sort !== field) {
      return '';
    }

    return this.query.order === 'asc' ? '↑' : '↓';
  }

  sortLabel(field: ProcessSortField): string {
    switch (field) {
      case 'id':
        return 'ID';
      case 'phNumber':
        return 'numer PH';
      case 'nip':
        return 'NIP';
      case 'status':
        return 'status';
      case 'lastUpdated':
        return 'ostatnia zmiana';
      case 'dateCreated':
        return 'data utworzenia';
    }
  }

  statusLabel(status: ProcessStatus): string {
    return PROCESS_STATUS_LABELS[status];
  }

  statusBadgeClass(status: ProcessStatus): string {
    switch (status) {
      case 'WAITING':
        return 'bg-warning text-dark';
      case 'ACCEPTED':
        return 'bg-success';
      case 'REJECTED':
        return 'bg-danger';
      case 'CORRECTION':
        return 'bg-info text-dark';
      case 'NEW':
        return 'bg-secondary';
      default:
        return 'bg-primary';
    }
  }

  formatDocuments(format: ProcessListItem['documentsFormat']): string {
    switch (format) {
      case 'ELECTRONIC':
        return 'Elektroniczne';
      case 'PAPER':
        return 'Papierowe';
      case 'TEMPLATE':
        return 'Szablon';
    }
  }

  truncate(value: string, maxLength = 36): string {
    return value.length > maxLength ? `${value.slice(0, maxLength - 3)}...` : value;
  }

  private async loadProcesses(): Promise<void> {
    this.isLoading = true;
    this.errorMessage = '';

    try {
      const response = await this.processListService.getProcesses(this.query);

      this.items = response.items;
      this.total = response.total;
      this.totalPages = response.totalPages;
      this.query = {
        ...this.query,
        page: response.page,
        size: response.size,
        sort: response.sort,
        order: response.order,
      };
    } catch {
      this.items = [];
      this.total = 0;
      this.totalPages = 0;
      this.errorMessage = 'Nie udało się wczytać listy procesów. Spróbuj ponownie.';
    } finally {
      this.isLoading = false;
    }
  }
}
