# SDLC-34: Zmiana nazwy przycisku

## Specification

### Overview
This task updates the offers list toolbar copy so the existing button label changes from `Wyczyść wszystkie filtry` to `Wyczyść`. The goal is to align the UI text with the Jira request without changing the button’s current behavior, placement, enable/disable logic, or filter reset scope.

### User Stories
- US-001: As a sales agent, I want the filter reset button to have a shorter label, so that the toolbar is clearer and more concise.
  - Given the offers list page is open
  - When the toolbar is rendered
  - Then the filter reset button label is shown as `Wyczyść`

### Functional Requirements
- FR-001: The button in the offers toolbar currently responsible for resetting filters must display the label `Wyczyść`.
- FR-002: The existing button behavior must remain unchanged:
  - it stays in the same toolbar action area,
  - it remains disabled when no filters/sort differ from defaults,
  - it continues to call the existing reset handler.
- FR-003: No other button labels, filter controls, or offer actions may be changed as part of this task.

### Edge Cases
- EC-001: If the button is disabled, it must still display the updated label `Wyczyść`.
- EC-002: If the toolbar wraps on smaller widths, the shorter label must not break existing layout behavior.
- EC-003: Any tests or selectors that assert the old full label must be updated to the new text.

### Success Criteria
- [ ] The offers page toolbar shows `Wyczyść` instead of `Wyczyść wszystkie filtry`
- [ ] Clicking the button still resets all filters and sorting to defaults
- [ ] The button enabled/disabled state still follows `filtersChanged()`
- [ ] No regressions in offers filtering, sorting, or toolbar actions

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, signals, and PrimeNG. The relevant implementation already exists in `src/app/features/offers/pages/offers-home-page.component.ts` and `src/app/features/offers/pages/offers-home-page.component.html`.

The current offers page already contains:
- reactive filter state via signals such as `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`,
- a computed `filtersChanged` signal,
- a `clearAllFilters()` method,
- a toolbar action button in the template with label `Wyczyść wszystkie filtry`.

This task is a copy-only UI update. The implementation should modify only the template label string in `offers-home-page.component.html`, keeping the existing click handler, disabled binding, CSS class, and inline styling untouched.

Confluence discovery found no relevant existing service documentation in `Services` and no relevant domain guidance in `SDLC` for this label change, so no external service or domain convention reuse applies here.

### Task Breakdown

#### Phase 1: Update toolbar label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Locate existing clear-filters button | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the current button is the toolbar action using `clearAllFilters()` and `[disabled]="!filtersChanged()"`. |
| 1.2 | Change displayed label text | `src/app/features/offers/pages/offers-home-page.component.html` | Replace `Wyczyść wszystkie filtry` with `Wyczyść` only. |
| 1.3 | Verify no logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm `clearAllFilters()` and `filtersChanged` already satisfy the required behavior and remain unchanged. |
| 1.4 | Check for test impact | `tests/e2e/smoke.spec.ts` | Confirm no existing automated test asserts the old label; add/update tests only if a label assertion exists elsewhere. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the clear-filters button label from the longer text to `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.ts` | REVIEW ONLY | No code change expected; existing reset logic already supports the requested label update. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open the offers page and confirm the toolbar button text is `Wyczyść`
5. [ ] Change any filter or sort option, ensure the button enables, click it, and confirm filters reset as before