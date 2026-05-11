import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test.beforeEach(async ({ page }) => {
  await page.goto('/');

  if (!page.url().includes('/processes')) {
    await page.goto('/processes');
  }
});

test('renders PH directly before Email PH and keeps row data aligned', async (
  { page },
  testInfo,
) => {
  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('table')).toBeVisible();
  await captureStep(page, testInfo, 'process-list-loaded');

  const headers = (await page.locator('thead th').allTextContents()).map((text) =>
    text.replace(/\s+/g, ' ').trim(),
  );
  const phHeaderIndex = headers.indexOf('PH');
  const emailPhHeaderIndex = headers.indexOf('Email PH');

  expect(phHeaderIndex).toBeGreaterThan(-1);
  expect(emailPhHeaderIndex).toBe(phHeaderIndex + 1);

  const firstRowCells = (await page.locator('tbody tr').first().locator('td').allTextContents()).map(
    (text) => text.replace(/\s+/g, ' ').trim(),
  );

  expect(firstRowCells[phHeaderIndex]).not.toBe('');
  expect(firstRowCells[phHeaderIndex]).not.toContain('@');
  expect(firstRowCells[emailPhHeaderIndex]).toContain('@');
  await captureStep(page, testInfo, 'column-order-verified');

  const detailLink = page.locator('tbody tr').first().getByRole('link', { name: 'Szczegóły' });

  await expect(detailLink).toBeVisible();
  const detailHref = await detailLink.getAttribute('href');

  expect(detailHref).toMatch(/\/processes\/\d+$/);

  await detailLink.click();
  await expect(page).toHaveURL(/\/processes\/\d+$/);
  await captureStep(page, testInfo, 'process-detail-opened');
});

test('filters the process list and restores the default view after clearing filters', async (
  { page },
  testInfo,
) => {
  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();

  await page.getByTestId('nip-filter').fill('1234567890');
  await page.getByTestId('observed-filter').check();
  await page.getByTestId('apply-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(1);
  await expect(page.getByText('Firma Helios Sp. z o.o.')).toBeVisible();
  await captureStep(page, testInfo, 'filtered-results');

  await page.getByTestId('clear-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(10);
  await expect(page.getByTestId('nip-filter')).toHaveValue('');
  await expect(page.getByTestId('observed-filter')).not.toBeChecked();
  await captureStep(page, testInfo, 'filters-cleared');
});

test('keeps sorting and pagination working on the process list', async ({ page }, testInfo) => {
  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(10);
  await captureStep(page, testInfo, 'default-list-state');

  const initialFirstId = Number(
    (await page.locator('tbody tr').first().locator('td').first().innerText()).trim(),
  );

  await page.getByTestId('sort-id').click();

  await expect(page.getByText(/Sortowanie:\s*ID \(rosnąco\)/)).toBeVisible();

  const sortedFirstId = Number(
    (await page.locator('tbody tr').first().locator('td').first().innerText()).trim(),
  );

  expect(sortedFirstId).toBeLessThan(initialFirstId);
  await captureStep(page, testInfo, 'sorted-by-id-ascending');

  await page.getByTestId('page-2').click();

  await expect(page.getByText('Strona 2 z 2')).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(4);
  await captureStep(page, testInfo, 'second-page-visible');
});
