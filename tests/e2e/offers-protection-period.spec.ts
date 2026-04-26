import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

function formatDate(date: Date): string {
  return [
    date.getFullYear(),
    `${date.getMonth() + 1}`.padStart(2, '0'),
    `${date.getDate()}`.padStart(2, '0'),
  ].join('-');
}

function getExpectedProtectionPeriod(): string {
  const start = new Date();
  const end = new Date(start);
  end.setFullYear(start.getFullYear() + 1);

  return `${formatDate(start)} – ${formatDate(end)}`;
}

test('offers list renders Okres ochrony before Aktualizacja in every row', async ({ page }, testInfo) => {
  await page.goto('/');

  const heading = page.getByRole('heading', { name: 'Przygotowane oferty' });
  await expect(heading).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();

  const rowCount = await offerRows.count();
  expect(rowCount).toBeGreaterThan(0);
  await captureStep(page, testInfo, 'offer-rows-visible');

  const expectedProtectionPeriod = getExpectedProtectionPeriod();
  const rowStates = await offerRows.evaluateAll((rows) =>
    rows.map((row) => {
      const metaCells = Array.from(row.querySelectorAll('.offer-row__meta-grid > div'));
      const metaLabels = metaCells.map(
        (cell) => cell.querySelector('.offer-row__meta-label')?.textContent?.trim() ?? '',
      );
      const protectionPeriodIndex = metaLabels.indexOf('Okres ochrony');
      const updatedAtIndex = metaLabels.indexOf('Aktualizacja');
      const protectionPeriodValue =
        metaCells[protectionPeriodIndex]?.querySelector('strong')?.textContent?.trim() ?? '';
      const updatedAtValue =
        metaCells[updatedAtIndex]?.querySelector('strong')?.textContent?.trim() ?? '';
      const updatedAtTime =
        metaCells[updatedAtIndex]?.querySelector('span:last-child')?.textContent?.trim() ?? '';

      return {
        protectionPeriodIndex,
        updatedAtIndex,
        protectionPeriodValue,
        updatedAtValue,
        updatedAtTime,
      };
    }),
  );

  for (const rowState of rowStates) {
    expect(rowState.protectionPeriodIndex).toBeGreaterThan(-1);
    expect(rowState.updatedAtIndex).toBeGreaterThan(-1);
    expect(rowState.protectionPeriodIndex).toBeLessThan(rowState.updatedAtIndex);
    expect(rowState.protectionPeriodValue).toBe(expectedProtectionPeriod);
    expect(rowState.updatedAtValue).not.toBe('');
    expect(rowState.updatedAtTime).not.toBe('');
  }
  await captureStep(page, testInfo, 'meta-order-confirmed');

  await expect(page.getByText('Składka').first()).toBeVisible();
  await expect(page.getByRole('button', { name: 'Przejdź' }).first()).toBeVisible();
  await captureStep(page, testInfo, 'offer-actions-still-visible');
});
