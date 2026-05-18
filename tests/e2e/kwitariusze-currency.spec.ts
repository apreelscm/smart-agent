import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const AUTH_USER = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
};

const USD_QUOTE = {
    table: 'A',
    code: 'USD',
    rates: [
        {
            no: '074/A/NBP/2026',
            effectiveDate: '2026-04-17',
            mid: 4,
        },
    ],
};

const EUR_QUOTE = {
    table: 'A',
    code: 'EUR',
    rates: [
        {
            no: '075/A/NBP/2026',
            effectiveDate: '2026-04-18',
            mid: 4.5,
        },
    ],
};

test.beforeEach(async ({ page }) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, AUTH_USER);
});

test('switches kwitariusze list amounts between PLN, USD, and EUR', async ({ page }, testInfo) => {
    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json', async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(USD_QUOTE),
        });
    });

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json', async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(EUR_QUOTE),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Lista');
    const rateTypeButton = page.getByRole('button', { name: 'Rata + odsetki' });
    const resultsCount = page.locator('.results-count');
    const infoBanner = page.locator('.currency-banner--info');
    const firstTotal = page.locator('.cell-amount__total').first();
    const firstInterest = page.locator('.cell-amount__interest').first();

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');
    await expect(firstTotal).toContainText('328,50 PLN');
    await expect(page.locator('.currency-banner--info')).toHaveCount(0);
    await captureStep(page, testInfo, 'kwitariusze-default-pln');

    await rateTypeButton.click();

    const filteredCountText = ((await resultsCount.textContent()) ?? '').trim();

    await expect(filteredCountText).not.toBe('');
    await expect(rateTypeButton).toHaveClass(/filter-type--active/);
    await captureStep(page, testInfo, 'kwitariusze-filter-applied');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(resultsCount).toHaveText(filteredCountText);
    await expect(firstTotal).toContainText('82,13 USD');
    await expect(firstInterest).toContainText('odsetki: 4,63 USD');
    await expect(infoBanner).toContainText('Kurs NBP: 1 USD = 4,0000 PLN');
    await expect(infoBanner).toContainText('tabela A');
    await expect(infoBanner).toContainText('074/A/NBP/2026');
    await expect(infoBanner).toContainText('2026-04-17');
    await captureStep(page, testInfo, 'kwitariusze-usd-conversion');

    await currencySelect.selectOption('EUR');

    await expect(currencySelect).toHaveValue('EUR');
    await expect(resultsCount).toHaveText(filteredCountText);
    await expect(firstTotal).toContainText('73,00 EUR');
    await expect(firstInterest).toContainText('odsetki: 4,11 EUR');
    await expect(infoBanner).toContainText('Kurs NBP: 1 EUR = 4,5000 PLN');
    await expect(infoBanner).toContainText('075/A/NBP/2026');
    await expect(infoBanner).toContainText('2026-04-18');
    await captureStep(page, testInfo, 'kwitariusze-eur-conversion');
});

test('resets the list currency to PLN after leaving and reopening the screen', async ({ page }, testInfo) => {
    let usdRequests = 0;

    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json', async (route) => {
        usdRequests += 1;
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(USD_QUOTE),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Lista');
    const firstTotal = page.locator('.cell-amount__total').first();

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(firstTotal).toContainText('82,13 USD');
    await expect(page.locator('.currency-banner--info')).toContainText('1 USD = 4,0000 PLN');
    await captureStep(page, testInfo, 'kwitariusze-usd-before-reopen');

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');
    await expect(firstTotal).toContainText('328,50 PLN');
    await expect(page.locator('.currency-banner--info')).toHaveCount(0);
    await expect(usdRequests).toBe(1);
    await captureStep(page, testInfo, 'kwitariusze-reset-to-pln');
});

test('keeps the previous currency presentation when both EUR lookups fail', async ({ page }, testInfo) => {
    await page.route('https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json', async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify(USD_QUOTE),
        });
    });

    await page.route(/https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/EUR\/.*format=json/, async (route) => {
        await route.fulfill({
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify({ message: 'NBP unavailable' }),
        });
    });

    await page.goto('/');
    await page.goto('/rozliczenia/kwitariusze');

    const currencySelect = page.getByLabel('Lista');
    const firstTotal = page.locator('.cell-amount__total').first();
    const infoBanner = page.locator('.currency-banner--info');
    const errorBanner = page.locator('.currency-banner--error');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(firstTotal).toContainText('82,13 USD');
    await expect(infoBanner).toContainText('074/A/NBP/2026');
    await captureStep(page, testInfo, 'kwitariusze-usd-before-eur-error');

    await currencySelect.selectOption('EUR');

    await expect(errorBanner).toContainText('Nie udało się pobrać kursu NBP dla EUR');
    await expect(currencySelect).toHaveValue('USD');
    await expect(firstTotal).toContainText('82,13 USD');
    await expect(infoBanner).toContainText('Kurs NBP: 1 USD = 4,0000 PLN');
    await captureStep(page, testInfo, 'kwitariusze-eur-error-keeps-usd');
});
