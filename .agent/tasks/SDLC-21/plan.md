# SDLC-21: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only UI adjustment on the offers list page: the existing `Wyczyść` filter button should change from its current blue-toned custom styling to a green appearance. The button must stay in the same location, retain the same label, disabled-state logic, and continue to trigger the existing `clearAllFilters()` behavior.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the requested visual emphasis.
  - Given the offers list page is open / When I view the toolbar actions / Then the `Wyczyść` button is displayed in green styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must use green visual styling instead of the current blue custom styling.
- FR-002: The button must remain labeled `Wyczyść`.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button must remain in the `toolbar__actions` area without layout changes.
- FR-006: Styling should be implemented in the component stylesheet instead of inline `style`, to align with existing SCSS-based component styling patterns.
- FR-007: The offers page unit test should be extended to verify the green styling contract in a maintainable way.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still keep the green visual variant styling appropriate for its disabled state.
- EC-002: Clicking the green `Wyczyść` button must still reset filters to defaults: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: The styling change must affect only the clear-filters button and must not unintentionally recolor the adjacent “Nowa oferta” button or shared PrimeNG button styles globally.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button remains labeled `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] The styling is no longer hardcoded inline in the template
- [ ] The offers page unit test passes with assertions aligned to the updated button styling approach

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG 20, with feature pages organized under `src/app/features/...` and page-local styling implemented in SCSS files. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase findings:
- The clear filters action already exists as `clearAllFilters()` in `offers-home-page.component.ts`; no behavior change is needed.
- The disabled state is already driven by the `filtersChanged()` computed signal.
- The button currently has `class="clear-filters-button"` and uses inline style values in the template:
  - `background-color: #cce5ff`
  - `color: #004085`
  - `border-color: #b8daff`
- Existing page styles live in `offers-home-page.component.scss`, including page-specific PrimeNG overrides via `:host ::ng-deep`, so this is the correct place for a scoped button variant.
- Existing unit tests in `offers-home-page.component.spec.ts` already assert the button label and presence, so this spec should be extended rather than creating a new test file.

Confluence discovery results:
- No existing service or API documentation relevant to this UI color change was found in Services space.
- No relevant domain/UI convention documentation was found in SDLC space.

Because this is a presentation-only change, no repository, model, service, or runtime integration changes are required.

Implementation strategy:
1. Remove the inline style from the clear-filters button in the template.
2. Keep the existing `clear-filters-button` class as the styling hook.
3. Add scoped SCSS rules in `offers-home-page.component.scss` to color that button green, ideally targeting the PrimeNG button element through the class already present on the host button.
4. Preserve current button semantics, bindings, and layout.
5. Update the unit test to assert the template no longer relies on inline styling and still renders the correct labeled button with the intended class hook. Since computed stylesheet colors are brittle in unit tests, verify the maintainable contract: class presence and removal of obsolete inline blue styling.

### Task Breakdown

#### Phase 1: Replace inline clear-button styling with green scoped styles
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline blue styling from template | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the current inline `style` attribute from the `Wyczyść` button while preserving label, click binding, disabled binding, and `clear-filters-button` class. |
| 1.2 | Add green page-scoped button styles | `src/app/features/offers/pages/offers-home-page.component.scss` | Define scoped styles for `.clear-filters-button` so the button uses a green background, green border, and readable contrasting text, including hover/focus/disabled-safe presentation as needed for PrimeNG rendering. |
| 1.3 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are needed in `clearAllFilters()` or `filtersChanged()` because the task only changes presentation. |
| 1.4 | Update unit test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the existing button rendering test to assert the `Wyczyść` button is present, retains the `clear-filters-button` class, and no longer carries the obsolete inline blue style contract. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove the inline blue styling from the `Wyczyść` button while preserving existing bindings and class hook. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add scoped green styling for the clear-filters button. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update assertions to validate the button remains present and is styled via class-based implementation instead of inline blue styles. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements