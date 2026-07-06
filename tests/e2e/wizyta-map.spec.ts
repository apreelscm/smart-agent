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
});

test('opens and closes the map popup on a visit with coordinates', async ({ page }, testInfo) => {
    await page.goto('/portal/wizyta/w1');

    const facilityBlock = page.getByTestId('visit-facility-block');
    const mapTrigger = page.getByTestId('visit-map-trigger');

    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await expect(facilityBlock).toBeVisible();
    await expect(facilityBlock).toContainText(
        'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
    );
    await expect(facilityBlock).toContainText('ul. Kasprzaka 25, 01-224 Warszawa');
    await expect(mapTrigger).toBeVisible();
    await captureStep(page, testInfo, 'visit-with-location');

    await mapTrigger.click();

    const mapDialog = page.getByTestId('visit-map-dialog');
    const mapFrame = page.getByTestId('visit-map-iframe');

    await expect(mapDialog).toBeVisible();
    await expect(
        page.getByRole('heading', {
            name: 'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
        }),
    ).toBeVisible();
    await expect(mapFrame).toBeVisible();

    const iframeSrc = await mapFrame.getAttribute('src');

    expect(iframeSrc).not.toBeNull();

    if (iframeSrc) {
        const decodedSrc = decodeURIComponent(iframeSrc);

        expect(decodedSrc).toContain('https://www.openstreetmap.org/export/embed.html');
        expect(decodedSrc).toContain('marker=52.230700,20.958300');
        expect(decodedSrc).toContain('bbox=20.948300,52.224700,20.968300,52.236700');
    }

    await captureStep(page, testInfo, 'map-popup-open');

    await page.getByRole('button', { name: 'Zamknij mapę' }).click();

    await expect(mapDialog).toHaveCount(0);
    await expect(page).toHaveURL(/\/portal\/wizyta\/w1$/);
    await expect(mapTrigger).toBeVisible();
    await captureStep(page, testInfo, 'map-popup-closed');
});

test(
    'hides facility details and map action when a visit has no coordinates',
    async ({ page }, testInfo) => {
        await page.goto('/portal/wizyta/w2');

        await expect(page).toHaveURL(/\/portal\/wizyta\/w2$/);
        await expect(page.getByRole('heading', { level: 1 })).toBeVisible();
        await expect(page.getByTestId('visit-facility-block')).toHaveCount(0);
        await expect(page.getByTestId('visit-map-trigger')).toHaveCount(0);
        await expect(page.getByTestId('visit-map-dialog')).toHaveCount(0);
        await expect(page.getByText('Telemedycyna Świat Zdrowia')).toHaveCount(0);
        await expect(page.getByText('Wideokonsultacja online')).toHaveCount(0);
        await captureStep(page, testInfo, 'visit-without-location');
    },
);
