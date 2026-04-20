# SDLC-25: Na ekranie ofert zmien kolor przycisku wyczysc na niebieski

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter button should use a blue presentation instead of its current custom light-blue inline styling. The button must remain in the same toolbar location, keep the same label, preserve its disabled/enabled behavior, and continue to invoke the existing `clearAllFilters()` logic without changing filtering functionality.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be blue, so that it better matches the expected UI styling.
  - Given the offers list page is open / When I view the toolbar actions / Then the `Wyczyść` button is rendered in blue styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be displayed with blue styling.
- FR-002: The button must remain bound to the existing `clearAllFilters()` method.
- FR-003: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-004: The button label must remain `Wyczyść`.
- FR-005: The button must remain in the `toolbar__actions` section beside the existing toolbar actions.
- FR-006: The implementation should follow existing PrimeNG/theming patterns already used by the application, avoiding ad hoc inline color overrides where possible.
- FR-007: The offers page unit test should be extended or adjusted to verify the button still renders correctly after the style change.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still appear as the blue-styled variant while respecting the disabled state.
- EC-002: When filters are active, clicking the blue `Wyczyść` button must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: The style change must not affect the adjacent `Nowa oferta` action or currency switcher layout.
- EC-004: The style change must remain responsive and not break the stacked mobile toolbar layout defined in the page stylesheet.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in blue
- [ ] The button still triggers the existing filter reset behavior
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] The toolbar layout remains unchanged on desktop and mobile breakpoints
- [ ] The offers page unit test passes after the style update

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG 20. The offers page is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The current implementation already contains the `Wyczyść` button in `offers-home-page.component.html`, but it is styled via inline overrides:

- `background-color: #cce5ff`
- `color: #004085`
- `border-color: #b8daff`

These hardcoded values are inconsistent with the app’s existing blue theme tokens and PrimeNG preset:
- global tokens in `src/styles/_tokens.scss`
- PrimeNG semantic primary colors in `src/app/core/config/primeng-theme.ts`

The lowest-risk implementation is to remove the inline style override and switch the button to PrimeNG’s built-in primary/blue appearance, preserving the existing `clear-filters-button` class for local targeting if needed. Because the app theme defines `primary` as blue, using the default button styling is the most codebase-aligned solution. If a slightly less prominent variant is desired while remaining blue, the page stylesheet can define a `.clear-filters-button` rule that references the existing blue palette rather than inline hex values.

For this task, repository patterns suggest:
- keep business logic untouched in `offers-home-page.component.ts`
- make the visual change in the template and, if needed, page-local SCSS
- keep tests lightweight and focused on rendering/behavior already covered by this component

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain convention or design-system guidance was found in SDLC space.

No existing service or integration is reused for this task, and no runtime integration changes are required.

### Task Breakdown

#### Phase 1: Update clear button styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Replace ad hoc inline colors with blue theme styling | `src/app/features/offers/pages/offers-home-page.component.html` | Remove the current inline `style` override from the `clear-filters-button` and apply PrimeNG-native blue styling via button attributes/classes. |
| 1.2 | Add page-level styling only if required | `src/app/features/offers/pages/offers-home-page.component.scss` | If the default PrimeNG primary button does not meet the requested appearance, add a scoped `.clear-filters-button` rule using the app’s existing blue palette instead of inline hardcoded styling. |
| 1.3 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no logic changes are needed because `clearAllFilters()` and `filtersChanged()` already implement the required behavior. |
| 1.4 | Update or extend component test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing rendering assertion for `Wyczyść` and, if practical within current test patterns, assert the button still exists with the `clear-filters-button` class after the styling refactor. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline light-blue styles and apply blue styling through PrimeNG/theme-aligned configuration. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add scoped blue button styles only if necessary to preserve the requested appearance without inline overrides. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update test expectations to cover the refactored button rendering and preserve regression coverage for the `Wyczyść` button. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements