# SDLC-134: Waluty

## Specification

### Overview
Dodanie wyboru waluty na ekranie `kalkulator/zakres` (`step-15-coverage`), tak aby wszystkie kwoty widoczne na tym ekranie mogły być prezentowane w `PLN`, `USD` albo `EUR`. Domyślną walutą pozostaje `PLN`. Dla `USD` i `EUR` ekran ma pobierać kurs NBP, pokazywać metadane kursu (wartość, tabela, data), przeliczać wszystkie widoczne kwoty na ekranie bez jego opuszczania oraz blokować wybór waluty, gdy nie uda się pobrać kursu bieżącego ani z dnia poprzedniego.

### User Stories
- US-001: As a user of the “Wybierz zakres” step, I want to switch the display currency between PLN, USD, and EUR, so that I can review coverage prices in my preferred currency.
  - Given the user opens `kalkulator/zakres` / When the screen renders / Then `PLN` is selected by default and all amounts are shown in PLN.

- US-002: As a user, I want to see the exchange-rate value, table identifier, and effective date for USD and EUR, so that I understand how visible prices were converted.
  - Given the user selects `USD` or `EUR` / When the rate is fetched successfully from NBP / Then the screen shows the applied rate, table number, and rate date next to the currency selector.

- US-003: As a business stakeholder, I want the app to fall back to the previous day’s NBP rate, so that the currency switch still works when the current quote is unavailable.
  - Given the current NBP quote cannot be fetched / When the user selects `USD` or `EUR` / Then the app retries with the previous day’s quote and uses that quote if available.

- US-004: As a user, I want unavailable currencies to be blocked with a clear message, so that I am not shown stale or invalid converted prices.
  - Given both current and previous-day rate fetches fail for `USD` or `EUR` / When the user attempts to select that currency / Then the currency remains unselected, the option becomes blocked, and an error message is shown.

### Functional Requirements
- FR-001: Add a currency selector to `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` with options `PLN`, `USD`, and `EUR`.
- FR-002: Default selected currency on every entry to the `kalkulator/zakres` route is `PLN`.
- FR-003: All monetary values visible on the “Wybierz zakres” screen must be displayed in the selected currency, including:
  - coverage prices in `mainCoverages`
  - addon prices in `addons`
  - total insurance price in the summary section
- FR-004: Base prices remain stored and calculated in PLN; currency conversion is a display-layer concern for this screen.
- FR-005: For `USD` and `EUR`, the app must call the documented NBP API and use the real runtime endpoint as default.
- FR-006: For `USD` and `EUR`, the app must first request the current quote and, on failure, retry using the previous day’s date.
- FR-007: The applied exchange-rate metadata must be displayed for `USD` and `EUR`: rate value, table identifier, and effective date.
- FR-008: For `PLN`, the screen must not show exchange-rate metadata.
- FR-009: Converted `USD` and `EUR` amounts must be rounded to 2 decimal places before display.
- FR-010: If both fetch attempts fail for `USD` or `EUR`, that currency option must be disabled for the current screen session and an inline user-facing message must be shown.
- FR-011: Switching currency must refresh displayed amounts immediately on the current screen without route navigation or full-page reload.
- FR-012: Existing coverage toggling, promo code input, total calculation, and navigation to detail routes (`/kalkulator/casco`, `/kalkulator/assistance`, `/kalkulator/nnw`, `/kalkulator/szyby`) must continue to work unchanged.

### Edge Cases
- EC-001: If the user switches from `USD`/`EUR` back to `PLN`, all exchange-rate metadata and error messages must be cleared from the UI.
- EC-002: If the NBP current-rate request fails but the previous-day request succeeds, the UI must show the previous-day date and table number, not today’s date.
- EC-003: If a currency fetch fails twice and becomes blocked, the previously selected valid currency must remain active.
- EC-004: If the user re-selects a currency whose rate was already fetched successfully during the same screen visit, the app should reuse the cached quote instead of issuing another HTTP request.
- EC-005: If the user changes currency rapidly, only the latest selection result should update the UI.
- EC-006: Date fallback must work across month/year boundaries when computing “previous day”.
- EC-007: Required coverage `OC` must remain always enabled and its displayed price must still react to currency changes.
- EC-008: Promo code input content must not be lost when currency changes.

### Success Criteria
- [ ] `PLN`, `USD`, and `EUR` selector is visible on `kalkulator/zakres`
- [ ] `PLN` is selected by default on screen entry
- [ ] All visible amounts on the step update immediately after a currency change
- [ ] `USD` and `EUR` amounts are displayed with 2 decimal places
- [ ] `USD` and `EUR` show exchange-rate value, table identifier, and effective date
- [ ] Previous-day fallback is used automatically when the current quote is unavailable
- [ ] Unavailable `USD`/`EUR` options are blocked and show a message after double failure
- [ ] Existing coverage selection flow and next-step navigation still work

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
Implement the feature directly in the existing standalone Angular step component `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts`, keeping pricing truth in PLN and applying conversion only in the presentation layer for this route.

The existing repo already follows these patterns:
- standalone Angular components with local state and `FormsModule`
- service-based shared state via `PolicyDraftService`
- HTTP access via root-provided services, e.g. `src/app/core/services/vehicle-data.service.ts`
- simple, custom HTML/CSS rather than heavy PrimeNG usage in the wizard flow

#### Reused external service
Reuse the documented NBP integration from Confluence as the runtime default:

- Confluence page: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Planned runtime integration:
- Base URL: `https://api.nbp.pl/api`
- Current quote endpoint: `GET /exchangerates/rates/A/{CODE}/?format=json`
- Previous-day fallback endpoint: `GET /exchangerates/rates/A/{CODE}/{YYYY-MM-DD}/?format=json`
- Auth: none required
- Expected response shape:
  ```ts
  {
    table: 'A',
    currency: string,
    code: 'USD' | 'EUR',
    rates: [
      {
        no: string,
        effectiveDate: string,
        mid: number
      }
    ]
  }
  ```
- Conversion rule: visible amount in foreign currency = `amountPln / mid`
- Runtime mocks are not allowed; mock responses may be used only in unit/e2e tests

#### Domain knowledge discovery
No relevant domain ADRs/conventions were found in the configured `SDLC` Confluence space, so the implementation will follow existing repository conventions.

#### Design decisions
- Keep `PolicyDraftService` data in PLN to avoid unintended scope expansion into later steps such as `step-21-review`.
- Add a dedicated exchange-rate service in `src/app/core/services` instead of embedding HTTP logic into the component.
- Cache successfully fetched quotes per currency within the screen session to avoid repeated NBP calls.
- Use explicit formatting helpers in the component for consistency with the current codebase, rather than introducing a global currency pipe.
- Do not use `public/data/*.json` as a runtime fallback; those files remain unrelated to exchange-rate retrieval.

### Task Breakdown

#### Phase 1: Add NBP exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Define exchange-rate types | `src/app/core/models/exchange-rate.model.ts` | Create small typed models for supported currency codes, NBP API response, and normalized quote data used by the UI. |
| 1.2 | Implement exchange-rate service | `src/app/core/services/exchange-rate.service.ts` | Add a root-provided `HttpClient` service that fetches current NBP quotes, falls back to previous-day quotes, normalizes the response, and exposes a method for `USD`/`EUR` retrieval. |
| 1.3 | Handle fallback and normalization | `src/app/core/services/exchange-rate.service.ts` | Map NBP response fields into a UI-ready object containing `code`, `mid`, `tableNo`, and `effectiveDate`; compute previous day safely and retry once on error. |
| 1.4 | Cover integration logic with tests | `src/app/core/services/exchange-rate.service.spec.ts` | Add unit tests with `HttpTestingController` for success, fallback-to-yesterday, and double-failure scenarios using test-only mocked HTTP fixtures. |

#### Phase 2: Update the “Wybierz zakres” screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component state for currency selection | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Add `selectedCurrency`, cached quotes, loading state, blocked currencies set, applied quote metadata, and inline error message state. |
| 2.2 | Keep prices in PLN and convert only for display | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Replace raw template price output with helper methods such as `getDisplayPrice(amountPln)` and `getDisplayTotal()`, while keeping `total` calculation in base PLN. |
| 2.3 | Add selector behavior | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Implement `selectCurrency(code)` that resets PLN view, fetches/caches USD/EUR quotes, applies stale-request protection, and blocks options after double failure. |
| 2.4 | Render selector and rate metadata | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Add a selector UI above coverage cards, render quote info for `USD`/`EUR`, hide it for `PLN`, and show a non-blocking loading state during fetch. |
| 2.5 | Replace hardcoded amount rendering | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Update each visible price badge and the summary total to use formatted display helpers instead of `{{ item.price }} zł` / `{{ total }} zł`. |
| 2.6 | Style new UI elements | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | Add styles for the currency selector, active/disabled states, rate-info block, loading text, and error banner while preserving the current visual style. |
| 2.7 | Preserve existing flow semantics | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Ensure `toggle`, `openDetail`, promo code binding, and `next()` continue to behave as today, with `totalPremium` still persisted in PLN. |

#### Phase 3: Verify user behavior end-to-end
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add Playwright coverage-step scenario | `tests/e2e/coverage-currency.spec.ts` | Create an e2e test that navigates directly to `/kalkulator/zakres`, intercepts NBP HTTP calls, verifies default PLN, successful USD/EUR conversion, metadata display, and fallback behavior. |
| 3.2 | Add failure-path e2e coverage | `tests/e2e/coverage-currency.spec.ts` | Verify that when both current and previous-day requests fail, the currency is blocked, the prior valid selection remains, and a user message is displayed. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/exchange-rate.model.ts` | CREATE | Typed contracts for supported currencies and normalized NBP quote data. |
| `src/app/core/services/exchange-rate.service.ts` | CREATE | Runtime NBP integration service with current-rate fetch and previous-day fallback. |
| `src/app/core/services/exchange-rate.service.spec.ts` | CREATE | Unit tests for NBP service success/fallback/error flows. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Add currency state, quote fetching, display formatting, caching, and blocked-currency behavior. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | Add currency selector, rate metadata, error message, and converted amount rendering. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | MODIFY | Add styles for selector, metadata, and error/disabled states. |
| `tests/e2e/coverage-currency.spec.ts` | CREATE | End-to-end verification of currency switching and NBP fallback behavior. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Unit tests pass with `npm run test`
3. [ ] E2E tests pass with `npm run e2e`
4. [ ] New tests cover:
   - default PLN rendering
   - USD conversion
   - EUR conversion
   - current-rate fallback to previous day
   - blocked currency on double failure
5. [ ] Manual check on `/kalkulator/zakres` confirms:
   - all visible prices change together
   - PLN hides rate info
   - USD/EUR show rate, table, and date
   - promo code text remains intact while switching currencies