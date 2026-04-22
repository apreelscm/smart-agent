import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches offer premiums, summary and dialog preview to EUR', async ({ page }, testInfo) => {
  const eurRateUrl = 'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json';
  let eurRequested = false;

  await page.route(
    (url) => url.href === eurRateUrl,
    async (route) => {
      eurRequested = true;
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

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const defaultCurrencyStatus = page.locator('.results-meta__currency-status');
  await expect(defaultCurrencyStatus).toContainText('Aktywna waluta: PLN');

  const premiumValues = page.locator('.premium-box strong');
  const firstPlnPremium = await premiumValues.first().innerText();
  expect(firstPlnPremium).toContain('zł');
  await captureStep(page, testInfo, 'default-pln-values');

  await page.locator('.toolbar__select--currency').click();
  await expect(page.getByRole('option', { name: 'EUR' })).toBeVisible();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect
    .poll(() => eurRequested, {
      message: 'Expected the page to request the documented EUR NBP endpoint',
    })
    .toBeTruthy();

  await expect(defaultCurrencyStatus).toContainText('Aktywna waluta: EUR');
  await expect(defaultCurrencyStatus).toContainText('17.04.2026');

  const eurPremiumTexts = await premiumValues.allInnerTexts();
  for (const premiumText of eurPremiumTexts) {
    expect(premiumText).toContain('€');
  }

  const firstEurPremium = eurPremiumTexts[0] ?? '';
  expect(firstEurPremium).not.toBe(firstPlnPremium);

  const averagePremiumValue = page
    .locator('app-stat-tile')
    .filter({ hasText: 'Średnia składka' })
    .locator('.stat-tile__value');
  await expect(averagePremiumValue).toContainText('€');
  await captureStep(page, testInfo, 'eur-values-applied');

  const rows = page.locator('.offer-row');
  const rowCount = await rows.count();
  let dialogOpened = false;
  let expectedDialogPremium = '';

  for (let index = 0; index < rowCount; index += 1) {
    const row = rows.nth(index);
    expectedDialogPremium = await row.locator('.premium-box strong').innerText();

    await row.locator('.p-splitbutton-dropdown').click();

    const cancelMenuItem = page.getByRole('menuitem', { name: 'Anuluj' });

    try {
      await cancelMenuItem.waitFor({ state: 'visible', timeout: 1000 });
      await cancelMenuItem.click();
      dialogOpened = true;
      break;
    } catch {
      await page.keyboard.press('Escape');
    }
  }

  expect(dialogOpened).toBeTruthy();

  const transitionDialog = page.locator('.transition-dialog');
  await expect(transitionDialog).toBeVisible();

  const dialogPremium = page
    .locator('.transition-dialog__summary div')
    .filter({ hasText: 'Składka' })
    .locator('strong');

  await expect(dialogPremium).toHaveText(expectedDialogPremium);
  await captureStep(page, testInfo, 'eur-dialog-preview');
});

test('keeps the last valid currency when USD rate loading fails', async ({ page }, testInfo) => {
  const eurRateUrl = 'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json';
  const usdRateUrl = 'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json';
  let eurRequested = false;
  let usdRequested = false;

  await page.route(
    (url) => url.href === eurRateUrl,
    async (route) => {
      eurRequested = true;
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

  await page.route(
    (url) => url.href === usdRateUrl,
    async (route) => {
      usdRequested = true;
      await route.fulfill({
        status: 500,
        contentType: 'application/json',
        body: JSON.stringify({
          message: 'NBP unavailable',
        }),
      });
    },
  );

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  await page.locator('.toolbar__select--currency').click();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect
    .poll(() => eurRequested, {
      message: 'Expected the page to request the documented EUR NBP endpoint',
    })
    .toBeTruthy();

  const currencyStatus = page.locator('.results-meta__currency-status');
  await expect(currencyStatus).toContainText('Aktywna waluta: EUR');

  const premiumValues = page.locator('.premium-box strong');
  const firstEurPremium = await premiumValues.first().innerText();
  expect(firstEurPremium).toContain('€');
  await captureStep(page, testInfo, 'eur-active-before-usd-failure');

  await page.locator('.toolbar__select--currency').click();
  await page.getByRole('option', { name: 'USD' }).click();

  await expect
    .poll(() => usdRequested, {
      message: 'Expected the page to request the documented USD NBP endpoint',
    })
    .toBeTruthy();

  await expect(currencyStatus).toContainText('Nie udało się pobrać kursu NBP dla USD');
  await expect(currencyStatus).toContainText('Pozostawiono EUR');

  const firstPremiumAfterFailure = await premiumValues.first().innerText();
  expect(firstPremiumAfterFailure).toBe(firstEurPremium);
  expect(firstPremiumAfterFailure).toContain('€');
  expect(firstPremiumAfterFailure).not.toContain('$');
  await captureStep(page, testInfo, 'usd-failure-keeps-eur');
});
