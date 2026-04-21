# SDLC-37: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Task wprowadza obsługę wielowalutowej prezentacji i wprowadzania kwot w aplikacji Smart Agent dla obszarów już obecnych w repozytorium: lista ofert, kreator oferty, lista polis oraz raporty. System ma nadal utrwalać dane wyłącznie w PLN, natomiast użytkownik ma móc:
- przełączać widok kwot między PLN, EUR i USD,
- wprowadzać kwoty w EUR/USD w kreatorze oferty,
- widzieć kurs i orientacyjną równowartość w PLN,
- otrzymać bezpieczną degradację do PLN przy braku danych kursowych.

Zmiana ma charakter prezentacyjno-wejściowy, bez przebudowy modelu domenowego utrwalania składek.

### User Stories
- US-001: As an agent, I want to switch offer amounts between PLN, EUR, and USD, so that I can discuss pricing with clients in their contract currency.
  - Given an offers view with premium amounts in PLN / When I select EUR or USD / Then all amounts on that view are recalculated consistently and the applied exchange rate is shown.

- US-002: As an agent, I want to enter premium amounts in EUR or USD in the offer wizard, so that I can work from partner price lists without manual conversion.
  - Given the variants step in the offer wizard / When I select EUR or USD for an input and enter a value / Then I see the entered foreign-currency amount and an immediate helper value in PLN based on the current rate.

- US-003: As an agent, I want policies to display in a selected currency, so that I can answer client questions about installments in EUR/USD without changing stored policy data.
  - Given the policies list / When I switch the view currency / Then annual and monthly premium values update consistently and stored data remains unchanged.

- US-004: As a sales manager, I want reports to present KPIs and trends in one selected currency, so that I can share portfolio summaries with stakeholders using a single monetary context.
  - Given the reports page / When I switch the report currency / Then KPI values, trend values, and business-line totals render in the same selected currency without mixing currencies on one screen.

- US-005: As a user, I want the app to fall back safely to PLN when exchange-rate data is unavailable, so that I can continue working without misleading values.
  - Given the exchange-rate service is unavailable / When I open a supported view / Then PLN remains available, foreign currencies are disabled or reset, and a clear message explains the limitation.

### Functional Requirements
- FR-001: Add a shared currency model supporting `PLN | EUR | USD` while preserving PLN as the persisted currency for all existing monetary fields.
- FR-002: Add a runtime exchange-rate integration using the documented NBP API as the default source: Confluence page [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- FR-003: The runtime integration must call the real documented exchange-rate endpoint(s) for NBP average rates and treat returned rates as the source of truth for conversion.
- FR-004: Production code must not use `public/mock/*.json` as a runtime fallback for exchange rates.
- FR-005: Add a shared currency-selection mechanism with options `PLN | EUR | USD`, defaulting to PLN on every supported page load.
- FR-006: On the offers page, all displayed monetary amounts must be rendered in the selected presentation currency, while preserving current PLN formatting behavior.
- FR-007: On the policies page, annual and monthly premium values must be rendered in the selected presentation currency, with one currency applied consistently to the full page.
- FR-008: On the reports page, KPI values, trend values, and business-line totals must be converted and formatted in the selected presentation currency.
- FR-009: In the offer wizard variants step, users must be able to choose an input currency for editable premium-related inputs introduced by this task and enter values in EUR/USD.
- FR-010: The offer wizard must show immediate helper text with the PLN equivalent and rate date for foreign-currency input.
- FR-011: Before saving or updating draft offer state, foreign-currency inputs must be converted to PLN and stored in existing PLN-based model fields.
- FR-012: The application must show the applied exchange rate and publication date in each foreign-currency view context.
- FR-013: If exchange-rate data cannot be loaded, the UI must force or reset display currency to PLN and disable EUR/USD entry controls.
- FR-014: Existing current PLN displays must remain visually unchanged for users who do not switch currencies.
- FR-015: Conversion formatting rules must be: PLN with current zero-decimal display style used in the app; EUR/USD with two decimal places.
- FR-016: The selected currency must apply only within the current view and reset to PLN after navigation to another view.
- FR-017: Add unit tests for conversion/formatting logic and component behavior around fallback to PLN.
- FR-018: Add or extend UI-level tests for the supported pages to verify currency switching and degraded mode behavior.

### Edge Cases
- EC-001: If the user selects EUR/USD and rates fail to load afterward, the view should automatically revert to PLN and display a non-blocking explanatory message.
- EC-002: If only PLN is available, the switcher must remain visible but foreign options must be disabled to keep behavior explicit.
- EC-003: If persisted data contains only PLN values, converting back to EUR/USD on a later date may produce a different foreign amount; the UI must explain that foreign presentation is based on the current rate.
- EC-004: If a screen contains multiple money values derived from different domain objects, all must use the same selected presentation currency for that screen.
- EC-005: If the selected rate for EUR or USD is missing from the response, the UI must treat that currency as unavailable and continue in PLN.
- EC-006: If an entered foreign-currency amount contains decimals, conversion to PLN must follow one consistent rounding rule before persistence.
- EC-007: If the user clears or changes a foreign-currency input, helper PLN text must update immediately and not leave stale values.
- EC-008: If reports have zero values, they should still render in the selected currency format without NaN/undefined artifacts.
- EC-009: If an unsupported money field is currently plain `number` rather than `Money`, the implementation must not silently skip it on affected screens; each affected field must be explicitly reviewed.
- EC-010: If a page is opened directly by route refresh, the default presentation currency must still initialize to PLN.

### Success Criteria
- [ ] Offers page provides a currency switcher with default PLN and correctly converted premium values for EUR/USD.
- [ ] Policies page provides a currency switcher with default PLN and correctly converted annual/monthly premium values for EUR/USD.
- [ ] Reports page provides a currency switcher with default PLN and correctly converted KPI, trend, and line-of-business values for EUR/USD.
- [ ] Offer wizard supports foreign-currency amount entry with immediate PLN helper text and stores resulting values in PLN.
- [ ] Every supported foreign-currency view shows the applied rate and publication date.
- [ ] When exchange rates are unavailable, affected views degrade to PLN and foreign-currency entry is disabled.
- [ ] Existing PLN rendering remains unchanged when the user never switches currency.
- [ ] No runtime path relies on mock JSON exchange-rate data.
- [ ] Automated tests cover conversion logic, formatting rules, and fallback behavior.

### Open Questions
- [NEEDS CLARIFICATION: which exact editable fields in the current MVP of the offer wizard should support foreign-currency input first? In the current repo, `variants-step-page` mostly presents calculated premiums and discount input in PLN rather than a direct premium entry field.]
- [NEEDS CLARIFICATION: should the policy detail and customer panel mentioned in Jira be implemented in this task, or is scope limited to pages currently present in the repository (`offers`, `policies`, `reports`, `offer-wizard`)?]
- [NEEDS CLARIFICATION: what rounding rule should be used when converting entered EUR/USD values to persisted PLN amounts (e.g. standard half-up to whole PLN vs 2 decimals then UI rounds for display)?]

---

## Implementation Plan

### Technical Approach
Repo to Angular 20 + standalone components + signals. Current monetary handling is mixed:
- `src/app/core/models/common/money.model.ts` defines `Money` as PLN-only.
- Offers and wizard variants mostly use `Money` objects with `amount` and hardcoded `currency: 'PLN'`.
- Policies and reports still use several raw `number` fields (`annualPremium`, `monthlyPremium`, trend/business totals) and render them directly through Angular `CurrencyPipe`.
- Data access currently relies on local mock repositories for offers, policies, and reference data, but per Confluence the exchange-rate integration must use the documented runtime service, not a mock file.

Existing integration knowledge found:
- Reuse NBP exchange-rate service documented in Confluence: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut).
- Runtime default in this plan is the real NBP API described there, for average daily rates. The application should implement an Angular repository/service that calls the documented endpoint(s), maps the documented response shape, and exposes rates for `EUR` and `USD` against `PLN`.
- No relevant domain ADR/convention was found in the configured `SDLC` Confluence space.

Implementation strategy:
1. **Introduce a shared currency domain + exchange-rate service layer**
   - Expand the currency type from PLN-only to `PLN | EUR | USD`.
   - Add a dedicated exchange-rate repository/service in `src/app/core/repositories` or a new `core/services` area following existing injectable repository patterns.
   - The service should fetch NBP average rates once per app session/view usage, expose a signal/observable state (`loading`, `available`, `error`, `rates`, `publishedAt`), and provide conversion helpers or feed a separate pure utility module.

2. **Centralize conversion and formatting**
   - Avoid duplicating arithmetic in page components.
   - Add pure utility functions for:
     - converting PLN -> selected currency,
     - converting selected currency -> PLN for inputs,
     - formatting values according to repo rules (PLN zero decimals, EUR/USD two decimals),
     - generating helper/rate labels.
   - Because current templates use Angular `CurrencyPipe` directly, create either:
     - a reusable custom pipe, or
     - component helper methods based on utilities.
   - Prefer a custom pipe/service combo for consistency across `offers-home-page`, `policies-home-page`, `reports-page`, and wizard pages.

3. **Keep persistence in PLN**
   - `OfferWizardStateService` remains the persistence boundary for in-progress offers.
   - Any new foreign-currency input state should be transient UI state only; before writing to `draftOfferState`, values must be normalized to PLN.
   - Existing offer/policy mock payloads should stay PLN-based.

4. **View-scoped currency state**
   - Jira explicitly says PLN is the default for every session/view/form and view selection should not globally mutate data.
   - Therefore, add local component-level currency signals in each supported page rather than a global persisted preference service.
   - A small shared UI component for the switcher can be added under `src/app/shared/ui` to avoid duplicated markup.

5. **Graceful degradation**
   - On load failure, each page should:
     - set selected currency back to PLN,
     - disable EUR/USD buttons/options,
     - show a helper banner/note near the switcher,
     - keep all current functionality usable in PLN.
   - In the wizard, EUR/USD entry controls should be disabled when rates are unavailable.

6. **Codebase-specific reuse**
   - Follow current standalone component pattern as seen in:
     - `src/app/features/offers/pages/offers-home-page.component.ts`
     - `src/app/features/policies/pages/policies-home-page.component.ts`
     - `src/app/features/reports/pages/reports-page.component.ts`
     - `src/app/features/offer-wizard/pages/variants-step-page.component.ts`
   - Reuse `signal`, `computed`, and `toSignal` instead of introducing external state libraries.
   - Follow current repository injection pattern from `OffersRepository`, `PoliciesRepository`, `ReferenceDataRepository`.

7. **Likely data-model adjustments**
   - `Money` type should allow all three currencies for presentation, but persisted offer data should still remain PLN. To avoid accidental persistence drift, keep persisted money instances created in repositories/state as PLN and use view models for converted amounts.
   - For policies/reports, do not refactor all raw numeric fields to domain `Money` in this task unless required by touched screens; instead add presentation-layer mapping to computed view models.

### Task Breakdown

#### Phase 1: Currency domain and NBP integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Expand currency type | `src/app/core/models/common/money.model.ts` | Change `Money.currency` from `'PLN'` to a shared `CurrencyCode` union (`'PLN' | 'EUR' | 'USD'`) while preserving existing persisted data in PLN. |
| 1.2 | Add exchange-rate model(s) | `src/app/core/models/common/*` or `src/app/core/models/index.ts` | Define types for supported currency, exchange-rate map, rate metadata/date, and UI availability state. |
| 1.3 | Add NBP repository | `src/app/core/repositories/exchange-rates.repository.ts` | Implement Angular `HttpClient` integration against the documented NBP API from Confluence, mapping EUR/USD average rates to internal model. |
| 1.4 | Add conversion utility/service | `src/app/core/*` new utility/service file(s) | Implement pure conversion helpers for PLN↔EUR/USD, formatting rules, and rate label generation. |
| 1.5 | Add tests for conversion and API mapping | `src/app/core/**/*.spec.ts` | Cover arithmetic, formatting, rounding, missing-rate handling, and HttpClient mapping with `HttpTestingController`. |

#### Phase 2: Shared UI primitives for currency selection
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add currency switcher component | `src/app/shared/ui/currency-switcher/*` | Create a reusable standalone component rendering `PLN | EUR | USD`, selected state, disabled foreign options, and optional rate/fallback message. |
| 2.2 | Add money presentation helper | `src/app/shared/*` new pipe/helper | Create a reusable way to format converted values, reducing repeated `currency` pipe usage across templates. |
| 2.3 | Export/reuse shared component | relevant imports in feature pages | Wire the switcher into offers, policies, reports, and wizard pages using existing standalone imports pattern. |

#### Phase 3: Offers page currency presentation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add local currency state to offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Introduce selected display currency signal, exchange-rate state, fallback handling, and helper methods/view models for converted amounts. |
| 3.2 | Update offers template | `src/app/features/offers/pages/offers-home-page.component.html` | Add currency switcher near toolbar/results meta and replace hardcoded PLN premium formatting with selected-currency rendering plus rate note. |
| 3.3 | Preserve current PLN behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure default currency is PLN and visual output in PLN stays identical to current `currency:'PLN':'symbol-narrow':'1.0-0':'pl-PL'`. |
| 3.4 | Add offers page tests | new/existing spec for offers page | Verify default PLN, EUR/USD conversion, disabled options on rate failure, and consistent conversion of all offer amounts shown on the page. |

#### Phase 4: Policies page currency presentation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Add local currency state to policies page | `src/app/features/policies/pages/policies-home-page.component.ts` | Introduce view-scoped currency signal, exchange-rate integration, and converted premium helpers for annual/monthly premium display. |
| 4.2 | Update policies template | `src/app/features/policies/pages/policies-home-page.component.html` | Add currency switcher and show converted annual/monthly premiums with applied-rate note. |
| 4.3 | Handle fallback to PLN | `src/app/features/policies/pages/policies-home-page.component.ts` | Force PLN when rates unavailable and disable EUR/USD choices with user message. |
| 4.4 | Add policies page tests | new/existing spec for policies page | Verify conversion and degraded PLN-only behavior. |

#### Phase 5: Reports page currency presentation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Add local currency state to reports page | `src/app/features/reports/pages/reports-page.component.ts` | Add selected currency signal and computed converted report view models for KPIs, monthly trend, and business-line totals. |
| 5.2 | Update reports template | `src/app/features/reports/pages/reports-page.component.html` | Add switcher in report controls and replace hardcoded PLN formatting with selected-currency formatting across KPI cards and chart labels. |
| 5.3 | Keep one currency per report view | `src/app/features/reports/pages/reports-page.component.ts` | Ensure all amounts on the page derive from one selected presentation currency, avoiding mixed-currency rendering. |
| 5.4 | Add reports tests | new/existing spec for reports page | Verify consistent conversion across KPIs, trend, and business lines, including zero-data cases. |

#### Phase 6: Offer wizard foreign-currency entry
| # | Task | Files | Description |
|---|------|-------|-------------|
| 6.1 | Identify editable money inputs in wizard MVP | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `.html` | Confirm which field(s) in current wizard should support EUR/USD entry without broadening scope beyond Jira MVP. |
| 6.2 | Add wizard currency/rate state | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | Add selected input/display currency state for eligible amount input(s), load rates, and expose helper text for PLN equivalent. |
| 6.3 | Add foreign-currency input controls | `src/app/features/offer-wizard/pages/variants-step-page.component.html` | Add currency selector adjacent to the supported amount input and show dynamic helper text like `≈ X zł po kursie ...`. |
| 6.4 | Normalize to PLN on state update | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` and/or page component | Convert entered EUR/USD values to PLN before calling existing state mutations so draft offer remains PLN-based. |
| 6.5 | Disable EUR/USD entry on fallback | `src/app/features/offer-wizard/pages/variants-step-page.component.ts`, `.html` | Lock foreign options/inputs when rates unavailable and show explanatory message. |
| 6.6 | Add wizard tests | new/existing spec for variants step/state service | Verify helper text, conversion before persistence, and disabled state on unavailable rates. |

#### Phase 7: Wiring, regression checks, and app configuration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 7.1 | Register any new providers | `src/app/app.config.ts` | Add any required `provideHttpClientTesting` only in specs; production app already provides `HttpClient`, so keep runtime config minimal. |
| 7.2 | Update model exports if needed | `src/app/core/models/index.ts` | Export new currency/exchange-rate types used by pages and repositories. |
| 7.3 | Review route-level defaults | feature page components | Ensure each page initializes selected currency to PLN on navigation and direct loads. |
| 7.4 | Add e2e/smoke coverage if feasible | `tests/e2e/smoke.spec.ts` or new e2e spec | Extend smoke flow to verify visible PLN default and presence of switcher in supported pages. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Expand money currency typing beyond PLN while keeping persisted values PLN-based. |
| `src/app/core/models/index.ts` | MODIFY | Export any new currency and exchange-rate models. |
| `src/app/core/repositories/exchange-rates.repository.ts` | CREATE | Runtime integration with documented NBP API for EUR/USD rates against PLN. |
| `src/app/core/services/currency-conversion.service.ts` or similar | CREATE | Central conversion, formatting, and helper-label logic. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | Reusable currency switcher standalone component. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Switcher markup and rate/fallback message UI. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Switcher styling aligned with current shared UI patterns. |
| `src/app/shared/pipes/*` or similar | CREATE | Reusable formatting/conversion pipe/helper for selected currency rendering. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add display-currency state, exchange-rate handling, converted view models, and fallback behavior. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add switcher and render premiums/rate info in selected currency. |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Add display-currency state and converted premium helpers. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Add switcher and selected-currency premium output. |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Add report currency state and converted KPI/trend/line models. |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Add switcher and selected-currency rendering for report data. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Add foreign-currency input state, helper text, and disabled-mode behavior. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Add input currency control and PLN equivalent helper. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Ensure entered foreign amounts are normalized to PLN before persistence. |
| `src/app/app.config.ts` | MODIFY | Only if needed for additional app-wide provider wiring. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | CREATE | Unit tests for NBP API mapping and failure handling. |
| `src/app/core/services/currency-conversion.service.spec.ts` | CREATE | Unit tests for formatting/conversion logic. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | CREATE/MODIFY | Component tests for offers currency switching and fallback. |
| `src/app/features/policies/pages/policies-home-page.component.spec.ts` | CREATE/MODIFY | Component tests for policies currency switching and fallback. |
| `src/app/features/reports/pages/reports-page.component.spec.ts` | CREATE/MODIFY | Component tests for reports currency switching and fallback. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.spec.ts` | CREATE/MODIFY | Component tests for foreign-currency input and PLN helper behavior. |
| `tests/e2e/smoke.spec.ts` or new e2e spec | MODIFY | Add top-level smoke coverage for currency switcher presence/defaults. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] NBP exchange-rate integration uses the documented runtime endpoint from Confluence, not mock JSON files
5. [ ] Offers page defaults to PLN and converts displayed premium values to EUR/USD correctly
6. [ ] Policies page defaults to PLN and converts annual/monthly premium values correctly
7. [ ] Reports page converts KPI, trend, and line-of-business values consistently in one selected currency
8. [ ] Offer wizard foreign-currency input shows immediate PLN equivalent and stores PLN values only
9. [ ] Foreign-currency views show applied rate and publication date
10. [ ] When rates are unavailable, supported views reset/degrade to PLN and disable EUR/USD input paths
11. [ ] Current PLN-only behavior remains visually unchanged where currency switch is not used