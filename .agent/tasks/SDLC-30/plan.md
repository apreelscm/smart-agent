# SDLC-30: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only update on the offers list screen: the existing `Wyczyść` filter button should change from its current light blue styling to a green styling. The button must remain in the same location, keep the same label, click handler, and disabled-state behavior, while aligning with the page’s existing Angular/PrimeNG implementation.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it is visually consistent with the requested UI design.
  - Given the offers list page is open / When I view the toolbar actions section / Then the `Wyczyść` button is displayed with green styling

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must be styled green instead of blue.
- FR-002: The button must keep the label `Wyczyść`.
- FR-003: The button must remain bound to `clearAllFilters()`.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button must remain in the `toolbar__actions` area next to the existing action controls.
- FR-006: The implementation should avoid unnecessary logic changes in `offers-home-page.component.ts` because the task is styling-only.
- FR-007: The offers page unit test should be extended or updated to verify the button still renders correctly and that the green styling hook is present in a maintainable way.

### Edge Cases
- EC-001: When the button is disabled because no filters are active, it should still retain the same green visual identity as allowed by the PrimeNG/browser disabled state.
- EC-002: Changing the button color must not affect the click behavior when the button is enabled.
- EC-003: The styling change must not unintentionally affect the adjacent “Nowa oferta” button or other buttons on the page.
- EC-004: The styling should remain responsive and not break the toolbar layout on narrower screens.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green instead of blue
- [ ] The button still displays the label `Wyczyść`
- [ ] Clicking the button still resets filters through `clearAllFilters()`
- [ ] The button disabled/enabled logic remains unchanged
- [ ] No other toolbar buttons inherit the green styling unintentionally
- [ ] Unit tests pass after the styling update

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG. The offers list page is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Relevant codebase findings:
- The target button already exists in `offers-home-page.component.html` with class `clear-filters-button`.
- It currently uses inline styles: `background-color: #cce5ff; color: #004085; border-color: #b8daff;`, which are the source of the current blue appearance.
- The button behavior is already implemented and should remain untouched:
  - click handler: `(click)="clearAllFilters()"`
  - disabled state: `[disabled]="!filtersChanged()"`
- There is an existing page spec in `offers-home-page.component.spec.ts` that already asserts the button label and can be extended to cover the styling hook.
- The project uses component-scoped SCSS and shared tokens in `src/styles/_tokens.scss`, but there is no existing green semantic token specifically for this button, so the smallest-scope change is to move styling from inline HTML into the page SCSS under `.clear-filters-button`.

Recommended implementation:
1. Remove the inline blue styles from the `Wyczyść` button in the HTML template.
2. Add a dedicated `.clear-filters-button` style block in `offers-home-page.component.scss` to apply a green background, text, and border.
3. If PrimeNG button styles require higher specificity, use component-scoped selectors or `:host ::ng-deep` only if necessary; prefer regular component styling first.
4. Keep `offers-home-page.component.ts` unchanged because no behavioral update is needed.
5. Update the page spec to continue checking the label and verify the styling contract in a stable way, preferably by asserting the presence of the `clear-filters-button` class rather than brittle computed-color checks. If the test suite already inspects inline styles, adapt it to the new class-based approach.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in Services space.
- No relevant domain guidance or UI convention was found in SDLC space.

Because no relevant Confluence guidance was found in configured scopes, the plan follows the existing repository patterns directly. No external service or runtime integration is involved.

### Task Breakdown

#### Phase 1: Restyle the clear filters button
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline blue styling from template | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the hard-coded blue `style` attribute from the `Wyczyść` button while preserving label, class, disabled binding, and click handler. |
| 1.2 | Add green button styling in component stylesheet | `src/app/features/offers/pages/offers-home-page.component.scss` | Define or update `.clear-filters-button` styles to render the button with green background, border, and readable foreground color without affecting other buttons. |
| 1.3 | Preserve existing behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no TypeScript changes are required because this ticket only changes presentation. |
| 1.4 | Update unit test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing label assertion for `Wyczyść` and add a maintainable assertion that the clear button uses the expected styling hook/class after moving away from inline styles. |

#### Phase 2: Validate visual isolation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Check toolbar button isolation | `src/app/features/offers/pages/offers-home-page.component.scss`, `src/app/features/offers/pages/offers-home-page.component.html` | Ensure the green styling targets only `.clear-filters-button` and does not bleed into the adjacent add/new-offer button or sort controls. |
| 2.2 | Verify responsive layout remains intact | `src/app/features/offers/pages/offers-home-page.component.scss` | Confirm the styling change does not alter spacing, wrapping, or width behavior in existing responsive breakpoints. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove the inline blue button styling while keeping the existing `Wyczyść` button structure and behavior. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add component-scoped green styles for `.clear-filters-button`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update/extend tests to validate the button still renders correctly and retains the expected styling hook. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements