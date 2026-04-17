# PZU-8: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task delivers a "Clear Filters" button on the "Przygotowane oferty" (Prepared Offers) screen that allows users to reset all applied filters and sorting to their default values with a single click. This improves user experience by enabling quick return to the full offers list without manually resetting each filter or sort field.

### User Stories
- US-001: As a sales agent, I want to clear all filters and sorting on the offers list with one click, so that I can quickly return to viewing the full portfolio of offers.
  - Given that I have applied one or more filters or changed sorting on the offers list,
  - When I click the "Wyczyść filtry" button,
  - Then all filters and sorting reset to their default values and the full list of offers is displayed without page refresh.

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" button in the toolbar on the "Przygotowane oferty" screen.
- FR-002: The button is enabled only if at least one filter or sorting differs from its default value.
- FR-003: Clicking the button resets:
  - searchTerm to ''
  - selectedStatus to 'ALL' or null (prefer 'ALL' as per existing code)
  - selectedProduct to 'ALL'
  - selectedSortField to 'ISSUE_DATE'
  - selectedSortDirection to 'DESC'
- FR-004: After clearing filters, the offers list and the offers count update automatically.
- FR-005: The clearing action happens without refreshing the page.
- FR-006: Existing filter, sorting, and offer actions behavior remain unchanged.

### Edge Cases
- EC-001: If no filters or sorting are changed from defaults, the "Wyczyść filtry" button is disabled.
- EC-002: If the user clicks the button multiple times, the state remains consistent and no errors occur.
- EC-003: If the offers list is empty due to filters, clearing filters restores the full list.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking the button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing.
- [ ] No page reload occurs on clearing filters.
- [ ] Existing filter and sorting functionality remains intact.

### Open Questions
- None. The default values and UI location are clearly specified.

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals and computed properties for reactive state management. The offers page component (`OffersHomePageComponent`) manages filter and sort state using Angular signals (`searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, `selectedSortDirection`). The filtered offers list and count are computed reactively.

We will add a "Wyczyść filtry" button in the toolbar next to existing filter controls. The button's enabled state will be computed by comparing current filter/sort signals to their default values. Clicking the button will call a method to reset all filter and sort signals to defaults, triggering reactive updates to the offers list and count automatically.

The button will be styled with a green color to meet the reviewer's request for a green button. This can be done by applying a CSS class or inline style consistent with the existing UI styling approach.

This approach follows existing patterns in `offers-home-page.component.ts` and the UI uses PrimeNG components and Angular templates.

### Task Breakdown

#### Phase 1: Add Clear Filters Button and Logic
| #   | Task                               | Files                                      | Description                                                                                   |
|-----|----------------------------------|--------------------------------------------|-----------------------------------------------------------------------------------------------|
| 1.1 | Add "Wyczyść filtry" button in toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a green-colored button in the toolbar filters section, styled consistent with existing buttons but with green color.      |
| 1.2 | Add computed signal for button enabled state | `src/app/features/offers/pages/offers-home-page.component.ts` | Implement a computed property that returns true if any filter or sort differs from default.   |
| 1.3 | Implement clearFilters() method to reset filters | `src/app/features/offers/pages/offers-home-page.component.ts` | Reset all filter and sort signals to their default values as specified in the Jira description. |
| 1.4 | Bind button enabled state and click handler | `src/app/features/offers/pages/offers-home-page.component.html` and `.ts` | Bind button disabled/enabled state to computed signal and click event to clearFilters().      |
| 1.5 | Verify reactive updates of offers list and count | N/A | Confirm that clearing filters triggers reactive recomputation of filteredOffers and totalVisibleOffers. |

### File Change Summary
| File                                                        | Action | Description                                  |
|-------------------------------------------------------------|--------|----------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add green "Wyczyść filtry" button in toolbar UI.  |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for button enabled state and implement clearFilters() method. |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Manual testing confirms "Wyczyść filtry" button appears on offers page toolbar.
3. [ ] Button is disabled when filters/sorting are at default values.
4. [ ] Button is enabled when any filter or sorting is changed.
5. [ ] Clicking button resets all filters and sorting to defaults.
6. [ ] Offers list and count update immediately after clearing filters.
7. [ ] No page reload occurs during the operation.
8. [ ] Existing filter and sorting functionality remains unchanged.
9. [ ] Automated unit tests (if present) pass or add tests for the new button behavior.

## Revision History
| Revision | Reviewer    | Summary of changes                          |
|----------|-------------|---------------------------------------------|
| 1        | matlipinski | Added requirement for green color on button |