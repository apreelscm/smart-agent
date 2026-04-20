# PZU-13: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
Dostarczyć MVP wsparcia dla prezentacji i wprowadzania kwot w trzech walutach: PLN, EUR, USD. Cel techniczny tego zadania to:
- wprowadzić model/serwis pobierający kursy walut (integracja z NBP zgodnie z istniejącą dokumentacją usług w Confluence),
- dodać warstwę konwersji i formatowania walut (bez zmiany modelu rozliczeniowego — dane utrwalane pozostają w PLN),
- dodać prosty komponent przełącznika waluty i zastosować go w krytycznym widoku kreatora ofert (variants-step) oraz w listach ofert/polis, aby pokazać działanie end‑to‑end.

Dokumentacja integracyjna, z którą należy się kierować (re-use): "v1 - API NBP – pobieranie kursów walut" (Services space):
https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut

### User Stories
- US-001: As an agent, I want to switch presentation currency to EUR or USD so that I can present premiums in client's currency.
  - Given the view is opened / When I select EUR or USD from the currency switch / Then all amounts on this view are shown converted from stored PLN and formatted according to chosen currency (EUR & USD: 2 decimals; PLN: no grosze).
- US-002: As an agent entering an offer, I want to see instantaneous PLN equivalent when I view amounts in EUR/USD, so that I know the stored PLN value.
  - Given a view with amounts stored in PLN / When I switch to EUR or USD / Then next to displayed foreign-currency amount a discrete label shows the rate used and approximate PLN equivalent.
- US-003: As developer/ops, I want the app to use official NBP specs to fetch rates and degrade gracefully, so that the app remains usable if the rates service is unavailable.
  - Given the rates endpoint is unreachable / When the app requests rates / Then the UI forces PLN-only and disables foreign-currency inputs with a clear message.

Each story (Given / When / Then) above is condensed into the UI behavior described.

### Functional Requirements
- FR-001: Implement a currency rates service that obtains PLN↔EUR and PLN↔USD rates according to the NBP spec and caches them locally (daily TTL).
- FR-002: Provide a conversion utility: convert(amount: number, from: 'PLN'|'EUR'|'USD', to: 'PLN'|'EUR'|'USD').
- FR-003: Add a shared currency-switch UI component (PLN | EUR | USD) emitting selected currency for the current view.
- FR-004: Add a display pipe/helper to format numbers per-currency:
  - PLN: no decimal places, use "zł" or "PLN" per current app style (keep existing PLN visuals intact),
  - EUR, USD: two decimals and correct symbol ($/€) and separators.
- FR-005: Apply conversion + formatting in the Offer Wizard variants view (variants-step) for:
  - variant.totalPremium,
  - variant.monthlyPremium / payment plan rows (first installment, remaining, total).
  Show also small text with the exchange rate and approximated PLN equivalent when a foreign currency is selected.
- FR-006: Store rates and lastUpdated timestamp in localStorage; when stale or unavailable, fetch from network; if network fails and no cached rates exist, mark currency service unavailable.
- FR-007: When currency service unavailable: views default to PLN and disable foreign-currency inputs (display a concise message).
- FR-008: Keep persistent data model semantics: all persisted Money values remain PLN. The currency switch only affects presentation/conversion.

### Edge Cases
- EC-001: Rates service returns incomplete data (e.g. only EUR). Behavior: allow conversion for available currencies; disable unavailable currency options with tooltip explaining missing rate. View must not show mixed-currency tables (a single selected currency per view).
- EC-002: Cached rates older than 24h. Behavior: try refresh in background; meanwhile use cached rates but indicate "rates as of <date>" and attempt to refresh; if refresh fails, continue with cached rates and show a warning that rates may be outdated.
- EC-003: Very large amounts or negative values. Behavior: conversion and formatting handle magnitude and sign; rounding rules: convert then round to currency-appropriate precision (PLN integer, EUR/USD 2 decimals); sums/allocations that depend on integer PLN rounding must be recomputed from PLN persisted values (no propagation of rounding errors to stored PLN).
- EC-004: User toggles currency repeatedly. Behavior: conversion is stateless (uses current cached rates); selection is view-local (resets to PLN on new view per business rules).
- EC-005: Payment plan installments recompute when conversion changes; ensure integer math for PLN-based totals and even split rules replicate existing approach (compute from PLN stored totalPremium then convert results for display).

### Success Criteria
- [ ] Currency service implemented and integrated with NBP API spec (link included).
- [ ] Currency-switch component available and present on Offer Wizard variants page and Offers/Policies main pages.
- [ ] When selecting EUR or USD, variants-step displays converted totals, installments and a small rate + PLN-equivalent label.
- [ ] If rates are unavailable and no cache exists, UI forces PLN and disables foreign-currency presentation/inputs with clear message.
- [ ] Unit tests for CurrencyService and currency display pipe; e2e manual check for variants-step conversions.
- [ ] Build succeeds and existing tests still pass.

### Open Questions
- [NEEDS CLARIFICATION: persistence of the selected presentation currency] — Should the selected currency be remembered across views in a session, or must it reset to PLN on view navigation? (Business spec suggests default reset to PLN, but early decision required.)
- [NEEDS CLARIFICATION: exact visual style for rate disclosure] — small footer vs inline helper text next to the switch? Provide a preferred design snippet if available.
- [NEEDS CLARIFICATION: production NBP endpoint credentials/URL] — Confluence documents the API shape; confirm which provider (NBP or commercial) will be used in production and if an API key is required.

---

## Implementation Plan

### Technical Approach
Strategy:
- Introduce a CurrencyService responsible for fetching, caching (localStorage), refreshing, and converting amounts. It will default to using a mock file in development (public/mock/currency-rates.json) and support switching to the NBP endpoint for production using environment configuration.
- Extend the existing Money model to allow currency union 'PLN' | 'EUR' | 'USD' (but preserve existing semantics: persistent Money amounts remain PLN). This avoids many type errors while enabling transient UI Money objects if needed.
- Implement a currency-format pipe and small utility to centralize formatting rules and rounding per currency.
- Implement a shared CurrencySwitch component that emits the selected currency. Keep selection view-scoped (component-level) and do not persist across views (default PLN when component initializes).
- Apply conversion when rendering amounts in variants-step-page.component.ts: convert stored PLN amounts to selected currency for display; show PLN-equivalent text and rate.
- Reuse Confluence NBP API doc for endpoint, response shapes and error handling: https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut

Rationale and repository patterns:
- The project uses Angular with HttpClient and existing repository pattern (ReferenceDataRepository). We'll follow this: create CurrencyService in src/app/core/services similar to ReferenceDataRepository (HttpClient injection, Observable public methods).
- Use localStorage caching similar to common Angular patterns. Keep defaults pointing to mock files in public/mock for local dev.
- Follow existing coding style: TypeScript interfaces in src/app/core/models, services in core/services, UI elements in src/app/shared/ui.

Reuses:
- Confluence "v1 - API NBP – pobieranie kursów walut" page for integration details:
  https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut

### Task Breakdown

#### Phase 1: Core models and currency service
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Update Money model | `src/app/core/models/common/money.model.ts` | Modify currency union to allow 'PLN'|'EUR'|'USD' and add JSDoc describing persisted semantics (stored in PLN). |
| 1.2 | Add currency rates mock | `public/mock/currency-rates.json` | CREATE mock rates file used in dev (contains PLN↔EUR, PLN↔USD, date). |
| 1.3 | Create CurrencyService | `src/app/core/services/currency.service.ts` | CREATE service: fetchRates(), getRates(), convert(amount, from, to), isAvailable(), rateInfo(); uses HttpClient, caches in localStorage with TTL 24h; fallback behavior if endpoint unavailable. Use NBP Confluence doc for endpoint/response mapping. |
| 1.4 | Add types for rates | `src/app/core/models/currency.model.ts` | CREATE RateSet interfaces (rates, base, date). |

#### Phase 2: Formatting utilities and UI components
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Create currency display pipe | `src/app/shared/pipes/currency-display.pipe.ts` | CREATE pipe to format amounts per currency rules (PLN: 0 decimals, EUR/USD: 2 decimals). |
| 2.2 | Create currency switch component | `src/app/shared/ui/currency-switch/currency-switch.component.ts` plus .html/.scss | CREATE a simple segmented control/dropdown emitting selected currency and showing disabled state if CurrencyService.isAvailable() is false or currency rate missing. |
| 2.3 | Wire styles | `src/app/shared/ui/currency-switch/currency-switch.component.scss` | CREATE minimal styles consistent with project theme. |

#### Phase 3: Integrate into variants view and lists (MVP UI)
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Inject CurrencyService into VariantsStep | `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY: add selectedCurrency signal, subscribe to CurrencyService.isAvailable/status, convert amounts in paymentPlanRows and discountedAmount for display using service.convert(). Show small rate/PLN equivalent text. Keep internal computations (discounts, stored totals) based on PLN. |
| 3.2 | Update variants HTML to show currency switch + rate info | `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY: place currency-switch in header area, use currency-display pipe where amounts are shown, add helper text: "≈ {PLN_equivalent} PLN po kursie {rate} z dnia {date}". |
| 3.3 | Integrate currency switch into offers/policies pages header | `src/app/features/offers/pages/offers-home-page.component.ts` and `src/app/features/policies/pages/policies-home-page.component.ts` plus their HTML | MODIFY: add currency-switch in page header and use currency-display pipe on summary tiles (minimal demonstration). These pages will use CurrencyService to convert displayed totals (mocked from public/mock data). |

#### Phase 4: Tests and fallback behavior
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Unit tests for CurrencyService | `src/app/core/services/currency.service.spec.ts` | CREATE tests: fetchRates (mock HttpClient), convert correctness, caching behavior, unavailable fallback. |
| 4.2 | Unit tests for currency-display pipe | `src/app/shared/pipes/currency-display.pipe.spec.ts` | CREATE tests for formatting rules. |
| 4.3 | Manual QA checklist and small e2e script (manual instructions) | `.agent/tasks/PZU-13/qa.md` | CREATE QA steps how to verify rates, degrade to PLN, verify rounding and displayed helper text. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Expand currency union to 'PLN'|'EUR'|'USD' and add docstring explaining stored semantics (PLN). |
| `src/app/core/models/currency.model.ts` | CREATE | Types for Rate, RateSet, RatesCache. |
| `public/mock/currency-rates.json` | CREATE | Mock rates payload (PLN base, EUR & USD rates, date). |
| `src/app/core/services/currency.service.ts` | CREATE | Service to fetch, cache, convert currency rates; dev uses mock file, production can use NBP endpoint per Confluence. |
| `src/app/shared/pipes/currency-display.pipe.ts` | CREATE | Pipe applying formatting rules per currency. |
| `src/app/shared/pipes/currency-display.pipe.spec.ts` | CREATE | Unit tests for pipe. |
| `src/app/shared/ui/currency-switch/currency-switch.component.ts` | CREATE | Currency switch UI component + outputs. |
| `src/app/shared/ui/currency-switch/currency-switch.component.html` | CREATE | Template for switch. |
| `src/app/shared/ui/currency-switch/currency-switch.component.scss` | CREATE | Styling. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.ts` | MODIFY | Inject CurrencyService, add selectedCurrency signal, call conversion for displayed amounts. |
| `src/app/features/offer-wizard/pages/variants-step-page.component.html` | MODIFY | Insert currency-switch and display rate/PLN-equivalent helper text. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Add currency switch to demonstrate view-level currency. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Use currency-display pipe on sum/tile (small demo). |
| `src/app/features/policies/pages/policies-home-page.component.ts` | MODIFY | Add currency switch to policy list header. |
| `src/app/features/policies/pages/policies-home-page.component.html` | MODIFY | Use currency-display pipe on summary numbers. |
| `src/app/core/services/currency.service.spec.ts` | CREATE | Unit tests for CurrencyService (mock HttpClient). |
| `.agent/tasks/PZU-13/qa.md` | CREATE | QA steps and verification instructions. |

### Verification Steps
1. [ ] Build succeeds: npm run build
2. [ ] Unit tests pass: npm test (new tests for CurrencyService and pipe)
3. [ ] Manual QA:
   - Start dev server (ng serve).
   - Open Offer Wizard -> Variants step:
     - By default currency = PLN; numbers display as before (no regression).
     - Switch to EUR and USD: variant amounts, payment plan rows show converted amounts; small helper shows "≈ {n} PLN po kursie {rate} z dnia {date}".
     - If public/mock/currency-rates.json is renamed/unavailable, the UI forces PLN and shows message about unavailable foreign-currency support.
   - Check Offers and Policies pages show currency-switch and converted summary numbers.
4. [ ] Edge-case tests:
   - Cached rates older than 24h: service attempts refresh; display indicates last update date.
   - Partial rate availability: unavailable currency option is disabled and shows reason on hover.

## Rules / Implementation Notes
- The Money model change is limited to widening the currency union type; persisted semantics stay unchanged: data in backend and mock data remain PLN. All conversions for display are computed on the client using CurrencyService.
- Follow existing repository patterns: services under src/app/core/services, models under src/app/core/models, shared UI under src/app/shared/ui. Implement service using HttpClient similar to ReferenceDataRepository.
- The CurrencyService will default to an internal dev URL 'mock/currency-rates.json' (public/mock) to ease local development. For production the base URL will be configurable (environment file) and should follow the NBP spec in Confluence.
- Keep scope limited: implement UI integration only for Offer Wizard variants and top-level Offers/Policies pages as demonstration of functionality. Additional views (reports, client panel) are out of scope for this ticket and will reuse the same CurrencyService/component later.
- Session/persistence of the selected currency: default behavior for MVP is view-scoped (resets to PLN on new view) — this matches the business spec and avoids cross-view state complexity. If persistence is requested later, the CurrencySwitch component can be enhanced to persist to sessionStorage.