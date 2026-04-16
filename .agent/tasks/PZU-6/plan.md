# PZU-6: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task delivers a "Clear Filters" button on the "Przygotowane oferty" (Prepared Offers) screen. The button allows users to reset all applied filters (search term, status, product) and sorting options to their default values with a single click, restoring the full list of offers. This improves user experience by simplifying filter management.

### User Stories
- US-001: As a sales agent, I want to clear all filters on the offers list with one click, so that I can quickly return to viewing the full portfolio of offers.
  - Given I have applied one or more filters or changed sorting on the offers list
  - When I click the "Wyczyść filtry" button
  - Then all filters and sorting reset to their default values and the full offers list is displayed without page refresh

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" button in the toolbar on the offers home page.
- FR-002: The button is enabled only if at least one filter or sorting option differs from its default value.
- FR-003: Clicking the button resets:
  - searchTerm to ''
  - selectedStatus to 'ALL' or null (default is 'ALL' in current code)
  - selectedProduct to 'ALL'
  - selectedSortField to 'ISSUE_DATE'
  - selectedSortDirection to 'DESC'
- FR-004: After clearing filters, the offers list and offer count update automatically without page reload.
- FR-005: Existing filter, sorting, and offer actions behavior remains unchanged.

### Edge Cases
- EC-001: If filters are already at default values, the button is disabled and clicking does nothing.
- EC-002: Clearing filters while a transition dialog is open should not interfere with the dialog.
- EC-003: If the offers list is empty due to filters, clearing filters shows the full list.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers home page toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing filters.
- [ ] No page reload occurs on clearing filters.
- [ ] No regressions in existing filter or offer actions.

### Open Questions
- None. The requirements and defaults are clearly specified.

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals for reactive state management. The offers home page component (`OffersHomePageComponent`) manages filters as signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`. The offers list is a computed signal filtered by these.

The clear filters button will be added to the existing toolbar in the offers home page HTML template. Its enabled state will be bound to a computed signal that checks if any filter or sorting differs from default values. Clicking the button calls a new method `clearFilters()` that resets all signals to defaults.

This approach follows the existing reactive pattern and UI conventions seen in `offers-home-page.component.ts` and `.html`. The button will use PrimeNG's `pButton` directive consistent with other toolbar buttons.

### Task Breakdown

#### Phase 1: Add Clear Filters Button UI and Logic
| #   | Task                                  | Files                                         | Description                                                                                      |
|-----|-------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------|
| 1.1 | Add "Wyczyść filtry" button to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Insert a button in the toolbar actions area with label "Wyczyść filtry" and PrimeNG styling.    |
| 1.2 | Add computed signal for button enabled state | `src/app/features/offers/pages/offers-home-page.component.ts`    | Create a computed signal that returns true if any filter or sorting differs from default.       |
| 1.3 | Implement `clearFilters()` method to reset filters | `src/app/features/offers/pages/offers-home-page.component.ts`    | Set all filter and sorting signals to their default values as specified in the Jira description.|
| 1.4 | Bind button disabled state to computed signal | `src/app/features/offers/pages/offers-home-page.component.html` | Bind `[disabled]` property of the button to the negation of the computed enabled signal.        |

#### Phase 2: Testing and Verification
| #   | Task                                  | Files                                         | Description                                                                                      |
|-----|-------------------------------------|-----------------------------------------------|------------------------------------------------------------------------------------------------|
| 2.1 | Add unit tests for clearFilters method and button enabled state | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Verify filters reset correctly and button enables/disables properly.                            |
| 2.2 | Manual testing of UI interaction and offers list update | N/A                                           | Verify button visibility, enablement, filter reset, and offers list update without reload.     |

### File Change Summary
| File                                                        | Action | Description                                               |
|-------------------------------------------------------------|--------|-----------------------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add "Wyczyść filtry" button in the toolbar actions area.  |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for button enabled state and implement `clearFilters()` method. Bind button disabled state. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add unit tests for new button and filter reset logic.     |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Unit tests pass including new tests for clear filters functionality.
3. [ ] On offers home page, "Wyczyść filtry" button is visible.
4. [ ] Button is disabled when filters are at default values.
5. [ ] Button is enabled when any filter or sorting differs from default.
6. [ ] Clicking button resets all filters and sorting to defaults.
7. [ ] Offers list and count update immediately after clearing filters, no page reload.
8. [ ] Existing filter and offer actions continue to work as before.