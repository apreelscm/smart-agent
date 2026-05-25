import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('redirects guests to login and renders the minimal login form', async ({ page }, testInfo) => {
    await page.goto('/');

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByTestId('login-form')).toBeVisible();
    await captureStep(page, testInfo, 'guest-redirect-to-login');

    const loginForm = page.getByTestId('login-form');

    await expect(page.getByLabel('Login')).toBeVisible();
    await expect(page.getByLabel('Hasło')).toBeVisible();
    await expect(page.getByTestId('login-submit-button')).toBeVisible();
    await expect(loginForm.locator('input')).toHaveCount(2);
    await expect(loginForm.locator('button')).toHaveCount(1);
    await expect(loginForm.locator('a')).toHaveCount(0);
    await expect(page.getByTestId('login-password-input')).toHaveAttribute('type', 'password');
    await captureStep(page, testInfo, 'minimal-login-form');

    await page.goto('/design');

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByTestId('login-form')).toBeVisible();
    await captureStep(page, testInfo, 'guarded-route-redirect');
});

test('shows validation feedback for whitespace-only credentials', async ({ page }, testInfo) => {
    await page.goto('/login');

    await page.getByTestId('login-username-input').fill('   ');
    await page.getByTestId('login-password-input').fill('   ');
    await page.getByTestId('login-submit-button').click();

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByText('Podaj login.')).toBeVisible();
    await expect(page.getByText('Podaj hasło.')).toBeVisible();
    await expect(page.getByTestId('login-error-message')).toHaveCount(0);
    await captureStep(page, testInfo, 'whitespace-validation');
});

test('shows a generic error after invalid login', async ({ page }, testInfo) => {
    await page.goto('/login');

    await page.getByTestId('login-username-input').fill('invalid-user');
    await page.getByTestId('login-password-input').fill('invalid-password');
    await page.getByTestId('login-submit-button').click();

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByTestId('login-error-message')).toHaveText(
        'Nieprawidłowy login lub hasło.',
    );
    await captureStep(page, testInfo, 'invalid-credentials-error');
});
