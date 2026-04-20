# PZU-14: Należy zmienić nazwę przycisku “Wyczyść wszystkie filtry” na “Wyczyść”

## Specification

### Overview
Ta zmiana upraszcza etykietę istniejącego przycisku czyszczenia filtrów na liście ofert. Funkcjonalność przycisku pozostaje bez zmian — użytkownik nadal jednym kliknięciem resetuje wszystkie filtry i sortowanie — ale widoczny tekst ma zostać skrócony z „Wyczyść wszystkie filtry” do „Wyczyść”.

### User Stories
- US-001: As a sales agent, I want the filter reset button to have a shorter label, so that the toolbar is clearer and more concise.
  - Given the offers list page is open
  - When I look at the toolbar actions area
  - Then I see the clear-filters button labeled „Wyczyść” instead of „Wyczyść wszystkie filtry”

### Functional Requirements
- FR-001: On the offers home page, the existing clear-filters button label must be changed from `Wyczyść wszystkie filtry` to `Wyczyść`.
- FR-002: The button must keep its current placement in the toolbar actions section.
- FR-003: The button must keep its current disabled/enabled behavior based on `filtersChanged()`.
- FR-004: Clicking the button must continue to call the existing `clearAllFilters()` logic without behavioral changes.
- FR-005: Existing button styling, including the custom light-blue inline styling currently used, must remain unchanged unless required only to support the label rename.

### Edge Cases
- EC-001: If filters are unchanged and the button is disabled, the new label must still render as `Wyczyść`.
- EC-002: If filters are active and the button is enabled, clicking `Wyczyść` must still reset search, status, product, sort field, and sort direction to defaults.
- EC-003: The shorter label must not break toolbar layout in responsive views.

### Success Criteria
- [ ] The offers page toolbar shows the button label `Wyczyść`.
- [ ] The old label `Wyczyść wszystkie filtry` is no longer rendered on the offers page.
- [ ] The button remains bound to the existing `clearAllFilters()` action.
- [ ] The button enabled/disabled behavior remains unchanged.
- [ ] Existing offers page tests continue to pass, and test coverage is updated if the label is asserted.

### Open Questions
- None identified.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone application. The relevant implementation already exists in the offers feature:
- component logic in `src/app/features/offers/pages/offers-home-page.component.ts`
- template in `src/app/features/offers/pages/offers-home-page.component.html`
- tests in `src/app/features/offers/pages/offers-home-page.component.spec.ts`

The Jira scope is limited to a text change only. The current implementation already contains:
- `filtersChanged` computed signal,
- `clearAllFilters()` method,
- a toolbar button in the template with label `Wyczyść wszystkie filtry`.

So the implementation should be a minimal template update, with optional test adjustment only if UI text assertions are added or already expected elsewhere.

Confluence discovery result:
- No relevant existing service/API documentation was found in `Services`.
- No relevant domain guidance or convention page was found in `PZU`.

Because this is a pure UI copy change, no new service, repository, model, or runtime behavior should be introduced.

### Task Breakdown

#### Phase 1: Update button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change clear-filters button text | `src/app/features/offers/pages/offers-home-page.component.html` | Replace the existing button `label` value from `Wyczyść wszystkie filtry` to `Wyczyść` while keeping all other bindings and styling intact. |
| 1.2 | Review component logic for impact | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm no TypeScript changes are required because behavior is already implemented via `filtersChanged()` and `clearAllFilters()`. |
| 1.3 | Update or extend UI test coverage if needed | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | If the spec suite verifies rendered labels, update assertions to the new text; otherwise add a focused assertion that the button renders `Wyczyść` and still works with current state bindings. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Rename the existing clear-filters button label to `Wyczyść`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Adjust or add test coverage for the updated visible button label, if needed. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements