# SDLC-34: Zmiana nazwy przycisku

## Specification

### Overview
Task changes the label of the clear-filters action on the offers list from **„Wyczyść wszystkie filtry”** to **„Wyczyść”**. The goal is to shorten the CTA without changing any existing filtering, sorting, enable/disable logic, or behavior of the offers toolbar.

### User Stories
- US-001: As a sales agent, I want a shorter clear-filters button label, so that the toolbar is simpler and easier to scan.
  - Given I am on the offers list page
  - When the toolbar is rendered
  - Then the clear-filters button is displayed with the label „Wyczyść”

### Functional Requirements
- FR-001: The clear-filters button on the offers list page must display the label „Wyczyść”.
- FR-002: The button must remain in the existing toolbar location next to the new-offer action.
- FR-003: The existing button behavior must remain unchanged:
  - disabled when filters/sorting are at defaults
  - enabled when any filter/sorting differs from defaults
  - resets filters and sorting to defaults on click
- FR-004: No other toolbar labels, styles, filtering logic, or offer actions should be changed as part of this task.

### Edge Cases
- EC-001: If the button is disabled because filters are unchanged, it should still display the updated label „Wyczyść”.
- EC-002: If the offers list is empty after filtering, the button label still remains „Wyczyść” and clearing works as before.
- EC-003: Accessibility bindings and button wiring must continue to function after the text change.

### Success Criteria
- [ ] On the offers page, the clear-filters button label is „Wyczyść”.
- [ ] Clicking the button still resets all filters and sorting to defaults.
- [ ] The button disabled/enabled behavior remains unchanged.
- [ ] No other UI text or toolbar behavior is unintentionally modified.
- [ ] Application build and relevant tests pass.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The issue scope is limited to a UI text update in the offers toolbar.

Confluence discovery was completed before repo exploration:
- No existing service/API relevant to this task was found in `Services` space.
- No relevant domain guidance or UI convention page was found in `SDLC` space.

Repository analysis shows this is an Angular 20 + PrimeNG application. The clear-filters control already exists in:
- `src/app/features/offers/pages/offers-home-page.component.html`
and its behavior is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`

Current implementation already includes:
- `filtersChanged` computed signal
- `clearAllFilters()` method
- button styling and disabled binding

So the implementation should reuse the existing component pattern and change only the button `label` value in the template from `Wyczyść wszystkie filtry` to `Wyczyść`. No TypeScript logic changes are required unless tests are added or updated to assert the rendered text.

### Task Breakdown

#### Phase 1: Update toolbar label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Update clear button text | `src/app/features/offers/pages/offers-home-page.component.html` | Change the PrimeNG button `label` from `Wyczyść wszystkie filtry` to `Wyczyść` while preserving existing bindings, classes, and inline styling. |
| 1.2 | Verify no logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm existing `filtersChanged` and `clearAllFilters()` behavior remains untouched and still matches ticket scope. |
| 1.3 | Add or update UI verification | `tests/e2e/smoke.spec.ts` or new focused e2e spec | Add a simple assertion for the updated visible label on the offers page if test coverage for this screen exists or can be extended without broadening scope. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Replace the clear-filters button label with the shorter text required by the Jira task. |
| `tests/e2e/smoke.spec.ts` | MODIFY | Optionally extend smoke coverage to verify the updated toolbar label if the offers page is part of the current e2e flow. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Navigate to the offers page and confirm the button label is `Wyczyść`
5. [ ] Confirm the button still enables/disables based on filter state
6. [ ] Confirm clicking the button still clears filters and sorting