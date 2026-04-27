# SDLC-96: Na liscie ofert okres ochrony powinien byc przed kolumna Wariant

## Specification

### Overview
This task adjusts the visual order of metadata shown for each row on the offers list so that **Okres ochrony** appears before **Wariant**. The protection-period value and existing variant behavior already exist in the current implementation; this ticket only changes the presentation order on the `/offers` screen.

### User Stories
- US-001: As a sales agent, I want to see the protection period before the variant on the offers list, so that the row metadata matches the expected business reading order.
  - Given I open the offers list / When offer rows are rendered / Then the `Okres ochrony` field is displayed before the `Wariant` field in each row.
- US-002: As a user reviewing renewal offers, I want the variant section to remain intact after the reorder, so that I can still access the selected variant and renewal link.
  - Given an offer row contains variant details or a renewal link / When the metadata order is updated / Then the variant content is still rendered correctly after the protection-period field.

### Functional Requirements
- FR-001: On `src/app/features/offers/pages/offers-home-page.component.html`, the metadata block labeled `Okres ochrony` must be rendered before the block labeled `Wariant`.
- FR-002: The reordered metadata sequence must apply to every visible offer row on the offers list.
- FR-003: The existing protection-period value generation must remain unchanged.
- FR-004: The existing variant rendering logic, including selected variant name and renewal link behavior, must remain unchanged.
- FR-005: The change must stay within the offers-list presentation and regression-test layers; no repository, model, or API contract changes are required.
- FR-006: The updated order must remain consistent in responsive layouts where the metadata grid collapses to fewer columns or a single column.

### Edge Cases
- EC-001: For renewal offers, the `Wariant` block must still render the source-policy link after being moved after `Okres ochrony`.
- EC-002: For crop offers and motor offers, the metadata order must be identical even though other row content differs.
- EC-003: On narrow screens where `.offer-row__meta-grid` stacks items vertically, `Okres ochrony` must still appear before `Wariant`.
- EC-004: If filters return no offers, the empty state remains unchanged.

### Success Criteria
- [ ] Each offer row on `/offers` shows `Okres ochrony` before `Wariant`.
- [ ] The displayed protection-period value remains unchanged from current behavior.
- [ ] Variant text and renewal-link behavior continue to work after the reorder.
- [ ] Automated tests verify the rendered order regression.
- [ ] No repository, model, mock-data, or runtime integration changes are introduced.

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The offers list is implemented as a standalone Angular page in:
- `src/app/features/offers/pages/offers-home-page.component.ts`
- `src/app/features/offers/pages/offers-home-page.component.html`
- `src/app/features/offers/pages/offers-home-page.component.scss`

The current HTML already renders both metadata blocks inside `.offer-row__meta-grid`, and the visible order is driven directly by DOM order. Because of that, the runtime behavior can be changed by moving the existing `Okres ochrony` block above the existing `Wariant` block in the template. No TypeScript change is expected, since `protectionPeriodLabel` is already computed and bound correctly in `offers-home-page.component.ts`.

Repository patterns indicate this feature should remain a page-level UI change:
- the offers page uses inline template sections for row metadata rather than a dedicated table/column abstraction,
- existing unit coverage lives in `offers-home-page.component.spec.ts`,
- existing end-to-end coverage for the protection period already exists in `tests/e2e/offers-protection-period.spec.ts`.

Confluence discovery results:
- No existing service/API relevant to this UI-only change was found in `Services` space — no reuse candidate required.
- Domain context was found in [`Ekrany`](https://apreel.atlassian.net/wiki/spaces/SDLC/pages/161120257/Ekrany), which describes smart-agent screens from the business perspective. This supports keeping the change scoped to the existing offers-list screen presentation rather than redesigning the feature.

Key decision:
- **Use template reordering only as the runtime change.**  
  This keeps scope minimal, preserves current protection-period calculation, and automatically applies the new sequence to desktop and stacked mobile layouts because CSS grid follows DOM order.

Testing approach:
- update the component spec to assert the metadata label order in rendered markup,
- update the Playwright regression to confirm the visible order on the offers list, while keeping existing protection-period assertions.

### Task Breakdown

#### Phase 1: Reorder offers-list metadata
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Move protection-period block before variant block | `src/app/features/offers/pages/offers-home-page.component.html` | Reorder the existing metadata `<div>` sections inside `.offer-row__meta-grid` so `Okres ochrony` is rendered before `Wariant` for every offer row. |
| 1.2 | Preserve variant-specific behavior after reorder | `src/app/features/offers/pages/offers-home-page.component.html` | Keep the current variant content unchanged, including selected variant text and renewal-policy link rendering. |
| 1.3 | Validate no style or logic changes are needed | `src/app/features/offers/pages/offers-home-page.component.html`, `src/app/features/offers/pages/offers-home-page.component.scss`, `src/app/features/offers/pages/offers-home-page.component.ts` | Confirm the existing grid styling and `protectionPeriodLabel` logic still work with the new DOM order; avoid unnecessary TS/SCSS edits unless visual regression is observed. |

#### Phase 2: Add regression coverage for field order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add unit assertion for metadata order | `src/app/features/offers/pages/offers-home-page.component.spec.ts` | Extend the component spec to verify the rendered `.offer-row__meta-grid` places `Okres ochrony` before `Wariant` in the DOM/text order. |
| 2.2 | Add e2e assertion for visible order | `tests/e2e/offers-protection-period.spec.ts` | Extend the existing Playwright offers protection-period test to assert the first visible offer row shows `Okres ochrony` before `Wariant`, preserving current value assertions. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/offers/pages/offers-home-page.component.html` | MODIFY | Reorder the metadata blocks so protection period appears before variant. |
| `src/app/features/offers/pages/offers-home-page.component.spec.ts` | MODIFY | Add regression coverage for metadata order in the rendered component. |
| `tests/e2e/offers-protection-period.spec.ts` | MODIFY | Add browser-level verification that the visible offers row order matches the requirement. |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Manual check on `/offers` confirms `Okres ochrony` appears before `Wariant` on desktop
5. [ ] Manual check on a narrow viewport confirms stacked metadata still shows `Okres ochrony` before `Wariant`