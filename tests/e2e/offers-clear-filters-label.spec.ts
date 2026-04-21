import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers toolbar shows shortened clear filters label', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const clearButton = page.getByRole('button', { name: 'Wyczyść' });
  await expect(clearButton).toBeVisible();
  await expect(clearButton).toBeDisabled();
  await captureStep(page, testInfo, 'clear-button-short-label-disabled');

  await expect(page.getByRole('button', { name: 'Wyczyść wszystkie filtry' })).toHaveCount(0);
  await captureStep(page, testInfo, 'old-clear-button-label-absent');
});
