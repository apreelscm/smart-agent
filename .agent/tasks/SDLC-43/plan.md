# SDLC-43: Wielowalutowość na liście ofert

## Specification

### Overview
This task delivers multi-currency presentation on the offers list so the user can switch displayed offer amounts to EUR or USD and see all currently visible list values recalculated consistently on the same screen. The change is presentation-only, limited to `/offers`, and must reuse the documented exchange-rate service rather than introduce a new rate source.

### User Stories
- US-001: As a user browsing the offers list, I want to choose EUR or USD as the display currency, so that I can compare offers in my preferred currency.
  - Given I am on the offers list / When I select EUR or USD / Then the list shows offer amounts in the selected currency

- US-002: As a user browsing the offers list, I want all visible offers to be recalculated after changing currency, so that I do not need to convert prices manually.
  - Given there are offers visible on the screen / When I change the display currency / Then all visible offer amounts are converted to the same selected currency

- US-003: As a business owner, I want exchange rates to come from the existing documented service, so that all presented values use one trusted source.
  - Given the application performs currency conversion / When it loads exchange rates / Then it uses the documented exchange-rate integration from Confluence

### Functional Requirements
- FR-001: The offers list must expose a display-currency selector that allows choosing EUR and USD.
- FR-002: Currency switching must recalculate all monetary values visible on the offers list, including row premium values and list-level monetary summaries.
- FR-003: After switching, all visible amounts on the offers list must be shown consistently in one selected currency.
- FR-004: The implementation must remain presentation-only; persisted offer data and domain money models stay PLN-based.
- FR-005: Exchange rates must be loaded from the documented existing service in Confluence, reusing:
  - https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut
  - https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- FR-006: The runtime default integration must use `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`, with no auth, reading the first table object, `effectiveDate`, and `rates[].code` / `rates[].mid` for `EUR` and `USD`.
- FR-007: The UI must clearly indicate the selected currency for displayed amounts.
- FR-008: The change scope is limited to the offers list screen; offer details, wizard steps, policies, reports, and other modules remain unchanged.
- FR-009: If rates cannot be loaded, the page must remain usable in PLN and must not switch to unsupported foreign currencies.
- FR-010: Currency switching must not trigger any write operations to offer runtime state or backend-side persistence.

### Edge Cases
- EC-001: If the exchange-rate request fails or returns malformed data, the offers list stays usable in PLN and foreign-currency options remain unavailable.
- EC-002: If only one of the required foreign currencies is returned, only that currency should be enabled.
- EC-003: If an offer has no `selectedPaymentPlan`, the list must continue using the existing fallback to `variants[0].totalPremium.amount`.
- EC-004: Leaving `/offers` and returning must reset the list-local currency selection instead of persisting it across screens.
- EC-005: Non-monetary values such as counters and offer counts must not be converted or reformatted as currency.
- EC-006: No runtime mock JSON file may be introduced as a fallback for exchange rates; mocks are allowed only in tests.

### Success Criteria
- [ ] The offers list allows switching displayed amounts to EUR and USD
- [ ] All visible offer amounts are recalculated consistently after a currency change
- [ ] Monetary summary values on the offers list use the same selected currency as row premiums
- [ ] The selected currency is clearly visible on the screen
- [ ] Exchange rates are loaded from the documented Confluence-backed NBP integration
- [ ] The implementation remains limited to the offers list
- [ ] If rates are unavailable, PLN remains usable and the page does not break
- [ ] Unit and e2e coverage confirm conversion, fallback, and non-persistence behavior

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository already contains the main building blocks for this feature, so SDLC-43 should reuse and align the existing implementation rather than rebuild it.

Codebase patterns to follow:
- `src/app/features/offers/pages/offers-home-page.component.ts` is a standalone Angular component built with `signal`, `computed`, `effect`, and `toSignal`.
- Read-only integrations are implemented as repository classes under `src/app/core/repositories/`.
- Tests use `HttpTestingController` for unit/integration-like specs and Playwright for e2e validation.
- `src/app/core/models/common/money.model.ts` is PLN-only, which matches the Jira scope: conversion is display-only, not a domain-model change.

Confluence reuse:
- Existing service reference: [Kursy walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157777921/Kursy+walut)
- Authoritative integration reference: [v1 - API NBP – pobieranie kursów walut](https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut)

Runtime default integration:
- Endpoint: `GET https://api.nbp.pl/api/exchangerates/tables/A/?format=json`
- Auth: none
- Response contract used by the app: array payload, first table object, `effectiveDate`, and `rates[]` entries with `code` and `mid`
- Required mapped currencies: `EUR`, `USD`

Repository-specific design decisions:
- Keep `ExchangeRatesRepository` in `src/app/core/repositories/exchange-rates.repository.ts` as the single read adapter for the documented NBP integration.
- Do not introduce a runtime mock file under `public/mock`; test fixtures must stay in `HttpTestingController`/Playwright route mocks only.
- Keep the selected display currency local to `OffersHomePageComponent`; do not persist it in `SalesFlowRuntimeRepository`, query params, or browser storage.
- Keep `Money` domain types unchanged and convert values only at render time through helper methods such as `convertFromPln(...)` and `formatMoney(...)`.
- Reuse the current offers-page rendering flow, where list amounts and the `Średnia składka` stat tile derive from the same selected display currency.

Confluence/domain result:
- No relevant domain guidance was found in the configured `SDLC` space, so the implementation should follow existing repository conventions only.

### Task Breakdown

#### Phase 1: Align exchange-rate integration with the documented service
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Verify runtime endpoint and mapping | `src/app/core/repositories/exchange-rates.repository.ts` | Confirm the repository continues to use the documented NBP endpoint and maps `effectiveDate` plus `EUR`/`USD` `mid` values from table A as the runtime default. |
| 1.2 | Preserve resilient fallback state | `src/app/core/repositories/exchange-rates.repository.ts` | Keep the normalized `available/loading/unavailable` state model so `/offers` remains usable when the integration fails or returns invalid data. |
| 1.3 | Tighten repository test coverage | `src/app/core/repositories/exchange-rates.repository.spec.ts` | Extend/retain tests for successful mapping, request failure, and partial/malformed payload behavior using `HttpTestingController` fixtures only. |

#### Phase 2: Keep currency selection list-local and apply it consistently on `/offers`
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Keep display currency as component-local state | `src/app/features/offers/pages/offers-home-page.component.ts` | Reuse the existing signal-based state so the currency choice affects only the offers list and resets on re-entry to the route. |
| 2.2 | Ensure all visible monetary values share one conversion path | `src/app/features/offers/pages/offers-home-page.component.ts` | Verify row premiums and the monetary summary tile both derive from the same PLN-to-selected-currency conversion helpers. |
| 2.3 | Preserve PLN-backed domain data | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/core/models/common/money.model.ts` | Keep persisted values and domain types in PLN; do not expand `Money.currency` beyond its current PLN-only contract. |
| 2.4 | Prevent unintended write-side effects | `src/app/features/offers/pages/offers-home-page.component.ts` | Ensure currency switching never calls `saveDraftOffer`, `promoteOfferToPolicy`, or mutates offers beyond presentation. |

#### Phase 3: Validate offers-list UI behavior and screen scope
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Keep the offers toolbar currency switcher | `src/app/features/offers/pages/offers-home-page.component.html` | Retain the list-level control that exposes the supported display currencies and makes the selected state obvious to the user. |
| 3.2 | Ensure currency labeling is explicit | `src/app/features/offers/pages/offers-home-page.component.html` | Verify premiums, list-level rate label, and warning states clearly communicate which currency is active. |
| 3.3 | Keep unavailable-rate feedback inline | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Preserve the warning message and disabled-state styling when foreign rates are not available. |
| 3.4 | Limit the change to `/offers` only | `src/app/app.routes.ts` | Confirm no other routes or feature pages are updated as part of SDLC-43. |

#### Phase 4: Lock the behavior with tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Maintain offers-page unit coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep assertions for default PLN display, EUR/USD switching, applied-rate visibility, disabled foreign currencies on failure, and no runtime write calls. |
| 4.2 | Maintain route-level regression coverage | `tests/e2e/offers-currency-switcher.spec.ts` | Keep e2e checks for PLN/EUR/USD switching, NBP failure fallback, and resetting to PLN after leaving and re-entering `/offers`. |
| 4.3 | Run final regression verification | `package.json` | Use the existing `build`, `test`, and `e2e` scripts to verify no regressions in the Angular app or the offers list behavior. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/repositories/exchange-rates.repository.ts` | MODIFY | Align and preserve the Confluence-backed runtime integration and fallback state handling. |
| `src/app/core/repositories/exchange-rates.repository.spec.ts` | MODIFY | Verify successful mapping plus unavailable/invalid response handling. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Keep conversion logic, component-local currency state, and presentation-only behavior consistent with Jira scope. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Validate currency switcher, converted amount rendering, rate label, and warning message on the offers list. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Preserve styling for the switcher, selected state, and feedback banners. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Lock down list-level conversion and non-persistence behavior with unit tests. |
| `tests/e2e/offers-currency-switcher.spec.ts` | MODIFY | Keep route-level regression coverage for switching, fallback, and reset behavior. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements