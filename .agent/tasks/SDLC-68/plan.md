# SDLC-68: Na ekranie listy ofert zmien przycisk z 'wyczyść wszystkie filtry' na 'Wyczyść'

## Specification

### Overview
This task updates the clear-filters button label on the offers list screen from `Wyczyść wszystkie filtry` to `Wyczyść` in the Polish UI. The change is presentation-only: the existing clear/reset behavior, button visibility, disabled state, selectors, styling, and surrounding toolbar layout must remain intact.

### User Stories
- US-001: As a user browsing the offers list, I want a shorter clear-filters label, so that the action is easier to scan and understand.
  - Given I am on the offers list screen in the Polish UI
  - When the clear-filters button is displayed
  - Then its visible label is `Wyczyść`
- US-002: As a product owner, I want to simplify interface copy without changing behavior, so that the screen remains consistent and readable.
  - Given one or more filters are applied
  - When I click `Wyczyść`
  - Then all filters are cleared exactly as they are today

### Functional Requirements
- FR-001: Update the offers list clear-filters button label to exactly `Wyczyść`.
- FR-002: Apply the label change only on the offers list screen.
- FR-003: Preserve the existing clear action bound to `clearAllFilters()`.
- FR-004: Preserve the existing disabled-state logic bound to `!filtersChanged()`.
- FR-005: Do not change button class names, selectors, routing, styles, or filter-reset defaults.
- FR-006: Do not modify any other labels or screens.

### Edge Cases
- EC-001: When no filters are changed and the button is disabled, the visible label must still be `Wyczyść`.
- EC-002: In the empty-state view (`Brak ofert dla podanych filtrów`), the toolbar button label must still be `Wyczyść`.
- EC-003: The shorter label must not affect button placement or overflow negatively in desktop or responsive layouts.
- EC-004: Existing automation relying on `.clear-filters-button` must keep working because the class remains unchanged.

### Success Criteria
- [ ] On `/offers`, the clear-filters button shows `Wyczyść`.
- [ ] Clicking the button still resets search, status, product, sort field, and sort direction to their current defaults.
- [ ] The button disabled/enabled behavior remains unchanged.
- [ ] No other screen text is changed.
- [ ] No visual regression is introduced in the offers toolbar.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone app with PrimeNG components. The offers list screen is implemented by `OffersHomePageComponent` and routed from `src/app/app.routes.ts` under `/offers`.

Relevant existing implementation:
- `src/app/features/offers/pages/offers-home-page.component.html`
  - contains the current button markup with `class="clear-filters-button"`
  - currently sets `label="Wyczyść wszystkie filtry"`
- `src/app/features/offers/pages/offers-home-page.component.ts`
  - already contains `clearAllFilters()`
  - already contains `filtersChanged` computed state controlling `[disabled]`

Because the reset logic already exists, this ticket should be implemented as a minimal copy change in the template, not a behavioral refactor. Keep the existing click handler, disabled binding, and CSS class unchanged.

Testing should follow the current repo setup:
- Jasmine/Karma exists (`src/app/app.spec.ts`) but there is no existing offers-page unit test pattern.
- Playwright e2e exists under `tests/e2e/`.

Recommended verification approach:
- Update the button label in the offers template.
- Add a focused Playwright regression test for `/offers` that verifies:
  - the button text is `Wyczyść`
  - after changing a filter/input, clicking the button still resets the screen state

Confluence discovery results:
- No existing service/API relevant to this task was found in `Services`.
- No relevant domain convention or UI/i18n guidance was found in `SDLC`.
- Since no reusable Confluence artifact was found in configured spaces, the plan reuses only existing repository code and patterns.

### Task Breakdown

#### Phase 1: Update offers-list button copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change visible button label | `src/app/features/offers/pages/offers-home-page.component.html` | Replace `label="Wyczyść wszystkie filtry"` with `label="Wyczyść"` on the existing `.clear-filters-button`. |
| 1.2 | Preserve existing behavior bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Ensure `[disabled]="!filtersChanged()"`, `(click)="clearAllFilters()"`, and `class="clear-filters-button"` remain unchanged. |
| 1.3 | Confirm no logic change is needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Validate that the existing `clearAllFilters()` and `filtersChanged` implementation already satisfies acceptance criteria; no code change expected unless testability requires it. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add offers-page e2e test | `tests/e2e/offers-filters.spec.ts` | Create a Playwright test that opens `/offers`, asserts the button text is `Wyczyść`, applies a filter/search change, clicks the button, and verifies the state resets. |
| 2.2 | Keep smoke coverage intact | `tests/e2e/smoke.spec.ts` | Leave the generic smoke test unchanged unless the team prefers consolidating the new assertion into the existing smoke spec. |
| 2.3 | Manual visual check | `src/app/features/offers/pages/offers-home-page.component.html` | Verify the shorter label does not introduce spacing, wrapping, or alignment issues in the toolbar. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the clear-filters button label from `Wyczyść wszystkie filtry` to `Wyczyść` while preserving existing bindings and class name. |
| `tests/e2e/offers-filters.spec.ts` | CREATE | Add regression coverage for the new label and unchanged clear-filters behavior on the offers list. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/offers` and confirm the button text is `Wyczyść`
5. [ ] Change search or filter state and confirm the button becomes enabled as before
6. [ ] Click `Wyczyść` and confirm all filters reset to current defaults
7. [ ] Confirm `.clear-filters-button` still exists and no toolbar layout regression is visible