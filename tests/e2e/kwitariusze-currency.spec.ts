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

const NBP_LATEST_TABLE_A_URL =
    'https://api.nbp.pl/api/exchangerates/tables/A/last/1/?format=json';

test('switches kwitariusze list currency to USD and back to PLN', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.route(NBP_LATEST_TABLE_A_URL, async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify([
                {
                    table: 'A',
                    no: '074/A/NBP/2026',
                    effectiveDate: '2026-04-17',
                    rates: [
                        {
                            currency: 'euro',
                            code: 'EUR',
                            mid: 4.4,
                        },
                        {
                            currency: 'dolar amerykański',
                            code: 'USD',
                            mid: 4,
                        },
                    ],
                },
            ]),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    await expect(page).toHaveURL(/\/rozliczenia\/kwitariusze$/);
    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();

    const currencySelect = page.getByLabel('Lista');

    await expect(currencySelect).toHaveValue('PLN');
    await expect(currencySelect.locator('option')).toHaveText(['PLN', 'USD', 'EUR']);
    await expect(page.getByText('328,50 zł', { exact: true })).toBeVisible();
    await expect(page.getByText('odsetki: 18,50 zł', { exact: true })).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-default-pln');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(page.locator('.currency-panel')).toContainText('1 USD = 4,0000 PLN');
    await expect(page.locator('.currency-panel')).toContainText('A / 074/A/NBP/2026');
    await expect(page.locator('.currency-panel')).toContainText('2026-04-17');
    await expect(page.getByText('82,13 USD', { exact: true })).toBeVisible();
    await expect(page.getByText('odsetki: 4,63 USD', { exact: true })).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-usd-converted');

    await currencySelect.selectOption('PLN');

    await expect(currencySelect).toHaveValue('PLN');
    await expect(page.locator('.currency-panel')).toHaveCount(0);
    await expect(page.getByText('328,50 zł', { exact: true })).toBeVisible();
    await expect(page.getByText('odsetki: 18,50 zł', { exact: true })).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-pln-restored');
});

test('starts from PLN after returning to the kwitariusze screen', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.route(NBP_LATEST_TABLE_A_URL, async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify([
                {
                    table: 'A',
                    no: '074/A/NBP/2026',
                    effectiveDate: '2026-04-17',
                    rates: [
                        {
                            currency: 'euro',
                            code: 'EUR',
                            mid: 4.4,
                        },
                        {
                            currency: 'dolar amerykański',
                            code: 'USD',
                            mid: 4,
                        },
                    ],
                },
            ]),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Lista');

    await currencySelect.selectOption('USD');
    await expect(currencySelect).toHaveValue('USD');
    await expect(page.getByText('82,13 USD', { exact: true })).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-before-return');

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    await expect(page).toHaveURL(/\/rozliczenia\/kwitariusze$/);
    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(page.getByLabel('Lista')).toHaveValue('PLN');
    await expect(page.locator('.currency-panel')).toHaveCount(0);
    await expect(page.getByText('328,50 zł', { exact: true })).toBeVisible();
    await expect(page.getByText('odsetki: 18,50 zł', { exact: true })).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-after-return-default-pln');
});

test('shows an error and hides converted amounts when NBP rate loading fails', async (
    { page },
    testInfo,
) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.route(NBP_LATEST_TABLE_A_URL, async (route) => {
        await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({
                message: 'NBP unavailable',
            }),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Lista');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(
        page.getByText('Nie udało się pobrać kursu NBP dla wybranej waluty.', {
            exact: true,
        }),
    ).toBeVisible();
    await expect(page.getByText('—', { exact: true }).first()).toBeVisible();
    await expect(page.getByText('odsetki: —', { exact: true }).first()).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-rate-error');
});
