import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('shows Okres ochrony for each visible offer on the offers list', async ({ page }, testInfo) => {
  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });

  const openOffersList = async () => {
    await page.goto('/');

    try {
      await expect(offersHeading).toBeVisible({ timeout: 2_000 });
    } catch {
      await page.goto('/offers');
    }

    await expect(offersHeading).toBeVisible();
  };

  await openOffersList();
  await captureStep(page, testInfo, 'offers-list-loaded');

  const rows = page.locator('.offer-row');
  const rowCount = await rows.count();

  expect(rowCount).toBeGreaterThan(0);

  const expectedProtectionPeriod = await page.evaluate(() => {
    const formatter = new Intl.DateTimeFormat('en-CA', {
      timeZone: 'Europe/Warsaw',
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
    });
    const parts = formatter.formatToParts(new Date());
    const year = Number(parts.find((part) => part.type === 'year')?.value ?? 0);
    const month = Number(parts.find((part) => part.type === 'month')?.value ?? 0);
    const day = Number(parts.find((part) => part.type === 'day')?.value ?? 0);

    const endDate = new Date(Date.UTC(year, month - 1, day));
    endDate.setUTCFullYear(endDate.getUTCFullYear() + 1);
    endDate.setUTCDate(endDate.getUTCDate() - 1);

    const pad = (value: number) => value.toString().padStart(2, '0');

    return `${year}/${pad(month)}/${pad(day)} 00:00 - ${endDate.getUTCFullYear()}/${pad(
      endDate.getUTCMonth() + 1,
    )}/${pad(endDate.getUTCDate())} 23:59`;
  });

  const protectionLabels = page.locator(
    '.offer-row__meta-item--protection-period .offer-row__meta-label',
  );
  const protectionValues = page.locator('.offer-row__meta-item--protection-period strong');

  await expect(protectionLabels).toHaveCount(rowCount);
  await expect(protectionValues).toHaveCount(rowCount);
  await expect(protectionLabels.first()).toHaveText('Okres ochrony');

  const renderedProtectionValues = (await protectionValues.allTextContents()).map((value) =>
    value.trim(),
  );

  expect(renderedProtectionValues).toEqual(
    Array.from({ length: rowCount }, () => expectedProtectionPeriod),
  );

  await captureStep(page, testInfo, 'protection-period-visible-on-each-row');
});

test('keeps the empty state unchanged when filtering hides all offers', async ({ page }, testInfo) => {
  const offersHeading = page.getByRole('heading', { name: 'Przygotowane oferty' });

  const openOffersList = async () => {
    await page.goto('/');

    try {
      await expect(offersHeading).toBeVisible({ timeout: 2_000 });
    } catch {
      await page.goto('/offers');
    }

    await expect(offersHeading).toBeVisible();
  };

  await openOffersList();

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  );
  await searchInput.fill('nie-ma-takiej-oferty-e2e');

  await expect(page.getByText('Brak ofert dla podanych filtrów')).toBeVisible();
  await expect(page.getByText('Zmień kryteria wyszukiwania albo wyczyść filtry.')).toBeVisible();
  await captureStep(page, testInfo, 'offers-empty-state-after-filter');

  const clearFiltersButton = page.getByRole('button', { name: 'Wyczyść wszystkie filtry' });
  await expect(clearFiltersButton).toBeEnabled();
  await clearFiltersButton.click();

  await expect(page.locator('.offer-row').first()).toBeVisible();
  await captureStep(page, testInfo, 'offers-restored-after-clearing-filters');
});
