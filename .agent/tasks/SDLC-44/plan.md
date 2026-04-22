# SDLC-44: Wybór waluty i przeliczanie na liście ofert

## Specification

### Overview
This task adds presentation-currency switching on the offers list screen so the user can view premiums in EUR or USD, with values recalculated from the existing PLN-based offer data using exchange rates fetched from the documented currency-rate service. The change is limited to the offers page and the premium values visible there.

### User Stories
- US-001: As a sales agent browsing the offers list, I want to switch premium presentation to EUR or USD, so that I can compare offers in my preferred currency.
  - Given I am on the offers list page / When I choose EUR or USD as the presentation currency / Then all premiums visible on that screen are recalculated and displayed in the selected currency.

- US-002: As the business, I want exchange rates to come from the existing documented service, so that displayed values stay consistent with the approved source of truth.
  - Given the offers list needs currency conversion / When the application calculates displayed premiums / Then it uses rates returned by the documented currency-rate integration, not hardcoded or mock runtime values.

### Functional Requirements
- FR-001: Add a presentation-currency control to `src/app/features/offers/pages/offers-home-page.component.html` with selectable values `EUR` and `USD`, placed in a new row below the existing filters.
- FR-002: Keep offer source premiums unchanged in their current base form; conversion must be presentation-only on the offers page.
- FR-003: When the user selects `EUR` or `USD`, convert premium amounts for all currently visible offers on the page using the runtime exchange-rate service.
- FR-004: The conversion must apply to every premium value visible on the offers page, including:
  - the premium shown in each offer row,
  - the “Średnia składka” summary tile,
  - the premium shown inside the transition dialog opened from the offers page.
- FR-005: For each offer, use the same premium source as the current screen logic:
  - `offer.selectedPaymentPlan?.totalPremium.amount`,
  - otherwise `offer.variants[0]?.totalPremium.amount`,
  - otherwise `0`.
- FR-006: Fetch exchange rates from the documented service in Confluence:
  - [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
  - [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- FR-007: Runtime default integration must call the documented NBP API table endpoint for rates, using the public `A` table response for EUR/USD lookup (`GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, no auth), and map returned `rates[].code` / `rates[].mid` values for conversion.
- FR-008: The application must not introduce any runtime fallback based on `public/mock/*.json` for exchange rates. Mock payloads are allowed only in test code.
- FR-009: The change scope is limited to the offers page; no other screens should change premium presentation in this task.
- FR-010: Both MOTOR and CROP offers, including runtime offers merged from `SalesFlowRuntimeRepository`, must use the same presentation-currency logic because they are rendered through the same `OffersHomePageComponent`.

### Edge Cases
- EC-001: If an offer has no `selectedPaymentPlan`, the UI continues using the first variant premium, then converts that fallback amount.
- EC-002: If the rate response does not contain EUR or USD, the selector must not show broken values; the screen should keep existing PLN presentation and surface a non-blocking error state.
- EC-003: If the exchange-rate call fails, the page must remain usable, keep current PLN presentation, and avoid rendering incorrect converted amounts.
- EC-004: If the user changes filters after selecting a currency, newly visible offers must render in the already selected presentation currency without an additional user action.
- EC-005: If the transition dialog is opened after a currency is selected, the premium inside the dialog must match the same converted currency as the list.
- EC-006: Crop and motor offers must behave identically even though their subject metadata differs, because their premium shape is shared.

### Success Criteria
- [ ] The offers page shows a currency selector with EUR and USD options in a new row below the existing filters.
- [ ] Selecting EUR recalculates all premium values visible on the offers page to EUR.
- [ ] Selecting USD recalculates all premium values visible on the offers page to USD.
- [ ] Converted values are based on the documented currency-rate service, not static or mock runtime data.
- [ ] Existing offer filtering, sorting, navigation, and status-transition flows continue to work.
- [ ] No other screens outside the offers page change currency behavior in this task.

### Open Questions
- [NEEDS CLARIFICATION: Should the offers page keep current PLN display until the user explicitly selects EUR/USD, or should one foreign currency be preselected on first load?]
- [NEEDS CLARIFICATION: What rounding rule should be used for converted premiums in EUR/USD presentation (standard 2-decimal currency formatting or another rule)?]
- [NEEDS CLARIFICATION: On exchange-rate service outage, should the UI keep PLN values with a warning, or should currency switching be blocked entirely?]

---

## Implementation Plan

### Technical Approach
The repo is an Angular 20 standalone app using signals/computed state and a repository pattern:
- page state lives directly in standalone components such as `src/app/features/offers/pages/offers-home-page.component.ts`,
- external/data access is encapsulated in `src/app/core/repositories/*.ts`,
- the offers page already uses `toSignal(...)`, `signal(...)`, and `computed(...)` heavily.

This implementation should follow those existing patterns:

1. **Add a dedicated repository for exchange rates**
   - Create `src/app/core/repositories/currency-rates.repository.ts`.
   - Reuse the documented service from Confluence:
     - [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
     - [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
   - Runtime default should call the documented NBP endpoint:
     - `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`
     - auth: none
     - expected response shape: array containing table metadata and `rates[]` entries with `code` and `mid`
   - Map only `EUR` and `USD` rates needed by this task.
   - Cache/share the response in the repository so the page does not refetch the same table repeatedly during a single session.

2. **Keep conversion in the presentation layer**
   - Current repo data in `public/mock/offers.json` is PLN-based, and current premium rendering is hardcoded to PLN in `offers-home-page.component.html`.
   - Do **not** mutate persisted offer objects or rewrite mock offer data.
   - Do **not** add any new runtime mock JSON file for exchange rates.
   - Perform conversion only when deriving values for display on the offers page.

3. **Extend `OffersHomePageComponent` with currency state**
   - Add a new signal for selected presentation currency.
   - Add a rates signal derived from the new repository.
   - Add helper/computed methods to:
     - resolve the base premium amount exactly as current logic does,
     - convert a PLN amount to EUR/USD using the selected `mid` rate,
     - expose currency-aware premium display for:
       - row premium,
       - summary tile,
       - transition dialog.

4. **Update the template and styles**
   - Add a currency selector below the existing filters, in a separate new row, while reusing the current PrimeNG `p-select` pattern already used for status/product/sort controls.
   - Replace hardcoded `PLN` currency pipe usage in the offer row and dialog with selected-currency-aware rendering.
   - Update the “Średnia składka” tile to render in the same selected currency instead of the current hardcoded `zł`.
   - Add minimal styling in `offers-home-page.component.scss` for the new selector row and for any small helper/error text.

5. **Error handling**
   - If rates are unavailable, keep the current PLN rendering rather than showing stale or incorrect converted values.
   - Surface the error non-disruptively on the offers page; do not block the rest of the page behavior.
   - This keeps the screen aligned with the existing repo style, which favors local component state rather than a global error framework.

6. **Confluence/domain findings**
   - Existing service found in `Services` space and should be reused; no new currency backend should be proposed.
   - No relevant additional domain guidance was found in the configured `SDLC` space, so no SDLC-specific ADR/convention constrains the design beyond the Jira requirements.

### Task Breakdown

#### Phase 1: Add exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create exchange-rate repository | `src/app/core/repositories/currency-rates.repository.ts` | Add a repository that calls the documented NBP table A endpoint and maps EUR/USD rates from the response. |
| 1.2 | Add repository-level caching/mapping | `src/app/core/repositories/currency-rates.repository.ts` | Share a single fetched response and expose a typed result for the offers page to consume. |
| 1.3 | Add repository unit tests | `src/app/core/repositories/currency-rates.repository.spec.ts` | Verify request URL, response mapping, EUR/USD extraction, and failure handling using `HttpTestingController` fixtures only. |

#### Phase 2: Wire currency selection into offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add presentation-currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add signal/computed state for selected currency, rates loading/error state, and selected-rate lookup. |
| 2.2 | Add conversion helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Implement helper logic to resolve base premium, convert from PLN using NBP `mid` rate, and return display-ready values. |
| 2.3 | Update summary tile calculation | `src/app/features/offers/pages/offers-home-page.component.ts` | Replace the current hardcoded `zł` string in `summaryTiles` with currency-aware formatting based on the selected presentation currency. |
| 2.4 | Add currency selector UI | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a new `p-select` control below the existing filters, in a dedicated new row for the currency action.<br>Follow the existing filter control structure while keeping this control visually separate from the filter row. |
| 2.5 | Replace hardcoded PLN rendering | `src/app/features/offers/pages/offers-home-page.component.html` | Update the offer-row premium box and transition dialog premium to use selected-currency display instead of the current `currency: 'PLN'` pipe usage. |
| 2.6 | Add rate-state messaging | `src/app/features/offers/pages/offers-home-page.component.html` | Show lightweight loading/error helper text near the selector in the new row when rates are being fetched or unavailable. |
| 2.7 | Style new controls | `src/app/features/offers/pages/offers-home-page.component.scss` | Add styling for the separate selector row, control sizing, and helper-text presentation so the new control sits below the filters and remains responsive. |

#### Phase 3: Add component-level regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers page component tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify selector rendering, EUR/USD conversion of visible premiums, summary tile updates, fallback to first variant premium, and non-blocking failure behavior. |
| 3.2 | Keep runtime integration mock-free | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/core/repositories/currency-rates.repository.spec.ts` | Use test-only fixtures and `HttpTestingController`; do not add or modify `public/mock` runtime files for rates. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/currency-rates.repository.ts` | CREATE | Repository for documented NBP currency-rate integration and EUR/USD rate mapping. |
| `src/app/core/repositories/currency-rates.repository.spec.ts` | CREATE | Unit tests for request URL, mapping, and error behavior. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency selector state, exchange-rate consumption, conversion helpers, and currency-aware summary calculations. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add EUR/USD selector below the filter row and replace hardcoded PLN premium rendering with selected-currency rendering. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new selector row and helper/error text in the existing toolbar layout. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component tests covering conversion behavior and regressions on the offers page. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] The offers page shows EUR/USD currency selection in a separate row below the existing filters
5. [ ] Selecting EUR updates row premiums, summary premium, and dialog premium to EUR
6. [ ] Selecting USD updates row premiums, summary premium, and dialog premium to USD
7. [ ] Network inspection confirms the page uses the documented NBP runtime endpoint, not a mock JSON file
8. [ ] Existing filtering, sorting, navigation, and status transitions on the offers page still work

## Revision History
- Revision 1
  - Reviewer: matlipinski
  - Summary of changes made: split task 2.4 description into a new line in the Phase 2 table.
- Revision 2
  - Reviewer: matlipinski
  - Summary of changes made: moved the currency selector from beside the filters to a separate new row below them across requirements, implementation approach, tasks, file summary, and verification steps.