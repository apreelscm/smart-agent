# SDLC-70: na ekranie ofert, zmien przycisk Wyczyść na Wyczyść wszystko

## Specification

### Overview
This task updates the clear-filters button label on the offers screen from `Wyczyść` to `Wyczyść wszystko` without changing the button’s placement, styling, enabled/disabled logic, or clear-all behavior. The scope is limited to the offers list UI and its regression coverage.

### User Stories
- US-001: As a sales agent, I want to see the clear-filters button labeled `Wyczyść wszystko` on the offers screen, so that the wording matches the expected interface vocabulary.
  - Given I open the offers screen
  - When the toolbar is rendered
  - Then the clear-filters button is displayed with the label `Wyczyść wszystko`

- US-002: As a sales agent, I want the `Wyczyść wszystko` button to keep resetting all active filters, so that I can quickly return to the default offers list view.
  - Given I changed at least one filter or sorting option on the offers screen
  - When I click the `Wyczyść wszystko` button
  - Then the same clear-all action previously available under `Wyczyść` is executed

### Functional Requirements
- FR-001: Change the offers-screen clear-filters button label from `Wyczyść` to `Wyczyść wszystko`.
- FR-002: Keep the button in the existing toolbar actions area in `offers-home-page.component.html`.
- FR-003: Preserve the existing click binding to `clearAllFilters()`.
- FR-004: Preserve the existing disabled state driven by `filtersChanged()`.
- FR-005: Do not change filtering, sorting, navigation, or data-loading logic in `OffersHomePageComponent`.
- FR-006: Update automated UI regression coverage to assert the new label and unchanged clear-all behavior.
- FR-007: Ensure the old label `Wyczyść` is no longer used for this specific offers-screen clear button.

### Edge Cases
- EC-001: When no filters are active, the `Wyczyść wszystko` button remains disabled exactly as today.
- EC-002: When the search filter produces an empty state, clicking `Wyczyść wszystko` restores the default results list and count.
- EC-003: When the page is opened through the root redirect (`/` to `/offers`), the button still renders with the label `Wyczyść wszystko`.
- EC-004: Other Polish labels on the offers page, including empty-state text and action menus, remain unchanged.

### Success Criteria
- [ ] On the offers screen, the clear-filters button label is `Wyczyść wszystko`
- [ ] The previous label `Wyczyść` no longer appears for that button
- [ ] Clicking `Wyczyść wszystko` still resets search, status, product, sort field, and sort direction to defaults
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] Automated e2e coverage validates both the new label and the preserved clear-all interaction

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, signals, and computed state. The offers list screen is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`

Current code inspection shows:
- the clear button already exists in `.toolbar__actions`
- it is bound to `clearAllFilters()`
- its disabled state is driven by `filtersChanged()`
- Playwright regression coverage already exists in `tests/e2e/offers-filters.spec.ts`
- `/` redirects to `/offers` via `src/app/app.routes.ts`

That means this Jira should be implemented as a minimal presentation and test-alignment change:
1. Update the button `label` in `offers-home-page.component.html` from `Wyczyść` to `Wyczyść wszystko`.
2. Leave `offers-home-page.component.ts` unchanged, because `clearAllFilters()` already resets:
   - `searchTerm`
   - `selectedStatus`
   - `selectedProduct`
   - `selectedSortField`
   - `selectedSortDirection`
3. Update the existing Playwright tests in `tests/e2e/offers-filters.spec.ts` to assert `Wyczyść wszystko` instead of `Wyczyść` while preserving the behavioral regression check.

Confluence findings:
- Domain context found: [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) confirms the offers screen is part of the sales workflow, but it does not add extra constraints beyond this UI-label change.
- No existing service/API relevant to this UI-only task was found in the `Services` space — no service reuse applies.

This approach follows the existing codebase pattern of hard-coded UI labels in Angular templates and focused Playwright coverage in `tests/e2e/offers-filters.spec.ts`. No backend, repository, mock-runtime, or routing changes are needed.

### Task Breakdown

#### Phase 1: Update the offers toolbar label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Confirm the current clear button location | `src/app/features/offers/pages/offers-home-page.component.html` | Verify the button in `.toolbar__actions` is the one wired to `clearAllFilters()` and controlled by `filtersChanged()`. |
| 1.2 | Change the visible button label | `src/app/features/offers/pages/offers-home-page.component.html` | Replace `label="Wyczyść"` with `label="Wyczyść wszystko"` without changing the click handler, class, inline style, or disabled binding. |
| 1.3 | Keep component logic unchanged | `src/app/features/offers/pages/offers-home-page.component.ts` | Reuse the existing `clearAllFilters()` implementation and avoid any logic changes, since the Jira is presentation-only. |

#### Phase 2: Align regression tests
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update label assertion in e2e test | `tests/e2e/offers-filters.spec.ts` | Change selectors and assertions from `Wyczyść` to `Wyczyść wszystko` in the label-verification scenario. |
| 2.2 | Preserve clear-all behavior coverage | `tests/e2e/offers-filters.spec.ts` | Keep the existing flow that activates a filter, clicks the clear button, and verifies the default offers state is restored. |
| 2.3 | Verify old label is absent | `tests/e2e/offers-filters.spec.ts` | Update the negative assertion so the previous `Wyczyść` label is not present for the offers clear button anymore. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the offers clear-filters button label from `Wyczyść` to `Wyczyść wszystko`. |
| `tests/e2e/offers-filters.spec.ts` | MODIFY | Update Playwright assertions to the new label while preserving regression coverage for filter reset behavior. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Running `ng build` completes without template or type errors
5. [ ] Running `playwright test tests/e2e/offers-filters.spec.ts` passes with the updated label expectations
6. [ ] Manual check on `/offers` confirms the button shows `Wyczyść wszystko` and still resets filters correctly

## Revision History
- Revision 1
  - Reviewer: apreeldev
  - Summary of changes made: Updated the target button label throughout the plan from `Clear` to `Wyczyść wszystko`, including requirements, implementation steps, tests, and verification notes.