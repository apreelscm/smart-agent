import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

function formatProtectionPeriodDate(date: Date): string {
  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, '0');
  const day = `${date.getDate()}`.padStart(2, '0');

  return `${year}/${month}/${day}`;
}

function buildExpectedProtectionPeriod(referenceDate: Date): string {
  const startDate = new Date(
    referenceDate.getFullYear(),
    referenceDate.getMonth(),
    referenceDate.getDate(),
  );
  const endDate = new Date(
    startDate.getFullYear() + 1,
    startDate.getMonth(),
    startDate.getDate(),
  );

  if (
    endDate.getMonth() !== startDate.getMonth() ||
    endDate.getDate() !== startDate.getDate()
  ) {
    endDate.setDate(0);
  }

  return `${formatProtectionPeriodDate(startDate)} - ${formatProtectionPeriodDate(endDate)}`;
}

test('shows the protection period before the variant on the offers list', async (
  { page },
  testInfo,
) => {
  await page.goto('/');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.getByText('Portfel ofert')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const firstOfferRow = page.locator('.offer-row').first();
  await expect(firstOfferRow).toBeVisible();
  await captureStep(page, testInfo, 'first-offer-row-visible');

  const browserToday = await page.evaluate(() => ({
    year: new Date().getFullYear(),
    month: new Date().getMonth(),
    day: new Date().getDate(),
  }));
  const expectedProtectionPeriod = buildExpectedProtectionPeriod(
    new Date(browserToday.year, browserToday.month, browserToday.day),
  );

  const protectionPeriodBlock = firstOfferRow.locator('.offer-row__meta-grid > div').filter({
    hasText: 'Okres ochrony',
  });
  await expect(protectionPeriodBlock).toContainText(expectedProtectionPeriod);
  await captureStep(page, testInfo, 'protection-period-rendered');

  const variantBlock = firstOfferRow.locator('.offer-row__meta-grid > div').filter({
    hasText: 'Wariant',
  });
  await expect(variantBlock.locator('strong')).toHaveText(/\S+/);

  const variantLink = variantBlock.locator('a');
  if ((await variantLink.count()) > 0) {
    await expect(variantLink.first()).toBeVisible();
  } else {
    await expect(variantBlock).toContainText('Wybrany wariant');
  }
  await captureStep(page, testInfo, 'variant-content-rendered');

  const metaLabels = (
    await firstOfferRow.locator('.offer-row__meta-grid .offer-row__meta-label').allTextContents()
  ).map((label) => label.trim());

  expect(metaLabels).toContain('Okres ochrony');
  expect(metaLabels).toContain('Wariant');
  expect(metaLabels.indexOf('Okres ochrony')).toBeLessThan(metaLabels.indexOf('Wariant'));
  await captureStep(page, testInfo, 'protection-period-before-variant');
});
