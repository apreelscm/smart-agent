# SDLC-69: Na ekranie listy ofert zmien przycisk z 'wyczyść wszystkie filtry' na 'Wyczyść'

## Specification

### Overview
This task updates the clear-filters button label on the offers list screen from `Wyczyść wszystkie filtry` to `Wyczyść`, while preserving the current clear-all behavior already implemented in the offers toolbar. The change is limited to the presentation layer and should not alter filtering, sorting, routing, or offer list refresh behavior.

### User Stories
- US-001: As a sales agent, I want to see a shorter clear-filters button label on the offers list, so that the toolbar is simpler and more readable.
  - Given I am on the offers list screen
  - When the filters toolbar is displayed
  - Then I see the button label `Wyczyść`

- US-002: As a sales agent, I want the `Wyczyść` button to keep clearing all active filters, so that I can quickly return to the default offers view.
  - Given I changed at least one filter or sorting option
  - When I click `Wyczyść`
  - Then all filters return to their defaults and the offers list refreshes as it does today

### Functional Requirements
- FR-001: Replace the current clear-filters button text on the offers list screen with `Wyczyść`.
- FR-002: Keep the existing button placement in the toolbar actions area on the offers page.
- FR-003: Preserve the current button behavior wired to `clearAllFilters()` in `OffersHomePageComponent`.
- FR-004: Preserve the current disabled/enabled behavior driven by `filtersChanged()`.
- FR-005: Ensure the old label `Wyczyść wszystkie filtry` no longer appears for the offers-list clear button.
- FR-006: Add regression coverage for the updated label and the unchanged clear-all interaction.

### Edge Cases
- EC-001: When no filters or sorting changes are active, the `Wyczyść` button remains disabled exactly as today.
- EC-002: When the list is empty because of active filters, clicking `Wyczyść` restores the default list state without navigation or reload.
- EC-003: If the toolbar is rendered from the `/offers` route redirect from `/`, the updated label must still be visible.
- EC-004: No other button labels on the offers page should be changed.

### Success Criteria
- [ ] On `/offers`, the clear-filters button label is `Wyczyść`.
- [ ] The previous label `Wyczyść wszystkie filtry` is no longer shown for that button.
- [ ] Clicking the button still resets search, status, product, sort field, and sort direction to their defaults.
- [ ] The offers list and visible count update after clearing filters with no page reload.
- [ ] Automated test coverage validates the new label and the retained clear-all behavior.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, signals, and computed state. The offers list screen is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`

Current code inspection shows this functionality already exists:
- `clearAllFilters()` resets:
  - `searchTerm`
  - `selectedStatus`
  - `selectedProduct`
  - `selectedSortField`
  - `selectedSortDirection`
- `filtersChanged` already controls whether the button is enabled
- the template already renders the clear button in the toolbar actions section

Because of that, this Jira should be implemented as a minimal presentation change, not a logic rewrite:
1. Update the button `label` in `offers-home-page.component.html` from `Wyczyść wszystkie filtry` to `Wyczyść`.
2. Leave `offers-home-page.component.ts` unchanged unless test coverage requires a minor selector aid; no business logic change is planned.
3. Add Playwright regression coverage, following the existing repo testing setup in `tests/e2e/smoke.spec.ts`, to verify:
   - the new label is rendered
   - the button still clears active filters on `/offers`

Confluence findings:
- Domain context found: [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) confirms the application screen landscape and that the offers list is part of the sales-agent workflow, but it does not define any additional UI-label constraints for this task.
- No relevant existing service/API was found in the `Services` space for this UI-only change, so no service reuse applies.

This approach follows the current codebase pattern of inline Polish UI labels in Angular templates rather than a centralized i18n resource file.

### Task Breakdown

#### Phase 1: Update offers-list button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Locate current clear-filters button | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the existing button in `.toolbar__actions` is the one bound to `clearAllFilters()` and `filtersChanged()`. |
| 1.2 | Change visible label text | `src/app/features/offers/pages/offers-home-page.component.html` | Replace `label="Wyczyść wszystkie filtry"` with `label="Wyczyść"` without changing styling, click handler, or disabled binding. |
| 1.3 | Verify no logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Reuse existing `clearAllFilters()` and `filtersChanged` implementation as-is to keep scope limited to presentation. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add offers-page e2e regression test | `tests/e2e/offers-filters.spec.ts` | Create a dedicated Playwright test that navigates to `/offers`, asserts the `Wyczyść` button is present, and confirms the old label is absent. |
| 2.2 | Validate unchanged clear-all behavior | `tests/e2e/offers-filters.spec.ts` | Apply a filter via the search input, verify the clear button becomes enabled, click `Wyczyść`, then assert the input resets and the full offers state returns. |
| 2.3 | Keep existing smoke coverage intact | `tests/e2e/smoke.spec.ts` | Leave the generic app-load smoke test unchanged unless the new spec reveals a need for shared helper reuse. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the clear-filters button label from `Wyczyść wszystkie filtry` to `Wyczyść`. |
| `tests/e2e/offers-filters.spec.ts` | CREATE | Add Playwright regression coverage for the updated label and preserved clear-all behavior on the offers page. |

### Verification Steps
1. [ ] Build succeeds with `ng build`
2. [ ] Tests pass with `playwright test`
3. [ ] New tests cover requirements for the label update and unchanged clear behavior
4. [ ] Manual check on `/offers` shows the button label as `Wyczyść`
5. [ ] Manual interaction confirms clearing filters still restores the default offers list state