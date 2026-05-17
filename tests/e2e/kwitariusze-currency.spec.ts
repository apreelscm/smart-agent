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

const USD_LATEST_RATE_URL =
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/USD\/\?format=json/;
const EUR_LATEST_RATE_URL =
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/EUR\/\?format=json/;
const EUR_FALLBACK_RATE_URL =
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/EUR\/\d{4}-\d{2}-\d{2}\/\?format=json/;

test.beforeEach(async ({ page }) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);
});

test('shows PLN currency selector by default and resets to PLN after reload', async ({ page }, testInfo) => {
    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();

    const currencySelect = page.getByLabel('Waluta');
    const totals = page.locator('.cell-amount__total');
    const interests = page.locator('.cell-amount__interest');

    await expect(currencySelect).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');
    await expect(totals.first()).toBeVisible();
    await expect(interests.first()).toBeVisible();

    const totalsBeforeReload = await totals.allTextContents();
    const interestsBeforeReload = await interests.allTextContents();

    expect(totalsBeforeReload.length).toBeGreaterThan(0);
    expect(interestsBeforeReload.length).toBe(totalsBeforeReload.length);

    await captureStep(page, testInfo, 'kwitariusze-default-pln');

    await page.reload();

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');
    await expect(page.locator('.cell-amount__total').allTextContents()).resolves.toEqual(
        totalsBeforeReload,
    );
    await expect(page.locator('.cell-amount__interest').allTextContents()).resolves.toEqual(
        interestsBeforeReload,
    );

    await captureStep(page, testInfo, 'kwitariusze-pln-after-reload');
});

test('converts visible kwitariusze amounts to USD and EUR and restores PLN values', async (
    { page },
    testInfo,
) => {
    await page.route(USD_LATEST_RATE_URL, async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
                table: 'A',
                currency: 'dolar amerykański',
                code: 'USD',
                rates: [
                    {
                        no: '074/A/NBP/2026',
                        effectiveDate: '2026-04-17',
                        mid: 4,
                    },
                ],
            }),
        });
    });

    await page.route(EUR_LATEST_RATE_URL, async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
                table: 'A',
                currency: 'euro',
                code: 'EUR',
                rates: [
                    {
                        no: '074/A/NBP/2026',
                        effectiveDate: '2026-04-17',
                        mid: 4.5,
                    },
                ],
            }),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Waluta');
    const totals = page.locator('.cell-amount__total');
    const interests = page.locator('.cell-amount__interest');

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');

    const plnTotals = await totals.allTextContents();
    const plnInterests = await interests.allTextContents();

    expect(plnTotals.length).toBeGreaterThan(0);
    expect(plnInterests.length).toBe(plnTotals.length);

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(totals.first()).toContainText('USD');
    await expect(interests.first()).toContainText('USD');

    const usdTotals = await totals.allTextContents();
    const usdInterests = await interests.allTextContents();

    expect(usdTotals).not.toEqual(plnTotals);
    expect(usdInterests).not.toEqual(plnInterests);

    for (const text of usdTotals) {
        expect(text).toContain('USD');
    }

    for (const text of usdInterests) {
        expect(text).toContain('USD');
    }

    await captureStep(page, testInfo, 'kwitariusze-usd-conversion');

    await currencySelect.selectOption('EUR');

    await expect(currencySelect).toHaveValue('EUR');
    await expect(totals.first()).toContainText('EUR');
    await expect(interests.first()).toContainText('EUR');

    const eurTotals = await totals.allTextContents();
    const eurInterests = await interests.allTextContents();

    expect(eurTotals).not.toEqual(usdTotals);
    expect(eurInterests).not.toEqual(usdInterests);

    for (const text of eurTotals) {
        expect(text).toContain('EUR');
    }

    for (const text of eurInterests) {
        expect(text).toContain('EUR');
    }

    await captureStep(page, testInfo, 'kwitariusze-eur-conversion');

    await currencySelect.selectOption('PLN');

    await expect(currencySelect).toHaveValue('PLN');
    await expect(totals.first()).toHaveText(plnTotals[0]);
    await expect(interests.first()).toHaveText(plnInterests[0]);

    expect(await totals.allTextContents()).toEqual(plnTotals);
    expect(await interests.allTextContents()).toEqual(plnInterests);

    await captureStep(page, testInfo, 'kwitariusze-pln-restored');
});

test('shows an error and keeps previous converted values when both EUR rate requests fail', async (
    { page },
    testInfo,
) => {
    let eurLatestRequests = 0;
    let eurFallbackRequests = 0;

    await page.route(USD_LATEST_RATE_URL, async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
                table: 'A',
                currency: 'dolar amerykański',
                code: 'USD',
                rates: [
                    {
                        no: '074/A/NBP/2026',
                        effectiveDate: '2026-04-17',
                        mid: 4,
                    },
                ],
            }),
        });
    });

    await page.route(EUR_LATEST_RATE_URL, async (route) => {
        eurLatestRequests += 1;
        await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({ message: 'temporary error' }),
        });
    });

    await page.route(EUR_FALLBACK_RATE_URL, async (route) => {
        eurFallbackRequests += 1;
        await route.fulfill({
            status: 404,
            contentType: 'application/json',
            body: JSON.stringify({ message: 'not found' }),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Waluta');
    const totals = page.locator('.cell-amount__total');
    const interests = page.locator('.cell-amount__interest');
    const errorAlert = page.getByRole('alert');

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(totals.first()).toContainText('USD');
    await expect(interests.first()).toContainText('USD');

    const usdTotals = await totals.allTextContents();
    const usdInterests = await interests.allTextContents();

    await captureStep(page, testInfo, 'kwitariusze-usd-before-error');

    await currencySelect.selectOption('EUR');

    await expect(errorAlert).toContainText(
        'Nie udało się pobrać kursu NBP. Wyświetlane kwoty pozostały bez zmian.',
    );
    await expect(currencySelect).toHaveValue('USD');

    expect(await totals.allTextContents()).toEqual(usdTotals);
    expect(await interests.allTextContents()).toEqual(usdInterests);
    expect(eurLatestRequests).toBe(1);
    expect(eurFallbackRequests).toBe(1);

    await captureStep(page, testInfo, 'kwitariusze-eur-error-keeps-usd');
});
