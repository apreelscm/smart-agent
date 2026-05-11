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

function normalizeText(value: string): string {
    return value.replace(/\s+/g, ' ').trim();
}

function normalizeHeader(value: string): string {
    return normalizeText(value).replace(/[↑↓]/g, '').trim();
}

test('renders Email PH immediately before PH on the process list', async ({ page }, testInfo) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page).toHaveURL(/\/processes(?:\/)?(?:\?.*)?$/);
    await expect(page.locator('tbody tr').first()).toBeVisible();
    await captureStep(page, testInfo, 'process-list-loaded');

    const headers = (await page.locator('thead th').allInnerTexts()).map((value) =>
        normalizeHeader(value),
    );

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
    await captureStep(page, testInfo, 'headers-visible');

    const segmentIndex = headers.indexOf('Segment');
    const emailPhIndex = headers.indexOf('Email PH');
    const phIndex = headers.indexOf('PH');
    const statusIndex = headers.indexOf('Status');

    expect(emailPhIndex).toBe(segmentIndex + 1);
    expect(phIndex).toBe(emailPhIndex + 1);
    expect(statusIndex).toBe(phIndex + 1);
    await captureStep(page, testInfo, 'header-order-verified');

    const firstRowCells = page.locator('tbody tr').first().locator('td');
    const firstRowValues = (await firstRowCells.allInnerTexts()).map((value) =>
        normalizeText(value),
    );

    expect(firstRowValues[emailPhIndex]).toContain('@');
    expect(firstRowValues[phIndex]).not.toContain('@');
    expect(firstRowValues[phIndex]).not.toBe('');
    expect(firstRowValues[statusIndex]).not.toBe('');
    await captureStep(page, testInfo, 'first-row-email-ph-before-ph');
});

test('keeps sorting, filtering, pagination and details navigation working', async (
    { page },
    testInfo,
) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Lista procesów' })).toBeVisible();
    await expect(page.locator('tbody tr').first()).toBeVisible();
    await captureStep(page, testInfo, 'default-list-visible');

    const initialFirstRowId = Number(
        normalizeText(await page.locator('tbody tr').first().locator('td').first().innerText()),
    );

    await page.getByTestId('sort-id').click();
    await expect(page.getByText(/Sortowanie:\s*ID\s*\(rosnąco\)/)).toBeVisible();

    const sortedFirstRowId = Number(
        normalizeText(await page.locator('tbody tr').first().locator('td').first().innerText()),
    );

    expect(sortedFirstRowId).toBeLessThan(initialFirstRowId);
    await captureStep(page, testInfo, 'sorted-by-id');

    await page.getByTestId('nip-filter').fill('1234567890');
    await page.getByTestId('observed-filter').check();
    await page.getByTestId('apply-filters-button').click();

    await expect(page.locator('tbody tr')).toHaveCount(1);
    await expect(page.getByText('Firma Helios Sp. z o.o.')).toBeVisible();
    await captureStep(page, testInfo, 'filtered-results');

    await page.getByTestId('clear-filters-button').click();

    await expect(page.locator('tbody tr')).toHaveCount(10);
    await expect(page.getByText(/Sortowanie:\s*ID\s*\(malejąco\)/)).toBeVisible();
    await captureStep(page, testInfo, 'filters-cleared');

    await page.getByTestId('page-2').click();
    await expect(page.getByText(/Strona 2 z \d+/)).toBeVisible();

    const secondPageFirstRowId = normalizeText(
        await page.locator('tbody tr').first().locator('td').first().innerText(),
    );

    expect(secondPageFirstRowId).toBe('1008');
    await captureStep(page, testInfo, 'second-page-visible');

    await page.getByTestId('detail-link-1008').click();
    await expect(page).toHaveURL(/\/processes\/1008$/);
    await captureStep(page, testInfo, 'process-details-opened');
});
