# SDLC-88: Na liście ofert kolumna z okresem ochrony powinna byc przed kolumna Aktualizacja.

## Specification

### Overview
This task reorders the metadata shown for each offer on the offers list so that **„Okres ochrony”** appears **before** **„Aktualizacja”**. The coverage-period feature is already implemented in the codebase; this ticket is a UI ordering correction on the `/offers` screen.

### User Stories
- US-001: As a sales agent, I want the protection period shown before the update date on the offers list, so that the row information follows the expected business order.
  - Given I open the offers list
  - When an offer row is rendered
  - Then the `Okres ochrony` field appears before `Aktualizacja`

- US-002: As a user reviewing different offer types, I want the metadata order to stay consistent, so that motor and crop offers are scanned the same way.
  - Given the list contains motor and/or crop offers
  - When the metadata grid is displayed
  - Then `Okres ochrony` is positioned before `Aktualizacja` for every row

### Functional Requirements
- FR-001: The offers list in `src/app/features/offers/pages/offers-home-page.component.html` must render the `Okres ochrony` metadata block before the `Aktualizacja` metadata block.
- FR-002: The displayed coverage-period value must remain unchanged and continue using the existing `coveragePeriodLabel()` binding.
- FR-003: No changes may be made to coverage-period calculation logic in `src/app/features/offers/utils/coverage-period.util.ts`.
- FR-004: No changes may be made to offer repository payloads, models, or API integrations for this task.
- FR-005: The visual order must remain correct across existing responsive breakpoints defined in `offers-home-page.component.scss`.
- FR-006: The reordered metadata must work for both motor and crop offers without changing their existing primary metadata content.

### Edge Cases
- EC-001: On narrower layouts where metadata wraps to multiple rows, `Okres ochrony` must still precede `Aktualizacja` in rendered order.
- EC-002: Long coverage-period text must remain readable after reordering and must not overlap adjacent metadata cells.
- EC-003: If the list is empty after filtering, the empty state remains unchanged.
- EC-004: Existing row actions, status tags, sorting, filtering, and navigation must remain unaffected.

### Success Criteria
- [ ] On `/offers`, each offer row shows `Okres ochrony` before `Aktualizacja`.
- [ ] The coverage-period value still matches the currently implemented formatted string.
- [ ] The order is correct for both motor and crop offers.
- [ ] Existing unit and e2e coverage verifies the new field order.
- [ ] No repository, model, or route behavior changes are introduced.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone-component app using signals, computed state, and PrimeNG. The offers list is implemented in `src/app/features/offers/pages/offers-home-page.component.ts` and rendered as card-style rows in `offers-home-page.component.html`, with metadata arranged in `.offer-row__meta-grid`.

The current codebase already includes the `Okres ochrony` feature:
- `coveragePeriodLabel` is exposed from `OffersHomePageComponent`
- formatting logic lives in `src/app/features/offers/utils/coverage-period.util.ts`
- rendering tests already exist in `src/app/features/offers/pages/offers-home-page.component.spec.ts`
- e2e coverage exists in `tests/e2e/offers-coverage-period.spec.ts`

This ticket should therefore be implemented as a **presentation-order change**, not a business-logic change:
1. Move the coverage-period metadata block above the update-date block in `offers-home-page.component.html`.
2. Adjust the grid styling in `offers-home-page.component.scss` so the new order is visually correct and still readable at desktop and responsive breakpoints. The current `.offer-row__meta-item--coverage { grid-column: span 2; }` behavior may need refinement so the reordered item still reads as preceding `Aktualizacja`, not as a visually detached trailing field.
3. Extend component and Playwright regression tests to assert metadata order, not just label/value presence.

Domain context found in Confluence confirms this screen belongs to the smart-agent sales workflow and should preserve the existing screen behavior: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany).

No existing offer-related service, endpoint, or integration reference was found in the configured `Services` space — this remains a frontend-only layout change with no external integration work.

### Task Breakdown

#### Phase 1: Reorder offer-row metadata
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Move coverage-period block before update block | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the two metadata sections so `Okres ochrony` is rendered before `Aktualizacja` inside `.offer-row__meta-grid`. |
| 1.2 | Preserve visual column order in the grid | `src/app/features/offers/pages/offers-home-page.component.scss` | Update grid sizing/span rules so the reordered metadata remains visually correct and readable on desktop and existing responsive breakpoints. |

#### Phase 2: Add regression coverage for field order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component spec to verify metadata order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert that the metadata labels within an offer row place `Okres ochrony` before `Aktualizacja`, while keeping the existing rendered-value assertion. |
| 2.2 | Extend e2e spec to verify user-visible order | `tests/e2e/offers-coverage-period.spec.ts` | Add a Playwright assertion that the metadata labels are rendered in the expected sequence on the offers page, covering the actual browser layout/DOM. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the `Okres ochrony` and `Aktualizacja` metadata blocks in the offer-row grid. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY | Adjust grid/span styling so the reordered field sequence remains visually correct and readable. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression assertions for metadata label order in the component template. |
| `tests/e2e/offers-coverage-period.spec.ts` | MODIFY | Add end-to-end verification that `Okres ochrony` appears before `Aktualizacja` on the offers list. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/offers` and confirm each row shows `Okres ochrony` before `Aktualizacja`
5. [ ] Confirm the coverage-period string remains unchanged after the reorder
6. [ ] Confirm the order is correct for available motor and crop offers
7. [ ] Confirm filtering to zero results still shows the unchanged empty state