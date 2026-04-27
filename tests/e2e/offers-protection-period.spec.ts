import { expect, test, type Page } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const mockDateStorageKey = 'e2e-mock-now';

async function installMockDate(page: Page): Promise<void> {
  await page.addInitScript((storageKey: string) => {
    window.eval(`
      (() => {
        const OriginalDate = Date;
        const getMockNow = () => {
          const storedValue = window.localStorage.getItem(${JSON.stringify(storageKey)});
          return storedValue ? new OriginalDate(storedValue).getTime() : OriginalDate.now();
        };

        const MockDate = function (...args) {
          if (new.target) {
            if (args.length === 0) {
              return new OriginalDate(getMockNow());
            }

            return new OriginalDate(...args);
          }

          return new OriginalDate(getMockNow()).toString();
        };

        MockDate.now = () => getMockNow();
        MockDate.parse = OriginalDate.parse;
        MockDate.UTC = OriginalDate.UTC;
        MockDate.prototype = OriginalDate.prototype;

        Object.defineProperty(window, 'Date', {
          configurable: true,
          writable: true,
          value: MockDate,
        });
      })();
    `);
  }, mockDateStorageKey);
}

async function seedInitialMockDate(page: Page, value: string): Promise<void> {
  await page.addInitScript(
    ({ storageKey, value: initialValue }: { storageKey: string; value: string }) => {
      if (!window.localStorage.getItem(storageKey)) {
        window.localStorage.setItem(storageKey, initialValue);
      }
    },
    {
      storageKey: mockDateStorageKey,
      value,
    },
  );
}

async function updateMockDate(page: Page, value: string): Promise<void> {
  await page.evaluate(
    ({ storageKey, value: nextValue }: { storageKey: string; value: string }) => {
      window.localStorage.setItem(storageKey, nextValue);
    },
    {
      storageKey: mockDateStorageKey,
      value,
    },
  );
}

test('shows the same protection period for each offer on the offers list', async ({ page }, testInfo) => {
  await installMockDate(page);
  await seedInitialMockDate(page, '2025-05-10T12:00:00');

  await page.goto('/offers');

  await expect(page).toHaveURL(/\/offers$/);
  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const protectionPeriodBlocks = page.locator('.offer-row__protection-period');
  const protectionPeriodValues = page.locator('.offer-row__protection-period-value');

  await expect(protectionPeriodBlocks.first()).toContainText('Okres ochrony');
  await expect(protectionPeriodValues.first()).toHaveText('2025/05/10 - 2026/05/10');
  await captureStep(page, testInfo, 'protection-period-visible');

  const rowCount = await page.locator('.offer-row').count();
  const protectionPeriodCount = await protectionPeriodBlocks.count();
  const visibleValues = (await protectionPeriodValues.allTextContents()).map((value) => value.trim());

  expect(rowCount).toBeGreaterThan(0);
  expect(protectionPeriodCount).toBe(rowCount);
  expect(new Set(visibleValues).size).toBe(1);
  expect(visibleValues[0]).toBe('2025/05/10 - 2026/05/10');
  await captureStep(page, testInfo, 'protection-period-shared-across-offers');
});

test('recalculates the protection period after opening offers on another day', async ({ page }, testInfo) => {
  await installMockDate(page);
  await seedInitialMockDate(page, '2025-05-10T12:00:00');

  await page.goto('/offers');

  const firstProtectionPeriodValue = page.locator('.offer-row__protection-period-value').first();

  await expect(firstProtectionPeriodValue).toHaveText('2025/05/10 - 2026/05/10');
  await captureStep(page, testInfo, 'protection-period-first-day');

  await updateMockDate(page, '2025-05-11T12:00:00');

  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await expect(firstProtectionPeriodValue).toHaveText('2025/05/11 - 2026/05/11');
  await captureStep(page, testInfo, 'protection-period-second-day');
});
