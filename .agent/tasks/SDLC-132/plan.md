# SDLC-132: Waluty

## Specification

### Overview
Dodanie na ekranie `kalkulator/zakres` („Wybierz zakres”) kontroli wyboru waluty prezentacji składek: `PLN`, `USD`, `EUR`, z domyślnym ustawieniem `PLN`. Ekran ma po wejściu pobrać kursy średnie NBP z tabeli A, używać ich wyłącznie do prezentacji kwot na tym widoku, automatycznie przeliczać wszystkie widoczne wartości pieniężne bez przeładowania strony oraz wyświetlać informację o zastosowanym kursie, numerze tabeli i dacie obowiązywania kursu. W przypadku niedostępności kursów dla bieżącego dnia oraz dnia poprzedniego ekran ma pozostać w `PLN` i pokazać komunikat techniczny.

### User Stories
- US-001: As a user, I want to switch premium presentation currency on the “Wybierz zakres” screen, so that I can compare insurance prices in PLN, USD, and EUR.
  - Given I am on `kalkulator/zakres` / When I select `USD` or `EUR` / Then all visible money amounts on that screen are recalculated from PLN using the fetched NBP rate and shown with 2 decimal places.
- US-002: As a user, I want to see the exchange-rate source details, so that I know which NBP table and date were used.
  - Given exchange rates were loaded / When I view the currency panel / Then I see the selected currency, applied rate vs PLN, NBP table number, and effective date.
- US-003: As a business owner, I want the app to fall back to the previous day’s NBP table, so that the feature still works when today’s table is unavailable.
  - Given the current-day NBP table cannot be fetched / When the screen initializes / Then the app retries with yesterday’s table before allowing foreign-currency presentation.

### Functional Requirements
- FR-001: The `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.*` screen shall render a currency selector with exactly three options: `PLN`, `USD`, `EUR`.
- FR-002: The selector shall default to `PLN` every time the `kalkulator/zakres` route is opened; the selection shall not be persisted in `PolicyDraftService`.
- FR-003: On screen initialization, the app shall fetch NBP table A rates using the documented runtime integration from Confluence: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut), with runtime default requests:
  - `GET https://api.nbp.pl/api/exchangerates/tables/A/today/?format=json`
  - fallback `GET https://api.nbp.pl/api/exchangerates/tables/A/{yyyy-MM-dd}/?format=json` for the previous day
  - no auth
  - expected response shape: `[{ table, no, effectiveDate, rates: [{ code, currency, mid }] }]`
- FR-004: The screen shall extract `USD` and `EUR` `mid` rates from the NBP response and keep them in memory for the lifetime of the component instance; changing selector value shall not trigger additional API calls.
- FR-005: All money values visible on the “Wybierz zakres” screen shall be converted from PLN presentation values, including:
  - main coverage prices,
  - add-on prices,
  - total price summary.
- FR-006: For `PLN`, the screen shall display original values without conversion and shall show rate `1,0000`.
- FR-007: Converted amounts shall be rounded and displayed with exactly 2 decimal places.
- FR-008: The screen shall show exchange metadata: selected currency, applied rate vs PLN, NBP table number, and effective date.
- FR-009: If today’s fetch fails or returns no usable table/rates, the app shall retry once with yesterday’s date.
- FR-010: If both fetch attempts fail, or if the response does not contain both required rates (`USD`, `EUR`), the screen shall remain in `PLN`, foreign-currency choices shall not be usable, and a technical unavailability message shall be shown.
- FR-011: Changing currency shall affect presentation only; the persisted quote data in `PolicyDraftService` shall remain PLN-based.
- FR-012: Advancing from step 15 shall continue to save `coverages.totalPremium` in PLN, not in the currently displayed foreign currency.

### Edge Cases
- EC-001: NBP current-day endpoint returns 404/no table — retry with previous day and use previous day metadata on screen.
- EC-002: Both NBP calls fail — keep `PLN`, show technical message, disable or prevent switching to `USD`/`EUR`.
- EC-003: NBP response exists but is missing `USD` or `EUR` rate entries — treat as unusable data and fall back to `PLN` error state.
- EC-004: User switches from `USD`/`EUR` back to `PLN` — restore original PLN amounts exactly, not by reverse-conversion from already rounded values.
- EC-005: User clicks “Dalej” while viewing `USD`/`EUR` — the next steps still receive the original PLN `totalPremium`.
- EC-006: User leaves and re-enters `kalkulator/zakres` — selector resets to `PLN` and rates are fetched again for the new screen session.
- EC-007: Weekend/holiday where both today and yesterday are unavailable — feature remains in `PLN` with the technical message, per ticket scope.

### Success Criteria
- [ ] `kalkulator/zakres` shows a 3-value currency selector with default `PLN`.
- [ ] Selecting `USD` recalculates every visible money amount on the step-15 screen without full-page reload.
- [ ] Selecting `EUR` recalculates every visible money amount on the step-15 screen without full-page reload.
- [ ] The screen displays applied rate, NBP table number, and effective date for usable exchange data.
- [ ] If today’s NBP table is unavailable, yesterday’s table is used automatically.
- [ ] If both attempts fail, the user stays in `PLN` and sees a technical unavailability message.
- [ ] Converted values are shown with 2 decimal places.
- [ ] `PolicyDraftService` continues storing source premiums in PLN only.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
Implement the feature only in the step-15 coverage screen, following the current Angular standalone-component structure and state patterns already used in the repo:

- route-driven standalone screen in `src/app/features/wizard/steps/...` (`src/app/app.routes.ts`)
- app-wide persisted business state via `PolicyDraftService` signals (`src/app/core/services/policy-draft.service.ts`)
- HTTP integration via `HttpClient` service with cached observable patterns already used in `src/app/core/services/vehicle-data.service.ts`

#### Reused knowledge and integrations
- Reuse the documented NBP runtime integration from Confluence: [`v1 - API NBP – pobieranie kursów walut`](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut). The implementation should call the real NBP API as the production default:
  - `GET https://api.nbp.pl/api/exchangerates/tables/A/today/?format=json`
  - fallback to previous day with `GET https://api.nbp.pl/api/exchangerates/tables/A/{yyyy-MM-dd}/?format=json`
  - consume `table/no/effectiveDate/rates[].code/rates[].mid`
  - no mock JSON in runtime code path
- Domain context found in SDLC: [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the wizard/screen-based navigation flow. No more specific ADR or formatting convention for currency handling was found in SDLC, so the plan follows existing application UI tokens and the Jira acceptance criteria.

#### Design decisions
1. **Keep currency selection local to step 15**
   - Do not extend `PolicyDraft` with `selectedCurrency`.
   - Keep `selectedCurrency` as component-local state in `Step15CoverageComponent`.
   - This directly satisfies “selection not remembered after reopening”.

2. **Keep source amounts in PLN**
   - Preserve current static PLN prices in `mainCoverages`, `addons`, and `totalPremium`.
   - Introduce explicit base-value fields such as `pricePln` and a separate `baseTotal` getter.
   - Render derived display values through a conversion helper.
   - Persist `totalPremium` using the PLN base total only.

3. **Encapsulate NBP integration in a dedicated service**
   - Add `CurrencyRatesService` under `src/app/core/services/`.
   - Mirror the lightweight `VehicleDataService` style: `HttpClient`, typed interfaces, RxJS mapping, single-fetch caching per request flow.
   - Service responsibility: fetch current-day table A, fallback to previous day, validate USD/EUR presence, normalize metadata for UI.

4. **Do not change other wizard steps**
   - Steps `step-16-casco`, `step-17-assistance`, `step-18-nnw`, `step-18b-szyby`, `step-21-review`, and `step-22-payment` stay PLN-based.
   - Ticket scope is the “Wybierz zakres” screen only.
   - This avoids unintended propagation of presentation-only currency state.

5. **Runtime behavior on failure**
   - Initial state: selector shows `PLN`.
   - Until rates load, foreign options are disabled or ignored.
   - On successful fetch: enable `USD`/`EUR`, show NBP metadata block.
   - On double failure: keep `PLN`, show technical message, leave UI usable in PLN.

6. **Testing strategy**
   - Unit-test the fallback logic with `HttpTestingController` or Angular HTTP testing utilities.
   - Unit-test step-15 conversion/persistence behavior.
   - E2E-test the screen with Playwright using request interception as test-only fixtures; mocks stay only in tests, never in runtime.

### Task Breakdown

#### Phase 1: Add exchange-rate integration foundation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Create typed NBP integration service | `src/app/core/services/currency-rates.service.ts` | Add a dedicated service using `HttpClient` to fetch NBP table A for today, fallback to previous day, extract `USD` and `EUR`, and expose normalized rate metadata. |
| 1.2 | Define service-local response/state types | `src/app/core/services/currency-rates.service.ts` | Add interfaces/types for NBP response table, rate entry, supported currency code, and normalized UI model. |
| 1.3 | Implement fallback and validation | `src/app/core/services/currency-rates.service.ts` | Use primary today endpoint, fallback to computed previous-day endpoint, reject incomplete responses, and surface a clean error state for the component. |

#### Phase 2: Update the “Wybierz zakres” screen
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Refactor step-15 pricing data to keep PLN as source of truth | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Replace current `price` usage with explicit PLN base values (`pricePln`) and separate getters for `baseTotal` and `displayTotal`. |
| 2.2 | Add local currency state and rate-loading lifecycle | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Introduce `selectedCurrency`, `rates`, `ratesError`, and initialization logic that fetches NBP data on screen entry without persisting selection. |
| 2.3 | Add conversion and formatting helpers | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Add methods for `convertAmount(pln)`, `formatAmount(amount, currency)`, `displayRate(currency)`, and route-safe guard logic when foreign rates are unavailable. |
| 2.4 | Prevent PLN source data corruption on next step | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | Ensure `next()` still writes `totalPremium: baseTotal` into `PolicyDraftService`, regardless of displayed currency. |
| 2.5 | Render currency selector and metadata section | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Add a selector UI above the coverage list plus a rate info panel showing selected currency, rate, NBP table number, and effective date. |
| 2.6 | Render converted prices on all visible money fields | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Replace hardcoded `{{ item.price }} zł` and `{{ total }} zł` with formatted values derived from conversion helpers. |
| 2.7 | Add technical error state for unavailable rates | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | Show a clear technical message when both NBP attempts fail and keep the selector in safe PLN mode. |
| 2.8 | Style selector, metadata panel, and error state | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | Extend the existing coverage-step styles using current design tokens from `src/styles.scss` and the card/toggle style already used on this screen. |

#### Phase 3: Verification and regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add unit tests for NBP fallback logic | `src/app/core/services/currency-rates.service.spec.ts` | Verify success for today, fallback to previous day after failure, and terminal error when both calls fail or required rates are missing. |
| 3.2 | Add unit tests for step-15 presentation behavior | `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | Verify default PLN state, conversion of all visible amounts, 2-decimal rendering, disabled foreign selection on failure, and PLN persistence in `next()`. |
| 3.3 | Add E2E coverage for currency switching | `tests/e2e/coverage-currency.spec.ts` | Navigate directly to `/kalkulator/zakres`, intercept NBP API responses, assert selector options, converted amounts, metadata rendering, fallback behavior, and PLN-only error behavior. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/services/currency-rates.service.ts` | CREATE | Real NBP integration service with today/previous-day fallback and normalized exchange-rate metadata. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.ts` | MODIFY | Add local currency state, NBP fetch lifecycle, conversion helpers, and PLN-safe persistence logic. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.html` | MODIFY | Add currency selector, rate metadata panel, technical error message, and converted amount rendering. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.scss` | MODIFY | Style the new selector, info panel, and error state to match existing coverage screen patterns. |
| `src/app/core/services/currency-rates.service.spec.ts` | CREATE | Unit tests for NBP endpoint handling, fallback behavior, and invalid-response handling. |
| `src/app/features/wizard/steps/step-15-coverage/step-15-coverage.component.spec.ts` | CREATE | Unit tests for UI state, conversion behavior, and PLN-only persistence. |
| `tests/e2e/coverage-currency.spec.ts` | CREATE | Playwright scenario for runtime currency switching and failure fallback. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements