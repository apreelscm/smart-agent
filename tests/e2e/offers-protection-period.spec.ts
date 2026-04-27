import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows the protection period column for every visible offer row', async ({ page }, testInfo) => {
  const fixedTime = new Date('2025-01-15T12:00:00').getTime();

  await page.addInitScript((now) => {
    const RealDate = Date;

    class MockDate extends RealDate {
      constructor(
        ...args:
          | []
          | [number]
          | [string]
          | [Date]
          | [number, number, number?, number?, number?, number?, number?]
      ) {
        if (args.length === 0) {
          super(now);
          return;
        }

        if (args.length === 1 && args[0] instanceof RealDate) {
          super(args[0].getTime());
          return;
        }

        super(
          ...(args as
            | [number]
            | [string]
            | [number, number, number?, number?, number?, number?, number?]),
        );
      }

      static now() {
        return now;
      }

      static parse(dateString: string) {
        return RealDate.parse(dateString);
      }

      static UTC(...args: Parameters<typeof RealDate.UTC>) {
        return RealDate.UTC(...args);
      }
    }

    Object.defineProperty(globalThis, 'Date', {
      configurable: true,
      writable: true,
      value: MockDate,
    });
  }, fixedTime);

  await page.goto('/');
  await page.goto('/offers');

  await expect(page.getByRole('heading', { name: 'Przygotowane oferty' })).toBeVisible();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const offerRows = page.locator('.offer-row');

  await expect(offerRows.first()).toBeVisible();

  const visibleOfferCount = await offerRows.count();

  expect(visibleOfferCount).toBeGreaterThan(0);
  await expect(page.getByText('Okres ochrony', { exact: true })).toHaveCount(visibleOfferCount);
  await expect(page.getByText('2025/01/15 - 2026/01/15', { exact: true })).toHaveCount(
    visibleOfferCount,
  );
  await captureStep(page, testInfo, 'protection-period-rendered');
});

test('renders a valid leap-day protection period on the offers list', async ({ page }, testInfo) => {
  const fixedTime = new Date('2024-02-29T12:00:00').getTime();

  await page.addInitScript((now) => {
    const RealDate = Date;

    class MockDate extends RealDate {
      constructor(
        ...args:
          | []
          | [number]
          | [string]
          | [Date]
          | [number, number, number?, number?, number?, number?, number?]
      ) {
        if (args.length === 0) {
          super(now);
          return;
        }

        if (args.length === 1 && args[0] instanceof RealDate) {
          super(args[0].getTime());
          return;
        }

        super(
          ...(args as
            | [number]
            | [string]
            | [number, number, number?, number?, number?, number?, number?]),
        );
      }

      static now() {
        return now;
      }

      static parse(dateString: string) {
        return RealDate.parse(dateString);
      }

      static UTC(...args: Parameters<typeof RealDate.UTC>) {
        return RealDate.UTC(...args);
      }
    }

    Object.defineProperty(globalThis, 'Date', {
      configurable: true,
      writable: true,
      value: MockDate,
    });
  }, fixedTime);

  await page.goto('/');
  await page.goto('/offers');

  const offerRows = page.locator('.offer-row');

  await expect(offerRows.first()).toBeVisible();
  await expect(page.getByText('2024/02/29 - 2025/02/28', { exact: true }).first()).toBeVisible();
  await captureStep(page, testInfo, 'leap-day-protection-period-visible');

  const visibleOfferCount = await offerRows.count();

  await expect(page.getByText('2024/02/29 - 2025/02/28', { exact: true })).toHaveCount(
    visibleOfferCount,
  );
  await captureStep(page, testInfo, 'leap-day-period-applied-to-all-rows');
});
