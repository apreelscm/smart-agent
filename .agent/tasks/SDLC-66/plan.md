# SDLC-66: Wybór waluty i przeliczanie na liście ofert

## Specification

### Overview
Dodanie na ekranie listy ofert (`Przygotowane oferty`) kontrolki wyboru waluty prezentacji z opcjami EUR i USD oraz automatycznego przeliczenia widocznych składek z bazowego PLN przy użyciu kursu pobranego z usługi kursów walut. Zmiana dotyczy wyłącznie warstwy prezentacji na liście ofert; dane źródłowe ofert i zapisane modele pozostają w PLN.

### User Stories
- US-001: As a sales agent, I want to choose EUR or USD on the offers list, so that I can view premiums in my preferred presentation currency.
  - Given the offers list is loaded in its default PLN presentation / When I select EUR or USD / Then all visible offer premiums are recalculated and displayed in the selected currency.
- US-002: As a sales agent, I want all visible offers to use one consistent rate after I change currency, so that the list remains comparable.
  - Given multiple offers are visible / When I change the presentation currency / Then every visible premium uses the same fetched exchange rate for that action.
- US-003: As a sales agent, I want to see the rate used for conversion, so that I can verify the shown amounts.
  - Given a conversion succeeds / When the list finishes recalculating / Then the screen shows the exchange rate and its effective date.
- US-004: As a sales agent, I want a clear error message when the exchange-rate service fails, so that I know why conversion was not applied.
  - Given the exchange-rate API is unavailable or returns invalid data / When I select EUR or USD / Then no conversion is applied and an error message is shown.

### Functional Requirements
- FR-001: Add a presentation-currency control to `src/app/features/offers/pages/offers-home-page.component.html` with selectable options `EUR` and `USD`.
- FR-002: Keep the initial list presentation in PLN, matching current behavior in `OffersHomePageComponent`; selecting EUR/USD changes only rendered values.
- FR-003: Fetch the runtime exchange rate from the documented Confluence integration pages:
  - https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut
  - https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- FR-004: The runtime default integration must call the documented NBP API endpoint `GET https://api.nbp.pl/api/exchangerates/rates/a/{currencyCode}/?format=json` for `EUR` or `USD`.
- FR-005: The exchange-rate client must treat the Confluence-documented NBP response as authoritative and map at minimum:
  - `code`
  - `currency`
  - `rates[0].mid`
  - `rates[0].effectiveDate`
- FR-006: No authentication is required for the runtime default NBP integration.
- FR-007: Because source premiums are stored in PLN and NBP returns table A mid rates as foreign-currency-to-PLN values, the UI conversion formula must be `convertedAmount = round(plnAmount / midRate)`.
- FR-008: Converted premiums must be displayed with the selected currency code/symbol and rounded to `0` decimal places.
- FR-009: The same fetched rate instance must be used for all offers rendered in the current list view for a single selection action.
- FR-010: The component must show the applied rate and effective date near the list metadata after a successful conversion.
- FR-011: If the rate fetch fails or the response lacks a usable rate, the component must show an error message and keep offer premiums in their original PLN presentation.
- FR-012: Conversion must not mutate `Offer`, `OfferVariant`, `PaymentPlan`, or `Money` source models loaded from `OffersRepository`; it must be computed in the presentation layer.
- FR-013: Existing filtering, sorting, status transitions, navigation, and summary tiles in `OffersHomePageComponent` must continue to work.
- FR-014: Tests must cover success and failure paths using HTTP test fixtures only; mock JSON files under `public/mock/` must not be used as a production fallback for exchange rates.

### Edge Cases
- EC-001: If the user changes currency quickly from EUR to USD (or vice versa), only the latest selection should be applied; stale HTTP responses must not overwrite newer state.
- EC-002: If the user clears the currency selection back to the base view, the page must immediately show original PLN premiums and hide rate/error presentation.
- EC-003: If the API returns an empty `rates` array or a malformed payload, treat it as a failure and do not convert.
- EC-004: If an offer has no selected payment plan, fallback to the existing premium source logic (`selectedPaymentPlan.totalPremium.amount` or `variants[0].totalPremium.amount` or `0`).
- EC-005: If filtering changes which offers are visible after a successful conversion, newly visible rows must reuse the already-fetched rate for the currently selected currency instead of re-fetching unnecessarily.
- EC-006: Summary tiles may remain PLN-based unless explicitly changed in the task; only row-level list premium presentation must be converted.
- EC-007: Existing persisted runtime offers in `SalesFlowRuntimeRepository` must remain unchanged in storage.

### Success Criteria
- [ ] The offers page shows a currency control with EUR and USD options.
- [ ] Initial page load still shows premiums in PLN.
- [ ] Selecting EUR converts all visible premiums from PLN using one fetched rate and rounds to whole amounts.
- [ ] Selecting USD converts all visible premiums from PLN using one fetched rate and rounds to whole amounts.
- [ ] The applied exchange rate and effective date are visible after successful conversion.
- [ ] If the exchange-rate API fails, the UI shows an error and premiums remain in PLN.
- [ ] No source offer data is mutated by the conversion flow.
- [ ] Existing filtering, sorting, transitions, and navigation continue to work.
- [ ] Unit tests cover successful conversion, API failure, and non-mutation behavior.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone app using signals/computed state and PrimeNG components. The relevant implementation lives in `src/app/features/offers/pages/offers-home-page.component.ts`, where filters, sorting, and row rendering are already derived reactively from signals. The cleanest implementation is to keep that pattern and add a presentation-only currency state plus a dedicated HTTP repository for exchange rates.

Reuse of existing service and domain context:
- Reuse the documented currency integration from Confluence as the runtime default:
  - [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
  - [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- Reuse product/screen context from:
  - [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany)
- No alternative existing exchange-rate client was found in the repo or Confluence search results; implement a new Angular repository that wraps the documented NBP API. No mock JSON runtime fallback will be added.

Implementation decisions aligned to current codebase:
- Keep `Offer` and `Money` source models unchanged. `src/app/core/models/common/money.model.ts` is currently restricted to `currency: 'PLN'`, which fits the domain source data and should not be widened just for presentation.
- Add a new repository under `src/app/core/repositories/`, matching existing patterns such as `offers.repository.ts` and `reference-data.repository.ts`.
- Keep conversion state local to `OffersHomePageComponent` using signals, similar to existing `searchTerm`, `selectedStatus`, `pendingTransition`, and computed row helpers.
- Render converted values via helper/computed methods instead of rewriting `filteredOffers()`.
- Use the documented NBP runtime endpoint directly:
  - `GET https://api.nbp.pl/api/exchangerates/rates/a/EUR/?format=json`
  - `GET https://api.nbp.pl/api/exchangerates/rates/a/USD/?format=json`
  - auth: none
  - response mapping: `rates[0].mid` + `rates[0].effectiveDate`
- Convert PLN to target currency by dividing PLN by the NBP mid rate and rounding with `Math.round(...)`.
- Test external calls with `HttpTestingController` fixtures only; production code must never read a mock exchange-rate JSON file.

### Task Breakdown

#### Phase 1: Add exchange-rate integration layer
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add exchange-rate model | `src/app/core/models/common/exchange-rate.model.ts` | Create a typed model for the NBP response and/or normalized applied-rate view (`targetCurrency`, `mid`, `effectiveDate`, derived `plnToTarget`). |
| 1.2 | Export new model | `src/app/core/models/index.ts` | Update the barrel export if the component/repository will consume the new shared model. |
| 1.3 | Create currency-rates repository | `src/app/core/repositories/currency-rates.repository.ts` | Add an injectable repository using `HttpClient` to call `https://api.nbp.pl/api/exchangerates/rates/a/{code}/?format=json`, validate `rates[0]`, and normalize the response. |
| 1.4 | Add repository unit tests | `src/app/core/repositories/currency-rates.repository.spec.ts` | Verify correct endpoint usage, payload mapping, and invalid-response/error handling with `HttpTestingController`. |

#### Phase 2: Wire currency state into the offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add presentation-currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce signals for selected presentation currency, loading state, applied exchange rate, and conversion error. |
| 2.2 | Add currency options and change handler | `src/app/features/offers/pages/offers-home-page.component.ts` | Add EUR/USD option definitions and a handler that fetches the rate on selection, resets to PLN on clear, and ignores stale responses. |
| 2.3 | Add computed display helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Implement helpers such as `getDisplayedPremium(offer)`, `isConvertedView()`, and `ratePresentation()` so row rendering remains presentation-only. |
| 2.4 | Preserve existing premium source logic | `src/app/features/offers/pages/offers-home-page.component.ts` | Reuse the existing `getPrimaryPremium()` fallback path and apply conversion on top of its numeric result only. |
| 2.5 | Keep summary/other flows unchanged | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure sort/filter logic, transition dialogs, routing, and status actions are untouched except where row premium rendering now references display helpers. |

#### Phase 3: Update the offers page UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add currency selector to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new `p-select` (or equivalent PrimeNG control already used on the page) in the toolbar with EUR/USD options and clear-to-PLN behavior. |
| 3.2 | Show rate metadata | `src/app/features/offers/pages/offers-home-page.component.html` | Add a small info line near `.results-meta` describing the applied rate and effective date when conversion is active. |
| 3.3 | Show error state | `src/app/features/offers/pages/offers-home-page.component.html` | Add an inline error banner/message when the rate request fails and no conversion is applied. |
| 3.4 | Render converted premiums | `src/app/features/offers/pages/offers-home-page.component.html` | Replace direct `currency:'PLN'` rendering in the premium box and transition dialog only where required by the task, using the display helper result. |
| 3.5 | Add styles for new UI elements | `src/app/features/offers/pages/offers-home-page.component.scss` | Extend the existing toolbar/meta styles with classes for the currency control, rate badge/info block, and error banner without disturbing the current responsive layout. |

#### Phase 4: Add component-level tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Create offers page spec | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add a focused spec for the offers page; current repo has no dedicated offers page test, so create one. |
| 4.2 | Test successful EUR conversion | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Mock `mock/offers.json`, `mock/reference-data.json`, and the NBP API response; assert converted whole-number premiums and visible rate text. |
| 4.3 | Test successful USD conversion | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify the second currency path behaves identically with the appropriate endpoint and currency label. |
| 4.4 | Test failure behavior | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Simulate API failure or invalid payload and assert PLN values remain plus an error message is shown. |
| 4.5 | Test non-mutation of source data | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert the loaded `Offer` objects still contain PLN-backed amounts after conversion logic runs. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Shared types for NBP exchange-rate payload and normalized applied-rate data. |
| `src/app/core/models/index.ts` | MODIFY | Export the new exchange-rate model if shared across component/repository/tests. |
| `src/app/core/repositories/currency-rates.repository.ts` | CREATE | Runtime integration with the Confluence-documented NBP exchange-rate API. |
| `src/app/core/repositories/currency-rates.repository.spec.ts` | CREATE | Unit tests for endpoint mapping and error handling. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency state, fetch flow, display helpers, and error/loading handling. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add selector, applied-rate info, error UI, and converted premium rendering. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new toolbar control and conversion/error metadata. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component tests covering conversion success/failure and non-mutation. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Launch the app and open `/offers`
5. [ ] Confirm initial premium rendering remains PLN
6. [ ] Select EUR and verify all visible offers show whole-number EUR values plus the applied rate/effective date
7. [ ] Select USD and verify all visible offers show whole-number USD values plus the applied rate/effective date
8. [ ] Force the NBP request to fail in tests and verify the UI shows an error while keeping PLN values
9. [ ] Verify filtering/sorting after conversion still updates visible rows using the active rate
10. [ ] Verify offer transition dialogs/navigation still function after the change