import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const expectedHeaders = [
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
];

const authenticatedUser = {
  username: 'admin',
  name: 'Administrator',
  role: 'EUM_ADMINISTRATOR',
  email: 'admin@eumowy.local',
  phone: '',
  auwId: 1,
} as const;

const phEmailHeaderIndex = expectedHeaders.indexOf('Email PH');
const phHeaderIndex = expectedHeaders.indexOf('PH');

function normalizeText(value: string | null | undefined): string {
  return (value ?? '')
    .replace(/[↑↓]/g, '')
    .replace(/\s+/g, ' ')
    .trim();
}

async function openProcessList(page: Parameters<typeof test.beforeEach>[0]['page']): Promise<void> {
  await page.addInitScript((user) => {
    window.localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, authenticatedUser);

  await page.goto('/processes');

  await expect(page.locator('app-root')).toBeVisible();
  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await expect(page.locator('table')).toBeVisible();
}

test.beforeEach(async ({ page }) => {
  await openProcessList(page);
});

test('renders Email PH immediately before PH in the process list table', async (
  { page },
  testInfo,
) => {
  await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
  await captureStep(page, testInfo, 'process-list-loaded');

  const headers = (await page.locator('thead th').allTextContents()).map((header) =>
    normalizeText(header),
  );

  expect(headers).toEqual(expectedHeaders);
  expect(phHeaderIndex).toBe(phEmailHeaderIndex + 1);
  await captureStep(page, testInfo, 'headers-order-verified');

  const firstRowCells = page.locator('tbody tr').first().locator('td');

  await expect(page.locator('tbody tr')).toHaveCount(10);
  expect(await firstRowCells.count()).toBe(expectedHeaders.length);
  await expect(firstRowCells.nth(phEmailHeaderIndex)).toContainText('@');

  const phNameText = normalizeText(await firstRowCells.nth(phHeaderIndex).textContent());

  expect(phNameText.length).toBeGreaterThan(0);
  expect(phNameText).not.toContain('@');

  await expect(
    page.locator('tbody tr').first().getByRole('link', { name: 'Szczegóły' }),
  ).toBeVisible();
  await captureStep(page, testInfo, 'first-row-column-order-verified');
});

test('keeps filtering working with the reordered Email PH and PH columns', async (
  { page },
  testInfo,
) => {
  await page.getByTestId('nip-filter').fill('1234567890');
  await page.getByTestId('observed-filter').check();
  await page.getByTestId('apply-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(1);
  await expect(page.getByText('Firma Helios Sp. z o.o.')).toBeVisible();
  await captureStep(page, testInfo, 'filtered-single-result');

  await page.getByTestId('clear-filters-button').click();

  await expect(page.locator('tbody tr')).toHaveCount(10);
  await expect(page.getByTestId('nip-filter')).toHaveValue('');
  await expect(page.getByTestId('observed-filter')).not.toBeChecked();
  await captureStep(page, testInfo, 'filters-cleared-default-list');
});

test('keeps sorting, pagination, and detail navigation working after the column change', async (
  { page },
  testInfo,
) => {
  await page.getByTestId('sort-id').click();

  await expect(page.getByText(/Sortowanie:\s*ID\s*\(rosnąco\)/)).toBeVisible();
  await captureStep(page, testInfo, 'sorting-changed-to-ascending');

  await page.getByTestId('page-2').click();

  await expect(page.getByText(/Strona 2 z \d+/)).toBeVisible();
  await expect(page.locator('tbody tr')).toHaveCount(4);
  await captureStep(page, testInfo, 'second-page-visible');

  await page.locator('tbody tr').first().getByRole('link', { name: 'Szczegóły' }).click();

  await expect(page).toHaveURL(/\/processes\/\d+$/);
  await captureStep(page, testInfo, 'process-detail-opened');
});
