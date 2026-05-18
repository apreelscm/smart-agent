# SDLC-126: Na ekranie listy kwitariuszy przesun kolumne ze statusem przed kolumne z Kwota (z odsetkami)

## Specification

### Overview
This task delivers a presentation-only change on the kwitariusze list screen so the `Status` column is rendered immediately before `Kwota (z odsetkami)`.

In the current implementation, `src/app/features/kwitariusze/kwitariusze.ts` defines `displayedColumns` as:
`['type', 'number', 'policyNumber', 'insuredName', 'issueDate', 'amount', 'status', 'actions']`

That order places `amount` before `status`, which does not match the Jira requirement. The change should update the table column order without modifying data structures, filter behavior, sorting logic, or cell content.

### User Stories
- US-001: As a user reviewing the kwitariusze list, I want to see the `Status` column before `Kwota (z odsetkami)`, so that I can assess document state before looking at the amount.
  - Given I open `/rozliczenia/kwitariusze` / When the table is rendered / Then `Status` appears directly to the left of `Kwota (z odsetkami)`

### Functional Requirements
- FR-001: The kwitariusze list table must render the `Status` column before the `Kwota (z odsetkami)` column.
- FR-002: `Status` must remain directly adjacent to `Kwota (z odsetkami)` on its left side.
- FR-003: The names of both columns must remain unchanged.
- FR-004: The contents of `Status` cells must remain unchanged and continue using the current badge rendering from `src/app/features/kwitariusze/kwitariusze.html`.
- FR-005: The contents and formatting of `Kwota (z odsetkami)` cells must remain unchanged.
- FR-006: All other columns must preserve their existing relative order.
- FR-007: Existing filters, result count, row actions, and routing links on the kwitariusze list must remain unchanged.
- FR-008: If column sorting is available, moving the column must not alter how sorting works for `Kwota (z odsetkami)` or any other sortable column.

### Edge Cases
- EC-001: Sticky header rendering must preserve the same visible order as row cells after the column move.
- EC-002: If the table is empty or filtered to zero rows, the header order must still show `Status` before `Kwota (z odsetkami)`.
- EC-003: Reordering columns must not break the existing `mat-sort-header` on the amount column.
- EC-004: Reordering columns must not affect the non-sortable nature of the `Status` column, which currently has no `mat-sort-header`.

### Success Criteria
- [ ] On `/rozliczenia/kwitariusze`, the `Status` header is visible before `Kwota (z odsetkami)`
- [ ] `Status` is directly adjacent to `Kwota (z odsetkami)` on the left
- [ ] Status badges render exactly as before
- [ ] Amount values and formatting render exactly as before
- [ ] No filters or actions on the kwitariusze list regress
- [ ] Existing unit tests pass and new coverage verifies the column order

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
This is a narrow UI configuration change in the standalone Angular kwitariusze feature.

Relevant codebase structure:
- `src/app/features/kwitariusze/kwitariusze.ts` holds the `displayedColumns` array used by the Angular Material table.
- `src/app/features/kwitariusze/kwitariusze.html` defines all `ng-container matColumnDef="..."` blocks, including `amount` and `status`.
- `src/app/features/kwitariusze/kwitariusze.spec.ts` contains feature-level unit tests for the component.
- `tests/e2e/kwitariusze-status-filter.spec.ts` shows existing browser coverage patterns for this screen, but the ticket itself can likely be covered sufficiently at component-test level.

Repository pattern to follow:
- The table order is controlled centrally by `displayedColumns`, while the template already contains both column definitions. Because Angular Material renders header and row cell order from `displayedColumns`, the minimal and correct implementation is to reorder that array rather than moving or rewriting the template blocks.
- This keeps scope limited to presentation, aligns with the Jira requirement, and avoids unnecessary changes to sorting or cell markup.

Confluence discovery result:
- No existing service/API documentation relevant to this UI-only change was found in the configured `Services` space.
- No relevant domain guidance for kwitariusze column conventions was found in the configured `SDLC` space.
- Since the ticket explicitly states there are no integration changes, no external service reuse is required.

Key decision:
- Update only the `displayedColumns` sequence from `... 'amount', 'status', ...` to `... 'status', 'amount', ...`.
- Add regression coverage that asserts rendered header order, so the change is protected against future accidental reordering.

### Task Breakdown

#### Phase 1: Reorder the kwitariusze table columns
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Update displayed column order | `src/app/features/kwitariusze/kwitariusze.ts` | Change the `displayedColumns` array so `status` appears immediately before `amount` while all other columns keep their current order |
| 1.2 | Confirm template compatibility | `src/app/features/kwitariusze/kwitariusze.html` | Verify the existing `matColumnDef="status"` and `matColumnDef="amount"` blocks can remain unchanged because Angular Material uses `displayedColumns` for render order |

#### Phase 2: Add regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Add component test for header order | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Add a test that renders `KwitariuszeComponent` and asserts the header sequence places `Status` directly before `Kwota (z odsetkami)` |
| 2.2 | Add component test for row cell order | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Assert that a rendered row places the status badge cell before the amount cell, protecting both header and body ordering |
| 2.3 | Ensure no behavior regressions | `src/app/features/kwitariusze/kwitariusze.spec.ts` | Keep current tests intact so existing status filter and currency behaviors continue to pass after the column reorder |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/kwitariusze/kwitariusze.ts` | MODIFY | Reorder `displayedColumns` so `status` renders before `amount` |
| `src/app/features/kwitariusze/kwitariusze.spec.ts` | MODIFY | Add regression tests validating header and row column order |

### Verification Steps
1. [ ] Build succeeds
2. [ ] Tests pass
3. [ ] New tests cover requirements
4. [ ] Open `/rozliczenia/kwitariusze`
5. [ ] Confirm the table header shows `Status` immediately before `Kwota (z odsetkami)`
6. [ ] Confirm row cells render the status badge before the amount cell
7. [ ] Confirm amount formatting and status badge styling remain unchanged
8. [ ] Confirm filtering and row actions still work as before