type CalendarDateParts = {
  year: number;
  month: number;
  day: number;
};

type DateTimeParts = CalendarDateParts & {
  hour: number;
  minute: number;
};

const OFFER_PROTECTION_PERIOD_TIME_ZONE = 'Europe/Warsaw';

export function formatOfferProtectionPeriod(referenceDate: Date = new Date()): string {
  const currentCetDay = getCalendarDateInTimeZone(referenceDate, OFFER_PROTECTION_PERIOD_TIME_ZONE);
  const protectionStart: DateTimeParts = {
    ...currentCetDay,
    hour: 0,
    minute: 0
  };
  const protectionEnd = getProtectionPeriodEnd(protectionStart);

  return `${formatDateTimeParts(protectionStart)} - ${formatDateTimeParts(protectionEnd)}`;
}

function getCalendarDateInTimeZone(referenceDate: Date, timeZone: string): CalendarDateParts {
  const formatter = new Intl.DateTimeFormat('en-CA', {
    timeZone,
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });

  const parts = formatter.formatToParts(referenceDate);
  const year = getNumericPart(parts, 'year');
  const month = getNumericPart(parts, 'month');
  const day = getNumericPart(parts, 'day');

  return { year, month, day };
}

function getProtectionPeriodEnd(start: DateTimeParts): DateTimeParts {
  const anniversaryUtc = new Date(Date.UTC(start.year + 1, start.month - 1, start.day, start.hour, start.minute));
  anniversaryUtc.setUTCMinutes(anniversaryUtc.getUTCMinutes() - 1);

  return {
    year: anniversaryUtc.getUTCFullYear(),
    month: anniversaryUtc.getUTCMonth() + 1,
    day: anniversaryUtc.getUTCDate(),
    hour: anniversaryUtc.getUTCHours(),
    minute: anniversaryUtc.getUTCMinutes()
  };
}

function getNumericPart(parts: Intl.DateTimeFormatPart[], type: 'year' | 'month' | 'day'): number {
  return Number(parts.find((part) => part.type === type)?.value ?? 0);
}

function formatDateTimeParts(value: DateTimeParts): string {
  return `${pad(value.year, 4)}/${pad(value.month)}/${pad(value.day)} ${pad(value.hour)}:${pad(value.minute)}`;
}

function pad(value: number, width = 2): string {
  return value.toString().padStart(width, '0');
}
