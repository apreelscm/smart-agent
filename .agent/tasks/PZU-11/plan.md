# PZU-11: Zmiana nazwy przycisku

## Specification

### Overview
This task requires changing the label of the button currently named “Wyczyść wszystkie filtry” to “Wyczyść” in the offers home page. This change improves UI clarity and aligns with updated design requirements. Additionally, the button should be made slightly darker to improve visibility.

### User Stories
- US-001: As a user, I want the clear filters button to be labeled “Wyczyść” instead of “Wyczyść wszystkie filtry”, so that the button label is concise and clear.
  - Given I am on the offers home page / When I look at the filter toolbar / Then I see the clear filters button labeled as “Wyczyść”.
- US-002: As a user, I want the clear filters button to have a slightly darker shade, so that it is more visible and stands out better on the UI.
  - Given I am on the offers home page / When I look at the clear filters button / Then I see it with a slightly darker color.

### Functional Requirements
- FR-001: The button label text must be changed from “Wyczyść wszystkie filtry” to “Wyczyść”.
- FR-002: The button’s functionality remains unchanged.
- FR-003: The button must remain disabled when no filters are changed and enabled otherwise.
- FR-004: The button’s color styling should be adjusted to a slightly darker shade than currently implemented.

### Edge Cases
- EC-001: If the button is disabled, the label still shows “Wyczyść” and the button color is the darker shade but visually indicates disabled state.
- EC-002: If the button is enabled, the label shows “Wyczyść” and clicking it clears all filters as before, with the button color in the darker shade.

### Success Criteria
- [ ] The button label on the offers home page filter toolbar reads “Wyczyść”.
- [ ] The button’s enable/disable state and functionality remain unchanged.
- [ ] The button color is updated to a slightly darker shade.
- [ ] No regressions or UI issues appear after the label and color changes.
- [ ] Automated or manual UI tests verify the label and color changes.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The project is an Angular application using TypeScript and HTML templates. The offers home page component template (`offers-home-page.component.html`) contains the button with the label “Wyczyść wszystkie filtry”. The change involves editing this HTML file to update the button label text. Additionally, the button’s CSS styling will be updated to use a slightly darker color. No changes to component logic are required.

### Task Breakdown

#### Phase 1: Change Button Label and Color
| #   | Task                               | Files                                | Description                                      |
|-----|----------------------------------|------------------------------------|------------------------------------------------|
| 1.1 | Locate the clear filters button  | `src/app/features/offers/pages/offers-home-page.component.html` | Find the button with label “Wyczyść wszystkie filtry” and change the label to “Wyczyść”. |
| 1.2 | Adjust button color styling       | `src/app/features/offers/pages/offers-home-page.component.html` and/or related CSS/SCSS files | Modify the button’s CSS classes or inline styles to apply a slightly darker color. |
| 1.3 | Verify button functionality       | N/A                                | Confirm the button remains enabled/disabled correctly and clears filters on click. |
| 1.4 | Test UI                         | N/A                                | Run existing UI tests or perform manual testing to ensure label and color changes are reflected and no regressions occur. |

### File Change Summary
| File                                                      | Action | Description                          |
|-----------------------------------------------------------|--------|------------------------------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Change button label text from “Wyczyść wszystkie filtry” to “Wyczyść” and update button color styling to be slightly darker. |
| (Potential CSS/SCSS file related to offers home page button styles) | MODIFY | Adjust button color to a slightly darker shade (if styles are not inline). |

### Verification Steps
1. [ ] Build succeeds without errors.
2. [ ] Existing tests pass.
3. [ ] Manual or automated UI verification confirms the button label is updated.
4. [ ] Manual or automated UI verification confirms the button color is slightly darker.
5. [ ] Button functionality (clear filters) remains unchanged.

## Revision History
| Revision | Reviewer    | Summary of Changes Made                          |
|----------|-------------|-------------------------------------------------|
| 1        | matlipinski | Added requirement to make the button color slightly darker and updated plan accordingly. |