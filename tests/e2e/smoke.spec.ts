import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('app loads and renders root view', async ({ page }, testInfo) => {
  await page.goto('/');
  await captureStep(page, testInfo, 'initial-load');

  await expect(page).toHaveTitle(/smart-agent|SmartAgent/i);
  await expect(page.locator('app-root')).toBeVisible();
});
