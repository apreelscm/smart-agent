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

test.beforeEach(async ({ page }) => {
    await page.addInitScript((user) => {
        localStorage.setItem('auth.currentUser', JSON.stringify(user));
    }, DEMO_USER);

    await page.goto('/');
});

test('opens and closes the map modal for a stationary visit', async ({ page }, testInfo) => {
    await page.goto('/portal/wizyta/w1');

    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await expect(
        page.getByRole('heading', { name: 'Opieka specjalistyczna – Ginekolog' }),
    ).toBeVisible();
    await expect(page.getByTestId('visit-map-action')).toBeVisible();
    await captureStep(page, testInfo, 'stationary-visit-map-action-visible');

    await page.getByTestId('visit-map-action').click();

    const mapModal = page.getByTestId('visit-map-modal');
    const mapFrame = page.getByTestId('visit-map-frame');

    await expect(mapModal).toBeVisible();
    await expect(
        page.getByRole('heading', {
            name: 'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
        }),
    ).toBeVisible();
    await captureStep(page, testInfo, 'map-modal-opened');

    await expect(mapFrame).toBeVisible();
    await expect(mapFrame).toHaveAttribute(
        'src',
        /https:\/\/www\.openstreetmap\.org\/export\/embed\.html\?/,
    );
    await captureStep(page, testInfo, 'map-iframe-rendered');

    await page.getByTestId('visit-map-close').click();

    await expect(page.getByTestId('visit-map-modal')).toHaveCount(0);
    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await expect(
        page.getByRole('heading', { name: 'Opieka specjalistyczna – Ginekolog' }),
    ).toBeVisible();
    await captureStep(page, testInfo, 'map-modal-closed');
});

test('hides the map action for a telemedicine visit', async ({ page }, testInfo) => {
    await page.goto('/portal/wizyta/w2');

    await expect(page).toHaveURL(/\/portal\/wizyta\/w2$/);
    await expect(
        page.getByRole('heading', { name: 'Konsultacja telemedyczna – Internista' }),
    ).toBeVisible();
    await expect(page.getByText('Wideokonsultacja online')).toBeVisible();
    await captureStep(page, testInfo, 'telemedicine-visit-details');

    await expect(page.getByTestId('visit-map-action')).toHaveCount(0);
    await expect(page.getByTestId('visit-map-modal')).toHaveCount(0);
    await captureStep(page, testInfo, 'telemedicine-without-map-action');
});
