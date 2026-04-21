import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('app loads and renders root view', async ({ page }, testInfo) => {
  await page.goto('/');
  await expect(page.locator('app-root')).toBeVisible();
  await captureStep(page, testInfo, 'initial-load');

  await expect(page).toHaveTitle(/smart-agent|SmartAgent/i);
});

test('currency switcher is visible on reports page', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.getByRole('link', { name: /raporty/i }).click();
  await expect(page.getByText('Raporty')).toBeVisible();
  await captureStep(page, testInfo, 'reports-open');

  await expect(page.getByRole('button', { name: 'PLN' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'EUR' })).toBeVisible();
  await expect(page.getByRole('button', { name: 'USD' })).toBeVisible();
  await captureStep(page, testInfo, 'currency-switcher-visible');
});
