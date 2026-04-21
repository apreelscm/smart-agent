import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const nbpUrl = /https:\/\/api\.nbp\.pl\/api\/exchangerates\/tables\/A\/\?format=json/;

const successfulRatesResponse = [
  {
    table: 'A',
    no: '079/A/NBP/2026',
    effectiveDate: '2026-04-20',
    rates: [
      { currency: 'euro', code: 'EUR', mid: 4.0 },
      { currency: 'dolar amerykański', code: 'USD', mid: 3.5 },
    ],
  },
];

function normalizeText(value: string | null): string {
  return (value ?? '').replace(/\s+/g, ' ').trim();
}

async function navigateWithinSpa(page: Parameters<typeof test>[0]['page'], path: string): Promise<void> {
  await page.evaluate((targetPath) => {
    window.history.pushState({}, '', targetPath);
    window.dispatchEvent(new PopStateEvent('popstate', { state: window.history.state }));
  }, path);
}

test('switches offers list amounts between PLN, EUR, and USD', async ({ page }, testInfo) => {
  await page.route(nbpUrl, async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(successfulRatesResponse),
    });
  });

  await page.goto('/');
  await navigateWithinSpa(page, '/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();

  const currencySwitcher = page.getByRole('group', {
    name: 'Przełącznik waluty listy ofert',
  });
  const plnButton = page.getByRole('button', { name: 'PLN', exact: true });
  const eurButton = page.getByRole('button', { name: 'EUR', exact: true });
  const usdButton = page.getByRole('button', { name: 'USD', exact: true });
  const firstPremium = page.locator('.premium-box strong').first();
  const averagePremiumTile = page
    .locator('app-stat-tile')
    .filter({ hasText: 'Średnia składka' })
    .first();
  const rateLabel = page.locator('.currency-feedback--rate');

  await expect(currencySwitcher).toBeVisible();
  await expect(currencySwitcher.getByRole('button')).toHaveCount(3);
  await expect(plnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(firstPremium).toContainText(/zł/);
  await expect(averagePremiumTile).toContainText(/zł/);
  await expect(rateLabel).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-default-pln');

  const plnPremiumText = normalizeText(await firstPremium.textContent());
  const plnAverageTileText = normalizeText(await averagePremiumTile.textContent());

  await eurButton.click();

  await expect(eurButton).toHaveAttribute('aria-pressed', 'true');
  await expect(firstPremium).toContainText(/€/);
  await expect(averagePremiumTile).toContainText(/€/);
  await expect(rateLabel).toContainText('kurs 4,00 PLN/EUR z 20.04.2026');

  const eurPremiumText = normalizeText(await firstPremium.textContent());
  const eurAverageTileText = normalizeText(await averagePremiumTile.textContent());

  expect(eurPremiumText).not.toBe(plnPremiumText);
  expect(eurAverageTileText).not.toBe(plnAverageTileText);
  await captureStep(page, testInfo, 'offers-eur-selected');

  await usdButton.click();

  await expect(usdButton).toHaveAttribute('aria-pressed', 'true');
  await expect(firstPremium).toContainText(/\$/);
  await expect(averagePremiumTile).toContainText(/\$/);
  await expect(rateLabel).toContainText('kurs 3,50 PLN/USD z 20.04.2026');

  const usdPremiumText = normalizeText(await firstPremium.textContent());
  const usdAverageTileText = normalizeText(await averagePremiumTile.textContent());

  expect(usdPremiumText).not.toBe(eurPremiumText);
  expect(usdAverageTileText).not.toBe(eurAverageTileText);
  await captureStep(page, testInfo, 'offers-usd-selected');
});

test('keeps PLN usable and disables foreign currencies when NBP is unavailable', async ({
  page,
}, testInfo) => {
  await page.route(nbpUrl, async (route) => {
    await route.fulfill({
      status: 503,
      contentType: 'text/plain',
      body: 'Service Unavailable',
    });
  });

  await page.goto('/');
  await navigateWithinSpa(page, '/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();

  const plnButton = page.getByRole('button', { name: 'PLN', exact: true });
  const eurButton = page.getByRole('button', { name: 'EUR', exact: true });
  const usdButton = page.getByRole('button', { name: 'USD', exact: true });
  const warningMessage = page.locator('.currency-feedback--warning');
  const firstPremium = page.locator('.premium-box strong').first();

  await expect(plnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(eurButton).toBeDisabled();
  await expect(usdButton).toBeDisabled();
  await expect(warningMessage).toContainText('Nie udało się pobrać kursów NBP');
  await expect(firstPremium).toContainText(/zł/);
  await captureStep(page, testInfo, 'offers-rates-unavailable');

  await expect(page.locator('.currency-feedback--rate')).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-pln-fallback');
});

test('resets the currency switcher to PLN after leaving and re-entering offers', async (
  { page },
  testInfo,
) => {
  await page.route(nbpUrl, async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify(successfulRatesResponse),
    });
  });

  const eurButton = page.getByRole('button', { name: 'EUR', exact: true });
  const plnButton = page.getByRole('button', { name: 'PLN', exact: true });
  const rateLabel = page.locator('.currency-feedback--rate');
  const firstPremium = page.locator('.premium-box strong').first();

  await page.goto('/');
  await navigateWithinSpa(page, '/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();

  await eurButton.click();
  await expect(eurButton).toHaveAttribute('aria-pressed', 'true');
  await expect(rateLabel).toContainText('kurs 4,00 PLN/EUR z 20.04.2026');
  await captureStep(page, testInfo, 'offers-before-leaving-eur');

  await navigateWithinSpa(page, '/policies');
  await navigateWithinSpa(page, '/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(plnButton).toHaveAttribute('aria-pressed', 'true');
  await expect(page.locator('.currency-feedback--rate')).toHaveCount(0);
  await expect(firstPremium).toContainText(/zł/);
  await captureStep(page, testInfo, 'offers-after-return-pln');
});
