# PZU-3: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task delivers a "Clear Filters" button on the "Przygotowane oferty" (Prepared Offers) screen. The button will reset all filter fields and sorting to their default values and refresh the offers list accordingly without a page reload. This improves user experience by allowing sales agents to quickly reset filters and view the full offers portfolio.

### User Stories
- US-001: As a sales agent, I want to clear all filters and sorting with one click, so that I can quickly return to the full list of offers.
  - Given that I have applied one or more filters or changed sorting on the offers list,
  - When I click the "Wyczyść filtry" button,
  - Then all filters and sorting reset to default values and the full list of offers is displayed immediately.

### Functional Requirements
- FR-001: Add a "Wyczyść filtry" (Clear Filters) button in the toolbar on the offers list page.
- FR-002: The button is enabled only if at least one filter or sorting is different from the default values.
- FR-003: Clicking the button resets the following filter and sorting fields:
  - searchTerm = ''
  - selectedStatus = 'ALL' or null
  - selectedProduct = 'ALL'
  - selectedSortField = 'ISSUE_DATE'
  - selectedSortDirection = 'DESC'
- FR-004: After clearing filters, the offers list and offer count update automatically without page reload.
- FR-005: Existing filter, sorting, and offer actions behavior remain unchanged.

### Edge Cases
- EC-001: If no filters or sorting have been changed from defaults, the "Wyczyść filtry" button is disabled.
- EC-002: If the user clears filters while the offers list is empty due to filters, the list updates to show all offers.
- EC-003: Clearing filters does not affect any other UI state or navigation.

### Success Criteria
- [ ] "Wyczyść filtry" button is visible on the offers list toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking the button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing filters.
- [ ] No page reload occurs on clearing filters.
- [ ] Existing filter and sorting functionality is unaffected.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals and computed properties for reactive state management. The offers list and filters are managed in `OffersHomePageComponent` (`src/app/features/offers/pages/offers-home-page.component.ts`). Filters are stored as signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`.

The plan is to:
- Add a green "Wyczyść filtry" button in the toolbar HTML (`offers-home-page.component.html`).
- Implement a computed signal or method to detect if filters/sorting differ from defaults to control button enabled state.
- Implement a method `clearFilters()` in the component that resets all filter signals to their default values.
- Bind the button click to `clearFilters()`.
- The existing computed `filteredOffers` and `totalVisibleOffers` will automatically update due to reactive signals.
- Follow existing UI and code patterns for buttons and toolbar layout.

### Task Breakdown

#### Phase 1: Add Clear Filters Button and Logic
| #   | Task                             | Files                                         | Description                                                                                   |
|-----|---------------------------------|-----------------------------------------------|-----------------------------------------------------------------------------------------------|
| 1.1 | Add green "Wyczyść filtry" button     | `src/app/features/offers/pages/offers-home-page.component.html` | Add button in toolbar actions area, styled consistently with existing buttons but with green color.                |
| 1.2 | Add computed property for button enabled state | `src/app/features/offers/pages/offers-home-page.component.ts` | Create a computed signal that returns true if any filter or sort differs from default values.  |
| 1.3 | Implement `clearFilters()` method | `src/app/features/offers/pages/offers-home-page.component.ts` | Reset all filter and sorting signals to their default values as per requirements.             |
| 1.4 | Bind button enabled state and click handler | `src/app/features/offers/pages/offers-home-page.component.html` | Bind button disabled attribute to computed property and click event to `clearFilters()`.      |
| 1.5 | Test filter clearing updates list | Manual and automated tests                    | Verify offers list and count update immediately and no page reload occurs.                    |

### File Change Summary
| File                                                         | Action | Description                                  |
|--------------------------------------------------------------|--------|----------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add green "Wyczyść filtry" button to toolbar       |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed for button enabled state and `clearFilters()` method |

### Verification Steps
1. [ ] Build project successfully.
2. [ ] Manual test: Apply filters and sorting, verify "Wyczyść filtry" button enables.
3. [ ] Click "Wyczyść filtry" button, verify filters reset to defaults and full offers list shown.
4. [ ] Verify offers count updates correctly after clearing filters.
5. [ ] Verify no page reload occurs on clearing filters.
6. [ ] Run existing unit tests to ensure no regressions.
7. [ ] Add unit tests if applicable to cover new button enabled state and clearFilters logic.

## Revision History
| Revision | Reviewer     | Summary of changes made                      |
|----------|--------------|----------------------------------------------|
| 1        | matlipinski  | Changed "Wyczyść filtry" button color to green as requested |