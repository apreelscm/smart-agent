# PZU-5: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task adds a "Clear Filters" button on the "Przygotowane oferty" (Prepared Offers) screen. The button allows users to reset all active filters and sorting options to their default values with a single click, improving usability by avoiding the need to reset each filter individually.

### User Stories
- US-001: As a sales agent, I want to clear all filters on the offers list with one click, so that I can quickly return to viewing the full portfolio of offers.
  - Given that I have applied one or more filters or changed sorting on the offers list
  - When I click the "Wyczyść filtry" button
  - Then all filters and sorting revert to their default values and the full list of offers is displayed without page refresh

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" button in the toolbar on the offers list page.
- FR-002: The button is enabled only if at least one filter or sorting option differs from its default value.
- FR-003: Clicking the button resets the following filter and sorting values:
  - searchTerm = ''
  - selectedStatus = 'ALL' (or null if applicable)
  - selectedProduct = 'ALL'
  - selectedSortField = 'ISSUE_DATE'
  - selectedSortDirection = 'DESC'
- FR-004: After clearing filters, the offers list and the offers count update automatically without a page reload.
- FR-005: Existing filter, sorting, and offer actions behavior remains unchanged.

### Edge Cases
- EC-001: If no filters or sorting are changed from defaults, the "Wyczyść filtry" button is disabled.
- EC-002: If filters are partially changed, the button is enabled and clears all filters to defaults.
- EC-003: Clearing filters when no offers match the current filters should show the empty state with the full list after reset.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers list toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking the button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing filters.
- [ ] No page refresh occurs on clearing filters.
- [ ] Existing filter and sorting behavior remains intact.

### Open Questions
- None, requirements and scope are clear.

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals for state management in the offers-home-page component (`src/app/features/offers/pages/offers-home-page.component.ts`). Filters and sorting are managed via Angular signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`.

The "Clear Filters" button will be added to the existing toolbar in the offers-home-page component HTML (`offers-home-page.component.html`). Its enabled state will be computed by comparing current filter/sort signals to their default values.

Clicking the button will trigger a new method in the component that resets all signals to default values, causing the filtered offers list and count to update reactively without page reload.

This approach follows existing patterns of signals and computed properties in the component and integrates seamlessly with the current UI and reactive data flow.

### Task Breakdown

#### Phase 1: Add Clear Filters Button and Logic
| #   | Task                                | Files                                      | Description                                                                                  |
|-----|-----------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------------|
| 1.1 | Add "Wyczyść filtry" button       | `src/app/features/offers/pages/offers-home-page.component.html` | Add button in toolbar next to existing filter controls with binding for click and disabled state. |
| 1.2 | Implement computed property to enable/disable button | `src/app/features/offers/pages/offers-home-page.component.ts` | Add computed signal that checks if any filter or sorting differs from default values.         |
| 1.3 | Implement method to clear filters | `src/app/features/offers/pages/offers-home-page.component.ts` | Add method to reset all filter and sort signals to default values.                            |
| 1.4 | Bind button disabled state and click handler | `offers-home-page.component.html` and `.ts` | Connect button disabled property to computed and click event to clear filters method.        |
| 1.5 | Verify reactive update of offers list and count | `offers-home-page.component.ts` | Confirm that resetting signals updates filteredOffers and totalVisibleOffers computed signals.|

### File Change Summary
| File                                                        | Action | Description                                |
|-------------------------------------------------------------|--------|--------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add "Wyczyść filtry" button to toolbar UI |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for button enabled state and method to reset filters |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] "Wyczyść filtry" button appears on offers list toolbar.
3. [ ] Button is disabled when filters and sorting are at default values.
4. [ ] Button is enabled when any filter or sorting differs from default.
5. [ ] Clicking button resets all filters and sorting to defaults.
6. [ ] Offers list and offers count update immediately after clearing filters.
7. [ ] No page reload occurs on clearing filters.
8. [ ] Existing filter and sorting functionality remains unchanged.
9. [ ] Manual and automated tests verify the above behaviors.