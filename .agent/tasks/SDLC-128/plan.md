# SDLC-128: Waluty

## Specification

### Overview
This task adds a currency selector to the `kalkulator/zakres` screen (`src/app/features/wizard/steps/step-15-coverage`) so users can view coverage premiums in PLN, USD, or EUR without changing the underlying policy data.

The screen currently renders hard-coded PLN prices for coverages/add-ons and a PLN total. The change will keep all persisted values in PLN, but allow the UI on this one screen to:
- default to PLN,
- fetch NBP exchange rates for USD/EUR,
- fall back to the previous day’s rate if the current rate fetch fails,
- show the applied rate, table designation, and rate date for converted currencies,
- keep the last successfully displayed values if both fetch attempts fail.

### User Stories
- US-001: As a customer reviewing insurance coverages, I want to switch the displayed currency on the “Wybierz zakres” screen, so that I can compare premiums in PLN, USD, or EUR.
  - Given the coverage screen is opened / When it loads / Then a currency control labeled “Waluta” is visible and defaults to PLN
- US-002: As a customer, I want all visible premium amounts on the coverage screen to refresh together using one NBP rate, so that the displayed values are consistent.
  - Given the screen shows coverage prices / When I choose USD or EUR and an NBP rate is available / Then all visible prices and the total are recalculated using the same fetched rate
- US-003: As a customer, I want to see which exchange rate was used, so that I can understand how the converted amounts were calculated.
  - Given USD or EUR is selected / When converted values are shown / Then the screen displays the applied rate, NBP table designation, and rate date
- US-004: As a customer, I want failed exchange-rate retrieval to not corrupt the current view, so that I do not see inconsistent prices.
  - Given I have a currently displayed currency view / When both the current and previous-day NBP lookups fail / Then the screen keeps the last successful values and shows an error message

### Functional Requirements
- FR-001: Add a currency presentation control to `step-15-coverage.component.html` labeled `Waluta` with options `PLN`, `USD`, and `EUR`.
- FR-002: The default selected and applied currency on first render must be `PLN`.
- FR-003: Currency switching must affect only the presentation layer of `src/app/features/wizard/steps/step-15-coverage/*`.
- FR-004: The underlying premium source values used by `Step15CoverageComponent` and the persisted `coverages.totalPremium` in `PolicyDraftService` must remain in PLN.
- FR-005: For PLN, the component must display base values without calling any external rate API and must hide exchange-rate metadata.
- FR-006: For USD/EUR, the component must call the documented NBP API and use the live endpoint as the runtime default:
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json`
  - fallback: `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/{YYYY-MM-DD}/?format=json`
  - no authentication
  - response shape consumed by the app: `{ table, code, rates: [{ no, effectiveDate, mid }] }`
- FR-007: The conversion formula must use the NBP `mid` rate as PLN-per-foreign-unit, i.e. displayed foreign amount = `basePln / mid`.
- FR-008: All prices on the coverage screen must be recalculated from base PLN values using one fetched rate per refresh cycle.
- FR-009: All displayed amounts on the coverage screen must always render with exactly 2 decimal places.
- FR-010: For USD/EUR, the UI must show the applied rate value, the NBP table designation, and the effective date used for conversion.
- FR-011: If the current-rate request fails, the app must automatically retry once with the previous calendar day.
- FR-012: If both requests fail, the app must show an inline error message and keep the last successfully applied currency view unchanged.
- FR-013: The selected currency control must reflect the last successfully applied currency so the selector and displayed amounts cannot drift apart.
- FR-014: The implementation must not change prices on other screens such as `step-16-casco`, `step-17-assistance`, `step-18-nnw`, `step-21-review`, or `step-22-payment`.

### Edge Cases
- EC-001: User switches from PLN to USD and the current NBP request succeeds — converted amounts, total, and rate metadata update together.
- EC-002: User switches from PLN to EUR, the current request fails, and previous-day request succeeds — converted amounts update and metadata shows the fallback date/table.
- EC-003: User switches from USD/EUR back to PLN — base PLN values are shown again and rate metadata disappears.
- EC-004: User selects USD/EUR but both API calls fail — the screen keeps the previously displayed values/currency and shows a clear error state.
- EC-005: User changes currency rapidly — only the latest selection may be applied; stale HTTP responses must be ignored.
- EC-006: Rounded line-item prices must still display with 2 decimals; total presentation should be derived consistently from the same rate and current displayed selection.
- EC-007: Returning to the coverage screen later should still start from PLN unless explicit persistence is introduced in a separate task.

### Success Criteria
- [ ] `kalkulator/zakres` shows a `Waluta` control with PLN/USD/EUR and defaults to PLN
- [ ] No NBP call is made for PLN
- [ ] USD and EUR values are fetched from live NBP endpoints and displayed with 2 decimal places
- [ ] Current-rate failure falls back to previous-day rate automatically
- [ ] If both NBP calls fail, the UI keeps the last successful values and shows an error message
- [ ] Rate, NBP table designation, and rate date are shown only for USD/EUR
- [ ] Persisted draft data remains PLN-based and unchanged outside presentation
- [ ] Automated tests cover default PLN, successful conversion, and failure/fallback behavior

### Open Questions
None at this time.

---

## Implementation Plan

### Technical Approach
This repo is an Angular 21 standalone app using `inject(...)`, route-level standalone components (`src/app/app.routes.ts`), and root-provided services under `src/app/core/services`. HTTP support is already enabled via `provideHttpClient()` in `src/app/app.config.ts`, so no bootstrap changes are required.

Reuse the documented NBP integration from Confluence:
- [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Runtime default must use the real NBP API described there, not mock JSON:
- current rate: `https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json`
- fallback rate: `https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/{YYYY-MM-DD}/?format=json`
- auth: none
- parsed fields: `table`, `code`, `rates[0].no`, `rates[0].effectiveDate`, `rates[0].mid`

No relevant ADRs/conventions were found in the configured `SDLC` Confluence space after scoped searches, so the implementation should follow the repository’s existing code patterns directly.

Code-level strategy:
1. **Introduce a dedicated exchange-rate service** in `src/app/core/services`, modeled after `vehicle-data.service.ts`, to isolate NBP HTTP access and fallback logic.
2. **Keep all source premiums in PLN** inside `Step15CoverageComponent` by refactoring current hard-coded `price` fields to `basePricePln`.
3. **Add presentation-only currency state** to `Step15CoverageComponent`, e.g.:
   - `appliedCurrency: 'PLN' | 'USD' | 'EUR'`
   - `pendingCurrency`
   - `rateInfo | null`
   - `currencyError | null`
   - `isCurrencyLoading`
4. **Compute display prices from base prices**, never mutating `cov`, `mainCoverages`, `addons`, or `coverages.totalPremium` into foreign currencies.
5. **Apply rate changes atomically** so all amounts on the screen refresh from one rate object.
6. **Prevent stale async updates** with `switchMap` or an equivalent request-token guard so rapid currency switching cannot apply an outdated response.
7. **Render an inline info panel** above or below the coverage cards to show:
   - selected currency,
   - applied rate,
   - table designation (`rates[0].no`, optionally with `table`),
   - effective date,
   - error message when both calls fail.
8. **Scope strictly to step 15**; do not modify premium displays on detail or later steps, because the Jira explicitly limits the change to presentation on this screen only.

Implementation details grounded in current files:
- `step-15-coverage.component.ts` already owns the displayed premium arrays and local `cov` state, so presentation logic belongs there.
- `step-15-coverage.component.html` already renders every amount that must change (`item.price` and `total`), making it the single required UI update surface.
- `PolicyDraftService` currently stores the draft and persists `coverages.totalPremium`; that value should remain the existing PLN total in `next()`.
- Existing styles in `step-15-coverage.component.scss` and global form patterns in `src/styles.scss` support adding a compact selector and metadata panel without introducing a new UI library.

### Task Breakdown

#### Phase 1: Exchange-rate integration layer
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add exchange-rate types | `src/app/core/models/exchange-rate.model.ts` | Create local types for `SupportedCurrency`, NBP response payload, and normalized rate metadata consumed by the component. |
| 1.2 | Implement NBP service | `src/app/core/services/exchange-rate.service.ts` | Add a root service using `HttpClient` to fetch current NBP rates, retry with previous-day date on failure, and normalize `{ mid, no, effectiveDate, table, code }`. |
| 1.3 | Add service tests | `src/app/core/services/exchange-rate.service.spec.ts` | Verify happy path, fallback path, and double-failure behavior with `HttpTestingController` or Angular HTTP testing utilities. |

#### Phase 2: Coverage-screen presentation refactor
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Refactor base price definitions | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Replace current hard-coded `price` display fields with PLN base-price fields so conversion is always derived from immutable source values. |
| 2.2 | Add presentation currency state | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Introduce applied/pending currency, loading/error state, normalized rate metadata, and display helpers for amount formatting and conversion. |
| 2.3 | Preserve PLN persistence | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Keep `next()` writing the PLN `totalPremium` exactly as today, regardless of current display currency. |
| 2.4 | Add currency selector UI | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Add a labeled `Waluta` control with PLN/USD/EUR, wire change events, and disable/guard interactions while a fetch is in progress if needed. |
| 2.5 | Bind all displayed amounts to formatters | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Replace direct `{{ item.price }} zł` / `{{ total }} zł` rendering with presentation helpers returning 2-decimal currency strings. |
| 2.6 | Add rate metadata and error UI | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Show applied rate/table/date for USD/EUR and an inline error when both NBP calls fail. |
| 2.7 | Style selector and info panel | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | Add focused styling for currency control, loading/error message, and metadata block while keeping the existing card design. |
| 2.8 | Add component tests | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Verify default PLN, successful USD/EUR application, metadata visibility, and “keep previous values on failure” behavior with a mocked service. |

#### Phase 3: End-to-end verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add focused Playwright scenario | `tests/e2e/coverage-currency.spec.ts` | Navigate to `kalkulator/zakres`, assert default PLN state, intercept NBP responses, validate converted display and metadata, and cover an error-preservation scenario. |
| 3.2 | Keep smoke coverage intact | `tests/e2e/smoke.spec.ts` | Leave unchanged unless test setup reuse is needed; do not expand unrelated smoke assertions. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/exchange-rate.model.ts` | CREATE | Shared currency/rate types for NBP integration and step-15 presentation state. |
| `src/app/core/services/exchange-rate.service.ts` | CREATE | Real NBP API integration with current-rate fetch and previous-day fallback. |
| `src/app/core/services/exchange-rate.service.spec.ts` | CREATE | Unit coverage for NBP success, fallback, and failure handling. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Add currency state, conversion logic, formatting helpers, and non-persistent presentation behavior. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | Add `Waluta` control, bind converted amounts, and render rate/error metadata. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | MODIFY | Style the currency selector, rate metadata panel, and error state. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | CREATE | Component tests for default, converted, and failure-preservation states. |
| `tests/e2e/coverage-currency.spec.ts` | CREATE | Playwright scenario validating user-visible currency switching on the coverage screen. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm run test`
3. [ ] E2E tests pass with `npm run e2e`
4. [ ] `kalkulator/zakres` defaults to PLN and shows no rate metadata
5. [ ] Switching to USD or EUR recalculates all visible coverage prices and total with 2 decimal places
6. [ ] Current-rate failure falls back to previous-day NBP rate
7. [ ] Double failure shows an error and leaves the last successful values unchanged
8. [ ] Navigating forward from step 15 still stores PLN `coverages.totalPremium` in the draft