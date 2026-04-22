import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers page defaults to PLN and exposes only PLN, EUR and USD', async ({ page }, testInfo) => {
  let rateRequests = 0;

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/a\/(EUR|USD)\/\?format=json/,
    async (route) => {
      rateRequests += 1;
      await route.abort();
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-default-load');

  const currencySelect = page.locator('.toolbar__select--currency');
  await expect(currencySelect).toBeVisible();
  await expect(currencySelect).toContainText('PLN');

  await currencySelect.click();
  await expect(page.getByRole('option')).toHaveText(['PLN', 'EUR', 'USD']);
  await captureStep(page, testInfo, 'currency-selector-options');
  await page.keyboard.press('Escape');

  await expect(page.locator('.rate-info')).toHaveCount(0);

  const premiums = page.locator('.premium-box strong');
  const premiumTexts = (await premiums.allTextContents()).map((text) =>
    text.replace(/\s+/g, ' ').trim(),
  );

  expect(premiumTexts.length).toBeGreaterThan(0);
  for (const premiumText of premiumTexts) {
    expect(premiumText).toContain('zł');
  }

  await expect(page.locator('.stat-tile__value').nth(2)).toContainText('zł');
  expect(rateRequests).toBe(0);
  await captureStep(page, testInfo, 'pln-premiums-and-summary');
});

test('switching to EUR recalculates visible premiums and returning to PLN hides rate info', async (
  { page },
  testInfo,
) => {
  let eurRequests = 0;

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/a\/EUR\/\?format=json/,
    async (route) => {
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
              effectiveDate: '2026-04-17',
              mid: 4.25,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-before-eur-switch');

  const currencySelect = page.locator('.toolbar__select--currency');
  await currencySelect.click();
  await page.getByRole('option', { name: 'EUR' }).click();

  const rateInfo = page.locator('.rate-info');
  await expect(rateInfo).toBeVisible();
  await expect(rateInfo).toContainText('Kurs EUR');
  await expect(rateInfo).toContainText('1 EUR = 4,25 PLN');
  await expect(rateInfo).toContainText('source: service');
  await expect(rateInfo).toContainText('data: 2026-04-17');
  await expect(currencySelect).toContainText('EUR');

  const eurPremiums = page.locator('.premium-box strong');
  await expect(eurPremiums.first()).toContainText('€');

  const eurPremiumTexts = (await eurPremiums.allTextContents()).map((text) =>
    text.replace(/\s+/g, ' ').trim(),
  );

  expect(eurPremiumTexts.length).toBeGreaterThan(0);
  for (const premiumText of eurPremiumTexts) {
    expect(premiumText).toContain('€');
  }

  await expect(page.locator('.stat-tile__value').nth(2)).toContainText('€');
  expect(eurRequests).toBe(1);
  await captureStep(page, testInfo, 'eur-conversion-visible');

  await currencySelect.click();
  await page.getByRole('option', { name: 'PLN' }).click();

  await expect(page.locator('.rate-info')).toHaveCount(0);
  await expect(currencySelect).toContainText('PLN');

  const plnPremiums = page.locator('.premium-box strong');
  await expect(plnPremiums.first()).toContainText('zł');

  const plnPremiumTexts = (await plnPremiums.allTextContents()).map((text) =>
    text.replace(/\s+/g, ' ').trim(),
  );

  expect(plnPremiumTexts.length).toBeGreaterThan(0);
  for (const premiumText of plnPremiumTexts) {
    expect(premiumText).toContain('zł');
  }

  await expect(page.locator('.stat-tile__value').nth(2)).toContainText('zł');
  await captureStep(page, testInfo, 'returned-to-pln');
});

test('switching to USD falls back to the configured fallback rate when the service data is invalid', async (
  { page },
  testInfo,
) => {
  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/a\/USD\/\?format=json/,
    async (route) => {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              mid: 0,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const currencySelect = page.locator('.toolbar__select--currency');
  await currencySelect.click();
  await page.getByRole('option', { name: 'USD' }).click();

  const rateInfo = page.locator('.rate-info');
  await expect(rateInfo).toBeVisible();
  await expect(rateInfo).toContainText('Kurs USD');
  await expect(rateInfo).toContainText('1 USD = 3,6 PLN');
  await expect(rateInfo).toContainText('source: fallback');
  await expect(rateInfo).not.toContainText('data:');
  await expect(currencySelect).toContainText('USD');

  const usdPremiums = page.locator('.premium-box strong');
  await expect(usdPremiums.first()).toContainText('$');

  const usdPremiumTexts = (await usdPremiums.allTextContents()).map((text) =>
    text.replace(/\s+/g, ' ').trim(),
  );

  expect(usdPremiumTexts.length).toBeGreaterThan(0);
  for (const premiumText of usdPremiumTexts) {
    expect(premiumText).toContain('$');
  }

  await expect(page.locator('.stat-tile__value').nth(2)).toContainText('$');
  await captureStep(page, testInfo, 'usd-fallback-rate-visible');
});
