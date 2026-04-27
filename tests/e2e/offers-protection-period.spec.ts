import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

type CalendarDateParts = {
  year: number;
  month: number;
  day: number;
};

type DateTimeParts = CalendarDateParts & {
  hour: number;
  minute: number;
};

const CET_TIME_ZONE = 'Europe/Warsaw';
const PROTECTION_PERIOD_PATTERN = /^\d{4}\/\d{2}\/\d{2} \d{2}:\d{2} - \d{4}\/\d{2}\/\d{2} \d{2}:\d{2}$/;

test('offers list shows the current protection period for every visible offer', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-open');

  const rows = page.locator('article.offer-row');
  const rowCount = await rows.count();

  expect(rowCount).toBeGreaterThan(0);
  await expect(rows.first().getByText('Okres ochrony')).toBeVisible();
  await captureStep(page, testInfo, 'protection-period-label-visible');

  const expectedProtectionPeriod = formatExpectedProtectionPeriod();
  const protectionPeriodValues = page.locator('.offer-row__protection-period');

  await expect(protectionPeriodValues.first()).toHaveText(expectedProtectionPeriod);

  const visibleValues = (await protectionPeriodValues.allTextContents()).map((value) =>
    value.replace(/\s+/g, ' ').trim(),
  );

  expect(visibleValues.length).toBe(rowCount);

  for (const value of visibleValues) {
    expect(value).toMatch(PROTECTION_PERIOD_PATTERN);
    expect(value).toBe(expectedProtectionPeriod);
  }

  await captureStep(page, testInfo, 'protection-period-values-visible');
});

function formatExpectedProtectionPeriod(referenceDate: Date = new Date()): string {
  const currentCetDay = getCalendarDateInTimeZone(referenceDate, CET_TIME_ZONE);
  const protectionStart: DateTimeParts = {
    ...currentCetDay,
    hour: 0,
    minute: 0,
  };
  const protectionEnd = getProtectionPeriodEnd(protectionStart);

  return `${formatDateTimeParts(protectionStart)} - ${formatDateTimeParts(protectionEnd)}`;
}

function getCalendarDateInTimeZone(referenceDate: Date, timeZone: string): CalendarDateParts {
  const formatter = new Intl.DateTimeFormat('en-CA', {
    timeZone,
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
  const parts = formatter.formatToParts(referenceDate);

  return {
    year: getNumericPart(parts, 'year'),
    month: getNumericPart(parts, 'month'),
    day: getNumericPart(parts, 'day'),
  };
}

function getProtectionPeriodEnd(start: DateTimeParts): DateTimeParts {
  const anniversaryUtc = new Date(
    Date.UTC(start.year + 1, start.month - 1, start.day, start.hour, start.minute),
  );

  anniversaryUtc.setUTCMinutes(anniversaryUtc.getUTCMinutes() - 1);

  return {
    year: anniversaryUtc.getUTCFullYear(),
    month: anniversaryUtc.getUTCMonth() + 1,
    day: anniversaryUtc.getUTCDate(),
    hour: anniversaryUtc.getUTCHours(),
    minute: anniversaryUtc.getUTCMinutes(),
  };
}

function getNumericPart(
  parts: Intl.DateTimeFormatPart[],
  type: 'year' | 'month' | 'day',
): number {
  return Number(parts.find((part) => part.type === type)?.value ?? 0);
}

function formatDateTimeParts(value: DateTimeParts): string {
  return `${pad(value.year, 4)}/${pad(value.month)}/${pad(value.day)} ${pad(value.hour)}:${pad(value.minute)}`;
}

function pad(value: number, width = 2): string {
  return value.toString().padStart(width, '0');
}
