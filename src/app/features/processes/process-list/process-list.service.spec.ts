import { MOCK_PROCESS_LIST_ITEMS } from './process-list.mock';
import {
  buildDefaultProcessListQuery,
  parseProcessDate,
} from './process-list.models';
import { ProcessListService } from './process-list.service';

describe('ProcessListService', () => {
  let service: ProcessListService;

  beforeEach(() => {
    service = new ProcessListService();
  });

  it('returns the default WAITING view sorted by id descending and paginated', async () => {
    const response = await service.getProcesses();

    expect(response.page).toBe(1);
    expect(response.size).toBe(10);
    expect(response.sort).toBe('id');
    expect(response.order).toBe('desc');
    expect(response.items.length).toBe(10);
    expect(response.total).toBeGreaterThan(10);
    expect(response.items.every((item) => item.status === 'WAITING')).toBeTrue();

    const ids = response.items.map((item) => item.id);
    const sortedIds = [...ids].sort((left, right) => right - left);
    expect(ids).toEqual(sortedIds);

    const defaults = buildDefaultProcessListQuery();
    const dateFrom = parseProcessDate(defaults.dateFrom);
    const dateToExclusive = parseProcessDate(defaults.dateTo);
    dateToExclusive?.setDate(dateToExclusive.getDate() + 1);

    expect(
      response.items.every((item) => {
        const lastUpdated = parseProcessDate(item.lastUpdated);
        return Boolean(
          lastUpdated &&
            dateFrom &&
            dateToExclusive &&
            lastUpdated >= dateFrom &&
            lastUpdated < dateToExclusive,
        );
      }),
    ).toBeTrue();
  });

  it('applies active filters with AND semantics', async () => {
    const response = await service.getProcesses({
      status: 'WAITING',
      nip: '1234567890',
      phNumber: 'PH/2026/00112',
      observed: 'isObserved',
      page: 1,
      size: 10,
      sort: 'id',
      order: 'desc',
      dateFrom: buildDefaultProcessListQuery().dateFrom,
      dateTo: buildDefaultProcessListQuery().dateTo,
    });

    expect(response.total).toBe(1);
    expect(response.items).toEqual([
      jasmine.objectContaining({
        id: 1012,
        nip: '1234567890',
        phNumber: 'PH/2026/00112',
        observed: true,
        status: 'WAITING',
      }),
    ]);
  });

  it('sorts by last updated in ascending order', async () => {
    const response = await service.getProcesses({
      status: '',
      dateFrom: '',
      dateTo: '',
      page: 1,
      size: 100,
      sort: 'lastUpdated',
      order: 'asc',
    });

    const timestamps = response.items.map(
      (item) => parseProcessDate(item.lastUpdated)?.getTime() ?? 0,
    );
    const sortedTimestamps = [...timestamps].sort((left, right) => left - right);

    expect(timestamps).toEqual(sortedTimestamps);
  });

  it('returns the requested page slice while preserving query context', async () => {
    const firstPage = await service.getProcesses({
      page: 1,
      size: 10,
    });
    const secondPage = await service.getProcesses({
      page: 2,
      size: 10,
    });

    expect(firstPage.total).toBe(secondPage.total);
    expect(secondPage.page).toBe(2);
    expect(secondPage.items.length).toBeGreaterThan(0);
    expect(secondPage.items[0].id).not.toBe(firstPage.items[0].id);
    expect(secondPage.items.map((item) => item.id)).toEqual([1008, 1007, 1006, 1005]);
  });

  it('returns an empty result when no items match filters', async () => {
    const response = await service.getProcesses({
      phNumber: 'PH/2099/99999',
    });

    expect(response.items).toEqual([]);
    expect(response.total).toBe(0);
    expect(response.totalPages).toBe(0);
    expect(response.page).toBe(1);
  });

  it('can fail once for error handling scenarios', async () => {
    service.failNextRequest();

    await expectAsync(service.getProcesses()).toBeRejectedWithError(
      Error,
      'Nie udało się pobrać listy procesów.',
    );

    const response = await service.getProcesses({
      status: '',
      dateFrom: '',
      dateTo: '',
      size: 100,
    });

    expect(response.total).toBe(MOCK_PROCESS_LIST_ITEMS.length);
  });
});
