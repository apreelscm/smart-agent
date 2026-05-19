import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const DEMO_USER = {
  username: 'admin',
  name: 'Administrator',
  role: 'EUM_ADMINISTRATOR',
  email: 'admin@eumowy.local',
  phone: '',
  auwId: 1,
};

const CONVERSION_ERROR_MESSAGE =
  'Nie udało się pobrać kursu waluty. Pozostawiono poprzednio wyświetlane kwoty.';

test('coverage currency defaults to PLN and resets after re-entering the step', async ({ page }, testInfo) => {
  await page.addInitScript(user => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/USD\/\?format=json/,
    async route => {
      await route.fulfill({
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              no: '101/A/NBP/2025',
              effectiveDate: '2025-05-19',
              mid: 4,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/kalkulator/zakres');

  const currencySelect = page.getByLabel('Waluta');
  const totalPrice = page.locator('.price-summary .price-total');

  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
  await expect(currencySelect.locator('option:checked')).toHaveText('PLN');
  await expect(totalPrice).toHaveText('289,00 PLN');
  await expect(page.getByText('Użyty kurs NBP')).toHaveCount(0);
  await captureStep(page, testInfo, 'coverage-default-pln');

  await currencySelect.selectOption({ label: 'USD' });

  await expect(currencySelect.locator('option:checked')).toHaveText('USD');
  await expect(page.getByText('1 USD = 4,0000 PLN')).toBeVisible();
  await expect(totalPrice).toHaveText('72,25 USD');
  await captureStep(page, testInfo, 'coverage-usd-before-reenter');

  await page.goto('/');
  await page.goto('/kalkulator/zakres');

  await expect(page.getByRole('heading', { name: 'Wybierz zakres' })).toBeVisible();
  await expect(currencySelect.locator('option:checked')).toHaveText('PLN');
  await expect(totalPrice).toHaveText('289,00 PLN');
  await expect(page.getByText('Użyty kurs NBP')).toHaveCount(0);
  await captureStep(page, testInfo, 'coverage-reset-pln-after-reenter');
});

test('coverage currency converts amounts to USD and shows NBP metadata', async ({ page }, testInfo) => {
  await page.addInitScript(user => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.route(
    /https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/USD\/\?format=json/,
    async route => {
      await route.fulfill({
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'dolar amerykański',
          code: 'USD',
          rates: [
            {
              no: '101/A/NBP/2025',
              effectiveDate: '2025-05-19',
              mid: 4,
            },
          ],
        }),
      });
    },
  );

  await page.goto('/kalkulator/zakres');

  const currencySelect = page.getByLabel('Waluta');
  const totalPrice = page.locator('.price-summary .price-total');
  const ocPrice = page
    .locator('.cov-card')
    .filter({ has: page.locator('.cov-label', { hasText: 'OC' }) })
    .locator('.cov-price');
  const acPrice = page
    .locator('.cov-card')
    .filter({ has: page.locator('.cov-label', { hasText: 'AC' }) })
    .locator('.cov-price');
  const szybyPrice = page
    .locator('.cov-card')
    .filter({ has: page.locator('.cov-label', { hasText: 'Szyby' }) })
    .locator('.cov-price');

  await currencySelect.selectOption({ label: 'USD' });

  await expect(currencySelect.locator('option:checked')).toHaveText('USD');
  await expect(page.getByText('Użyty kurs NBP')).toBeVisible();
  await expect(page.getByText('1 USD = 4,0000 PLN')).toBeVisible();
  await expect(page.getByText('Tabela: 101/A/NBP/2025')).toBeVisible();
  await expect(page.getByText('Data kursu: 2025-05-19')).toBeVisible();
  await expect(ocPrice).toHaveText('72,25 USD');
  await expect(acPrice).toHaveText('97,25 USD');
  await expect(szybyPrice).toHaveText('37,50 USD');
  await expect(totalPrice).toHaveText('72,25 USD');
  await captureStep(page, testInfo, 'coverage-usd-converted');

  await currencySelect.selectOption({ label: 'PLN' });

  await expect(currencySelect.locator('option:checked')).toHaveText('PLN');
  await expect(page.getByText('Użyty kurs NBP')).toHaveCount(0);
  await expect(page.getByText(CONVERSION_ERROR_MESSAGE)).toHaveCount(0);
  await expect(ocPrice).toHaveText('289,00 PLN');
  await expect(totalPrice).toHaveText('289,00 PLN');
  await captureStep(page, testInfo, 'coverage-back-to-pln');
});

test('coverage currency falls back to previous day and keeps previous values on total failure', async ({ page }, testInfo) => {
  await page.addInitScript(user => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.route(/https:\/\/api\.nbp\.pl\/api\/exchangerates\/rates\/A\/(EUR|USD)\/.*$/, async route => {
    const url = route.request().url();

    if (url.includes('/EUR/?format=json')) {
      await route.fulfill({
        status: 404,
        contentType: 'application/json',
        body: JSON.stringify({ message: 'missing current EUR rate' }),
      });
      return;
    }

    if (/\/EUR\/\d{4}-\d{2}-\d{2}\/\?format=json$/.test(url)) {
      await route.fulfill({
        contentType: 'application/json',
        body: JSON.stringify({
          table: 'A',
          currency: 'euro',
          code: 'EUR',
          rates: [
            {
              no: '100/A/NBP/2025',
              effectiveDate: '2025-05-18',
              mid: 5,
            },
          ],
        }),
      });
      return;
    }

    if (url.includes('/USD/?format=json') || /\/USD\/\d{4}-\d{2}-\d{2}\/\?format=json$/.test(url)) {
      await route.fulfill({
        status: 404,
        contentType: 'application/json',
        body: JSON.stringify({ message: 'USD rate unavailable' }),
      });
      return;
    }

    await route.continue();
  });

  await page.goto('/kalkulator/zakres');

  const currencySelect = page.getByLabel('Waluta');
  const totalPrice = page.locator('.price-summary .price-total');

  await currencySelect.selectOption({ label: 'EUR' });

  await expect(currencySelect.locator('option:checked')).toHaveText('EUR');
  await expect(page.getByText('1 EUR = 5,0000 PLN')).toBeVisible();
  await expect(page.getByText('Tabela: 100/A/NBP/2025')).toBeVisible();
  await expect(page.getByText('Data kursu: 2025-05-18')).toBeVisible();
  await expect(totalPrice).toHaveText('57,80 EUR');
  await captureStep(page, testInfo, 'coverage-eur-fallback');

  await currencySelect.selectOption({ label: 'USD' });

  await expect(page.getByText(CONVERSION_ERROR_MESSAGE)).toBeVisible();
  await expect(currencySelect.locator('option:checked')).toHaveText('EUR');
  await expect(page.getByText('1 EUR = 5,0000 PLN')).toBeVisible();
  await expect(totalPrice).toHaveText('57,80 EUR');
  await captureStep(page, testInfo, 'coverage-usd-failure-keeps-eur');
});
