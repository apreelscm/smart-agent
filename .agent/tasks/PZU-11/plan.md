# PZU-11: Zmiana nazwy przycisku

## Specification

### Overview
This task requires changing the label of the button currently named “Wyczyść wszystkie filtry” to “Wyczyść” in the offers home page. This change improves UI clarity and aligns with updated design requirements.

### User Stories
- US-001: As a user, I want the clear filters button to be labeled “Wyczyść” instead of “Wyczyść wszystkie filtry”, so that the button label is concise and clear.
  - Given I am on the offers home page / When I look at the filter toolbar / Then I see the clear filters button labeled as “Wyczyść”.

### Functional Requirements
- FR-001: The button label text must be changed from “Wyczyść wszystkie filtry” to “Wyczyść”.
- FR-002: The button’s functionality and styling remain unchanged.
- FR-003: The button must remain disabled when no filters are changed and enabled otherwise.

### Edge Cases
- EC-001: If the button is disabled, the label still shows “Wyczyść”.
- EC-002: If the button is enabled, the label shows “Wyczyść” and clicking it clears all filters as before.

### Success Criteria
- [ ] The button label on the offers home page filter toolbar reads “Wyczyść”.
- [ ] The button’s enable/disable state and functionality remain unchanged.
- [ ] No regressions or UI issues appear after the label change.
- [ ] Automated or manual UI tests verify the label change.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The project is an Angular application using TypeScript and HTML templates. The offers home page component template (`offers-home-page.component.html`) contains the button with the label “Wyczyść wszystkie filtry”. The change involves editing this HTML file to update the button label text. No changes to component logic or styles are required.

### Task Breakdown

#### Phase 1: Change Button Label
| #   | Task                               | Files                                | Description                                      |
|-----|----------------------------------|------------------------------------|------------------------------------------------|
| 1.1 | Locate the clear filters button  | `src/app/features/offers/pages/offers-home-page.component.html` | Find the button with label “Wyczyść wszystkie filtry” and change the label to “Wyczyść”. |
| 1.2 | Verify button functionality       | N/A                                | Confirm the button remains enabled/disabled correctly and clears filters on click. |
| 1.3 | Test UI                         | N/A                                | Run existing UI tests or perform manual testing to ensure label change is reflected and no regressions occur. |

### File Change Summary
| File                                                      | Action | Description                          |
|-----------------------------------------------------------|--------|------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Change button label text from “Wyczyść wszystkie filtry” to “Wyczyść”. |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Existing tests pass.
3. [ ] Manual or automated UI verification confirms the button label is updated.
4. [ ] Button functionality (clear filters) remains unchanged.