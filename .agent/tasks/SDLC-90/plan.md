# SDLC-90: Waluty

## Specification

### Overview
Add a presentation-currency switcher to the offers list (`/offers`) so the user can view visible offer premiums in PLN, EUR, or USD without leaving the page. PLN remains the source/base currency in application data. When EUR or USD is selected, the UI must fetch the current exchange rate from the documented currency-rate service, recalculate only the premiums shown in the current offers view, round values to whole units, display the rate used, and preserve the previously displayed values if the rate lookup fails.

### User Stories
- US-001: As a sales agent, I want to choose the premium presentation currency on the offers list, so that I can compare offers in PLN, EUR, or USD on the same screen.
  - Given the offers list is opened / When the page loads / Then premiums are shown in PLN by default.
- US-002: As a sales agent, I want visible offer premiums to be recalculated after changing the currency, so that I can compare the currently displayed offers in EUR or USD.
  - Given I am on the offers list and select EUR or USD / When the exchange rate is fetched successfully / Then all visible offer premiums are recalculated from PLN and rounded to 0 decimal places.
- US-003: As a sales agent, I want to see the exchange rate used for recalculation, so that I know how displayed values were derived.
  - Given premiums are shown in EUR or USD / When recalculation completes / Then the page displays the rate used for the selected currency.
- US-004: As a sales agent, I want a clear error message when the exchange-rate service is unavailable, so that I understand why values were not changed.
  - Given I switch to EUR or USD / When the exchange-rate request fails / Then the page shows an error and keeps the previously displayed premium values unchanged.

### Functional Requirements
- FR-001: The offers page must expose a currency selector with exactly three options: `PLN`, `EUR`, `USD`.
- FR-002: The default selected currency on initial page load must be `PLN`.
- FR-003: Existing offer data loaded from `OffersRepository` must remain PLN-based; currency switching must affect presentation only.
- FR-004: Selecting `PLN` must restore source premium values without calling the exchange-rate API.
- FR-005: Selecting `EUR` or `USD` must call the documented NBP exchange-rate endpoint and use the returned current rate as the runtime default.
- FR-006: The integration must use `GET https://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json` for `{code}` = `EUR` or `USD`, with no mock JSON runtime fallback.
- FR-007: The exchange-rate response must be mapped from the documented NBP payload shape: `{ table, currency, code, rates: [{ no, effectiveDate, mid }] }`, using `rates[0].mid`.
- FR-008: Premium conversion must use PLN as base and calculate the displayed foreign amount as `Math.round(plnAmount / mid)`.
- FR-009: Conversion must apply only to premiums rendered in the current offers-list view (`filteredOffers()` output).
- FR-010: The premium amount source must stay aligned with existing page logic: `selectedPaymentPlan.totalPremium.amount` fallback to `variants[0].totalPremium.amount`.
- FR-011: When EUR or USD is active, the page must show the applied rate and effective date near the list metadata.
- FR-012: When the exchange-rate lookup fails, the UI must show an inline error message and keep the previously active currency/premium values unchanged.
- FR-013: The selector should be temporarily disabled while the exchange-rate request is in flight to avoid overlapping updates.
- FR-014: New unit tests must cover default PLN rendering, successful EUR/USD conversion, PLN reset, and failed-rate lookup behavior.

### Edge Cases
- EC-001: If the user re-selects the already active currency, the page should not issue a redundant exchange-rate request.
- EC-002: If an offer has no `selectedPaymentPlan`, the UI must keep using the existing fallback to the first variant premium.
- EC-003: If a premium amount is `0`, conversion must still render `0` in the selected currency without errors.
- EC-004: If the user switches from EUR/USD back to PLN, the rate banner must disappear and original PLN values must be restored immediately.
- EC-005: If the exchange-rate API returns an unexpected payload without `rates[0].mid`, the UI must treat it as a failed lookup and keep existing values unchanged.
- EC-006: Filtering or searching after a currency switch must continue showing converted values only for currently visible rows, without mutating the underlying offers collection.

### Success Criteria
- [ ] The offers page loads with `PLN` selected and existing PLN premiums displayed.
- [ ] Switching to `EUR` recalculates all visible offer premiums using the fetched EUR rate and rounds to whole amounts.
- [ ] Switching to `USD` recalculates all visible offer premiums using the fetched USD rate and rounds to whole amounts.
- [ ] Switching back to `PLN` restores source PLN values without a rate-service call.
- [ ] The UI displays the applied exchange rate and effective date for EUR/USD.
- [ ] On rate-service failure, an error is shown and previously displayed premium values remain unchanged.
- [ ] Automated tests cover the main success and failure scenarios.

### Open Questions
- [NEEDS CLARIFICATION: Should the portfolio summary tile ("Średnia składka") and the premium shown inside the transition dialog also follow the selected presentation currency, or should conversion be limited strictly to offer-row premiums on the list?]

---

## Implementation Plan

### Technical Approach
The implementation should follow the existing Angular standalone + signals pattern already used in `src/app/features/offers/pages/offers-home-page.component.ts`, and the repository abstraction pattern used in `src/app/core/repositories/offers.repository.ts` and `src/app/core/repositories/reference-data.repository.ts`.

Reuse the documented currency integration from Confluence instead of inventing a new source:
- Service overview: [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
- Authoritative API reference: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Runtime default must be the documented public NBP API:
- Endpoint: `GET https://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json`
- Supported codes for this ticket: `EUR`, `USD`
- Response shape consumed by the app: `table`, `currency`, `code`, `rates[0].effectiveDate`, `rates[0].mid`
- Auth: none
- Conversion rule in UI: base PLN amount divided by `mid`, rounded to `0` decimal places

Domain context found in SDLC space:
- Product/screen context: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany)
- No more specific offer-list currency ADR/convention was found in the SDLC search results, so the plan follows current repo conventions.

Code-level approach:
- Keep `Money` as PLN-based source data to reflect the task constraint that PLN remains the base currency.
- Introduce a separate presentation-currency model and exchange-rate model rather than mutating stored offer data.
- Create a dedicated `ExchangeRatesRepository` under `src/app/core/repositories` to encapsulate the NBP HTTP call and response mapping.
- Extend `OffersHomePageComponent` with signals for:
  - active presentation currency
  - active exchange-rate quote
  - loading state
  - error message
- Add a handler for currency changes that:
  - immediately restores PLN locally when `PLN` is selected
  - fetches EUR/USD via `ExchangeRatesRepository` when needed
  - updates display state only after a successful response
  - preserves the current display state on failure
- Keep conversion presentation-only by computing displayed premium values at render time from the existing `getPrimaryPremium(...)` source.
- Update the template to add:
  - a new currency `p-select` in the toolbar
  - a rate-info area near `.results-meta`
  - an inline error state for lookup failures
- Update styles in `offers-home-page.component.scss` to fit the new control and message area into the existing toolbar/responsive layout.
- Do not add any runtime JSON file under `public/mock` for exchange rates. Mock data is acceptable only in unit tests via `HttpTestingController` or repository stubs.

### Task Breakdown

#### Phase 1: Core exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add presentation currency model | `src/app/core/models/common/presentation-currency.model.ts`, `src/app/core/models/index.ts` | Introduce a shared union type for `PLN \| EUR \| USD` used by the offers page and rate repository. |
| 1.2 | Add exchange-rate quote model | `src/app/core/models/common/exchange-rate.model.ts`, `src/app/core/models/index.ts` | Define a typed model for mapped NBP responses, including currency code, effective date, and `mid` rate. |
| 1.3 | Implement NBP repository | `src/app/core/repositories/exchange-rates.repository.ts` | Create a repository using `HttpClient` that calls `https://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json`, validates the payload, and maps it to the internal quote model. |
| 1.4 | Add repository tests | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Verify the exact request URL, successful response mapping, and invalid-payload/error handling with `HttpTestingController`. |

#### Phase 2: Offers page currency switching
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add signals for active presentation currency, exchange-rate data, loading, and error state; implement the currency-change workflow and retain current values on failure. |
| 2.2 | Add display-premium helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce helpers that derive the displayed amount/currency from existing PLN premium data without mutating `Offer` objects. |
| 2.3 | Add toolbar currency selector | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new `p-select` alongside existing filters for `PLN`, `EUR`, and `USD`, with disabled/loading behavior during rate fetches. |
| 2.4 | Render converted premiums and rate info | `src/app/features/offers/pages/offers-home-page.component.html` | Replace the hard-coded PLN premium rendering in the offer row with dynamic display currency output; add rate/effective-date metadata and inline error feedback. |
| 2.5 | Style the new UI elements | `src/app/features/offers/pages/offers-home-page.component.scss` | Extend toolbar and metadata styles for the new selector, rate banner, and error state while preserving current responsive behavior. |

#### Phase 3: Component verification
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers page unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Cover default PLN state, successful EUR conversion, successful USD conversion, switch-back-to-PLN behavior, and failed lookup preserving prior values. |
| 3.2 | Verify visible-list behavior | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert that conversion is applied through the rendered `filteredOffers()` list, not by mutating the underlying offers source data. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/presentation-currency.model.ts` | CREATE | Shared union type for supported display currencies. |
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Internal model for mapped NBP exchange-rate data. |
| `src/app/core/models/index.ts` | MODIFY | Export new shared models for repo/component usage. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Runtime NBP integration for EUR/USD exchange-rate retrieval. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit tests for request URL, mapping, and failure handling. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency-switch state, conversion helpers, API integration flow, and error preservation logic. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency selector, rate/error messaging, and dynamic premium rendering. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the currency switcher and feedback areas within the existing offers-page layout. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component-level tests for currency switching and failure scenarios. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test -- --watch=false`
3. [ ] New tests cover PLN default, EUR/USD conversion, PLN reset, and rate-service failure handling
4. [ ] Manual verification on `/offers` confirms visible premiums recalculate to whole amounts only after successful EUR/USD rate fetch
5. [ ] Manual verification confirms the applied rate/effective date is shown for EUR/USD and hidden for PLN
6. [ ] Manual verification confirms a failed EUR/USD lookup leaves the previously displayed values unchanged