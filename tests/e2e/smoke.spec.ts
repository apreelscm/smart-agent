import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const DEMO_USER = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
};

test('app loads and renders root view', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/');
    await captureStep(page, testInfo, 'initial-load');

    await expect(page).toHaveTitle(/smart-agent|eUmowy/i);
    await expect(page.locator('app-root')).toBeVisible();
});
