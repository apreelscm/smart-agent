# SDLC-106: Na liscie ofert kolumna okres ochrony powinna byc przedostatnia przed kolumna Aktualizacja

## Specification

### Overview
Ta zmiana porządkuje układ metadanych na liście ofert. Kolumna `Okres ochrony`, która już istnieje na ekranie ofert, ma zostać przeniesiona tak, aby była wyświetlana bezpośrednio przed kolumną `Aktualizacja`. Celem jest dostosowanie kolejności pól do oczekiwanego układu biznesowego bez zmiany logiki wyliczania okresu ochrony, filtrowania, sortowania ani akcji na ofercie.

### User Stories
- US-001: As a sales agent, I want the protection period column to appear immediately before the update column on the offers list, so that the row layout matches the expected business order.
  - Given the offers list is visible / When an offer row is rendered / Then `Okres ochrony` is shown before `Aktualizacja`.

### Functional Requirements
- FR-001: Reorder the metadata cells in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` is displayed immediately before `Aktualizacja`.
- FR-002: Keep the existing labels, values, and helper bindings unchanged:
  - `Okres ochrony` must still use `coveragePeriodLabel()`.
  - `Aktualizacja` must still use `offer.updatedAt | date`.
- FR-003: Preserve the existing five-cell layout inside `.offer-row__meta-grid`.
- FR-004: The change must affect both desktop and responsive layouts consistently, following DOM order.
- FR-005: Existing offer actions, currency switching, filters, sorting, and navigation must continue to work unchanged.

### Edge Cases
- EC-001: On narrower breakpoints where `.offer-row__meta-grid` collapses to fewer columns or a single column, the visual order must still remain `... Wariant -> Okres ochrony -> Aktualizacja`.
- EC-002: Crop and motor offers must use the same reordered metadata sequence.
- EC-003: Empty-state rendering must remain unchanged when no offers are visible.

### Success Criteria
- [ ] On the offers list, `Okres ochrony` is rendered directly before `Aktualizacja` in every offer row.
- [ ] The values shown in both fields remain unchanged after the reorder.
- [ ] Responsive layouts preserve the same field sequence.
- [ ] Existing unit/e2e coverage is updated to verify the new order.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The repository is an Angular 20 standalone-component app. The offers list screen is implemented in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The relevant existing pattern is that the row metadata area is rendered as sequential `<div>` cells inside `.offer-row__meta-grid` in `offers-home-page.component.html`, while layout is controlled by CSS grid in `offers-home-page.component.scss`. Because the grid follows DOM order, the smallest and safest fix is to reorder the two existing metadata blocks in the template only, without changing component logic or styles.

Existing code already includes:
- `coveragePeriodLabel()` in `src/app/features/offers/pages/offers-home-page.component.ts`
- the `Okres ochrony` cell in the offers row template
- unit and Playwright tests for protection-period rendering

This ticket should therefore reuse the current implementation and only correct presentation order.

Confluence discovery outcome:
- **API/SERVICE scope (`Services`)**: No relevant existing service or endpoint was found for this UI-only column-order task, so no service reuse applies.
- **DOMAIN scope (`SDLC`)**: The screen overview page [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany) provides general product context for the offers cockpit, but no stricter ADR or UI convention for this exact column order was found. The implementation will therefore follow the current offers-page template structure already used in the repo.

### Task Breakdown

#### Phase 1: Reorder metadata cells in the offers row
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Move protection-period block before update block | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the existing `<div>` nodes inside `.offer-row__meta-grid` so `Okres ochrony` is the fourth cell and `Aktualizacja` is the fifth cell. |
| 1.2 | Preserve existing bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `coveragePeriodLabel()` and `offer.updatedAt | date` unchanged; only move their template positions. |

#### Phase 2: Add regression coverage for column order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add unit test for metadata order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Assert that the labels rendered inside the first `.offer-row__meta-grid` appear in the expected order, ending with `Okres ochrony`, then `Aktualizacja`. |
| 2.2 | Update e2e test to validate order | `tests/e2e/offers-protection-period.spec.ts` | Extend the existing offers protection-period scenario to verify that, within an offer row, `Okres ochrony` appears before `Aktualizacja`. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the existing metadata cells so `Okres ochrony` is directly before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression coverage for the metadata label order in the offers row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Verify the visible row order of `Okres ochrony` and `Aktualizacja` in the browser flow. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] On `/offers`, each offer row shows `Okres ochrony` before `Aktualizacja`
5. [ ] `Okres ochrony` still shows the same computed value as before
6. [ ] `Aktualizacja` still shows the same date/time value as before
7. [ ] Responsive layout preserves the same field sequence