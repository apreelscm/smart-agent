import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows Okres ochrony before Aktualizacja in visible rows', async ({ page }, testInfo) => {
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers(?:\?.*)?$/);
  await expect(page.getByText('Portfel ofert')).toBeVisible();
  await expect(page.locator('article.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('article.offer-row');
  const rowCount = await offerRows.count();

  expect(rowCount).toBeGreaterThan(0);

  const firstProtectionPeriod = offerRows.first().locator('.offer-row__protection-period');
  await expect(firstProtectionPeriod).toBeVisible();
  await expect(firstProtectionPeriod).toHaveText(
    /\d{4}\/\d{2}\/\d{2} \d{2}:\d{2} - \d{4}\/\d{2}\/\d{2} \d{2}:\d{2}/,
  );
  await captureStep(page, testInfo, 'protection-period-visible');

  for (let index = 0; index < rowCount; index += 1) {
    const labels = (await offerRows
      .nth(index)
      .locator('.offer-row__meta-grid > div .offer-row__meta-label')
      .allTextContents()).map((text) => text.trim());

    expect(labels).toContain('Okres ochrony');
    expect(labels).toContain('Aktualizacja');
    expect(labels.indexOf('Okres ochrony')).toBeLessThan(labels.indexOf('Aktualizacja'));
  }

  await captureStep(page, testInfo, 'metadata-order-verified');
});

test('offers empty state hides metadata rows when filters return no results', async ({ page }, testInfo) => {
  await page.goto('/offers');

  await expect(page.getByText('Portfel ofert')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-before-filtering');

  await page
    .getByPlaceholder('Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja')
    .fill('brak-wynikow-sdlc-86');

  await expect(page.getByText('Brak ofert dla podanych filtrów')).toBeVisible();
  await expect(page.locator('article.offer-row')).toHaveCount(0);
  await expect(page.locator('.offer-row__meta-item--protection')).toHaveCount(0);
  await captureStep(page, testInfo, 'offers-empty-state');
});
