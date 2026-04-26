# SDLC-77: Na ekranie listy ofert okres ochrony powinien byc przed kolumna Aktualizacja

## Specification

### Overview
This task corrects the visual order of metadata fields on the offers list so that `Okres ochrony` is rendered before `Aktualizacja` in each offer row. The repository already contains the protection-period feature on the offers page, but the current template places it after the update field. The change is limited to the existing offers-list UI and its regression coverage.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the row matches the expected business column order.
  - Given I open the offers list / When offer rows are rendered / Then `Okres ochrony` is displayed before `Aktualizacja`

- US-002: As a product owner, I want the existing offers-list content and actions to remain unchanged, so that only the field order is corrected without side effects.
  - Given the offers list is loaded / When the metadata order is updated / Then filtering, sorting, navigation, premium display, and actions still work as before

### Functional Requirements
- FR-001: In `src/app/features/offers/pages/offers-home-page.component.html`, the `Okres ochrony` metadata block must appear before the `Aktualizacja` metadata block within `.offer-row__meta-grid`.
- FR-002: The displayed protection-period value must continue to use the existing `protectionPeriodDisplay` binding.
- FR-003: The displayed update date/time must continue to use the existing `offer.updatedAt` bindings and formatting.
- FR-004: No changes may be made to the offers data source, protection-period calculation logic, filtering, sorting, status transitions, or navigation behavior.
- FR-005: Automated tests must verify the rendered order so the regression does not reappear.

### Edge Cases
- EC-001: The order must be correct for both `MOTOR` and `CROP` offers, since both use the same `.offer-row__meta-grid`.
- EC-002: Responsive layouts must preserve the same source order when the grid collapses from 5 columns to 3 or 1 column.
- EC-003: Existing tests that only verify field presence are insufficient; regression coverage must assert relative order, not just visibility.

### Success Criteria
- [ ] Each offer row renders `Okres ochrony` before `Aktualizacja`
- [ ] The values shown in both fields remain unchanged
- [ ] No regression is introduced in offer-row layout or actions
- [ ] Unit/component and/or e2e coverage verifies the new order

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a targeted UI-order fix in the Angular 20 standalone offers page. The current offers list is implemented in:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The repo already includes the protection-period feature from the earlier implementation:
- `protectionPeriodDisplay` is already computed in `offers-home-page.component.ts`
- `Okres ochrony` is already rendered in the template
- `.offer-row__meta-grid` is already configured for 5 metadata cells in `offers-home-page.component.scss`

So the runtime behavior does not need new business logic. The fix should reuse the existing implementation and only reorder the two metadata blocks in the HTML template. Regression coverage should be extended to validate DOM order, because the current tests in:
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`
- `tests/e2e/offers-protection-period.spec.ts`

verify presence/value of `Okres ochrony`, but not that it appears before `Aktualizacja`.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space — no service reuse is needed.
- The `SDLC` space search returned the general context page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which gives product context but does not impose additional implementation constraints for this UI-order fix.

### Task Breakdown

#### Phase 1: Reorder the offers-row metadata blocks
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Swap metadata block order in template | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` block so it is declared immediately before the existing `Aktualizacja` block inside `.offer-row__meta-grid`. |
| 1.2 | Preserve existing bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `{{ protectionPeriodDisplay }}` and the existing `offer.updatedAt | date` bindings unchanged so only visual order changes. |

#### Phase 2: Add regression coverage for relative order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Strengthen component test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the existing spec to assert that, within each `.offer-row__meta-grid`, the `Okres ochrony` cell appears before the `Aktualizacja` cell. |
| 2.2 | Strengthen e2e test | `tests/e2e/offers-protection-period.spec.ts` | Add a DOM-order assertion on the first row, and optionally multiple rows, confirming `Okres ochrony` precedes `Aktualizacja` in the rendered metadata sequence. |
| 2.3 | Keep current protection-period assertions | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Retain existing checks for label presence and formatted value so the order fix does not weaken previous coverage. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the existing metadata cells so `Okres ochrony` comes before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add assertions for metadata-field order in each rendered offer row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add end-to-end verification that the rendered offers-row order places `Okres ochrony` before `Aktualizacja`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes successfully
5. [ ] `npm test` passes with the updated `offers-home-page.component.spec.ts`
6. [ ] `npm run e2e` passes with the updated `offers-protection-period.spec.ts`
7. [ ] Manual check on `/offers` confirms `Okres ochrony` is visually before `Aktualizacja` for both motor and crop offers