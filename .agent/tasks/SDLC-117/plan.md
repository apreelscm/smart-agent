# SDLC-117: Na liscie kwitariuszy dodaj mozliwosc zmiany waluty, kursy wez z nbp (EUR oraz USD), domyslnie PLN, po zmianie z PLN pokaz gdzies kurs z aktualnego dnia

## Specification

### Overview
Dodanie na liście kwitariuszy kontrolki zmiany waluty prezentacji danych z domyślnego PLN na EUR lub USD. Funkcja ma działać wyłącznie w warstwie prezentacji: dane źródłowe pozostają w PLN, a wszystkie widoczne kwoty na widoku listy są przeliczane na podstawie bieżącego kursu NBP. Dla walut innych niż PLN użytkownik widzi także informację o zastosowanym kursie i dacie kursu.

### User Stories
- US-001: As a user of the kwitariusze list, I want to switch the display currency between PLN, EUR, and USD, so that I can review all visible monetary values in one selected currency.
  - Given the kwitariusze list is open / When I select PLN, EUR, or USD / Then all visible amounts on the list are shown in the selected currency.

- US-002: As a user, I want PLN to be selected by default, so that the list behaves as it does today when I first open or refresh the page.
  - Given I open or refresh the kwitariusze list / When the page initializes / Then PLN is selected and no exchange-rate info is shown.

- US-003: As a user, I want to see the applied NBP exchange rate for EUR or USD, so that I know how the amounts were converted.
  - Given I selected EUR or USD and today’s NBP rate was loaded / When the list re-renders / Then I see the currency code, rate value, effective date, and NBP source.

### Functional Requirements
- FR-001: Add a currency selector to `src/app/features/kwitariusze/kwitariusze.html`, positioned on the right side of the filters row.
- FR-002: Supported display currencies must be limited to `PLN`, `EUR`, and `USD`.
- FR-003: The initial state of `KwitariuszeComponent` must default to `PLN` on every fresh load of the route.
- FR-004: The selected currency must not be persisted in storage, query params, or shared app state; recreating the view must reset it to `PLN`.
- FR-005: When `PLN` is selected, the list must display source values without conversion and without exchange-rate info.
- FR-006: When `EUR` or `USD` is selected, the app must call the real NBP API documented in Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-007: Runtime integration must use the public NBP endpoint `GET https://api.nbp.pl/api/exchangerates/rates/A/{EUR|USD}/today/?format=json`, with no authentication, and map the response from `rates[0].mid`, `rates[0].effectiveDate`, and `code`.
- FR-008: All visible monetary values on the kwitariusze list must be converted from PLN into the selected currency in the presentation layer only.
- FR-009: In the current list implementation, conversion must cover both the total amount shown by `totalAmount(row)` and the interest amount shown by `row.interest`.
- FR-010: The applied exchange-rate info must include at least: currency code, rate value, effective date, and NBP as source.
- FR-011: The component must not mutate `KwitariuszService.kwitariusze`; conversion must happen only in display helpers/view-model state.
- FR-012: Switching between PLN, EUR, and USD must immediately refresh rendered values for the current table rows.
- FR-013: To match the existing codebase style, component UI state should be held with Angular signals in `KwitariuszeComponent`, and HTTP access should be encapsulated in a new core service.

### Edge Cases
- EC-001: If the user switches repeatedly between EUR and USD during one page session, already fetched rates should be reused from in-memory cache to avoid unnecessary duplicate requests.
- EC-002: If the NBP API returns no current-day rate or the request fails, the component should show a non-blocking inline error, revert to PLN presentation, and avoid leaving stale converted values on screen.
- EC-003: Zero-value amounts must still render correctly in the selected currency using the same decimal formatting rules as other amounts.
- EC-004: Refreshing the browser must recreate the component with `PLN` selected and no previously fetched rate applied.
- EC-005: Conversion precision should use the raw NBP `mid` rate for calculations and round only at display formatting time to keep totals visually consistent.

### Success Criteria
- [ ] The kwitariusze list shows a selector with `PLN`, `EUR`, and `USD`.
- [ ] The selector is rendered on the right side of the filters section.
- [ ] Opening or refreshing the view always starts in `PLN`.
- [ ] Switching to `EUR` converts every visible kwota on the list using the current NBP EUR rate.
- [ ] Switching to `USD` converts every visible kwota on the list using the current NBP USD rate.
- [ ] In `PLN`, no exchange-rate banner/info is displayed.
- [ ] In `EUR` or `USD`, the applied rate info shows currency, rate, date, and NBP source.
- [ ] Source kwitariusz data remains unchanged in `KwitariuszService`.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The repo is an Angular 21 standalone-component app using local signals/effects and lightweight feature services. The relevant existing pattern is visible in:
- `src/app/features/kwitariusze/kwitariusze.ts` — feature-local UI state with signals and formatting helpers.
- `src/app/features/oferty/oferty.ts` — similar filters/table behavior using signals rather than shared state.
- `src/app/core/services/kwitariusz.service.ts` — source data service with computed filtering.

This task should follow the same structure:
1. Keep kwitariusz source data in `KwitariuszService` unchanged and PLN-based.
2. Add a dedicated exchange-rate integration service in `src/app/core/services`.
3. Keep selected currency, loading/error state, and rate cache in `KwitariuszeComponent` as signals.
4. Convert values only in display helper methods used by the template.

Service reuse:
- Reuse the documented NBP integration from Confluence as the runtime default: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- Runtime call shape:
  - Endpoint: `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/today/?format=json`
  - Supported codes: `EUR`, `USD`
  - Auth: none
  - Response fields used: `code`, `rates[0].mid`, `rates[0].effectiveDate`
- No mock JSON runtime fallback is allowed. Even though the current repo uses in-memory mock data for kwitariusze, exchange rates must come from the real NBP API in production code. Mocks are acceptable only in unit tests via `HttpTestingController` fixtures.

Confluence/domain discovery outcome:
- Relevant existing service/API was found in `Services` and should be reused.
- No directly relevant SDLC domain convention/ADR for this specific currency-switch behavior was found in the `SDLC` space; implementation should therefore follow repo-local Angular patterns.

Implementation details:
- `src/app/app.config.ts` currently does not provide `HttpClient`; add `provideHttpClient()` so the new NBP service can call the public API.
- Create a typed model for the NBP response in `src/app/core/models`, consistent with the repo’s existing model organization.
- Extend `KwitariuszeComponent` with:
  - `selectedCurrency` signal defaulting to `PLN`
  - `appliedRate` signal for current `EUR`/`USD` metadata
  - loading/error signals
  - in-memory rate cache keyed by currency code
  - conversion/formatting helpers that preserve current Polish decimal display style
- Update `kwitariusze.html` so the selector lives on the right side of `.filters`, likely via a `filters__right` container or spacer aligned by flex.
- Show the applied-rate info near the selector, matching the Jira clarification that it belongs around the list header/filters area.
- Do not change detail/edit/new kwitariusz screens, because the ticket scope is limited to the list view.

### Task Breakdown

#### Phase 1: Add exchange-rate integration foundation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Enable HTTP in app config | `src/app/app.config.ts` | Add `provideHttpClient()` to the application providers so feature services can call external APIs. |
| 1.2 | Add exchange-rate models | `src/app/core/models/exchange-rate.model.ts` | Create typed interfaces for supported display currencies, normalized exchange-rate data, and the raw NBP API response shape. |
| 1.3 | Implement NBP API service | `src/app/core/services/nbp-exchange-rate.service.ts` | Add a dedicated service wrapping `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/today/?format=json`, mapping `mid` and `effectiveDate` into a normalized model and surfacing request errors cleanly. |
| 1.4 | Add service tests | `src/app/core/services/nbp-exchange-rate.service.spec.ts` | Cover request URL construction, response mapping, and failure scenarios with `HttpTestingController` fixtures. |

#### Phase 2: Add currency-switch state and UI to kwitariusze list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component state | `src/app/features/kwitariusze/kwitariusze.ts` | Add `selectedCurrency`, applied-rate metadata, error/loading state, and in-memory caching using the same signal-based style already used in the feature. |
| 2.2 | Add currency switching logic | `src/app/features/kwitariusze/kwitariusze.ts` | On EUR/USD selection, fetch today’s rate if not cached, store normalized rate data, and revert to PLN on fetch failure or missing rate. |
| 2.3 | Refactor amount formatting | `src/app/features/kwitariusze/kwitariusze.ts` | Replace the current PLN-only `formatAmount()` usage with helpers that convert from PLN for display while leaving source `Kwitariusz` objects unchanged. |
| 2.4 | Update template for selector and rate info | `src/app/features/kwitariusze/kwitariusze.html` | Render a right-aligned selector for `PLN/EUR/USD`, show loading/error/rate info near the filters row, and bind the table amount cells to the new display helpers. |
| 2.5 | Style the new controls | `src/app/features/kwitariusze/kwitariusze.scss` | Extend the existing filter-row SCSS with a right-aligned selector group, rate badge/info block, and responsive wrapping behavior consistent with current page styles. |

#### Phase 3: Verify rendered behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add component tests | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify default PLN state, hiding/showing exchange-rate info, successful EUR/USD conversion rendering, and reset-to-PLN behavior on fresh component creation. |
| 3.2 | Cover error handling in UI tests | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Verify that an NBP failure does not leave converted values visible and that the UI falls back to PLN presentation with an inline error message. |
| 3.3 | Run build and test smoke checks | `src/app/app.config.ts`, `src/app/core/services/nbp-exchange-rate.service.ts`, `src/app/features/kwitariusze/kwitariusze.ts` | Validate that the new HTTP provider and feature logic compile cleanly in the standalone Angular app. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/app.config.ts` | MODIFY | Add `provideHttpClient()` for external NBP API access. |
| `src/app/core/models/exchange-rate.model.ts` | CREATE | Define supported currencies and typed NBP response/normalized rate models. |
| `src/app/core/services/nbp-exchange-rate.service.ts` | CREATE | Encapsulate real runtime integration with the public NBP exchange-rate API. |
| `src/app/core/services/nbp-exchange-rate.service.spec.ts` | CREATE | Unit-test NBP integration with mocked HTTP responses. |
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Add selector state, caching, conversion logic, and currency-aware formatting helpers. |
| `src/app/features/kwitariusze/kwitariusze.html` | MODIFY | Add the currency selector, rate/error info, and bind amount cells to converted display values. |
| `src/app/features/kwitariusze/kwitariusze.scss` | MODIFY | Style the selector and exchange-rate info within the existing filters/header layout. |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | CREATE | Verify UI behavior for PLN/EUR/USD switching and error fallback. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check: `/rozliczenia/kwitariusze` opens with PLN selected and no rate info
5. [ ] Manual check: selecting EUR shows converted totals/interest and NBP rate info
6. [ ] Manual check: selecting USD shows converted totals/interest and NBP rate info
7. [ ] Manual check: browser refresh returns the view to PLN