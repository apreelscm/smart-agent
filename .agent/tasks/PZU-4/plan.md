# PZU-4: Dodanie przycisku „Wyczyść filtry” na liście ofert

## Specification

### Overview
This task delivers a "Clear Filters" button on the "Prepared Offers" screen that resets all active filters and sorting to their default values with one click. This improves user experience by allowing sales agents to quickly return to the full offers list without manually resetting each filter.

### User Stories
- US-001: As a sales agent, I want to clear all filters on the offers list with one click, so that I can quickly return to the full portfolio of offers.
  - Given the user has applied one or more filters or changed sorting on the offers list,
  - When the user clicks the "Clear Filters" button,
  - Then all filters and sorting are reset to default values and the full offers list is displayed automatically without page refresh.

### Functional Requirements
- FR-001: Add a "Clear Filters" button in the toolbar on the "Prepared Offers" screen.
- FR-002: The button is only enabled if at least one filter or sorting differs from its default value.
- FR-003: Clicking the button resets the following filters and sorting:
  - searchTerm = ''
  - selectedStatus = 'ALL' or null (default is 'ALL' in code)
  - selectedProduct = 'ALL'
  - selectedSortField = 'ISSUE_DATE'
  - selectedSortDirection = 'DESC'
- FR-004: After clearing filters, the offers list and offer count update automatically.
- FR-005: The solution works without page reload.
- FR-006: Existing filter, sorting, and offer actions behavior remains unchanged.

### Edge Cases
- EC-001: If no filters or sorting are changed from defaults, the button is disabled.
- EC-002: If filters are partially changed, the button is enabled.
- EC-003: Clearing filters when no offers are shown should show the full list.
- EC-004: Clearing filters should not affect other UI states or navigation.

### Success Criteria
- [ ] "Clear Filters" button is visible on the "Prepared Offers" screen toolbar.
- [ ] Button is enabled only when filters or sorting differ from defaults.
- [ ] Clicking the button resets all filters and sorting to defaults.
- [ ] Offers list and count update immediately after clearing filters.
- [ ] No page reload occurs on clearing filters.
- [ ] Existing filter and sorting functionality remains unchanged.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The project is an Angular 20 application using signals and computed properties for reactive UI state. The offers list component is `OffersHomePageComponent` (`src/app/features/offers/pages/offers-home-page.component.ts`) with filters as signals: `searchTerm`, `selectedStatus`, `selectedProduct`, `selectedSortField`, and `selectedSortDirection`. The offers list is filtered reactively via a computed property `filteredOffers`.

The toolbar with filters is defined in the component's HTML template (`offers-home-page.component.html`). The "Clear Filters" button will be added next to existing filters in the toolbar.

The button's enabled state will be computed by comparing current filter signals to their default values. On click, a new method `clearAllFilters()` will reset all filter signals to their defaults, triggering reactive updates to the offers list and count.

This approach follows existing code patterns, uses Angular signals and computed properties, and requires minimal changes localized to the offers home page component and template.

### Task Breakdown

#### Phase 1: Add Clear Filters Button and Logic
| #   | Task                                  | Files                                           | Description                                                                                   |
|-----|-------------------------------------|------------------------------------------------|-----------------------------------------------------------------------------------------------|
| 1.1 | Add "Clear Filters" button to toolbar | `src/app/features/offers/pages/offers-home-page.component.html` | Add a button with label/icon in the toolbar next to filters.                                  |
| 1.2 | Add computed signal for button enabled state | `src/app/features/offers/pages/offers-home-page.component.ts` | Create a computed signal that returns true if any filter or sort differs from default.        |
| 1.3 | Implement `clearAllFilters()` method | `src/app/features/offers/pages/offers-home-page.component.ts` | Reset all filter signals to default values on button click.                                   |
| 1.4 | Bind button enabled state and click handler | `src/app/features/offers/pages/offers-home-page.component.html` | Bind button's `[disabled]` and `(click)` to the computed signal and clear method.             |
| 1.5 | Verify offers list and count update reactively | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm reactive filtering updates automatically on signals reset.                            |
| 1.6 | Add unit tests for new button and logic | Create or extend test file for `OffersHomePageComponent` | Test button visibility, enabled state, click resets filters, and reactive list update.       |

### File Change Summary
| File                                                      | Action | Description                                         |
|-----------------------------------------------------------|--------|-----------------------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Add "Clear Filters" button to toolbar with bindings |
| `src/app/features/offers/pages/offers-home-page.component.ts`   | MODIFY | Add computed signal for button enabled state, implement clearAllFilters method |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` (if exists) | MODIFY/CREATE | Add tests for clear filters button and functionality |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] "Clear Filters" button is visible on the offers home page toolbar.
3. [ ] Button is disabled when no filters or sorting are changed.
4. [ ] Button is enabled when any filter or sorting differs from default.
5. [ ] Clicking the button resets all filters and sorting to default values.
6. [ ] Offers list and offer count update immediately after clearing filters.
7. [ ] No page reload occurs on clearing filters.
8. [ ] Existing filter and sorting functionality remains unchanged.
9. [ ] Automated tests cover the new button's visibility, enabled state, and clearing behavior.