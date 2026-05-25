import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const VALID_CREDENTIALS = {
    username: 'askonieczny',
    password: 'CzerwiecSierpien2023%',
};

test(
    'logs in successfully, restores the session after refresh, and redirects from /login',
    async ({ page }, testInfo) => {
        await page.goto('/login');

        await page.getByTestId('login-username-input').fill(VALID_CREDENTIALS.username);
        await page.getByTestId('login-password-input').fill(VALID_CREDENTIALS.password);
        await page.getByTestId('login-submit-button').click();

        await expect(page).toHaveURL(/\/empty$/);
        await expect(page.getByRole('heading', { name: '/empty' })).toBeVisible();
        await expect(page.getByText('Logowanie zakończyło się powodzeniem.')).toBeVisible();
        await captureStep(page, testInfo, 'login-success');

        await page.reload();

        await expect(page).toHaveURL(/\/empty$/);
        await expect(page.getByRole('heading', { name: '/empty' })).toBeVisible();
        await captureStep(page, testInfo, 'session-restored-after-refresh');

        await page.goto('/login');

        await expect(page).toHaveURL(/\/empty$/);
        await expect(
            page.getByText('To jest docelowa strona po zalogowaniu w tym zadaniu.'),
        ).toBeVisible();
        await captureStep(page, testInfo, 'authenticated-user-redirected-from-login');
    },
);
