import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('redirects guests to the login screen and protects app routes', async (
  { page },
  testInfo,
) => {
  await page.goto('/');
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByRole('heading', { name: 'Logowanie' })).toBeVisible();
  await expect(page.getByTestId('login-form')).toBeVisible();
  await expect(page.getByTestId('login-password-input')).toHaveAttribute('type', 'password');
  await captureStep(page, testInfo, 'root-redirects-to-login');

  await page.goto('/design');
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByTestId('login-submit-button')).toBeVisible();
  await captureStep(page, testInfo, 'protected-route-redirects-to-login');
});

test('logs in with the default admin account and keeps authenticated users on /empty', async (
  { page },
  testInfo,
) => {
  await page.route('**/api/auth/login', async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify({
        token: 'jwt-token',
        user: {
          username: 'admin',
          firstName: 'Admin',
          lastName: 'Administrator',
          email: 'admin@eumowy.local',
          sellerNumber: 'ADM001',
          auwId: 1,
          roles: ['EUM_ADMINISTRATOR'],
        },
      }),
    });
  });

  const loginRequestPromise = page.waitForRequest('**/api/auth/login');

  await page.goto('/login');
  await page.getByTestId('login-username-input').fill(' admin ');
  await page.getByTestId('login-password-input').fill(' admin ');
  await page.getByTestId('login-submit-button').click();

  const loginRequest = await loginRequestPromise;
  expect(loginRequest.postDataJSON()).toEqual({
    username: 'admin',
    password: 'admin',
  });

  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByRole('heading', { name: 'Pusta strona' })).toBeVisible();
  await expect(page.getByText('Użytkownik został zalogowany.')).toBeVisible();
  await captureStep(page, testInfo, 'successful-login-redirects-to-empty');

  await page.reload();
  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByRole('heading', { name: 'Pusta strona' })).toBeVisible();
  await captureStep(page, testInfo, 'authenticated-refresh-stays-on-empty');

  await page.goto('/login');
  await expect(page).toHaveURL(/\/empty$/);
  await expect(page.getByRole('heading', { name: 'Pusta strona' })).toBeVisible();
  await captureStep(page, testInfo, 'authenticated-user-is-redirected-away-from-login');
});

test('shows validation feedback and invalid credentials errors on the login form', async (
  { page },
  testInfo,
) => {
  let loginAttempts = 0;

  await page.route('**/api/auth/login', async (route) => {
    loginAttempts += 1;

    await route.fulfill({
      status: 401,
      contentType: 'application/json',
      body: JSON.stringify({
        error: 'AUTH_INVALID_CREDENTIALS',
      }),
    });
  });

  await page.goto('/login');
  await page.getByTestId('login-username-input').fill('   ');
  await page.getByTestId('login-password-input').fill('   ');
  await page.getByTestId('login-submit-button').click();

  await expect(page.getByText('Podaj login.')).toBeVisible();
  await expect(page.getByText('Podaj hasło.')).toBeVisible();
  await expect.poll(() => loginAttempts).toBe(0);
  await captureStep(page, testInfo, 'required-field-validation');

  await page.getByTestId('login-username-input').fill('admin');
  await page.getByTestId('login-password-input').fill('wrong');
  await page.getByTestId('login-submit-button').click();

  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByTestId('login-error-message')).toHaveText(
    'Nieprawidłowy login lub hasło.',
  );
  await expect.poll(() => loginAttempts).toBe(1);
  await captureStep(page, testInfo, 'invalid-credentials-error');
});
