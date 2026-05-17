# SDLC-118: Obsluga walut na ekranie z lista kwitariuszy

## Specification

### Overview
Dodanie wyboru waluty prezentacji na ekranie listy kwitariuszy (`/rozliczenia/kwitariusze`) tak, aby użytkownik mógł przełączać widoczne kwoty między PLN, USD i EUR bez zmiany danych źródłowych. Domyślnie ekran ma startować w PLN, a po wyborze USD/EUR wszystkie kwoty widoczne na liście mają być spójnie przeliczane z użyciem aktualnego kursu NBP. Przy braku kursu system ma spróbować użyć kursu z dnia poprzedniego, a przy dalszym niepowodzeniu ma pokazać błąd i pozostawić widok bez częściowo przeliczonych danych.

### User Stories
- US-001: As a user reviewing the kwitariusze list, I want to switch the display currency, so that I can compare amounts in PLN, USD, or EUR.
  - Given the user opens `/rozliczenia/kwitariusze` / When the page loads / Then the currency selector is visible next to the existing filters and defaults to PLN
- US-002: As a user, I want all money values on the screen to switch together, so that I do not compare mixed currencies in one view.
  - Given the list is visible / When the user selects USD or EUR / Then both the total amount and interest amount in every visible row are recalculated and formatted in the selected currency
- US-003: As a business owner, I want conversions to use the NBP reference rate, so that displayed values come from the approved external source.
  - Given the user changes currency to USD or EUR / When the application fetches the exchange rate / Then it uses the latest NBP publication, falls back to the previous day if needed, and shows an error without changing displayed values if both attempts fail

### Functional Requirements
- FR-001: Add a currency selector to `src/app/features/kwitariusze/kwitariusze.html` in the filters row, adjacent to existing filters.
- FR-002: Supported display currencies must be exactly `PLN`, `USD`, and `EUR`.
- FR-003: The default selected currency must be `PLN` on first load, refresh, and re-entry to the page.
- FR-004: Currency selection must be component-local only; it must not be persisted in `localStorage`, router params, or shared application state.
- FR-005: All monetary values currently shown on the list view must use the selected currency. In the current template this includes:
  - `Kwota (z odsetkami)` total
  - `odsetki` value in each row
- FR-006: Conversion must always be calculated from source PLN values stored in the row model, not from already converted display values.
- FR-007: Amounts must be rounded to 2 decimal places and formatted as currency values for the selected currency.
- FR-008: Selecting `USD` or `EUR` must trigger runtime exchange-rate retrieval from the documented NBP integration, not from local mock JSON files.
- FR-009: The runtime default NBP integration must call the documented public API endpoint for table A rates:
  - latest rate: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`
  - fallback date rate: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/{date}/?format=json`
  - auth: none, per the public API description on Confluence
  - response shape used by the app: `table`, `currency`, `code`, `rates[0].effectiveDate`, `rates[0].mid`
- FR-010: For PLN -> foreign conversion, the app must use `converted = amountPln / midRate`.
- FR-011: If the latest-rate call fails or returns no usable rate, the app must retry once with the previous calendar day.
- FR-012: If both NBP requests fail, the app must show a visible error message on the page and keep the previously displayed currency/amounts unchanged.
- FR-013: While a currency change request is in progress, the selector should prevent overlapping changes to avoid race conditions.
- FR-014: Existing filtering behavior in `KwitariuszService` must remain unchanged.

### Edge Cases
- EC-001: If the user selects `PLN`, no NBP request is made and values are displayed directly from source amounts.
- EC-002: If the user switches from USD to EUR or back, conversion is recalculated from original PLN values to avoid cumulative rounding drift.
- EC-003: If the same foreign currency is selected repeatedly during one page visit, the app should reuse the already fetched rate for that currency instead of refetching unnecessarily.
- EC-004: If the latest NBP endpoint is unavailable on weekends/holidays or due to temporary API issues, the app retries with the previous day before failing.
- EC-005: If both NBP requests fail, the selector must not leave the UI in a partially converted state.
- EC-006: Refreshing the browser or navigating away and back resets the selector to PLN.
- EC-007: If the rate is fetched successfully but the payload is malformed or missing `rates[0].mid`, treat it as a fetch failure and execute fallback/error handling.

### Success Criteria
- [ ] Currency selector is visible on the kwitariusze list next to the filters
- [ ] Default currency is PLN on initial load and after refresh/re-entry
- [ ] Selecting USD converts every visible list amount and interest value to USD
- [ ] Selecting EUR converts every visible list amount and interest value to EUR
- [ ] Selecting PLN restores display to PLN source values without mutating data
- [ ] Conversion uses NBP latest rate, with previous-day fallback on failure
- [ ] Failed latest and fallback requests show one clear error message and do not partially convert the list
- [ ] Displayed converted values are rounded to 2 decimal places

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The repo is a standalone Angular 21 SPA using feature-local templates/styles and signal-based state management:
- feature component pattern: `src/app/features/kwitariusze/kwitariusze.ts`
- in-memory domain state and computed filters: `src/app/core/services/kwitariusz.service.ts`
- inline alert pattern already exists in the kwitariusze feature flow: `src/app/features/kwitariusze/new-kwitariusz/new-kwitariusz.html`

The implementation should keep those patterns:
1. **Leave source kwitariusz data untouched** in `KwitariuszService`.
2. **Add a dedicated NBP integration service** in `src/app/core/services` for fetching rates.
3. **Keep display-currency state inside `KwitariuszeComponent`** so it resets to PLN whenever the component is recreated.
4. **Render converted amounts only in the presentation layer** via helper methods in the component.
5. **Use real NBP HTTP calls as the runtime default**, not mock data files.

Reuse of existing documented service:
- Reuse the documented NBP integration from Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- Runtime default in the plan:
  - latest: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`
  - fallback: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/{yyyy-mm-dd}/?format=json`
  - auth: none
  - payload fields consumed: `rates[0].mid` and `rates[0].effectiveDate`

Domain discovery result:
- No relevant ADR/convention/glossary page was found in the configured `SDLC` space for this feature, so the implementation should follow repository-local patterns already present in the Angular codebase.

Key design decisions:
- **HTTP enablement:** `src/app/app.config.ts` currently does not provide `HttpClient`; add `provideHttpClient()`.
- **UI control:** implement a lightweight styled native `<select>` in the existing filters row rather than introducing new Angular Material form-field patterns not currently used on this screen.
- **Formatting:** replace the hardcoded PLN-only formatter for this screen with `Intl.NumberFormat('pl-PL', { style: 'currency', currency, minimumFractionDigits: 2, maximumFractionDigits: 2 })` so USD/EUR are formatted correctly while still matching Polish locale conventions.
- **Caching:** cache successful USD/EUR rates in component memory for the current page session only. This improves UX without violating the “do not remember user choice” requirement.
- **Failure behavior:** on failed latest + fallback fetch, keep the previous currency active, preserve existing displayed amounts, and show one inline error banner above the table.

When integrating with the external service documented in Confluence, mocks must be limited to the test layer only:
- unit tests for the NBP service should use `HttpTestingController`
- component tests may stub the service
- production code must always use the real NBP endpoint

Operational note:
- No credentials/secrets are required for the public NBP API.
- Deployment/runtime must allow outbound HTTPS access to `api.nbp.pl`.

### Task Breakdown

#### Phase 1: Add NBP exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Enable Angular HTTP client | `src/app/app.config.ts` | Add `provideHttpClient()` so the SPA can call the public NBP API |
| 1.2 | Add exchange-rate types | `src/app/core/models/currency.model.ts` | Define `DisplayCurrency`, foreign-currency types, NBP response interface, and normalized rate snapshot type |
| 1.3 | Create NBP rate service | `src/app/core/services/nbp-exchange-rate.service.ts` | Implement latest-rate fetch, previous-day fallback, payload validation, and normalized `{ code, rate, effectiveDate }` output |
| 1.4 | Add service tests | `src/app/core/services/nbp-exchange-rate.service.spec.ts` | Verify latest success, previous-day fallback, malformed response handling, and double-failure propagation with `HttpTestingController` |

#### Phase 2: Update kwitariusze list UI and conversion logic
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add component currency state | `src/app/features/kwitariusze/kwitariusze.ts` | Add `selectedCurrency`, `isRateLoading`, `currencyError`, cached rates, and `changeCurrency()` handler |
| 2.2 | Keep conversion presentation-only | `src/app/features/kwitariusze/kwitariusze.ts` | Add helpers to compute converted total/interest from source PLN fields and format them using `Intl.NumberFormat` |
| 2.3 | Preserve default/reset behavior | `src/app/features/kwitariusze/kwitariusze.ts` | Initialize selector to PLN and avoid any persistence outside component lifecycle |
| 2.4 | Add selector and error UI | `src/app/features/kwitariusze/kwitariusze.html` | Insert the currency dropdown beside existing filters and render inline loading/error messaging near the filters/table |
| 2.5 | Update amount rendering | `src/app/features/kwitariusze/kwitariusze.html` | Replace PLN-only `formatAmount(totalAmount(row))` / `formatAmount(row.interest)` calls with currency-aware display helpers |
| 2.6 | Style selector/error states | `src/app/features/kwitariusze/kwitariusze.scss` | Add styling for the currency control, disabled/loading state, and inline error alert matching existing screen aesthetics |

#### Phase 3: Add component coverage and verify behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add kwitariusze component tests | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Test default PLN state, successful USD/EUR conversion, PLN reset, and failure behavior keeping previous values |
| 3.2 | Verify no filter regressions | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Confirm existing filter toggles and result count still work with the new currency state present |
| 3.3 | Manual verification | `src/app/features/kwitariusze/kwitariusze.ts`, `src/app/features/kwitariusze/kwitariusze.html` | Validate selector placement, conversion consistency, fallback messaging, and reset-on-refresh behavior in the browser |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Register Angular HTTP client providers |
| `src/app/core/models/currency.model.ts` | CREATE | Currency and NBP response typings used by the new integration |
| `src/app/core/services/nbp-exchange-rate.service.ts` | CREATE | Real runtime integration with NBP latest/fallback rate endpoints |
| `src/app/core/services/nbp-exchange-rate.service.spec.ts` | CREATE | Unit tests for endpoint selection, fallback, and failure cases |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Add display-currency state, conversion helpers, and async rate-fetch flow |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Add currency selector, error/loading UI, and currency-aware amount bindings |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style the new selector and error banner within the existing filters layout |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | CREATE | Component tests for default currency, conversion, and failure handling |

### Verification Steps
1. [ ] Build succeeds (`npm run build`)
2. [ ] Tests pass (`npm test -- --watch=false`)
3. [ ] New tests cover requirements (NBP service fallback/error paths and kwitariusze currency-switch scenarios)