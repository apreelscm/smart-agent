import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows protection period before update date in each row', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.locator('.offer-list')).toBeVisible();
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();

  const rowCount = await offerRows.count();
  expect(rowCount).toBeGreaterThan(0);

  const coverageValue = offerRows
    .first()
    .locator('.offer-row__meta-item--coverage .offer-row__coverage-value');
  await expect(coverageValue).toHaveText(
    /\d{4}\/\d{2}\/\d{2} 00:00 - \d{4}\/\d{2}\/\d{2} 23:59/,
  );
  await captureStep(page, testInfo, 'coverage-period-visible');

  for (let i = 0; i < rowCount; i++) {
    const labels = await offerRows
      .nth(i)
      .locator('.offer-row__meta-grid > div .offer-row__meta-label')
      .allTextContents();
    const normalizedLabels = labels.map((label) => label.trim());
    const coverageIndex = normalizedLabels.indexOf('Okres ochrony');
    const updateIndex = normalizedLabels.indexOf('Aktualizacja');

    expect(coverageIndex).toBeGreaterThan(-1);
    expect(updateIndex).toBeGreaterThan(-1);
    expect(coverageIndex).toBeLessThan(updateIndex);
  }

  await captureStep(page, testInfo, 'coverage-period-before-update');
});

test('offers empty state stays unchanged after filtering to zero results', async ({ page }, testInfo) => {
  await page.goto('/');

  const searchInput = page.locator('input[placeholder*="Szukaj"]');
  await expect(searchInput).toBeVisible();

  await searchInput.fill('brak-oferty-e2e-12345');

  const emptyState = page.locator('.empty-state');
  await expect(emptyState).toBeVisible();
  await expect(emptyState.getByText('Brak ofert dla podanych filtrów')).toBeVisible();
  await captureStep(page, testInfo, 'empty-state-after-filter');

  await expect(
    emptyState.getByText('Zmień kryteria wyszukiwania albo wyczyść filtry.'),
  ).toBeVisible();
  await expect(page.getByText('0 ofert(y) na liście')).toBeVisible();
  await captureStep(page, testInfo, 'empty-state-details');
});
