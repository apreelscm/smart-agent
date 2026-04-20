# SDLC-32: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual update on the offers list page: the existing `Wyczyść` filter reset button should change from its current light blue styling to a green variant. The button must remain in the same place, keep the same label, disabled-state logic, and continue invoking the existing `clearAllFilters()` behavior.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the requested UI styling.
  - Given the offers list page is open / When I view the toolbar actions / Then the `Wyczyść` button is displayed using green styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must use green styling instead of the current blue styling.
- FR-002: The button must keep the label `Wyczyść`.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button must remain in the `toolbar__actions` area with the existing `clear-filters-button` class.
- FR-006: Styling should follow existing component-level styling patterns in `offers-home-page.component.scss` rather than relying on ad hoc inline color values where possible.
- FR-007: The offers page unit test should be extended or updated to verify the button remains rendered correctly after the styling change.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, the button should still use the green visual treatment appropriate for its disabled state.
- EC-002: When filters are active, clicking `Wyczyść` must still reset search term, status, product, sort field, and sort direction to defaults.
- EC-003: The style change must not affect adjacent actions such as the currency switcher or the “Nowa oferta” button.
- EC-004: The style change must not unintentionally alter other PrimeNG buttons on the page.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button still displays the label `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] Unit tests pass after the styling update

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
This repository is an Angular 20 application using standalone components and PrimeNG. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase patterns observed:
- Page-specific presentation is primarily handled in component SCSS files, e.g. `offers-home-page.component.scss`.
- The `Wyczyść` button currently exists in `offers-home-page.component.html` with class `clear-filters-button`.
- The current green/blue appearance is being forced via an inline `style` attribute on the button, which is not ideal for maintainability.
- Filter reset behavior already exists in `clearAllFilters()` in `offers-home-page.component.ts`.
- Existing test coverage in `offers-home-page.component.spec.ts` already validates presence and label of the clear button.

Confluence discovery results:
- No existing service or API documentation relevant to this UI-only color change was found in `Services` space.
- No relevant domain convention or ADR specific to this button styling change was found in `SDLC` space.

Because this is a UI-only change, no service integration or backend/runtime changes are required.

Recommended implementation:
1. Remove the hard-coded inline blue style from the clear button in `offers-home-page.component.html`.
2. Move button color styling into `offers-home-page.component.scss` using the existing `clear-filters-button` hook.
3. Apply a green background, text, and border treatment there, including hover/focus/disabled-safe overrides as needed for PrimeNG button output.
4. Leave `clearAllFilters()`, `filtersChanged()`, and all current button behavior unchanged.
5. Keep the existing spec assertion for the `Wyczyść` label and, if useful, add a lightweight assertion that the button still has the `clear-filters-button` class, since styling is class-driven.

This keeps scope strictly limited to the Jira request while aligning with the repo’s component styling pattern.

### Task Breakdown

#### Phase 1: Restyle clear filters button
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline button colors | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the current inline `style` attribute from the `Wyczyść` button so color is no longer hard-coded in the template. |
| 1.2 | Add green button styling | `src/app/features/offers/pages/offers-home-page.component.scss` | Define styles for `.clear-filters-button` and PrimeNG-rendered button states so the button appears green while preserving layout and component isolation. |
| 1.3 | Preserve existing behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are needed because `clearAllFilters()` and `filtersChanged()` already implement the desired behavior. |
| 1.4 | Update/extend unit test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing label assertion and add a check that the button still renders with `clear-filters-button`, ensuring the class-based styling hook remains intact. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline blue styling from the `Wyczyść` button while preserving label, class, click handler, and disabled binding. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add component-scoped green styling for the clear filters button, including PrimeNG-compatible selectors and state handling. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Retain label verification and add/assert the styling class hook for the clear button. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements