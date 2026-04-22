# SDLC-46: Wybór waluty i przeliczanie na liście ofert

## Specification

### Overview
Task adds a display-currency selector to the offers list so the user can switch premium presentation between PLN, EUR, and USD. All currently visible offers must be recalculated from their existing PLN base premium using one shared exchange rate for the selected currency. The screen must also show which rate was used and transparently fall back to fixed rates when the currency-rate service is unavailable or returns invalid data.

### User Stories
- US-001: As a user browsing the offers list, I want to choose PLN, EUR, or USD, so that I can compare visible offers in my preferred currency.
  - Given I open the offers list / When the screen loads / Then the default display currency is PLN.
- US-002: As a business user, I want all visible offers to be converted with one shared rate, so that comparison across offers is consistent.
  - Given visible offers are shown on the list / When I switch currency to EUR or USD / Then every visible offer premium is recalculated using the same fetched or fallback rate.
- US-003: As a user browsing the offers list, I want to see the exchange rate used for conversion, so that I understand how displayed premiums were calculated.
  - Given I selected EUR or USD / When conversion finishes / Then the screen shows the exact rate value that was actually used.

### Functional Requirements
- FR-001: Add a currency selector to `src/app/features/offers/pages/offers-home-page.component.html` with exactly three values: `PLN`, `EUR`, `USD`.
- FR-002: The default selected currency must be `PLN`.
- FR-003: Selecting `PLN` must display the existing PLN premium values without calling the currency-rate API.
- FR-004: Selecting `EUR` or `USD` must fetch one exchange-rate quote for the selected currency and use it for all currently displayed offers.
- FR-005: The runtime-default integration for exchange rates must reuse the documented service from Confluence:
  - `Kursy walut`: https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut
  - `v1 - API NBP – pobieranie kursów walut`: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- FR-006: The exchange-rate repository must call the documented NBP endpoint `GET https://api.nbp.pl/api/exchangerates/rates/a/{currencyCode}/?format=json`, treat `rates[0].mid` as the PLN-per-unit exchange rate, and require no authentication.
- FR-007: If the service call fails, or the payload is missing a usable `mid` value, the app must use fallback constants:
  - `EUR = 4.4 PLN`
  - `USD = 3.6 PLN`
- FR-008: Premium conversion must be presentation-only. Existing offer data loaded from `src/app/core/repositories/offers.repository.ts` and current `Money` model in `src/app/core/models/common/money.model.ts` remain PLN-based source data.
- FR-009: Premiums shown in the offer rows must be derived from the same base amount currently used by `getPrimaryPremium(offer)` in `src/app/features/offers/pages/offers-home-page.component.ts`.
- FR-010: For EUR/USD, the screen must display a rate-information message containing:
  - selected currency,
  - exact numeric rate used,
  - source of the rate (`service` or `fallback`),
  - if available, the effective date from the API response.
- FR-011: For PLN, the rate-information message must be hidden.
- FR-012: Currency labels and premium formatting must update reactively without page reload and without affecting existing filtering, sorting, transitions, or navigation actions on the offers page.
- FR-013: Existing offers from `mock/offers.json` and runtime offers from `SalesFlowRuntimeRepository` must both participate in display conversion, because `allOffers()` merges both sources today.
- FR-014: The average-premium summary tile should use the same selected display currency as the visible offer list so the screen remains internally consistent.

### Edge Cases
- EC-001: If the user switches from EUR/USD back to PLN, the component must stop showing rate info and revert to base PLN values immediately.
- EC-002: If the user changes currency rapidly, only the latest selection should be applied; stale HTTP responses must not overwrite the newest state.
- EC-003: If the API returns malformed data, empty `rates`, or a non-positive `mid`, the app must treat it as a failure and use the configured fallback rate.
- EC-004: Offers missing `selectedPaymentPlan` must still convert correctly using the existing fallback path to `variants[0]?.totalPremium.amount`.
- EC-005: Empty-state rendering (`Brak ofert dla podanych filtrów`) must still work regardless of selected currency.
- EC-006: Crop offers and motor offers must use the same conversion logic, because both currently expose premium via the shared offer model.
- EC-007: Conversion must not mutate original offer objects or persist converted values to local storage; only displayed values change.
- EC-008: If the selected currency is EUR/USD and filters change after the rate was fetched, newly visible offers must use the already active rate until the user selects a different currency.

### Success Criteria
- [ ] The offers page shows a selector with only PLN, EUR, and USD.
- [ ] The default currency on first load is PLN.
- [ ] Switching to EUR recalculates all visible offer premiums using one EUR rate.
- [ ] Switching to USD recalculates all visible offer premiums using one USD rate.
- [ ] If the rate service fails, EUR uses 4.4 and USD uses 3.6 without blocking the UI.
- [ ] For EUR/USD, the screen shows the exact rate actually used.
- [ ] For PLN, no exchange-rate info is shown.
- [ ] Existing filtering, sorting, status transitions, and navigation continue to work.

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The offers screen is already implemented as a standalone Angular component with signals and computed state in `src/app/features/offers/pages/offers-home-page.component.ts`. Repositories under `src/app/core/repositories` are the established pattern for data access, and the app already has `provideHttpClient()` configured in `src/app/app.config.ts`, so the new exchange-rate integration should follow the same repository-based structure.

Reuse existing service documentation before building anything new:
- Currency service overview: https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut
- API reference: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut

Runtime default for this task must be the documented real integration, not a mock file:
- Endpoint: `GET https://api.nbp.pl/api/exchangerates/rates/a/{currencyCode}/?format=json`
- Supported runtime codes for this task: `EUR`, `USD`
- Response field to use: `rates[0].mid`
- Auth: none
- Fallback constants in application code only: `EUR=4.4`, `USD=3.6`
- Mock JSON files remain acceptable only in tests; they must not become a production/runtime fallback path.

Code-level strategy:
- Keep `Offer`, `PaymentPlan`, and `Money` models unchanged as PLN source-of-truth. `src/app/core/models/common/money.model.ts` currently hardcodes `currency: 'PLN'`, and widening that model would create unnecessary cross-feature impact.
- Add a dedicated `CurrencyRatesRepository` in `src/app/core/repositories` that hides HTTP details, validates the NBP response, and returns a normalized quote object with currency, rate, effective date, and source.
- In `OffersHomePageComponent`, add a `selectedCurrency` signal defaulting to `PLN` and derive a signal-backed active quote using `toObservable(...).pipe(distinctUntilChanged(), switchMap(...))` or equivalent reactive composition so only the latest selection wins.
- Continue using existing `getPrimaryPremium(offer)` as the PLN base-premium source, then add presentation helpers for converted amount, display currency code, rate label, and formatting metadata.
- Update the template to add a PrimeNG selector aligned with existing toolbar controls and show rate metadata in the existing `results-meta` area to minimize layout churn.
- Update the summary tile calculation so the average premium matches the selected display currency; counts remain unchanged.
- No relevant domain guidance was found in the configured `SDLC` Confluence domain space, so implementation should follow the repo’s existing Angular/PrimeNG conventions.

### Task Breakdown

#### Phase 1: Add exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create currency-rate repository | `src/app/core/repositories/currency-rates.repository.ts` | Add repository responsible for calling the documented NBP endpoint, mapping `rates[0].mid`, and returning normalized quote data for `EUR` and `USD`. |
| 1.2 | Implement fallback handling | `src/app/core/repositories/currency-rates.repository.ts` | Add validation and `catchError` logic so malformed/unavailable responses fall back to `EUR=4.4`, `USD=3.6`, while preserving the source metadata shown in UI. |
| 1.3 | Add repository tests | `src/app/core/repositories/currency-rates.repository.spec.ts` | Cover successful API mapping, malformed payload fallback, and transport-error fallback using `HttpTestingController`. |

#### Phase 2: Wire currency state into offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add display-currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add a `selectedCurrency` signal defaulting to `PLN` plus an active-quote signal/effect that fetches EUR/USD rates only when needed. |
| 2.2 | Add presentation helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce helpers/computed values for converted premium amount, currency code, digits info, exchange-rate label, and visibility of the rate banner. |
| 2.3 | Keep conversion presentation-only | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure existing offer entities remain unchanged and all conversions are derived from `getPrimaryPremium(offer)` output. |
| 2.4 | Update summary tile conversion | `src/app/features/offers/pages/offers-home-page.component.ts` | Convert the average-premium tile to the selected display currency while leaving count-based tiles unchanged. |
| 2.5 | Preserve existing page behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure filters, sorting, runtime statuses, split-button actions, and transitions keep using the current flow unchanged. |

#### Phase 3: Update offers page UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add currency selector to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new `p-select` with `PLN`, `EUR`, `USD`, using the same toolbar pattern as existing filter controls. |
| 3.2 | Render converted premiums | `src/app/features/offers/pages/offers-home-page.component.html` | Replace hardcoded `currency: 'PLN'` binding in the premium box with dynamic display helpers so all visible offers show the selected currency. |
| 3.3 | Show used exchange rate | `src/app/features/offers/pages/offers-home-page.component.html` | Add a rate-information block in `results-meta`, visible only for EUR/USD, showing rate value, source, and effective date when present. |
| 3.4 | Style new controls and rate banner | `src/app/features/offers/pages/offers-home-page.component.scss` | Extend existing BEM-style classes for selector width, responsive stacking, and rate-info presentation without disrupting current layout. |

#### Phase 4: Add component verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add offers page unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify default PLN state, available currency options, EUR/USD conversion, hidden rate info for PLN, and displayed fallback metadata when the service fails. |
| 4.2 | Regressions around existing offers behavior | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Confirm filtering/sorting still work with selected currency and that converted premiums are derived from the same visible offers set. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/currency-rates.repository.ts` | CREATE | Repository for real NBP-backed exchange-rate retrieval with fallback constants. |
| `src/app/core/repositories/currency-rates.repository.spec.ts` | CREATE | Unit tests for success and fallback paths of the rate integration. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add selected currency state, active rate handling, conversion helpers, and summary-tile currency support. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency selector, dynamic premium rendering, and used-rate information. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new selector and exchange-rate info area using existing page conventions. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component tests for default state, conversion behavior, and fallback display. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test -- --watch=false`
3. [ ] `CurrencyRatesRepository` tests confirm request path `https://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json`
4. [ ] On `/offers`, default currency is PLN and premiums render in PLN
5. [ ] Switching to EUR recalculates all visible offer premiums and shows EUR rate info
6. [ ] Switching to USD recalculates all visible offer premiums and shows USD rate info
7. [ ] Simulated API failure falls back to 4.4 for EUR and 3.6 for USD
8. [ ] Returning to PLN hides rate info and restores base values
9. [ ] Existing search, filters, sorting, and offer actions still work after currency changes
10. [ ] New tests cover both service-success and fallback scenarios