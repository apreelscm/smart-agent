# SDLC-20: Na ekranie listy ofert należy zmienić nazwę przycisku “Reset” na “Clear”

## Specification

### Overview
This task delivers a copy-only UI update on the offers list page: the existing filter reset button should display `Clear` instead of `Reset`. The button must remain in the same place, keep the same styling, disabled state logic, and continue to invoke the existing `clearAllFilters()` behavior.

### User Stories
- US-001: As a sales agent, I want the filter reset button on the offers list to say `Clear`, so that the UI wording matches the requested terminology.
  - Given the offers list page is open / When I look at the toolbar actions / Then I see a button labeled `Clear`

### Functional Requirements
- FR-001: On the offers list page, the filter reset button must display the label `Clear` instead of `Reset`.
- FR-002: The button must remain bound to the existing `clearAllFilters()` method.
- FR-003: The button must preserve its current enabled/disabled behavior based on `filtersChanged()`.
- FR-004: The button position in the `toolbar__actions` section must remain unchanged.
- FR-005: Existing button styling, including the `clear-filters-button` class and inline styles, must remain unchanged unless required for the text update.
- FR-006: The offers page unit test must be updated to verify the new `Clear` label.
- FR-007: The outdated `Reset` label must no longer be asserted in the offers page test for this button.

### Edge Cases
- EC-001: When no filters are active and the button is disabled, it must still display `Clear`.
- EC-002: When filters are active, clicking `Clear` must still restore default values: empty search term, status `ALL`, product `ALL`, sort field `ISSUE_DATE`, and sort direction `DESC`.
- EC-003: No stale `Reset` label should remain in the offers page template or related unit test assertions.

### Success Criteria
- [ ] The offers list page shows a button labeled `Clear`
- [ ] The button no longer displays `Reset`
- [ ] Clicking the button still resets filters through the existing implementation
- [ ] The button enabled/disabled behavior remains unchanged
- [ ] The offers page unit test passes with the new label

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components and PrimeNG. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Current codebase findings:
- The reset behavior already exists in `clearAllFilters()` in `offers-home-page.component.ts`.
- The button state logic already exists in the computed signal `filtersChanged`.
- The current template in `offers-home-page.component.html` already renders the button with label `Reset`.
- The current unit test in `offers-home-page.component.spec.ts` already checks this rendered label.

Therefore, implementation should be minimal and limited to:
1. updating the button `label` in the template from `Reset` to `Clear`,
2. updating the existing unit test to expect `Clear` and ensure the old `Reset` label is no longer present.

Confluence discovery results:
- No existing service or API documentation relevant to this UI copy change was found in [Services](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22ofert%22+OR+text+%7E+%22offers+list%22+OR+text+%7E+%22reset%22+OR+text+%7E+%22clear%22%29+AND+space+in+%28Services%29).
- No relevant domain guidance or convention was found in [SDLC](https://apreel.atlassian.net/wiki/rest/api/search?expand=body.view%2Cspace&cql=%28text+%7E+%22offers+list%22+OR+text+%7E+%22button+label%22+OR+text+%7E+%22ui+copy%22+OR+text+%7E+%22clear%22%29+AND+space+in+%28SDLC%29).

No runtime integrations, repositories, models, or services need to change for this task.

### Task Breakdown

#### Phase 1: Update offers list button copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change filter reset button label | `src/app/features/offers/pages/offers-home-page.component.html` | Update the `clear-filters-button` PrimeNG button label from `Reset` to `Clear` without changing click bindings, disabled state binding, class name, or styles. |
| 1.2 | Preserve existing reset behavior | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no code changes are required in `clearAllFilters()` or `filtersChanged()` because this task is copy-only. |
| 1.3 | Update unit test expectations | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Adjust the existing button rendering test to expect `Clear` and verify that `Reset` is no longer present in the rendered output. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Change the visible filter reset button label from `Reset` to `Clear`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Update the unit test assertions to validate the new `Clear` label. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements