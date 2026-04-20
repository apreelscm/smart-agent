# PZU-12: Zmiana nazwy przycisku

## Specification

### Overview
This task requires changing the label of the button currently named “Wyczyść wszystkie filtry” to a shorter label “Wyczyść”. This change is intended to improve UI clarity and brevity on the offers home page where the filter clearing button is displayed.

### User Stories
- US-001: As a user, I want the button label for clearing filters to be shorter and clearer, so that the interface looks cleaner and is easier to understand.
  - Given the offers home page is displayed
  - When I look at the filter clearing button
  - Then I see the label changed from “Wyczyść wszystkie filtry” to “Wyczyść”

### Functional Requirements
- FR-001: The button label text must be changed from “Wyczyść wszystkie filtry” to “Wyczyść”.
- FR-002: The button functionality remains unchanged.
- FR-003: The button style and placement remain unchanged.

### Edge Cases
- EC-001: If the button is disabled (no filters changed), the label still shows “Wyczyść”.
- EC-002: The label change should not affect any accessibility attributes or aria-labels.

### Success Criteria
- [ ] The button on the offers home page that clears all filters displays the label “Wyczyść”.
- [ ] No regressions in button functionality or style.
- [ ] All existing tests related to this button pass.

### Open Questions
- None

---

## Implementation Plan

### Technical Approach
The project is an Angular application using PrimeNG components. The button in question is a PrimeNG button (`pButton`) with a `label` attribute. The label text is currently hardcoded in the template of the offers home page component. The change involves editing the HTML template file to update the label string.

The relevant file is `src/app/features/offers/pages/offers-home-page.component.html`. The button is inside a toolbar div with class `toolbar__actions`. The label attribute on the button needs to be changed from "Wyczyść wszystkie filtry" to "Wyczyść".

No changes to TypeScript or styles are required. The existing button disabling logic and click handler remain intact.

### Task Breakdown

#### Phase 1: Change Button Label
| #   | Task                          | Files                                      | Description                                  |
|-----|-------------------------------|--------------------------------------------|----------------------------------------------|
| 1.1 | Locate button in HTML template | `src/app/features/offers/pages/offers-home-page.component.html` | Find the button with label “Wyczyść wszystkie filtry” |
| 1.2 | Change label text              | `src/app/features/offers/pages/offers-home-page.component.html` | Update label attribute to “Wyczyść”          |
| 1.3 | Verify UI and functionality   | N/A                                        | Run the app, verify button label and behavior |
| 1.4 | Run tests                     | N/A                                        | Run existing unit tests to ensure no breakage |

### File Change Summary
| File                                                        | Action | Description                          |
|-------------------------------------------------------------|--------|------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Change button label text to “Wyczyść” |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Unit tests pass.
3. [ ] Manually verify on the offers home page that the button label reads “Wyczyść”.
4. [ ] Verify button functionality (clear filters) works as before.
5. [ ] Verify button styling and disabled state remain consistent.