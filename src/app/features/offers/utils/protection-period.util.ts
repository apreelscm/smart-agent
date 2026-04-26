export const WARSAW_BUSINESS_TIME_ZONE = 'Europe/Warsaw';

type CivilDate = {
  year: number;
  month: number;
  day: number;
};

function getCivilDate(referenceDate: Date, timeZone: string): CivilDate {
  const parts = new Intl.DateTimeFormat('en-CA', {
    timeZone,
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).formatToParts(referenceDate);

  const year = Number(parts.find((part) => part.type === 'year')?.value);
  const month = Number(parts.find((part) => part.type === 'month')?.value);
  const day = Number(parts.find((part) => part.type === 'day')?.value);

  if ([year, month, day].some((value) => Number.isNaN(value))) {
    throw new Error('Unable to derive civil date for protection period.');
  }

  return { year, month, day };
}

function addOneYearMinusOneDay(date: CivilDate): CivilDate {
  const nextYearDate = new Date(Date.UTC(date.year + 1, date.month - 1, date.day));
  nextYearDate.setUTCDate(nextYearDate.getUTCDate() - 1);

  return {
    year: nextYearDate.getUTCFullYear(),
    month: nextYearDate.getUTCMonth() + 1,
    day: nextYearDate.getUTCDate()
  };
}

function pad(value: number): string {
  return value.toString().padStart(2, '0');
}

function formatCivilDateTime(date: CivilDate, time: string): string {
  return `${date.year}/${pad(date.month)}/${pad(date.day)} ${time}`;
}

export function formatProtectionPeriod(referenceDate: Date = new Date(), timeZone: string = WARSAW_BUSINESS_TIME_ZONE): string {
  const startDate = getCivilDate(referenceDate, timeZone);
  const endDate = addOneYearMinusOneDay(startDate);

  return `${formatCivilDateTime(startDate, '00:00')} - ${formatCivilDateTime(endDate, '23:59')}`;
}
