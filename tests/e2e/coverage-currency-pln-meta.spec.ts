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

const nbpRatesResponse = [
  {
    table: 'A',
    no: '074/A/NBP/2026',
    effectiveDate: '2026-05-20',
    rates: [
      {
        currency: 'dolar amerykański',
        code: 'USD',
        mid: 3,
      },
      {
        currency: 'euro',
        code: 'EUR',
        mid: 4.5,
      },
    ],
  },
];

const todayRatesUrl = /\/api\/exchangerates\/tables\/A\/today\/\?format=json$/;
const fallbackRatesUrl = /\/api\/exchangerates\/tables\/A\/\d{4}-\d{2}-\d{2}\/\?format=json$/;

test.beforeEach(async ({ page }) => {
  await page.addInitScript(user => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);
});

test('hides exchange metadata for PLN and shows it for foreign currencies', async (
  { page },
  testInfo,
) => {
  await page.route(todayRatesUrl, async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(nbpRatesResponse),
    });
  });

  await page.route(fallbackRatesUrl, async route => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(nbpRatesResponse),
    });
  });

  await page.goto('/kalkulator/zakres');

  const currencySelect = page.getByLabel('Waluta prezentacji składek');
  const exchangeMeta = page.locator('.currency-meta');
  const loadingHint = page.locator('.currency-hint');
  const totalPrice = page.locator('.price-total');

  await expect(currencySelect).toBeVisible();
  await expect(loadingHint).toBeHidden();
  await expect(currencySelect).toHaveValue('PLN');
  await expect(exchangeMeta).toBeHidden();
  await expect(totalPrice).toHaveText('1 046,00 PLN');
  await captureStep(page, testInfo, 'pln-default-meta-hidden');

  await currencySelect.selectOption('USD');

  await expect(currencySelect).toHaveValue('USD');
  await expect(exchangeMeta).toBeVisible();
  await expect(exchangeMeta).toContainText('Wybrana waluta');
  await expect(exchangeMeta).toContainText('USD');
  await expect(exchangeMeta).toContainText('1 USD = 3,0000 PLN');
  await expect(exchangeMeta).toContainText('A / 074/A/NBP/2026');
  await expect(exchangeMeta).toContainText('20.05.2026');
  await expect(totalPrice).toHaveText('348,67 USD');
  await captureStep(page, testInfo, 'usd-meta-visible');

  await currencySelect.selectOption('EUR');

  await expect(currencySelect).toHaveValue('EUR');
  await expect(exchangeMeta).toBeVisible();
  await expect(exchangeMeta).toContainText('EUR');
  await expect(exchangeMeta).toContainText('1 EUR = 4,5000 PLN');
  await expect(totalPrice).toHaveText('232,44 EUR');
  await captureStep(page, testInfo, 'eur-meta-visible');

  await currencySelect.selectOption('PLN');

  await expect(currencySelect).toHaveValue('PLN');
  await expect(exchangeMeta).toBeHidden();
  await expect(totalPrice).toHaveText('1 046,00 PLN');
  await captureStep(page, testInfo, 'pln-meta-hidden-after-return');
});

test('keeps PLN active and shows the technical message when rates are unavailable', async (
  { page },
  testInfo,
) => {
  await page.route(todayRatesUrl, async route => {
    await route.fulfill({
      status: 500,
      contentType: 'application/json',
      body: JSON.stringify({ message: 'NBP unavailable' }),
    });
  });

  await page.route(fallbackRatesUrl, async route => {
    await route.fulfill({
      status: 500,
      contentType: 'application/json',
      body: JSON.stringify({ message: 'NBP unavailable' }),
    });
  });

  await page.goto('/kalkulator/zakres');

  const currencySelect = page.getByLabel('Waluta prezentacji składek');
  const exchangeMeta = page.locator('.currency-meta');
  const errorMessage = page.locator('.currency-error');
  const usdOption = page.locator('option[value="USD"]');
  const eurOption = page.locator('option[value="EUR"]');

  await expect(currencySelect).toBeVisible();
  await expect(currencySelect).toHaveValue('PLN');
  await expect(usdOption).toBeDisabled();
  await expect(eurOption).toBeDisabled();
  await expect(errorMessage).toContainText(
    'Kursy walut są obecnie niedostępne. Prezentujemy kwoty wyłącznie w PLN.',
  );
  await expect(exchangeMeta).toBeHidden();
  await captureStep(page, testInfo, 'rates-unavailable-pln-fallback');
});
