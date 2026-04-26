export const POLAND_BUSINESS_TIME_ZONE = 'Europe/Warsaw';

export type BusinessDate = {
  year: number;
  month: number;
  day: number;
};

export type CoveragePeriodBoundary = BusinessDate & {
  hour: number;
  minute: number;
};

export type CoveragePeriod = {
  start: CoveragePeriodBoundary;
  end: CoveragePeriodBoundary;
};

const businessDateFormatters = new Map<string, Intl.DateTimeFormat>();

export function getBusinessDateInTimeZone(now: Date, timeZone = POLAND_BUSINESS_TIME_ZONE): BusinessDate {
  const formatter = getBusinessDateFormatter(timeZone);
  const parts = formatter.formatToParts(now);

  const year = Number(parts.find((part) => part.type === 'year')?.value);
  const month = Number(parts.find((part) => part.type === 'month')?.value);
  const day = Number(parts.find((part) => part.type === 'day')?.value);

  if ([year, month, day].some((value) => Number.isNaN(value))) {
    throw new Error(`Unable to derive business date for timezone "${timeZone}".`);
  }

  return { year, month, day };
}

export function calculateCoveragePeriod(businessDate: BusinessDate): CoveragePeriod {
  const coverageEndDate = getOneYearMinusOneDay(businessDate);

  return {
    start: {
      ...businessDate,
      hour: 0,
      minute: 0
    },
    end: {
      ...coverageEndDate,
      hour: 23,
      minute: 59
    }
  };
}

export function formatCoveragePeriod(period: CoveragePeriod): string {
  return `${formatCoverageBoundary(period.start)} - ${formatCoverageBoundary(period.end)}`;
}

export function getCoveragePeriodLabel(now = new Date(), timeZone = POLAND_BUSINESS_TIME_ZONE): string {
  return formatCoveragePeriod(calculateCoveragePeriod(getBusinessDateInTimeZone(now, timeZone)));
}

function getBusinessDateFormatter(timeZone: string): Intl.DateTimeFormat {
  let formatter = businessDateFormatters.get(timeZone);

  if (!formatter) {
    formatter = new Intl.DateTimeFormat('en-CA', {
      timeZone,
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
    businessDateFormatters.set(timeZone, formatter);
  }

  return formatter;
}

function getOneYearMinusOneDay(date: BusinessDate): BusinessDate {
  const oneYearLater = new Date(Date.UTC(date.year + 1, date.month - 1, date.day));
  oneYearLater.setUTCDate(oneYearLater.getUTCDate() - 1);

  return {
    year: oneYearLater.getUTCFullYear(),
    month: oneYearLater.getUTCMonth() + 1,
    day: oneYearLater.getUTCDate()
  };
}

function formatCoverageBoundary(boundary: CoveragePeriodBoundary): string {
  return `${boundary.year}/${padTwoDigits(boundary.month)}/${padTwoDigits(boundary.day)} ${padTwoDigits(boundary.hour)}:${padTwoDigits(boundary.minute)}`;
}

function padTwoDigits(value: number): string {
  return `${value}`.padStart(2, '0');
}
