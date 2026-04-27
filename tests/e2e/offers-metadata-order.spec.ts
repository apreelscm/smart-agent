import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test(
  'offers list shows variant before protection period for every visible offer row',
  async ({ page }, testInfo) => {
    await page.goto('/');
    await page.goto('/offers');

    await expect(page).toHaveURL(/\/offers(?:\?.*)?$/);
    await expect(page.getByText('Przygotowane oferty')).toBeVisible();
    await expect(page.locator('.offer-row').first()).toBeVisible();
    await captureStep(page, testInfo, 'offers-list-loaded');

    const metaGrids = page.locator('.offer-row__meta-grid');
    const metaGridCount = await metaGrids.count();

    expect(metaGridCount).toBeGreaterThan(0);

    const expectedProtectionPeriod = buildProtectionPeriodLabel(new Date());

    for (let index = 0; index < metaGridCount; index += 1) {
      const metaGrid = metaGrids.nth(index);
      const labels = (await metaGrid.locator('.offer-row__meta-label').allInnerTexts()).map((label) =>
        label.trim(),
      );

      expect(labels).toContain('Wariant');
      expect(labels).toContain('Okres ochrony');
      expect(labels.indexOf('Wariant')).toBeLessThan(labels.indexOf('Okres ochrony'));
      await expect(metaGrid).toContainText(expectedProtectionPeriod);
    }

    await captureStep(page, testInfo, 'metadata-order-verified');
  },
);

test(
  'narrow offers layout keeps variant before protection period in the first visible row',
  async ({ page }, testInfo) => {
    await page.setViewportSize({ width: 375, height: 900 });
    await page.goto('/');
    await page.goto('/offers');

    const firstMetaGrid = page.locator('.offer-row__meta-grid').first();

    await expect(page.getByText('Przygotowane oferty')).toBeVisible();
    await expect(firstMetaGrid).toBeVisible();

    const variantLabel = firstMetaGrid.locator('.offer-row__meta-label', { hasText: 'Wariant' });
    const protectionLabel = firstMetaGrid.locator('.offer-row__meta-label', {
      hasText: 'Okres ochrony',
    });

    await expect(variantLabel).toBeVisible();
    await expect(protectionLabel).toBeVisible();
    await captureStep(page, testInfo, 'narrow-layout-loaded');

    const variantBox = await variantLabel.boundingBox();
    const protectionBox = await protectionLabel.boundingBox();

    if (!variantBox || !protectionBox) {
      throw new Error('Expected metadata labels to have layout boxes');
    }

    const variantAppearsBeforeProtection =
      variantBox.y < protectionBox.y ||
      (Math.abs(variantBox.y - protectionBox.y) < 4 && variantBox.x < protectionBox.x);

    expect(variantAppearsBeforeProtection).toBeTruthy();
    await captureStep(page, testInfo, 'narrow-layout-order-verified');
  },
);

function buildProtectionPeriodLabel(today: Date): string {
  const endDate = addOneCalendarYear(today);

  return `${formatDate(today)} - ${formatDate(endDate)}`;
}

function addOneCalendarYear(date: Date): Date {
  const endDate = new Date(date);
  const targetMonth = endDate.getMonth();

  endDate.setFullYear(endDate.getFullYear() + 1);

  if (endDate.getMonth() !== targetMonth) {
    endDate.setDate(0);
  }

  return endDate;
}

function formatDate(date: Date): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}/${month}/${day}`;
}
