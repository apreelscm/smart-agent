# SDLC-74: Na ekranie listy ofert okres ochrony powinien byc przed kolumna Aktualizacja

## Specification

### Overview
This task reorders the metadata shown for each offer on the offers list so that `Okres ochrony` appears before `Aktualizacja`. In the current implementation, the offers list is rendered as card rows in `src/app/features/offers/pages/offers-home-page.component.html`, and the business term “column” maps to the items inside `.offer-row__meta-grid`.

The task does not introduce new data, new calculations, or backend changes. It only changes the visual order of two existing metadata blocks while preserving the current protection-period behavior added earlier.

### User Stories
- US-001: As a sales agent, I want to see `Okres ochrony` before `Aktualizacja` on the offers list, so that the row layout matches the expected business order.
  - Given I open the offers list
  - When offer rows are rendered
  - Then the `Okres ochrony` field is displayed before `Aktualizacja`

- US-002: As a user reviewing multiple offers, I want the metadata order to be consistent across all rows, so that I can scan the list quickly.
  - Given multiple offers are visible
  - When I compare their metadata blocks
  - Then every row uses the same field order with `Okres ochrony` before `Aktualizacja`

### Functional Requirements
- FR-001: Reorder the offer-row metadata in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` is rendered before `Aktualizacja`.
- FR-002: Keep the existing `Okres ochrony` value source unchanged; it must continue using the already exposed `protectionPeriodDisplay`.
- FR-003: Keep the existing `Aktualizacja` value source unchanged; it must continue using `offer.updatedAt`.
- FR-004: Apply the new order consistently for all visible offer rows, regardless of product or status.
- FR-005: Do not change filtering, sorting, transitions, navigation, or premium presentation on the offers list.
- FR-006: Add automated coverage that verifies the rendered order of these two metadata fields.

### Edge Cases
- EC-001: Both motor and crop offer rows must keep the same metadata order after the template change.
- EC-002: Responsive layouts must preserve the relative order even when the grid collapses to fewer columns at narrower breakpoints.
- EC-003: Existing tests that only verify presence of `Okres ochrony` should be extended so a future regression in display order is caught.

### Success Criteria
- [ ] On the offers list, `Okres ochrony` is visually rendered before `Aktualizacja`.
- [ ] The displayed values for both fields remain unchanged after the reorder.
- [ ] All offer rows use the same metadata order.
- [ ] Existing filters, sorting, and actions still work without regression.
- [ ] Automated tests verify the new order.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a narrow UI-only change in the offers feature. The offers list is implemented as a standalone Angular component with an HTML template-driven metadata grid:

- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.spec.ts`

The current template renders `Aktualizacja` before `Okres ochrony`. The implementation should simply swap these two metadata blocks in `offers-home-page.component.html`, keeping all bindings intact:
- `Okres ochrony` continues to use `protectionPeriodDisplay`
- `Aktualizacja` continues to use `offer.updatedAt | date`

No TypeScript logic, repository code, mock data, or styling changes should be made unless verification shows a layout regression. The SCSS already supports a five-item `.offer-row__meta-grid`, so this ticket should stay scoped to template order plus regression tests.

Confluence findings:
- No existing service/API relevant to this task was found in the `Services` space — no service reuse is needed.
- A general domain page was found in SDLC: [Ekrany](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany). It provides product context only and does not define a stricter implementation constraint for this metadata ordering change.

Reference codebase patterns:
- The offers list “columns” are implemented as `<div>` items inside `.offer-row__meta-grid` in `src/app/features/offers/pages/offers-home-page.component.html`.
- Existing component tests in `src/app/features/offers/pages/offers-home-page.component.spec.ts` already inspect rendered metadata blocks and are the right place to add DOM-order assertions.
- Existing Playwright coverage in `tests/e2e/offers-protection-period.spec.ts` already targets these rows and can be extended to assert the visible label order in the browser.

### Task Breakdown

#### Phase 1: Reorder the offers-list metadata
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Swap metadata block order in template | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` block so it is rendered immediately before the existing `Aktualizacja` block inside `.offer-row__meta-grid`. |
| 1.2 | Preserve current bindings | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `protectionPeriodDisplay` and `offer.updatedAt` bindings unchanged while reordering only the DOM structure. |

#### Phase 2: Add regression coverage for field order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component rendering test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add an assertion that the metadata labels within an offer row appear in the expected order, specifically verifying `Okres ochrony` comes before `Aktualizacja`. |
| 2.2 | Extend browser-level verification | `tests/e2e/offers-protection-period.spec.ts` | Add a Playwright assertion on the first visible offer row that the rendered metadata labels place `Okres ochrony` before `Aktualizacja`, preventing UI-order regressions. |

#### Phase 3: Validate no unintended regressions
| # | Task | Files | Description |
|---|------|-------|-------------|
| 3.1 | Re-run existing offers checks | `src/app/features/offers/pages/offers-home-page.component.spec.ts`, `tests/e2e/offers-protection-period.spec.ts` | Ensure the reorder does not affect existing assertions for presence and value of the protection period. |
| 3.2 | Confirm layout remains intact | `src/app/features/offers/pages/offers-home-page.component.html` | Verify that the same five metadata cells still render correctly without requiring SCSS changes. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the metadata cells so `Okres ochrony` appears before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add unit-level DOM-order assertions for the metadata labels in each offer row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add browser-level verification that `Okres ochrony` is rendered before `Aktualizacja`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] `npm run build` completes successfully
5. [ ] `npm test` passes with the updated `OffersHomePageComponent` spec
6. [ ] `npm run e2e` passes with the updated offers protection-period scenario
7. [ ] Manual check on `/offers` confirms `Okres ochrony` is shown before `Aktualizacja` on all rows