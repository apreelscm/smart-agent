# PZU-2: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task adds a "Clear Filters" button to the toolbar on the "Przygotowane oferty" (Prepared Offers) screen. The button allows users to reset all filters and sorting options to their default values with one click, improving usability and efficiency in navigating the offers list.

### User Stories
- US-001: As a sales agent, I want to clear all filters on the offers list with a single click, so that I can quickly return to viewing the full portfolio of offers.
  - Given the user has applied one or more filters or changed sorting on the offers list,
  - When the user clicks the "Wyczyść filtry" button,
  - Then all filters and sorting reset to default values and the full list of offers is displayed without page refresh.

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" button in the toolbar on the offers list page.
- FR-002: The button is enabled only if at least one filter or sorting option differs from its default value.
- FR-003: On clicking the button, reset the following states:
  - searchTerm = ''
  - selectedStatus = 'ALL' or null (default is 'ALL' in current code)
  - selectedProduct = 'ALL'
  - selectedSortField = 'ISSUE_DATE'
  - selectedSortDirection = 'DESC'
- FR-004: After clearing, the offers list and offer count update automatically without page reload.
- FR-005: Existing filter, sorting, and offer actions behavior remain unchanged.

### Edge Cases
- EC-001: If no filters or sorting have been changed (all are default), the "Wyczyść filtry" button is disabled.
- EC-002: If filters are partially changed, the button is enabled and resets all filters and sorting.
- EC-003: Clearing filters does not affect any other UI state or navigation.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers list toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking the button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing.
- [ ] No page refresh occurs.
- [ ] No regressions in existing filter or sorting functionality.

### Open Questions
- None

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals for reactive state management in the offers list component (`OffersHomePageComponent`). Filters and sorting are managed via signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`.

The "Clear Filters" button will be added to the existing toolbar in the offers list HTML template. Its enabled state will be computed by comparing current filter/sort signals to their default values. The button's click handler will reset these signals to their defaults.

This approach follows existing reactive patterns and leverages Angular signals for state management, ensuring immediate UI updates without page reload.

### Task Breakdown

#### Phase 1: Add Clear Filters Button UI and Logic
| #   | Task                                  | Files                                            | Description                                                                                   |
|-----|-------------------------------------|-------------------------------------------------|-----------------------------------------------------------------------------------------------|
| 1.1 | Add "Wyczyść filtry" button to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a button in the toolbar with label "Wyczyść filtry" and bind click event to clear filters. |
| 1.2 | Add computed signal for button enabled state | `src/app/features/offers/pages/offers-home-page.component.ts` | Create a computed signal that returns true if any filter or sorting differs from defaults.     |
| 1.3 | Implement clearFilters() method to reset filters | `src/app/features/offers/pages/offers-home-page.component.ts` | Extend existing `clearFilters()` method to reset all filters and sorting signals to defaults. |
| 1.4 | Bind button disabled state to computed signal | `src/app/features/offers/pages/offers-home-page.component.html` | Disable the button when filters are at default values.                                        |
| 1.5 | Ensure offers list updates reactively | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm that resetting signals triggers offers list recomputation and UI update automatically.|

### File Change Summary
| File                                                      | Action | Description                                      |
|-----------------------------------------------------------|--------|--------------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add "Wyczyść filtry" button to toolbar UI.       |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for button enabled state; implement filter reset logic in `clearFilters()`. |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] "Wyczyść filtry" button is visible on the offers list toolbar.
3. [ ] Button is disabled when no filters or sorting are changed.
4. [ ] Button is enabled when any filter or sorting differs from defaults.
5. [ ] Clicking the button resets filters and sorting to defaults.
6. [ ] Offers list and offer count update immediately after clearing.
7. [ ] No page reload occurs.
8. [ ] Existing filter and sorting functionality remains unchanged.
9. [ ] Automated tests (if any) pass or add tests to cover the new button behavior.

This plan is scoped strictly to adding the "Clear Filters" button and its behavior on the offers list page, following the existing Angular signal-based state management patterns.

## Revision History
| Revision | Reviewer     | Summary of changes made                   |
|----------|--------------|------------------------------------------|
| 1        | matlipinski  | No changes requested; plan confirmed as is |