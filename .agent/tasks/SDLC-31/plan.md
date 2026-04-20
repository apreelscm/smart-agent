# SDLC-31: Na ekranie ofert zmien kolor przycisku wyczysc na zielony

## Specification

### Overview
This task delivers a visual-only UI adjustment on the offers list page: the existing `Wyczyść` button in the offers toolbar should change from its current blue styling to a green styling. The button must keep its current placement, label, click handler, and disabled-state behavior, while aligning its appearance with the requested color change.

### User Stories
- US-001: As a sales agent, I want the `Wyczyść` button on the offers screen to be green, so that it matches the desired visual design.
  - Given the offers list page is open / When I look at the toolbar actions / Then the `Wyczyść` button is rendered with green visual styling

### Functional Requirements
- FR-001: The offers list page must render the `Wyczyść` button with green background, text, and border styling instead of the current blue styling.
- FR-002: The button must remain bound to the existing `clearAllFilters()` method.
- FR-003: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-004: The button label must remain `Wyczyść`.
- FR-005: The button must keep the existing `clear-filters-button` class so current selectors and tests remain stable.
- FR-006: Styling for the button should be moved out of the inline `style` attribute into component SCSS so the color change is maintainable and consistent with existing codebase styling patterns.
- FR-007: The offers page unit test suite must be extended or updated to verify the button is still rendered correctly after the styling change.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, the button should still retain the intended green styling as much as PrimeNG disabled styles allow without breaking disabled affordance.
- EC-002: Clicking the green `Wyczyść` button must still restore default filter values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: No obsolete blue inline style should remain in the template after the change.
- EC-004: The styling change must not affect the neighboring currency switcher or new-offer action button layout in `toolbar__actions`.

### Success Criteria
- [ ] The offers list page shows the `Wyczyść` button in green instead of blue
- [ ] The button still displays `Wyczyść`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] No inline color style remains on the button in the template
- [ ] The offers page unit test passes after the update

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

Repository analysis shows:
- The `Wyczyść` button already exists in `offers-home-page.component.html`.
- It is currently styled with an inline `style` attribute: `background-color: #cce5ff; color: #004085; border-color: #b8daff;`, which is blue.
- The reset behavior is already implemented in `clearAllFilters()` in `offers-home-page.component.ts`.
- The enabled/disabled state is already controlled by the `filtersChanged` computed signal.
- Existing tests already assert the button label and presence through the `.clear-filters-button` selector.

For this task, the implementation should be limited to a maintainable style refactor and visual color update:
1. remove the inline blue style from the template,
2. add scoped SCSS rules for `.clear-filters-button` to render the button in green,
3. if needed, use `:host ::ng-deep` selectors to target PrimeNG-generated button structure consistently,
4. keep component logic unchanged,
5. keep or lightly extend the existing unit test so it continues validating button presence and label while preserving task scope.

Confluence discovery results:
- No existing service or API documentation relevant to this UI styling change was found in the Services space: https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22ofert%22+OR+text+%7E+%22wyczysc%22+OR+text+%7E+%22clear%22+OR+text+%7E+%22button%22+OR+text+%7E+%22ui%22%29+AND+space+in+%28Services%29
- No relevant domain guidance or UI convention was found in the SDLC space: https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22ofert%22+OR+text+%7E+%22wyczysc%22+OR+text+%7E+%22clear%22+OR+text+%7E+%22button%22+OR+text+%7E+%22ui%22+OR+title+%7E+%22frontend%22%29+AND+space+in+%28SDLC%29

No existing service reuse applies because this is a local UI-only change.

### Task Breakdown

#### Phase 1: Update clear button styling
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Remove inline button color styles | `src/app/features/offers/pages/offers-home-page.component.html` | Delete the inline blue `style` attribute from the `Wyczyść` button while preserving its label, class, disabled binding, and click binding. |
| 1.2 | Add green scoped styling for clear button | `src/app/features/offers/pages/offers-home-page.component.scss` | Introduce component-level styles for `.clear-filters-button` to render a green background, green border, and readable contrasting text, following existing SCSS organization in the page stylesheet. |
| 1.3 | Handle PrimeNG button structure if necessary | `src/app/features/offers/pages/offers-home-page.component.scss` | If PrimeNG encapsulation prevents direct styling, add narrowly scoped `:host ::ng-deep` rules only for `.clear-filters-button` and its interactive states (`hover`, `focus`, `disabled`) to keep the green appearance stable. |
| 1.4 | Preserve existing reset logic | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no TypeScript logic changes are required because `clearAllFilters()` and `filtersChanged()` already satisfy the task behavior. |
| 1.5 | Keep test coverage aligned | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Retain the existing button rendering assertion and, if useful, add a focused assertion that the button still uses the `clear-filters-button` selector and `Wyczyść` label after template cleanup. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Remove the inline blue color styling from the `Wyczyść` button while keeping behavior unchanged. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Add maintainable green styling for the clear-filters button, including any required PrimeNG-specific overrides. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Keep or slightly extend the existing test to validate the button remains present with the expected label and selector. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements