import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page shows the Clear button after redirect from root', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearButton = page.getByRole('button', { name: 'Clear' });

  await expect(clearButton).toBeVisible();
  await expect(clearButton).toBeDisabled();
  await expect(page.getByRole('button', { name: 'Wyczyść' })).toHaveCount(0);
  await captureStep(page, testInfo, 'clear-button-label-and-default-state');
});

test('Clear resets the offers search filter and restores the default list state', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page).toHaveURL(/\/offers$/);

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const clearButton = page.getByRole('button', { name: 'Clear' });
  const resultsMeta = page.locator('.results-meta span');
  const emptyStateTitle = page.getByText('Brak ofert dla podanych filtrów');

  await expect(searchInput).toBeVisible();
  await expect(clearButton).toBeDisabled();
  await expect(resultsMeta).not.toHaveText('');
  const initialResultsText = ((await resultsMeta.textContent()) ?? '').trim();
  await captureStep(page, testInfo, 'offers-default-filters-state');

  await searchInput.fill('zzzx-no-results-987654321');

  await expect(clearButton).toBeEnabled();
  await expect(resultsMeta).toHaveText('0 ofert(y) na liście');
  await expect(emptyStateTitle).toBeVisible();
  await captureStep(page, testInfo, 'offers-empty-state-after-filter');

  await clearButton.click();

  await expect(page).toHaveURL(/\/offers$/);
  await expect(searchInput).toHaveValue('');
  await expect(clearButton).toBeDisabled();
  await expect(emptyStateTitle).toHaveCount(0);
  await expect(resultsMeta).toHaveText(initialResultsText);
  await captureStep(page, testInfo, 'offers-restored-after-clear');
});
