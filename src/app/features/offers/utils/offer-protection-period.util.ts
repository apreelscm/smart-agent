const BUSINESS_TIME_ZONE = 'Europe/Warsaw';

type BusinessCalendarDate = {
  year: number;
  month: number;
  day: number;
};

type BusinessDateTime = BusinessCalendarDate & {
  hour: number;
  minute: number;
  second: number;
};

const businessDateTimeFormatter = new Intl.DateTimeFormat('en-CA', {
  timeZone: BUSINESS_TIME_ZONE,
  year: 'numeric',
  month: '2-digit',
  day: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
  second: '2-digit',
  hourCycle: 'h23'
});

const businessOffsetFormatter = new Intl.DateTimeFormat('en-US', {
  timeZone: BUSINESS_TIME_ZONE,
  timeZoneName: 'shortOffset',
  hour: '2-digit',
  minute: '2-digit',
  second: '2-digit',
  hourCycle: 'h23'
});

export function buildOfferProtectionPeriodLabel(now: Date = new Date()): string {
  const startDate = getBusinessCalendarDate(now);
  const endDate = addOneYearThenSubtractOneDay(startDate);

  return `${formatBusinessDateTime(startDate, '00:00')} - ${formatBusinessDateTime(endDate, '23:59')}`;
}

export function getMillisecondsUntilNextOfferProtectionPeriodRefresh(now: Date = new Date()): number {
  const currentBusinessDate = getBusinessCalendarDate(now);
  const nextBusinessDateUtc = new Date(
    Date.UTC(currentBusinessDate.year, currentBusinessDate.month - 1, currentBusinessDate.day)
  );
  nextBusinessDateUtc.setUTCDate(nextBusinessDateUtc.getUTCDate() + 1);

  const nextBusinessMidnightTimestamp = businessDateTimeToUtcTimestamp({
    year: nextBusinessDateUtc.getUTCFullYear(),
    month: nextBusinessDateUtc.getUTCMonth() + 1,
    day: nextBusinessDateUtc.getUTCDate(),
    hour: 0,
    minute: 0,
    second: 0
  });

  return Math.max(1, nextBusinessMidnightTimestamp - now.getTime());
}

function getBusinessCalendarDate(date: Date): BusinessCalendarDate {
  const parts = getBusinessDateTimeParts(date);

  return {
    year: parts.year,
    month: parts.month,
    day: parts.day
  };
}

function getBusinessDateTimeParts(date: Date): BusinessDateTime {
  const parts = businessDateTimeFormatter.formatToParts(date);

  return {
    year: getNumericPart(parts, 'year'),
    month: getNumericPart(parts, 'month'),
    day: getNumericPart(parts, 'day'),
    hour: getNumericPart(parts, 'hour'),
    minute: getNumericPart(parts, 'minute'),
    second: getNumericPart(parts, 'second')
  };
}

function getNumericPart(parts: Intl.DateTimeFormatPart[], type: Intl.DateTimeFormatPartTypes): number {
  return Number(parts.find((part) => part.type === type)?.value ?? 0);
}

function addOneYearThenSubtractOneDay(date: BusinessCalendarDate): BusinessCalendarDate {
  const anniversaryUtc = new Date(Date.UTC(date.year, date.month - 1, date.day));
  anniversaryUtc.setUTCFullYear(anniversaryUtc.getUTCFullYear() + 1);
  anniversaryUtc.setUTCDate(anniversaryUtc.getUTCDate() - 1);

  return {
    year: anniversaryUtc.getUTCFullYear(),
    month: anniversaryUtc.getUTCMonth() + 1,
    day: anniversaryUtc.getUTCDate()
  };
}

function formatBusinessDateTime(date: BusinessCalendarDate, time: string): string {
  return `${date.year}/${pad(date.month)}/${pad(date.day)} ${time}`;
}

function pad(value: number): string {
  return value.toString().padStart(2, '0');
}

function businessDateTimeToUtcTimestamp(dateTime: BusinessDateTime): number {
  const naiveTimestamp = Date.UTC(
    dateTime.year,
    dateTime.month - 1,
    dateTime.day,
    dateTime.hour,
    dateTime.minute,
    dateTime.second
  );

  let resolvedTimestamp = naiveTimestamp;

  for (let attempt = 0; attempt < 3; attempt += 1) {
    const offsetMinutes = getBusinessOffsetMinutes(new Date(resolvedTimestamp));
    const adjustedTimestamp = naiveTimestamp - offsetMinutes * 60_000;

    if (adjustedTimestamp === resolvedTimestamp) {
      break;
    }

    resolvedTimestamp = adjustedTimestamp;
  }

  return resolvedTimestamp;
}

function getBusinessOffsetMinutes(date: Date): number {
  const timeZoneName =
    businessOffsetFormatter.formatToParts(date).find((part) => part.type === 'timeZoneName')?.value ?? 'GMT';

  if (timeZoneName === 'GMT' || timeZoneName === 'UTC') {
    return 0;
  }

  const match = timeZoneName.match(/^(?:GMT|UTC)([+-])(\d{1,2})(?::?(\d{2}))?$/);

  if (!match) {
    return 0;
  }

  const [, sign, hours, minutes] = match;
  const totalMinutes = Number(hours) * 60 + Number(minutes ?? '0');

  return sign === '-' ? -totalMinutes : totalMinutes;
}
