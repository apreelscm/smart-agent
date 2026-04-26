import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page shows the shortened clear-filters label and keeps reset behavior', async ({
  page,
}, testInfo) => {
  await page.goto('/offers');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść' });
  const oldClearFiltersButton = page.getByRole('button', {
    name: 'Wyczyść wszystkie filtry',
  });
  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const emptyStateTitle = page.getByText('Brak ofert dla podanych filtrów');

  await expect(clearFiltersButton).toBeVisible();
  await expect(clearFiltersButton).toBeDisabled();
  await expect(oldClearFiltersButton).toHaveCount(0);
  await captureStep(page, testInfo, 'short-clear-label-visible');

  await searchInput.fill('nieistniejąca oferta');
  await expect(searchInput).toHaveValue('nieistniejąca oferta');
  await expect(clearFiltersButton).toBeEnabled();
  await expect(emptyStateTitle).toBeVisible();
  await captureStep(page, testInfo, 'empty-state-with-short-clear-label');

  await clearFiltersButton.click();
  await expect(searchInput).toHaveValue('');
  await expect(clearFiltersButton).toBeDisabled();
  await expect(oldClearFiltersButton).toHaveCount(0);
  await captureStep(page, testInfo, 'filters-cleared-with-short-label');
});
