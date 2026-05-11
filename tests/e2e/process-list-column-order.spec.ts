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

function normalizeText(value: string | null | undefined): string {
  return value?.replace(/\s+/g, ' ').trim() ?? '';
}

function normalizeHeaderText(value: string): string {
  return normalizeText(value).replace(/[↑↓]/g, '').trim();
}

test('renders Email PH directly before PH in the process list table', async (
  { page },
  testInfo,
) => {
  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(10);
  await captureStep(page, testInfo, 'process-list-loaded');

  const headers = (await page.locator('thead th').allTextContents()).map(normalizeHeaderText);

  expect(headers).toEqual([
    'ID',
    'Numer PH',
    'NIP',
    'Nazwa klienta',
    'Aktywność',
    'Segment',
    'Email PH',
    'PH',
    'Status',
    'Ostatnia zmiana',
    'Data utworzenia',
    'Format dokumentów',
    'Uwagi COA',
    'Uwagi ZRD',
    'Obserwowane',
    'Akcje',
  ]);
  await captureStep(page, testInfo, 'headers-in-new-order');

  const rowCount = await page.locator('tbody tr').count();
  const rowsToCheck = Math.min(rowCount, 3);

  expect(rowsToCheck).toBeGreaterThan(0);

  for (let rowIndex = 0; rowIndex < rowsToCheck; rowIndex += 1) {
    const row = page.locator('tbody tr').nth(rowIndex);
    const emailText = normalizeText(await row.locator('td.col-phEmail').textContent());
    const phText = normalizeText(await row.locator('td.col-phName').textContent());

    expect(emailText).toContain('@');
    expect(phText.length).toBeGreaterThan(0);
    await expect(row.locator('td').nth(6)).toHaveText(emailText);
    await expect(row.locator('td').nth(7)).toHaveText(phText);
  }

  await captureStep(page, testInfo, 'row-values-stay-aligned');
});

test('keeps Email PH before PH when both columns stay visible on a narrow screen', async (
  { page },
  testInfo,
) => {
  await page.setViewportSize({ width: 768, height: 900 });

  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('tbody tr').first()).toBeVisible();
  await captureStep(page, testInfo, 'responsive-process-list-loaded');

  const visibleHeaders = (await page.locator('thead th:visible').allTextContents()).map(
    normalizeHeaderText,
  );
  const phEmailHeaderIndex = visibleHeaders.indexOf('Email PH');
  const phHeaderIndex = visibleHeaders.indexOf('PH');

  expect(phEmailHeaderIndex).toBeGreaterThan(-1);
  expect(phHeaderIndex).toBe(phEmailHeaderIndex + 1);
  await captureStep(page, testInfo, 'responsive-column-order');
});

test('keeps filtering, sorting, pagination, and detail navigation working', async (
  { page },
  testInfo,
) => {
  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(10);

  const initialFirstId = Number(
    normalizeText(await page.locator('tbody tr').first().locator('td').first().textContent()),
  );

  expect(initialFirstId).toBeGreaterThan(0);

  await page.getByTestId('nip-filter').fill('1234567890');
  await page.getByTestId('observed-filter').check();
  await page.getByTestId('apply-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(1);
  await expect(page.locator('tbody tr').first()).toContainText('Firma Helios Sp. z o.o.');
  await captureStep(page, testInfo, 'filtered-results');

  await page.getByTestId('clear-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(10);
  await expect(page.getByText(/Wyświetlanie \d+–\d+ z \d+ procesów/)).toBeVisible();
  await captureStep(page, testInfo, 'filters-cleared');

  await page.getByTestId('page-2').click();

  await expect(page.getByText('Strona 2 z 2')).toBeVisible();
  await expect(page.locator('tbody tr').first().locator('td').first()).toHaveText('1008');
  await captureStep(page, testInfo, 'pagination-still-works');

  await page.getByTestId('page-1').click();
  await expect(page.getByText('Strona 1 z 2')).toBeVisible();

  await page.getByTestId('sort-id').click();

  const firstSortedRow = page.locator('tbody tr').first();
  const sortedFirstId = Number(
    normalizeText(await firstSortedRow.locator('td').first().textContent()),
  );

  await expect(page.getByText(/Sortowanie: ID \(rosnąco\)/)).toBeVisible();
  expect(sortedFirstId).toBeLessThan(initialFirstId);
  await captureStep(page, testInfo, 'sorting-still-works');

  await firstSortedRow.getByRole('link', { name: 'Szczegóły' }).click();

  await expect(page).toHaveURL(new RegExp(`/processes/${sortedFirstId}$`));
  await captureStep(page, testInfo, 'detail-navigation-still-works');
});
