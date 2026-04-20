# SDLC-28: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter button should use a green presentation instead of its current light blue custom styling. The button must remain in the same place, keep the same label, enabled/disabled behavior, and continue to trigger the existing `clearAllFilters()` logic.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it is visually aligned with the requested UI styling.
  - Given the offers list page is open / When I view the toolbar actions / Then I see the `Wyczyść` button styled in green

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be displayed in green.
- FR-002: The button must keep the existing `Wyczyść` label.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button position in the `toolbar__actions` area must remain unchanged.
- FR-006: The button should use codebase-consistent styling rather than inline one-off colors where possible.
- FR-007: The offers page unit test should be extended to cover the green styling contract in a maintainable way.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still retain the intended green visual variant subject to PrimeNG disabled-state rendering.
- EC-002: When filters are active, clicking the green `Wyczyść` button must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: No stale blue inline styling should remain on the button after the change.
- EC-004: The styling change must not affect the adjacent “Nowa oferta” action button or currency switcher.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button still displays `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] The old light blue inline styling is removed
- [ ] The offers page unit test passes with updated styling assertions

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG 20. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Current codebase patterns show:
- PrimeNG buttons are used declaratively via `pButton`.
- The offers page currently styles the `Wyczyść` button with a hard-coded inline `style`:
  - `background-color: #cce5ff`
  - `color: #004085`
  - `border-color: #b8daff`
- The application theme primary color is blue in `src/app/core/config/primeng-theme.ts`, so this button is intentionally overridden locally rather than themed globally.
- Shared app tokens in `src/styles/_tokens.scss` contain general surface/text colors, but no green semantic action token exists yet.

Given the Jira scope is only this button color, the safest implementation is a localized component-level styling update:
1. remove the inline blue style from the button in `offers-home-page.component.html`,
2. retain the existing `clear-filters-button` class,
3. define the green appearance in `offers-home-page.component.scss` using a specific selector for this button,
4. keep behavior in `offers-home-page.component.ts` unchanged,
5. extend the component spec to verify the template no longer uses the blue inline style and that the button still renders as the clear-filters control.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain guidance or UI convention was found in SDLC space.

No existing service found in configured Confluence scopes that should be reused for this task.

### Task Breakdown

#### Phase 1: Replace custom blue clear-button styling with green variant
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline button color override | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the current inline blue `style` from the `clear-filters-button` while preserving label, disabled binding, click handler, and placement. |
| 1.2 | Add local green button styles | `src/app/features/offers/pages/offers-home-page.component.scss` | Introduce a scoped style for `.clear-filters-button` that applies green background, text, and border styling compatible with the existing offers page layout and PrimeNG button rendering. Include hover/focus treatment only if needed to avoid regressions. |
| 1.3 | Preserve reset behavior without logic changes | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no TypeScript changes are required because `clearAllFilters()` and `filtersChanged()` already satisfy the functional behavior. |
| 1.4 | Update component test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the existing clear-filters button test to confirm the button still renders with `Wyczyść`, and assert the previous inline blue style is no longer present. If practical in the current Angular test setup, also assert the presence of the `clear-filters-button` class as the styling hook. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove the hard-coded blue inline style from the `Wyczyść` button while preserving behavior and label. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add scoped green styling for the clear filters button. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update tests to validate the button label remains `Wyczyść` and the old inline style has been removed. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements