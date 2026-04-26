import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows PLN premiums and available currency options by default', async ({ page }, testInfo) => {
  await gotoOffersPage(page);

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const currencySelect = page.locator('.toolbar__select--currency');
  await expect(currencySelect).toContainText('PLN');

  await currencySelect.click();

  const currencyOptions = page.getByRole('option');
  await expect(currencyOptions).toHaveCount(3);
  await expect(currencyOptions.nth(0)).toHaveText('PLN');
  await expect(currencyOptions.nth(1)).toHaveText('EUR');
  await expect(currencyOptions.nth(2)).toHaveText('USD');
  await captureStep(page, testInfo, 'currency-options-open');

  await page.getByRole('option', { name: 'PLN', exact: true }).click();

  await expect(page.locator('.offer-row .premium-box strong').first()).toContainText('zł');
  await expect(page.locator('.results-meta__rate')).toHaveCount(0);
  await captureStep(page, testInfo, 'default-pln-premiums');
});

test('offers list converts visible premiums to EUR and restores PLN values', async ({ page }, testInfo) => {
  let eurRequests = 0;

  await page.route('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json', async (route) => {
    eurRequests += 1;
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
            mid: 4.2941,
          },
        ],
      }),
    });
  });

  await gotoOffersPage(page);

  const plnPremiums = await getVisiblePremiumAmounts(page);
  expect(plnPremiums.length).toBeGreaterThan(0);
  await captureStep(page, testInfo, 'pln-baseline');

  await selectPresentationCurrency(page, 'EUR');

  const currencySelect = page.locator('.toolbar__select--currency');
  const rateBanner = page.locator('.results-meta__rate');

  await expect(currencySelect).toContainText('EUR');
  await expect(rateBanner).toContainText('Kurs EUR');
  await expect(rateBanner).toContainText('1 EUR = 4,2941 PLN');
  await expect(rateBanner).toContainText('17.04.2026');
  await expect(page.locator('.offer-row .premium-box strong').first()).toContainText('€');

  const eurPremiums = await getVisiblePremiumAmounts(page);
  expect(eurPremiums).toEqual(plnPremiums.map((amount) => Math.round(amount / 4.2941)));
  await captureStep(page, testInfo, 'eur-converted');

  await selectPresentationCurrency(page, 'PLN');

  await expect(currencySelect).toContainText('PLN');
  await expect(page.locator('.results-meta__rate')).toHaveCount(0);
  await expect(page.locator('.offer-row .premium-box strong').first()).toContainText('zł');

  const restoredPremiums = await getVisiblePremiumAmounts(page);
  expect(restoredPremiums).toEqual(plnPremiums);
  expect(eurRequests).toBe(1);
  await captureStep(page, testInfo, 'pln-restored');
});

test('offers list keeps USD premiums unchanged when a later rate lookup fails', async ({ page }, testInfo) => {
  let usdRequests = 0;
  let eurRequests = 0;

  await page.route('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json', async (route) => {
    usdRequests += 1;
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
            mid: 3.85,
          },
        ],
      }),
    });
  });

  await page.route('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json', async (route) => {
    eurRequests += 1;
    await route.fulfill({
      status: 503,
      contentType: 'text/plain',
      body: 'NBP unavailable',
    });
  });

  await gotoOffersPage(page);

  const plnPremiums = await getVisiblePremiumAmounts(page);

  await selectPresentationCurrency(page, 'USD');

  const currencySelect = page.locator('.toolbar__select--currency');
  const rateBanner = page.locator('.results-meta__rate');
  const errorBanner = page.locator('.results-meta__error');

  await expect(currencySelect).toContainText('USD');
  await expect(rateBanner).toContainText('Kurs USD');
  await expect(rateBanner).toContainText('1 USD = 3,8500 PLN');
  await expect(page.locator('.offer-row .premium-box strong').first()).toContainText('$');

  const usdPremiums = await getVisiblePremiumAmounts(page);
  expect(usdPremiums).toEqual(plnPremiums.map((amount) => Math.round(amount / 3.85)));
  await captureStep(page, testInfo, 'usd-converted');

  await selectPresentationCurrency(page, 'EUR');

  await expect(errorBanner).toContainText('Nie udało się pobrać kursu EUR');
  await expect(currencySelect).toContainText('USD');
  await expect(rateBanner).toContainText('Kurs USD');

  const preservedPremiums = await getVisiblePremiumAmounts(page);
  expect(preservedPremiums).toEqual(usdPremiums);
  expect(usdRequests).toBe(1);
  expect(eurRequests).toBe(1);
  await captureStep(page, testInfo, 'lookup-failure-preserves-usd');
});

async function gotoOffersPage(page) {
  await page.goto('/');

  if (!/\/offers(?:$|[/?#])/.test(page.url())) {
    await page.goto('/offers');
  }

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
}

async function selectPresentationCurrency(page, currency: 'PLN' | 'EUR' | 'USD') {
  await page.locator('.toolbar__select--currency').click();
  await page.getByRole('option', { name: currency, exact: true }).click();
}

async function getVisiblePremiumAmounts(page) {
  const premiumTexts = await page.locator('.offer-row .premium-box strong').allTextContents();
  return premiumTexts.map((text) => parseDisplayedAmount(text));
}

function parseDisplayedAmount(value: string) {
  const digitsOnly = value.replace(/[^\d-]/g, '');
  return Number(digitsOnly);
}
