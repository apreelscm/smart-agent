import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows a protection period for every visible offer', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-visible');

  const rows = page.locator('.offer-row');

  await expect(rows.first()).toBeVisible();

  const rowCount = await rows.count();

  expect(rowCount).toBeGreaterThan(0);

  await expect(page.getByText('Okres ochrony', { exact: true })).toHaveCount(rowCount);
  await captureStep(page, testInfo, 'protection-column-visible');

  const protectionValues = await page.locator('.offer-row__protection-period').allTextContents();

  expect(protectionValues).toHaveLength(rowCount);

  const normalizedValues = protectionValues.map((value) => value.trim());
  const firstValue = normalizedValues[0];

  expect(firstValue).toBeTruthy();

  for (const value of normalizedValues) {
    expect(value).toBe(firstValue);
    expect(value).toMatch(/^\d{4}\/\d{2}\/\d{2} 00:00 - \d{4}\/\d{2}\/\d{2} 23:59$/);
  }

  await captureStep(page, testInfo, 'protection-period-values');
});

test('keeps the empty state without protection period values when no offers match', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-before-filter');

  await page
    .getByPlaceholder('Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja')
    .fill('no-matching-offer-12345');

  await expect(page.getByText('Brak ofert dla podanych filtrów', { exact: true })).toBeVisible();
  await expect(page.locator('.offer-row')).toHaveCount(0);
  await expect(page.locator('.offer-row__meta-item--protection')).toHaveCount(0);
  await captureStep(page, testInfo, 'empty-state-without-protection-values');
});
