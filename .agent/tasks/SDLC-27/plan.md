# SDLC-27: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter button should change from its current light blue styling to green styling. The button must remain in the same toolbar location, keep the same label, disabled-state behavior, and continue to invoke the existing `clearAllFilters()` logic without any functional changes.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the requested UI appearance.
  - Given the offers list page is open / When I look at the toolbar actions / Then I see the `Wyczyść` button styled in green

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be displayed with green styling instead of the current blue styling.
- FR-002: The button label must remain `Wyczyść`.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button must remain in the `toolbar__actions` area in the same position relative to the currency switcher and new-offer action.
- FR-006: The existing `clear-filters-button` class must be retained unless a minimal template/style refactor is required to apply the green styling consistently.
- FR-007: Unit tests for the offers page must continue to validate the presence of the `Wyczyść` button, and should be extended if needed to verify the green styling hook used by the implementation.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still render with the green visual variant appropriate for the disabled state.
- EC-002: When filters are active, clicking the green `Wyczyść` button must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: No stale blue inline styling should remain on the button after the change.
- EC-004: The styling change must not affect other PrimeNG buttons in the toolbar, including the sort direction button and the new-offer button.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button label remains `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] No other toolbar buttons change appearance unintentionally
- [ ] The offers page unit test suite passes

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase patterns:
- The offers toolbar and clear action are already defined in `offers-home-page.component.html`.
- The reset behavior already exists in `clearAllFilters()` in `offers-home-page.component.ts`.
- The enabled/disabled state is already controlled by the computed signal `filtersChanged`.
- The `Wyczyść` button currently uses inline styles in the template:
  - `background-color: #cce5ff`
  - `color: #004085`
  - `border-color: #b8daff`

Because this ticket is a UI-color adjustment only, the implementation should be minimal and scoped to replacing the current blue button styling with green styling. The preferred approach is to remove the hard-coded inline color styles from the template and move the button appearance into `offers-home-page.component.scss` under the existing `clear-filters-button` hook. That keeps the template cleaner and follows the repo’s component-scoped styling pattern already used throughout feature pages.

Planned styling approach:
- Preserve the existing `clear-filters-button` class in the template.
- Replace the blue appearance with green colors in component SCSS, using a normal, hover/focus-safe visual treatment consistent with the rest of the page palette.
- Ensure disabled styling remains readable and visually distinct.
- Avoid changing PrimeNG severity or broad global button rules, so only this button is affected.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain guidance or UI convention was found in SDLC space.

No existing service found in configured Confluence scopes that applies to this task — this is a local frontend styling update only.

### Task Breakdown

#### Phase 1: Update clear button styling on offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Identify current clear button styling hook | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the `Wyczyść` button uses the `clear-filters-button` class and currently relies on inline blue styles. |
| 1.2 | Move visual styling to component stylesheet | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Remove the inline color styles from the template and define green button styles under `.clear-filters-button`, keeping the label, click handler, and disabled binding unchanged. |
| 1.3 | Preserve behavior logic unchanged | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no TypeScript changes are required because `clearAllFilters()` and `filtersChanged()` already satisfy the behavior needed for this task. |
| 1.4 | Update/extend offers page test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing assertion for the `Wyczyść` label and, if practical for the current test setup, add an assertion that verifies the button still exposes the `clear-filters-button` class used for green styling. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline blue styles from the `Wyczyść` button while preserving existing bindings and class hook. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add component-scoped green styling for `.clear-filters-button`, including safe disabled-state presentation if needed. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Keep the label assertion and optionally strengthen the test around the styling hook/class used by the green button. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements