# SDLC-75: Bledna kolumna

## Specification

### Overview
This task corrects the column order on the offers list screen so that `Okres ochrony` is displayed before `Aktualizacja`. The current repository already renders both metadata fields on each offer row in `src/app/features/offers/pages/offers-home-page.component.html`, but `Okres ochrony` is placed after `Aktualizacja`. The required change is a presentation-layer reorder only; the displayed protection-period value and existing offers-list behavior must remain unchanged.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the metadata order matches the expected business layout.
  - Given I open the offers list
  - When offer rows are rendered
  - Then the `Okres ochrony` field appears immediately before `Aktualizacja`

- US-002: As a sales agent, I want the existing protection-period value to stay unchanged while its position is corrected, so that I keep seeing the same information in the right place.
  - Given an offer row shows both `Okres ochrony` and `Aktualizacja`
  - When the layout is corrected
  - Then the same protection-period value is preserved and only the column order changes

### Functional Requirements
- FR-001: Reorder the metadata cells in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` is rendered before `Aktualizacja`.
- FR-002: Keep the existing `Okres ochrony` label and current value source (`protectionPeriodDisplay`) unchanged.
- FR-003: Keep the existing `Aktualizacja` label and current formatting (`date: 'dd.MM.yyyy'` and `date: 'HH:mm'`) unchanged.
- FR-004: Preserve the current five-item offer metadata grid and do not alter offer filtering, sorting, navigation, actions, or status transitions.
- FR-005: Preserve the current behavior for both MOTOR and CROP offers.
- FR-006: Add regression coverage that verifies the relative order of `Okres ochrony` and `Aktualizacja` in the offer-row metadata.

### Edge Cases
- EC-001: The corrected order must apply consistently to every visible offer row, not only the first row.
- EC-002: Both MOTOR and CROP rows must keep their product-specific first metadata cell while still placing `Okres ochrony` before `Aktualizacja`.
- EC-003: Reordering DOM nodes must not change the displayed protection-period text or the update-date formatting.
- EC-004: The existing responsive grid in `offers-home-page.component.scss` must continue to work without requiring layout regressions.

### Success Criteria
- [ ] On `/offers`, `Okres ochrony` is rendered before `Aktualizacja` in each offer row.
- [ ] The displayed protection-period value remains unchanged from the current implementation.
- [ ] The displayed update date and time remain unchanged from the current implementation.
- [ ] Existing offers-list filters, sorting, actions, and navigation continue to work without regression.
- [ ] Automated tests verify the corrected metadata order.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a minimal UI-order correction in the existing Angular 20 standalone offers feature.

Current code inspection shows:
- `src/app/features/offers/pages/offers-home-page.component.html` already renders both metadata blocks.
- `src/app/features/offers/pages/offers-home-page.component.ts` already provides `protectionPeriodDisplay` via `formatProtectionPeriod(new Date())`.
- `src/app/features/offers/pages/offers-home-page.component.scss` already supports a five-column `.offer-row__meta-grid`.
- Existing tests cover presence of `Okres ochrony`, but not its relative position versus `Aktualizacja`.

Therefore, the safest implementation is:
1. Reorder only the two metadata `<div>` blocks in the template.
2. Leave TypeScript logic, utility formatting, repositories, and mock data untouched.
3. Extend regression tests to assert order, not just presence.

If any existing service or convention was found in Step 2, explicitly state which parts of the plan reuse it and link the Confluence page. If nothing relevant was found in a configured scope, state that explicitly:
- No existing service/API relevant to this task was found in the `Services` space — no service reuse applies.
- Domain search in `SDLC` returned the general context page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), but it does not define a specific implementation constraint for this column-order fix.

This approach follows the current codebase pattern:
- UI labels and metadata ordering are defined directly in the Angular template.
- The offers row layout is controlled by the existing `.offer-row__meta-grid`.
- Regression coverage for this screen already lives in:
  - `src/app/features/offers/pages/offers-home-page.component.spec.ts`
  - `tests/e2e/offers-protection-period.spec.ts`

### Task Breakdown

#### Phase 1: Correct the offers-row metadata order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Reorder metadata blocks in template | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` metadata `<div>` so it appears immediately before the existing `Aktualizacja` metadata `<div>`. |
| 1.2 | Preserve existing bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `{{ protectionPeriodDisplay }}` and the existing `updatedAt` date pipes unchanged so only order changes. |
| 1.3 | Confirm no logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.ts` | Verify the current `protectionPeriodDisplay` implementation remains valid and requires no TypeScript modification. |

#### Phase 2: Add regression coverage for column order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend unit/component test for metadata order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add an assertion that the metadata labels in each `.offer-row__meta-grid` place `Okres ochrony` before `Aktualizacja`. |
| 2.2 | Extend e2e test for visible order | `tests/e2e/offers-protection-period.spec.ts` | Verify on the rendered offers page that `Okres ochrony` is positioned before `Aktualizacja` in an offer row, while keeping the existing value assertion. |
| 2.3 | Keep existing protection-period assertions | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Preserve current checks confirming the field still renders and still shows the expected protection-period value. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the `Okres ochrony` and `Aktualizacja` metadata cells so the protection period appears first. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression assertions for the corrected metadata-label order in each offer row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add Playwright coverage confirming `Okres ochrony` is displayed before `Aktualizacja` on the offers page. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes without template errors
5. [ ] `npm test` confirms the offers component spec passes with the new order assertion
6. [ ] `npm run e2e` confirms `Okres ochrony` is visible and rendered before `Aktualizacja`
7. [ ] Manual check on `/offers` confirms the metadata order is corrected for both MOTOR and CROP rows