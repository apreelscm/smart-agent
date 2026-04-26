const COVERAGE_PERIOD_TIME_ZONE = 'Europe/Warsaw';

type BusinessDateParts = {
  year: number;
  month: number;
  day: number;
};

export function getCoveragePeriodLabel(now: Date = new Date()): string {
  const businessDate = getBusinessDate(now);
  const yearlyAnniversary = new Date(businessDate);
  yearlyAnniversary.setUTCFullYear(yearlyAnniversary.getUTCFullYear() + 1);

  const coverageEndDate = new Date(yearlyAnniversary);
  coverageEndDate.setUTCDate(coverageEndDate.getUTCDate() - 1);

  return `${formatDateTime(businessDate, '00', '00')} - ${formatDateTime(coverageEndDate, '23', '59')}`;
}

function getBusinessDate(now: Date): Date {
  const { year, month, day } = getBusinessDateParts(now);
  return new Date(Date.UTC(year, month - 1, day));
}

function getBusinessDateParts(now: Date): BusinessDateParts {
  const parts = new Intl.DateTimeFormat('en-CA', {
    timeZone: COVERAGE_PERIOD_TIME_ZONE,
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).formatToParts(now);

  return {
    year: getNumericPart(parts, 'year'),
    month: getNumericPart(parts, 'month'),
    day: getNumericPart(parts, 'day')
  };
}

function getNumericPart(parts: Intl.DateTimeFormatPart[], type: 'year' | 'month' | 'day'): number {
  const value = parts.find((part) => part.type === type)?.value;

  if (!value) {
    throw new Error(`Missing ${type} part for coverage period calculation.`);
  }

  return Number(value);
}

function formatDateTime(date: Date, hours: string, minutes: string): string {
  const year = date.getUTCFullYear();
  const month = padTwoDigits(date.getUTCMonth() + 1);
  const day = padTwoDigits(date.getUTCDate());

  return `${year}/${month}/${day} ${hours}:${minutes}`;
}

function padTwoDigits(value: number): string {
  return value.toString().padStart(2, '0');
}
