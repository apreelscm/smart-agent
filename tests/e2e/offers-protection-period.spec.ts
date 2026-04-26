import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows Okres ochrony before Aktualizacja', async ({ page }, testInfo) => {
  await page.goto('/');

  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  if (!(await offersHeading.isVisible().catch(() => false))) {
    await page.goto('/offers');
  }

  await expect(offersHeading).toBeVisible();

  const firstOfferRow = page.locator('.offer-row').first();
  await expect(firstOfferRow).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-visible');

  const metaLabels = (
    await firstOfferRow.locator('.offer-row__meta-grid .offer-row__meta-label').allTextContents()
  ).map((label) => label.trim());

  const protectionLabelIndex = metaLabels.indexOf('Okres ochrony');
  const updatedAtLabelIndex = metaLabels.indexOf('Aktualizacja');

  expect(protectionLabelIndex).toBeGreaterThan(-1);
  expect(updatedAtLabelIndex).toBeGreaterThan(-1);
  expect(protectionLabelIndex).toBeLessThan(updatedAtLabelIndex);
  await captureStep(page, testInfo, 'metadata-order-confirmed');

  const protectionValue = firstOfferRow.locator(
    '.offer-row__meta-item--protection .offer-row__protection-period',
  );
  await expect(protectionValue).toHaveText(/\S/);

  const updatedAtBlock = firstOfferRow.locator('.offer-row__meta-grid > div', {
    hasText: 'Aktualizacja',
  });
  await expect(updatedAtBlock.locator('strong')).toHaveText(/\d{2}\.\d{2}\.\d{4}/);
  await expect(updatedAtBlock.locator('span').last()).toHaveText(/\d{2}:\d{2}/);
  await captureStep(page, testInfo, 'metadata-values-visible');
});
