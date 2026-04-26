# SDLC-86: Kolumna okres ochrony na liscie ofert powinna byc przed kolumna Aktualizacja

## Specification

### Overview
This task reorders the existing `Okres ochrony` field on the offers list so it appears before `Aktualizacja` in each offer row. The feature does not introduce new business logic or data sources; it only changes the presentation order of already-rendered metadata to match the requested UI layout.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the offer metadata is shown in the expected business order.
  - Given I open the offers list / When offer rows are rendered / Then `Okres ochrony` is displayed before `Aktualizacja` in each row.
- US-002: As a user reviewing offers, I want the existing protection-period value to remain visible and unchanged, so that only the layout changes and not the meaning of the data.
  - Given an offer row is visible / When I inspect its metadata / Then the same `Okres ochrony` value is shown, only in a new position.

### Functional Requirements
- FR-001: Reorder the offer metadata blocks in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` is rendered before `Aktualizacja`.
- FR-002: Keep the existing `Okres ochrony` label and value unchanged.
- FR-003: Keep the existing `Aktualizacja` label and formatting unchanged.
- FR-004: Do not change offer filtering, sorting, transitions, navigation, or premium display behavior.
- FR-005: Do not change offer models, repositories, mock payloads, or protection-period calculation utilities for this task.

### Edge Cases
- EC-001: On desktop layouts, the DOM order must produce the requested visual order even with `offer-row__meta-item--protection` spanning multiple grid columns.
- EC-002: On tablet/mobile breakpoints, the stacked layout must remain readable and must still keep `Okres ochrony` before `Aktualizacja` in DOM/render order.
- EC-003: Empty-state behavior must remain unchanged; when no offers are shown, neither metadata field is rendered.
- EC-004: Crop and motor offers must both preserve the same metadata ordering rule.

### Success Criteria
- [ ] On `/offers`, each visible offer row shows `Okres ochrony` before `Aktualizacja`.
- [ ] The existing protection-period value remains unchanged after the reorder.
- [ ] Responsive layouts still render the metadata grid correctly.
- [ ] Existing offers-page behaviors continue to work without regressions.
- [ ] Automated tests cover the new ordering expectation.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular component in `src/app/features/offers/pages/offers-home-page.component.ts` with the row layout defined in `src/app/features/offers/pages/offers-home-page.component.html`. Although the Jira refers to a “column,” this screen is currently rendered as a card/grid metadata layout using `.offer-row__meta-grid`, so the correct implementation is to reorder the relevant metadata blocks in the template rather than introduce a new table structure.

This task should reuse the existing implementation added for protection period:
- `Okres ochrony` is already computed through `formatProtectionPeriod()` and exposed as `protectionPeriodLabel`.
- No service/API integration is involved.
- No model or repository change is needed.

Confluence findings:
- No existing offer-list service, endpoint, or API reference was found in the configured `Services` space, so no external service reuse applies.
- Domain context found: [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms this is a sales workflow UI and supports treating this change as a screen-level presentation adjustment.
- No more specific ADR or convention for offer-row column ordering was found in the `SDLC` space.

Planned implementation:
- Move the existing `Okres ochrony` metadata block so it is declared immediately before the `Aktualizacja` block in `offers-home-page.component.html`.
- Keep the current CSS class `offer-row__meta-item--protection` and existing grid styling unless a quick visual check shows a layout regression; based on the current SCSS, the span-based layout should continue to work without stylesheet changes.
- Strengthen regression coverage in the existing offers component test and/or Playwright offers test to verify ordering, not just presence.

### Task Breakdown

#### Phase 1: Reorder the offer metadata layout
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Move protection-period block before update block | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the existing metadata sections inside `.offer-row__meta-grid` so `Okres ochrony` is rendered before `Aktualizacja` for every offer row. |
| 1.2 | Validate existing layout classes still apply | `src/app/features/offers/pages/offers-home-page.component.html` | Keep `offer-row__meta-item--protection` on the moved block so the existing grid-span behavior remains intact. |

#### Phase 2: Add regression coverage for ordering
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component-level regression test | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add assertions that the metadata label order in a rendered offer row places `Okres ochrony` before `Aktualizacja`, while preserving the existing non-empty value checks. |
| 2.2 | Extend e2e protection-period test | `tests/e2e/offers-protection-period.spec.ts` | Add a DOM-order assertion for the first visible offer row so Playwright verifies the requested UI ordering in the browser. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder metadata blocks so `Okres ochrony` appears before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression assertions for metadata ordering in rendered offer rows. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Verify browser-rendered order of `Okres ochrony` and `Aktualizacja`. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/offers` and confirm `Okres ochrony` appears before `Aktualizacja` on visible rows
5. [ ] Verify the displayed protection-period value is unchanged after the reorder
6. [ ] Verify the layout remains correct on desktop and narrow viewport widths