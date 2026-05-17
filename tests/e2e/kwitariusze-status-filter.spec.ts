import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const DEMO_USER = {
  username: 'admin',
  name: 'Administrator',
  role: 'EUM_ADMINISTRATOR',
  email: 'admin@eumowy.local',
  phone: '',
  auwId: 1,
};

test.beforeEach(async ({ page }) => {
  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.goto('/');
  await page.goto('/rozliczenia/kwitariusze');

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
});

test('filters kwitariusze by multiple statuses and clears the filter', async ({ page }, testInfo) => {
  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' });
  const resultsCount = page.locator('.results-count');
  const tableStatuses = page.locator('table td .status-badge');

  await expect(statusChip).toBeVisible();
  await expect(resultsCount).toHaveText('Wyników: 10');
  await captureStep(page, testInfo, 'kwitariusze-list-loaded');

  await statusChip.click();
  await page.getByRole('button', { name: /Oplacony/i }).click();
  await page.getByRole('button', { name: /Oczekujacy/i }).click();

  await expect(statusChip).toContainText('Status (2)');
  await expect(resultsCount).toHaveText('Wyników: 4');
  await captureStep(page, testInfo, 'multi-status-filter-applied');

  await expect(tableStatuses).toHaveCount(4);

  const visibleStatuses = (await tableStatuses.allTextContents()).map((text) =>
    text.trim().toLowerCase(),
  );

  expect(visibleStatuses.every((status) => ['oplacony', 'oczekujacy'].includes(status))).toBeTruthy();
  expect(visibleStatuses).toContain('oplacony');
  expect(visibleStatuses).toContain('oczekujacy');
  await captureStep(page, testInfo, 'filtered-statuses-visible-in-table');

  await page.getByRole('button', { name: 'Zresetuj' }).click();

  await expect(resultsCount).toHaveText('Wyników: 10');
  await expect(statusChip).not.toContainText('(2)');
  await captureStep(page, testInfo, 'status-filter-cleared');
});

test('shows the no-data row when status and policy filters leave no matches', async (
  { page },
  testInfo,
) => {
  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' });
  const policyChip = page.locator('.filter-chip').filter({ hasText: 'Numer polisy' });
  const resultsCount = page.locator('.results-count');
  const noDataRow = page.locator('.no-data-row');

  await statusChip.click();
  await page.getByRole('button', { name: /Oplacony/i }).click();

  await expect(resultsCount).toHaveText('Wyników: 3');
  await captureStep(page, testInfo, 'single-status-filter-applied');

  await policyChip.click();
  await page.getByPlaceholder('Wpisz numer polisy…').fill('BRAK-WYNIKOW');

  await expect(resultsCount).toHaveText('Wyników: 0');
  await expect(noDataRow).toHaveText('Brak kwitariuszy spełniających wybrane filtry.');
  await captureStep(page, testInfo, 'no-data-row-visible');
});

test('resets the status filter after page reload', async ({ page }, testInfo) => {
  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' });
  const resultsCount = page.locator('.results-count');

  await statusChip.click();
  await page.getByRole('button', { name: /Oplacony/i }).click();

  await expect(statusChip).toContainText('Status (1)');
  await expect(resultsCount).toHaveText('Wyników: 3');
  await captureStep(page, testInfo, 'status-filter-set-before-reload');

  await page.reload();

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(resultsCount).toHaveText('Wyników: 10');
  await expect(statusChip).not.toContainText('(1)');

  await statusChip.click();
  await expect(page.locator('.status-option--selected')).toHaveCount(0);
  await captureStep(page, testInfo, 'status-filter-reset-after-reload');
});
