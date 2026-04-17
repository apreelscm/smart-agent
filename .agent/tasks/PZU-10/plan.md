# PZU-10: Zmiana nazwy przycisku

## Specification

### Overview
This task requires changing the label of the button currently named “Wyczyść wszystkie filtry” to “Wyczyść” in the offers home page filter toolbar. This is a minor UI text update to improve clarity and conciseness.

### User Stories
- US-001: As a user, I want the filter clear button to be labeled “Wyczyść” instead of “Wyczyść wszystkie filtry”, so that the UI is cleaner and the button label is concise.
  - Given I am on the offers home page / When I look at the filter toolbar / Then I see the button labeled “Wyczyść”

### Functional Requirements
- FR-001: The button that clears all filters on the offers home page must have the label “Wyczyść” instead of “Wyczyść wszystkie filtry”.
- FR-002: The button’s functionality and styling remain unchanged.
- FR-003: The button should remain disabled when no filters are changed, enabled otherwise.

### Edge Cases
- EC-001: If the button is disabled (no filters changed), the label should still be “Wyczyść”.
- EC-002: The button’s accessibility attributes and styles should not be affected by the label change.

### Success Criteria
- [ ] The button on the offers home page filter toolbar is labeled “Wyczyść”.
- [ ] The button’s functionality to clear filters works as before.
- [ ] No visual or functional regressions occur.
- [ ] Unit and/or UI tests (if any) reflect the updated label.

### Open Questions
- None

---

## Implementation Plan

### Technical Approach
The application is an Angular 20 project using PrimeNG UI components. The offers home page component is `OffersHomePageComponent` located in `src/app/features/offers/pages/`. The button in question is in the template file `offers-home-page.component.html`. The label is currently set as a static string on the button’s `label` attribute.

The change involves modifying the button’s `label` attribute from `"Wyczyść wszystkie filtry"` to `"Wyczyść"` in the HTML template. No changes are needed in the TypeScript component logic or styles.

This approach follows the existing codebase pattern of static button labels in the template.

### Task Breakdown

#### Phase 1: Update Button Label
| #   | Task                        | Files                                  | Description                                  |
|-----|-----------------------------|---------------------------------------|----------------------------------------------|
| 1.1 | Modify button label          | `src/app/features/offers/pages/offers-home-page.component.html` | Change the label attribute of the clear filters button from "Wyczyść wszystkie filtry" to "Wyczyść". |

#### Phase 2: Verification
| #   | Task                        | Files                                  | Description                                  |
|-----|-----------------------------|---------------------------------------|----------------------------------------------|
| 2.1 | Build and test              | N/A                                   | Build the project and run existing tests to ensure no regressions. |
| 2.2 | Manual UI verification      | N/A                                   | Verify in the running app that the button label is updated and functionality unchanged. |

### File Change Summary
| File                                                      | Action | Description                          |
|-----------------------------------------------------------|--------|------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Change clear filters button label to “Wyczyść” |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Existing unit tests pass.
3. [ ] Manual verification confirms the button label is “Wyczyść”.
4. [ ] Manual verification confirms the button clears filters as before.