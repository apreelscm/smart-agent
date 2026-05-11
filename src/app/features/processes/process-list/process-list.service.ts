import { Injectable } from '@angular/core';
import { MOCK_PROCESS_LIST_ITEMS } from './process-list.mock';
import {
  ProcessListItem,
  ProcessListQuery,
  ProcessListResponse,
  ProcessSortField,
  ProcessStatus,
  ProcessObservedFilter,
  buildDefaultProcessListQuery,
  isProcessSortField,
  isProcessStatus,
  parseProcessDate,
} from './process-list.models';

@Injectable({
  providedIn: 'root',
})
export class ProcessListService {
  private shouldFailNextRequest = false;

  failNextRequest(): void {
    this.shouldFailNextRequest = true;
  }

  async getProcesses(query: Partial<ProcessListQuery> = {}): Promise<ProcessListResponse> {
    await Promise.resolve();

    if (this.shouldFailNextRequest) {
      this.shouldFailNextRequest = false;
      throw new Error('Nie udało się pobrać listy procesów.');
    }

    const normalizedQuery = this.normalizeQuery(query);
    const filteredItems = this.applyFilters(MOCK_PROCESS_LIST_ITEMS, normalizedQuery);
    const sortedItems = this.applySorting(filteredItems, normalizedQuery.sort, normalizedQuery.order);
    const total = sortedItems.length;
    const totalPages = total === 0 ? 0 : Math.ceil(total / normalizedQuery.size);
    const page = totalPages === 0 ? 1 : Math.min(normalizedQuery.page, totalPages);
    const startIndex = (page - 1) * normalizedQuery.size;
    const items = sortedItems.slice(startIndex, startIndex + normalizedQuery.size);

    return {
      items,
      page,
      size: normalizedQuery.size,
      total,
      totalPages,
      sort: normalizedQuery.sort,
      order: normalizedQuery.order,
      filters: {
        status: normalizedQuery.status,
        nip: normalizedQuery.nip,
        phNumber: normalizedQuery.phNumber,
        dateFrom: normalizedQuery.dateFrom,
        dateTo: normalizedQuery.dateTo,
        observed: normalizedQuery.observed,
      },
    };
  }

  private normalizeQuery(query: Partial<ProcessListQuery>): ProcessListQuery {
    const defaults = buildDefaultProcessListQuery();

    const page = Number(query.page);
    const size = Number(query.size);

    return {
      page: Number.isFinite(page) && page > 0 ? Math.floor(page) : defaults.page,
      size: Number.isFinite(size) ? this.clamp(Math.floor(size), 1, 100) : defaults.size,
      sort:
        typeof query.sort === 'string' && isProcessSortField(query.sort)
          ? query.sort
          : defaults.sort,
      order: query.order === 'asc' ? 'asc' : 'desc',
      status: this.normalizeStatus(query.status, defaults.status),
      nip: typeof query.nip === 'string' ? query.nip.trim() : defaults.nip,
      phNumber: typeof query.phNumber === 'string' ? query.phNumber.trim() : defaults.phNumber,
      dateFrom: typeof query.dateFrom === 'string' ? query.dateFrom.trim() : defaults.dateFrom,
      dateTo: typeof query.dateTo === 'string' ? query.dateTo.trim() : defaults.dateTo,
      observed: query.observed === 'isObserved' ? 'isObserved' : defaults.observed,
    };
  }

  private normalizeStatus(
    value: ProcessListQuery['status'] | undefined,
    defaultStatus: ProcessStatus,
  ): ProcessStatus | '' {
    if (value === '') {
      return '';
    }

    if (typeof value === 'string' && isProcessStatus(value)) {
      return value;
    }

    return defaultStatus;
  }

  private applyFilters(items: ProcessListItem[], query: ProcessListQuery): ProcessListItem[] {
    const parsedDateFrom = parseProcessDate(query.dateFrom);
    const parsedDateTo = parseProcessDate(query.dateTo);
    const exclusiveDateTo = parsedDateTo ? this.addDays(parsedDateTo, 1) : null;
    const shouldFilterByNip = /^\d+$/.test(query.nip);

    return items.filter((item) => {
      if (query.status && item.status !== query.status) {
        return false;
      }

      if (shouldFilterByNip && item.nip !== query.nip) {
        return false;
      }

      if (query.phNumber && item.phNumber !== query.phNumber) {
        return false;
      }

      const itemLastUpdated = parseProcessDate(item.lastUpdated);

      if (parsedDateFrom && itemLastUpdated && itemLastUpdated < parsedDateFrom) {
        return false;
      }

      if (exclusiveDateTo && itemLastUpdated && itemLastUpdated >= exclusiveDateTo) {
        return false;
      }

      if (query.observed === 'isObserved' && !item.observed) {
        return false;
      }

      return true;
    });
  }

  private applySorting(
    items: ProcessListItem[],
    sortField: ProcessSortField,
    order: 'asc' | 'desc',
  ): ProcessListItem[] {
    const sorted = [...items].sort((left, right) => {
      const leftValue = this.getSortValue(left, sortField);
      const rightValue = this.getSortValue(right, sortField);

      let comparison = 0;

      if (typeof leftValue === 'number' && typeof rightValue === 'number') {
        comparison = leftValue - rightValue;
      } else {
        comparison = String(leftValue).localeCompare(String(rightValue), 'pl');
      }

      if (comparison === 0) {
        comparison = left.id - right.id;
      }

      return order === 'asc' ? comparison : -comparison;
    });

    return sorted;
  }

  private getSortValue(item: ProcessListItem, field: ProcessSortField): number | string {
    switch (field) {
      case 'id':
        return item.id;
      case 'phNumber':
        return item.phNumber;
      case 'nip':
        return item.nip;
      case 'status':
        return item.status;
      case 'lastUpdated':
        return parseProcessDate(item.lastUpdated)?.getTime() ?? 0;
      case 'dateCreated':
        return parseProcessDate(item.dateCreated)?.getTime() ?? 0;
    }
  }

  private addDays(date: Date, days: number): Date {
    const shifted = new Date(date);
    shifted.setHours(12, 0, 0, 0);
    shifted.setDate(shifted.getDate() + days);

    return shifted;
  }

  private clamp(value: number, min: number, max: number): number {
    return Math.min(Math.max(value, min), max);
  }
}
