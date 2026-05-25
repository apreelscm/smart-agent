import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test.beforeEach(async ({ page }) => {
    await page.addInitScript(() => {
        localStorage.clear();
        sessionStorage.clear();
    });
});

test('renders the minimal login screen without wizard content', async ({ page }, testInfo) => {
    await page.goto('/login');

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByTestId('login-form')).toBeVisible();
    await expect(page.getByTestId('login-username-input')).toBeVisible();
    await expect(page.getByTestId('login-password-input')).toBeVisible();
    await expect(page.getByTestId('login-submit-button')).toHaveText('Zaloguj się');
    await expect(page.getByTestId('login-error-message')).toHaveCount(0);
    await captureStep(page, testInfo, 'minimal-login-form');

    await expect(page.locator('app-wizard-card')).toHaveCount(0);
    await expect(page.getByText('Zaloguj się do systemu', { exact: true })).toHaveCount(0);
    await expect(page.getByText('Dane dostępowe', { exact: true })).toHaveCount(0);
    await expect(
        page.getByText('Po zalogowaniu od razu przejdziesz do ekranu startowego.', {
            exact: true,
        }),
    ).toHaveCount(0);
    await captureStep(page, testInfo, 'legacy-login-content-absent');
});

test('shows the existing 401 error message for invalid credentials', async ({ page }, testInfo) => {
    await page.goto('/login');
    await page.getByTestId('login-username-input').fill('admin');
    await page.getByTestId('login-password-input').fill('wrong');
    await captureStep(page, testInfo, 'invalid-credentials-filled');

    await page.getByTestId('login-submit-button').click();

    await expect(page).toHaveURL(/\/login$/);
    await expect(page.getByTestId('login-error-message')).toBeVisible();
    await expect(page.getByTestId('login-error-message')).toHaveText(
        'Nieprawidłowy login lub hasło.',
    );
    await expect(page.getByTestId('login-submit-button')).toHaveText('Zaloguj się');
    await captureStep(page, testInfo, 'invalid-credentials-error');
});

test('logs in with valid credentials and redirects to the empty screen', async ({ page }, testInfo) => {
    await page.goto('/login');
    await page.getByTestId('login-username-input').fill('admin');
    await page.getByTestId('login-password-input').fill('admin');
    await captureStep(page, testInfo, 'valid-credentials-filled');

    await page.getByTestId('login-submit-button').click();

    await expect(page).toHaveURL(/\/empty$/);
    await expect(page.getByTestId('login-form')).toHaveCount(0);
    await captureStep(page, testInfo, 'redirected-to-empty');
});
