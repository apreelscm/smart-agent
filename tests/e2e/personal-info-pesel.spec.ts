import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const VALID_PESEL = '44051401359';
const INVALID_PESEL = '44051401358';

test('defaults to PESEL mode and updates the date only for a valid PESEL', async ({
    page,
}, testInfo) => {
    await page.addInitScript(() => {
        window.localStorage.clear();
        window.sessionStorage.clear();
    });

    await page.goto('/kalkulator/kto-ty');

    const heading = page.getByRole('heading', { name: 'Kilka informacji o Tobie' });
    const peselModeButton = page.getByRole('button', { name: /PESEL/ });
    const dateOfBirthModeButton = page.getByRole('button', { name: /Data urodzenia/ });
    const firstNameInput = page.getByLabel('Imię');
    const lastNameInput = page.getByLabel('Nazwisko');
    const peselInput = page.getByLabel('Podaj numer PESEL');
    const dateOfBirthInput = page.getByLabel('Podaj swoją datę urodzenia');
    const nextButton = page.locator('app-wizard-card').getByRole('button').last();

    await expect(heading).toBeVisible();
    await expect(peselModeButton).toHaveClass(/selected/);
    await expect(dateOfBirthModeButton).not.toHaveClass(/selected/);
    await expect(peselInput).toBeVisible();
    await expect(dateOfBirthInput).toBeVisible();
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', true);
    await captureStep(page, testInfo, 'default-pesel-mode');

    await firstNameInput.fill('Anna');
    await lastNameInput.fill('Kowalska');
    await peselInput.fill(VALID_PESEL);

    await expect(dateOfBirthInput).toHaveValue('1944-05-14');
    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'valid-pesel-autofills-date');

    await peselInput.fill(INVALID_PESEL);

    await expect(page.getByText('PESEL ma nieprawidłową sumę kontrolną.')).toBeVisible();
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(nextButton).toBeDisabled();
    await captureStep(page, testInfo, 'invalid-pesel-clears-date');
});

test('persists PESEL mode values after continuing and returning to the step', async ({
    page,
}, testInfo) => {
    await page.addInitScript(() => {
        window.localStorage.clear();
        window.sessionStorage.clear();
    });

    await page.goto('/kalkulator/kto-ty');

    const heading = page.getByRole('heading', { name: 'Kilka informacji o Tobie' });
    const peselModeButton = page.getByRole('button', { name: /PESEL/ });
    const firstNameInput = page.getByLabel('Imię');
    const lastNameInput = page.getByLabel('Nazwisko');
    const peselInput = page.getByLabel('Podaj numer PESEL');
    const dateOfBirthInput = page.getByLabel('Podaj swoją datę urodzenia');
    const nextButton = page.locator('app-wizard-card').getByRole('button').last();

    await expect(heading).toBeVisible();

    await firstNameInput.fill('Jan');
    await lastNameInput.fill('Nowak');
    await peselInput.fill(VALID_PESEL);

    await expect(dateOfBirthInput).toHaveValue('1944-05-14');
    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'ready-to-continue-with-pesel');

    await nextButton.click();

    await expect(page).toHaveURL(/\/kalkulator\/rejestracja$/);
    await captureStep(page, testInfo, 'registration-step-after-save');

    await page.goBack();

    await expect(page).toHaveURL(/\/kalkulator\/kto-ty$/);
    await expect(heading).toBeVisible();
    await expect(peselModeButton).toHaveClass(/selected/);
    await expect(firstNameInput).toHaveValue('Jan');
    await expect(lastNameInput).toHaveValue('Nowak');
    await expect(peselInput).toHaveValue(VALID_PESEL);
    await expect(dateOfBirthInput).toHaveValue('1944-05-14');
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', true);
    await captureStep(page, testInfo, 'returned-step-restores-pesel-data');
});

test('switching birth-date modes clears stale values and keeps manual entry working', async ({
    page,
}, testInfo) => {
    await page.addInitScript(() => {
        window.localStorage.clear();
        window.sessionStorage.clear();
    });

    await page.goto('/kalkulator/kto-ty');

    const peselModeButton = page.getByRole('button', { name: /PESEL/ });
    const dateOfBirthModeButton = page.getByRole('button', { name: /Data urodzenia/ });
    const firstNameInput = page.getByLabel('Imię');
    const lastNameInput = page.getByLabel('Nazwisko');
    const peselInput = page.getByLabel('Podaj numer PESEL');
    const dateOfBirthInput = page.getByLabel('Podaj swoją datę urodzenia');
    const nextButton = page.locator('app-wizard-card').getByRole('button').last();
    const peselError = page.getByText('PESEL ma nieprawidłową sumę kontrolną.');

    await firstNameInput.fill('Anna');
    await lastNameInput.fill('Kowalska');
    await peselInput.fill(INVALID_PESEL);

    await expect(peselError).toBeVisible();
    await expect(nextButton).toBeDisabled();
    await captureStep(page, testInfo, 'invalid-pesel-before-mode-switch');

    await dateOfBirthModeButton.click();

    await expect(dateOfBirthModeButton).toHaveClass(/selected/);
    await expect(peselInput).toBeHidden();
    await expect(peselError).toHaveCount(0);
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', false);

    await dateOfBirthInput.fill('1982-03-03');

    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'manual-date-mode-after-switch');

    await peselModeButton.click();

    await expect(peselModeButton).toHaveClass(/selected/);
    await expect(peselInput).toBeVisible();
    await expect(peselInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveValue('');
    await expect(dateOfBirthInput).toHaveJSProperty('readOnly', true);
    await expect(nextButton).toBeDisabled();
    await captureStep(page, testInfo, 'switching-back-clears-manual-date');
});
