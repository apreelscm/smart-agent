# SDLC-36: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Task introduces multi-currency amount presentation and entry across the Smart Agent frontend for PLN, EUR, and USD, while preserving PLN as the only persisted system currency.

The implementation should:
- let users switch the visible currency per view,
- let agents enter offer amounts in EUR/USD in the offer wizard,
- fetch real exchange rates from the documented NBP API integration,
- clearly communicate the applied rate and the “presentation-only” nature of foreign-currency values,
- degrade safely to PLN-only mode when rates are unavailable.

This is a frontend-focused change in an Angular 20 + PrimeNG application that currently assumes `PLN` everywhere, including the domain model (`src/app/core/models/common/money.model.ts`).

### User Stories
- US-001: As a sales agent, I want to switch visible amounts between PLN, EUR, and USD on offers, so that I can discuss pricing in the customer’s contract currency.
  - Given I am on the offers list
  - When I switch the currency from PLN to EUR or USD
  - Then all offer amounts on that screen are recalculated consistently and the applied rate is shown

- US-002: As a sales agent, I want to view policy amounts in EUR or USD, so that I can answer customer questions about installments and premiums in a foreign currency.
  - Given I am on the policies screen
  - When I select EUR or USD
  - Then annual and monthly premium values are shown in the selected currency using one consistent rate for the view

- US-003: As a sales agent, I want to enter offer amounts in EUR or USD in the variants step, so that I can work from a supplier or partner price list denominated in a foreign currency.
  - Given I am editing an offer in the variants step
  - When I choose EUR or USD as the input currency and type an amount
  - Then I see the current PLN equivalent immediately and the saved draft remains stored in PLN

- US-004: As a manager, I want reports to be presented in a selected currency, so that I can share portfolio metrics in a single consistent currency.
  - Given I am on the reports page
  - When I switch the report currency
  - Then KPI tiles, trend labels, and business-line totals are converted uniformly with no mixed-currency presentation

- US-005: As a user, I want the application to fall back safely to PLN when the rate service is unavailable, so that I can still work without broken financial views.
  - Given exchange rates cannot be loaded
  - When I open offers, policies, reports, or the offer wizard
  - Then PLN remains available, EUR/USD display is disabled, and the UI shows a clear explanation

### Functional Requirements
- FR-001: Introduce application-wide support for currency codes `PLN | EUR | USD` for display and input context.
- FR-002: Keep PLN as the default currency for every view, route entry, and new session.
- FR-003: Keep persisted monetary values in PLN only; foreign-currency values are derived from current rates.
- FR-004: Reuse the documented NBP integration as the runtime default exchange-rate source: Confluence page [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-005: Implement a frontend exchange-rate service/repository that fetches EUR and USD rates against PLN from the NBP API and exposes rate metadata including effective date.
- FR-006: Support one consistent conversion rate set per loaded view state; all amounts inside a given view must use the same loaded rates.
- FR-007: Add a reusable currency switcher UI for offers, policies, and reports with options `PLN | EUR | USD`.
- FR-008: In offers list (`src/app/features/offers/pages/offers-home-page.component.*`), convert visible premium values and summary tile premium metric according to selected currency.
- FR-009: In policies page (`src/app/features/policies/pages/policies-home-page.component.*`), convert annual and monthly premium values and premium-based summary tile values according to selected currency.
- FR-010: In reports page (`src/app/features/reports/pages/reports-page.component.*`), convert KPI amount metrics, trend point labels, and business-line totals according to selected currency.
- FR-011: Reports must remain visually consistent in one selected currency for the whole page; no mixed-currency totals in the same report.
- FR-012: Extend money modeling so code can represent and format converted values in EUR and USD without breaking existing PLN flows.
- FR-013: Add centralized formatting rules:
  - PLN: current behavior preserved, no grosz display (`1.0-0`)
  - EUR/USD: two decimal places
  - locale remains `pl-PL`
- FR-014: Add centralized conversion helpers that transform PLN amounts into EUR/USD for display.
- FR-015: In the offer wizard variants step (`src/app/features/offer-wizard/pages/variants-step-page.component.*`), allow the user to choose input currency for editable monetary inputs related to premium/discount entry.
- FR-016: When entering a value in EUR/USD, show a helper text with approximate PLN equivalent and applied rate date.
- FR-017: On blur/save, foreign-currency input must be converted back to PLN and stored via existing wizard state mechanisms.
- FR-018: Existing readonly and draft-edit behavior in `OfferWizardStateService` must remain intact.
- FR-019: When rates are unavailable, the application must:
  - keep PLN formatting and values working,
  - disable EUR/USD switcher options,
  - disable EUR/USD input mode in the offer wizard,
  - show a user-facing message explaining fallback to PLN.
- FR-020: No production runtime path may depend on mock JSON files for exchange rates; mocks may only be used in tests.

### Edge Cases
- EC-001: If the NBP API responds for only one currency, the UI should treat foreign-currency mode as unavailable unless both EUR and USD rates required by the selector are present.
- EC-002: If the exchange-rate fetch fails on initial page load, the view should render in PLN with disabled EUR/USD options rather than blocking the page.
- EC-003: If a user has EUR selected and rates later fail to refresh, the current view should revert to PLN with a visible informational message.
- EC-004: Reopening an offer entered previously in USD may display a different USD amount later because only PLN is persisted; the UI must explain that foreign-currency values are approximate and based on current rates.
- EC-005: Discount validation in `VariantsStepPageComponent` must still enforce business caps after converting foreign input back into PLN.
- EC-006: Empty, invalid, negative, or partially typed foreign-currency inputs must not corrupt the PLN value in wizard state.
- EC-007: Summary/count tiles that are not monetary must remain unchanged when currency changes.
- EC-008: Currency switches are contextual to a page; navigating to another route should start again in PLN unless a deliberate shared-session mechanism is implemented.
- EC-009: If a page contains hardcoded `'zł'` suffix strings, those must be refactored to use centralized formatting to avoid mixed labels.
- EC-010: Existing crop and motor flows must both continue to work even if only motor variants get editable foreign-currency entry in MVP.

### Success Criteria
- [ ] Offers, policies, and reports pages each show a currency switcher with default `PLN`.
- [ ] Switching to EUR or USD recalculates all monetary values on the active page using one consistent rate set.
- [ ] PLN presentation remains visually identical to current behavior.
- [ ] Offer wizard variants step supports EUR/USD input for monetary entry and stores resulting values in PLN.
- [ ] Helper text near foreign-currency input shows approximate PLN equivalent, rate, and effective date.
- [ ] When rates are unavailable, pages remain usable in PLN and EUR/USD controls are disabled with a clear message.
- [ ] No runtime exchange-rate logic uses local mock JSON as a fallback.
- [ ] Unit tests cover conversion, formatting, failure fallback, and wizard input normalization.
- [ ] Existing offers, policies, and reporting flows continue to build and render without regressions.

### Open Questions
- [NEEDS CLARIFICATION: Which exact editable fields in the MVP variants step must support foreign-currency entry beyond the existing discount input, given current premium totals are largely derived from selected variants rather than directly typed by the user?]
- [NEEDS CLARIFICATION: Should the contextual currency choice reset to PLN on every route change exactly as suggested in the Jira comment, or should it persist within a single browser session across offers/policies/reports?]

---

## Implementation Plan

### Technical Approach
The codebase is a standalone Angular 20 app using signals/computed state and PrimeNG components. Monetary display is currently fragmented and mostly hardcoded in templates with Angular `currency` pipe calls and direct `'zł'` string concatenation, while the domain model only allows `currency: 'PLN'` in `src/app/core/models/common/money.model.ts`.

The implementation should introduce a small shared currency layer rather than sprinkling conversion logic into each page:
1. **Shared currency domain/types**
   - expand the money model from PLN-only to `PLN | EUR | USD`,
   - add exchange-rate and currency-selection types in `src/app/core/models/common/` or a new shared currency folder.

2. **Runtime default integration**
   - reuse the documented NBP API from Confluence as the authoritative runtime source: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
   - implement a repository/service around Angular `HttpClient` that calls the documented NBP endpoint(s) and maps rates for EUR and USD.
   - production code must fetch real rates from NBP; `public/mock/*.json` must not be used as runtime fallback.

3. **Centralized formatting and conversion**
   - add a shared utility/service for:
     - converting persisted PLN numeric values into display currency,
     - converting entered EUR/USD values back into PLN,
     - formatting according to current business rules.
   - this will replace repeated direct `currency` pipe assumptions in offers, policies, and reports.

4. **Reusable page-level currency state**
   - for each page component, add:
     - `selectedCurrency` signal defaulting to `PLN`,
     - `exchangeRates` signal from the shared repository,
     - computed “currency availability / fallback message” state.
   - start with page-local state to match the Jira’s “contextual choice” guidance and minimize scope.

5. **Reusable UI**
   - create a shared currency switcher component under `src/app/shared/ui/` following existing reusable UI patterns like `page-header`, `section-card`, and `stat-tile`.
   - use PrimeNG controls already present in the app (`Select`, `Tag`, `ButtonDirective`) instead of inventing a new UI stack.

6. **Page-specific integration**
   - **Offers**: refactor `summaryTiles()` and `getPrimaryPremium()` usage so displayed strings are based on converted amounts; show applied rate note near toolbar.
   - **Policies**: convert premium box values and paid-premium tile; keep counts unchanged.
   - **Reports**: refactor KPI generation, `monthlyTrend`, and `businessLines` output so values stay numeric until final formatting in the selected currency.
   - **Offer wizard variants**: integrate foreign-currency entry where user-editable monetary input already exists (`discountInput`) and, if confirmed, extend to additional editable amount fields. Conversion back to PLN should happen through `OfferWizardStateService.setDiscountAmount(...)` or parallel setters, preserving current state flow.

7. **Failure mode**
   - if rates cannot be loaded, all pages remain operational in PLN:
     - selected currency is forced back to PLN,
     - EUR/USD options are disabled,
     - the wizard’s foreign input controls are disabled,
     - an informational banner/help text is shown.
   - credentials are not required for the public NBP API; if deployment needs endpoint configurability, expose base URL as environment/app configuration.

Confluence/domain discovery results:
- Reused runtime integration: NBP API documentation in Services space: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)
- No relevant additional domain convention/glossary page was found in the configured `SDLC` space.

### Task Breakdown

#### Phase 1: Establish currency foundations
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Expand money model | `src/app/core/models/common/money.model.ts`, `src/app/core/models/index.ts` | Change `Money.currency` from PLN-only to a `SupportedCurrency` union and export the new type for shared use. |
| 1.2 | Add exchange-rate models | `src/app/core/models/common/exchange-rate.model.ts`, `src/app/core/models/index.ts` | Define types for supported currencies, exchange-rate map, effective date, loading state, and fallback status. |
| 1.3 | Add conversion utilities | `src/app/shared/utils/currency.utils.ts` or `src/app/core/utils/currency.utils.ts` | Implement pure helpers for PLN→EUR/USD conversion, EUR/USD→PLN normalization, precision rules, and display metadata. |
| 1.4 | Add centralized formatter | `src/app/shared/pipes/amount-display.pipe.ts` or `src/app/shared/utils/amount-format.utils.ts` | Encapsulate formatting rules currently duplicated via `currency` pipe and string concatenation so PLN formatting remains unchanged and EUR/USD gains two decimals. |

#### Phase 2: Integrate authoritative NBP exchange-rate source
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add exchange-rate repository | `src/app/core/repositories/exchange-rates.repository.ts` | Implement `HttpClient` calls to the documented NBP API endpoint(s), map EUR and USD rates against PLN, and expose an observable/signal-friendly API. |
| 2.2 | Add rate facade/service for UI state | `src/app/core/services/currency-rates.service.ts` | Provide page-friendly state: latest rates, loading flag, error flag, effective date, and “foreign currencies available” decision. |
| 2.3 | Register/consume service with app patterns | `src/app/app.config.ts` | Keep provider setup aligned with existing standalone app config if any explicit provider or interceptor wiring is needed. |
| 2.4 | Add unit tests for repository mapping | `src/app/core/repositories/exchange-rates.repository.spec.ts`, `src/app/core/services/currency-rates.service.spec.ts` | Use `HttpTestingController` fixtures only in tests to verify endpoint mapping, fallback behavior, and disabled foreign-currency mode. |

#### Phase 3: Create reusable currency switcher and status messaging
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Build shared switcher component | `src/app/shared/ui/currency-switcher/currency-switcher.component.ts`, `.html`, `.scss` | Create reusable UI with options `PLN | EUR | USD`, disabled foreign options when rates unavailable, and optional rate/effective-date note. |
| 3.2 | Add shared info message styling | `src/styles.scss` or component SCSS files | Introduce a consistent visual treatment for fallback/rate info messages used across pages and the wizard. |

#### Phase 4: Apply currency support to offers list
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add page-level currency state | `src/app/features/offers/pages/offers-home-page.component.ts` | Add `selectedCurrency`, inject rate service, compute active rate note, and force PLN fallback on service failure. |
| 4.2 | Refactor offer premium display | `src/app/features/offers/pages/offers-home-page.component.ts`, `.html` | Replace direct PLN `currency` pipe usage with converted/ formatted amount rendering for premium box and transition dialog premium. |
| 4.3 | Refactor summary tiles | `src/app/features/offers/pages/offers-home-page.component.ts` | Convert only monetary tile values; keep count tiles unchanged. Remove hardcoded `'zł'` suffixes in generated strings. |
| 4.4 | Add switcher to toolbar | `src/app/features/offers/pages/offers-home-page.component.html`, `.scss` | Place the shared currency switcher near existing filters/actions using current toolbar layout patterns. |

#### Phase 5: Apply currency support to policies page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add page-level currency state | `src/app/features/policies/pages/policies-home-page.component.ts` | Mirror offers-page pattern with local default PLN state and rate availability checks. |
| 5.2 | Convert premium outputs | `src/app/features/policies/pages/policies-home-page.component.html`, `.ts` | Convert annual/monthly premium rendering and the “Wszystkie polisy” premium tile using shared utilities. |
| 5.3 | Add switcher and fallback info | `src/app/features/policies/pages/policies-home-page.component.html`, `.scss` | Display switcher plus explanatory note when EUR/USD are unavailable. |

#### Phase 6: Apply currency support to reports page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 6.1 | Add page-level currency state | `src/app/features/reports/pages/reports-page.component.ts` | Add selected currency, injected rate state, and derived formatters for the whole report page. |
| 6.2 | Refactor KPI generation | `src/app/features/reports/pages/reports-page.component.ts`, `.html` | Keep KPI numeric values raw until render time, then format in selected currency where applicable. |
| 6.3 | Refactor trend and business-line rendering | `src/app/features/reports/pages/reports-page.component.ts`, `.html` | Convert displayed trend labels and business-line totals while preserving chart geometry calculations based on raw PLN or normalized display values consistently. |
| 6.4 | Add switcher to report header area | `src/app/features/reports/pages/reports-page.component.html`, `.scss` | Add the reusable switcher near date-range controls with rate note visible for foreign-currency mode. |

#### Phase 7: Add foreign-currency input to offer wizard variants step
| # | Task | Files | Description |
|---|------|-------|-------------|
| 7.1 | Add wizard currency input state | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | Introduce selected input currency signal defaulting to PLN, bind it to rates availability, and keep readonly mode respected. |
| 7.2 | Convert editable amount handling | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | Parse EUR/USD input, convert to PLN before validation/storage, and preserve current business caps in PLN. |
| 7.3 | Update variants-step template | `src/app/features/offer-wizard/pages/variants-step-page.component.html`, `.scss` | Add input-currency selector, helper text showing `≈ PLN`, applied rate, and warning that foreign amount display is approximate. |
| 7.4 | Preserve state-service contract | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | Keep persisted values and wizard calculations in PLN; only adapt any setter entry points necessary for clean conversion boundaries. |
| 7.5 | Extend to additional editable amount fields if clarified | `src/app/features/offer-wizard/pages/variants-step-page.component.*`, related wizard components | Apply the same pattern to any other confirmed money-entry fields in scope after clarification. |

#### Phase 8: Validation and regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 8.1 | Add unit tests for conversion/formatting | `src/app/shared/utils/currency.utils.spec.ts`, formatter tests | Cover PLN formatting parity, EUR/USD precision, reverse conversion, and rounding rules. |
| 8.2 | Add component tests for fallback mode | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `src/app/features/policies/pages/policies-home-page.component.spec.ts`, `src/app/features/reports/pages/reports-page.component.spec.ts`, `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts` | Verify default PLN mode, disabled foreign options on rate failure, and visible fallback messaging. |
| 8.3 | Add integration tests for NBP-backed fetch behavior | repository/service spec files | Verify runtime default logic uses API responses and never production mock files. |
| 8.4 | Add/extend e2e smoke coverage | `tests/e2e/smoke.spec.ts` or new e2e spec | Validate currency switch presence, PLN default, and one happy-path conversion on a key page. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Expand money currency support beyond PLN. |
| `src/app/core/models/common/exchange-rate.model.ts` | CREATE | Add supported currency and exchange-rate domain types. |
| `src/app/core/models/index.ts` | MODIFY | Re-export new currency-related models. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Integrate with the documented NBP API as runtime default. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Test HTTP mapping and failure handling for NBP responses. |
| `src/app/core/services/currency-rates.service.ts` | CREATE | Expose view-friendly rate state and fallback logic. |
| `src/app/core/services/currency-rates.service.spec.ts` | CREATE | Test state transitions and PLN fallback behavior. |
| `src/app/shared/utils/currency.utils.ts` | CREATE | Add conversion and normalization helpers. |
| `src/app/shared/utils/currency.utils.spec.ts` | CREATE | Test currency conversion and rounding rules. |
| `src/app/shared/pipes/amount-display.pipe.ts` or `src/app/shared/utils/amount-format.utils.ts` | CREATE | Centralize amount formatting rules. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | Add reusable currency selector component. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Currency selector template. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Currency selector styling. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency state, conversion logic, and fallback messaging. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Render switcher, rate info, and converted monetary values. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Fit switcher and info message into toolbar/layout. |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Add page-level currency handling and converted premium outputs. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Render switcher, fallback note, and converted policy premiums. |
| `src/app/features/policies/pages/policies-home-page.component.scss` | MODIFY | Style switcher and info note on policies page. |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Add report-wide currency state and refactor monetary data rendering. |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Add switcher and replace PLN-only renderings. |
| `src/app/features/reports/pages/reports-page.component.scss` | MODIFY | Layout switcher and rate note in reports UI. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Add foreign-currency input handling and PLN persistence conversion. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Add input currency selector and helper conversion text. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.scss` | MODIFY | Style foreign-currency controls and helper messaging. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Keep state contract PLN-based while supporting converted inputs. |
| `src/app/app.config.ts` | MODIFY | Add any required provider wiring for new currency services if needed. |
| `src/styles.scss` | MODIFY | Add shared status/info styles for rate and fallback messaging. |
| `tests/e2e/smoke.spec.ts` or new e2e spec | MODIFY/CREATE | Add end-to-end validation for currency switching and PLN fallback. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Offers page defaults to PLN and can switch to EUR/USD when NBP rates load
5. [ ] Policies page defaults to PLN and converts annual/monthly premiums consistently
6. [ ] Reports page converts KPI amounts, trend labels, and line totals without mixed currencies
7. [ ] PLN formatting remains unchanged compared to current UI
8. [ ] Variants step accepts EUR/USD input for in-scope editable amount fields and stores PLN in state
9. [ ] Foreign-currency helper text shows approximate PLN equivalent, rate, and effective date
10. [ ] When the exchange-rate API fails, EUR/USD options are disabled and PLN-only fallback messaging is shown
11. [ ] No production code path uses local mock JSON files for exchange rates