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

const normalizeText = (text: string): string => text.replace(/\s+/g, ' ').trim();

test('shows Status directly before Kwota (z odsetkami) on the kwitariusze list', async ({ page }, testInfo) => {
  await page.addInitScript((user) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(user));
  }, DEMO_USER);

  await page.goto('/rozliczenia/kwitariusze');
  await expect(page.locator('app-root')).toBeVisible();
  await expect(page).toHaveURL(/\/rozliczenia\/kwitariusze$/);
  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await captureStep(page, testInfo, 'kwitariusze-list-open');

  const table = page.locator('table').first();
  const headerCells = table.locator('tr.mat-mdc-header-row th');

  await expect(table).toBeVisible();

  const statusHeader = headerCells.filter({ hasText: 'Status' });
  const amountHeader = headerCells.filter({ hasText: 'Kwota (z odsetkami)' });

  await expect(statusHeader).toHaveCount(1);
  await expect(amountHeader).toHaveCount(1);
  await expect(statusHeader).toBeVisible();
  await expect(amountHeader).toBeVisible();
  await captureStep(page, testInfo, 'kwitariusze-headers-visible');

  const headerTexts = (await headerCells.allTextContents()).map((text) => normalizeText(text));
  const statusIndex = headerTexts.findIndex((text) => text === 'Status');
  const amountIndex = headerTexts.findIndex((text) => text.includes('Kwota (z odsetkami)'));

  expect(statusIndex).toBeGreaterThan(-1);
  expect(amountIndex).toBeGreaterThan(-1);
  expect(amountIndex).toBe(statusIndex + 1);
  await captureStep(page, testInfo, 'kwitariusze-header-order');

  const firstRow = table.locator('tr.mat-mdc-row').first();

  await expect(firstRow).toBeVisible();
  await expect(firstRow.locator('.status-badge')).toBeVisible();
  await expect(firstRow.locator('.cell-amount__total')).toBeVisible();
  await expect(firstRow.locator('.cell-amount__interest')).toBeVisible();

  const rowColumns = await firstRow.locator('td').evaluateAll((cells) =>
    cells.map((cell) => {
      const columnClass =
        Array.from(cell.classList).find(
          (name) => name.startsWith('mat-column-') || name.startsWith('cdk-column-'),
        ) ?? '';

      return columnClass.replace(/^mat-column-/, '').replace(/^cdk-column-/, '');
    }),
  );
  const statusCellIndex = rowColumns.findIndex((column) => column === 'status');
  const amountCellIndex = rowColumns.findIndex((column) => column === 'amount');

  expect(statusCellIndex).toBeGreaterThan(-1);
  expect(amountCellIndex).toBeGreaterThan(-1);
  expect(amountCellIndex).toBe(statusCellIndex + 1);
  await captureStep(page, testInfo, 'kwitariusze-row-order');
});
