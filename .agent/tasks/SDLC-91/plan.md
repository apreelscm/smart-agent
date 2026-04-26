# SDLC-91: Na liscie ofert zmien przycis "wyczyść wszystkie oferty" na "Wyczyść"

## Specification

### Overview
This task delivers a copy update on the offers list screen so the existing clear-filters action uses the shorter label **"Wyczyść"** instead of the current longer text. The goal is to align the CTA wording on the prepared offers screen without changing the existing filtering or reset behavior.

### User Stories
- US-001: As a sales agent, I want the clear-filters action on the offers list to have a short, readable label, so that the toolbar is clearer and easier to scan.
  - Given I am on the prepared offers list
  - When the toolbar actions are rendered
  - Then the existing filter-reset button should display the label "Wyczyść"

### Functional Requirements
- FR-001: The clear-filters button on the offers list page must display the label `Wyczyść`.
- FR-002: The button must remain bound to the existing `clearAllFilters()` action.
- FR-003: The existing disabled/enabled behavior based on `filtersChanged()` must remain unchanged.
- FR-004: The previous label `Wyczyść wszystkie filtry` must no longer be shown on the offers list page.

### Edge Cases
- EC-001: When the button is disabled because filters are already at defaults, its label must still read `Wyczyść`.
- EC-002: When the page enters the empty-state view after filtering, the toolbar button label remains `Wyczyść`; only the CTA copy changes, not the reset behavior.
- EC-003: The shortened label must not break the existing toolbar layout on desktop or stacked mobile layouts defined in `offers-home-page.component.scss`.

### Success Criteria
- [ ] On `/offers`, the toolbar button label is `Wyczyść`.
- [ ] The old label `Wyczyść wszystkie filtry` is not rendered on the offers list page.
- [ ] Clicking the button still resets filters exactly as before.
- [ ] The button disabled state still follows the existing `filtersChanged()` logic.
- [ ] No visual regression is introduced in the offers toolbar layout.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone-component app using signals and PrimeNG. The offers list screen is implemented in `src/app/features/offers/pages/offers-home-page.component.ts` and `src/app/features/offers/pages/offers-home-page.component.html`.

This Jira is a narrow UI copy change. The existing clear-filters behavior is already implemented in code:
- `filtersChanged` computed signal controls button enabled state in `offers-home-page.component.ts`
- `clearAllFilters()` resets the current filters/sort state
- the button is rendered in `offers-home-page.component.html`

The implementation should therefore reuse the current behavior and only update the button label in the template from `Wyczyść wszystkie filtry` to `Wyczyść`. No TypeScript logic or repository/service integration changes are required.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space — no service integration changes are needed.
- Domain context found in SDLC: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the offers list is part of the sales-agent application flow; this task is limited to copy on that existing screen.

For verification, add a focused Playwright assertion for the offers page so the label change is covered by automated UI validation. This fits the existing test tooling already present in `tests/e2e`.

### Task Breakdown

#### Phase 1: Update offers toolbar copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change clear-filters button label | `src/app/features/offers/pages/offers-home-page.component.html` | Replace the current PrimeNG button label `Wyczyść wszystkie filtry` with `Wyczyść` while keeping the existing `clearAllFilters()` click handler, disabled binding, styling, and button placement unchanged. |
| 1.2 | Keep existing reset behavior untouched | `src/app/features/offers/pages/offers-home-page.component.ts` | Do not alter `filtersChanged()` or `clearAllFilters()` unless required by template binding consistency; this phase is explicitly a no-logic-change confirmation. |
| 1.3 | Add regression coverage for button copy | `tests/e2e/offers.spec.ts` | Add a Playwright test that navigates to `/offers`, verifies the button with text `Wyczyść` is visible, and confirms the old text `Wyczyść wszystkie filtry` is absent. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the clear-filters button text to the new short label. |
| `tests/e2e/offers.spec.ts` | CREATE | Add targeted E2E coverage for the offers toolbar button label. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements