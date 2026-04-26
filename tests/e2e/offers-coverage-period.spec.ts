import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

async function mockCurrentDate(page, isoDate: string) {
  const timestamp = new Date(isoDate).getTime();

  await page.addInitScript(`
    {
      const __fixedNow = ${timestamp};
      const __RealDate = Date;

      class __MockDate extends __RealDate {
        constructor(...args) {
          if (args.length === 0) {
            super(__fixedNow);
            return;
          }

          super(...args);
        }

        static now() {
          return __fixedNow;
        }
      }

      globalThis.Date = __MockDate;
    }
  `);
}

async function openOffersPage(page) {
  await page.goto('/');
  await page.goto('/offers');
}

test('shows the protection period for each offer with the expected formatted range', async ({ page }, testInfo) => {
  await mockCurrentDate(page, '2025-05-10T10:15:00.000Z');
  await openOffersPage(page);

  await expect(page.getByText('Przygotowane oferty', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-opened');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();

  const offerRowCount = await offerRows.count();
  expect(offerRowCount).toBeGreaterThan(0);

  const coverageLabels = page.locator('.offer-row__meta-item--coverage .offer-row__meta-label');
  await expect(coverageLabels).toHaveCount(offerRowCount);
  await captureStep(page, testInfo, 'coverage-label-visible-in-each-row');

  const coverageValues = page.locator('.offer-row__coverage-value');
  await expect(coverageValues).toHaveCount(offerRowCount);
  await expect(coverageValues.first()).toHaveText('2025/05/10 00:00 - 2026/05/09 23:59');

  const normalizedValues = (await coverageValues.allTextContents()).map((value) =>
    value.replace(/\s+/g, ' ').trim(),
  );

  expect(normalizedValues).toHaveLength(offerRowCount);
  expect(new Set(normalizedValues).size).toBe(1);
  expect(normalizedValues[0]).toBe('2025/05/10 00:00 - 2026/05/09 23:59');
  await captureStep(page, testInfo, 'coverage-range-consistent-across-offers');
});

test('keeps the empty state unchanged when filters return zero offers', async ({ page }, testInfo) => {
  await mockCurrentDate(page, '2025-05-10T10:15:00.000Z');
  await openOffersPage(page);

  const searchInput = page.locator('input[placeholder*="Szukaj"]');
  await expect(searchInput).toBeVisible();

  await searchInput.fill('brak-wynikow-e2e');
  await expect(page.getByText('Brak ofert dla podanych filtrów', { exact: true })).toBeVisible();
  await captureStep(page, testInfo, 'empty-state-after-filtering');

  await expect(page.locator('.offer-row')).toHaveCount(0);
  await expect(page.locator('.offer-row__meta-item--coverage')).toHaveCount(0);
  await captureStep(page, testInfo, 'no-coverage-period-outside-offer-rows');
});
