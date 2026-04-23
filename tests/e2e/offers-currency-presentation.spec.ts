import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('converts visible offer premiums to EUR and restores PLN after clearing selection', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json', async (route) => {
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

  await page.goto('/');

  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  let onOffersPage = true;

  try {
    await offersHeading.waitFor({ state: 'visible', timeout: 3000 });
  } catch {
    onOffersPage = false;
  }

  if (!onOffersPage) {
    await page.goto('/offers');
  }

  await expect(offersHeading).toBeVisible();
  await expect(page.getByText('Waluta prezentacji składki')).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const initialPremiums = await page.locator('.premium-box strong').allTextContents();
  expect(initialPremiums.length).toBeGreaterThan(0);

  for (const premium of initialPremiums) {
    expect(premium).toContain('zł');
  }

  await captureStep(page, testInfo, 'offers-list-default-pln');

  await page.locator('.toolbar__currency-control .p-select').first().click();
  await page.getByText('EUR', { exact: true }).click();

  await expect(page.locator('.results-meta__rate')).toBeVisible();
  await expect(page.locator('.results-meta__rate')).toContainText('1 EUR = 4,2941 PLN');
  await expect(page.locator('.results-meta__rate')).toContainText('17.04.2026');

  const eurPremiums = await page.locator('.premium-box strong').allTextContents();
  expect(eurPremiums).toHaveLength(initialPremiums.length);

  for (const [index, premium] of eurPremiums.entries()) {
    expect(premium).toContain('€');
    expect(premium).not.toContain('zł');
    expect(premium).not.toBe(initialPremiums[index]);
  }

  await captureStep(page, testInfo, 'offers-list-eur-conversion');

  await expect(page.locator('.toolbar__currency-control .p-select-clear-icon').first()).toBeVisible();
  await page.locator('.toolbar__currency-control .p-select-clear-icon').first().click();

  await expect(page.locator('.results-meta__rate')).toHaveCount(0);
  await expect(page.locator('.currency-error-banner')).toHaveCount(0);

  const restoredPremiums = await page.locator('.premium-box strong').allTextContents();
  expect(restoredPremiums).toEqual(initialPremiums);

  for (const premium of restoredPremiums) {
    expect(premium).toContain('zł');
  }

  await captureStep(page, testInfo, 'offers-list-restored-pln');
});

test('shows an error and keeps PLN premiums when the exchange-rate request fails', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json', async (route) => {
    await route.fulfill({
      status: 503,
      contentType: 'application/json',
      body: JSON.stringify({
        message: 'Service unavailable',
      }),
    });
  });

  await page.goto('/');

  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  let onOffersPage = true;

  try {
    await offersHeading.waitFor({ state: 'visible', timeout: 3000 });
  } catch {
    onOffersPage = false;
  }

  if (!onOffersPage) {
    await page.goto('/offers');
  }

  await expect(offersHeading).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();

  const initialPremiums = await page.locator('.premium-box strong').allTextContents();
  expect(initialPremiums.length).toBeGreaterThan(0);

  for (const premium of initialPremiums) {
    expect(premium).toContain('zł');
  }

  await captureStep(page, testInfo, 'offers-list-before-usd-error');

  await page.locator('.toolbar__currency-control .p-select').first().click();
  await page.getByText('USD', { exact: true }).click();

  await expect(page.locator('.currency-error-banner')).toBeVisible();
  await expect(page.locator('.currency-error-banner')).toContainText(
    'Nie udało się pobrać kursu USD. Wyświetlamy składki w PLN.',
  );
  await expect(page.locator('.results-meta__rate')).toHaveCount(0);

  const premiumsAfterFailure = await page.locator('.premium-box strong').allTextContents();
  expect(premiumsAfterFailure).toEqual(initialPremiums);

  for (const premium of premiumsAfterFailure) {
    expect(premium).toContain('zł');
    expect(premium).not.toContain('$');
  }

  await captureStep(page, testInfo, 'offers-list-usd-error');
});
