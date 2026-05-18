# SDLC-124: Obsluga walut na ekranie z lista kwitariuszy

## Specification

### Overview
This task adds a currency presentation selector to the kwitariusze list screen so users can view all visible monetary amounts in PLN, USD, or EUR without changing the underlying source data.

On `src/app/features/kwitariusze/kwitariusze.html`, the screen currently shows filters and a table whose monetary presentation is hard-coded to PLN via `formatAmount()` in `src/app/features/kwitariusze/kwitariusze.ts`. The change must:
- add a currency selector labeled `Lista` next to the existing filters,
- default the screen to `PLN` on every entry,
- convert all visible amounts on the list screen using the current NBP rate,
- show the applied NBP rate, table designation, and effective date for USD/EUR,
- keep the existing filter state and list context unchanged,
- avoid persisting the selected currency across screen re-entry or session restart.

### User Stories
- US-001: As a user of the kwitariusze list, I want to switch the display currency to PLN, USD, or EUR, so that I can review the list in the currency I need.
  - Given I am on `/rozliczenia/kwitariusze` / When I change the `Lista` currency selector / Then all visible monetary values on the screen are re-rendered in the selected currency

- US-002: As a user, I want to see the exact NBP rate metadata used for conversion, so that I know how the displayed values were calculated.
  - Given I selected USD or EUR / When the conversion succeeds / Then I see the applied NBP rate, table designation, and effective date

- US-003: As a user, I want the screen to stay in its current working context while only the presentation currency changes, so that I do not lose my filters or current result set.
  - Given I already applied filters on the kwitariusze list / When I switch the display currency / Then the same records stay visible and only the amount presentation changes

- US-004: As a user, I want the screen to reopen in PLN every time, so that currency selection is temporary and local to the current view only.
  - Given I previously viewed the list in USD or EUR / When I reload the app, start a new session, or reopen the screen / Then the currency selector is back to PLN

### Functional Requirements
- FR-001: Add a currency selector labeled `Lista` to `src/app/features/kwitariusze/kwitariusze.html`, positioned in the existing filters row.
- FR-002: The selector must support exactly `PLN`, `USD`, and `EUR`.
- FR-003: The default selected value on screen entry must be `PLN`.
- FR-004: `PLN` must show the original list values without any exchange-rate conversion.
- FR-005: `USD` and `EUR` must convert every visible amount on the kwitariusze list screen using the NBP runtime integration documented in Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-006: The runtime default must use the documented NBP API response shape with `table`, `code`, and `rates[0]` metadata, including `rates[0].mid`, `rates[0].no`, and `rates[0].effectiveDate`.
- FR-007: For PLN source amounts, conversion to USD/EUR must divide the PLN amount by the NBP `mid` rate and round the presented result to 2 decimal places.
- FR-008: The conversion must cover all monetary values rendered in the list screen’s amount cell:
  - the total amount currently rendered from `totalAmount(row)`,
  - the interest amount currently rendered from `row.interest`.
- FR-009: The component must display the selected currency code together with presented amounts instead of the current hard-coded `zł` suffix.
- FR-010: When USD/EUR is active, the screen must display the applied NBP rate, table designation, and effective date.
- FR-011: When the primary NBP lookup is unavailable, the app must retry once using the previous-day lookup described by the documented API.
- FR-012: If both current and previous-day lookups fail, the screen must keep the last successfully applied presentation unchanged and show an error message.
- FR-013: Currency switching must not modify `KwitariuszService.kwitariusze()` source data or any persisted business data.
- FR-014: Currency switching must not clear or alter existing filters managed by `src/app/core/services/kwitariusz.service.ts`.
- FR-015: Currency selection must not be persisted in `localStorage`, user profile state, or any backend preference store.
- FR-016: The implementation must use a real runtime HTTP integration for NBP rates; mock JSON files are allowed only in unit/e2e test fixtures.

### Edge Cases
- EC-001: If the user selects `PLN`, no NBP HTTP call is made, rate metadata is hidden, and raw PLN values are shown.
- EC-002: If the user selects the already active currency, the component should not trigger another fetch or rerender cycle beyond the no-op selection update.
- EC-003: If the NBP lookup for USD/EUR fails, the selector must revert to the last committed currency state and keep the previously displayed amounts unchanged.
- EC-004: If the user already had USD/EUR applied and then switches back to PLN, the screen must clear rate metadata and restore unconverted values.
- EC-005: If the user changes filters while USD/EUR is active, the newly visible rows must still be rendered using the already loaded exchange rate until the currency changes again.
- EC-006: If the user leaves the screen and comes back, the currency must reset to PLN even though status filters are currently persisted by `KwitariuszService`.
- EC-007: If the user switches currencies quickly, only the latest successful selection should be committed to the UI state.
- EC-008: If the NBP response is structurally invalid or missing `rates[0].mid`, the request must be treated as failed and handled via fallback/error flow.

### Success Criteria
- [ ] `/rozliczenia/kwitariusze` shows a `Lista` currency selector next to the existing filters
- [ ] The selector defaults to `PLN` on every screen entry
- [ ] Choosing `USD` converts all visible amount values on the list and shows NBP metadata
- [ ] Choosing `EUR` converts all visible amount values on the list and shows NBP metadata
- [ ] Choosing `PLN` restores original PLN presentation without exchange-rate metadata
- [ ] Converted values are rounded to 2 decimal places
- [ ] Existing list filters and result count remain unchanged when currency changes
- [ ] If both current and previous-day NBP lookups fail, the prior presentation remains visible and an error message is shown
- [ ] Reloading or reopening the screen resets the selector to `PLN`

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The existing kwitariusze list is a standalone Angular component using signals and `MatTableDataSource`:
- UI: `src/app/features/kwitariusze/kwitariusze.ts`
- template: `src/app/features/kwitariusze/kwitariusze.html`
- styles: `src/app/features/kwitariusze/kwitariusze.scss`
- data/filter source: `src/app/core/services/kwitariusz.service.ts`

The cleanest implementation is to keep `KwitariuszService` as the source of raw PLN data and add a dedicated NBP integration service for exchange-rate lookup. Currency selection itself should stay component-local inside `KwitariuszeComponent`, because the Jira explicitly requires reset-to-PLN behavior on every screen entry and no persistence.

Reuse of existing service knowledge:
- Reuse the documented NBP integration from Confluence as the runtime default: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- The implementation should call the documented NBP endpoint for a single currency quote in table `A` and map the documented response shape:
  - `table`
  - `code`
  - `rates[0].mid`
  - `rates[0].no`
  - `rates[0].effectiveDate`
- If the primary lookup is unavailable, the service should retry the documented previous-day endpoint once.
- No relevant domain ADR/convention was found in the configured `SDLC` Confluence space, so repository-local Angular patterns should be followed.

Repository patterns to follow:
- Use standalone providers in `src/app/app.config.ts`.
- Keep presentation logic in the component, similar to existing formatting helpers in `kwitariusze.ts`.
- Reuse the custom sorting/accessor pattern already used in `src/app/features/noty-prowizyjne/noty-prowizyjne.ts` if amount sorting needs to stay aligned with displayed values.
- Keep current list/filter behavior owned by `KwitariuszService`; do not move filter state into the new exchange-rate integration.

Key design decisions:
1. **Introduce a dedicated `NbpExchangeRateService`**
   - New core service wrapping the documented NBP HTTP integration.
   - Returns a normalized quote object for `USD`/`EUR`.
   - Encapsulates fallback-to-previous-day logic so the component stays thin.

2. **Keep raw kwitariusz data in PLN**
   - `KwitariuszService` remains unchanged as the source of `baseAmount` and `interest`.
   - `KwitariuszeComponent` computes presentation amounts only.

3. **Commit currency changes only after successful fetch**
   - For USD/EUR, do not immediately replace the active currency.
   - Fetch the NBP quote first; only on success update `selectedCurrency` and `rateInfo`.
   - On failure, keep the previous currency and values visible, then show an error banner/message.

4. **Do not persist currency state**
   - Unlike the status filter, currency selection must stay local to the component lifecycle.
   - No `localStorage` key, query param, or service-level root persistence should be added.

5. **Use real HTTP at runtime, test doubles only in tests**
   - Add `provideHttpClient()` in `src/app/app.config.ts`.
   - In unit tests use `HttpTestingController`.
   - In Playwright use route interception for `api.nbp.pl` responses; never introduce a production mock fallback file.

### Task Breakdown

#### Phase 1: Add the NBP exchange-rate integration layer
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Enable HTTP client in the standalone app | `src/app/app.config.ts` | Add `provideHttpClient()` so the runtime can call the documented NBP API |
| 1.2 | Add normalized exchange-rate models | `src/app/core/models/exchange-rate.model.ts` | Create types for supported currencies (`PLN`, `USD`, `EUR`), foreign currencies (`USD`, `EUR`), raw NBP payload, and normalized quote metadata used by the UI |
| 1.3 | Implement NBP service | `src/app/core/services/nbp-exchange-rate.service.ts` | Create a root service that calls the Confluence-documented NBP endpoint, maps `rates[0]` into a normalized quote, and exposes a `getLatestOrPreviousRate(currency)` API |
| 1.4 | Implement fallback logic | `src/app/core/services/nbp-exchange-rate.service.ts` | Retry once with the previous-day endpoint when the current quote is unavailable, then surface a typed error if both attempts fail |
| 1.5 | Add service-level tests | `src/app/core/services/nbp-exchange-rate.service.spec.ts` | Verify latest-quote success, previous-day fallback, invalid payload handling, and double-failure behavior using `HttpTestingController` and fixed system time |

#### Phase 2: Add currency presentation state to the kwitariusze list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component state | `src/app/features/kwitariusze/kwitariusze.ts` | Add component-local signals for `selectedCurrency`, `rateInfo`, `currencyError`, and a stable list of available selector options |
| 2.2 | Implement transactional currency switching | `src/app/features/kwitariusze/kwitariusze.ts` | Add a `changeCurrency()` flow that skips HTTP for PLN, fetches USD/EUR quotes via `NbpExchangeRateService`, commits only on success, and preserves the previous presentation on failure |
| 2.3 | Refactor amount helpers | `src/app/features/kwitariusze/kwitariusze.ts` | Replace the current hard-coded PLN formatter with helpers that convert from PLN using `mid`, round to 2 decimals, and render explicit currency codes |
| 2.4 | Keep sorting aligned with rendered amounts | `src/app/features/kwitariusze/kwitariusze.ts` | If needed, add `sortingDataAccessor` for the `amount` column following the pattern from `noty-prowizyjne.ts`, so the displayed amount column remains internally consistent |
| 2.5 | Add the selector to the filters row | `src/app/features/kwitariusze/kwitariusze.html` | Insert a compact control labeled `Lista` beside existing filters, matching the current filter-row layout |
| 2.6 | Add rate metadata and error messaging | `src/app/features/kwitariusze/kwitariusze.html` | Render an info banner for USD/EUR showing the applied NBP rate, table designation, and effective date; render an error banner when both lookups fail |
| 2.7 | Preserve filter context | `src/app/features/kwitariusze/kwitariusze.ts`, `src/app/features/kwitariusze/kwitariusze.html` | Ensure currency changes do not touch existing calls such as `clearFilters()`, `toggleStatus()`, `filterType`, `filterPolicySearch`, or `filterInsuredSearch` |
| 2.8 | Style the new UI elements | `src/app/features/kwitariusze/kwitariusze.scss` | Add styles for the currency selector and the rate/error banner using the same visual language as existing chips and filter panels |

#### Phase 3: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Update component tests for default currency behavior | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify the component starts in PLN, does not call the exchange-rate service on init, and hides rate metadata in the default state |
| 3.2 | Add component tests for successful conversion | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Mock `NbpExchangeRateService` and verify USD/EUR selection updates rendered totals, interest values, and metadata while preserving result count/filter state |
| 3.3 | Add component tests for failure fallback | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify that if the NBP lookup fails, the last committed currency/value set remains visible and the error message appears |
| 3.4 | Add component tests for reset-to-PLN behavior | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify switching back to PLN clears metadata and shows raw values again |
| 3.5 | Add e2e coverage for the happy path | `tests/e2e/kwitariusze-currency.spec.ts` | Intercept NBP HTTP calls and verify selector placement, USD/EUR conversion, and rate metadata rendering in the browser |
| 3.6 | Add e2e coverage for reset/no-persistence | `tests/e2e/kwitariusze-currency.spec.ts` | Verify that navigating away and back to `/rozliczenia/kwitariusze` resets the selector to PLN and does not restore the last chosen currency |
| 3.7 | Add e2e coverage for error behavior | `tests/e2e/kwitariusze-currency.spec.ts` | Intercept both NBP attempts with failures and verify that the previous presentation remains visible with an error message |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Enable Angular HTTP client for the real NBP runtime integration |
| `src/app/core/models/exchange-rate.model.ts` | CREATE | Define currency and NBP response/quote types used by the integration and UI |
| `src/app/core/services/nbp-exchange-rate.service.ts` | CREATE | Implement documented NBP API calls, payload mapping, and previous-day fallback |
| `src/app/core/services/nbp-exchange-rate.service.spec.ts` | CREATE | Unit-test latest-rate success, fallback, and failure handling |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Add component-local currency state, conversion helpers, and exchange-rate fetch flow |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Add the `Lista` selector and the NBP metadata/error presentation |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style the selector and the rate/error info blocks to match existing filter UI |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Add unit coverage for PLN default, successful conversion, reset, and failure fallback |
| `tests/e2e/kwitariusze-currency.spec.ts` | CREATE | Add browser-level regression coverage for conversion, metadata, and non-persistent selector behavior |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/rozliczenia/kwitariusze` and confirm the new `Lista` selector is visible next to the filters with default value `PLN`
5. [ ] Select `USD` and confirm all visible list amounts switch from PLN to USD, rounded to 2 decimals
6. [ ] Confirm the USD state shows NBP metadata: rate, table designation, and effective date
7. [ ] Select `EUR` and confirm the same conversion/metadata behavior for EUR
8. [ ] Select `PLN` and confirm the list returns to raw PLN presentation with no NBP metadata shown
9. [ ] Apply one or more existing filters, switch currency, and confirm the same records remain visible and only amount presentation changes
10. [ ] Simulate unavailable current quote and available previous-day quote in tests; confirm fallback succeeds transparently
11. [ ] Simulate failure of both quote attempts in tests; confirm the previous presentation remains visible and an error message is shown
12. [ ] Navigate away from `/rozliczenia/kwitariusze` and back; confirm the selector resets to `PLN` and does not remember the prior USD/EUR choice