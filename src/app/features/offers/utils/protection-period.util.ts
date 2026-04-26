const PROTECTION_PERIOD_FALLBACK = '—';

export function formatProtectionPeriod(referenceDate: Date): string {
  if (!isValidDate(referenceDate)) {
    return PROTECTION_PERIOD_FALLBACK;
  }

  const startDate = new Date(
    referenceDate.getFullYear(),
    referenceDate.getMonth(),
    referenceDate.getDate(),
    0,
    0,
    0,
    0
  );

  const endDate = buildEndDate(startDate);

  if (!isValidDate(startDate) || !isValidDate(endDate)) {
    return PROTECTION_PERIOD_FALLBACK;
  }

  return `${formatLocalDate(startDate)} – ${formatLocalDate(endDate)}`;
}

function buildEndDate(startDate: Date): Date {
  const targetYear = startDate.getFullYear() + 1;
  const targetMonth = startDate.getMonth();
  const targetDay = Math.min(startDate.getDate(), getDaysInMonth(targetYear, targetMonth));

  return new Date(targetYear, targetMonth, targetDay, 23, 59, 0, 0);
}

function getDaysInMonth(year: number, monthIndex: number): number {
  return new Date(year, monthIndex + 1, 0).getDate();
}

function formatLocalDate(date: Date): string {
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, '0');
  const day = `${date.getDate()}`.padStart(2, '0');

  return `${year}-${month}-${day}`;
}

function isValidDate(value: Date): boolean {
  return value instanceof Date && !Number.isNaN(value.getTime());
}
