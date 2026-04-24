import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows the Wyczyść button on offers after redirect from root', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-after-root-redirect');

  const clearButton = page.getByRole('button', { name: 'Wyczyść' });

  await expect(clearButton).toBeVisible();
  await expect(clearButton).toBeDisabled();
  await expect(clearButton).toHaveText(/Wyczyść/);
  await expect(page.getByRole('button', { name: 'Clear' })).toHaveCount(0);
  await captureStep(page, testInfo, 'wyczysc-button-visible-and-disabled');
});

test('clears the search filter and restores the default offers list', async ({ page }, testInfo) => {
  await page.goto('/');

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const clearButton = page.getByRole('button', { name: 'Wyczyść' });
  const resultsMeta = page.locator('.results-meta span');
  const offerRows = page.locator('.offer-row');
  const emptyStateTitle = page.getByText('Brak ofert dla podanych filtrów');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(searchInput).toBeVisible();
  await expect(clearButton).toBeDisabled();
  await expect(offerRows.first()).toBeVisible();

  const initialOffersCount = await offerRows.count();
  expect(initialOffersCount).toBeGreaterThan(0);

  await captureStep(page, testInfo, 'offers-default-list');

  await searchInput.fill('___brak-oferty___');

  await expect(clearButton).toBeEnabled();
  await expect(resultsMeta).toHaveText('0 ofert(y) na liście');
  await expect(emptyStateTitle).toBeVisible();
  await expect(page.getByText('Zmień kryteria wyszukiwania albo wyczyść filtry.')).toBeVisible();
  await expect(offerRows).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-empty-state-before-clear');

  await clearButton.click();

  await expect(searchInput).toHaveValue('');
  await expect(clearButton).toBeDisabled();
  await expect(emptyStateTitle).toHaveCount(0);
  await expect(offerRows).toHaveCount(initialOffersCount);
  await expect(resultsMeta).not.toHaveText('0 ofert(y) na liście');
  await captureStep(page, testInfo, 'offers-list-restored-after-clear');
});
