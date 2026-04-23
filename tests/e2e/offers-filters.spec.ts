import { expect, test } from '@playwright/test'
import { captureStep } from './helpers/visual-snapshot'

test('offers toolbar shows the updated clear filters label', async ({ page }, testInfo) => {
  await page.goto('/')

  await expect(page.getByText('Przygotowane oferty')).toBeVisible()
  await captureStep(page, testInfo, 'offers-page-loaded')

  const clearButton = page.getByRole('button', { name: 'Wyczyść' })

  await expect(clearButton).toBeVisible()
  await expect(clearButton).toBeDisabled()
  await expect(page.getByRole('button', { name: 'Wyczyść wszystkie filtry' })).toHaveCount(0)
  await captureStep(page, testInfo, 'offers-clear-button-label')
})

test('clear button resets an active search filter to the default offers view', async ({ page }, testInfo) => {
  await page.goto('/')

  const searchInput = page.getByPlaceholder(
    'Szukaj: numer oferty, klient, pojazd/uprawy, rejestracja',
  )
  const clearButton = page.getByRole('button', { name: 'Wyczyść' })
  const resultsMeta = page.locator('.results-meta span')
  const emptyStateTitle = page.getByText('Brak ofert dla podanych filtrów')

  await expect(searchInput).toBeVisible()
  await expect(resultsMeta).toBeVisible()

  const initialResultsText = ((await resultsMeta.textContent()) ?? '').trim()

  expect(initialResultsText).not.toBe('')
  await captureStep(page, testInfo, 'offers-default-state')

  await searchInput.fill('zzzz-no-match')

  await expect(clearButton).toBeEnabled()
  await expect(resultsMeta).toHaveText('0 ofert(y) na liście')
  await expect(emptyStateTitle).toBeVisible()
  await captureStep(page, testInfo, 'offers-filtered-empty-state')

  await clearButton.click()

  await expect(searchInput).toHaveValue('')
  await expect(clearButton).toBeDisabled()
  await expect(resultsMeta).toHaveText(initialResultsText)
  await expect(emptyStateTitle).toHaveCount(0)
  await captureStep(page, testInfo, 'offers-cleared-filters-default-state')
})
