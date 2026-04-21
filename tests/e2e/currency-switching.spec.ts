import { test, expect } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('switches offers page to EUR and shows applied NBP rate', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A/?format=json', async (route) => {
    await route.fulfill({
      json: [
        {
          table: 'A',
          no: '074/A/NBP/2026',
          effectiveDate: '2026-04-17',
          rates: [
            { code: 'EUR', mid: 4.3 },
            { code: 'USD', mid: 3.9 }
          ]
        }
      ]
    });
  });

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('button', { name: 'EUR' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-switcher-visible');

  await page.getByRole('button', { name: 'EUR' }).click();
  await expect(page.getByText('Kurs NBP')).toBeVisible();
  await captureStep(page, testInfo, 'offers-rate-note-visible');

  await expect(page.locator('main')).toContainText('€');
  await captureStep(page, testInfo, 'offers-eur-amounts-visible');
});

test('falls back to PLN when exchange rates request fails', async ({ page }, testInfo) => {
  await page.route('https://api.nbp.pl/api/exchangerates/tables/A/?format=json', async (route) => {
    await route.abort();
  });

  await page.goto('/');
  await page.goto('/reports');

  await expect(page.getByText('Kursy EUR i USD są chwilowo niedostępne. Widok działa wyłącznie w PLN.')).toBeVisible();
  await captureStep(page, testInfo, 'reports-fallback-message-visible');

  await expect(page.getByRole('button', { name: 'EUR' })).toBeDisabled();
  await captureStep(page, testInfo, 'reports-foreign-currency-disabled');
});
