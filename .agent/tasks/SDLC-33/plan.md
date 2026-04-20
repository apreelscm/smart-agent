# SDLC-33: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual update on the offers list page by changing the existing `Wyczyść` filter button from its current blue-styled appearance to a green appearance. The button must remain in the same toolbar location, keep the same label, continue to use the existing `clearAllFilters()` logic, and preserve the current enabled/disabled behavior controlled by `filtersChanged()`.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the requested visual style.
  - Given the offers list page is open / When I view the toolbar actions / Then the `Wyczyść` button is displayed with green styling

### Functional Requirements
- FR-001: The offers list page must display the existing `Wyczyść` button in green instead of the current blue style.
- FR-002: The button must remain in the `toolbar__actions` area of `offers-home-page.component.html`.
- FR-003: The button must keep the existing `clear-filters-button` class.
- FR-004: The button must remain bound to `clearAllFilters()` with no change to reset behavior.
- FR-005: The button must preserve its current disabled state logic via `[disabled]="!filtersChanged()"`.
- FR-006: The visible label must remain `Wyczyść`.
- FR-007: The offers page unit test suite must be updated to verify the green styling in a stable way appropriate for the current implementation pattern.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still retain the green visual styling definition.
- EC-002: When filters are active and the button is enabled, clicking `Wyczyść` must continue to reset search term, status, product, sort field, and sort direction to defaults.
- EC-003: No stale blue inline color values should remain on the `Wyczyść` button after the change.
- EC-004: The styling change must not affect the adjacent currency switcher or new-offer button in the same toolbar.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button label remains `Wyczyść`
- [ ] The button still calls `clearAllFilters()` when clicked
- [ ] The enabled/disabled behavior remains unchanged
- [ ] The offers page unit test passes with assertions updated for the green styling

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, signals, and PrimeNG. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant existing codebase patterns:
- The `Wyczyść` button already exists in `offers-home-page.component.html` inside `.toolbar__actions`.
- The current implementation applies custom styling through an inline `style` attribute:
  - `background-color: #cce5ff`
  - `color: #004085`
  - `border-color: #b8daff`
- The button behavior already exists in `offers-home-page.component.ts` through `clearAllFilters()` and `filtersChanged()`.
- Existing tests for this component already validate the button label in `offers-home-page.component.spec.ts`.

Implementation should stay narrowly scoped to the visual change:
1. replace the current blue inline style values on the `Wyczyść` button with green equivalents,
2. keep the existing class, label, click handler, and disabled binding untouched,
3. extend the existing unit test to verify the button still renders correctly and now carries green styling.

Because the current implementation uses inline styles directly on the button, the most consistent low-risk change is to update those inline color values rather than refactor styling into SCSS for this ticket.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain convention or ADR for this specific offers-button color change was found in SDLC space.

No existing service was found in configured API/SERVICE spaces, so no service reuse applies for this task.

### Task Breakdown

#### Phase 1: Update clear button styling on offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Update clear button color to green | `src/app/features/offers/pages/offers-home-page.component.html` | Change the inline style values on the `clear-filters-button` from blue tones to green tones while keeping label, class, click binding, and disabled binding unchanged. |
| 1.2 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are needed because `clearAllFilters()` and `filtersChanged()` already satisfy the task scope. |
| 1.3 | Update component test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the existing clear-button rendering test to assert the button still displays `Wyczyść` and that the rendered button includes the updated green style values instead of the previous blue ones. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Replace the current blue inline style on the `Wyczyść` button with green color values. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update assertions to validate the `Wyczyść` button styling reflects the green implementation. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements