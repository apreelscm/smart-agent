import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches offers list amounts to EUR and resets to PLN after refresh', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A?format=json', async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify([
        {
          table: 'A',
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          rates: [
            { currency: 'euro', code: 'EUR', mid: 4.0 },
            { currency: 'dolar amerykański', code: 'USD', mid: 3.5 },
          ],
        },
      ]),
    });
  });

  await page.goto('/');
  await page.waitForLoadState('networkidle');

  const initialCurrencyGroup = page.getByRole('group', { name: 'Waluta prezentacji' });
  if ((await initialCurrencyGroup.count()) === 0) {
    await page.goto('/offers');
    await page.waitForLoadState('networkidle');
  }

  const currencyGroup = page.getByRole('group', { name: 'Waluta prezentacji' });
  const plnButton = currencyGroup.getByRole('button', { name: 'PLN' });
  const eurButton = currencyGroup.getByRole('button', { name: 'EUR' });
  const usdButton = currencyGroup.getByRole('button', { name: 'USD' });
  const premiumValue = page.locator('.premium-box strong').first();
  const averagePremiumTile = page.locator('app-stat-tile').filter({ hasText: 'Średnia składka' });
  const eurRateCaption = page.getByText(/Kurs NBP:\s*1 EUR = 4,0000 PLN · 17\.04\.2026/);

  await expect(currencyGroup).toBeVisible();
  await expect(plnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(eurButton).toBeEnabled();
  await expect(usdButton).toBeEnabled();
  await expect(premiumValue).toBeVisible();
  await expect(premiumValue).toContainText('zł');
  await expect(averagePremiumTile).toContainText('Średnia składka');
  await expect(averagePremiumTile).toContainText('zł');
  await captureStep(page, testInfo, 'offers-default-pln');

  await eurButton.click();

  await expect(eurButton).toHaveAttribute('aria-pressed', 'true');
  await expect(premiumValue).toContainText('€');
  await expect(averagePremiumTile).toContainText('€');
  await expect(eurRateCaption).toBeVisible();
  await captureStep(page, testInfo, 'offers-converted-to-eur');

  await page.reload();
  await page.waitForLoadState('networkidle');

  const reloadedCurrencyGroup = page.getByRole('group', { name: 'Waluta prezentacji' });
  const reloadedPlnButton = reloadedCurrencyGroup.getByRole('button', { name: 'PLN' });
  const reloadedEurButton = reloadedCurrencyGroup.getByRole('button', { name: 'EUR' });
  const reloadedPremiumValue = page.locator('.premium-box strong').first();
  const reloadedAveragePremiumTile = page.locator('app-stat-tile').filter({
    hasText: 'Średnia składka',
  });

  await expect(reloadedCurrencyGroup).toBeVisible();
  await expect(reloadedPlnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(reloadedEurButton).toBeEnabled();
  await expect(reloadedPremiumValue).toContainText('zł');
  await expect(reloadedAveragePremiumTile).toContainText('zł');
  await expect(eurRateCaption).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-reset-to-pln-after-refresh');
});

test('keeps PLN active and disables foreign currencies when FX service is unavailable', async ({
  page,
}, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A?format=json', async (route) => {
    await route.fulfill({
      status: 503,
      contentType: 'application/json',
      body: JSON.stringify({
        message: 'Service unavailable',
      }),
    });
  });

  await page.goto('/');
  await page.waitForLoadState('networkidle');

  const initialCurrencyGroup = page.getByRole('group', { name: 'Waluta prezentacji' });
  if ((await initialCurrencyGroup.count()) === 0) {
    await page.goto('/offers');
    await page.waitForLoadState('networkidle');
  }

  const currencyGroup = page.getByRole('group', { name: 'Waluta prezentacji' });
  const plnButton = currencyGroup.getByRole('button', { name: 'PLN' });
  const eurButton = currencyGroup.getByRole('button', { name: 'EUR' });
  const usdButton = currencyGroup.getByRole('button', { name: 'USD' });
  const premiumValue = page.locator('.premium-box strong').first();
  const averagePremiumTile = page.locator('app-stat-tile').filter({ hasText: 'Średnia składka' });
  const unavailableMessage = page.getByText(
    'Kursy EUR i USD są obecnie niedostępne. Ceny pozostają w PLN.',
  );

  await expect(currencyGroup).toBeVisible();
  await expect(plnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(eurButton).toBeDisabled();
  await expect(usdButton).toBeDisabled();
  await expect(unavailableMessage).toBeVisible();
  await expect(premiumValue).toContainText('zł');
  await expect(averagePremiumTile).toContainText('zł');
  await captureStep(page, testInfo, 'offers-fx-unavailable');
});
