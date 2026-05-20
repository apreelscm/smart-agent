# SDLC-131: Waluty

## Specification

### Overview
Dodanie wyboru waluty na ekranie `Wybierz zakres` (`src/app/features/wizard/steps/step-15-coverage`) tak, aby wszystkie kwoty widoczne na tym ekranie były prezentowane w `PLN`, `USD` albo `EUR`. Domyślną walutą pozostaje `PLN`. Dla `USD` i `EUR` aplikacja ma pobierać średni kurs NBP, najpierw bieżący, a przy niepowodzeniu kurs z dnia poprzedniego. Zmiana ma dotyczyć wyłącznie warstwy prezentacji na tym ekranie i nie może zmieniać logiki biznesowej ani danych zapisywanych do draftu.

### User Stories
- US-001: As a user, I want to switch the displayed premium currency on the coverage selection screen, so that I can compare values in my preferred currency.
  - Given I am on the coverage selection step / When I choose `USD` or `EUR` / Then all prices shown on that step are converted using the NBP average exchange rate and displayed with the selected currency.
- US-002: As a user, I want `PLN` to remain the default currency, so that the screen behaves as it does today until I explicitly change it.
  - Given I open the coverage selection step / When the screen renders / Then the currency control is visible and defaults to `PLN`.
- US-003: As a user, I want the app to fall back to the previous day’s NBP rate, so that conversion still works when the current rate is unavailable.
  - Given I selected `USD` or `EUR` / When the current NBP rate cannot be fetched / Then the app fetches yesterday’s rate and uses it for display.

### Functional Requirements
- FR-001: Add a currency selector to `step-15-coverage` with exactly three options: `PLN`, `USD`, `EUR`.
- FR-002: Default the selector to `PLN` whenever the coverage step is entered.
- FR-003: For `PLN`, do not call the exchange-rate API; use a multiplier/rate of `1.00`.
- FR-004: For `USD` and `EUR`, fetch the NBP average rate and use it as the runtime default integration.
- FR-005: If the current NBP rate fetch fails, retry once using the previous calendar day.
- FR-006: Convert every price rendered on `step-15-coverage`, including:
  - main coverage item prices,
  - add-on prices,
  - total insurance price summary.
- FR-007: Converted values must be rounded to 2 decimal places before display.
- FR-008: Displayed values must include the selected currency code.
- FR-009: Currency conversion must be presentation-only; underlying base prices and `coverages.totalPremium` persisted through `PolicyDraftService` remain in PLN.
- FR-010: The change must not affect later steps such as `step-21-review` or payment calculations, because the Jira scope is limited to the coverage-selection screen.

### Edge Cases
- EC-001: Selecting `PLN` after `USD`/`EUR` immediately restores original PLN values without any API call.
- EC-002: If the current NBP endpoint returns unavailable/not found, the app retries with yesterday’s date and uses that rate.
- EC-003: If both NBP requests fail, the UI should fail gracefully instead of showing broken/empty amounts; the safest fallback is to keep PLN presentation and show a non-blocking inline error.
- EC-004: Rapid currency changes must not allow a stale HTTP response to overwrite the latest selected currency.
- EC-005: Navigating away from the coverage step and returning resets the selector to `PLN`, because the currency choice is not part of the policy draft.

### Success Criteria
- [ ] The coverage step shows a visible currency selector with `PLN`, `USD`, and `EUR`.
- [ ] Opening the coverage step defaults the selector to `PLN`.
- [ ] Switching to `USD` converts all visible prices on the step using NBP data, rounds to 2 decimals, and labels values as `USD`.
- [ ] Switching to `EUR` converts all visible prices on the step using NBP data, rounds to 2 decimals, and labels values as `EUR`.
- [ ] Switching back to `PLN` restores PLN display without calling NBP.
- [ ] If the current NBP rate is unavailable, the app successfully uses the previous day’s rate.
- [ ] The saved draft premium remains PLN-based and unchanged by the presentation currency toggle.

### Open Questions
- [NEEDS CLARIFICATION: If both the current-day and previous-day NBP requests fail, should the UI automatically revert to PLN, or should it keep the selected foreign currency with an inline error state?]

---

## Implementation Plan

### Technical Approach
The implementation should stay local to the standalone coverage step component and follow existing repo patterns:

- `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` currently owns local UI state and renders base PLN prices from in-component arrays.
- `src/app/core/services/vehicle-data.service.ts` shows the current service pattern: `inject(HttpClient)`, RxJS pipelines, and `shareReplay(1)` for reuse/caching.
- `src/app/app.config.ts` already provides `HttpClient`, so no app-level provider changes are required.

#### Reused external service
Reuse the documented NBP integration from Confluence as the runtime default:
- [API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Planned runtime integration:
- Current rate:
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json`
- Fallback rate for previous day:
  - `GET https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/{YYYY-MM-DD}/?format=json`
- Supported `CODE`: `USD`, `EUR`
- Auth: none
- Expected response shape:
  - `{ table, currency, code, rates: [{ no, effectiveDate, mid }] }`
- Conversion rule:
  - Base prices in this repo are PLN values, and NBP `mid` is PLN per 1 unit of foreign currency, so display conversion should be `amountInSelectedCurrency = amountInPln / mid`
- Mocks are test-only via Angular HTTP testing utilities; no runtime JSON fallback files should be introduced.

#### Confluence/domain findings
- No relevant domain/convention pages were found in the configured `SDLC` space for this feature, so the plan follows existing repository conventions only.

#### Code-level strategy
1. Create a dedicated exchange-rate service in `src/app/core/services`:
   - expose a method for `PLN | USD | EUR`,
   - return `1.00` immediately for `PLN`,
   - fetch current NBP rate for `USD`/`EUR`,
   - retry with yesterday’s date on failure,
   - optionally cache per currency/date to avoid duplicate calls during one session.

2. Keep all source prices in PLN inside `Step15CoverageComponent`:
   - keep `mainCoverages.price`, `addons.price`, and `total` as base PLN values,
   - add presentation helpers to convert and format values for the selected currency,
   - continue saving `totalPremium: this.total` in PLN inside `next()`.

3. Add local UI state to the component:
   - `selectedCurrency: 'PLN' | 'USD' | 'EUR' = 'PLN'`
   - `currentRate = 1`
   - `rateEffectiveDate`
   - `isRateLoading`
   - `rateError`
   - request sequencing/guard logic so stale responses do not override the latest selection.

4. Update the template:
   - add a labeled currency control near the top of the step or above the price list,
   - switch all displayed price bindings from raw `{{ item.price }} zł` to formatted helper output,
   - optionally show a small helper text for active rate date when `USD`/`EUR` is selected.

5. Limit scope:
   - do not add currency to `PolicyDraft` model,
   - do not modify `PolicyDraftService`,
   - do not alter downstream steps such as `step-21-review`,
   - do not change premium calculation logic beyond on-screen display formatting.

### Task Breakdown

#### Phase 1: Add exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create NBP exchange-rate service | `src/app/core/services/exchange-rate.service.ts` | Add a root-provided service using `HttpClient` and RxJS, following `vehicle-data.service.ts` patterns. |
| 1.2 | Implement supported currency typing | `src/app/core/services/exchange-rate.service.ts` | Define a narrow union for `PLN`, `USD`, `EUR` and a typed response contract for NBP payloads. |
| 1.3 | Implement current-rate fetch | `src/app/core/services/exchange-rate.service.ts` | Call `https://api.nbp.pl/api/exchangerates/rates/A/{CODE}/?format=json` and parse `rates[0].mid` and `rates[0].effectiveDate`. |
| 1.4 | Implement fallback-to-yesterday logic | `src/app/core/services/exchange-rate.service.ts` | On failure for `USD`/`EUR`, retry once with yesterday’s ISO date endpoint. |
| 1.5 | Add lightweight caching | `src/app/core/services/exchange-rate.service.ts` | Cache results per request key to prevent repeated identical fetches during one app session. |

#### Phase 2: Update coverage-step UI and presentation logic
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add component currency state | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Add selected currency, active rate, loading, error, and rate-date state to the component. |
| 2.2 | Wire selector change handling | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | On currency change, bypass API for `PLN`; for `USD`/`EUR`, call the new service and guard against stale async responses. |
| 2.3 | Keep business totals in PLN | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Preserve the existing `total` getter as PLN base data and ensure `next()` persists PLN `totalPremium` only. |
| 2.4 | Add conversion helpers | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Introduce helper methods for convert/round/format logic so the template renders currency-aware values consistently. |
| 2.5 | Add currency selector to template | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Render a labeled selector with `PLN`, `USD`, `EUR`, defaulting to `PLN`. |
| 2.6 | Replace raw price rendering | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Update all item and total price bindings to use formatted converted values rather than hard-coded `zł`. |
| 2.7 | Add loading/error/rate date messaging | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Show non-blocking UI feedback while rates load or if both requests fail. |
| 2.8 | Style the new control | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | Add selector/help-text styling aligned with the existing form tokens in `src/styles.scss`. |

#### Phase 3: Add automated tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Test NBP service happy path | `src/app/core/services/exchange-rate.service.spec.ts` | Verify current-rate endpoint parsing for `USD`/`EUR`. |
| 3.2 | Test NBP fallback path | `src/app/core/services/exchange-rate.service.spec.ts` | Verify failed current request triggers yesterday retry and returns fallback rate/date. |
| 3.3 | Test PLN bypass | `src/app/core/services/exchange-rate.service.spec.ts` | Verify `PLN` returns rate `1.00` without any HTTP request. |
| 3.4 | Test component rendering in PLN | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Verify selector default is `PLN` and prices render as PLN on initial load. |
| 3.5 | Test component conversion behavior | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Verify switching to `USD`/`EUR` updates all visible prices and rounds to 2 decimals. |
| 3.6 | Test persistence boundary | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Verify clicking next still patches `totalPremium` in PLN, not converted currency. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/services/exchange-rate.service.ts` | CREATE | New service for NBP exchange-rate retrieval, fallback handling, and typed parsing. |
| `src/app/core/services/exchange-rate.service.spec.ts` | CREATE | Unit tests for current-rate fetch, fallback-to-yesterday behavior, and PLN bypass. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Add selector state, rate-loading logic, conversion helpers, and preserve PLN persistence. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | Add currency selector and replace raw PLN labels with converted/formatted outputs. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | MODIFY | Style the selector, helper text, and any loading/error state for the coverage step. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | CREATE | Component tests covering default currency, conversion display, rounding, and PLN-only persistence. |

### Verification Steps
1. [ ] Build succeeds with `npm run build`
2. [ ] Tests pass with `npm run test -- --watch=false` (or equivalent CI `ng test` invocation)
3. [ ] New tests cover: selector defaulting to PLN, USD/EUR conversion, previous-day fallback, 2-decimal rounding, and PLN-only draft persistence