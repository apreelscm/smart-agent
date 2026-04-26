# SDLC-80: Waluty

## Specification

### Overview
This task adds currency presentation switching on the offers list screen (`/offers`). The current source premium values remain in PLN, but the user can switch the displayed premium amounts for the currently visible offers to EUR or USD. The application must fetch the exchange rate from the documented external service, round converted amounts to whole numbers, display the applied rate, and show an error when the exchange-rate service is unavailable.

### User Stories
- US-001: As a sales agent, I want to switch the premium presentation currency on the offers list, so that I can review visible offers in EUR or USD.
  - Given the offers list is loaded in PLN
  - When I select EUR or USD
  - Then the visible offer premiums are immediately re-rendered in the selected currency without reloading the page

- US-002: As a sales agent, I want to see the exchange rate used for conversion, so that I understand how the displayed amounts were calculated.
  - Given I selected EUR or USD
  - When the rate is fetched successfully
  - Then the screen shows the applied PLN→target currency rate used for the visible premiums

- US-003: As a sales agent, I want a clear error when the rate service is unavailable, so that I know why the currency switch could not be applied.
  - Given I attempt to change the presentation currency
  - When the exchange-rate service fails or returns no usable rate
  - Then the screen shows an error message and does not apply a partial/invalid conversion

### Functional Requirements
- FR-001: The offers home page must expose a currency selector in the toolbar for premium presentation.
- FR-002: The default presentation currency on page load must remain PLN.
- FR-003: The selector must allow switching premium presentation to EUR and USD.
- FR-004: Selecting EUR or USD must call the documented exchange-rate service at runtime, not a local mock JSON file.
- FR-005: The runtime default integration must reuse the documented NBP API from Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-006: The integration must use the documented public NBP endpoint shape `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json` for `CODE in {EUR, USD}`.
- FR-007: The integration must assume no authentication, using the documented public API contract.
- FR-008: The response handling must read the NBP payload shape `{ table, currency, code, rates: [{ effectiveDate, mid }] }` and use `rates[0].mid` as the exchange rate.
- FR-009: Conversion must be presentation-only and must not mutate the underlying `Offer`, `OfferVariant`, or `PaymentPlan` source data, which is currently PLN-based.
- FR-010: Converted amounts must be calculated from the existing base PLN premium and rounded to `0` decimal places before display.
- FR-011: The selected rate must be displayed to the user together with the selected target currency.
- FR-012: Conversion scope must be limited to offers visible on the current offers list view.
- FR-013: Existing filtering, sorting, transitions, and navigation on `OffersHomePageComponent` must continue to work unchanged.
- FR-014: If the exchange-rate request fails or the response does not contain a usable `mid` value, the page must show an error message after the selection attempt.
- FR-015: On failed conversion, the UI must keep the last valid presentation currency/amounts instead of showing mixed or empty values.

### Edge Cases
- EC-001: Switching back to PLN must not call the external API and must clear the displayed exchange-rate information.
- EC-002: Both motor and crop offers must use the same conversion logic because both currently render premium values from PLN-based offer data.
- EC-003: Offers without `selectedPaymentPlan` must continue using the existing fallback to `variants[0]?.totalPremium.amount` before conversion.
- EC-004: If NBP returns a response without `rates[0].mid`, the app must treat it as an error and keep the previous valid state.
- EC-005: Changing filters or sorting after selecting EUR/USD must continue to display the newly visible offers in the already selected currency.
- EC-006: Repeated switching between EUR and USD must always re-render the list using the rate for the currently selected target currency only.
- EC-007: Large or very small converted values must still be rendered using the Angular currency pipe with `1.0-0` formatting.

### Success Criteria
- [ ] The offers page shows a currency control with PLN as the default presentation and EUR/USD as selectable targets.
- [ ] Selecting EUR converts all currently visible offer premiums to EUR using the fetched NBP rate.
- [ ] Selecting USD converts all currently visible offer premiums to USD using the fetched NBP rate.
- [ ] Converted values are rounded to full amounts with no decimal places.
- [ ] The applied exchange rate is visible after successful conversion.
- [ ] If the rate service fails, the user sees an error message and the last valid displayed values remain on screen.
- [ ] The implementation uses the real NBP runtime endpoint and does not introduce a mock JSON runtime fallback.
- [ ] Existing offer filtering, sorting, transitions, and navigation continue to behave correctly.

### Open Questions
- [NEEDS CLARIFICATION: Czy zmiana waluty ma obejmować wyłącznie składki w wierszach listy ofert, czy także kafel „Średnia składka” oraz składkę pokazywaną w modalu zmiany statusu?]

---

## Implementation Plan

### Technical Approach
The codebase is an Angular 20 standalone app using:
- repository classes with `inject(HttpClient)` in `src/app/core/repositories/*.ts`
- local reactive UI state with `signal`, `computed`, and `toSignal`
- PrimeNG controls already used in `src/app/features/offers/pages/offers-home-page.component.ts`
- local SCSS styling backed by shared design tokens from `src/styles/_tokens.scss`

The implementation should follow those patterns rather than introducing a new state library.

#### Reused external service
Reuse the documented service from Confluence as the runtime default:
- [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Planned runtime integration:
- Endpoint: `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json`
- Supported runtime codes in scope: `EUR`, `USD`
- Auth: none
- Response contract used by the app: `table`, `currency`, `code`, `rates[0].effectiveDate`, `rates[0].mid`

This repo currently loads offers and reference data from `/public/mock/*.json` via `OffersRepository` and `ReferenceDataRepository`, but the currency-rate integration must **not** follow that mock-at-runtime pattern. Mocked exchange-rate payloads are acceptable only in unit tests via `HttpTestingController` or equivalent test fixtures.

#### Domain/repo considerations
- `src/app/core/models/common/money.model.ts` currently constrains `currency` to `'PLN'`, which matches the Jira scope that source data stays in PLN.
- Because the ticket is explicitly presentation-only, the plan should keep domain offer models unchanged and layer conversion logic in the offers page.
- The existing premium source logic is already centralized in `getPrimaryPremium(offer)` inside `src/app/features/offers/pages/offers-home-page.component.ts`; conversion should build on top of that helper instead of rewriting offer data.

#### UI/state approach
- Add a presentation-currency signal on `OffersHomePageComponent`, defaulting to `PLN`.
- Add a small exchange-rate state object on the page for:
  - selected target currency
  - fetched `mid` rate
  - effective date
  - loading/error status
- Add an `ExchangeRatesRepository` under `src/app/core/repositories` to encapsulate the NBP HTTP call and map the payload to a UI-friendly shape.
- Keep all conversion logic local to the offers page:
  - base amount = current `getPrimaryPremium(offer)`
  - PLN display = existing amount
  - EUR/USD display = `Math.round(plnAmount / mid)`
- Render converted amounts through the existing Angular currency pipe, but with a dynamic currency code instead of hardcoded `'PLN'`.
- Show the applied rate in the toolbar or results metadata area so it is global for the current list view, not repeated on every row.
- Show a page-level inline error near the selector/rate metadata when rate retrieval fails.
- Preserve existing list behavior (`filteredOffers`, transitions, split button actions, navigation) by treating conversion as a read-time presentation concern only.

#### Confluence/domain search outcome
- Relevant API/service documentation **was found** in `Services` and should be reused as above.
- No relevant ADR/convention/product-context pages were found in the configured `SDLC` domain space after scoped searches, so the plan relies on existing repository code patterns as the implementation convention.

### Task Breakdown

#### Phase 1: Add exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create exchange-rate repository | `src/app/core/repositories/exchange-rates.repository.ts` | Add a new repository using `inject(HttpClient)` that calls `https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`, validates `rates[0].mid`, and maps the response to a typed result used by the offers page. |
| 1.2 | Add repository unit tests | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Verify correct request URL for EUR/USD, successful mapping of `mid` and `effectiveDate`, and error propagation when the API fails or returns an invalid payload. |

#### Phase 2: Wire currency switching into the offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend offers page state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add signals for presentation currency, exchange-rate state, loading/error handling, and previous valid currency tracking so failed requests do not leave the UI in an inconsistent state. |
| 2.2 | Keep base offer data immutable | `src/app/features/offers/pages/offers-home-page.component.ts` | Add read helpers such as `getDisplayedPremiumAmount(offer)`, `getDisplayedPremiumCurrency()`, and `exchangeRateLabel()` rather than changing `Offer` or `Money` models. |
| 2.3 | Fetch rate on EUR/USD selection | `src/app/features/offers/pages/offers-home-page.component.ts` | Trigger `ExchangeRatesRepository` when the user switches away from PLN, store the active rate, and revert to the last valid display state on failure. |
| 2.4 | Add toolbar currency control | `src/app/features/offers/pages/offers-home-page.component.html` | Add a new `p-select` next to the existing filters, with PLN as default and EUR/USD as target choices. |
| 2.5 | Render dynamic premium and applied rate | `src/app/features/offers/pages/offers-home-page.component.html` | Replace hardcoded `currency: 'PLN'` bindings with dynamic currency/amount helpers and add a global rate display for the currently selected target currency. |
| 2.6 | Add inline error state | `src/app/features/offers/pages/offers-home-page.component.html` | Display a visible error message when the exchange-rate request fails, tied to the failed selection attempt. |
| 2.7 | Adjust responsive styling | `src/app/features/offers/pages/offers-home-page.component.scss` | Extend the toolbar layout for the new selector, rate chip/text, and error message while preserving current responsive behavior. |

#### Phase 3: Add page-level automated coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers page unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Test default PLN rendering, successful EUR/USD conversion, `0`-decimal rounding, applied-rate display, and failed selection behavior with the previous valid values preserved. |
| 3.2 | Use test-only HTTP fixtures | `src/app/core/repositories/exchange-rates.repository.spec.ts`, `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Use `HttpTestingController` payloads for success/error scenarios, ensuring mocks stay in the test layer only and never in production code paths. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Runtime integration with the documented NBP exchange-rate API. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit coverage for endpoint construction, payload mapping, and error handling. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add presentation-currency state, rate fetch flow, conversion helpers, and error handling. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency selector, rate display, error message, and dynamic premium rendering. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new currency-switching UI and keep the toolbar responsive. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component-level tests for conversion behavior and failure scenarios. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test -- --watch=false`
3. [ ] New repository tests verify the NBP request URL and payload mapping for EUR/USD
4. [ ] New offers page tests verify PLN default, EUR/USD conversion, `0`-decimal rounding, and applied-rate rendering
5. [ ] Manual verification on `/offers` confirms failed rate fetch shows an error and preserves the last valid displayed premiums