import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows the protection period on every visible offer row', async ({ page }, testInfo) => {
  await openOffersList(page);

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-opened');

  const expectedPeriod = formatCoveragePeriod(new Date());
  const offerRows = page.locator('.offer-row');
  const rowCount = await offerRows.count();

  expect(rowCount).toBeGreaterThan(0);
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offer-rows-visible');

  for (let index = 0; index < rowCount; index += 1) {
    await expect(offerRows.nth(index)).toContainText('Okres ochrony');
    await expect(offerRows.nth(index)).toContainText(expectedPeriod);
  }

  await captureStep(page, testInfo, 'protection-period-visible-on-all-rows');
});

test('renders the leap-day protection period without shifting the day', async ({ page }, testInfo) => {
  await page.clock.setFixedTime(new Date('2024-02-29T08:15:00'));
  await openOffersList(page);

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'leap-day-offers-list-opened');

  const expectedPeriod = '2024/02/29 - 2025/02/28';
  const offerRows = page.locator('.offer-row');
  const rowCount = await offerRows.count();

  expect(rowCount).toBeGreaterThan(0);

  for (let index = 0; index < rowCount; index += 1) {
    await expect(offerRows.nth(index)).toContainText(expectedPeriod);
  }

  await captureStep(page, testInfo, 'leap-day-period-visible');
});

test('keeps the protection period unchanged after switching presentation currency', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json', async (route) => {
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
  });

  await openOffersList(page);

  const expectedPeriod = formatCoveragePeriod(new Date());
  const firstOfferRow = page.locator('.offer-row').first();

  await expect(firstOfferRow).toContainText(expectedPeriod);
  await captureStep(page, testInfo, 'period-before-currency-change');

  await page.locator('.toolbar__select--currency').click();

  const eurOption = page.getByRole('option', { name: 'EUR' });
  await expect(eurOption).toBeVisible();
  await eurOption.click();

  await expect(page.getByText(/Kurs NBP: 1 EUR = 4,0000 PLN/i)).toBeVisible();
  await captureStep(page, testInfo, 'eur-currency-selected');

  await expect(firstOfferRow).toContainText(expectedPeriod);
  await expect(firstOfferRow).toContainText('Okres ochrony');
  await captureStep(page, testInfo, 'period-stable-after-currency-change');
});

async function openOffersList(page: Parameters<typeof test>[0]['page']): Promise<void> {
  await page.goto('/');
  await expect(page.locator('app-root')).toBeVisible();

  const offersTitle = page.getByText('Przygotowane oferty', { exact: true });

  if (!(await offersTitle.isVisible())) {
    const offersLink = page.getByRole('link', { name: /ofert/i }).first();
    await expect(offersLink).toBeVisible();
    await offersLink.click();
  }

  await expect(offersTitle).toBeVisible();
}

function formatCoveragePeriod(sourceDate: Date): string {
  const startDate = new Date(
    sourceDate.getFullYear(),
    sourceDate.getMonth(),
    sourceDate.getDate(),
  );
  const endDate = new Date(
    startDate.getFullYear() + 1,
    startDate.getMonth(),
    startDate.getDate(),
  );

  endDate.setDate(endDate.getDate() - 1);

  return `${formatCoverageDate(startDate)} - ${formatCoverageDate(
    new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate()),
  )}`;
}

function formatCoverageDate(date: Date): string {
  const year = `${date.getFullYear()}`;
  const month = `${date.getMonth() + 1}`.padStart(2, '0');
  const day = `${date.getDate()}`.padStart(2, '0');

  return `${year}/${month}/${day}`;
}
