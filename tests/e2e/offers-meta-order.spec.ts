import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows protection period before update for visible offers', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  const offerCount = await offerRows.count();

  expect(offerCount).toBeGreaterThan(0);

  for (let index = 0; index < offerCount; index += 1) {
    const metaLabels = offerRows.nth(index).locator('.offer-row__meta-label');
    const protectionPeriod = offerRows.nth(index).locator('.offer-row__protection-period');

    await expect(metaLabels.nth(3)).toHaveText('Okres ochrony');
    await expect(metaLabels.nth(4)).toHaveText('Aktualizacja');
    await expect(protectionPeriod).toHaveText(
      /\d{4}\/\d{2}\/\d{2} - \d{4}\/\d{2}\/\d{2}/,
    );
  }

  await captureStep(page, testInfo, 'protection-period-before-update');
});

test('keeps protection period before update after filtering to one offer', async (
  { page },
  testInfo,
) => {
  await page.goto('/');

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  const firstOfferRow = page.locator('.offer-row').first();

  await expect(searchInput).toBeVisible();
  await expect(firstOfferRow).toBeVisible();

  const firstOfferNumber = (await firstOfferRow.locator('.offer-row__number span').textContent())?.trim();

  expect(firstOfferNumber).toBeTruthy();

  if (!firstOfferNumber) {
    throw new Error('Expected first offer number to be present');
  }

  await searchInput.fill(firstOfferNumber);

  await expect(page.locator('.offer-row')).toHaveCount(1);
  await expect(firstOfferRow).toContainText(firstOfferNumber);
  await expect(page.getByText('1 ofert(y) na liście')).toBeVisible();
  await captureStep(page, testInfo, 'offers-filtered-to-single-row');

  const filteredMetaLabels = firstOfferRow.locator('.offer-row__meta-label');

  await expect(filteredMetaLabels.nth(3)).toHaveText('Okres ochrony');
  await expect(filteredMetaLabels.nth(4)).toHaveText('Aktualizacja');
  await expect(firstOfferRow.locator('.offer-row__protection-period')).toHaveText(
    /\d{4}\/\d{2}\/\d{2} - \d{4}\/\d{2}\/\d{2}/,
  );
  await captureStep(page, testInfo, 'filtered-row-keeps-meta-order');
});
