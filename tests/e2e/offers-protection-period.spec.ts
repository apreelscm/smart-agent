import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const buildExpectedProtectionPeriod = (year: number, month: number, day: number) => {
  const start = `${year}/${String(month + 1).padStart(2, '0')}/${String(day).padStart(2, '0')}`;
  const endYear = year + 1;
  const lastDayOfTargetMonth = new Date(endYear, month + 1, 0).getDate();
  const endDay = Math.min(day, lastDayOfTargetMonth);
  const end = `${endYear}/${String(month + 1).padStart(2, '0')}/${String(endDay).padStart(2, '0')}`;

  return `${start} - ${end}`;
};

test('offers list shows the same protection period on every offer card', async ({ page }, testInfo) => {
  await page.goto('/');
  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();

  const offerCount = await offerRows.count();
  expect(offerCount).toBeGreaterThan(0);

  const protectionLabels = page.locator('.offer-row__meta-label').filter({
    hasText: 'Okres ochrony',
  });
  await expect(protectionLabels.first()).toBeVisible();
  await expect(protectionLabels).toHaveCount(offerCount);

  const browserDate = await page.evaluate(() => {
    const now = new Date();

    return {
      year: now.getFullYear(),
      month: now.getMonth(),
      day: now.getDate(),
    };
  });
  const expectedProtectionPeriod = buildExpectedProtectionPeriod(
    browserDate.year,
    browserDate.month,
    browserDate.day,
  );

  const protectionPeriods = page.locator('.offer-row__protection-period');
  await expect(protectionPeriods).toHaveCount(offerCount);

  for (let index = 0; index < offerCount; index += 1) {
    await expect(protectionPeriods.nth(index)).toHaveText(expectedProtectionPeriod);
  }

  await captureStep(page, testInfo, 'protection-period-rendered');
});

test('search filtering keeps the protection period visible for the remaining offer', async ({ page }, testInfo) => {
  await page.goto('/');
  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();

  const firstOfferNumber = await page.locator('.offer-row__number span').first().textContent();
  if (!firstOfferNumber?.trim()) {
    throw new Error('Expected at least one offer number on the offers list.');
  }

  await captureStep(page, testInfo, 'offers-before-filtering');

  const searchInput = page.locator('input[placeholder*="Szukaj"]');
  await expect(searchInput).toBeVisible();
  await searchInput.fill(firstOfferNumber.trim());

  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść wszystkie filtry' });
  await expect(clearFiltersButton).toBeEnabled();
  await expect(page.locator('.offer-row')).toHaveCount(1);
  await expect(page.locator('.offer-row__number span').first()).toHaveText(firstOfferNumber.trim());
  await expect(page.getByText('1 ofert(y) na liście', { exact: true })).toBeVisible();

  const browserDate = await page.evaluate(() => {
    const now = new Date();

    return {
      year: now.getFullYear(),
      month: now.getMonth(),
      day: now.getDate(),
    };
  });
  const expectedProtectionPeriod = buildExpectedProtectionPeriod(
    browserDate.year,
    browserDate.month,
    browserDate.day,
  );

  await expect(page.locator('.offer-row__protection-period').first()).toHaveText(
    expectedProtectionPeriod,
  );
  await captureStep(page, testInfo, 'offers-filtered-with-protection-period');
});
