import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const TODAY_RATES_URL = 'https://api.nbp.pl/api/exchangerates/tables/A/today/?format=json';
const PREVIOUS_DAY_RATES_URL =
  /https:\/\/api\.nbp\.pl\/api\/exchangerates\/tables\/A\/\d{4}-\d{2}-\d{2}\/\?format=json/;

type MockRates = {
  tableNumber: string;
  effectiveDate: string;
  usd: number;
  eur: number;
};

test(
  'shows PLN by default and recalculates visible amounts after currency changes',
  async ({ page }, testInfo) => {
    await mockTodayRates(page, {
      tableNumber: '074/A/NBP/2026',
      effectiveDate: '2026-05-20',
      usd: 3,
      eur: 4.5,
    });

    await openCoverageStep(page);

    const currencySelect = page.getByLabel('Waluta prezentacji składek');
    const currencyOptions = currencySelect.locator('option');
    const metadataPanel = page.locator('.currency-meta');
    const priceSummary = page.locator('.price-summary');

    await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
    await expect(page).toHaveURL(/\/kalkulator\/zakres$/);
    await expect(currencySelect).toHaveValue('PLN');
    await expect(currencyOptions).toHaveCount(3);
    await expect(currencyOptions.nth(0)).toHaveText('PLN');
    await expect(currencyOptions.nth(1)).toHaveText('USD');
    await expect(currencyOptions.nth(2)).toHaveText('EUR');
    await expect(metadataPanel).toContainText('A / 074/A/NBP/2026');
    await expect(metadataPanel).toContainText('20.05.2026');
    await expect(metadataPanel).toContainText('1 PLN = 1,0000 PLN');
    await expect(priceSummary).toContainText(/1\s046,00 PLN/);
    await captureStep(page, testInfo, 'coverage-default-pln');

    await currencySelect.selectOption('USD');

    await expect(currencySelect).toHaveValue('USD');
    await expect(metadataPanel).toContainText('Wybrana waluta');
    await expect(metadataPanel).toContainText('USD');
    await expect(metadataPanel).toContainText('1 USD = 3,0000 PLN');
    await expect(coveragePrice(page, 'OC')).toHaveText('96,33 USD');
    await expect(coveragePrice(page, 'AC')).toHaveText('129,67 USD');
    await expect(coveragePrice(page, 'Szyby')).toHaveText('50,00 USD');
    await expect(priceSummary).toContainText('348,67 USD');
    await expect(page).toHaveURL(/\/kalkulator\/zakres$/);
    await captureStep(page, testInfo, 'coverage-usd-view');

    await currencySelect.selectOption('EUR');

    await expect(currencySelect).toHaveValue('EUR');
    await expect(metadataPanel).toContainText('EUR');
    await expect(metadataPanel).toContainText('1 EUR = 4,5000 PLN');
    await expect(coveragePrice(page, 'OC')).toHaveText('64,22 EUR');
    await expect(coveragePrice(page, 'AC')).toHaveText('86,44 EUR');
    await expect(coveragePrice(page, 'Szyby')).toHaveText('33,33 EUR');
    await expect(priceSummary).toContainText('232,44 EUR');
    await captureStep(page, testInfo, 'coverage-eur-view');
  },
);

test(
  'uses the previous-day NBP table when today rates are unavailable',
  async ({ page }, testInfo) => {
    const previousDay = getPreviousDayIso();

    await mockPreviousDayFallback(page, {
      tableNumber: '073/A/NBP/2026',
      effectiveDate: previousDay,
      usd: 3.81,
      eur: 4.25,
    });

    await openCoverageStep(page);

    const currencySelect = page.getByLabel('Waluta prezentacji składek');
    const metadataPanel = page.locator('.currency-meta');
    const priceSummary = page.locator('.price-summary');

    await expect(metadataPanel).toContainText('A / 073/A/NBP/2026');
    await expect(metadataPanel).toContainText(formatPolishDate(previousDay));
    await expect(currencySelect).toHaveValue('PLN');
    await captureStep(page, testInfo, 'coverage-fallback-loaded');

    await currencySelect.selectOption('EUR');

    await expect(currencySelect).toHaveValue('EUR');
    await expect(metadataPanel).toContainText('1 EUR = 4,2500 PLN');
    await expect(coveragePrice(page, 'OC')).toHaveText('68,00 EUR');
    await expect(coveragePrice(page, 'AC')).toHaveText('91,53 EUR');
    await expect(priceSummary).toContainText('246,12 EUR');
    await captureStep(page, testInfo, 'coverage-fallback-eur');
  },
);

test(
  'keeps PLN active and shows a technical message when rates cannot be loaded',
  async ({ page }, testInfo) => {
    await mockUnavailableRates(page);

    await openCoverageStep(page);

    const currencySelect = page.getByLabel('Waluta prezentacji składek');
    const errorMessage = page.getByText(
      'Kursy walut są obecnie niedostępne. Prezentujemy kwoty wyłącznie w PLN.',
    );
    const priceSummary = page.locator('.price-summary');

    await expect(errorMessage).toBeVisible();
    await expect(currencySelect).toHaveValue('PLN');
    await expect(currencySelect.locator('option[value="USD"]')).toBeDisabled();
    await expect(currencySelect.locator('option[value="EUR"]')).toBeDisabled();
    await expect(page.locator('.currency-meta')).toHaveCount(0);
    await expect(priceSummary).toContainText(/1\s046,00 PLN/);
    await captureStep(page, testInfo, 'coverage-rates-unavailable');
  },
);

function coveragePrice(page: Parameters<typeof test>[0]['page'], label: string) {
  return page.locator('.cov-card').filter({ hasText: label }).locator('.cov-price').first();
}

async function openCoverageStep(page: Parameters<typeof test>[0]['page']) {
  await page.goto('/');
  await page.goto('/kalkulator/zakres');
  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
}

async function mockTodayRates(
  page: Parameters<typeof test>[0]['page'],
  rates: MockRates,
) {
  await page.route(TODAY_RATES_URL, async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: buildRatesResponse(rates),
    });
  });
}

async function mockPreviousDayFallback(
  page: Parameters<typeof test>[0]['page'],
  rates: MockRates,
) {
  await page.route(TODAY_RATES_URL, async (route) => {
    await route.fulfill({
      status: 404,
      contentType: 'application/json',
      body: 'Not Found',
    });
  });

  await page.route(PREVIOUS_DAY_RATES_URL, async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: buildRatesResponse(rates),
    });
  });
}

async function mockUnavailableRates(page: Parameters<typeof test>[0]['page']) {
  await page.route(TODAY_RATES_URL, async (route) => {
    await route.fulfill({
      status: 404,
      contentType: 'application/json',
      body: 'Not Found',
    });
  });

  await page.route(PREVIOUS_DAY_RATES_URL, async (route) => {
    await route.fulfill({
      status: 404,
      contentType: 'application/json',
      body: 'Not Found',
    });
  });
}

function buildRatesResponse(rates: MockRates): string {
  return JSON.stringify([
    {
      table: 'A',
      no: rates.tableNumber,
      effectiveDate: rates.effectiveDate,
      rates: [
        {
          currency: 'dolar amerykański',
          code: 'USD',
          mid: rates.usd,
        },
        {
          currency: 'euro',
          code: 'EUR',
          mid: rates.eur,
        },
      ],
    },
  ]);
}

function getPreviousDayIso(): string {
  const date = new Date();
  date.setDate(date.getDate() - 1);

  const year = date.getFullYear();
  const month = `${date.getMonth() + 1}`.padStart(2, '0');
  const day = `${date.getDate()}`.padStart(2, '0');

  return `${year}-${month}-${day}`;
}

function formatPolishDate(isoDate: string): string {
  const [year, month, day] = isoDate.split('-');

  return `${day}.${month}.${year}`;
}
