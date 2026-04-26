import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('offers list shows Okres ochrony before Aktualizacja in every row', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const offerRows = page.locator('.offer-row');
  await expect(offerRows.first()).toBeVisible();
  await captureStep(page, testInfo, 'offer-rows-visible');

  const rowCount = await offerRows.count();
  expect(rowCount).toBeGreaterThan(1);

  const rowsMeta = await offerRows.evaluateAll((rows) =>
    rows.map((row) => {
      const cells = Array.from(row.querySelectorAll('.offer-row__meta-grid > div'));
      const labels = cells.map(
        (cell) => cell.querySelector('.offer-row__meta-label')?.textContent?.trim() ?? '',
      );
      const protectionCell = cells.find(
        (cell) => cell.querySelector('.offer-row__meta-label')?.textContent?.trim() === 'Okres ochrony',
      );
      const updatedCell = cells.find(
        (cell) => cell.querySelector('.offer-row__meta-label')?.textContent?.trim() === 'Aktualizacja',
      );
      const updatedSpans = updatedCell ? Array.from(updatedCell.querySelectorAll('span')) : [];

      return {
        labels,
        protectionValue: protectionCell?.querySelector('strong')?.textContent?.trim() ?? '',
        updatedDate: updatedCell?.querySelector('strong')?.textContent?.trim() ?? '',
        updatedTime: updatedSpans[1]?.textContent?.trim() ?? '',
      };
    }),
  );

  const leadingLabels = rowsMeta.map((row) => row.labels[0]);
  expect(leadingLabels).toContain('Pojazd');
  expect(leadingLabels).toContain('Uprawy');

  for (const rowMeta of rowsMeta) {
    expect(rowMeta.labels).toHaveLength(5);
    expect(rowMeta.labels[0]).toMatch(/^(Pojazd|Uprawy)$/);
    expect(rowMeta.labels.slice(1)).toEqual(['Kanał', 'Wariant', 'Okres ochrony', 'Aktualizacja']);
    expect(rowMeta.protectionValue).not.toBe('');
    expect(rowMeta.updatedDate).toMatch(/^\d{2}\.\d{2}\.\d{4}$/);
    expect(rowMeta.updatedTime).toMatch(/^\d{2}:\d{2}$/);
  }

  await captureStep(page, testInfo, 'metadata-order-verified');
});
