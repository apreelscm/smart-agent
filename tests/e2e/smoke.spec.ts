import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('app loads and shows mixed currency offers on dashboard', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.locator('app-root')).toBeVisible();
  await captureStep(page, testInfo, 'app-root-visible');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.getByText('1 280 €')).toBeVisible();
  await expect(page.getByText('1 490 USD')).toBeVisible();
  await captureStep(page, testInfo, 'dashboard-currencies-visible');

  await expect(page.getByText('tylko dla ofert PLN')).toBeVisible();
  await captureStep(page, testInfo, 'pln-average-note-visible');
});
