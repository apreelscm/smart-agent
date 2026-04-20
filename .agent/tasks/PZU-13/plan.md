# PZU-13: Obsługa kwot w walutach obcych (PLN, EUR, USD)

## Specification

### Overview
This task delivers multi-currency presentation and input support for money values across the sales flow, policies, customers, and reports screens. The system remains PLN as the canonical persisted currency, while allowing users to view and enter amounts in PLN, EUR, or USD with live conversion and clear exchange-rate transparency.

### User Stories
- US-001: As an agent, I want to switch displayed money amounts between PLN, EUR, and USD, so that I can discuss quotations with customers in the contract currency.
  - Given a view with monetary values / When I choose EUR or USD / Then all visible amounts are recalculated and formatted consistently for that currency.
- US-002: As an agent, I want to enter a quote amount in EUR or USD and see its PLN equivalent immediately, so that I can save the offer in the system’s accounting currency.
  - Given a form field that accepts monetary input / When I enter a foreign-currency value / Then the UI shows the live PLN equivalent and persists the value in PLN.
- US-003: As a manager, I want reports and policy/customer screens to use a single selected currency per view, so that the whole table/dashboard stays readable and comparable.
  - Given a report or list view / When I switch currency / Then every amount on that view uses the same currency and exchange rate.
- US-004: As a user, I want the system to fall back safely when exchange rates are unavailable, so that core workflows remain usable.
  - Given the exchange-rate source is unavailable / When I open a currency-aware view / Then the UI stays in PLN and foreign-currency entry is disabled with a clear message.

### Functional Requirements
- FR-001: The app must support the currency set `{PLN, EUR, USD}` only.
- FR-002: PLN must remain the default currency for every new view, session, and form.
- FR-003: Currency choice must be view-scoped, not a global user profile preference.
- FR-004: Each affected view must expose a visible currency switcher with the options PLN, EUR, USD.
- FR-005: Switching currency must recalculate all monetary values on the current view using the current exchange rate.
- FR-006: Foreign-currency values entered by the user must be converted to PLN before persistence.
- FR-007: The UI must show the applied exchange rate in a clear but unobtrusive way.
- FR-008: If exchange-rate retrieval fails, the system must degrade to PLN-only display and disable EUR/USD entry fields.
- FR-009: Reports must render all monetary measures in a single chosen currency with no mixed-currency rows.
- FR-010: Existing PLN-only behavior must remain visually unchanged for users who do not switch currency.

### Edge Cases
- EC-001: If exchange rates are unavailable, the selector must effectively behave as PLN-only and show a user-facing availability message.
- EC-002: If a user changes the view currency multiple times, the underlying persisted amounts must not change.
- EC-003: When displaying historical saved amounts in a foreign currency, the value may differ from the original entered foreign amount because conversion uses the current rate.
- EC-004: If a screen contains multiple monetary groups, they must all use the same currency within that screen.
- EC-005: A route change to another view should reset currency back to PLN unless an existing session-scoped convention already handles this consistently.
- EC-006: Invalid numeric input must be rejected with validation feedback before conversion.
- EC-007: Zero/empty amounts must format correctly in each supported currency.
- EC-008: If a policy/offer has no monetary value available, the currency switcher should not break layout or calculations.

### Success Criteria
- [ ] Currency switcher is available on all in-scope views: offers, policies, customer panel, reports, and offer wizard monetary entry points.
- [ ] PLN is the default currency on initial load of each in-scope view.
- [ ] Switching to EUR or USD recalculates all visible amounts on the current view.
- [ ] Entering EUR/USD in the offer flow shows an immediate PLN equivalent.
- [ ] Saved offers/policies continue to persist amounts in PLN.
- [ ] Exchange rate provenance is visible to the user on currency-aware screens.
- [ ] The app degrades to PLN-only mode when exchange rates cannot be loaded.
- [ ] Existing PLN-only screens remain functionally unchanged when currency switching is not used.
- [ ] Unit tests cover conversion, formatting, fallback behavior, and view-scoped state handling.

### Open Questions
- [NEEDS CLARIFICATION: Should the selected currency reset to PLN on every route change, or should it persist within the browser session for related screens?]
- [NEEDS CLARIFICATION: Which exchange-rate provider is the approved runtime integration if NBP is not acceptable for deployment credentials/compliance reasons?]

---

## Implementation Plan

### Technical Approach
Reuse the existing Angular signal-based architecture and the current runtime repository pattern rather than introducing a separate currency subsystem from scratch.

Relevant repo patterns:
- `src/app/core/repositories/sales-flow-runtime.repository.ts` already centralizes runtime state in `signal()` storage and localStorage persistence.
- `src/app/features/offer-wizard/pages/*.ts`, `src/app/features/policies/pages/policies-home-page.component.ts`, and `src/app/features/reports/pages/reports-page.component.ts` already use computed signals for derived UI state.
- `src/app/core/models/common/money.model.ts` currently hard-codes `currency: 'PLN'`, which is the main type-level blocker for multi-currency UI state.
- Existing monetary presentation already uses Angular `CurrencyPipe` in places like variants and reports, so the implementation should extend that convention.

Confluence reuse:
- Runtime exchange-rate integration should follow **v1 - API NBP – pobieranie kursów walut** (Services space): https://apreel.atlassian.net/wiki/spaces/Services/pages/157483010/v1+-+API+NBP+pobieranie+kurs+w+walut
- The ticket’s business framing is compatible with the PZU domain context; no contradictory ADR or product convention was found in the PZU space search results.

Planned implementation:
1. Introduce a currency domain model and conversion helper so views can request display currency and convert values from persisted PLN.
2. Add a small exchange-rate service/repository layer that reads NBP rates as the runtime default, caches daily rates, and exposes PLN↔EUR and PLN↔USD conversions.
3. Extend the runtime state to store the current view currency, likely at the wizard/page level first, then propagate to policies and reports.
4. Update monetary components and pages to bind to the selected currency and render amounts with the correct symbol, separators, and decimals.
5. Update offer entry forms so users can input EUR/USD, see a live PLN equivalent, and still save only PLN to the underlying models.
6. Implement degradation logic: if rates are unavailable, foreign-currency controls disable and screens fall back to PLN with an explanatory banner.
7. Add unit tests around conversion accuracy, formatting, fallback behavior, and the PLN-default rule.

Important constraint from Confluence:
- Because the NBP API is documented, the production path must use the real documented NBP integration as the runtime default. No mock JSON fallback is allowed in production code. Mock data may only be used in tests.

### Task Breakdown

#### Phase 1: Currency model and exchange-rate integration
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Relax money model to support app-level display currency | `src/app/core/models/common/money.model.ts`, `src/app/core/models/index.ts` | Introduce a money display type or extend the existing model so UI state can represent PLN/EUR/USD without breaking persisted PLN storage. |
| 1.2 | Add exchange-rate provider abstraction | `src/app/core/repositories/` or new `src/app/core/services/` files | Create a reusable service/repository that fetches NBP rates and exposes conversion utilities for PLN↔EUR↔USD. |
| 1.3 | Add rate caching and fallback handling | same as above | Cache daily rates and expose a safe “unavailable” state so UI can degrade to PLN-only mode. |
| 1.4 | Add shared currency formatting helpers | `src/app/core/` shared helper file | Centralize symbol/precision/format rules for PLN, EUR, and USD. |

#### Phase 2: View-scoped currency state
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add selected currency state to offer wizard runtime | `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | Store the current view currency, defaulting to PLN, and expose setters/getters to steps. |
| 2.2 | Add currency state to policy and report screens | `src/app/features/policies/pages/policies-home-page.component.ts`, `src/app/features/reports/pages/reports-page.component.ts`, possibly shared service | Mirror the same view-scoped currency pattern so lists and dashboards remain consistent within a screen. |
| 2.3 | Reset currency on view initialization if required by the product rule | page component constructors / route hooks | Ensure each screen starts in PLN unless session persistence is explicitly chosen. |

#### Phase 3: UI updates for viewing and entering money
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Add currency switcher to offer wizard monetary steps | `src/app/features/offer-wizard/pages/variants-step-page.component.html/.ts`, `src/app/features/offer-wizard/pages/crop-variants-step-page.component.*`, `src/app/features/offer-wizard/pages/summary-step-page.component.*`, `src/app/features/offer-wizard/pages/customer-step-page.component.*`, `src/app/features/offer-wizard/pages/vehicle-step-page.component.*` as applicable | Render a single switcher for the current view and bind displayed monetary values to the selected currency. |
| 3.2 | Support foreign-currency offer input with live PLN preview | `src/app/features/offer-wizard/pages/variants-step-page.component.ts/.html` and related money entry templates | Add input currency selection, validation, and helper text showing the PLN equivalent using the current rate. |
| 3.3 | Render policies in selected currency | `src/app/features/policies/pages/policies-home-page.component.html/.ts` | Convert annual/monthly premiums, totals, and detail amounts to the selected view currency. |
| 3.4 | Render reports in selected currency | `src/app/features/reports/pages/reports-page.component.html/.ts` | Convert KPI cards, chart values, and business-line totals uniformly for the chosen currency. |
| 3.5 | Preserve PLN-only styling for unchanged paths | existing templates/styles | Ensure default PLN presentation remains identical when no currency switch is used. |

#### Phase 4: Persistence and testing
| # | Task | Files | Description |
|---|------|-------|-------------|
| 4.1 | Persist PLN-only values in repositories | `src/app/core/repositories/sales-flow-runtime.repository.ts`, offer/policy repositories if needed | Confirm saved offer/policy values remain in PLN regardless of UI display currency. |
| 4.2 | Add unit tests for conversion and formatting | `src/app/**/**/*.spec.ts` new or existing specs | Verify correct conversion math, currency formatting, and fallback behavior. |
| 4.3 | Add component tests for switcher and input flow | relevant page/component spec files | Verify switching currency updates the view and that foreign-currency entry shows PLN preview and saves PLN. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/core/models/common/money.model.ts` | MODIFY | Allow currency-aware UI representation while preserving PLN persistence semantics. |
| `src/app/core/repositories/sales-flow-runtime.repository.ts` | MODIFY | Store view-scoped currency state if shared across sales flow screens. |
| `src/app/core/services/*` or `src/app/core/repositories/*` | CREATE | Add NBP exchange-rate integration and conversion utilities. |
| `src/app/features/offer-wizard/state/offer-wizard-state.service.ts` | MODIFY | Add selected-currency state and helpers for wizard steps. |
| `src/app/features/offer-wizard/pages/*.ts/.html/.scss` | MODIFY | Add switchers, formatting, and foreign-currency input support in wizard screens. |
| `src/app/features/policies/pages/policies-home-page.component.ts/.html/.scss` | MODIFY | Add currency switching and convert displayed policy amounts. |
| `src/app/features/reports/pages/reports-page.component.ts/.html/.scss` | MODIFY | Add currency switching and convert report metrics uniformly. |
| `src/app/core/models/index.ts` | MODIFY | Export any new currency-related types/helpers. |
| `src/app/**/**/*.spec.ts` | MODIFY/CREATE | Add tests for conversion, formatting, and fallback behavior. |

### Verification Steps
1. [ ] Build succeeds.
2. [ ] Unit tests pass.
3. [ ] Currency switcher renders on in-scope views.
4. [ ] Default load is PLN on each in-scope view.
5. [ ] EUR/USD switching recalculates all visible monetary values.
6. [ ] Offer entry in EUR/USD shows live PLN equivalent and persists PLN.
7. [ ] Exchange-rate failure falls back to PLN-only with clear messaging.
8. [ ] No regressions in existing offer, policy, and report flows.