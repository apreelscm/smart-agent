# SDLC-72: Przycisk Clear powienien się nazywac Wyczyść

## Specification

### Overview
This task changes the clear-filters button label on the offers screen from `Clear` to `Wyczyść` so the UI uses the expected Polish wording. The change is presentation-only and must preserve the current filter reset behavior, placement, styling, and enabled/disabled logic.

### User Stories
- US-001: As a sales agent, I want the clear-filters button on the offers screen to be labeled `Wyczyść`, so that the interface uses consistent Polish wording.
  - Given I open the offers page
  - When the filter toolbar is displayed
  - Then the clear button label is `Wyczyść`

- US-002: As a sales agent, I want the `Wyczyść` button to work exactly like the current `Clear` button, so that I can still reset all filters in one click.
  - Given I changed at least one filter on the offers page
  - When I click `Wyczyść`
  - Then the filters return to their default values and the full offers list is restored

### Functional Requirements
- FR-001: Replace the visible label of the offers clear-filters button from `Clear` to `Wyczyść`.
- FR-002: Keep the button in the existing toolbar actions area in `src/app/features/offers/pages/offers-home-page.component.html`.
- FR-003: Preserve the existing `(click)="clearAllFilters()"` binding.
- FR-004: Preserve the existing disabled state controlled by `[disabled]="!filtersChanged()"`.
- FR-005: Do not change the filter-reset logic in `src/app/features/offers/pages/offers-home-page.component.ts`.
- FR-006: Update Playwright coverage in `tests/e2e/offers-filters.spec.ts` to assert the new label `Wyczyść`.
- FR-007: Ensure the old label `Clear` is no longer asserted or expected on the offers page.

### Edge Cases
- EC-001: When no filters are changed, the `Wyczyść` button remains disabled.
- EC-002: When a search produces zero results, clicking `Wyczyść` restores the default offers list state.
- EC-003: When the app opens at `/` and redirects to `/offers`, the button must still render as `Wyczyść`.
- EC-004: The label change must be limited to the offers page button and must not alter unrelated labels or PrimeNG translation config.

### Success Criteria
- [ ] The offers toolbar button displays `Wyczyść` instead of `Clear`.
- [ ] Clicking `Wyczyść` still resets search, status, product, sort field, and sort direction to defaults.
- [ ] The button remains disabled when no filters have changed.
- [ ] The offers page no longer expects `Clear` for this button.
- [ ] Playwright e2e coverage passes with the updated label and existing reset behavior.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 application using standalone components, signals, computed state, and PrimeNG. The offers list page is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`

Current code inspection shows:
- the offers toolbar button is already wired to `clearAllFilters()`
- its disabled state is driven by `filtersChanged()`
- the current hardcoded label in the template is `Clear`
- Playwright tests in `tests/e2e/offers-filters.spec.ts` currently look up the button by the `Clear` label

The implementation should therefore be a minimal template-text update plus regression-test alignment:
1. Change the offers-page button label from `Clear` to `Wyczyść` in the template.
2. Leave `OffersHomePageComponent` TypeScript logic unchanged, because `clearAllFilters()` already resets all relevant filters.
3. Update Playwright selectors and expectations from `Clear` to `Wyczyść` while preserving the same behavioral assertions.

Relevant codebase patterns supporting this approach:
- UI labels are defined inline in templates, e.g. the policies page already uses `label="Wyczyść"` in `src/app/features/policies/pages/policies-home-page.component.html`.
- Root navigation redirects `/` to `/offers` in `src/app/app.routes.ts`, and the current e2e test already validates entry via that route.
- PrimeNG Polish translation already defines `clear: 'Wyczyść'` in `src/app/core/config/primeng-pl.ts`, so this task should align the hardcoded offers button label with the existing Polish UI convention rather than introduce a new pattern.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space.
- No relevant domain guidance was found in the `SDLC` space for this label-only change.

### Task Breakdown

#### Phase 1: Update the offers-page button label
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Locate the clear-filters control | `src/app/features/offers/pages/offers-home-page.component.html` | Confirm the toolbar action button with `(click)="clearAllFilters()"` is the target control. |
| 1.2 | Rename the button label | `src/app/features/offers/pages/offers-home-page.component.html` | Change `label="Clear"` to `label="Wyczyść"` without altering styling, layout, click handling, or disabled binding. |
| 1.3 | Verify no logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm `clearAllFilters()` already resets search, status, product, sort field, and sort direction to their defaults. |

#### Phase 2: Update regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update button selectors | `tests/e2e/offers-filters.spec.ts` | Replace `getByRole('button', { name: 'Clear' })` lookups with `Wyczyść`. |
| 2.2 | Update visible-label assertions | `tests/e2e/offers-filters.spec.ts` | Assert the offers page shows `Wyczyść` and no longer expects `Clear` for this button. |
| 2.3 | Preserve reset-behavior coverage | `tests/e2e/offers-filters.spec.ts` | Keep the existing flow that changes a filter, verifies the button enables, clicks it, and confirms the default list state is restored. |
| 2.4 | Keep redirect-entry validation | `tests/e2e/offers-filters.spec.ts`, `src/app/app.routes.ts` | Continue testing entry via `/` to ensure the label is correct after redirect to `/offers`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Rename the offers clear-filters button label from `Clear` to `Wyczyść`. |
| `tests/e2e/offers-filters.spec.ts` | MODIFY | Update Playwright selectors and assertions to the `Wyczyść` label while preserving clear-filter behavior checks. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check on `/offers` shows the button label `Wyczyść`
5. [ ] Clicking `Wyczyść` resets the offers toolbar filters and restores the default list state