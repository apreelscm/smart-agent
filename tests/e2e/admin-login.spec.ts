import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('logs in with admin credentials and preserves session after reload', async (
  { page },
  testInfo,
) => {
  await page.goto('/');

  await expect(page).toHaveURL(/\/login(?:\?.*)?$/);

  const loginForm = page.locator('form').first();
  const usernameInput = loginForm
    .locator(
      'input[formcontrolname="username"], input[name="username"], input[type="text"], input[type="email"]',
    )
    .first();
  const passwordInput = loginForm
    .locator('input[formcontrolname="password"], input[name="password"], input[type="password"]')
    .first();
  const submitButton = loginForm.locator('button[type="submit"], button').first();

  await expect(loginForm).toBeVisible();
  await expect(usernameInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await expect(submitButton).toBeVisible();
  await captureStep(page, testInfo, 'login-form-visible');

  await usernameInput.fill(' admin ');
  await passwordInput.fill(' admin ');
  await submitButton.click();

  await expect(page).toHaveURL(/\/empty(?:\?.*)?$/);
  await expect(page.locator('app-root')).toBeVisible();

  const storedCurrentUser = await page.evaluate(() => localStorage.getItem('auth.currentUser'));
  expect(storedCurrentUser).toContain('"username":"admin"');

  await captureStep(page, testInfo, 'admin-redirected-to-empty');

  await page.reload();

  await expect(page).toHaveURL(/\/empty(?:\?.*)?$/);
  await expect(page.locator('app-root')).toBeVisible();
  await captureStep(page, testInfo, 'session-preserved-after-reload');
});

test('shows an error for admin with an invalid password', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page).toHaveURL(/\/login(?:\?.*)?$/);

  const loginForm = page.locator('form').first();
  const usernameInput = loginForm
    .locator(
      'input[formcontrolname="username"], input[name="username"], input[type="text"], input[type="email"]',
    )
    .first();
  const passwordInput = loginForm
    .locator('input[formcontrolname="password"], input[name="password"], input[type="password"]')
    .first();
  const submitButton = loginForm.locator('button[type="submit"], button').first();
  const invalidCredentialsMessage = page.getByText(/nieprawidłowy login lub hasło/i);

  await expect(loginForm).toBeVisible();
  await expect(usernameInput).toBeVisible();
  await expect(passwordInput).toBeVisible();
  await captureStep(page, testInfo, 'invalid-login-form-visible');

  await usernameInput.fill('admin');
  await passwordInput.fill('wrong-password');
  await submitButton.click();

  await expect(page).toHaveURL(/\/login(?:\?.*)?$/);
  await expect(loginForm).toBeVisible();
  await captureStep(page, testInfo, 'invalid-admin-login-stays-on-form');

  await expect(invalidCredentialsMessage).toBeVisible();
  await captureStep(page, testInfo, 'invalid-admin-login-error-visible');
});
