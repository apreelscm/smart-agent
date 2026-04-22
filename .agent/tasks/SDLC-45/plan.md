# SDLC-45: Zmiana label przycisku Wyczyść wszystkie filtry

## Specification

### Overview
This task updates the copy of the clear-filters action on the offers list page so the button currently labeled `Wyczyść wszystkie filtry` is shortened to `Wyczyść`. The change is limited to the offers page UI and should not alter any existing filtering behavior, button state, or layout logic.

### User Stories
- US-001: As a sales user browsing the offers list, I want the clear-filters action to be labeled `Wyczyść`, so that the toolbar uses the requested shorter wording.
  - Given I am on the offers list page and the clear-filters action is visible / When the page renders / Then the button label is `Wyczyść`.

### Functional Requirements
- FR-001: Update the clear-filters button label on the offers list page from `Wyczyść wszystkie filtry` to `Wyczyść`.
- FR-002: Keep the existing button behavior unchanged:
  - it still calls `clearAllFilters()`,
  - it remains disabled when `filtersChanged()` is false,
  - it stays in the same toolbar action area.
- FR-003: Do not change any filtering, sorting, currency, navigation, or dialog behavior as part of this task.
- FR-004: Add or update automated coverage to assert the new button label on the offers page.

### Edge Cases
- EC-001: When no filters are active, the button should remain disabled exactly as today; only the visible label changes.
- EC-002: When filters are active, clicking `Wyczyść` must still reset search, status, product, sort field, and sort direction exactly as implemented today.
- EC-003: The shorter label must not require layout changes; the button should continue rendering correctly in the existing toolbar.

### Success Criteria
- [ ] The offers page shows the clear-filters button label as `Wyczyść`.
- [ ] The button remains wired to the existing clear-filters behavior.
- [ ] No other UI text or behavior on the offers page changes.
- [ ] Automated test coverage verifies the updated label.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a localized UI copy change in the existing standalone Angular offers page component.

Relevant codebase patterns:
- `src/app/features/offers/pages/offers-home-page.component.ts` holds the page state and the existing `clearAllFilters()` behavior.
- `src/app/features/offers/pages/offers-home-page.component.html` defines the toolbar actions, including the current clear-filters button.
- `src/app/features/offers/pages/offers-home-page.component.spec.ts` already uses `TestBed` with the component directly for offers-page regression coverage.

Implementation strategy:
1. Update only the button label in `offers-home-page.component.html`, keeping:
   - `class="clear-filters-button"`,
   - `[disabled]="!filtersChanged()"`,
   - `(click)="clearAllFilters()"`,
   - existing placement inside `.toolbar__actions`.
2. Do not modify component logic in `offers-home-page.component.ts`, because the Jira request is copy-only and the current reset behavior already exists.
3. Add a focused regression test in `offers-home-page.component.spec.ts` that asserts the clear-filters control exposes the requested `label="Wyczyść"` value in the rendered template.
4. Do not change styles unless manual verification shows a regression; the current button has no fixed-width styling in `src/app/features/offers/pages/offers-home-page.component.scss`, so a shorter label should fit without SCSS updates.

Confluence discovery outcome:
- No relevant existing service/API documentation was found in the configured `Services` space for this UI-label task.
- No relevant domain guidance was found in the configured `SDLC` space.
- Therefore, the plan reuses only existing in-repo UI patterns and does not introduce any new integration.

### Task Breakdown

#### Phase 1: Update offers page button copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change clear button label | `src/app/features/offers/pages/offers-home-page.component.html` | Replace the current PrimeNG button `label` value from `Wyczyść wszystkie filtry` to `Wyczyść` without changing its click handler, disabled state, styling, or placement. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add label assertion test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add a unit test that queries `.clear-filters-button` and verifies the rendered template exposes the updated `label` attribute/value as `Wyczyść`. |
| 2.2 | Guard existing behavior scope | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the new test narrow so it validates the label change without rewriting unrelated offers-page tests. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the clear-filters button label to `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression coverage for the updated button label. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open the offers page and confirm the button label is `Wyczyść`
5. [ ] Confirm the button still clears all filters when enabled
6. [ ] Confirm the button remains disabled when no filters are changed
7. [ ] Confirm no other toolbar controls or offers-page behaviors changed