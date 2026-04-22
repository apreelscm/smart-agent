import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page shows the clear filters action with the updated label', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść' });

  await expect(clearFiltersButton).toBeVisible();
  await expect(clearFiltersButton).toBeDisabled();
  await expect(clearFiltersButton).toHaveText('Wyczyść');
  await expect(page.getByRole('button', { name: 'Wyczyść wszystkie filtry' })).toHaveCount(0);
  await captureStep(page, testInfo, 'clear-filters-label');
});

test('offers page still clears the search filter with the Wyczyść action', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść' });

  await searchInput.fill('Jan');
  await expect(searchInput).toHaveValue('Jan');
  await expect(clearFiltersButton).toBeEnabled();
  await captureStep(page, testInfo, 'search-filter-applied');

  await clearFiltersButton.click();

  await expect(searchInput).toHaveValue('');
  await expect(clearFiltersButton).toBeDisabled();
  await captureStep(page, testInfo, 'search-filter-cleared');
});
