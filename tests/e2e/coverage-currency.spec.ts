import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

async function openCoverageStep(page) {
  await page.goto('/');
  await page.goto('/kalkulator/zakres');

  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
}

test('coverage step defaults to PLN, converts to USD, and restores PLN values', async (
  { page },
  testInfo,
) => {
  let nbpRequests = 0;

  await page.route('https://api.nbp.pl/**', async route => {
    nbpRequests += 1;

    const url = new URL(route.request().url());
    const path = url.pathname;

    if (path.endsWith('/USD/')) {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              no: '100/A/NBP/2026',
              effectiveDate: '2026-04-17',
              mid: 4.1234,
            },
          ],
        }),
      });
      return;
    }

    await route.abort();
  });

  await openCoverageStep(page);

  const currencySelect = page.getByLabel('Waluta prezentacji');
  const ocPrice = page.locator('.cov-card').filter({ hasText: 'OC' }).locator('.cov-price');
  const szybyPrice = page.locator('.cov-card').filter({ hasText: 'Szyby' }).locator('.cov-price');
  const totalPrice = page.locator('.price-total');

  await expect(currencySelect).toHaveValue('PLN');
  await expect(ocPrice).toHaveText('289.00 PLN');
  await expect(szybyPrice).toHaveText('150.00 PLN');
  await expect(totalPrice).toHaveText('1046.00 PLN');

  const optionTexts = await currencySelect.locator('option').allTextContents();
  expect(optionTexts).toEqual(['PLN', 'USD', 'EUR']);

  await captureStep(page, testInfo, 'coverage-default-pln');

  await currencySelect.selectOption('USD');

  await expect(currencySelect).toHaveValue('USD');
  await expect(ocPrice).toHaveText('70.08 USD');
  await expect(szybyPrice).toHaveText('36.38 USD');
  await expect(totalPrice).toHaveText('253.67 USD');
  await expect(page.getByText('Kurs NBP z dnia 2026-04-17')).toBeVisible();
  await expect.poll(() => nbpRequests).toBe(1);

  await captureStep(page, testInfo, 'coverage-usd-conversion');

  await currencySelect.selectOption('PLN');

  await expect(currencySelect).toHaveValue('PLN');
  await expect(ocPrice).toHaveText('289.00 PLN');
  await expect(szybyPrice).toHaveText('150.00 PLN');
  await expect(totalPrice).toHaveText('1046.00 PLN');
  expect(nbpRequests).toBe(1);

  await captureStep(page, testInfo, 'coverage-restored-pln');
});

test('coverage step uses fallback NBP rate when current USD rate is unavailable', async (
  { page },
  testInfo,
) => {
  let nbpRequests = 0;

  await page.route('https://api.nbp.pl/**', async route => {
    nbpRequests += 1;

    const url = new URL(route.request().url());
    const path = url.pathname;

    if (path.endsWith('/USD/')) {
      await route.fulfill({
        status: 404,
        contentType: 'text/plain',
        body: 'Not Found',
      });
      return;
    }

    if (/\/USD\/\d{4}-\d{2}-\d{2}\/$/.test(path)) {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              no: '099/A/NBP/2026',
              effectiveDate: '2026-04-17',
              mid: 4.1234,
            },
          ],
        }),
      });
      return;
    }

    await route.abort();
  });

  await openCoverageStep(page);

  const currencySelect = page.getByLabel('Waluta prezentacji');
  const totalPrice = page.locator('.price-total');

  await currencySelect.selectOption('USD');

  await expect(currencySelect).toHaveValue('USD');
  await expect(totalPrice).toHaveText('253.67 USD');
  await expect(page.getByText('Kurs NBP z dnia 2026-04-17')).toBeVisible();
  await expect.poll(() => nbpRequests).toBe(2);

  await captureStep(page, testInfo, 'coverage-usd-fallback-rate');
});
