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

test('show Status column directly before Kwota (z odsetkami) on kwitariusze list', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(page.locator('table')).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-list-loaded');

    await expect(page.getByRole('columnheader', { name: 'Status' })).toBeVisible();
    await expect(page.getByRole('columnheader', { name: /Kwota \(z odsetkami\)/ })).toBeVisible();
    await captureStep(page, testInfo, 'status-and-amount-headers-visible');

    const headers = (await page.locator('th.mat-mdc-header-cell').allInnerTexts()).map((text) =>
        text.replace(/\s+/g, ' ').trim(),
    );

    const statusHeaders = headers.filter((text) => text === 'Status');
    const amountHeaders = headers.filter((text) => text.includes('Kwota (z odsetkami)'));
    const statusIndex = headers.findIndex((text) => text === 'Status');
    const amountIndex = headers.findIndex((text) => text.includes('Kwota (z odsetkami)'));

    expect(statusHeaders).toHaveLength(1);
    expect(amountHeaders).toHaveLength(1);
    expect(statusIndex).toBeGreaterThan(-1);
    expect(amountIndex).toBeGreaterThan(-1);
    expect(statusIndex).toBe(amountIndex - 1);

    await captureStep(page, testInfo, 'status-column-before-amount');
});
