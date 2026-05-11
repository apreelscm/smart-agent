export const PROCESS_STATUSES = [
  'NEW',
  'REJECTED',
  'WAIT_FOR_SUBSCRIPTION',
  'WAIT_FOR_SUBSCRIPTION_PAPER_VERSION',
  'SUBSCRIPTIONS_DONE',
  'WAITING',
  'CORRECTION',
  'ACCEPTED',
] as const;

export type ProcessStatus = (typeof PROCESS_STATUSES)[number];

export type ProcessDocumentsFormat = 'ELECTRONIC' | 'PAPER' | 'TEMPLATE';
export type ProcessObservedFilter = '' | 'isObserved';
export type SortDirection = 'asc' | 'desc';
export type ProcessSortField = 'id' | 'phNumber' | 'nip' | 'status' | 'lastUpdated' | 'dateCreated';

export interface ProcessListItem {
  id: number;
  phNumber: string;
  nip: string;
  clientName: string;
  activity: string;
  salesSegment: string;
  phEmail: string;
  phName: string;
  status: ProcessStatus;
  lastUpdated: string;
  dateCreated: string;
  documentsFormat: ProcessDocumentsFormat;
  notesFromCoa: string;
  notesFromZrd: string;
  observed: boolean;
}

export interface ProcessListFilters {
  status: ProcessStatus | '';
  nip: string;
  phNumber: string;
  dateFrom: string;
  dateTo: string;
  observed: ProcessObservedFilter;
}

export interface ProcessListQuery extends ProcessListFilters {
  page: number;
  size: number;
  sort: ProcessSortField;
  order: SortDirection;
}

export interface ProcessListResponse {
  items: ProcessListItem[];
  page: number;
  size: number;
  total: number;
  totalPages: number;
  sort: ProcessSortField;
  order: SortDirection;
  filters: ProcessListFilters;
}

export interface ProcessStatusOption {
  value: ProcessStatus;
  label: string;
}

export const PROCESS_STATUS_LABELS: Record<ProcessStatus, string> = {
  NEW: 'Edycja',
  REJECTED: 'Odrzucony',
  WAIT_FOR_SUBSCRIPTION: 'Oczekiwanie na podpis',
  WAIT_FOR_SUBSCRIPTION_PAPER_VERSION: 'Oczekiwanie na podpis papierowy',
  SUBSCRIPTIONS_DONE: 'Złożono podpisy',
  WAITING: 'Oczekujący',
  CORRECTION: 'Przekazano do korekty',
  ACCEPTED: 'Zaakceptowany',
};

export const PROCESS_STATUS_OPTIONS: ReadonlyArray<ProcessStatusOption> = PROCESS_STATUSES.map(
  (status) => ({
    value: status,
    label: PROCESS_STATUS_LABELS[status],
  }),
);

export const PROCESS_PAGE_SIZE_OPTIONS = [10, 50, 100] as const;

export function formatProcessDate(date: Date): string {
  const normalized = new Date(date);
  normalized.setHours(12, 0, 0, 0);

  const day = String(normalized.getDate()).padStart(2, '0');
  const month = String(normalized.getMonth() + 1).padStart(2, '0');
  const year = normalized.getFullYear();

  return `${day}.${month}.${year}`;
}

export function parseProcessDate(value: string): Date | null {
  const trimmedValue = value.trim();
  const match = /^(\d{2})\.(\d{2})\.(\d{4})$/.exec(trimmedValue);

  if (!match) {
    return null;
  }

  const day = Number(match[1]);
  const month = Number(match[2]) - 1;
  const year = Number(match[3]);

  const parsed = new Date(year, month, day, 12, 0, 0, 0);

  if (
    parsed.getFullYear() !== year ||
    parsed.getMonth() !== month ||
    parsed.getDate() !== day
  ) {
    return null;
  }

  return parsed;
}

export function buildDefaultProcessListFilters(referenceDate: Date = new Date()): ProcessListFilters {
  return {
    status: 'WAITING',
    nip: '',
    phNumber: '',
    dateFrom: formatProcessDate(shiftDate(referenceDate, -30)),
    dateTo: formatProcessDate(referenceDate),
    observed: '',
  };
}

export function buildDefaultProcessListQuery(referenceDate: Date = new Date()): ProcessListQuery {
  return {
    ...buildDefaultProcessListFilters(referenceDate),
    page: 1,
    size: 10,
    sort: 'id',
    order: 'desc',
  };
}

export function isProcessStatus(value: string): value is ProcessStatus {
  return (PROCESS_STATUSES as readonly string[]).includes(value);
}

export function isProcessSortField(value: string): value is ProcessSortField {
  return ['id', 'phNumber', 'nip', 'status', 'lastUpdated', 'dateCreated'].includes(value);
}

function shiftDate(referenceDate: Date, days: number): Date {
  const shifted = new Date(referenceDate);
  shifted.setHours(12, 0, 0, 0);
  shifted.setDate(shifted.getDate() + days);

  return shifted;
}
