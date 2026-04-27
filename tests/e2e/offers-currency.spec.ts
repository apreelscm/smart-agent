import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches offer premiums between PLN and EUR without leaving the page', async ({ page }, testInfo) => {
  const nbpRequests: string[] = [];

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/(EUR|USD)\/\?format=json/,
    async (route) => {
      const url = route.request().url();
      nbpRequests.push(url);

      if (url.includes('/EUR/')) {
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
              mid: 5,
            },
          ],
        }),
      });
    },
  );

  await openOffersPage(page);

  const currencySelect = page.locator('.toolbar__actions p-select').first();
  const initialPremium = await getFirstPremiumAmount(page);

  await expect(currencySelect).toContainText('PLN');
  await expect(page.locator('.currency-state-row')).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-default-pln');

  await selectCurrency(page, 'EUR');

  await expect(currencySelect).toContainText('EUR');
  await expect
    .poll(async () => getFirstPremiumAmount(page))
    .toBe(Math.round(initialPremium / 4));
  await expect(page.locator('.currency-state-row')).toContainText(
    'Kurs NBP: 1 EUR = 4,0000 PLN',
  );
  await captureStep(page, testInfo, 'offers-eur-conversion');

  await selectCurrency(page, 'PLN');

  await expect(currencySelect).toContainText('PLN');
  await expect.poll(async () => getFirstPremiumAmount(page)).toBe(initialPremium);
  await expect(page.locator('.currency-state-row')).toHaveCount(0);
  expect(nbpRequests).toHaveLength(1);
  await captureStep(page, testInfo, 'offers-pln-restored');
});

test('keeps the last valid currency presentation when the next conversion fails', async (
  { page },
  testInfo,
) => {
  const nbpRequests: string[] = [];

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/(EUR|USD)\/\?format=json/,
    async (route) => {
      const url = route.request().url();
      nbpRequests.push(url);

      if (url.includes('/EUR/')) {
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

      await route.fulfill({
        status: 503,
        contentType: 'application/json',
        body: JSON.stringify({
          message: 'NBP unavailable',
        }),
      });
    },
  );

  await openOffersPage(page);

  const currencySelect = page.locator('.toolbar__actions p-select').first();
  const initialPremium = await getFirstPremiumAmount(page);
  const expectedEurPremium = Math.round(initialPremium / 4);

  await selectCurrency(page, 'EUR');

  await expect(currencySelect).toContainText('EUR');
  await expect.poll(async () => getFirstPremiumAmount(page)).toBe(expectedEurPremium);
  await expect(page.locator('.currency-state-row')).toContainText(
    'Kurs NBP: 1 EUR = 4,0000 PLN',
  );
  await captureStep(page, testInfo, 'offers-eur-before-error');

  await selectCurrency(page, 'USD');

  await expect(currencySelect).toContainText('EUR');
  await expect.poll(async () => getFirstPremiumAmount(page)).toBe(expectedEurPremium);
  await expect(page.locator('.currency-state-row')).toContainText(
    'Nie udało się pobrać aktualnego kursu NBP. Wyświetlane wartości pozostają bez zmian.',
  );
  expect(nbpRequests).toEqual([
    'https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json',
    'https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json',
  ]);
  await captureStep(page, testInfo, 'offers-currency-error');
});

async function openOffersPage(page: Parameters<typeof test>[0]['page']): Promise<void> {
  await page.goto('/');

  const heading = page.getByRole('heading', { name: 'Przygotowane oferty' });

  if (!(await heading.isVisible())) {
    await page.goto('/offers');
  }

  await expect(heading).toBeVisible();
  await expect(page.locator('.toolbar__actions p-select').first()).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
}

async function selectCurrency(
  page: Parameters<typeof test>[0]['page'],
  currency: 'PLN' | 'EUR' | 'USD',
): Promise<void> {
  const currencySelect = page.locator('.toolbar__actions p-select').first();

  await currencySelect.click();
  await page.locator('.p-select-option').filter({ hasText: new RegExp(`^${currency}$`) }).click();
}

async function getFirstPremiumAmount(
  page: Parameters<typeof test>[0]['page'],
): Promise<number> {
  const premiumText = await page.locator('.offer-row .premium-box strong').first().textContent();

  return parseWholeAmount(premiumText ?? '');
}

function parseWholeAmount(text: string): number {
  return Number(text.replace(/[^\d]/g, ''));
}
