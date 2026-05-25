import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const AUTHENTICATED_USER = {
  username: 'admin',
  name: 'Administrator',
  role: 'EUM_ADMINISTRATOR',
  email: 'admin@eumowy.local',
  phone: '',
  auwId: 1,
  sellerNumber: null,
  roles: ['EUM_ADMINISTRATOR'],
};

test('authenticated user stays on /empty after refresh and never sees the removed banner', async ({ page }, testInfo) => {
  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, AUTHENTICATED_USER);

  await page.goto('/empty');

  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await captureStep(page, testInfo, 'empty-start-initial');

  await expect(page.locator('.hero-wrap')).toHaveCount(0);
  await expect(page.locator('.empty-stage')).toBeVisible();
  await expect(page.locator('body')).not.toContainText('Logowanie zakończyło się powodzeniem.');
  await captureStep(page, testInfo, 'empty-start-no-banner');

  await page.reload();

  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await expect(page.locator('.hero-wrap')).toHaveCount(0);
  await captureStep(page, testInfo, 'empty-start-after-refresh');
});
