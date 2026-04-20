# SDLC-26: Na ekranie ofert zmien kolor przycisku wyczysc na niebieski

## Specification

### Overview
This task delivers a visual update on the offers list screen: the existing `Wyczyść` filter reset button should use a blue appearance instead of its current styling. The button must remain in the same location, keep the same label and behavior, and continue to use the existing `clearAllFilters()` flow and disabled-state logic.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be blue, so that it matches the requested visual styling.
  - Given the offers list page is open / When I view the toolbar actions / Then the `Wyczyść` button is rendered with blue styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be styled in blue.
- FR-002: The button must keep the existing `Wyczyść` label.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button must remain in the `toolbar__actions` area next to the other toolbar actions.
- FR-006: The implementation should follow existing styling conventions in the repo and prefer maintainable component styling over hard-coded inline presentation where possible.
- FR-007: The offers page unit test should be updated to validate the intended blue styling in a stable way.

### Edge Cases
- EC-001: When the button is disabled because no filters are active, it should still retain the intended blue visual treatment consistent with PrimeNG disabled behavior.
- EC-002: The styling update must not affect the button click action, label, or layout spacing within `.toolbar__actions`.
- EC-003: The styling change must not unintentionally recolor other buttons on the offers page, such as the sort-direction button or the new-offer action.
- EC-004: If the page is rendered on smaller breakpoints, the blue `Wyczyść` button must remain readable and visually consistent in the stacked toolbar layout.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in blue
- [ ] The button still triggers the existing filter reset behavior
- [ ] The button label remains `Wyczyść`
- [ ] No other offer page action buttons are unintentionally restyled
- [ ] The offers page unit test passes with assertions updated for the blue styling

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG (`primeng/button`, `primeng/select`, `primeng/splitbutton`, etc.). The offers list page is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase patterns and findings:
- The reset action already exists as `clearAllFilters()` in `src/app/features/offers/pages/offers-home-page.component.ts`.
- The current enable/disable logic already exists as the `filtersChanged` computed signal in the same component.
- The current `Wyczyść` button is rendered in `offers-home-page.component.html` with class `clear-filters-button`.
- The current implementation uses inline styles on that button:
  - `background-color: #cce5ff`
  - `color: #004085`
  - `border-color: #b8daff`
- The application theme defines blue as the semantic primary color in `src/app/core/config/primeng-theme.ts`, with the main primary tone `#1d4ed8`.
- Shared design tokens in `src/styles/_tokens.scss` include app-level surface, text, and border variables, but there is no dedicated token yet for this specific button.

Confluence discovery:
- No existing service or API documentation relevant to this UI-only color change was found in the configured `Services` space.
- No relevant domain guideline or UI convention page was found in the configured `SDLC` space.

Given the task scope, this should remain a UI-only change localized to the offers page. To align with existing repo conventions and improve maintainability, the plan should move the button’s custom presentation out of the inline `style` attribute and into `offers-home-page.component.scss`, scoped to `.clear-filters-button`. This keeps the behavior untouched while making the styling easier to maintain and test.

Recommended implementation:
1. Remove the current inline color styles from the `Wyczyść` button in the template.
2. Add scoped SCSS for `.clear-filters-button` that applies a blue button treatment matching the app’s blue theme.
3. If needed for PrimeNG button internals, use component-scoped selectors compatible with the existing stylesheet approach.
4. Update the component spec to assert the presence of the button class and verify that the template no longer relies on the old inline light-blue style string, or verify the intended blue styling in the most stable way available for the current test setup.

Because this is a visual update only:
- no repository/service/model changes are required,
- no runtime integrations are involved,
- no mock/runtime data path changes are needed.

### Task Breakdown

#### Phase 1: Update offers-page clear button styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Replace inline clear-button styling with component-scoped styling | `src/app/features/offers/pages/offers-home-page.component.html` | Remove the hard-coded inline `style` attribute from the `Wyczyść` button while keeping the same label, class, click handler, and disabled binding. |
| 1.2 | Implement blue button presentation | `src/app/features/offers/pages/offers-home-page.component.scss` | Add or update `.clear-filters-button` styles so the button renders in blue and remains visually consistent with the app’s PrimeNG-based design language. Scope styles so they affect only this button. |
| 1.3 | Preserve existing behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are needed because `clearAllFilters()` and `filtersChanged()` already satisfy the required behavior. |
| 1.4 | Update unit test coverage for styling change | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the existing button test to verify the `Wyczyść` button still renders and that the implementation reflects the new blue-styling approach without regressing label or selector usage. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline clear-button color styles while preserving the existing `Wyczyść` label and bindings. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add scoped blue styling for `.clear-filters-button` consistent with the offers page and app theme. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update tests to validate the `Wyczyść` button remains present and reflects the new styling implementation. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements