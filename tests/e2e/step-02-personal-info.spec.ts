import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

test.beforeEach(async ({ page }) => {
    await page.goto('/');
    await page.goto('/kalkulator/kto-ty');
    await expect(page.locator('h2')).toHaveText('Kilka informacji o Tobie');
});

test('defaults to manual birth date entry and allows continuing', async ({ page }, testInfo) => {
    const firstNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Imię' }) })
        .locator('input');
    const lastNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Nazwisko' }) })
        .locator('input');
    const dateOfBirthInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Podaj swoją datę urodzenia' }) })
        .locator('input');
    const nextButton = page.getByRole('button', { name: /dalej/i });

    await expect(page.locator('h2')).toHaveText('Kilka informacji o Tobie');
    await captureStep(page, testInfo, 'personal-info-loaded');

    await expect(page.locator('.radio-option.selected')).toContainText('Data urodzenia');
    await expect(
        page.locator('.form-group').filter({ hasText: 'Podaj numer PESEL' }),
    ).toHaveCount(0);
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', false);
    await captureStep(page, testInfo, 'manual-date-mode-default');

    await firstNameInput.fill('Jan');
    await lastNameInput.fill('Nowak');
    await dateOfBirthInput.fill('1990-05-12');

    await expect(dateOfBirthInput).toHaveValue('1990-05-12');
    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'manual-date-form-complete');

    await nextButton.click();

    await expect(page).toHaveURL(/\/kalkulator\/rejestracja$/);
    await captureStep(page, testInfo, 'manual-date-next-step');
});

test('accepts PESEL, derives date of birth, and restores saved values on return', async ({ page }, testInfo) => {
    const firstNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Imię' }) })
        .locator('input');
    const lastNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Nazwisko' }) })
        .locator('input');
    const dateOfBirthInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Podaj swoją datę urodzenia' }) })
        .locator('input');
    const peselInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Podaj numer PESEL' }) })
        .locator('input');
    const nextButton = page.getByRole('button', { name: /dalej/i });

    await firstNameInput.fill('Anna');
    await lastNameInput.fill('Kowalska');
    await page.locator('.radio-option').filter({ hasText: 'PESEL' }).click();

    await expect(page.locator('.radio-option.selected')).toContainText('PESEL');
    await expect(page.locator('.readonly-text')).toHaveText(
        'Data urodzenia została odczytana z numeru PESEL.',
    );
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', true);
    await captureStep(page, testInfo, 'pesel-mode-selected');

    await peselInput.fill('82030312340');

    await expect(peselInput).toHaveValue('82030312340');
    await expect(dateOfBirthInput).toHaveValue('1982-03-03');
    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'pesel-decoded-date');

    await nextButton.click();

    await expect(page).toHaveURL(/\/kalkulator\/rejestracja$/);
    await captureStep(page, testInfo, 'pesel-next-step');

    await page.goBack();

    await expect(page).toHaveURL(/\/kalkulator\/kto-ty$/);
    await expect(page.locator('.radio-option.selected')).toContainText('PESEL');
    await expect(peselInput).toHaveValue('82030312340');
    await expect(dateOfBirthInput).toHaveValue('1982-03-03');
    await captureStep(page, testInfo, 'pesel-values-restored');
});

test('shows an error for invalid PESEL and clears inactive method values when switching', async ({ page }, testInfo) => {
    const firstNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Imię' }) })
        .locator('input');
    const lastNameInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Nazwisko' }) })
        .locator('input');
    const dateOfBirthInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Podaj swoją datę urodzenia' }) })
        .locator('input');
    const peselInput = page
        .locator('.form-group')
        .filter({ has: page.locator('label', { hasText: 'Podaj numer PESEL' }) })
        .locator('input');
    const nextButton = page.getByRole('button', { name: /dalej/i });

    await firstNameInput.fill('Piotr');
    await lastNameInput.fill('Zieliński');
    await page.locator('.radio-option').filter({ hasText: 'PESEL' }).click();
    await peselInput.fill('82030312341');

    await expect(page.getByText('Wpisz poprawny numer PESEL.')).toBeVisible();
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(nextButton).toBeDisabled();
    await captureStep(page, testInfo, 'invalid-pesel-error');

    await peselInput.fill('82030312340');

    await expect(page.getByText('Wpisz poprawny numer PESEL.')).toHaveCount(0);
    await expect(dateOfBirthInput).toHaveValue('1982-03-03');
    await captureStep(page, testInfo, 'valid-pesel-before-switch');

    await page.locator('.radio-option').filter({ hasText: 'Data urodzenia' }).click();

    await expect(page.locator('.radio-option.selected')).toContainText('Data urodzenia');
    await expect(
        page.locator('.form-group').filter({ hasText: 'Podaj numer PESEL' }),
    ).toHaveCount(0);
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', false);
    await captureStep(page, testInfo, 'switched-back-to-manual-date');

    await dateOfBirthInput.fill('1994-11-23');
    await page.locator('.radio-option').filter({ hasText: 'PESEL' }).click();

    await expect(page.locator('.radio-option.selected')).toContainText('PESEL');
    await expect(peselInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', true);
    await captureStep(page, testInfo, 'switching-to-pesel-clears-manual-date');
});
