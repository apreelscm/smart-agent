# SDLC-93: Na liscie ofert zmien przycisk 'Wyczyść wszystkie filtry' na 'Wyczyść'

## Specification

### Overview
This task shortens the existing clear-filters button label on the offers list from `Wyczyść wszystkie filtry` to `Wyczyść`. The change is limited to the user-facing copy on the prepared offers screen and must preserve the current clear-filters behavior, styling intent, placement, and disabled/enabled logic.

### User Stories
- US-001: As a sales agent, I want a shorter clear-filters button label on the offers list, so that the toolbar is simpler and easier to scan.
  - Given I am on the prepared offers screen
  - When the offers toolbar is rendered
  - Then the existing clear-filters action is labeled `Wyczyść` instead of `Wyczyść wszystkie filtry`

### Functional Requirements
- FR-001: The clear-filters action on the offers list toolbar must display the label `Wyczyść`.
- FR-002: The existing button placement in the offers toolbar must remain unchanged.
- FR-003: The existing click handler must remain unchanged and continue clearing filters and sorting as currently implemented.
- FR-004: The existing disabled/enabled state logic for the button must remain unchanged.
- FR-005: The previous label `Wyczyść wszystkie filtry` must no longer appear on the offers list screen.

### Edge Cases
- EC-001: When the button is disabled, it must still show the new label `Wyczyść`.
- EC-002: The shorter label must not break toolbar layout on desktop or responsive views.
- EC-003: Any automated UI assertion that depended on the old label must be updated to the new wording.

### Success Criteria
- [ ] On `/offers`, the toolbar action is labeled `Wyczyść`.
- [ ] The old label `Wyczyść wszystkie filtry` is no longer rendered on the offers page.
- [ ] Clicking the button still triggers the existing clear-filters behavior.
- [ ] No other filtering, sorting, or offer-list behavior changes.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
This is a small UI-copy update in the Angular standalone offers page component. The relevant implementation already exists in `src/app/features/offers/pages/offers-home-page.component.html` and is wired to existing state-management logic in `src/app/features/offers/pages/offers-home-page.component.ts` via `filtersChanged()` and `clearAllFilters()`.

The change should:
- modify only the button `label` in the offers toolbar template from `Wyczyść wszystkie filtry` to `Wyczyść`
- keep the existing `clear-filters-button` class, disabled binding, click binding, and inline styling unchanged
- avoid TypeScript or SCSS changes unless a test update requires them

Confluence discovery found:
- No relevant existing API/service to reuse in `Services` space for this UI-only copy change.
- General product context in SDLC space: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). It describes application screens at a high level but does not define a specific copy convention for this button, so the Jira wording should be applied directly.

Repository patterns to follow:
- UI behavior for the offers list is encapsulated in `OffersHomePageComponent`.
- Existing Playwright coverage lives in `tests/e2e/`; a lightweight assertion for visible user copy fits current repo structure.

### Task Breakdown

#### Phase 1: Update offers toolbar copy
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Change clear-filters button label | `src/app/features/offers/pages/offers-home-page.component.html` | Replace the current PrimeNG button `label` value `Wyczyść wszystkie filtry` with `Wyczyść`, leaving bindings and styling intact. |

#### Phase 2: Add regression coverage for visible label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add UI assertion for new label | `tests/e2e/offers.spec.ts` | Add a focused Playwright test for `/offers` that verifies the `Wyczyść` button is visible. |
| 2.2 | Guard against old wording | `tests/e2e/offers.spec.ts` | Assert that `Wyczyść wszystkie filtry` is no longer present on the offers page to prevent accidental regressions in displayed copy. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Update the toolbar button label from `Wyczyść wszystkie filtry` to `Wyczyść`. |
| `tests/e2e/offers.spec.ts` | CREATE | Add an end-to-end assertion covering the new button wording on the offers page. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Navigate to `/offers` and confirm the toolbar button reads `Wyczyść`
5. [ ] Confirm the old text `Wyczyść wszystkie filtry` is not rendered on the page
6. [ ] Click the button after changing a filter and confirm existing reset behavior still works