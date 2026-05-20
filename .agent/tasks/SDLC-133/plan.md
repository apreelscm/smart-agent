# SDLC-133: Wartsoci kursowe dla pln powinny byc ukryte

## Specification

### Overview
Naprawa błędu na ekranie `kalkulator/zakres`, gdzie po poprawnym pobraniu kursów NBP widok nadal pokazuje panel danych kursowych także dla domyślnej waluty `PLN`. Zadanie ogranicza się do warstwy prezentacji: dla `PLN` wartości kursowe mają być ukryte, a po przełączeniu na `USD` lub `EUR` panel ma nadal działać bez zmian, korzystając z istniejącej integracji NBP.

### User Stories
- US-001: As a user, I want exchange-rate details hidden when I view prices in PLN, so that I do not see irrelevant conversion metadata for the base currency.
  - Given I am on `kalkulator/zakres` and `PLN` is selected / When rates are loaded successfully / Then the currency selector remains visible, but exchange-rate values are not shown.
- US-002: As a user, I want exchange-rate details shown only for foreign currencies, so that I can understand the conversion source when viewing `USD` or `EUR`.
  - Given I am on `kalkulator/zakres` and rates are available / When I switch to `USD` or `EUR` / Then the screen shows the applied rate, table number, and effective date.
- US-003: As a business owner, I want the PLN flow unchanged apart from hiding exchange metadata, so that premium calculation and navigation continue to work as before.
  - Given I stay in `PLN` or return to `PLN` / When I continue to the next step / Then `totalPremium` is still persisted in PLN and the wizard flow is unchanged.

### Functional Requirements
- FR-001: The step-15 coverage screen shall keep the currency selector visible for all supported currencies.
- FR-002: When `selectedCurrency === 'PLN'`, the exchange metadata block currently rendered from `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` shall not be displayed.
- FR-003: When `selectedCurrency === 'USD'` or `selectedCurrency === 'EUR'` and rates are available, the exchange metadata block shall be displayed.
- FR-004: Hiding exchange values for `PLN` shall not change the existing NBP runtime integration documented in Confluence: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-005: The existing rate-loading behavior shall remain unchanged:
  - `GET https://api.nbp.pl/api/exchangerates/tables/A/today/?format=json`
  - fallback `GET https://api.nbp.pl/api/exchangerates/tables/A/{yyyy-MM-dd}/?format=json`
  - no auth
- FR-006: The existing technical error behavior shall remain unchanged: if rates are unavailable, the screen shall keep `PLN` active and show the technical message.
- FR-007: Price rendering, coverage toggling, and persistence to `PolicyDraftService` shall remain PLN-based and unchanged by this bug fix.

### Edge Cases
- EC-001: Rates load successfully, but the user stays on default `PLN` — metadata panel stays hidden.
- EC-002: User switches from `PLN` to `USD` or `EUR` — metadata panel becomes visible immediately without reloading the page.
- EC-003: User switches from `USD` or `EUR` back to `PLN` — metadata panel is hidden again immediately.
- EC-004: Rates fail to load — the technical error message remains visible in `PLN`; the bug fix must not suppress that error state.
- EC-005: User proceeds to the next step while viewing `PLN` or after switching back to `PLN` — persisted `totalPremium` remains the same as before the fix.

### Success Criteria
- [ ] On `kalkulator/zakres`, the currency selector is still visible in `PLN`.
- [ ] With successful NBP data loaded and `PLN` selected, exchange metadata is not rendered.
- [ ] With successful NBP data loaded and `USD` selected, exchange metadata is rendered.
- [ ] With successful NBP data loaded and `EUR` selected, exchange metadata is rendered.
- [ ] Existing PLN price display and next-step persistence remain unchanged.
- [ ] Existing unavailable-rates error message still appears when NBP data cannot be loaded.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The current implementation in `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` renders the `.currency-meta` block under `@if (hasRates)`, which means the panel is shown even for the base currency `PLN`. The fix should be a narrow UI condition change, not a service or pricing refactor.

This repo already has the full exchange-rate feature in place:
- `src/app/core/services/currency-rates.service.ts` handles NBP table A loading and previous-day fallback.
- `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` keeps `selectedCurrency` local and exposes `hasRates`, `displayRate()`, and formatting helpers.
- `tests/e2e/coverage-currency.spec.ts` and `step-15-coverage.component.spec.ts` cover currency behavior.

Reuse the documented NBP integration as-is from Confluence: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut). This ticket does not introduce any new endpoint, payload, or runtime fallback. No relevant domain guidance was found in the configured `SDLC` Confluence space, so the change should follow the existing Angular standalone-component patterns already present in the repo.

Key decision:
- Treat “wartości kursowe” as the exchange metadata panel (`.currency-meta`) rather than the selector or the PLN premium amounts themselves.
- Keep the selector and rate-loading lifecycle intact.
- Keep the technical error message intact, because it is not the same thing as the exchange-value panel and remains useful in `PLN` fallback mode.

### Task Breakdown

#### Phase 1: Narrow UI behavior fix
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Restrict metadata rendering to foreign currencies | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Change the current `@if (hasRates)` condition for `.currency-meta` so the panel renders only when rates are available and `selectedCurrency !== 'PLN'`. |
| 1.2 | Add an explicit template guard if needed | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Optionally add a small computed getter such as `showExchangeMeta` to keep template logic readable and aligned with current component patterns. |
| 1.3 | Preserve PLN and error-state behavior | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Ensure the selector stays visible for `PLN` and the existing `ratesError` message still renders independently of the hidden metadata panel. |

#### Phase 2: Update regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Adjust unit test expectations for default PLN view | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Replace the current assertion that expects `.currency-meta` content in default `PLN` state with an assertion that the metadata panel is absent while prices remain correct. |
| 2.2 | Preserve foreign-currency metadata assertions | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Keep or extend assertions proving that after switching to `USD`, the metadata panel appears and shows the expected rate/table/date values. |
| 2.3 | Update E2E default-state behavior | `tests/e2e/coverage-currency.spec.ts` | Change the initial PLN scenario so it asserts `.currency-meta` is hidden in default PLN view. |
| 2.4 | Verify metadata reappears after currency switch | `tests/e2e/coverage-currency.spec.ts` | Keep the existing USD/EUR assertions confirming the metadata panel is visible and populated once a foreign currency is selected. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | Hide the exchange metadata block for `PLN` while keeping selector and error message behavior unchanged. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Optionally add a dedicated getter for template readability if used to control metadata visibility. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | MODIFY | Update unit tests to assert hidden metadata for default `PLN` and visible metadata for foreign currencies. |
| `tests/e2e/coverage-currency.spec.ts` | MODIFY | Update Playwright expectations so default `PLN` hides exchange values and foreign currencies still show them. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements