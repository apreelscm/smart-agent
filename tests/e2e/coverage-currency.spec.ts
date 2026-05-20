import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows PLN by default and converts visible prices for USD and EUR', async (
  { page },
  testInfo,
) => {
  let usdRequests = 0;
  let eurRequests = 0;

  await page.route(/https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/.*/, async (route) => {
    const url = route.request().url();

    if (url.endsWith('/USD/?format=json')) {
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
              no: '101/A/NBP/2026',
              effectiveDate: '2026-05-20',
              mid: 4,
            },
          ],
        }),
      });
      return;
    }

    if (url.endsWith('/EUR/?format=json')) {
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
              no: '102/A/NBP/2026',
              effectiveDate: '2026-05-20',
              mid: 5,
            },
          ],
        }),
      });
      return;
    }

    await route.abort();
  });

  await page.goto('/');
  await page.goto('/kalkulator/zakres');

  const plnButton = page.getByRole('button', { name: 'PLN' });
  const usdButton = page.getByRole('button', { name: 'USD' });
  const eurButton = page.getByRole('button', { name: 'EUR' });
  const promoCodeInput = page.getByPlaceholder('Wpisz łaskawy kod');
  const totalPrice = page.locator('.price-summary .price-total');
  const ocPrice = page
    .locator('.cov-card')
    .filter({ has: page.locator('.cov-label', { hasText: 'OC' }) })
    .first()
    .locator('.cov-price');

  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
  await expect(plnButton).toHaveClass(/active/);
  await expect(ocPrice).toHaveText('289 zł');
  await expect(totalPrice).toHaveText('289 zł');
  await expect(page.getByText(/Kurs NBP:/)).toHaveCount(0);
  await captureStep(page, testInfo, 'coverage-default-pln');

  await promoCodeInput.fill('PROMO2026');

  await usdButton.click();

  await expect(usdButton).toHaveClass(/active/);
  await expect(ocPrice).toHaveText('72.25 USD');
  await expect(totalPrice).toHaveText('72.25 USD');
  await expect(page.getByText('Kurs NBP: 1 USD = 4.0000 PLN')).toBeVisible();
  await expect(page.getByText('tabela 101/A/NBP/2026')).toBeVisible();
  await expect(page.getByText('data 2026-05-20')).toBeVisible();
  await captureStep(page, testInfo, 'coverage-usd-conversion');

  await plnButton.click();

  await expect(plnButton).toHaveClass(/active/);
  await expect(ocPrice).toHaveText('289 zł');
  await expect(totalPrice).toHaveText('289 zł');
  await expect(page.getByText(/Kurs NBP:/)).toHaveCount(0);
  await expect(promoCodeInput).toHaveValue('PROMO2026');
  await captureStep(page, testInfo, 'coverage-reset-pln');

  await usdButton.click();

  await expect(usdButton).toHaveClass(/active/);
  await expect(totalPrice).toHaveText('72.25 USD');
  expect(usdRequests).toBe(1);

  await eurButton.click();

  await expect(eurButton).toHaveClass(/active/);
  await expect(ocPrice).toHaveText('57.80 EUR');
  await expect(totalPrice).toHaveText('57.80 EUR');
  await expect(page.getByText('Kurs NBP: 1 EUR = 5.0000 PLN')).toBeVisible();
  await expect(page.getByText('tabela 102/A/NBP/2026')).toBeVisible();
  await expect(page.getByText('data 2026-05-20')).toBeVisible();
  expect(usdRequests).toBe(1);
  expect(eurRequests).toBe(1);
  await captureStep(page, testInfo, 'coverage-eur-conversion');
});

test('falls back to the previous day and blocks an unavailable currency', async (
  { page },
  testInfo,
) => {
  await page.route(/https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/.*/, async (route) => {
    const url = route.request().url();

    if (/\/EUR\/\?format=json$/.test(url)) {
      await route.fulfill({
        status: 404,
        contentType: 'text/plain',
        body: 'Not Found',
      });
      return;
    }

    if (/\/EUR\/\d{4}-\d{2}-\d{2}\/\?format=json$/.test(url)) {
      await route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'euro',
          code: 'EUR',
          rates: [
            {
              no: '100/A/NBP/2026',
              effectiveDate: '2026-05-19',
              mid: 4,
            },
          ],
        }),
      });
      return;
    }

    if (/\/USD\/\?format=json$/.test(url) || /\/USD\/\d{4}-\d{2}-\d{2}\/\?format=json$/.test(url)) {
      await route.fulfill({
        status: 404,
        contentType: 'text/plain',
        body: 'Not Found',
      });
      return;
    }

    await route.abort();
  });

  await page.goto('/');
  await page.goto('/kalkulator/zakres');

  const eurButton = page.getByRole('button', { name: 'EUR' });
  const usdButton = page.getByRole('button', { name: 'USD' });
  const totalPrice = page.locator('.price-summary .price-total');
  const ocPrice = page
    .locator('.cov-card')
    .filter({ has: page.locator('.cov-label', { hasText: 'OC' }) })
    .first()
    .locator('.cov-price');

  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();

  await eurButton.click();

  await expect(eurButton).toHaveClass(/active/);
  await expect(ocPrice).toHaveText('72.25 EUR');
  await expect(totalPrice).toHaveText('72.25 EUR');
  await expect(page.getByText('Kurs NBP: 1 EUR = 4.0000 PLN')).toBeVisible();
  await expect(page.getByText('tabela 100/A/NBP/2026')).toBeVisible();
  await expect(page.getByText('data 2026-05-19')).toBeVisible();
  await captureStep(page, testInfo, 'coverage-eur-fallback');

  await usdButton.click();

  await expect(page.getByText(/Waluta USD jest chwilowo niedostępna/)).toBeVisible();
  await expect(usdButton).toBeDisabled();
  await expect(eurButton).toHaveClass(/active/);
  await expect(totalPrice).toHaveText('72.25 EUR');
  await expect(page.getByText('data 2026-05-19')).toBeVisible();
  await captureStep(page, testInfo, 'coverage-usd-blocked');
});
