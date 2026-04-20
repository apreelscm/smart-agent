# SDLC-29: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only update on the offers list page: the existing `Wyczyść` filter-reset button should change from its current light blue styling to a green styling. The button must remain in the same location, keep the same label, preserve its disabled-state behavior, and continue to invoke the existing `clearAllFilters()` logic with no functional regression.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the requested visual design.
  - Given the offers list page is open / When I view the toolbar actions / Then I see the `Wyczyść` button styled in green

### Functional Requirements
- FR-001: On the offers list page, the `Wyczyść` button must use green visual styling instead of the current blue styling.
- FR-002: The button label must remain `Wyczyść`.
- FR-003: The button must remain bound to the existing `clearAllFilters()` method.
- FR-004: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-005: The button position in the `toolbar__actions` section must remain unchanged.
- FR-006: Styling should be implemented using the codebase’s existing SCSS approach rather than expanding inline styling further.
- FR-007: The offers page unit test should continue verifying the presence of the `Wyczyść` button label after the style change.
- FR-008: The old blue background, text, and border styling must no longer be applied to the `Wyczyść` button.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still appear as the `Wyczyść` button and remain visually consistent with the green variant.
- EC-002: When filters are active, clicking `Wyczyść` must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: The green styling must not unintentionally affect the adjacent “Nowa oferta” button or other PrimeNG buttons on the page.
- EC-004: Responsive toolbar layouts must preserve the button appearance in both desktop and stacked mobile layouts.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green
- [ ] The button label remains `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] No other buttons on the offers page inherit the new green style unintentionally
- [ ] The offers page unit test passes

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, PrimeNG UI components, and per-component SCSS styling. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Current codebase findings:
- The `Wyczyść` button already exists in `src/app/features/offers/pages/offers-home-page.component.html`.
- The reset behavior is already implemented in `clearAllFilters()` in `offers-home-page.component.ts`.
- The enabled/disabled state is already controlled by `filtersChanged()`.
- The button is currently styled with an inline `style` attribute:
  - background-color: `#cce5ff`
  - color: `#004085`
  - border-color: `#b8daff`
- Component styling conventions favor SCSS in `offers-home-page.component.scss`, with targeted overrides when needed.

Implementation should therefore stay tightly scoped to presentation:
1. remove the inline blue style from the `clear-filters-button` in the HTML template,
2. add a dedicated SCSS rule for `.clear-filters-button` in `offers-home-page.component.scss` to define green background, border, and text colors,
3. if PrimeNG styling specificity requires it, use a scoped `:host ::ng-deep` selector only for this button class so the override remains localized,
4. retain the existing test coverage for the `Wyczyść` button label, with no behavioral test expansion unless required by breakage.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in [Services](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22oferty%22+OR+text+%7E+%22offers%22+OR+text+%7E+%22wyczysc%22+OR+text+%7E+%22clear%22%29+AND+space+in+%28Services%29).
- No relevant domain guidance or convention was found in [SDLC](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22oferty%22+OR+text+%7E+%22offers%22+OR+text+%7E+%22button%22+OR+text+%7E+%22ui%22+OR+text+%7E+%22kolor%22%29+AND+space+in+%28SDLC%29).

No runtime integrations, repositories, models, or services need to change for this task.

### Task Breakdown

#### Phase 1: Update clear button styling on offers page
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline blue styling | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the current inline `style` attribute from the `clear-filters-button` so color styling is owned by component SCSS. |
| 1.2 | Add green button styles | `src/app/features/offers/pages/offers-home-page.component.scss` | Add a scoped style for `.clear-filters-button` defining green background, text, and border colors, plus any required hover/disabled adjustments consistent with PrimeNG rendering. |
| 1.3 | Preserve existing behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no code changes are required in `clearAllFilters()` or `filtersChanged()` because this task is styling-only. |
| 1.4 | Validate existing test coverage | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Keep the existing label assertion for `Wyczyść`; update only if template refactoring affects selector or rendered structure. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove inline blue color styling from the `Wyczyść` button while preserving label, click binding, class, and disabled state. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add localized green styling for the `clear-filters-button`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Adjust only if needed to keep the existing `Wyczyść` button assertion stable after template styling changes. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements