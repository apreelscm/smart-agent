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

test('renders Status before Kwota (z odsetkami) on the kwitariusze list', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/rozliczenia/kwitariusze');

    await expect(page).toHaveURL(/\/rozliczenia\/kwitariusze$/);
    await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
    await expect(page.locator('table.data-table')).toBeVisible();
    await captureStep(page, testInfo, 'kwitariusze-list-loaded');

    const headerCells = page.locator('tr.mat-mdc-header-row th.mat-mdc-header-cell');
    await expect(headerCells).toHaveCount(8);

    const headerColumns = await headerCells.evaluateAll((cells) =>
        cells.map((cell) => {
            const columnClass = Array.from(cell.classList).find((className) =>
                className.startsWith('mat-column-'),
            );

            return {
                column: columnClass?.replace('mat-column-', '') ?? '',
                text: (cell.textContent ?? '').replace(/\s+/g, ' ').trim(),
                isSortable: cell.querySelector('.mat-sort-header-container') !== null,
            };
        }),
    );

    expect(headerColumns.map((cell) => cell.column)).toEqual([
        'type',
        'number',
        'policyNumber',
        'insuredName',
        'issueDate',
        'status',
        'amount',
        'actions',
    ]);
    expect(headerColumns.find((cell) => cell.column === 'status')?.text).toBe('Status');
    expect(headerColumns.find((cell) => cell.column === 'amount')?.text).toContain(
        'Kwota (z odsetkami)',
    );
    expect(headerColumns.find((cell) => cell.column === 'status')?.isSortable).toBe(false);
    expect(headerColumns.find((cell) => cell.column === 'amount')?.isSortable).toBe(true);
    await captureStep(page, testInfo, 'header-order-verified');

    const firstRow = page.locator('tr.mat-mdc-row').first();

    await expect(firstRow.locator('td.mat-column-status .status-badge')).toBeVisible();
    await expect(firstRow.locator('td.mat-column-amount .cell-amount__total')).toContainText('PLN');
    await expect(firstRow.locator('td.mat-column-amount .cell-amount__interest')).toContainText(
        'odsetki:',
    );
    await captureStep(page, testInfo, 'first-row-status-and-amount-visible');

    await expect(page.locator('th.mat-column-issueDate')).toBeVisible();
    await expect(page.locator('th.mat-column-actions')).toBeVisible();
    await captureStep(page, testInfo, 'surrounding-columns-visible');
});
