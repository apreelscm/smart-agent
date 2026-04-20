# SDLC-24: Na ekranie ofert zmien kolor przycisku wyczysc na niebieski

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter-reset button should use a blue appearance instead of its current styling. The button must remain in the same location, keep the same label, disabled logic, and continue to trigger the existing `clearAllFilters()` behavior.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to appear blue, so that it matches the requested UI styling.
  - Given the offers list page is open / When I view the toolbar actions area / Then I see the `Wyczyść` button styled in blue

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be presented with blue visual styling.
- FR-002: The button must remain bound to the existing `clearAllFilters()` method.
- FR-003: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-004: The button label must remain `Wyczyść`.
- FR-005: The button must remain in the `toolbar__actions` section and keep the existing `clear-filters-button` class.
- FR-006: Blue styling should be implemented using the codebase’s existing styling patterns, preferring component SCSS over inline styling for maintainability.
- FR-007: The offers page unit test suite must continue to validate presence of the `Wyczyść` button; if style assertions are added, they must only verify implementation that is stable in unit tests.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still retain the intended blue visual treatment within the disabled-state constraints of PrimeNG/browser rendering.
- EC-002: When filters are active, clicking the blue `Wyczyść` button must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: The styling change must not affect the adjacent currency switcher or “Nowa oferta” action button layout.
- EC-004: The styling change must not unintentionally recolor other buttons on the offers page.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in blue
- [ ] The button label remains `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] The styling is defined in component styles rather than runtime logic
- [ ] The offers page unit tests pass

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG, with feature pages styled through per-component SCSS files. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase patterns observed:
- UI behavior is already encapsulated in `OffersHomePageComponent`.
- The `Wyczyść` button already exists in `offers-home-page.component.html` with class `clear-filters-button`, bound to `clearAllFilters()` and `[disabled]="!filtersChanged()"`.
- The button is currently styled via an inline `style` attribute in the template.
- Component-specific visual customizations for PrimeNG elements are handled in SCSS, including `:host ::ng-deep` selectors for nested PrimeNG button structures.

Implementation should stay narrowly scoped to the visual change:
1. remove the inline color styling from the `Wyczyść` button in the template,
2. move the blue styling into `offers-home-page.component.scss` under the existing `clear-filters-button` hook,
3. preserve the existing click handler, label, class, and disabled logic,
4. keep the existing label-focused unit test, and only extend it if a stable DOM class/style assertion is warranted.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain ADR/convention specific to this offers button color change was found in SDLC space.

No existing service found in configured Confluence scopes that should be reused for this task. This is a local frontend styling change only.

### Task Breakdown

#### Phase 1: Move and apply blue button styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Inspect current clear button markup | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the `Wyczyść` button uses `clear-filters-button`, `clearAllFilters()`, and disabled binding so only styling is changed. |
| 1.2 | Replace inline styling with stylesheet-driven blue theme | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Remove the inline `style` attribute and define blue background, text, and border styles in component SCSS targeting the clear button without impacting other buttons. |
| 1.3 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are required because `clearAllFilters()` and `filtersChanged()` already satisfy the task. |
| 1.4 | Validate test coverage remains aligned | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing assertion that the button renders with `Wyczyść`; optionally add a stable class-presence assertion if useful, but avoid brittle style-value checks unless they reliably reflect the chosen implementation. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline style from the `Wyczyść` button while preserving label, class, click binding, and disabled binding. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add scoped blue styling for `.clear-filters-button`, likely via PrimeNG-compatible selectors, so the button appears blue. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Keep or minimally adjust button-rendering assertions to ensure the `Wyczyść` button remains present after the styling refactor. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements