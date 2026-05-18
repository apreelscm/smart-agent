# SDLC-123: Waluty na ekranie kwitariuszy

## Specification

### Overview
This task adds currency switching to the kwitariusze list screen at `src/app/features/kwitariusze/kwitariusze.*`.

Today the screen renders monetary values with a hardcoded PLN formatter in `KwitariuszeComponent.formatAmount()` and derives totals from local mock data in `KwitariuszService`. The change will let the user switch the list display currency to PLN, USD, or EUR from a control placed next to the existing filters, recalculate all visible monetary values on the screen using the current NBP rate, and show the selected rate, table number, and effective date.

The selected currency must be session-local for the current screen only. Unlike the existing status filter persistence, currency choice must **not** be remembered after leaving/reloading the page.

### User Stories
- US-001: As a user of the kwitariusze list, I want to switch the displayed currency to USD or EUR, so that I can review the same list in another currency.
  - Given I am on `/rozliczenia/kwitariusze` / When I choose USD or EUR from the currency control / Then all visible list amounts are recalculated and rendered in the selected currency

- US-002: As a user, I want to see the NBP rate details used for the conversion, so that I know which exchange table and date the screen is using.
  - Given I selected USD or EUR / When the conversion data is loaded / Then the screen shows the rate, NBP table number, and effective date

- US-003: As a user, I do not want the currency selection remembered later, so that the screen always opens in its default PLN state.
  - Given I selected USD or EUR / When I reload the page or return later to the screen / Then the list opens again in PLN

### Functional Requirements
- FR-001: Add a currency selector on the kwitariusze list screen, positioned in the existing filter row in `src/app/features/kwitariusze/kwitariusze.html`.
- FR-002: Support three display currencies on the screen: `PLN` (default), `USD`, and `EUR`.
- FR-003: Keep the source kwitariusz data unchanged in `KwitariuszService`; conversion is a presentation-layer concern for the list screen.
- FR-004: Use the documented NBP integration from Confluence as the runtime default: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-005: Fetch the current NBP exchange table from the documented public API endpoint `GET https://api.nbp.pl/api/exchangerates/tables/A?format=json` with no auth, and use the returned `no`, `effectiveDate`, and `rates[].mid` values as the runtime source of truth.
- FR-006: Read USD and EUR rates from NBP table A and convert the visible kwitariusze monetary values from their current PLN-based values to the selected foreign currency.
- FR-007: Recalculate all amounts visible on the list screen, including:
  - total amount shown in the `Kwota (z odsetkami)` column
  - interest amount shown in the secondary line in the same column
- FR-008: When `PLN` is selected, show original values with no conversion applied.
- FR-009: When `USD` or `EUR` is selected, show the NBP metadata used for conversion: currency code, exchange rate, table number, and effective date.
- FR-010: Replace the current hardcoded PLN formatter in `src/app/features/kwitariusze/kwitariusze.ts` with a currency-aware formatter.
- FR-011: Add HTTP support to the Angular app configuration in `src/app/app.config.ts` so the frontend can call the NBP API at runtime.
- FR-012: Do **not** persist the selected currency in localStorage, router state, or backend storage; re-entering the screen must restore the default `PLN`.
- FR-013: Keep the existing status-filter persistence in `KwitariuszService` unchanged.
- FR-014: Mock JSON responses may be used only in unit/e2e tests via test fixtures/intercepts, never as a production fallback.

### Edge Cases
- EC-001: If the NBP request fails while switching from PLN to USD/EUR, the screen should keep the previous successfully displayed currency and show a non-blocking inline error.
- EC-002: If the NBP response does not contain the selected currency code, the screen should not switch to that currency and should show an inline error.
- EC-003: If the user switches back to PLN, the rate-information block should disappear and the original PLN amounts should be shown again.
- EC-004: If the user toggles between USD and EUR after the latest table has already been loaded, the screen should reuse the in-memory table data instead of refetching on every click.
- EC-005: On weekends or holidays, the latest available NBP table A response should still be accepted as the current runtime rate source.
- EC-006: Reloading the page or navigating away and back must reset currency selection to PLN, even though the status filter may still be restored from its separate persistence logic.

### Success Criteria
- [ ] The kwitariusze list shows a currency control next to the existing filters
- [ ] Selecting USD recalculates all visible list amounts using the current NBP table A rate
- [ ] Selecting EUR recalculates all visible list amounts using the current NBP table A rate
- [ ] The screen shows exchange rate, table number, and effective date for USD/EUR selections
- [ ] Returning the selector to PLN restores original PLN values and hides rate metadata
- [ ] Reloading or reopening the screen resets the currency to PLN
- [ ] Existing kwitariusze filters, sorting, actions, and status-filter persistence continue to work

### Open Questions
- [NEEDS CLARIFICATION: should the new selector label explicitly read "Waluta", or does product expect a different visible label based on the Jira note "pole Lista obok filtrow"?]

---

## Implementation Plan

### Technical Approach
The repo is an Angular 21 standalone app using signals and thin feature components. The kwitariusze list already follows this pattern:
- UI state in `src/app/features/kwitariusze/kwitariusze.ts`
- filter/data state in `src/app/core/services/kwitariusz.service.ts`
- template-driven filter controls and Angular Material table/menu in `src/app/features/kwitariusze/kwitariusze.html`

This task should **not** extend the existing localStorage-based filter persistence in `KwitariuszService`. Currency selection has different behavior from the status filter: it is transient and must reset to PLN on re-entry. The safest implementation is therefore:
1. keep currency selection in `KwitariuszeComponent`
2. add a dedicated NBP integration service for runtime rate loading and in-memory caching
3. leave `KwitariuszService` data and status-filter persistence untouched

Confluence service reuse:
- Reuse the documented NBP integration from [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- Runtime default integration:
  - endpoint: `GET https://api.nbp.pl/api/exchangerates/tables/A?format=json`
  - auth: none
  - expected response shape: latest table A entry containing `table`, `no`, `effectiveDate`, `rates[]`
  - selected rates: `rates[].mid` for `USD` and `EUR`
- No relevant domain guidance was found in the configured `SDLC` space, so implementation should follow repository-local Angular patterns.

Key design decisions:
- Add `provideHttpClient()` in `src/app/app.config.ts`, because the app currently has no HTTP provider.
- Create a new `NbpExchangeRateService` under `src/app/core/services/` rather than embedding `HttpClient` calls in the component.
- Use a single latest-table fetch and extract USD/EUR from one response, because the UI also needs table number and effective date.
- Cache the latest table in memory for the current SPA session to avoid refetching when the user switches between USD and EUR.
- Keep `PLN` as the initial/default currency and never persist the selected currency.
- Convert from the existing kwitariusze monetary values already exposed by the repo (`baseAmount`, `interest`; current mock records carry `finCurrency: 'PLN'`), so no backend/model migration is required.
- Replace the current `formatAmount()` hardcoded `zł` suffix with `Intl.NumberFormat('pl-PL', { style: 'currency', currency })`-based rendering.
- Test the real integration contract in unit tests with `HttpTestingController` and in e2e with Playwright network interception only; production code must call the NBP API directly.

### Task Breakdown

#### Phase 1: Add NBP runtime integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Enable HTTP in app bootstrap | `src/app/app.config.ts` | Add `provideHttpClient()` to the standalone application providers so frontend services can call NBP |
| 1.2 | Introduce exchange-rate types | `src/app/core/models/exchange-rate.model.ts` | Create strongly typed interfaces for supported currency codes, NBP table payload, and mapped quote metadata used by the screen |
| 1.3 | Create NBP integration service | `src/app/core/services/nbp-exchange-rate.service.ts` | Add a dedicated service that calls `https://api.nbp.pl/api/exchangerates/tables/A?format=json`, validates the response, exposes latest table data, and maps USD/EUR quotes |
| 1.4 | Add in-memory latest-table caching | `src/app/core/services/nbp-exchange-rate.service.ts` | Store the most recent successful table in a signal/private field so switching between USD and EUR reuses the same NBP payload during the current app session |
| 1.5 | Handle integration failures explicitly | `src/app/core/services/nbp-exchange-rate.service.ts` | Normalize network/shape errors into a predictable error message for the component; do not introduce mock runtime fallback |

#### Phase 2: Add transient currency switching to the kwitariusze screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add component-level currency state | `src/app/features/kwitariusze/kwitariusze.ts` | Add a transient `selectedCurrency` signal defaulting to `PLN`, plus loading/error/rate metadata state owned by the component |
| 2.2 | Load NBP data on foreign-currency selection | `src/app/features/kwitariusze/kwitariusze.ts` | Implement a `changeCurrency()` flow that loads the latest NBP table when switching to USD/EUR, commits the selection only after a valid quote is available, and resets metadata on PLN |
| 2.3 | Replace hardcoded amount formatting | `src/app/features/kwitariusze/kwitariusze.ts` | Refactor `formatAmount()` into currency-aware formatting and add helpers to convert totals and interest from PLN to the selected currency |
| 2.4 | Add selector UI in filter row | `src/app/features/kwitariusze/kwitariusze.html` | Add a `mat-menu`-backed control next to existing filters using the page’s current chip/button patterns so the user can choose PLN/USD/EUR |
| 2.5 | Show NBP rate metadata | `src/app/features/kwitariusze/kwitariusze.html` | Render an inline information block below the filters with the selected currency rate, NBP table number, and effective date when USD/EUR is active |
| 2.6 | Update list amount cells | `src/app/features/kwitariusze/kwitariusze.html` | Replace current `formatAmount(totalAmount(row))` / `formatAmount(row.interest)` calls with converted display helpers so all visible kwota values follow the selected currency |
| 2.7 | Add loading/error styling | `src/app/features/kwitariusze/kwitariusze.scss` | Style the selector active state, rate metadata block, and non-blocking error/loading states consistently with existing filter-chip/table styles |
| 2.8 | Preserve no-persistence behavior | `src/app/features/kwitariusze/kwitariusze.ts` | Ensure no localStorage write/read is introduced; a new component instance must always start from PLN regardless of previous selection |

#### Phase 3: Cover the behavior with unit and e2e tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add NBP service tests | `src/app/core/services/nbp-exchange-rate.service.spec.ts` | Verify table A request URL, USD/EUR extraction, metadata mapping, caching behavior, and invalid-response handling with `HttpTestingController` |
| 3.2 | Update kwitariusze component tests | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Add coverage for default PLN state, successful USD/EUR switch, visible rate metadata, formatter/conversion behavior, API failure handling, and reset-to-PLN-on-recreate behavior |
| 3.3 | Keep existing status-filter coverage intact | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Ensure new currency tests do not break or rewrite the current status-filter persistence assertions already present in this spec file |
| 3.4 | Add browser regression test for currency switch | `tests/e2e/kwitariusze-currency.spec.ts` | Create a Playwright scenario that intercepts the NBP request, selects USD/EUR, verifies converted amounts and rate metadata, then reloads and confirms the screen returns to PLN |
| 3.5 | Verify filter coexistence | `tests/e2e/kwitariusze-currency.spec.ts` | Confirm the currency control coexists with existing filters/status persistence and does not block core list actions |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Enable `HttpClient` support for runtime NBP calls |
| `src/app/core/models/exchange-rate.model.ts` | CREATE | Define supported currency and NBP response/quote types |
| `src/app/core/services/nbp-exchange-rate.service.ts` | CREATE | Implement NBP table A integration, mapping, caching, and error handling |
| `src/app/core/services/nbp-exchange-rate.service.spec.ts` | CREATE | Unit-test the NBP integration contract with `HttpTestingController` |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Add transient currency state, conversion helpers, and NBP-loading flow |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Add currency selector, rate info block, and converted amount rendering |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style the selector and rate/error presentation |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Add component tests for currency switching and non-persistence |
| `tests/e2e/kwitariusze-currency.spec.ts` | CREATE | Add end-to-end coverage for selector behavior, conversion, metadata, and reload reset |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/rozliczenia/kwitariusze` and confirm the default currency is PLN with no NBP metadata block visible
5. [ ] Select USD and confirm one NBP request is made to `https://api.nbp.pl/api/exchangerates/tables/A?format=json`
6. [ ] Confirm the `Kwota (z odsetkami)` totals and `odsetki` values are recalculated and formatted in USD
7. [ ] Confirm the UI shows the USD rate, NBP table number, and effective date
8. [ ] Switch from USD to EUR and confirm the displayed values update correctly without requiring a second fetch when cached data is already available
9. [ ] Switch back to PLN and confirm original PLN amounts return and the NBP metadata block disappears
10. [ ] Reload the page and confirm the list opens again in PLN
11. [ ] Re-run the existing kwitariusze status-filter tests and confirm status persistence still works independently of currency selection