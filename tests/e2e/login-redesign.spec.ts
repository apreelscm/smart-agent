import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('login page renders the redesigned layout and validates whitespace-only input', async ({ page }, testInfo) => {
  await page.goto('/login');

  await expect(page.getByRole('heading', { name: 'Zaloguj się do systemu' })).toBeVisible();
  await expect(page.getByText('Dane dostępowe')).toBeVisible();
  await expect(page.getByTestId('login-form')).toBeVisible();
  await captureStep(page, testInfo, 'login-redesigned-layout');

  await expect(page.getByText('Użyj swojego aktualnego loginu i hasła.')).toBeVisible();
  await expect(page.getByTestId('login-username-input')).toBeVisible();
  await expect(page.getByTestId('login-password-input')).toBeVisible();
  await expect(page.getByTestId('login-submit-button')).toHaveText('Zaloguj się');
  await captureStep(page, testInfo, 'login-form-controls');

  await page.getByTestId('login-username-input').fill('   ');
  await page.getByTestId('login-password-input').fill('   ');
  await page.getByTestId('login-submit-button').click();

  await expect(page.getByText('Podaj login.')).toBeVisible();
  await expect(page.getByText('Podaj hasło.')).toBeVisible();
  await expect(page).toHaveURL(/\/login$/);
  await captureStep(page, testInfo, 'login-validation-errors');
});

test('successful login redirects to the sparse empty start view without a banner', async ({ page }, testInfo) => {
  await page.goto('/login');

  await page.getByTestId('login-username-input').fill('admin');
  await page.getByTestId('login-password-input').fill('admin');
  await captureStep(page, testInfo, 'login-credentials-entered');

  await page.getByTestId('login-submit-button').click();

  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await captureStep(page, testInfo, 'empty-start-after-login');

  await expect(page.locator('.hero-wrap')).toHaveCount(0);
  await expect(page.locator('.empty-stage')).toBeVisible();
  await expect(page.locator('body')).not.toContainText('Pusta strona');
  await captureStep(page, testInfo, 'empty-start-bannerless');
});
