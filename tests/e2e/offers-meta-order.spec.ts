import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('renders Okres ochrony before Aktualizacja in every visible offer row', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-open');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offer-rows-visible');

  const offerCount = await offerRows.count();
  expect(offerCount).toBeGreaterThan(0);

  for (let index = 0; index < offerCount; index += 1) {
    const row = offerRows.nth(index);
    const metaLabels = await row.locator('.offer-row__meta-grid .offer-row__meta-label').evaluateAll((labels) =>
      labels.map((label) => label.textContent?.trim() ?? '')
    );

    const protectionPeriodIndex = metaLabels.indexOf('Okres ochrony');
    const updatedAtIndex = metaLabels.indexOf('Aktualizacja');

    expect(protectionPeriodIndex).toBeGreaterThanOrEqual(0);
    expect(updatedAtIndex).toBeGreaterThanOrEqual(0);
    expect(protectionPeriodIndex).toBeLessThan(updatedAtIndex);
  }

  await captureStep(page, testInfo, 'meta-order-verified');
});
