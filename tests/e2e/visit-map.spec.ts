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

test('opens the visit map modal and shows facility details with embedded map', async (
    { page },
    testInfo,
) => {
    let geocodeQuery = '';

    await page.route('https://nominatim.openstreetmap.org/search**', async (route) => {
        geocodeQuery = new URL(route.request().url()).searchParams.get('q') ?? '';
        await new Promise((resolve) => setTimeout(resolve, 300));
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify([{ lat: '52.228000', lon: '20.989000' }]),
        });
    });

    await page.goto('/portal/wizyta/w1');

    await expect(page.getByTestId('visit-map-open')).toBeVisible();
    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await captureStep(page, testInfo, 'visit-details-map-action-visible');

    await page.getByTestId('visit-map-open').click();

    await expect(page.getByTestId('visit-map-modal')).toBeVisible();
    await expect(page.getByTestId('visit-map-loading')).toBeVisible();
    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await captureStep(page, testInfo, 'visit-map-modal-loading');

    await expect(page.getByTestId('visit-map-frame-container')).toBeVisible();
    await expect(page.getByTestId('visit-map-modal')).toContainText(
        'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
    );
    await expect(page.getByTestId('visit-map-modal')).toContainText(
        'ul. Kasprzaka 25, 01-224 Warszawa',
    );
    await expect(
        page.getByTestId('visit-map-frame-container').locator('iframe[title="Mapa placówki"]'),
    ).toHaveAttribute('src', /openstreetmap\.org\/export\/embed\.html/);
    await expect(
        page.getByTestId('visit-map-frame-container').locator('iframe[title="Mapa placówki"]'),
    ).toHaveAttribute('src', /marker=52\.228(?:0+)?%2C20\.989(?:0+)?/);
    expect(geocodeQuery).toBe('ul. Kasprzaka 25, 01-224 Warszawa');
    await captureStep(page, testInfo, 'visit-map-modal-loaded');

    await page.getByTestId('visit-map-close').click();

    await expect(page.getByTestId('visit-map-modal')).toHaveCount(0);
    await expect(page.getByTestId('visit-map-open')).toBeVisible();
    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await captureStep(page, testInfo, 'visit-map-modal-closed');
});

test('shows a user-friendly error state when geocoding returns no result', async (
    { page },
    testInfo,
) => {
    await page.route('https://nominatim.openstreetmap.org/search**', async (route) => {
        await route.fulfill({
            status: 200,
            contentType: 'application/json',
            body: JSON.stringify([]),
        });
    });

    await page.goto('/portal/wizyta/w1');

    await expect(page.getByTestId('visit-map-open')).toBeVisible();
    await captureStep(page, testInfo, 'visit-details-before-map-error');

    await page.getByTestId('visit-map-open').click();

    await expect(page.getByTestId('visit-map-modal')).toBeVisible();
    await expect(page.getByTestId('visit-map-error')).toBeVisible();
    await expect(page.getByTestId('visit-map-error')).toContainText(
        'Nie udało się pokazać lokalizacji tej placówki. Spróbuj ponownie później.',
    );
    await expect(page.getByTestId('visit-map-frame-container')).toHaveCount(0);
    await captureStep(page, testInfo, 'visit-map-error-state');
});

test('hides the map action for telemedicine visits', async ({ page }, testInfo) => {
    await page.goto('/portal/wizyta/w2');

    await expect(page.getByText('telemedyczna', { exact: false })).toBeVisible();
    await expect(page.getByTestId('visit-map-open')).toHaveCount(0);
    await captureStep(page, testInfo, 'telemedicine-visit-without-map-action');
});
