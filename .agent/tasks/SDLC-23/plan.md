# SDLC-23: Na ekranie ofert zmien kolor przycisku wyczysc na żółty

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter button should use a yellow appearance instead of its current blue custom styling. The button must remain in the same toolbar location, keep the same label, disabled-state behavior, and continue to execute the existing `clearAllFilters()` action.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be yellow, so that it matches the requested UI emphasis.
  - Given the offers list page is open / When I look at the toolbar actions / Then the `Wyczyść` button is rendered with yellow styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be displayed in yellow styling.
- FR-002: The button must remain bound to the existing `clearAllFilters()` method.
- FR-003: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-004: The button label must remain `Wyczyść`.
- FR-005: The button must remain in the `toolbar__actions` section next to the currency switcher and new-offer action.
- FR-006: Styling changes must be implemented using the existing component styling approach instead of permanent inline hard-coded blue button styles.
- FR-007: The offers page unit test suite must be updated to verify the yellow-styled clear button in a way appropriate for the component template.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still retain the yellow visual variant consistent with PrimeNG disabled rendering.
- EC-002: When filters are active, clicking the yellow `Wyczyść` button must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: No outdated blue inline button color styling should remain in the template after the change.
- EC-004: The new styling must not affect adjacent controls such as the currency switcher, sort toggle, or add-offer button.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in yellow
- [ ] The button label remains `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] Obsolete inline blue styling is removed from the template
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

Relevant codebase patterns observed:
- The offers page already centralizes behavior in signals/computed values inside `offers-home-page.component.ts`.
- The current clear button is rendered in `offers-home-page.component.html` with class `clear-filters-button`, `[disabled]="!filtersChanged()"`, and `(click)="clearAllFilters()"`.
- The current button color is forced with inline styles in the template: `background-color: #cce5ff; color: #004085; border-color: #b8daff;`.
- Component-specific visual customization is otherwise handled in `offers-home-page.component.scss`, including PrimeNG overrides via `:host ::ng-deep`.
- Existing tests for this screen already assert the presence and label of the clear button in `offers-home-page.component.spec.ts`.

Confluence discovery results:
- No existing service or API documentation relevant to this UI color change was found in the configured `Services` space.
- No relevant domain guidance or UI convention was found in the configured `SDLC` space.

Because this is a presentation-only bug fix, no service, repository, runtime integration, or domain model changes are required.

Implementation should keep scope tight:
1. remove the hard-coded inline blue style from the clear button in `offers-home-page.component.html`,
2. apply a yellow button treatment through the component stylesheet using the existing `clear-filters-button` hook and PrimeNG button structure,
3. retain current label, click handler, and disabled binding unchanged,
4. update the component spec to verify the button still exists with label `Wyczyść` and that the old inline blue style is no longer part of the template output.

If PrimeNG severity styling is sufficient for the requested yellow look, prefer using PrimeNG’s built-in `severity="warn"` on the button and only add scoped stylesheet adjustments if needed to match the current custom-button approach. This keeps the implementation aligned with the component’s existing PrimeNG usage and avoids unnecessary hard-coded runtime styles.

### Task Breakdown

#### Phase 1: Replace clear-button styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline blue styling from clear button | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the current hard-coded blue `style` attribute from the `clear-filters-button` while preserving label, disabled binding, class, and click handler. |
| 1.2 | Apply yellow button appearance | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Set the button to a yellow variant using PrimeNG (`severity="warn"`) and add scoped SCSS only if necessary to fine-tune the visual result without affecting other buttons. |
| 1.3 | Preserve existing reset logic | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no behavioral changes are needed because `clearAllFilters()` and `filtersChanged()` already implement the required functionality. |
| 1.4 | Update unit tests for visual contract | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing label assertion for `Wyczyść` and add/update assertions so the test reflects the yellow-button implementation path and ensures the obsolete inline blue style is not expected anymore. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Replace the current inline blue styling approach for the `Wyczyść` button with a yellow-styled button configuration. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add or adjust scoped styles for the clear button only if PrimeNG warn styling needs refinement. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update tests to validate the unchanged label and the new styling approach. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements