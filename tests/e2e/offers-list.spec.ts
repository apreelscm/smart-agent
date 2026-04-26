import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows the same protection period on every offer row', async ({ page }, testInfo) => {
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  const rowCount = await offerRows.count();

  expect(rowCount).toBeGreaterThan(0);

  const protectionFields = offerRows
    .locator('.offer-row__meta-grid > div')
    .filter({ has: page.getByText('Okres ochrony', { exact: true }) });
  const protectionValues = protectionFields.locator('strong');
  const periodFormat =
    /^\d{4}\/\d{2}\/\d{2} \d{2}:\d{2} - \d{4}\/\d{2}\/\d{2} \d{2}:\d{2}$/;

  await expect(protectionFields).toHaveCount(rowCount);
  await expect(protectionFields.first().getByText('Okres ochrony', { exact: true })).toBeVisible();
  await expect(protectionFields.first().locator('strong')).toBeVisible();
  await expect(protectionFields.first().getByText('Wyliczany według dnia biznesowego PL')).toBeVisible();
  await captureStep(page, testInfo, 'protection-period-field-visible');

  const values = await protectionValues.allTextContents();

  expect(values).toHaveLength(rowCount);

  for (const value of values) {
    expect(value).toMatch(periodFormat);
  }

  expect(new Set(values)).toHaveSize(1);
  await captureStep(page, testInfo, 'protection-period-consistent-across-rows');
});
