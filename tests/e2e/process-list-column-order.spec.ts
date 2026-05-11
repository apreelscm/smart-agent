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

const EXPECTED_HEADERS = [
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

const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function normalizeText(value: string): string {
    return value.replace(/[↑↓]/g, '').replace(/\s+/g, ' ').trim();
}

test.beforeEach(async ({ page }) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);
});

test('renders Email PH directly before PH on the process list', async ({ page }, testInfo) => {
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr').first()).toBeVisible();
    await captureStep(page, testInfo, 'process-list-loaded');

    const headers = (await page.locator('thead th').allTextContents()).map(normalizeText);

    expect(headers).toEqual(EXPECTED_HEADERS);
    await captureStep(page, testInfo, 'desktop-header-order');

    const firstRowCells = await page.locator('tbody tr').first().locator('td').evaluateAll((cells) =>
        cells.map((cell) => ({
            text: (cell.textContent ?? '').replace(/\s+/g, ' ').trim(),
            className: cell.className,
        })),
    );

    const phEmailIndex = firstRowCells.findIndex((cell) => cell.className.includes('col-phEmail'));
    const phNameIndex = firstRowCells.findIndex((cell) => cell.className.includes('col-phName'));

    expect(phEmailIndex).toBeGreaterThan(-1);
    expect(phNameIndex).toBeGreaterThan(-1);
    expect(phNameIndex).toBe(phEmailIndex + 1);
    expect(firstRowCells[phEmailIndex]?.text).toMatch(EMAIL_PATTERN);
    expect(firstRowCells[phNameIndex]?.text).not.toBe('');
    expect(firstRowCells[phNameIndex]?.text).not.toBe(firstRowCells[phEmailIndex]?.text);
    await captureStep(page, testInfo, 'desktop-row-order');
});

test('keeps filtering, sorting, pagination, and details navigation working', async (
    { page },
    testInfo,
) => {
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr')).toHaveCount(10);

    await page.getByTestId('nip-filter').fill('1234567890');
    await page.getByTestId('observed-filter').check();
    await page.getByTestId('apply-filters-button').click();

    await expect(page.locator('tbody tr')).toHaveCount(1);
    await expect(page.getByText('Firma Helios Sp. z o.o.')).toBeVisible();
    await captureStep(page, testInfo, 'filtered-single-result');

    await page.getByTestId('clear-filters-button').click();
    await expect(page.locator('tbody tr')).toHaveCount(10);

    const initialFirstId = Number(
        normalizeText((await page.locator('tbody tr').first().locator('td.col-id').textContent()) ?? '0'),
    );

    await page.getByTestId('sort-id').click();
    await expect(page.getByText('Sortowanie: ID (rosnąco)')).toBeVisible();

    const sortedFirstId = Number(
        normalizeText((await page.locator('tbody tr').first().locator('td.col-id').textContent()) ?? '0'),
    );

    expect(sortedFirstId).toBeLessThan(initialFirstId);
    await captureStep(page, testInfo, 'sorted-by-id-asc');

    await page.getByTestId('sort-id').click();
    await expect(page.getByText('Sortowanie: ID (malejąco)')).toBeVisible();

    await page.getByTestId('page-2').click();
    await expect(page.locator('tbody tr')).toHaveCount(4);
    await expect(page.locator('tbody tr').first().locator('td.col-id')).toHaveText('1008');
    await captureStep(page, testInfo, 'second-page');

    await page.getByTestId('detail-link-1008').click();
    await expect(page).toHaveURL(/\/processes\/1008$/);
    await captureStep(page, testInfo, 'process-detail');
});

test('keeps Email PH and PH visible next to each other on mobile', async ({ page }, testInfo) => {
    await page.setViewportSize({ width: 760, height: 900 });
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr').first()).toBeVisible();

    await expect(page.locator('thead th.col-phEmail')).toBeVisible();
    await expect(page.locator('thead th.col-phName')).toBeVisible();

    const visibleHeaders = await page.locator('thead th').evaluateAll((headers) =>
        headers
            .filter((header) => getComputedStyle(header).display !== 'none')
            .map((header) => (header.textContent ?? '').replace(/[↑↓]/g, '').replace(/\s+/g, ' ').trim()),
    );

    const phEmailHeaderIndex = visibleHeaders.indexOf('Email PH');
    const phHeaderIndex = visibleHeaders.indexOf('PH');

    expect(phEmailHeaderIndex).toBeGreaterThan(-1);
    expect(phHeaderIndex).toBeGreaterThan(-1);
    expect(phHeaderIndex).toBe(phEmailHeaderIndex + 1);

    const visibleRowCells = await page.locator('tbody tr').first().locator('td').evaluateAll((cells) =>
        cells
            .filter((cell) => getComputedStyle(cell).display !== 'none')
            .map((cell) => ({
                text: (cell.textContent ?? '').replace(/\s+/g, ' ').trim(),
                className: cell.className,
            })),
    );

    const phEmailCellIndex = visibleRowCells.findIndex((cell) => cell.className.includes('col-phEmail'));
    const phCellIndex = visibleRowCells.findIndex((cell) => cell.className.includes('col-phName'));

    expect(phEmailCellIndex).toBeGreaterThan(-1);
    expect(phCellIndex).toBeGreaterThan(-1);
    expect(phCellIndex).toBe(phEmailCellIndex + 1);
    expect(visibleRowCells[phEmailCellIndex]?.text).toMatch(EMAIL_PATTERN);
    expect(visibleRowCells[phCellIndex]?.text).not.toBe('');
    await captureStep(page, testInfo, 'mobile-adjacent-columns');
});
