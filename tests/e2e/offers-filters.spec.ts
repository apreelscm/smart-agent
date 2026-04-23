import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page shows the shortened clear filters label', async ({ page }, testInfo) => {
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearButton = page.locator('.clear-filters-button');

  await expect(clearButton).toBeVisible();
  await expect(clearButton).toHaveText('Wyczyść');
  await captureStep(page, testInfo, 'clear-button-short-label');

  await expect(clearButton).toBeDisabled();
  await captureStep(page, testInfo, 'clear-button-disabled-by-default');
});

test('clear button resets the search filter from the empty state', async ({ page }, testInfo) => {
  await page.goto('/offers');

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const clearButton = page.locator('.clear-filters-button');

  await expect(searchInput).toBeVisible();
  await expect(clearButton).toHaveText('Wyczyść');
  await captureStep(page, testInfo, 'offers-filters-initial-state');

  await searchInput.fill('zzznieistniejaca-oferta');
  await expect(clearButton).toBeEnabled();
  await expect(page.getByText('Brak ofert dla podanych filtrów')).toBeVisible();
  await captureStep(page, testInfo, 'empty-state-with-short-clear-label');

  await clearButton.click();

  await expect(searchInput).toHaveValue('');
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await expect(page.locator('.empty-state')).toHaveCount(0);
  await captureStep(page, testInfo, 'filters-cleared-and-results-restored');

  await expect(clearButton).toBeDisabled();
  await captureStep(page, testInfo, 'clear-button-disabled-after-reset');
});
