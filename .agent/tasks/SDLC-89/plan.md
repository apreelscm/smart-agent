# SDLC-89: Na liście ofert brakuje kolumny z okresem ochrony.

## Specification

### Overview
This task completes the offers-list presentation so every offer row shows **„Okres ochrony”** on the `/offers` screen. In this repository the list is rendered as a card-style metadata grid, so the requested “column” maps to a new metadata cell inside `src/app/features/offers/pages/offers-home-page.component.html`.

The value must be calculated dynamically from the current system date using the CET business context, formatted as `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`, identical for all offers rendered at the same time, and placed before **„Aktualizacja”**.

### User Stories
- US-001: As a user viewing the offers list, I want to see the protection period for each offer, so that I can verify the coverage window without opening offer details.
  - Given I have access to the offers list
  - When the offers page is rendered
  - Then each offer row shows `Okres ochrony`

- US-002: As a user comparing offers, I want the protection period to be calculated consistently from the same current business date, so that all rows are comparable.
  - Given multiple offers are visible on the list
  - When the page computes the coverage period
  - Then every row shows the same period label derived from the same current system date

- US-003: As a business user, I want the protection period shown before the update date, so that the row metadata follows the expected order.
  - Given an offer row is displayed
  - When metadata cells are rendered
  - Then `Okres ochrony` appears before `Aktualizacja`

### Functional Requirements
- FR-001: The offers list must render a new metadata field labeled `Okres ochrony` for every visible offer row.
- FR-002: The displayed value must be calculated dynamically from the current system date in the CET business context, not read from persisted offer data.
- FR-003: The period start must be the current business date at `00:00`.
- FR-004: The period end must be the day before the yearly anniversary at `23:59`.
- FR-005: The displayed format must be exactly `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- FR-006: The same computed period value must be reused for all offers rendered in one page view.
- FR-007: `Okres ochrony` must be rendered immediately before `Aktualizacja` in the offer-row metadata grid.
- FR-008: No additional visibility or role-based rules may be introduced; any user who can access the offers list must see the field.
- FR-009: The implementation must reuse the existing shared coverage-period utility rather than duplicate date-calculation logic in the template or repository layer.
- FR-010: No backend contract, repository payload, or `Offer` model persistence change is required for this task.

### Edge Cases
- EC-001: If the UTC date differs from the CET/Poland business date around midnight, the displayed period must still use the business date, not raw UTC.
- EC-002: If the current business date is leap day, the end date must resolve to the day before the rolled yearly anniversary and still end at `23:59`.
- EC-003: At year rollover, the format and year transition must remain correct.
- EC-004: On narrow layouts where the metadata grid collapses, `Okres ochrony` must still precede `Aktualizacja` in DOM/render order.
- EC-005: If filtering returns zero results, the empty state must remain unchanged.
- EC-006: Existing actions, transitions, filtering, sorting, and navigation on the offers page must remain unaffected.

### Success Criteria
- [ ] `/offers` shows `Okres ochrony` on every offer row.
- [ ] The value matches the exact format `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`.
- [ ] The start is displayed as current business date `00:00`.
- [ ] The end is displayed as the day before the yearly anniversary at `23:59`.
- [ ] All visible offers show the same period label during one page render.
- [ ] `Okres ochrony` is positioned before `Aktualizacja`.
- [ ] Unit and e2e tests cover formatting and placement without regressions.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application built with standalone components, PrimeNG, and signal-based view state. The offers list is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The codebase already contains a reusable date-calculation helper in `src/app/features/offers/utils/coverage-period.util.ts` plus boundary-focused unit tests in `src/app/features/offers/utils/coverage-period.util.spec.ts`. The plan should **reuse that utility** and expose its formatted label through the offers page component using the same `computed(...)` pattern already used there for filters, summaries, and derived lists.

Current repository state shows a **partial implementation**:
- the template already references `coveragePeriodLabel()` and renders `Okres ochrony` before `Aktualizacja`
- dedicated util and test files already exist
- component/e2e specs already target this feature
- but `OffersHomePageComponent` does not yet wire in the coverage-period signal, so the page logic is incomplete

Implementation should therefore focus on completing the component/view-model integration and stabilizing the presentation.

Reuse and constraints from discovery:
- Domain context found in Confluence: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), confirming the offers screen is part of the smart-agent sales workflow and should be enhanced without changing surrounding behavior.
- No existing offer-related service, endpoint, or integration reference was found in the configured `Services` space — proposing no backend or external integration work for this ticket.

Key implementation decisions:
1. **Use the existing shared utility** `getCoveragePeriodLabel(...)` from `coverage-period.util.ts`.
2. **Expose one component-level computed signal** such as `coveragePeriodLabel = computed(() => getCoveragePeriodLabel())` so all rows use the same value for a given render.
3. **Keep the calculation in the presentation layer**, because Jira explicitly says the value does not need to be stored on the offer model.
4. **Represent CET in code via the existing IANA timezone approach** already used by the utility (`Europe/Warsaw`), which is the correct runtime-compatible form for browser date formatting APIs.
5. **Keep mocks out of scope for runtime design**: this ticket does not introduce a new data source and should not add persisted/mock coverage values to offers.

### Task Breakdown

#### Phase 1: Complete coverage-period view-model wiring
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Import shared coverage util into offers page | `src/app/features/offers/pages/offers-home-page.component.ts` | Add the missing import for `getCoveragePeriodLabel` from `../utils/coverage-period.util`. |
| 1.2 | Expose a single computed coverage label | `src/app/features/offers/pages/offers-home-page.component.ts` | Add a component-level `computed` signal that returns the formatted protection period once per page state, ensuring all rows use the same current business date. |
| 1.3 | Keep calculation out of the data model | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/core/models/offer/offer.model.ts`, `src/app/core/repositories/offers.repository.ts` | Do not add persisted fields or repository mapping; keep the feature as derived UI state only. |

#### Phase 2: Finalize rendering and layout
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Confirm metadata block placement | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `Okres ochrony` rendered before `Aktualizacja` inside `.offer-row__meta-grid`, bound to the new component signal. |
| 2.2 | Ensure long formatted value remains readable | `src/app/features/offers/pages/offers-home-page.component.scss` | Add or refine styles for `.offer-row__meta-item--coverage` / `.offer-row__coverage-value` only if needed so the full date range wraps or aligns cleanly across desktop and responsive breakpoints. |

#### Phase 3: Harden regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Keep component spec aligned with final view model | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify the component renders the exact expected coverage label and that `Okres ochrony` stays before `Aktualizacja`. Extend the spec if needed to assert the value is consistent across multiple rows. |
| 3.2 | Keep utility boundary coverage green | `src/app/features/offers/utils/coverage-period.util.spec.ts` | Reuse existing tests for CET/Warsaw date selection, leap-day handling, and year rollover; only adjust if implementation details change. |
| 3.3 | Validate browser-level behavior | `tests/e2e/offers-coverage-period.spec.ts` | Ensure Playwright checks the rendered format and field order for visible rows on the actual offers page. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.ts` | MODIFY | Wire the existing coverage-period utility into the offers page via a computed signal shared by all rows. |
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Confirm the `Okres ochrony` metadata cell is rendered with the computed value before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Refine coverage-period field styling if required for readability and responsive layout. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Keep/add regression assertions for exact value and field order. |
| `src/app/features/offers/utils/coverage-period.util.spec.ts` | MODIFY | Preserve or extend date-boundary coverage if final utility usage changes. |
| `tests/e2e/offers-coverage-period.spec.ts` | MODIFY | Verify final browser-visible order and formatting on `/offers`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `ng build` completes without template/type errors in `OffersHomePageComponent`
5. [ ] `ng test --watch=false` passes including `coverage-period.util.spec.ts` and `offers-home-page.component.spec.ts`
6. [ ] `npx playwright test tests/e2e/offers-coverage-period.spec.ts` passes
7. [ ] On `/offers`, each row shows `Okres ochrony` before `Aktualizacja`
8. [ ] The rendered value matches `yyyy/MM/dd HH:mm - yyyy/MM/dd HH:mm`
9. [ ] Multiple offers on the same page render show the same coverage-period label
10. [ ] Empty-state behavior remains unchanged after filtering to zero results