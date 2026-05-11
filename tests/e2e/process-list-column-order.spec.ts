import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test('renders the process list with PH immediately before Email PH', async ({ page }, testInfo) => {
    await page.goto('/processes');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr').first()).toBeVisible();
    await captureStep(page, testInfo, 'process-list-loaded');

    const headers = (await page.locator('thead th').allInnerTexts()).map((text) =>
        text.replace(/[↑↓]/g, '').replace(/\s+/g, ' ').trim(),
    );

    expect(headers).toEqual([
        'ID',
        'Numer PH',
        'NIP',
        'Nazwa klienta',
        'Aktywność',
        'Segment',
        'PH',
        'Email PH',
        'Status',
        'Ostatnia zmiana',
        'Data utworzenia',
        'Format dokumentów',
        'Uwagi COA',
        'Uwagi ZRD',
        'Obserwowane',
        'Akcje',
    ]);
    await captureStep(page, testInfo, 'column-order-verified');

    const phColumnIndex = headers.indexOf('PH');
    const phEmailColumnIndex = headers.indexOf('Email PH');
    const firstRowCells = (await page
        .locator('tbody tr')
        .first()
        .locator('td')
        .allInnerTexts()).map((text) => text.replace(/\s+/g, ' ').trim());

    expect(phColumnIndex).toBeGreaterThan(-1);
    expect(phEmailColumnIndex).toBe(phColumnIndex + 1);

    const phCell = firstRowCells[phColumnIndex];
    const phEmailCell = firstRowCells[phEmailColumnIndex];

    expect(phCell).toBeTruthy();
    expect(phCell).not.toContain('@');
    expect(phEmailCell).toContain('@');
    expect(phEmailCell).not.toBe(phCell);
    await captureStep(page, testInfo, 'row-values-verified');
});

test('keeps sorting, filtering, pagination and detail navigation working', async (
    { page },
    testInfo,
) => {
    await page.goto('/processes');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr').first()).toBeVisible();

    const initialFirstIdText = await page.locator('tbody tr').first().locator('td').first().textContent();
    expect(initialFirstIdText).not.toBeNull();

    const initialFirstId = Number(initialFirstIdText?.trim());

    await page.getByTestId('sort-id').click();

    const updatedFirstIdText = await page.locator('tbody tr').first().locator('td').first().textContent();
    expect(updatedFirstIdText).not.toBeNull();

    const updatedFirstId = Number(updatedFirstIdText?.trim());

    expect(updatedFirstId).toBeLessThan(initialFirstId);
    await captureStep(page, testInfo, 'sorting-verified');

    await page.goto('/processes');

    await page.getByTestId('nip-filter').fill('1234567890');
    await page.getByTestId('observed-filter').check();
    await page.getByTestId('apply-filters-button').click();

    await expect(page.locator('tbody tr')).toHaveCount(1);
    await expect(page.getByText('Firma Helios Sp. z o.o.')).toBeVisible();
    await captureStep(page, testInfo, 'filtered-results');

    await page.getByTestId('clear-filters-button').click();

    await expect(page.locator('tbody tr')).toHaveCount(10);
    await expect(page.getByTestId('page-2')).toBeVisible();
    await captureStep(page, testInfo, 'filters-cleared');

    await page.getByTestId('page-2').click();

    await expect(page.locator('tbody tr')).toHaveCount(4);
    await expect(page.locator('tbody tr').first().locator('td').first()).toHaveText('1008');
    await captureStep(page, testInfo, 'pagination-verified');

    await page.getByTestId('detail-link-1008').click();

    await expect(page).toHaveURL(/\/processes\/1008$/);
    await captureStep(page, testInfo, 'detail-navigation-verified');
});
