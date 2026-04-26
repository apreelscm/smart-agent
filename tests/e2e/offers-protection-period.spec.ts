import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows Okres ochrony before Aktualizacja in every visible offer row', async ({ page }, testInfo) => {
  await page.goto('/');

  await expect(page.getByText('Przygotowane oferty')).toBeVisible();
  await captureStep(page, testInfo, 'offers-page-loaded');

  const offerRows = page.locator('.offer-row');

  await expect(offerRows.first()).toBeVisible();
  const rowCount = await offerRows.count();
  expect(rowCount).toBeGreaterThan(0);
  await captureStep(page, testInfo, 'offers-list-visible');

  const rowMetadata = await offerRows.evaluateAll((rows) =>
    rows.map((row) => {
      const labels = Array.from(
        row.querySelectorAll('.offer-row__meta-grid > div > .offer-row__meta-label'),
      ).map((label) => label.textContent?.trim() ?? '');

      const protectionValue =
        row.querySelector('.offer-row__meta-item--protection strong')?.textContent?.trim() ?? '';

      const updateContainer = Array.from(row.querySelectorAll('.offer-row__meta-grid > div')).find(
        (item) =>
          item.querySelector('.offer-row__meta-label')?.textContent?.trim() === 'Aktualizacja',
      );

      const updateValue = updateContainer?.querySelector('strong')?.textContent?.trim() ?? '';

      return {
        labels,
        protectionValue,
        updateValue,
      };
    }),
  );

  expect(rowMetadata).toHaveLength(rowCount);

  const normalizedLabelOrders = rowMetadata.map(({ labels }) =>
    labels.map((label, index) =>
      index === 0 && (label === 'Pojazd' || label === 'Uprawy') ? 'Przedmiot' : label,
    ),
  );

  for (const metadata of rowMetadata) {
    expect(metadata.labels.filter((label) => label === 'Okres ochrony')).toHaveLength(1);
    expect(metadata.labels.filter((label) => label === 'Aktualizacja')).toHaveLength(1);
    expect(metadata.labels.indexOf('Okres ochrony')).toBeLessThan(
      metadata.labels.indexOf('Aktualizacja'),
    );
    expect(metadata.protectionValue).not.toBe('');
    expect(metadata.updateValue).not.toBe('');
  }

  for (const labels of normalizedLabelOrders.slice(1)) {
    expect(labels).toEqual(normalizedLabelOrders[0]);
  }

  await captureStep(page, testInfo, 'protection-period-before-update');
});
