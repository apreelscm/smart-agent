import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches visible offer premiums from PLN to EUR and back', async ({ page }, testInfo) => {
  let eurRequests = 0;

  await page.route(
    new RegExp('https://api\\.nbp\\.pl/api/exchangerates/rates/A/EUR/\\?format=json'),
    async (route) => {
      eurRequests += 1;

      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        headers: {
          'access-control-allow-origin': '*',
        },
        body: JSON.stringify({
          table: 'A',
          currency: 'euro',
          code: 'EUR',
          rates: [
            {
              effectiveDate: '2026-04-17',
              mid: 4,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const currencySelect = page.getByRole('combobox', { name: 'Waluta składki' });
  const firstPremium = page.locator('.premium-box strong').first();
  const rateLabel = page.locator('.results-meta__rate');

  await expect(currencySelect).toContainText('PLN');
  await expect(firstPremium).toContainText('zł');
  await expect(rateLabel).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-default-pln');

  const plnPremiumText = await firstPremium.innerText();

  await currencySelect.click();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect(currencySelect).toContainText('EUR');
  await expect(firstPremium).toContainText('€');
  await expect(rateLabel).toHaveText(/Kurs NBP EUR z 17\.04\.2026: 1 EUR = 4,0000 PLN/);
  expect(await firstPremium.innerText()).not.toBe(plnPremiumText);
  await captureStep(page, testInfo, 'offers-eur-conversion');

  await currencySelect.click();
  await page.getByRole('option', { name: 'PLN' }).click();

  await expect(currencySelect).toContainText('PLN');
  await expect(firstPremium).toContainText('zł');
  await expect(rateLabel).toHaveCount(0);
  expect(eurRequests).toBe(1);
  await captureStep(page, testInfo, 'offers-back-to-pln');
});

test('keeps USD presentation for newly filtered visible offers', async ({ page }, testInfo) => {
  await page.route(
    new RegExp('https://api\\.nbp\\.pl/api/exchangerates/rates/A/USD/\\?format=json'),
    async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        headers: {
          'access-control-allow-origin': '*',
        },
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              effectiveDate: '2026-04-17',
              mid: 3.5,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const currencySelect = page.getByRole('combobox', { name: 'Waluta składki' });
  const rateLabel = page.locator('.results-meta__rate');
  const firstOfferNumber = page.locator('.offer-row__number span').first();
  const searchInput = page.locator(
    'input[placeholder="Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja"]',
  );

  const offerNumber = (await firstOfferNumber.textContent())?.trim() ?? '';

  expect(offerNumber).not.toBe('');

  await currencySelect.click();
  await page.getByRole('option', { name: 'USD' }).click();

  await expect(currencySelect).toContainText('USD');
  await expect(page.locator('.premium-box strong').first()).toContainText('$');
  await expect(rateLabel).toHaveText(/Kurs NBP USD z 17\.04\.2026: 1 USD = 3,5000 PLN/);
  await captureStep(page, testInfo, 'offers-usd-conversion');

  await searchInput.fill(offerNumber);

  await expect(page.locator('.offer-row')).toHaveCount(1);
  await expect(page.locator('.offer-row__number span').first()).toHaveText(offerNumber);
  await expect(page.locator('.premium-box strong').first()).toContainText('$');
  await expect(rateLabel).toContainText('Kurs NBP USD');
  await captureStep(page, testInfo, 'offers-usd-after-filter');
});

test('shows an error and keeps the last valid currency when rate loading fails', async ({
  page,
}, testInfo) => {
  await page.route(
    new RegExp('https://api\\.nbp\\.pl/api/exchangerates/rates/A/EUR/\\?format=json'),
    async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        headers: {
          'access-control-allow-origin': '*',
        },
        body: JSON.stringify({
          table: 'A',
          currency: 'euro',
          code: 'EUR',
          rates: [
            {
              effectiveDate: '2026-04-17',
              mid: 4,
            },
          ],
        }),
      });
    },
  );

  await page.route(
    new RegExp('https://api\\.nbp\\.pl/api/exchangerates/rates/A/USD/\\?format=json'),
    async (route) => {
      await route.fulfill({
        status: 503,
        contentType: 'text/plain',
        headers: {
          'access-control-allow-origin': '*',
        },
        body: 'NBP unavailable',
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const currencySelect = page.getByRole('combobox', { name: 'Waluta składki' });
  const firstPremium = page.locator('.premium-box strong').first();
  const rateLabel = page.locator('.results-meta__rate');
  const errorMessage = page.getByRole('alert');

  await currencySelect.click();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect(currencySelect).toContainText('EUR');
  await expect(firstPremium).toContainText('€');
  await expect(rateLabel).toContainText('Kurs NBP EUR');
  await captureStep(page, testInfo, 'offers-valid-eur-before-failure');

  const eurPremiumText = await firstPremium.innerText();

  await currencySelect.click();
  await page.getByRole('option', { name: 'USD' }).click();

  await expect(errorMessage).toContainText(
    'Nie udało się pobrać kursu waluty. Spróbuj ponownie.',
  );
  await expect(currencySelect).toContainText('EUR');
  await expect(rateLabel).toContainText('Kurs NBP EUR');
  expect(await firstPremium.innerText()).toBe(eurPremiumText);
  await captureStep(page, testInfo, 'offers-failed-usd-keeps-last-valid-state');
});
