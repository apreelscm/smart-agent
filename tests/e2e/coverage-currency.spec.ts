import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

function getPreviousCalendarDate() {
    const previousDate = new Date();
    previousDate.setDate(previousDate.getDate() - 1);

    const year = previousDate.getFullYear();
    const month = String(previousDate.getMonth() + 1).padStart(2, '0');
    const day = String(previousDate.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

test('shows PLN by default without fetching exchange rates', async ({ page }, testInfo) => {
    let nbpRequests = 0;

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/**', async route => {
        nbpRequests += 1;
        await route.fulfill({
            status: 500,
            body: 'unexpected NBP request',
        });
    });

    await page.goto('/');
    await page.goto('/kalkulator/zakres');

    await expect(page).toHaveURL(/\/kalkulator\/zakres$/);
    await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();

    const currencySelect = page.getByLabel('Waluta');
    const ocCard = page.locator('.cov-card').filter({ hasText: 'OC' });
    const totalPrice = page.locator('.price-total');

    await expect(currencySelect).toHaveValue('PLN');
    await expect(ocCard.locator('.cov-price')).toHaveText('289.00 PLN');
    await expect(totalPrice).toHaveText('1046.00 PLN');
    await expect(page.locator('.currency-meta')).toHaveCount(0);
    await expect(page.getByRole('alert')).toHaveCount(0);
    await expect.poll(() => nbpRequests).toBe(0);

    await captureStep(page, testInfo, 'coverage-default-pln');
});

test('applies fallback EUR rate and returns to PLN values', async ({ page }, testInfo) => {
    const previousDate = getPreviousCalendarDate();

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json', async route => {
        await route.fulfill({
            status: 500,
            body: 'NBP unavailable',
        });
    });

    await page.route(
        `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${previousDate}/?format=json`,
        async route => {
            await route.fulfill({
                status: 200,
                contentType: 'application/json',
                body: JSON.stringify({
                    table: 'A',
                    code: 'EUR',
                    rates: [
                        {
                            no: '073/A/NBP/2026',
                            effectiveDate: previousDate,
                            mid: 4.5,
                        },
                    ],
                }),
            });
        },
    );

    await page.goto('/');
    await page.goto('/kalkulator/zakres');

    await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();

    const currencySelect = page.getByLabel('Waluta');
    const ocCard = page.locator('.cov-card').filter({ hasText: 'OC' });
    const totalPrice = page.locator('.price-total');
    const currencyMeta = page.locator('.currency-meta');

    await currencySelect.selectOption('EUR');

    await expect(currencySelect).toHaveValue('EUR');
    await expect(ocCard.locator('.cov-price')).toHaveText('64.22 EUR');
    await expect(totalPrice).toHaveText('232.44 EUR');
    await expect(currencyMeta).toContainText('1 EUR = 4.5000 PLN');
    await expect(currencyMeta).toContainText('A / 073/A/NBP/2026');
    await expect(currencyMeta).toContainText(previousDate);

    await captureStep(page, testInfo, 'coverage-eur-fallback');

    await currencySelect.selectOption('PLN');

    await expect(currencySelect).toHaveValue('PLN');
    await expect(ocCard.locator('.cov-price')).toHaveText('289.00 PLN');
    await expect(totalPrice).toHaveText('1046.00 PLN');
    await expect(page.locator('.currency-meta')).toHaveCount(0);
    await expect(page.getByRole('alert')).toHaveCount(0);

    await captureStep(page, testInfo, 'coverage-back-to-pln');
});

test('keeps the last successful USD view when the next lookup fails', async ({ page }, testInfo) => {
    const previousDate = getPreviousCalendarDate();

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json', async route => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify({
                table: 'A',
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

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json', async route => {
        await route.fulfill({
            status: 500,
            body: 'NBP unavailable',
        });
    });

    await page.route(
        `https://api.nbp.pl/api/exchangerates/rates/A/EUR/${previousDate}/?format=json`,
        async route => {
            await route.fulfill({
                status: 503,
                body: 'NBP still unavailable',
            });
        },
    );

    await page.goto('/');
    await page.goto('/kalkulator/zakres');

    await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();

    const currencySelect = page.getByLabel('Waluta');
    const totalPrice = page.locator('.price-total');
    const currencyMeta = page.locator('.currency-meta');
    const errorAlert = page.getByRole('alert');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(totalPrice).toHaveText('261.50 USD');
    await expect(currencyMeta).toContainText('1 USD = 4.0000 PLN');
    await expect(currencyMeta).toContainText('A / 074/A/NBP/2026');
    await expect(currencyMeta).toContainText('2026-04-17');

    await captureStep(page, testInfo, 'coverage-usd-applied');

    await currencySelect.selectOption('EUR');

    await expect(errorAlert).toContainText(
        'Nie udało się pobrać kursu waluty. Pozostawiliśmy ostatnio wyświetlone wartości.',
    );
    await expect(currencySelect).toHaveValue('USD');
    await expect(totalPrice).toHaveText('261.50 USD');
    await expect(currencyMeta).toContainText('1 USD = 4.0000 PLN');
    await expect(currencyMeta).toContainText('2026-04-17');

    await captureStep(page, testInfo, 'coverage-error-preserves-usd');
});
