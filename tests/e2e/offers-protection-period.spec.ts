import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const expectedMetaLabels = [/Pojazd|Uprawy/, 'Kanał', 'Wariant', 'Okres ochrony', 'Aktualizacja'];

test('shows protection period before update date for visible offers', async ({ page }, testInfo) => {
  await openOffersPage(page);

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const offerRows = page.locator('.offer-list .offer-row');
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offer-list-visible');

  const firstRowLabels = offerRows.first().locator('.offer-row__meta-grid .offer-row__meta-label');
  await expect(firstRowLabels).toHaveText(expectedMetaLabels);
  await captureStep(page, testInfo, 'first-offer-meta-order');

  const offerCount = await offerRows.count();

  for (let index = 0; index < offerCount; index += 1) {
    await expect(
      offerRows.nth(index).locator('.offer-row__meta-grid .offer-row__meta-label'),
    ).toHaveText(expectedMetaLabels);
  }

  await captureStep(page, testInfo, 'all-offers-meta-order');
});

test('keeps the new metadata order on mobile layout', async ({ page }, testInfo) => {
  await page.setViewportSize({ width: 900, height: 900 });
  await openOffersPage(page);

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-mobile-loaded');

  const firstRowLabels = page
    .locator('.offer-list .offer-row')
    .first()
    .locator('.offer-row__meta-grid .offer-row__meta-label');

  await expect(firstRowLabels).toHaveText(expectedMetaLabels);
  await captureStep(page, testInfo, 'mobile-meta-order');
});

async function openOffersPage(page: Parameters<typeof test>[0]['page']) {
  await page.goto('/');
  await page.goto('/offers');
  await expect(page).toHaveURL(/\/offers$/);
}
