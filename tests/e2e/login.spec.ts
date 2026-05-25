import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const VALID_CREDENTIALS = {
  username: 'askonieczny',
  password: 'CzerwiecSierpien2023%',
};

test('redirects guests to the redesigned login screen and protects app routes', async (
  { page },
  testInfo,
) => {
  await page.goto('/');
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByRole('heading', { name: 'Zaloguj się do systemu' })).toBeVisible();
  await expect(page.getByTestId('login-form')).toBeVisible();
  await expect(page.getByTestId('login-password-input')).toHaveAttribute('type', 'password');
  await captureStep(page, testInfo, 'root-redirects-to-login');

  await page.goto('/design');
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByTestId('login-submit-button')).toBeVisible();
  await captureStep(page, testInfo, 'protected-route-redirects-to-login');
});

test('logs in with a valid account and keeps authenticated users on /empty', async (
  { page },
  testInfo,
) => {
  await page.goto('/login');
  await page.getByTestId('login-username-input').fill(VALID_CREDENTIALS.username);
  await page.getByTestId('login-password-input').fill(VALID_CREDENTIALS.password);
  await page.getByTestId('login-submit-button').click();

  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await expect(page.locator('.empty-stage')).toBeVisible();
  await expect(page.locator('.hero-wrap')).toHaveCount(0);
  await expect(page.locator('body')).not.toContainText('Pusta strona');
  await captureStep(page, testInfo, 'successful-login-redirects-to-empty');

  await page.reload();
  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await expect(page.locator('.hero-wrap')).toHaveCount(0);
  await captureStep(page, testInfo, 'authenticated-refresh-stays-on-empty');

  await page.goto('/login');
  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByTestId('empty-start-view')).toBeVisible();
  await captureStep(page, testInfo, 'authenticated-user-is-redirected-away-from-login');
});

test('shows validation feedback and invalid credentials errors on the login form', async (
  { page },
  testInfo,
) => {
  await page.goto('/login');
  await page.getByTestId('login-username-input').fill('   ');
  await page.getByTestId('login-password-input').fill('   ');
  await page.getByTestId('login-submit-button').click();

  await expect(page.getByText('Podaj login.')).toBeVisible();
  await expect(page.getByText('Podaj hasło.')).toBeVisible();
  await expect(page.getByTestId('login-error-message')).toHaveCount(0);
  await captureStep(page, testInfo, 'required-field-validation');

  await page.getByTestId('login-username-input').fill('invalid-user');
  await page.getByTestId('login-password-input').fill('wrong');
  await page.getByTestId('login-submit-button').click();

  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByTestId('login-error-message')).toHaveText(
    'Nieprawidłowy login lub hasło.',
  );
  await captureStep(page, testInfo, 'invalid-credentials-error');
});
