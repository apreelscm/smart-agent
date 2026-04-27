import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows the current protection period in every visible row', async ({
  page,
}, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const expectedProtectionPeriod = getExpectedProtectionPeriod(
    new Date(await page.evaluate(() => Date.now())),
  );
  const metaGrids = page.locator('.offer-row__meta-grid');

  await expect(page.getByText('Okres ochrony').first()).toBeVisible();
  await expect(metaGrids.first()).toContainText(expectedProtectionPeriod);
  await captureStep(page, testInfo, 'protection-period-visible-on-first-row');

  const metaGridTexts = await metaGrids.allTextContents();
  expect(metaGridTexts.length).toBeGreaterThan(0);

  for (const metaGridText of metaGridTexts) {
    expect(metaGridText).toContain('Okres ochrony');
    expect(metaGridText).toContain(expectedProtectionPeriod);
  }

  const firstRowLabels = (await offerRows.first().locator('.offer-row__meta-label').allTextContents())
    .map((label) => label.trim())
    .filter((label) => label.length > 0);

  expect(firstRowLabels).toContain('Okres ochrony');
  expect(firstRowLabels).toContain('Wariant');
  expect(firstRowLabels.indexOf('Okres ochrony')).toBeLessThan(
    firstRowLabels.indexOf('Wariant'),
  );
  await captureStep(page, testInfo, 'protection-period-in-meta-layout');
});

function getExpectedProtectionPeriod(startDate: Date): string {
  const endDate = addOneCalendarYear(startDate);

  return `${formatDate(startDate)} - ${formatDate(endDate)}`;
}

function addOneCalendarYear(date: Date): Date {
  const nextYearDate = new Date(date);
  nextYearDate.setFullYear(nextYearDate.getFullYear() + 1);

  if (nextYearDate.getMonth() !== date.getMonth()) {
    nextYearDate.setDate(0);
  }

  return nextYearDate;
}

function formatDate(date: Date): string {
  const year = `${date.getFullYear()}`;
  const month = `${date.getMonth() + 1}`.padStart(2, '0');
  const day = `${date.getDate()}`.padStart(2, '0');

  return `${year}/${month}/${day}`;
}
