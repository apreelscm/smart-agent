# SDLC-43: Wielowalutowość na liście ofert

## Specification

### Overview
Task delivers display-level multicurrency support on the offers list screen (`/offers`) so the user can switch offer amount presentation to EUR or USD and immediately see all visible offer premiums recalculated using exchange rates from the existing documented currency service.

Current repository state shows that offer money values are stored as PLN-only (`src/app/core/models/common/money.model.ts`), and the offers list hardcodes PLN formatting in three places inside `src/app/features/offers/pages/offers-home-page.component.*`. The implementation should therefore keep persisted offer data unchanged in PLN and add conversion only in the presentation layer for the offers list screen.

### User Stories
- US-001: As a user browsing the offers list, I want to switch the display currency to EUR or USD, so that I can compare offers in my preferred currency.
  - Given I am on the offers list / When I open the currency selector / Then I can choose EUR or USD
- US-002: As a user browsing the offers list, I want all visible offer amounts to be recalculated after I change currency, so that I do not need to convert values manually.
  - Given offers are visible on the list / When I switch the display currency / Then all visible offer premiums are shown in the selected currency
- US-003: As a business owner, I want exchange rates to come from the existing documented service, so that the values are consistent and based on one source of truth.
  - Given the application needs an EUR or USD rate / When it requests the rate / Then it uses the documented exchange-rate integration instead of a local mock fallback

### Functional Requirements
- FR-001: Add a display currency selector to the offers list page with PLN as the initial current view and EUR/USD as selectable presentation currencies.
- FR-002: Recalculate all visible offer premiums on `filteredOffers()` when EUR or USD is selected, using PLN as the stored source amount.
- FR-003: Recalculate every amount rendered within the offers list screen from the same selected currency context, including:
  - offer row premium box,
  - transition dialog premium preview,
  - average premium summary tile.
- FR-004: Reuse the documented currency service from Confluence as the runtime default integration:
  - service catalog page: [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
  - reference contract: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- FR-005: The runtime integration must call the real NBP API endpoints documented by the Confluence reference, using:
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json`
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json`
  - no auth
  - response shape mapped from `{ table, currency, code, rates: [{ no, effectiveDate, mid }] }`
- FR-006: Converted values must be formatted with the selected currency code/symbol so the user can clearly identify the active display currency.
- FR-007: The offers repository (`src/app/core/repositories/offers.repository.ts`) and stored offer data remain unchanged as the source of base PLN amounts; no mutation of persisted offer premiums is introduced.
- FR-008: No mock JSON file may be used as a runtime exchange-rate fallback; mocks are allowed only in unit tests.
- FR-009: Scope remains limited to the offers list screen; no changes are required in offer details, wizard pages, policies, or other modules.

### Edge Cases
- EC-001: If the EUR or USD rate request fails, the page should keep displaying the last valid active currency state, show a non-blocking error/info message, and avoid showing mismatched selected currency vs PLN values.
- EC-002: If a visible offer has no `selectedPaymentPlan`, conversion must continue to use the current fallback source amount from `offer.variants[0]?.totalPremium.amount`.
- EC-003: If the user changes filters or sorting after selecting a display currency, newly visible offers must render in the same active currency without requiring another user action.
- EC-004: If the exchange-rate API returns a rate with an `effectiveDate` older than the current day, the UI should still use it and expose the returned effective date as the applied rate date.
- EC-005: If one supported currency is unavailable but the other is available, only the unavailable option should be treated as unusable; the page should not lose the whole feature.

### Success Criteria
- [ ] The offers page exposes EUR and USD as selectable display currencies
- [ ] All visible premiums on the offers list are consistently displayed in the selected currency
- [ ] The average premium summary tile updates to the same selected currency
- [ ] The transition dialog premium uses the same currency conversion as the list row
- [ ] Exchange rates are fetched from the documented NBP integration, not from a local runtime mock
- [ ] Unit tests cover endpoint mapping and UI conversion behavior

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Follow existing repository and page-component patterns already used in the codebase:

- data access classes live under `src/app/core/repositories` and use `HttpClient`
- standalone Angular components keep page state with `signal`, `computed`, and `toSignal`
- the offers list page already centralizes filtering/sorting and premium rendering in `OffersHomePageComponent`

Implementation should reuse the documented exchange-rate service from Confluence:
- [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
- [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Runtime default integration:
- call the real NBP endpoints directly from a new repository:
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json`
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/USD/?format=json`
- map `rates[0].mid` as the PLN-per-currency quote
- map `rates[0].effectiveDate` for UI traceability
- do not add any runtime JSON fallback under `public/mock`

Domain knowledge discovery:
- No relevant additional domain conventions were found in the configured `SDLC` space.

Design decisions:
1. **Keep source offer amounts in PLN**
   - Current domain model and mocked source data are PLN-based.
   - Conversion should be display-only to avoid broad model/storage churn.

2. **Introduce a small exchange-rate repository**
   - Add a dedicated repository aligned with `OffersRepository` / `ReferenceDataRepository`.
   - Repository owns the external HTTP contract and response mapping.

3. **Drive conversion from the offers page state**
   - Add a `selectedDisplayCurrency` signal and a cached rate state for EUR/USD.
   - Keep list filtering and sorting untouched.
   - Apply conversion only where money is rendered on the offers list screen.

4. **Use one shared conversion helper inside the component**
   - Replace hardcoded PLN formatting with shared methods for:
     - resolving base premium,
     - converting PLN -> selected currency,
     - formatting digits by currency,
     - exposing selected currency / rate date.
   - This prevents drift between list rows, summary tile, and transition dialog.

5. **Handle failures without inconsistency**
   - If rate loading fails, do not leave the page in a state where the selector shows EUR/USD but amounts still render in PLN.
   - Prefer retaining the last valid active currency and showing an inline status message.

### Task Breakdown

#### Phase 1: Currency contracts and external integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Add reusable currency types | `src/app/core/models/common/currency-code.model.ts`, `src/app/core/models/common/money.model.ts`, `src/app/core/models/index.ts` | Introduce a shared `CurrencyCode` union (`PLN`, `EUR`, `USD`) and widen `Money.currency` typing so display helpers can use the same model vocabulary without changing stored offer source data semantics. |
| 1.2 | Add exchange-rate response model | `src/app/core/models/common/exchange-rate.model.ts`, `src/app/core/models/index.ts` | Define typed DTOs for the documented NBP response and a normalized quote shape (`code`, `mid`, `effectiveDate`). |
| 1.3 | Create exchange-rate repository | `src/app/core/repositories/exchange-rates.repository.ts` | Implement `HttpClient` access to `https://api.nbp.pl/api/exchangerates/rates/A/{code}/?format=json`, normalize the response, and optionally cache per-currency requests to avoid duplicate calls during repeated switching. |

#### Phase 2: Offers page multicurrency presentation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend offers page state with currency selection | `src/app/features/offers/pages/offers-home-page.component.ts` | Add page state for active display currency, loading/error state, and fetched EUR/USD quotes while preserving current filter/sort logic. |
| 2.2 | Add conversion helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Add shared helpers for base premium resolution, PLN-to-foreign conversion (`amount / mid`), currency digits config, and active rate metadata. |
| 2.3 | Update summary tile calculation | `src/app/features/offers/pages/offers-home-page.component.ts` | Convert the average premium tile to the active display currency instead of appending a hardcoded `zł` suffix. |
| 2.4 | Add currency selector and status UI | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Add a new toolbar control for currency selection and a small inline indicator/message showing active currency and applied NBP rate date or load/error state. |
| 2.5 | Replace hardcoded PLN rendering in offer rows | `src/app/features/offers/pages/offers-home-page.component.html` | Replace `currency:'PLN'` pipes in the premium box with dynamic formatting based on the selected display currency and converted amount. |
| 2.6 | Replace hardcoded PLN rendering in dialog | `src/app/features/offers/pages/offers-home-page.component.html` | Ensure the transition dialog premium uses the same conversion helper so all amounts on the page stay consistent. |

#### Phase 3: Tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add repository unit tests | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Use `HttpTestingController` to verify the exact NBP endpoint URLs, request method, and response mapping from `rates[0].mid` / `effectiveDate`. |
| 3.2 | Add offers page unit tests | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify that selecting EUR/USD updates rendered premiums and summary amounts, and that rate-load failure keeps a valid fallback UI state without using runtime mocks. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/currency-code.model.ts` | CREATE | Shared currency union for display currency selection and exchange-rate typing |
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Typed NBP response and normalized quote model |
| `src/app/core/models/common/money.model.ts` | MODIFY | Widen money currency typing beyond hardcoded PLN |
| `src/app/core/models/index.ts` | MODIFY | Export new shared currency and exchange-rate models |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Runtime integration with documented NBP exchange-rate endpoints |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit tests for endpoint usage and response mapping |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency state, rate loading, conversion helpers, and dynamic summary calculations |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency selector and replace hardcoded PLN render paths with dynamic display values |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new currency control and inline rate/status indicator |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Unit tests for multicurrency UI behavior on the offers page |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test -- --watch=false`
3. [ ] New repository tests confirm requests go to `https://api.nbp.pl/api/exchangerates/rates/A/EUR/?format=json` and `.../USD/?format=json`
4. [ ] Manual check on `/offers`: switching currency updates all visible offer premiums, summary tile, and dialog premium consistently
5. [ ] Failure path check: when rate request fails in tests, the page keeps a valid display state and shows non-blocking feedback
6. [ ] No production code path reads exchange rates from `public/mock/*`