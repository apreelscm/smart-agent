# SDLC-84: Kolumna okres ochrony na liscie ofert powinna byc przed kolumna Aktualizacja

## Specification

### Overview
This task adjusts the visual order of metadata on the offers list so that `Okres ochrony` is displayed before `Aktualizacja` for every offer row. The change is purely presentational and must preserve the existing protection-period value, formatting, filtering, sorting, actions, and responsive layout behavior.

### User Stories
- US-001: As a sales agent, I want to see `Okres ochrony` before `Aktualizacja` on the offers list, so that the row information appears in the expected business order.
  - Given the offers list is visible / When an offer row is rendered / Then the `Okres ochrony` field appears before `Aktualizacja`.
- US-002: As a user reviewing many offers, I want the column order to remain consistent for all rows, so that I can scan the list quickly.
  - Given multiple offers are shown / When I compare rows / Then the metadata order is identical across all rows.
- US-003: As a user of the current offers page, I want only the field order to change, so that existing list behavior remains stable.
  - Given the offers page already supports filtering, sorting, and actions / When this task is delivered / Then those behaviors continue unchanged.

### Functional Requirements
- FR-001: In `src/app/features/offers/pages/offers-home-page.component.html`, the `Okres ochrony` metadata block must be rendered before the `Aktualizacja` metadata block inside `offer-row__meta-grid`.
- FR-002: The visible label text for both fields must remain unchanged: `Okres ochrony` and `Aktualizacja`.
- FR-003: The protection-period value must continue to use the existing `protectionPeriodLabel()` binding.
- FR-004: The update date/time must continue to use the existing `offer.updatedAt | date` bindings.
- FR-005: The change must apply uniformly to all offer rows, including motor and crop offers.
- FR-006: No repository, model, or mock-data contract changes are required for this task.
- FR-007: Existing filtering, sorting, navigation, row actions, and premium/status rendering must remain unchanged.

### Edge Cases
- EC-001: When no offers are visible, the empty state must remain unchanged and no metadata-order assertions should depend on absent rows.
- EC-002: The reordered markup must preserve the existing responsive grid behavior, including the wider protection-period cell styling via `.offer-row__meta-item--protection`.
- EC-003: Both motor and crop offers must show the same metadata order even though other row fields differ by product type.
- EC-004: Reordering must not accidentally duplicate or remove either metadata field.

### Success Criteria
- [ ] Every rendered offer row shows `Okres ochrony` before `Aktualizacja`.
- [ ] The displayed values for both fields remain unchanged from current behavior.
- [ ] Existing offers-page interactions continue to work without regression.
- [ ] Automated tests verify the new field order.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
This is a small UI-only adjustment in the existing Angular standalone offers page. The current offers-list implementation lives in `src/app/features/offers/pages/offers-home-page.component.html`, where the metadata row is rendered through `offer-row__meta-grid`. The `Okres ochrony` block already exists but is currently placed after `Aktualizacja`; the implementation should simply move that block earlier in the template so the DOM and visual order match the Jira expectation.

The current component logic in `src/app/features/offers/pages/offers-home-page.component.ts` already exposes `protectionPeriodLabel()` via a computed signal, and the styles in `src/app/features/offers/pages/offers-home-page.component.scss` already support the protection field through `.offer-row__meta-item--protection`. Because the requirement is only about order, no TypeScript or styling change should be made unless verification shows a layout regression.

Confluence findings:
- No existing offer-list service, endpoint, or integration reference was found in the configured `Services` space — no service reuse applies.
- Domain context found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the application is an offer-centric sales workflow UI. No more specific domain convention for this column ordering was found in `SDLC`.

### Task Breakdown

#### Phase 1: Reorder the metadata blocks
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Move protection-period block before update block | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the two existing metadata `<div>` blocks inside `.offer-row__meta-grid` so `Okres ochrony` appears before `Aktualizacja` without changing bindings or labels. |
| 1.2 | Verify no style changes are needed | `src/app/features/offers/pages/offers-home-page.component.scss` | Confirm the existing `.offer-row__meta-item--protection` span/layout still renders correctly after the template reorder; modify only if a real layout issue appears. |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component test for metadata order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add/assert DOM order so the test verifies `Okres ochrony` is rendered before `Aktualizacja` within each offer row, while keeping current protection-period assertions. |
| 2.2 | Extend e2e coverage for visible order | `tests/e2e/offers-protection-period.spec.ts` | Add a browser-level assertion that the metadata labels appear in the expected order on the rendered row(s), preventing future regressions in the offers-list markup. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the existing metadata blocks so `Okres ochrony` appears before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression assertions for metadata order in the offers row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add end-to-end verification that the visible field order matches the Jira requirement. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY (if needed) | Only adjust styles if verification shows a layout issue after reordering. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check on `/offers` confirms `Okres ochrony` is displayed before `Aktualizacja` in every visible row
5. [ ] Manual check confirms no regression in filtering, sorting, row actions, or responsive layout