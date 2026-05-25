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
        await expect(page.getByTestId('empty-start-view')).toBeVisible();
        await expect(page.locator('.empty-stage')).toBeVisible();
        await expect(page.locator('.hero-wrap')).toHaveCount(0);
        await captureStep(page, testInfo, 'login-success');

        await page.reload();

        await expect(page).toHaveURL(/\/empty$/);
        await expect(page.getByTestId('empty-start-view')).toBeVisible();
        await expect(page.locator('.hero-wrap')).toHaveCount(0);
        await captureStep(page, testInfo, 'session-restored-after-refresh');

        await page.goto('/login');

        await expect(page).toHaveURL(/\/empty$/);
        await expect(page.getByTestId('empty-start-view')).toBeVisible();
        await expect(page.locator('body')).not.toContainText('Pusta strona');
        await captureStep(page, testInfo, 'authenticated-user-redirected-from-login');
    },
);
