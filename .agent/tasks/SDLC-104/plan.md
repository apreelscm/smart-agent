# SDLC-104: Waluty

## Specification

### Overview
Dodanie na ekranie listy ofert przełącznika waluty prezentacji składek (`PLN`, `EUR`, `USD`) oraz integracji z bieżącym kursem NBP, tak aby użytkownik mógł zmieniać walutę bez opuszczania ekranu. Przeliczenie ma dotyczyć wyłącznie warstwy prezentacji na liście ofert, bez modyfikacji danych źródłowych ofert i innych ekranów procesu.

### User Stories
- US-001: As a sales agent, I want to switch the premium presentation currency on the offers list, so that I can review visible offers in PLN, EUR, or USD.
  - Given the offers list is loaded / When I choose `PLN`, `EUR`, or `USD` from the currency selector / Then the visible premium values update automatically in the selected currency.
- US-002: As a sales agent, I want to see the NBP rate used for conversion, so that I know how the displayed premium values were calculated.
  - Given I selected `EUR` or `USD` and the NBP call succeeded / When conversion is applied / Then a separate row between the filters row and the offers list shows the used exchange rate.
- US-003: As a sales agent, I want the screen to reject failed conversions safely, so that I never see incorrect premium values.
  - Given the NBP service is unavailable or returns invalid data / When I try to switch to `EUR` or `USD` / Then the app shows an error message and keeps the last valid premium presentation unchanged.

### Functional Requirements
- FR-001: Add a currency selector without label to `src/app/features/offers/pages/offers-home-page.component.html` in the toolbar row, positioned before the existing `Wyczyść` button.
- FR-002: The selector must offer exactly `PLN`, `EUR`, and `USD`.
- FR-003: The default presentation currency on initial page load must be `PLN`.
- FR-004: Switching from `PLN` to `EUR` or `USD` must call the NBP runtime API and use the returned current exchange rate as the default production integration.
- FR-005: The runtime integration must reuse the documented NBP service from Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-006: The NBP call must use the public endpoint `GET https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json` for `code in {EUR, USD}`, with no runtime mock fallback in production code.
- FR-007: The response parser must read `rates[0].mid` as the current PLN exchange rate and treat missing/invalid `rates[0].mid` as an integration error.
- FR-008: Premium conversion must be presentation-only and must not mutate `Offer`, `OfferVariant`, `PaymentPlan`, or persisted runtime offer data.
- FR-009: Conversion from PLN to `EUR`/`USD` must divide the source PLN premium by the NBP `mid` rate and round the result to `0` decimal places before display.
- FR-010: When `PLN` is selected, the screen must display original premium values and must not call NBP.
- FR-011: All premium displays on the offers list screen that are derived from visible offers must stay consistent with the selected currency, including:
  - row premium in the offer card,
  - average premium summary tile,
  - premium shown in the transition dialog.
- FR-012: After successful conversion to `EUR` or `USD`, display a separate exchange-rate row between the filters row and the offers list, showing the selected currency and the rate used for conversion.
- FR-013: When NBP is unavailable or returns an error, the screen must show an error message in that intermediate row area and keep the previous valid currency/premium presentation unchanged.
- FR-014: Currency switching must happen without page reload or navigation.
- FR-015: No new runtime JSON fixtures may be added under `public/mock` for exchange rates; mocked responses are allowed only in unit tests via HTTP testing utilities.

### Edge Cases
- EC-001: Selecting `PLN` after `EUR`/`USD` must restore original PLN amounts and hide the exchange-rate row.
- EC-002: If the user selects the same currency that is already active, no extra NBP request should be sent.
- EC-003: If the user changes from one foreign currency to another and the second NBP request fails, the screen must keep the last successful currency and values.
- EC-004: If NBP returns a structurally valid response but `rates` is empty or `mid` is missing, treat it as an error and do not update displayed premiums.
- EC-005: Rapid repeated currency changes should not leave the screen in a mixed state; only the last successful selection should become active.
- EC-006: Existing status/product/search/sort filters and offer actions must continue to work unchanged after currency switching.
- EC-007: Empty offer lists must still allow currency selection; the rate row may be shown even if no offers are visible, but no errors should occur.

### Success Criteria
- [ ] Currency selector is visible on the offers toolbar before `Wyczyść` and defaults to `PLN`.
- [ ] Choosing `EUR` recalculates visible premiums using the current NBP rate, rounded to whole amounts.
- [ ] Choosing `USD` recalculates visible premiums using the current NBP rate, rounded to whole amounts.
- [ ] After successful foreign-currency conversion, the rate row is visible between the filters row and the offers list.
- [ ] When the NBP call fails, an error is shown and no incorrect converted premiums are displayed.
- [ ] The change affects only the offers list screen and does not alter source offer data or other screens.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The codebase is an Angular 20 standalone-component app using PrimeNG, with page state managed through Angular signals and data access encapsulated in repositories under `src/app/core/repositories`. The offers list already follows this pattern in `OffersHomePageComponent`, which combines `OffersRepository`, `ReferenceDataRepository`, and `SalesFlowRuntimeRepository` via `toSignal(...)`, then derives view state with `computed(...)`.

This task should follow that same pattern:

1. **Reuse existing documented service instead of inventing a new backend**
   - Reuse the Confluence-documented NBP integration as the runtime default: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
   - Implement a new repository, e.g. `src/app/core/repositories/nbp-exchange-rates.repository.ts`, with `HttpClient`, matching the existing repository style in `offers.repository.ts` and `reference-data.repository.ts`.
   - Runtime call:
     - `GET https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json`
     - `GET https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json`
   - Expected response shape:
     ```ts
     {
       table: 'A';
       currency: string;
       code: 'EUR' | 'USD';
       rates: Array<{
         no: string;
         effectiveDate: string;
         mid: number;
       }>;
     }
     ```
   - Auth: none, per the public NBP API documented in Confluence.
   - No runtime JSON fallback is allowed for exchange rates. Test-only fixtures should be provided through `HttpTestingController`.

2. **Keep source offer data immutable**
   - Do not modify `Offer`, `OfferVariant`, `PaymentPlan`, or persisted runtime offers in `SalesFlowRuntimeRepository`.
   - Keep all conversion logic inside the offers page presentation layer by introducing component-level state such as:
     - committed presentation currency,
     - loading/error state for rate fetch,
     - current applied NBP quote metadata.
   - Derive displayed premium values from existing PLN amounts at render time.

3. **Add currency-specific presentation state to `OffersHomePageComponent`**
   - Extend the component with:
     - supported currency union: `type PresentationCurrency = 'PLN' | 'EUR' | 'USD'`,
     - committed selected currency signal defaulting to `PLN`,
     - active NBP rate signal for successful foreign-currency selections,
     - error signal/message,
     - loading signal to disable repeated fetches while a request is pending.
   - Use a dedicated handler for selector changes instead of directly binding `selectedCurrency.set(...)`, so the component can:
     - skip API calls for `PLN`,
     - call NBP for `EUR`/`USD`,
     - only commit the new currency after a successful response,
     - preserve the previous valid display on failure.

4. **Update only the offers page UI**
   - Add the unlabeled `p-select` before the existing `Wyczyść` button in `offers-home-page.component.html`.
   - Insert a new rate/error row between `.toolbar` and `.results-meta`, satisfying the Jira clarification about placement.
   - Replace hard-coded `currency: 'PLN'` usages in the offer row and transition dialog with the selected presentation currency.
   - Update `summaryTiles` so the “Średnia składka” tile reflects the selected currency instead of always appending `zł`.

5. **Formatting and conversion rules**
   - Source premium remains PLN.
   - Conversion formula:
     - `PLN -> EUR/USD = Math.round(plnAmount / midRate)`
   - Formatting:
     - keep `0` decimal places in UI,
     - use dynamic currency code in the Angular currency pipe for row/dialog premiums,
     - use `Intl.NumberFormat('pl-PL', ...)` or equivalent for the NBP rate row text.
   - Suggested rate-row content:
     - `Kurs NBP: 1 EUR = 4,3072 PLN (2026-04-17, tabela A, nr 074/A/NBP/2026)`
   - Hide the rate row for `PLN`.

6. **Domain knowledge reuse**
   - The SDLC Confluence search returned the generic application screen overview: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany).
   - No more specific ADR or UI convention for currency presentation was found in the configured DOMAIN scope, so the implementation should follow the existing toolbar/card patterns already used in `offers-home-page.component.html` and `.scss`.

### Task Breakdown

#### Phase 1: Add NBP integration and presentation state
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create NBP repository | `src/app/core/repositories/nbp-exchange-rates.repository.ts` | Add a new `HttpClient`-based repository for public NBP table A rates for `EUR` and `USD`, including response mapping and validation of `rates[0].mid`. |
| 1.2 | Define local exchange-rate types | `src/app/core/repositories/nbp-exchange-rates.repository.ts` | Export minimal typed interfaces for the NBP response and the normalized quote consumed by the offers page. |
| 1.3 | Add currency state to offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce committed presentation currency, available selector options, loading state, active rate metadata, and error state. |
| 1.4 | Implement currency change flow | `src/app/features/offers/pages/offers-home-page.component.ts` | Add handler that skips NBP for `PLN`, fetches NBP for `EUR`/`USD`, commits the new currency only on success, and preserves the previous display on failure. |
| 1.5 | Implement conversion helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Add helper methods/computed values for displayed premium amount, average premium tile value, and transition dialog premium based on the committed currency. |

#### Phase 2: Update offers list UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add currency selector to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Insert unlabeled `p-select` before the `Wyczyść` button in `.toolbar__actions`, bound to the new currency change handler. |
| 2.2 | Add rate/error row | `src/app/features/offers/pages/offers-home-page.component.html` | Render an info row between `.toolbar` and `.results-meta` for the successful NBP rate, and an error variant when conversion fails. |
| 2.3 | Replace fixed PLN formatting | `src/app/features/offers/pages/offers-home-page.component.html` | Update the offer row premium and transition dialog premium to use dynamic currency output instead of hard-coded `PLN`. |
| 2.4 | Update summary tile value | `src/app/features/offers/pages/offers-home-page.component.ts` | Make “Średnia składka” reflect the selected currency, not a hard-coded `zł` suffix. |
| 2.5 | Style new controls and state rows | `src/app/features/offers/pages/offers-home-page.component.scss` | Add styles for the currency selector width/alignment, rate row, error row, and loading/disabled states while keeping existing responsive toolbar behavior. |

#### Phase 3: Test the behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add offers page unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Create tests with `provideHttpClientTesting()` / `HttpTestingController` covering default `PLN`, successful `EUR` conversion, successful `USD` conversion, `PLN` reset without NBP call, and error handling that preserves the previous valid state. |
| 3.2 | Verify no runtime mock fallback | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert that exchange-rate behavior is driven by HTTP responses only; do not add any `public/mock/*exchange*` file. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/nbp-exchange-rates.repository.ts` | CREATE | Repository for public NBP current exchange-rate retrieval and normalization. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add presentation currency state, NBP fetch flow, conversion helpers, rate/error state, and dynamic summary value logic. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency selector before `Wyczyść`, exchange-rate/error row, and dynamic currency rendering for premiums. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new selector and the info/error row while preserving current responsive toolbar behavior. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Unit tests for currency switching, NBP integration behavior, conversion rounding, and error handling. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] On `/offers`, the currency selector appears before `Wyczyść` and defaults to `PLN`
5. [ ] Switching to `EUR` sends `GET https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json` and updates visible premiums to rounded EUR values
6. [ ] Switching to `USD` sends `GET https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json` and updates visible premiums to rounded USD values
7. [ ] The NBP rate row appears between the toolbar and the offers list when `EUR` or `USD` is active
8. [ ] Switching back to `PLN` restores original PLN values and hides the rate row
9. [ ] If the NBP request fails, an error row is shown and the previous valid currency/premium display remains unchanged
10. [ ] No new runtime mock exchange-rate file exists under `public/mock`