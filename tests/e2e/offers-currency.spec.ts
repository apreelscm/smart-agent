import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches visible offer premiums from PLN to EUR and shows the applied rate', async ({ page }, testInfo) => {
  await page.route(
    'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json',
    async (route) => {
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
              mid: 4,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/offers');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.locator('.premium-box strong').first()).toContainText('zł');
  await captureStep(page, testInfo, 'offers-default-pln');

  const selectPresentationCurrency = async (currency: 'EUR' | 'USD') => {
    await page.locator('.toolbar__presentation .p-select').click();
    await expect(page.getByRole('option', { name: currency, exact: true })).toBeVisible();
    await page.getByRole('option', { name: currency, exact: true }).click();
  };

  await selectPresentationCurrency('EUR');

  const rateBanner = page.locator('.presentation-meta__rate');
  await expect(rateBanner).toBeVisible();
  await expect(rateBanner).toContainText('1 EUR = 4,00 PLN');
  await expect(rateBanner).toContainText('17.04.2026');
  await expect(page.locator('.presentation-meta__error')).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-eur-rate-banner');

  const premiumValues = page.locator('.premium-box strong');
  const premiumCount = await premiumValues.count();

  for (let index = 0; index < premiumCount; index += 1) {
    await expect(premiumValues.nth(index)).toContainText('€');
  }

  await captureStep(page, testInfo, 'offers-eur-premiums');
});

test('keeps the last successful currency presentation when the next rate request fails', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/rates/A/**', async (route) => {
    const requestUrl = route.request().url();

    if (requestUrl.includes('/EUR/')) {
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
              mid: 4,
            },
          ],
        }),
      });
      return;
    }

    if (requestUrl.includes('/USD/')) {
      await route.fulfill({
        status: 500,
        contentType: 'application/json',
        body: JSON.stringify({
          message: 'NBP unavailable',
        }),
      });
      return;
    }

    await route.continue();
  });

  await page.goto('/offers');

  const selectPresentationCurrency = async (currency: 'EUR' | 'USD') => {
    await page.locator('.toolbar__presentation .p-select').click();
    await expect(page.getByRole('option', { name: currency, exact: true })).toBeVisible();
    await page.getByRole('option', { name: currency, exact: true }).click();
  };

  await expect(page.locator('.premium-box strong').first()).toContainText('zł');
  await selectPresentationCurrency('EUR');

  const firstPremium = page.locator('.premium-box strong').first();
  await expect(firstPremium).toContainText('€');
  await expect(page.locator('.presentation-meta__rate')).toContainText('1 EUR = 4,00 PLN');
  const eurPremiumText = (await firstPremium.innerText()).trim();
  await captureStep(page, testInfo, 'offers-eur-before-usd-error');

  await selectPresentationCurrency('USD');

  const errorAlert = page.getByRole('alert');
  await expect(errorAlert).toBeVisible();
  await expect(errorAlert).toContainText('Nie udało się pobrać kursu USD z NBP');
  await captureStep(page, testInfo, 'offers-usd-error-alert');

  await expect(page.locator('.presentation-meta__rate')).toContainText('1 EUR = 4,00 PLN');
  await expect(firstPremium).toHaveText(eurPremiumText);
  await expect(firstPremium).toContainText('€');
  await captureStep(page, testInfo, 'offers-premium-kept-after-error');
});
