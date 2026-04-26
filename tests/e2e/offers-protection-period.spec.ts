import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows the same protection period for every offer row', async ({ page }, testInfo) => {
  await page.goto('/');
  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const offerRows = page.locator('.offer-row');
  const offerRowCount = await offerRows.count();

  expect(offerRowCount).toBeGreaterThan(1);

  const expectedProtectionPeriod = await page.evaluate(() => {
    const referenceDate = new Date();
    const startDate = new Date(
      referenceDate.getFullYear(),
      referenceDate.getMonth(),
      referenceDate.getDate(),
      0,
      0,
      0,
      0,
    );

    const targetYear = startDate.getFullYear() + 1;
    const targetMonth = startDate.getMonth();
    const daysInTargetMonth = new Date(targetYear, targetMonth + 1, 0).getDate();
    const targetDay = Math.min(startDate.getDate(), daysInTargetMonth);
    const endDate = new Date(targetYear, targetMonth, targetDay, 23, 59, 0, 0);

    const formatLocalDate = (date: Date) => {
      const year = date.getFullYear();
      const month = `${date.getMonth() + 1}`.padStart(2, '0');
      const day = `${date.getDate()}`.padStart(2, '0');

      return `${year}-${month}-${day}`;
    };

    return `${formatLocalDate(startDate)} – ${formatLocalDate(endDate)}`;
  });

  const firstProtectionPeriodCell = offerRows
    .first()
    .locator('.offer-row__meta-grid > div')
    .filter({
      has: offerRows.first().locator('.offer-row__meta-label', { hasText: 'Okres ochrony' }),
    });

  await expect(firstProtectionPeriodCell).toHaveCount(1);
  await expect(firstProtectionPeriodCell.locator('strong')).toHaveText(
    /\d{4}-\d{2}-\d{2} – \d{4}-\d{2}-\d{2}/,
  );
  await captureStep(page, testInfo, 'protection-period-visible');

  for (let index = 0; index < offerRowCount; index += 1) {
    const row = offerRows.nth(index);
    const protectionPeriodCell = row.locator('.offer-row__meta-grid > div').filter({
      has: row.locator('.offer-row__meta-label', { hasText: 'Okres ochrony' }),
    });

    await expect(protectionPeriodCell).toHaveCount(1);
    await expect(protectionPeriodCell.locator('strong')).toHaveText(expectedProtectionPeriod);
  }

  await captureStep(page, testInfo, 'protection-period-consistent-across-rows');
});
