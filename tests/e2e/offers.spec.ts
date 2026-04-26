import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page shows the updated clear filters label', async ({ page }, testInfo) => {
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść' });
  await expect(clearFiltersButton).toBeVisible();
  await captureStep(page, testInfo, 'clear-button-visible');

  await expect(page.getByText('Wyczyść wszystkie filtry')).toHaveCount(0);
  await captureStep(page, testInfo, 'old-clear-button-label-absent');
});

test('clear button resets the search filter', async ({ page }, testInfo) => {
  await page.goto('/offers');

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść' });

  await expect(searchInput).toBeVisible();
  await expect(clearFiltersButton).toBeDisabled();
  await captureStep(page, testInfo, 'filters-initial-state');

  await searchInput.fill('Jan');
  await expect(searchInput).toHaveValue('Jan');
  await expect(clearFiltersButton).toBeEnabled();
  await captureStep(page, testInfo, 'search-filter-applied');

  await clearFiltersButton.click();
  await expect(searchInput).toHaveValue('');
  await expect(clearFiltersButton).toBeDisabled();
  await captureStep(page, testInfo, 'search-filter-cleared');
});
