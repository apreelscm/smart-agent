# SDLC-85: Kolumna okres ochrony na liscie ofert powinna byc przed kolumna Aktualizacja

## Specification

### Overview
This task reorders the metadata fields on the offers list so that `Okres ochrony` is displayed before `Aktualizacja`. The repository already contains the protection-period value and rendering logic added in the offers list view; this change is limited to correcting the presentation order in the existing offer row layout.

### User Stories
- US-001: As a sales agent, I want `Okres ochrony` to appear before `Aktualizacja` on the offers list, so that the row information matches the expected business layout.
  - Given the offers list is visible / When an offer row is rendered / Then the `Okres ochrony` field is shown before `Aktualizacja`.
- US-002: As a user reviewing offers, I want the existing protection-period value to remain unchanged while only its position moves, so that I keep the same information in the expected place.
  - Given an offer row contains both fields / When I read the metadata section / Then `Okres ochrony` still shows the same formatted value and `Aktualizacja` remains visible after it.

### Functional Requirements
- FR-001: Reorder the metadata blocks in `src/app/features/offers/pages/offers-home-page.component.html` so `Okres ochrony` appears before `Aktualizacja` within each offer row.
- FR-002: Keep the existing `Okres ochrony` label and value binding unchanged.
- FR-003: Keep the existing `Aktualizacja` label and date/time formatting unchanged.
- FR-004: Do not change filtering, sorting, navigation, status actions, premium display, or other offer-row content.
- FR-005: Do not change the offer data model, repositories, or protection-period calculation utility for this task.

### Edge Cases
- EC-001: On responsive breakpoints, the visual order should still follow DOM order, so `Okres ochrony` remains before `Aktualizacja` when the grid collapses.
- EC-002: Rows with long protection-period text must continue to wrap correctly after reordering.
- EC-003: If no offers are shown, the empty state remains unchanged and no metadata order assertion applies.

### Success Criteria
- [ ] Each rendered offer row shows `Okres ochrony` before `Aktualizacja`.
- [ ] The protection-period value remains populated and formatted as before.
- [ ] The `Aktualizacja` field remains present and formatted as before.
- [ ] Existing unit and/or e2e coverage verifies the new order.
- [ ] No unrelated offers-list behaviors regress.

### Open Questions
- None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular component in `src/app/features/offers/pages/offers-home-page.component.ts` with the row structure defined in `src/app/features/offers/pages/offers-home-page.component.html`. Although the Jira says “column”, the screen is not a literal table; it uses the `.offer-row__meta-grid` layout from `src/app/features/offers/pages/offers-home-page.component.scss`.

The current template places the `Aktualizacja` metadata block before the `Okres ochrony` block. Since the protection-period value is already computed via `formatProtectionPeriod()` and exposed through `protectionPeriodLabel`, the lowest-risk fix is to swap the two markup blocks in the template rather than introduce CSS `order` rules or new component logic. This keeps:
- accessibility and reading order aligned with visual order,
- responsive layout behavior consistent with DOM order,
- existing business logic untouched.

Confluence findings:
- No existing offer-list service, endpoint, or API contract relevant to this task was found in the configured `Services` space — proposing no service/API changes.
- Domain context found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which confirms the application is a sales-agent UI with offer-related screens, but no more specific convention for this field ordering was documented.

### Task Breakdown

#### Phase 1: Reorder the offers list metadata
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Swap metadata block order in template | `src/app/features/offers/pages/offers-home-page.component.html` | Move the existing `Okres ochrony` block so it renders immediately before the existing `Aktualizacja` block inside `.offer-row__meta-grid`. |
| 1.2 | Validate layout impact | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss` | Confirm the existing grid styling still works after the DOM reorder; only adjust SCSS if the reordered span item causes layout issues at current breakpoints. |

#### Phase 2: Add regression coverage for field order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Extend component spec for DOM order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Add an assertion against the first rendered row’s metadata labels to verify `Okres ochrony` appears before `Aktualizacja`. |
| 2.2 | Extend e2e verification | `tests/e2e/offers-protection-period.spec.ts` | Add a browser-level assertion that the visible offer row metadata order places `Okres ochrony` before `Aktualizacja`, alongside the existing protection-period checks. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the existing metadata sections so `Okres ochrony` is before `Aktualizacja`. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression coverage for metadata label order in the rendered row. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add end-to-end verification that the field order is correct in the browser. |
| `src/app/features/offers/pages/offers-home-page.component.scss` | MODIFY (if needed) | Only adjust if visual verification shows the reordered spanning item breaks the current grid layout. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] On the offers list, `Okres ochrony` appears before `Aktualizacja` for visible rows
5. [ ] The protection-period and update values remain unchanged after the reorder