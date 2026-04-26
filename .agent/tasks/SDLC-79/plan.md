# SDLC-79: Na ekranie listy ofert okres ochrony powinien byc przed kolumna Aktualizacja

## Specification

### Overview
Task scope is a UI ordering rule on the offers list: `Okres ochrony` must be shown before `Aktualizacja` in the metadata area of each offer row. Based on the inspected repository, this behavior is already implemented in the current tree, so the plan focuses on preserving that order, avoiding unnecessary business-logic changes, and reconciling any branch drift if SDLC-79 targets a branch that does not yet contain the existing fix.

### User Stories
- US-001: As an agent sprzedaży, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the row layout matches the expected business order.
  - Given the offers list is displayed / When an offer row is rendered / Then `Okres ochrony` is shown before `Aktualizacja`

- US-002: As a product owner, I want this correction to affect only field order, so that filtering, sorting, navigation, status actions, and premium display remain unchanged.
  - Given the offers list is loaded / When the metadata order is validated or corrected / Then all existing list behaviors continue to work as before

### Functional Requirements
- FR-001: In the offers list row metadata grid, the `Okres ochrony` block must appear before the `Aktualizacja` block.
- FR-002: The protection-period value must continue to use the existing `protectionPeriodDisplay` binding from `OffersHomePageComponent`.
- FR-003: The update date and time must continue to use the existing `offer.updatedAt` date bindings and formatting.
- FR-004: No changes may be introduced to offer filtering, sorting, row actions, navigation, or status-transition behavior.
- FR-005: Regression coverage must verify relative DOM order, not only the presence of both labels.

### Edge Cases
- EC-001: The order must remain correct for both motor and crop offers, because both use the same `.offer-row__meta-grid`.
- EC-002: Responsive layout changes must not alter the source order of metadata cells.
- EC-003: If the target branch differs from the inspected repository and still has the old order, only the template order should change; no data-model or repository changes are needed.
- EC-004: If `protectionPeriodDisplay` or `offer.updatedAt` is blank or fallback-rendered, the label order must still remain `Okres ochrony` before `Aktualizacja`.

### Success Criteria
- [ ] Every offer row renders `Okres ochrony` before `Aktualizacja`
- [ ] Protection-period formatting remains unchanged
- [ ] Update date/time formatting remains unchanged
- [ ] No regressions occur in filtering, sorting, navigation, or actions on the offers page
- [ ] Component-level and e2e regression checks cover the rendered order

### Open Questions
- [NEEDS CLARIFICATION: the inspected repository already contains this behavior in `src/app/features/offers/pages/offers-home-page.component.html` and matching regression tests, and `.agent/tasks/SDLC-77/plan.md` describes the same requirement; should SDLC-79 be treated as verification-only, or does it target another branch/view not represented in the current repo snapshot?]

---

## Implementation Plan

### Technical Approach
The offers list is implemented as an Angular 20 standalone page using PrimeNG controls and template-driven bindings:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

Current repository findings:
- `offers-home-page.component.html` already declares the metadata cells in the correct order: `Pojazd/Uprawy`, `Kanał`, `Wariant`, `Okres ochrony`, `Aktualizacja`.
- `offers-home-page.component.ts` already provides `protectionPeriodDisplay` via `formatProtectionPeriod(new Date())`.
- `src/app/features/offers/pages/offers-home-page.component.spec.ts` already asserts that `Okres ochrony` appears before `Aktualizacja` in every row.
- `tests/e2e/offers-protection-period.spec.ts` already validates the same order end to end.

Therefore, the safest implementation strategy is:
1. Treat the current offers-page structure as the codebase pattern to preserve.
2. If the delivery branch differs from the inspected tree, reconcile it by reordering only the two metadata blocks in `offers-home-page.component.html`.
3. Keep all existing bindings intact and avoid touching repositories, models, or utility logic.
4. Preserve the existing regression approach that inspects `.offer-row__meta-grid > div` label order at both component and Playwright levels.

Confluence findings:
- No existing service/API relevant to this UI-only task was found in the `Services` space — no service reuse is needed.
- Domain context found in [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the application context but does not add extra constraints for this metadata-order fix.

### Task Breakdown

#### Phase 1: Verify and reconcile offers-row metadata order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Confirm current DOM order in target branch | `src/app/features/offers/pages/offers-home-page.component.html` | Verify that the `Okres ochrony` metadata cell is declared immediately before `Aktualizacja` inside `.offer-row__meta-grid`. |
| 1.2 | Reorder template only if branch drift exists | `src/app/features/offers/pages/offers-home-page.component.html` | If the target branch still has the old order, move the existing `Okres ochrony` block ahead of `Aktualizacja` without changing bindings or markup structure beyond position. |
| 1.3 | Preserve existing display logic | `src/app/features/offers/pages/offers-home-page.component.ts`, `src/app/features/offers/utils/protection-period.util.ts` | Leave `protectionPeriodDisplay` and date formatting unchanged; this task does not require business-logic changes. |

#### Phase 2: Preserve regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Keep component-level order assertion authoritative | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Ensure the spec continues to assert index order for `Okres ochrony` and `Aktualizacja` within every `.offer-row__meta-grid`. |
| 2.2 | Keep e2e order assertion authoritative | `tests/e2e/offers-protection-period.spec.ts` | Ensure Playwright coverage continues to validate the same metadata order in rendered rows. |
| 2.3 | Avoid weakening existing checks | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Retain assertions for displayed values so the ticket remains a layout-order fix with regression protection. |

#### Phase 3: Final validation
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Validate offers page behavior after reconciliation | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Run build, unit tests, and e2e checks to confirm no regressions in the list page. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Only if needed on the target branch, keep `Okres ochrony` positioned before `Aktualizacja` in `.offer-row__meta-grid`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Only if needed on the target branch, preserve or restore component regression coverage for metadata order. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Only if needed on the target branch, preserve or restore end-to-end verification of metadata order. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes successfully
5. [ ] `npm test` passes, including `offers-home-page.component.spec.ts`
6. [ ] `npm run e2e` passes, including `tests/e2e/offers-protection-period.spec.ts`
7. [ ] Manual check on `/offers` confirms `Okres ochrony` is visually before `Aktualizacja` for rendered offer rows