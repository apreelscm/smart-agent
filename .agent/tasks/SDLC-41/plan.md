# SDLC-41: Wielowalutowość na liście ofert

## Specification

### Overview
This task adds a presentation-only currency switcher to the offers list so agents can display list amounts in PLN, EUR, or USD without changing any stored offer data. The offers view must still open in PLN every time, use live exchange rates from the documented NBP integration, keep all list amounts in one consistent currency, and fall back safely to PLN when the exchange-rate service is unavailable.

### User Stories
- US-001: As a sales agent, I want to switch the offers list between PLN, EUR, and USD, so that I can discuss offers with clients in the currency they use.
  - Given I am on `/offers`
  - When I choose EUR or USD in the currency switcher
  - Then all monetary values shown on the offers list are recalculated from PLN and rendered in the selected currency

- US-002: As a sales agent, I want PLN to remain the default and safe fallback, so that the list is always usable even if exchange rates are unavailable.
  - Given I open the offers list in a new visit/session
  - When the page loads
  - Then the selected currency is PLN by default

- US-003: As a sales agent, I want to see which rate was used, so that foreign-currency amounts are understandable and auditable during the conversation.
  - Given the list is displayed in EUR or USD
  - When exchange rates are available
  - Then the UI shows the applied rate and effective date

### Functional Requirements
- FR-001: Add a currency switcher on the offers list header with exactly three options: `PLN | EUR | USD`.
- FR-002: The offers list must always initialize in PLN on each entry to `/offers`; the selection must not be persisted in profile, local storage, or backend state.
- FR-003: All currently rendered monetary values on the offers list must use the selected display currency, including:
  - offer premium values in each row
  - monetary summary tiles shown above the list
  - any additional list-level monetary summaries introduced as part of this task
- FR-004: PLN formatting must stay visually consistent with the current implementation (`pl-PL`, narrow symbol, zero decimals).
- FR-005: EUR and USD formatting must use `pl-PL` locale with currency symbol and exactly two decimal places.
- FR-006: Exchange rates must be fetched at runtime from the documented NBP service in Confluence: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- FR-007: Runtime integration must use the real NBP endpoint `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, with no auth, mapping `effectiveDate` plus `rates[].code` / `rates[].mid` for EUR and USD.
- FR-008: Conversion must treat PLN as the source currency for persisted offer amounts and compute foreign display values from PLN using the latest fetched NBP mid rate.
- FR-009: When EUR or USD is active, the UI must show the applied rate in a discreet but visible form, e.g. `kurs 4,35 PLN/EUR z 20.04.2026`.
- FR-010: If the exchange-rate service is unavailable, the page must remain usable in PLN, EUR/USD options must be disabled, and the user must see a clear explanatory message.
- FR-011: Switching currency must not trigger any write operation to offer storage, runtime offer state, policy promotion state, or backend APIs.
- FR-012: Offer details, offer wizard pages, policies, reports, and other screens remain unchanged and continue to display PLN only.

### Edge Cases
- EC-001: If the NBP request fails, times out, or returns malformed data, the list stays in PLN, EUR/USD are disabled, and the page does not break.
- EC-002: If only one foreign rate is present in the NBP payload, enable only that currency and disable the missing one.
- EC-003: If an offer lacks `selectedPaymentPlan`, premium conversion must continue using the existing fallback to `variants[0].totalPremium.amount`.
- EC-004: If the rate fetch finishes after initial render, the list must remain in PLN until the user explicitly switches currency.
- EC-005: Navigating away from `/offers` and back must reset the switcher to PLN because the selection is list-local, not persisted.
- EC-006: Non-monetary counters such as offer counts remain unchanged and must not be formatted as currency.
- EC-007: No mock JSON exchange-rate file may be introduced for production runtime; mocks are allowed only in specs via `HttpTestingController` fixtures.

### Success Criteria
- [ ] `/offers` shows a visible `PLN | EUR | USD` switcher in the list header/toolbar
- [ ] The offers list always opens in PLN
- [ ] Premiums and monetary summary tiles switch consistently between PLN, EUR, and USD
- [ ] PLN formatting is unchanged from the current UI
- [ ] EUR/USD formatting uses two decimals and the correct currency symbol
- [ ] The applied NBP rate and effective date are visible when EUR or USD is selected
- [ ] If the NBP service is unavailable, PLN remains usable and EUR/USD are disabled with a clear message
- [ ] Currency switching does not modify offer data or write to runtime repositories
- [ ] No screens outside the offers list are changed
- [ ] Unit tests cover conversion, default/fallback behavior, and unavailable-service handling

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The existing offers screen is a standalone Angular component built around `signal`, `computed`, and `toSignal` in `src/app/features/offers/pages/offers-home-page.component.ts`, with data access abstracted behind repository classes in `src/app/core/repositories/*.repository.ts`. The safest implementation is to keep persisted domain models unchanged and add a display-only currency layer inside the offers page.

Reuse of existing service/documentation:
- Reuse the documented external service from Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- Runtime default integration:
  - Endpoint: `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`
  - Auth: none
  - Response fields to map: table payload `effectiveDate` and `rates[]` entries for `EUR` and `USD` using `mid`
- No relevant domain guidance was found in the configured `SDLC` Confluence space, so the implementation will follow current repo conventions only.

Codebase-aligned design decisions:
- Keep `Money` as PLN-only in `src/app/core/models/common/money.model.ts`; the Jira explicitly says this is presentation-only and persisted data must remain in PLN.
- Introduce a new repository dedicated to exchange-rate reads, following the same pattern as `OffersRepository`, `PoliciesRepository`, and `ReferenceDataRepository`.
- Keep the selected currency as a component-local signal in `OffersHomePageComponent`; do not store it in `SalesFlowRuntimeRepository`, query params, or browser storage.
- Replace the hardcoded `currency: 'PLN'` template pipe usage in the offers list with helper methods/computed values that convert from persisted PLN to the selected display currency before formatting.
- Use the real NBP service as the runtime default. Do not add `public/mock/exchange-rates.json` or any equivalent production fallback.
- Handle service failure defensively in the read layer so the page still renders offers in PLN and foreign options can be disabled cleanly.

Implementation shape:
- Create `ExchangeRatesRepository` returning a small normalized view model, e.g. `{ effectiveDate, rates: { EUR, USD }, available }`, catching transport/mapping failures and exposing an unavailable state instead of throwing into the component tree.
- In `OffersHomePageComponent`, add:
  - `selectedCurrency` signal with default `PLN`
  - computed currency option metadata for `PLN`, `EUR`, `USD`
  - computed rate availability state
  - helpers for conversion from PLN and locale-aware formatting
  - computed/derived rate label for the active foreign currency
- Update the toolbar UI in `offers-home-page.component.html` with a compact segmented switch built from existing `pButton` usage, which allows PLN to remain enabled even when EUR/USD are unavailable.
- Update `summaryTiles` so the existing monetary tile (`Średnia składka`) renders in the active display currency while count tiles remain unchanged.
- Leave dialogs, offer wizard routes, and other feature pages untouched so the scope stays limited to the offers list.

### Task Breakdown

#### Phase 1: Add exchange-rate runtime integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create exchange-rate repository | `src/app/core/repositories/exchange-rates.repository.ts` | Add a new repository that calls `https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, extracts `effectiveDate`, `EUR.mid`, and `USD.mid`, and returns a normalized read model for the UI. |
| 1.2 | Implement safe fallback state | `src/app/core/repositories/exchange-rates.repository.ts` | Catch HTTP/mapping failures and return an unavailable state instead of throwing, so `/offers` can still render in PLN. |
| 1.3 | Add repository unit tests | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Verify successful mapping of NBP response and unavailable fallback on HTTP error using `HttpTestingController` fixtures only. |

#### Phase 2: Add display-currency state and conversion logic to offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Inject exchange-rate repository into offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Follow existing repository injection style and expose the rate state via `toSignal(...)`. |
| 2.2 | Add component-local currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce `selectedCurrency` with default `PLN` and option metadata for `PLN`, `EUR`, and `USD`. |
| 2.3 | Add conversion and formatting helpers | `src/app/features/offers/pages/offers-home-page.component.ts` | Implement helpers that convert persisted PLN amounts to EUR/USD using NBP `mid` rates and format output according to the Jira rules. |
| 2.4 | Preserve existing premium fallback | `src/app/features/offers/pages/offers-home-page.component.ts` | Reuse the current `selectedPaymentPlan -> variants[0]` fallback path so display logic stays compatible with existing offer payloads. |
| 2.5 | Update summary tile currency handling | `src/app/features/offers/pages/offers-home-page.component.ts` | Keep count tiles unchanged and convert only monetary summary values such as `Średnia składka`. |
| 2.6 | Enforce PLN-only fallback behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure unavailable foreign rates disable EUR/USD options and keep the active selection in PLN. |
| 2.7 | Keep persistence untouched | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm the new state does not call `saveDraftOffer`, `promoteOfferToPolicy`, or mutate offer objects during currency switching. |

#### Phase 3: Update offers list UI
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add currency switcher to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Add a list-header control with `PLN | EUR | USD`, placed alongside existing toolbar controls and styled consistently with the current PrimeNG button usage. |
| 3.2 | Render converted monetary values | `src/app/features/offers/pages/offers-home-page.component.html` | Replace hardcoded PLN currency-pipe rendering in the offer premium box with the new formatter/helper output. |
| 3.3 | Show applied rate info | `src/app/features/offers/pages/offers-home-page.component.html` | Add a subtle helper text line for foreign currency mode showing the active NBP rate and effective date. |
| 3.4 | Show unavailable-service message | `src/app/features/offers/pages/offers-home-page.component.html` | Add a clear inline message when exchange rates cannot be loaded and foreign options are disabled. |
| 3.5 | Adjust layout styling | `src/app/features/offers/pages/offers-home-page.component.scss` | Add styles for the currency switcher, helper text, disabled state messaging, and responsive wrapping without disturbing the existing toolbar layout. |

#### Phase 4: Add focused component tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Create offers page spec | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add a standalone component spec covering default PLN render, switching to EUR/USD, rate-label visibility, and disabled foreign options on rate failure. |
| 4.2 | Mock runtime dependencies in tests only | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Use `HttpTestingController` for `mock/offers.json`, `mock/reference-data.json`, and the NBP endpoint; do not introduce production mock files. |
| 4.3 | Verify no regression in current list behavior | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert existing list content still renders and that PLN formatting remains unchanged when no foreign currency is selected. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Runtime repository for NBP table A exchange-rate fetch and normalization. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit tests for successful NBP mapping and unavailable fallback behavior. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency state, exchange-rate signal wiring, conversion helpers, and summary-tile updates. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add currency switcher, converted amount rendering, rate info, and unavailable-service message. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Style the new switcher and helper messaging within the existing responsive toolbar/card layout. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE | Component-level tests for default, switched, and fallback currency-display scenarios. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm test -- --watch=false`
3. [ ] `/offers` opens with PLN selected by default
4. [ ] Clicking EUR recalculates row premiums and the monetary summary tile using the NBP EUR rate
5. [ ] Clicking USD recalculates row premiums and the monetary summary tile using the NBP USD rate
6. [ ] PLN rendering matches the current UI formatting when PLN is selected
7. [ ] A visible rate label appears only for EUR/USD and includes the effective date
8. [ ] When the NBP request fails, EUR/USD are disabled, a clear message is shown, and PLN remains usable
9. [ ] Navigating away from `/offers` and back resets the view to PLN
10. [ ] No write-side repositories are called as part of currency switching
11. [ ] New tests cover repository mapping, foreign-currency conversion, and unavailable-service fallback