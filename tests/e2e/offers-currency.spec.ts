import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches visible offer premiums, summary tile and dialog to EUR and USD', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A/?format=json', async (route) => {
    await route.fulfill({
      status: 200,
      contentType: 'application/json',
      body: JSON.stringify([
        {
          table: 'A',
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          rates: [
            { currency: 'euro', code: 'EUR', mid: 4 },
            { currency: 'dolar amerykański', code: 'USD', mid: 5 },
          ],
        },
      ]),
    });
  });

  await page.goto('/');
  await expect(page.locator('app-root')).toBeVisible();
  await page.waitForLoadState('networkidle');

  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  if (!(await offersHeading.isVisible())) {
    const offersLink = page.getByRole('link', { name: /oferty/i }).first();
    if (await offersLink.isVisible()) {
      await offersLink.click();
    } else {
      const offersButton = page.getByRole('button', { name: /oferty/i }).first();
      if (await offersButton.isVisible()) {
        await offersButton.click();
      }
    }
  }

  await expect(offersHeading).toBeVisible();
  const currencyRow = page.locator('.toolbar__currency-row');
  await expect(currencyRow).toBeVisible();
  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-ready');

  const firstRowPremium = offerRows.first().locator('.premium-box strong');
  const averagePremiumTile = page.locator('app-stat-tile', { hasText: 'Średnia składka' }).first();

  const initialRowPremiumText = normalizeWhitespace(await firstRowPremium.innerText());
  const initialAveragePremiumText = extractDisplayedAmount(await averagePremiumTile.innerText());

  expect(initialRowPremiumText).toContain('zł');
  expect(initialAveragePremiumText).toContain('zł');

  const initialRowPremium = parseCurrencyValue(initialRowPremiumText);
  const initialAveragePremium = parseCurrencyValue(initialAveragePremiumText);

  const currencySelect = currencyRow.getByRole('combobox');
  await currencySelect.click();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect(firstRowPremium).toContainText('€');
  await expect(averagePremiumTile).toContainText('€');

  const eurRowPremiumText = normalizeWhitespace(await firstRowPremium.innerText());
  const eurAveragePremiumText = extractDisplayedAmount(await averagePremiumTile.innerText());

  expect(parseCurrencyValue(eurRowPremiumText)).toBeCloseTo(initialRowPremium / 4, 2);
  expect(parseCurrencyValue(eurAveragePremiumText)).toBeCloseTo(initialAveragePremium / 4, 2);
  await captureStep(page, testInfo, 'eur-presentation-selected');

  await currencySelect.click();
  await page.getByRole('option', { name: 'USD' }).click();

  await expect(firstRowPremium).toContainText('USD');
  await expect(averagePremiumTile).toContainText('USD');

  const usdRowPremiumText = normalizeWhitespace(await firstRowPremium.innerText());
  const usdAveragePremiumText = extractDisplayedAmount(await averagePremiumTile.innerText());

  expect(parseCurrencyValue(usdRowPremiumText)).toBeCloseTo(initialRowPremium / 5, 2);
  expect(parseCurrencyValue(usdAveragePremiumText)).toBeCloseTo(initialAveragePremium / 5, 2);
  await captureStep(page, testInfo, 'usd-presentation-selected');

  let dialogOpened = false;
  const rowCount = await offerRows.count();

  for (let index = 0; index < rowCount; index += 1) {
    const row = offerRows.nth(index);
    const dropdownButton = row.locator('.p-splitbutton-dropdown');

    if (!(await dropdownButton.isVisible())) {
      continue;
    }

    const rowPremiumText = normalizeWhitespace(await row.locator('.premium-box strong').innerText());

    await dropdownButton.click();

    const cancelTransition = page.getByText('Anuluj', { exact: true }).first();

    try {
      await cancelTransition.waitFor({ state: 'visible', timeout: 750 });
      await cancelTransition.click();

      const dialog = page.locator('.transition-dialog');
      await expect(dialog).toBeVisible();

      const dialogPremium = dialog
        .locator('.transition-dialog__summary div')
        .filter({ hasText: 'Składka' })
        .locator('strong');

      await expect(dialogPremium).toContainText('USD');
      expect(normalizeWhitespace(await dialogPremium.innerText())).toBe(rowPremiumText);

      dialogOpened = true;
      break;
    } catch {
      await page.keyboard.press('Escape');
    }
  }

  expect(dialogOpened).toBe(true);
  await captureStep(page, testInfo, 'transition-dialog-uses-selected-currency');
});

test('keeps PLN presentation and shows a non-blocking error when rates fail', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A/?format=json', async (route) => {
    await route.fulfill({
      status: 500,
      contentType: 'application/json',
      body: JSON.stringify({ message: 'NBP unavailable' }),
    });
  });

  await page.goto('/');
  await expect(page.locator('app-root')).toBeVisible();
  await page.waitForLoadState('networkidle');

  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  if (!(await offersHeading.isVisible())) {
    const offersLink = page.getByRole('link', { name: /oferty/i }).first();
    if (await offersLink.isVisible()) {
      await offersLink.click();
    } else {
      const offersButton = page.getByRole('button', { name: /oferty/i }).first();
      if (await offersButton.isVisible()) {
        await offersButton.click();
      }
    }
  }

  await expect(offersHeading).toBeVisible();
  const currencyRow = page.locator('.toolbar__currency-row');
  const firstRowPremium = page.locator('.offer-row .premium-box strong').first();

  await expect(firstRowPremium).toBeVisible();
  await expect(currencyRow.locator('.toolbar__currency-message--error')).toContainText(
    'Wyświetlamy składki w PLN.',
  );
  await captureStep(page, testInfo, 'rates-error-visible');

  const initialPremiumText = normalizeWhitespace(await firstRowPremium.innerText());
  expect(initialPremiumText).toContain('zł');

  const currencySelect = currencyRow.getByRole('combobox');
  await currencySelect.click();
  await page.getByRole('option', { name: 'EUR' }).click();

  await expect(firstRowPremium).toContainText('zł');
  await expect(firstRowPremium).not.toContainText('€');
  await expect(currencyRow.locator('.toolbar__currency-message--error')).toContainText(
    'Kursy walut są obecnie niedostępne.',
  );
  await captureStep(page, testInfo, 'pln-presentation-kept-after-rate-error');
});

function normalizeWhitespace(value: string | null | undefined): string {
  return (value ?? '').replace(/\s+/g, ' ').trim();
}

function extractDisplayedAmount(blockText: string): string {
  const match = normalizeWhitespace(blockText).match(/[\d\s,.]+(?:zł|€|USD)/u);

  if (!match) {
    throw new Error(`Could not find a currency amount in: ${blockText}`);
  }

  return normalizeWhitespace(match[0]);
}

function parseCurrencyValue(text: string): number {
  const normalized = text
    .replace(/\s+/g, '')
    .replace('zł', '')
    .replace('€', '')
    .replace('USD', '')
    .replace(',', '.');

  return Number(normalized);
}
