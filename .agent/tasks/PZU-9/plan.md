# PZU-9: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task adds a "Clear Filters" button to the toolbar on the "Przygotowane oferty" (Prepared Offers) screen. The button allows users to reset all filters and sorting options to their default values with a single click, improving usability by avoiding the need to reset each filter individually.

### User Stories
- US-001: As a sales agent, I want to clear all filters on the offers list with one click, so that I can quickly return to viewing the full portfolio of offers.
  - Given the user has applied one or more filters or sorting changes,
  - When the user clicks the "Wyczyść filtry" button,
  - Then all filters and sorting options reset to their default values and the full list of offers is shown.

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" button in the toolbar on the offers home page.
- FR-002: The button is enabled only if at least one filter or sorting option differs from its default value.
- FR-003: Clicking the button resets the following to defaults:
  - searchTerm = ''
  - selectedStatus = 'ALL' (or null if 'ALL' is not used)
  - selectedProduct = 'ALL'
  - selectedSortField = 'ISSUE_DATE'
  - selectedSortDirection = 'DESC'
- FR-004: After clearing filters, the offers list and the offers count update automatically without page reload.
- FR-005: Existing filter, sorting, and offer actions remain unchanged.

### Edge Cases
- EC-001: If no filters are changed from defaults, the "Wyczyść filtry" button is disabled.
- EC-002: If filters are partially changed, the button is enabled.
- EC-003: Clearing filters when no offers are shown should show the full list without errors.
- EC-004: The reset action should not cause any page reload or navigation.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers home page toolbar.
- [ ] Button is disabled when filters/sorting are at default values.
- [ ] Button is enabled when any filter/sorting differs from default.
- [ ] Clicking the button resets all filters/sorting to defaults.
- [ ] Offers list and count update immediately after clearing filters.
- [ ] No regressions in existing filtering, sorting, or offer actions.

### Open Questions
- None identified; requirements and defaults are clear.

---

## Implementation Plan

### Technical Approach
The project uses Angular with signals and computed properties for reactive state management in the offers home page component (`OffersHomePageComponent`). Filters and sorting are managed via signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`.

The plan is to:
- Add a button labeled "Wyczyść filtry" in the toolbar HTML (`offers-home-page.component.html`).
- Add a computed signal `filtersChanged` in the component TypeScript to determine if any filter/sort differs from defaults.
- Bind the button's disabled state to `!filtersChanged`.
- On button click, call a new method `clearAllFilters()` that sets all filter signals to their default values.
- The existing computed `filteredOffers` and `totalVisibleOffers` will automatically update due to reactive signals.
- Follow existing UI and code style patterns for consistency.

### Task Breakdown

#### Phase 1: Add Clear Filters Button and Logic
| #   | Task                        | Files                                        | Description                                                                                       |
|-----|-----------------------------|----------------------------------------------|-------------------------------------------------------------------------------------------------|
| 1.1 | Add "Wyczyść filtry" button | `src/app/features/offers/pages/offers-home-page.component.html` | Add button in toolbar next to existing filters and actions.                                     |
| 1.2 | Add computed signal to detect filter changes | `src/app/features/offers/pages/offers-home-page.component.ts` | Add `filtersChanged` computed signal comparing current filters/sorting to defaults.             |
| 1.3 | Implement clearAllFilters() method | `src/app/features/offers/pages/offers-home-page.component.ts` | Method to reset all filter and sort signals to default values.                                  |
| 1.4 | Bind button disabled state and click handler | `offers-home-page.component.html` and `.ts` | Bind button disabled to `!filtersChanged` and click to `clearAllFilters()`.                     |
| 1.5 | Test reactive update of offers list and count | `offers-home-page.component.ts` | Verify that clearing filters updates the filtered offers and count without page reload.        |

### File Change Summary
| File                                                         | Action | Description                                  |
|--------------------------------------------------------------|--------|----------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add "Wyczyść filtry" button in toolbar UI.  |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for filter changes, clearAllFilters() method, and button binding. |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] "Wyczyść filtry" button is visible on offers page toolbar.
3. [ ] Button is disabled when filters/sorting are default.
4. [ ] Button is enabled when any filter/sorting is changed.
5. [ ] Clicking button resets filters/sorting to defaults.
6. [ ] Offers list and count update immediately after clearing filters.
7. [ ] No regressions in filtering, sorting, or offer actions.