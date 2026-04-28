import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows protection period directly before update on desktop', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  const rowCount = await offerRows.count();

  expect(rowCount).toBeGreaterThan(0);

  for (let index = 0; index < rowCount; index += 1) {
    const metaLabels = offerRows.nth(index).locator('.offer-row__meta-grid .offer-row__meta-label');

    await expect(metaLabels).toHaveCount(5);
    await expect(metaLabels.nth(2)).toHaveText('Wariant');
    await expect(metaLabels.nth(3)).toHaveText('Okres ochrony');
    await expect(metaLabels.nth(4)).toHaveText('Aktualizacja');
  }

  const firstRowMetaCells = offerRows.first().locator('.offer-row__meta-grid > div');
  const protectionPeriodCell = firstRowMetaCells.nth(3);
  const updateCell = firstRowMetaCells.nth(4);

  await expect(protectionPeriodCell.locator('strong')).toHaveText(
    /\d{4}\/\d{2}\/\d{2} - \d{4}\/\d{2}\/\d{2}/,
  );
  await expect(updateCell.locator('strong')).toHaveText(/\d{2}\.\d{2}\.\d{4}/);
  await expect(updateCell.locator('span').nth(1)).toHaveText(/\d{2}:\d{2}/);
  await captureStep(page, testInfo, 'protection-period-before-update-desktop');
});

test('offers list keeps protection period before update on a narrow viewport', async ({ page }, testInfo) => {
  await page.setViewportSize({ width: 600, height: 900 });
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();

  const firstRow = page.locator('.offer-row').first();

  await expect(firstRow).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-narrow');

  const metaLabels = firstRow.locator('.offer-row__meta-grid .offer-row__meta-label');

  await expect(metaLabels).toHaveCount(5);
  await expect(metaLabels.nth(2)).toHaveText('Wariant');
  await expect(metaLabels.nth(3)).toHaveText('Okres ochrony');
  await expect(metaLabels.nth(4)).toHaveText('Aktualizacja');
  await captureStep(page, testInfo, 'protection-period-before-update-narrow');
});
