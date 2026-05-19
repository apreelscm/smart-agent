import { expect, test } from '@playwright/test';
import { captureStep } from './helpers/visual-snapshot';

const VALID_PESEL = '82030312340';
const INVALID_CHECKSUM_PESEL = '82030312341';

test('fills date of birth from a valid PESEL and keeps the choice after returning', async ({
    page,
}, testInfo) => {
    await page.goto('/kalkulator/kto-ty');

    await expect(page).toHaveURL(/\/kalkulator\/kto-ty$/);
    await expect(page.getByRole('heading', { name: 'Kilka informacji o Tobie' })).toBeVisible();
    await captureStep(page, testInfo, 'personal-info-step-loaded');

    await page.getByLabel('Imię').fill('Anna');
    await page.getByLabel('Nazwisko').fill('Kowalska');
    await page.getByRole('button', { name: 'PESEL' }).click();

    const peselInput = page.getByLabel('Numer PESEL');
    const birthDateInput = page.getByLabel('Podaj swoją datę urodzenia');

    await expect(peselInput).toBeVisible();
    await expect(birthDateInput).toHaveJSProperty('readOnly', true);
    await captureStep(page, testInfo, 'pesel-mode-enabled');

    await peselInput.fill(VALID_PESEL);

    await expect(birthDateInput).toHaveValue('1982-03-03');
    await expect(
        page.getByText(
            'Data urodzenia jest uzupełniana automatycznie na podstawie poprawnego numeru PESEL.',
        ),
    ).toBeVisible();
    await captureStep(page, testInfo, 'birth-date-decoded-from-pesel');

    const nextButton = page.getByRole('button', { name: 'Dalej' });

    await expect(nextButton).toBeEnabled();
    await nextButton.click();

    await expect(page).toHaveURL(/\/kalkulator\/rejestracja$/);
    await captureStep(page, testInfo, 'registration-step-opened');

    await page.goBack();

    await expect(page).toHaveURL(/\/kalkulator\/kto-ty$/);
    await expect(page.getByRole('button', { name: 'PESEL' })).toHaveClass(/active/);
    await expect(peselInput).toHaveValue(VALID_PESEL);
    await expect(birthDateInput).toHaveValue('1982-03-03');
    await captureStep(page, testInfo, 'pesel-state-restored-after-return');
});

test('shows a validation error for invalid PESEL and allows switching back to manual date entry', async ({
    page,
}, testInfo) => {
    await page.goto('/kalkulator/kto-ty');

    await expect(page).toHaveURL(/\/kalkulator\/kto-ty$/);

    await page.getByLabel('Imię').fill('Jan');
    await page.getByLabel('Nazwisko').fill('Nowak');
    await page.getByRole('button', { name: 'PESEL' }).click();

    const peselInput = page.getByLabel('Numer PESEL');
    const birthDateInput = page.getByLabel('Podaj swoją datę urodzenia');
    const nextButton = page.getByRole('button', { name: 'Dalej' });

    await peselInput.fill(INVALID_CHECKSUM_PESEL);

    await expect(page.getByText('PESEL ma nieprawidłową sumę kontrolną.')).toBeVisible();
    await expect(birthDateInput).toHaveValue('');
    await expect(nextButton).toBeDisabled();
    await captureStep(page, testInfo, 'invalid-pesel-error');

    await page.getByRole('button', { name: 'Data urodzenia' }).click();

    await expect(peselInput).toBeHidden();
    await expect(birthDateInput).toHaveJSProperty('readOnly', false);
    await captureStep(page, testInfo, 'manual-date-mode-restored');

    await birthDateInput.fill('1990-05-12');

    await expect(birthDateInput).toHaveValue('1990-05-12');
    await expect(nextButton).toBeEnabled();
    await captureStep(page, testInfo, 'manual-date-entry-valid');
});
