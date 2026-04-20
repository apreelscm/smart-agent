# PZU-13: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Deliver front-end multi-currency presentation and input support for PLN, EUR and USD in the SmartAgent Angular app. Preserve PLN as canonical storage currency (no back-end migration). Implement:
- a typed Money model supporting 3 currencies,
- an ExchangeRate repository (mocked for DEV) and a CurrencyService that converts and formats values,
- a reusable CurrencySwitcher UI (contextual, resets on navigation),
- integrate presentation and input helper hints across Offers, Policies, Reports and Offer Wizard pages per the Jira business requirements:
  - view-level currency switcher (PLN | EUR | USD), default PLN on view open,
  - switching recalculates displayed values using current exchange rates and adjusts formatting (PLN: no grosze; EUR/USD: two decimals),
  - form inputs allow entering amounts in EUR/USD, show immediate helper ≈ {PLN} with applied rate,
  - if exchange rates unavailable, UI degrades: force PLN and disable foreign-currency input with clear message.

### User Stories
- US-001: As an agent, I want to switch a view's currency (PLN|EUR|USD) so that monetary values are presented in that currency.
  - Given the view is opened / When I select EUR or USD / Then all amounts on the view are re-rendered using the current exchange rate and formatting rules, and the rate used is visible near the switcher.

- US-002: As an agent creating an offer, I want to enter monetary fields in EUR or USD and see an immediate approximate PLN equivalent so I can validate with PLN-based backend.
  - Given exchange rates are available / When I input 500 in USD and mark the field currency as USD / Then the UI shows “≈ 2 020 zł po kursie 4,04 PLN/USD z dnia YYYY-MM-DD” next to the field.

- US-003: As an agent, if the exchange-rate service is unavailable, I want the UI to remain functional in PLN and show that foreign-currency presentation/input is disabled.
  - Given exchange API fails / When I open the view / Then the switcher is forced to PLN and foreign-currency input controls are disabled with a message.

### Functional Requirements
- FR-001: Money model must support currency = 'PLN' | 'EUR' | 'USD'.
- FR-002: Provide an ExchangeRateRepository that loads daily rates from configurable endpoint; include a DEV mock file public/mock/exchange-rates.json.
- FR-003: CurrencyService must:
  - store current view currency (contextual; default PLN),
  - provide format(money: Money, targetCurrency?: Currency): string,
  - provide convertToDisplay(money: Money, targetCurrency: Currency): Money (uses loaded rates),
  - provide convertFromInput(value: number, inputCurrency: Currency): { plnAmount: number, usedRate: RateMeta } for saving,
  - provide isRatesAvailable(): boolean and lastUpdateDate().
- FR-004: Add CurrencySwitcherComponent (shared) exposing current currency and selection events; show small text with used rate and date when non-PLN selected.
- FR-005: Integrate CurrencyService + CurrencySwitcherComponent into:
  - src/app/features/offers/pages/offers-home-page.component.*,
  - src/app/features/policies/pages/policies-home-page.component.*,
  - src/app/features/reports/pages/reports-page.component.*,
  - the Offer Wizard pages: variants-step-page.component.*, summary-step-page.component.*, and input fields where premiums are entered.
- FR-006: Update computed UI values (summary tiles, primary premium, monthly premium, offer variant lists) to use CurrencyService.format instead of hard-coded PLN formatting.
- FR-007: Provide graceful fallback: if rates missing/unavailable, disable switcher to foreign currencies and show message in switcher and input helpers.

### Edge Cases
- EC-001: Missing currency property in mock or backend Money objects — treat as PLN and log a warning; UI renders as PLN.
- EC-002: Exchange-rate fetch fails (network, 5xx) — UI forces PLN, disables foreign inputs, shows banner/text: "Usługa kursowa niedostępna — widok w PLN".
- EC-003: Mixed currencies inside a single view (some items marked PLN, others PLN-only) — the view-level switcher dictates presentation: convert all numerically to selected currency; do not mix currency symbols in the same table (presented currency uniform).
- EC-004: Rounding differences after repeated convert/display — display uses target currency rounding rules (PLN: 0 decimals by default for list/tiles where currently used; configurable), internal PLN storage unchanged.
- EC-005: Historical offers saved as PLN cannot be recovered to original foreign input 1:1 — when user switches to foreign currency, converted presentation uses latest rate and an explanatory note is shown per business rules.

### Success Criteria
- [ ] Money model updated to accept PLN|EUR|USD and code compiles.
- [ ] ExchangeRateRepository returns rates from public/mock/exchange-rates.json in DEV; CurrencyService consumes them.
- [ ] CurrencySwitcherComponent added and present on Offers, Policies, Reports and Offer Wizard pages.
- [ ] All visible monetary amounts on those pages re-render on currency switch using rates and formatting rules.
- [ ] Offer wizard input fields allow choosing input currency (PLN/EUR/USD) and display immediate PLN helper and used rate.
- [ ] If rates unavailable, foreign currency options are disabled and UI shows fallback message.
- [ ] Unit tests added for CurrencyService and ExchangeRateRepository; key formatting/convert cases covered.
- [ ] Manual acceptance: example scenarios from Jira (Agent in EUR, Agent entering USD, Manager viewing report in EUR) verified.

### Open Questions
- [NEEDS CLARIFICATION: session vs view persistence] Should a user's currency selection persist across views within the same session, or must it reset to PLN on every navigation as default? (Jira suggests default reset on view change but allows session-memory as alternative.)
- [NEEDS CLARIFICATION: authoritative exchange provider] Which external exchange-rate provider should be used in production? (Ticket recommends NBP; need confirmation and API contract / frequency.)
- [NEEDS CLARIFICATION: exact PLN formatting rules] Confirm PLN display should be without grosze (0 decimals) everywhere, and EUR/USD with two decimals. Should there be exceptions (e.g., in forms where cents might be needed for precision)?

---

## Implementation Plan

### Technical Approach
- Technology: Angular 20 app. Follow existing repository patterns: repositories under src/app/core/repositories, models under src/app/core/models, shared UI under src/app/shared/ui, services under src/app/core/services (new).
- Reuse: attempted to search Confluence Services and PZU spaces for an existing exchange-rate service or conventions, but Confluence calls returned 403 (permission error). Therefore no existing internal service could be discovered programmatically; plan implements a front-end repository to fetch exchange rates from a configurable endpoint and includes a DEV mock file at public/mock/exchange-rates.json. If later a centralized rates API exists, ExchangeRateRepository will be pointed at that endpoint (no other changes needed).
- Design decisions:
  - All persisted data remains PLN (no changes to server payloads).
  - Currency selection is contextual: CurrencyService will expose an API to set per-component currency. By default a view sets currency to PLN on init (consistent with ticket). If product owner wants session persistence later, we can add sessionStorage persistence with a flag.
  - Formatting: use Intl.NumberFormat with locale 'pl-PL' and currency code as needed; implement PLN formatting rule to hide grosze when presenting lists/tiles; for input fields show two decimals for EUR/USD.
  - Rates semantics: repository returns RateMeta { plnPerUnit: number, date: string } for EUR and USD. Conversion functions are deterministic and documented.

### Task Breakdown

#### Phase 1: Core models & services
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Update Money model | `src/app/core/models/common/money.model.ts` | Modify interface to allow currency: 'PLN' | 'EUR' | 'USD'. |
| 1.2 | Create ExchangeRateRepository | `src/app/core/repositories/exchange-rate.repository.ts` | New Injectable repository to fetch rates from configurable URL; DEV points to `mock/exchange-rates.json`. Exposes getRates(): Observable<ExchangeRates> and isRatesAvailable(). |
| 1.3 | Create CurrencyService | `src/app/core/services/currency.service.ts` | New service providing: currentCurrency (signal), setCurrency(), format(money,targetCurrency?), convertToDisplay(money,targetCurrency), convertFromInput(value,inputCurrency) and rate metadata getters. Uses ExchangeRateRepository internally. |

#### Phase 2: Mock data and fallback behaviour
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add mock exchange rates | `public/mock/exchange-rates.json` | Create JSON with PLN per 1 EUR and PLN per 1 USD and date, e.g. {"date":"2026-04-20","rates":{"EUR":4.35,"USD":4.04}}. |
| 2.2 | Update ReferenceData repository config | `src/app/core/repositories/reference-data.repository.ts` | (Optional) ensure repository base path can be used by ExchangeRateRepository pattern; add nothing breaking. |

#### Phase 3: UI component - Currency switcher
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Create CurrencySwitcherComponent | `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` `src/app/shared/ui/currency-switcher/currency-switcher.component.html` `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | Component emits selected currency and shows used exchange-rate hint when non-PLN selected. If rates unavailable, presents disabled controls and explanatory tooltip/message. Expose @Output currencyChanged and uses CurrencyService. |
| 3.2 | Export component so it can be imported by pages | update module/ barrel if present (no NgModule in repo; components are standalone) | Ensure the component is standalone (imports CommonModule) so it can be added to pages' imports arrays. |

#### Phase 4: Integrate into views (Offers, Policies, Reports, Offer Wizard)
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Integrate switcher to Offers page | `src/app/features/offers/pages/offers-home-page.component.ts` `offers-home-page.component.html` | Add CurrencySwitcherComponent to imports and to template header area; inject CurrencyService; replace hard-coded PLN formatting in summaryTiles and any string concatenation (currently uses `.toLocaleString('pl-PL') + ' zł'`) with CurrencyService.format; update getPrimaryPremium to return number used only for calculations and formatting via CurrencyService in UI. |
| 4.2 | Integrate switcher to Policies page | `src/app/features/policies/pages/policies-home-page.component.ts` `policies-home-page.component.html` | Same integration for selected pages that show premiums / totals. |
| 4.3 | Integrate switcher to Reports page | `src/app/features/reports/pages/reports-page.component.ts` `reports-page.component.html` | Same integration for reports, ensure charts/tables use CurrencyService.format for labels (charts may need string formatting only). |
| 4.4 | Integrate input helpers in Offer Wizard | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` `summary-step-page.component.ts` and relevant *.html files | When a premium input field can accept foreign currency, add a per-field small helper text showing ≈ {PLN} and used rate provided by CurrencyService.convertFromInput. If UI uses generic input components, add currency choice select near monetary inputs (small select with PLN/EUR/USD), disabled when rates unavailable. |
| 4.5 | Update other places showing Money | Search repository for direct usages of `.toLocaleString('pl-PL')` with 'zł' or places where money is concatenated and replace with CurrencyService.format(...) (files to change: offers-home-page.component.ts (done), potentially other components). |

#### Phase 5: Tests and docs
| # | Task | Files | Description |
|---|------|-------|-------------|
| 5.1 | Unit tests for CurrencyService | `src/app/core/services/currency.service.spec.ts` | Cover format, convertToDisplay, convertFromInput and fallback behavior when rates missing. |
| 5.2 | Unit tests for ExchangeRateRepository | `src/app/core/repositories/exchange-rate.repository.spec.ts` | Mock HttpClient to return mock/exchange-rates.json and test parsing and isRatesAvailable. |
| 5.3 | Update README or add developer note | `README.md` or `docs/CURRENCY.md` (CREATE) | Short note how to configure ExchangeRateRepository in production and where to change mock data. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Extend Money.currency to 'PLN'|'EUR'|'USD' and update any comments. |
| `src/app/core/repositories/exchange-rate.repository.ts` | CREATE | Repository to fetch exchange-rate JSON from configurable URL (DEV -> mock/exchange-rates.json). |
| `src/app/core/services/currency.service.ts` | CREATE | Central service for current currency, formatting and conversion functions. |
| `public/mock/exchange-rates.json` | CREATE | DEV mock with sample rates and date. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.ts` | CREATE | Standalone component implementing the currency switcher UI. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.html` | CREATE | Markup for switcher and rate display. |
| `src/app/shared/ui/currency-switcher/currency-switcher.component.scss` | CREATE | Styling for switcher component. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Inject CurrencyService, add CurrencySwitcher to imports, replace formatting logic. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add switcher component markup and present formatted values via CurrencyService. |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Add CurrencySwitcher integration and use CurrencyService.format. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Add switcher and format amounts. |
| `src/app/features/reports/pages/reports-page.component.ts` | MODIFY | Inject CurrencyService and format numeric labels. |
| `src/app/features/reports/pages/reports-page.component.html` | MODIFY | Add switcher on reports page header. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Add currency input helper logic. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Add per-field currency select & PLN helper. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.ts` | MODIFY | Format summary totals with CurrencyService. |
| `src/app/features/offer-wizard/pages/summary-step-page.component.html` | MODIFY | Show currency notes and used rate. |
| `src/app/core/services/currency.service.spec.ts` | CREATE | Unit tests. |
| `src/app/core/repositories/exchange-rate.repository.spec.ts` | CREATE | Unit tests. |
| `docs/CURRENCY.md` | CREATE | Developer docs for configuration & provider choices. |

Notes:
- Exact HTML insertion points: each page has an existing header area (page header component). The CurrencySwitcher should be added near PageHeaderComponent usage or in the template top bar for consistency.
- The CurrencySwitcher is implemented as a standalone component so it can be added to the `imports` array in component metadata (matching the project's pattern for standalone components like PageHeaderComponent).

### Verification Steps
1. [ ] ng build succeeds (no TypeScript errors after changing Money model and usages).
2. [ ] Unit tests for CurrencyService and ExchangeRateRepository pass (ng test).
3. [ ] Manual QA:
   - Open Offers page: default currency = PLN; switch to EUR -> all amounts re-render; rate displayed near switcher.
   - Open Offer Wizard: select USD input for premium, type 500 -> helper shows approximate PLN and used rate.
   - Simulate exchange-rate fetch failure (simulate network error or return invalid JSON) -> switcher disables EUR/USD and shows explanatory message; inputs for foreign currency disabled.
   - Reports page: switching currency converts chart labels and table sums; no mixed-currency cells appear.
4. [ ] Confirm saved data unchanged (still amounts stored in PLN in existing mocks and code flows).
5. [ ] Code review checklist: clear separation of conversion/format logic, decoupled exchange-rate source, test coverage.

## Rules / Implementation constraints
- Keep all persistence in PLN — do not change any backend payloads or storage contracts.
- Use a front-end repository/service approach so that when a central exchange-rate API is provided it can be swapped by only changing ExchangeRateRepository URL and minor config.
- Follow existing code patterns: repositories are Angular injectables under src/app/core/repositories, models under src/app/core/models, and standalone UI components used in pages’ `imports` arrays (see PageHeaderComponent). CurrencySwitcher must be standalone to follow that pattern.
- Keep scope limited to the UI, the conversion/formatting layer and the mock exchange-rate source for DEV.
- Avoid changing business logic around offer totals and calculations — conversions are presentation-only. When user enters non-PLN values, convert to PLN right before calling existing save code paths (use convertFromInput in CurrencyService).