import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

function normalizeText(value: string): string {
  return value.replace(/\s+/g, ' ').trim();
}

test('offers list shows one shared coverage period before update date', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();

  const rowCount = await offerRows.count();
  expect(rowCount).toBeGreaterThan(0);

  const coverageValues = (await page.locator('.offer-row__coverage-value').allTextContents()).map((value) =>
    normalizeText(value),
  );

  expect(coverageValues).toHaveLength(rowCount);

  const coveragePattern = /^\d{4}\/\d{2}\/\d{2} \d{2}:\d{2} - \d{4}\/\d{2}\/\d{2} \d{2}:\d{2}$/;
  for (const value of coverageValues) {
    expect(value).toMatch(coveragePattern);
  }

  expect(new Set(coverageValues).size).toBe(1);
  await captureStep(page, testInfo, 'coverage-period-visible');

  const metaLabels = (
    await offerRows
      .first()
      .locator('.offer-row__meta-grid .offer-row__meta-label')
      .allTextContents()
  ).map((label) => normalizeText(label));

  const coverageIndex = metaLabels.indexOf('Okres ochrony');
  const updatedAtIndex = metaLabels.indexOf('Aktualizacja');

  expect(coverageIndex).toBeGreaterThan(-1);
  expect(updatedAtIndex).toBeGreaterThan(-1);
  expect(coverageIndex).toBeLessThan(updatedAtIndex);
  await captureStep(page, testInfo, 'coverage-period-before-updated-at');
});

test('offers empty state stays visible when filters return no results', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-before-filter');

  await page
    .getByPlaceholder('Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja')
    .fill('brak-wynikow-e2e');

  await expect(page.getByText('Brak ofert dla podanych filtrów', { exact: true })).toBeVisible();
  await expect(page.getByText('Zmień kryteria wyszukiwania albo wyczyść filtry.')).toBeVisible();
  await expect(page.locator('.offer-row')).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-empty-state');
});
