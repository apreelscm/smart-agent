# SDLC-71: na ekranie ofert, zmien przycisk Wyczyść na Clear

## Specification

### Overview
This task updates the clear-filters button label on the offers screen from `Wyczyść` to `Clear` without changing any existing behavior. The scope is limited to the presentation layer and required regression coverage so the button still clears the same filters, remains in the same place, and keeps the same enabled/disabled behavior.

### User Stories
- US-001: As a sales agent, I want to see the offers-screen clear button labeled `Clear`, so that the UI wording matches the expected text.
  - Given I open the offers screen
  - When the filters toolbar is visible
  - Then the clear-filters button label is `Clear`

- US-002: As a sales agent, I want the `Clear` button to behave exactly like the previous `Wyczyść` button, so that I can still reset the offers filters in one click.
  - Given I changed at least one filter or sorting option on the offers screen
  - When I click `Clear`
  - Then all filters return to their default values and the offers list refreshes as before

### Functional Requirements
- FR-001: Replace the visible label of the offers-screen clear-filters button from `Wyczyść` to `Clear`.
- FR-002: Keep the button in the existing toolbar actions area in `offers-home-page.component.html`.
- FR-003: Preserve the existing click binding to `clearAllFilters()`.
- FR-004: Preserve the existing disabled state controlled by `filtersChanged()`.
- FR-005: Do not change filtering, sorting, navigation, or list rendering logic in `OffersHomePageComponent`.
- FR-006: Update automated e2e coverage to assert the new label `Clear` and the unchanged clear-all behavior.
- FR-007: Ensure the old label `Wyczyść` no longer appears for this button on the offers page.

### Edge Cases
- EC-001: When no filters are changed, the `Clear` button remains disabled exactly as it is today.
- EC-002: When filters reduce the list to zero results, clicking `Clear` restores the default offers list state without reloading or navigating away.
- EC-003: When the app is opened via `/` and redirected to `/offers`, the button label must still render as `Clear`.
- EC-004: No other Polish labels on the offers page should be changed as part of this task.

### Success Criteria
- [ ] The offers toolbar button displays `Clear` instead of `Wyczyść`.
- [ ] Clicking `Clear` still resets search, status, product, sort field, and sort direction to their defaults.
- [ ] The button remains disabled when no filters have changed.
- [ ] The old `Wyczyść` label is no longer used for the offers clear-filters button.
- [ ] Playwright coverage passes with assertions updated to the new label and preserved behavior.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 app using standalone components, signals, computed state, and PrimeNG. The offers list screen is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.ts`

Current code inspection shows the required behavior already exists:
- the clear button is rendered in `.toolbar__actions`
- the button currently has `label="Wyczyść"`
- the button is bound to `(click)="clearAllFilters()"`
- the disabled state is driven by `[disabled]="!filtersChanged()"`
- `clearAllFilters()` already resets:
  - `searchTerm`
  - `selectedStatus`
  - `selectedProduct`
  - `selectedSortField`
  - `selectedSortDirection`

Because of that, implementation should be a minimal UI-text update plus regression-test maintenance:
1. Change the button label in `offers-home-page.component.html` from `Wyczyść` to `Clear`.
2. Leave `offers-home-page.component.ts` unchanged, because the ticket explicitly excludes behavior changes and the existing reset logic already matches the acceptance criteria.
3. Update the existing Playwright spec in `tests/e2e/offers-filters.spec.ts`, which currently asserts `Wyczyść`, so it validates `Clear` instead and still verifies the reset workflow.

Confluence findings:
- Domain context found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the offers screen is part of the sales workflow, but it does not introduce additional constraints for this label-only change.
- No existing service/API relevant to this task was found in the `Services` space — this is a UI-only change, so no service reuse applies.

This plan follows existing codebase patterns:
- inline UI labels in Angular templates rather than centralized translation files
- route redirect from `/` to `/offers` defined in `src/app/app.routes.ts`
- Playwright regression coverage already colocated under `tests/e2e/`

### Task Breakdown

#### Phase 1: Update the offers toolbar label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Locate the clear-filters control | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the existing PrimeNG button in `.toolbar__actions` is the one wired to `clearAllFilters()` and controlled by `filtersChanged()`. |
| 1.2 | Replace the visible label | `src/app/features/offers/pages/offers-home-page.component.html` | Change `label="Wyczyść"` to `label="Clear"` without changing styling, bindings, or placement. |
| 1.3 | Preserve current reset logic | `src/app/features/offers/pages/offers-home-page.component.ts` | Verify no TypeScript changes are needed because `clearAllFilters()` already satisfies the required behavior. |

#### Phase 2: Update regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update label assertion | `tests/e2e/offers-filters.spec.ts` | Replace button-role lookups and visible-label assertions from `Wyczyść` to `Clear`. |
| 2.2 | Keep negative assertion aligned | `tests/e2e/offers-filters.spec.ts` | Update the test so it confirms the old offers-button label `Wyczyść` is no longer present in the covered location. |
| 2.3 | Preserve behavior regression | `tests/e2e/offers-filters.spec.ts` | Keep the existing interaction flow: apply a search filter, verify the button enables, click `Clear`, and assert the search field and result state return to defaults. |
| 2.4 | Validate redirect entry path | `tests/e2e/offers-filters.spec.ts`, `src/app/app.routes.ts` | Continue using `page.goto('/')`, relying on the existing redirect to `/offers`, so the test matches real app entry behavior. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Rename the offers clear-filters button label from `Wyczyść` to `Clear`. |
| `tests/e2e/offers-filters.spec.ts` | MODIFY | Update existing Playwright assertions and selectors to the `Clear` label while preserving reset-behavior checks. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check on `/offers` shows the button label `Clear`
5. [ ] Clicking `Clear` still resets the offers toolbar filters and restores the default list state