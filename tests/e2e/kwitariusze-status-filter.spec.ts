import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const USER_ONE = {
  username: 'agent-one',
  name: 'Agent One',
  role: 'EUM_ADMINISTRATOR',
  email: 'agent-one@eumowy.local',
  phone: '',
  auwId: 'agent-one',
};

const USER_TWO = {
  username: 'agent-two',
  name: 'Agent Two',
  role: 'EUM_ADMINISTRATOR',
  email: 'agent-two@eumowy.local',
  phone: '',
  auwId: 'agent-two',
};

async function seedCurrentUser(page: Parameters<typeof test>[0]['page'], user: typeof USER_ONE): Promise<void> {
  await page.goto('/');
  await page.evaluate((currentUser) => {
    localStorage.clear();
    localStorage.setItem('auth.currentUser', JSON.stringify(currentUser));
  }, user);
}

async function switchCurrentUser(page: Parameters<typeof test>[0]['page'], user: typeof USER_ONE): Promise<void> {
  await page.goto('/');
  await page.evaluate((currentUser) => {
    localStorage.setItem('auth.currentUser', JSON.stringify(currentUser));
  }, user);
}

test('restores selected status after page reload', async ({ page }, testInfo) => {
  await seedCurrentUser(page, USER_ONE);
  await page.goto('/rozliczenia/kwitariusze');

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await captureStep(page, testInfo, 'kwitariusze-list-opened');

  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' }).first();

  await statusChip.click();
  await page
    .locator('.filter-expand--status')
    .getByRole('button', { name: 'Oplacony' })
    .click();

  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 3', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/005' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/009' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toHaveCount(0);
  await captureStep(page, testInfo, 'status-filter-applied');

  await page.reload();

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 3', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toHaveCount(0);
  await captureStep(page, testInfo, 'status-filter-restored-after-reload');
});

test('restores the filter after leaving the screen and clears it via reset', async (
  { page },
  testInfo,
) => {
  await seedCurrentUser(page, USER_ONE);
  await page.goto('/rozliczenia/kwitariusze');

  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' }).first();

  await statusChip.click();
  await page
    .locator('.filter-expand--status')
    .getByRole('button', { name: 'Wystawiony' })
    .click();

  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 5', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toHaveCount(0);
  await captureStep(page, testInfo, 'status-filter-selected-before-navigation');

  await page.goto('/');
  await page.goto('/rozliczenia/kwitariusze');

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 5', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toHaveCount(0);
  await captureStep(page, testInfo, 'status-filter-restored-after-navigation');

  await page.getByText('Zresetuj', { exact: true }).click();

  await expect(statusChip).toContainText('Status');
  await expect(statusChip).not.toContainText('Status (1)');
  await expect(page.getByText('Wyników: 10', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await captureStep(page, testInfo, 'all-filters-reset');

  await page.reload();

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(statusChip).toContainText('Status');
  await expect(statusChip).not.toContainText('Status (1)');
  await expect(page.getByText('Wyników: 10', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await captureStep(page, testInfo, 'reset-persists-as-empty-state');
});

test('keeps remembered status selections isolated per user', async ({ page }, testInfo) => {
  await seedCurrentUser(page, USER_ONE);
  await page.goto('/rozliczenia/kwitariusze');

  const statusChip = page.locator('.filter-chip').filter({ hasText: 'Status' }).first();

  await statusChip.click();
  await page
    .locator('.filter-expand--status')
    .getByRole('button', { name: 'Oplacony' })
    .click();

  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 3', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toHaveCount(0);
  await captureStep(page, testInfo, 'user-one-filter-saved');

  await switchCurrentUser(page, USER_TWO);
  await page.goto('/rozliczenia/kwitariusze');

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(statusChip).toContainText('Status');
  await expect(statusChip).not.toContainText('Status (1)');
  await expect(page.getByText('Wyników: 10', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await captureStep(page, testInfo, 'user-two-sees-default-state');

  await switchCurrentUser(page, USER_ONE);
  await page.goto('/rozliczenia/kwitariusze');

  await expect(page.getByRole('heading', { name: 'Kwitariusze' })).toBeVisible();
  await expect(statusChip).toContainText('Status (1)');
  await expect(page.getByText('Wyników: 3', { exact: true })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/002' })).toBeVisible();
  await expect(page.getByRole('link', { name: 'KW/2025/001' })).toHaveCount(0);
  await captureStep(page, testInfo, 'user-one-filter-restored-again');
});
