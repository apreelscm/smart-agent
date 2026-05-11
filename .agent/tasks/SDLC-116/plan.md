# SDLC-116: Na liście procesów przesuń kolumnę PH za eMail PH wylacznie na ekranie listy procesow

## Specification

### Overview
Zmiana dotyczy wyłącznie prezentacji tabeli na ekranie listy procesów. Kolumna `PH` ma zostać przeniesiona tak, aby była renderowana bezpośrednio po kolumnie `Email PH`, bez zmian w danych, logice filtrowania, sortowania, paginacji, nawigacji do detalu ani w innych widokach systemu.

### User Stories
- US-001: As a user reviewing the process list, I want the `PH` column to appear immediately after `Email PH`, so that the table layout matches the expected business order.
  - Given the user opens the process list / When the table is rendered / Then the `PH` column is displayed directly after `Email PH`
- US-002: As a user working with the process list, I want sorting, filtering, pagination, and detail navigation to behave as before, so that the UI-only column reorder does not introduce regressions.
  - Given the user interacts with the existing table controls / When the column order change is deployed / Then all existing list behaviors continue to work unchanged

### Functional Requirements
- FR-001: On `src/app/features/processes/process-list/process-list.component.ts`, the table header order must render `Email PH` immediately before `PH`.
- FR-002: In each process row, the `phEmail` cell must render immediately before the `phName` cell.
- FR-003: The change must be limited to the process list view available under `/processes`.
- FR-004: No changes may be introduced to `ProcessListService`, mock data structure, routing, filtering, sorting, pagination, or detail links.
- FR-005: Existing automated tests for process list column order must be updated to validate the new order in both unit and E2E coverage.

### Edge Cases
- EC-001: On responsive breakpoints where both `Email PH` and `PH` remain visible, their visual order must still be `Email PH` followed by `PH`.
- EC-002: Reordering adjacent columns must not alter displayed values; `phEmail` must still contain the email value and `phName` must still contain the salesperson name.
- EC-003: Sort, filter, and pagination controls must remain bound to the same data and continue working even though the visible column sequence changes.
- EC-004: Other screens, including `process-detail` and any future views using the same model/service, must not inherit unintended column-order changes.

### Success Criteria
- [ ] On `/processes`, the header sequence shows `Email PH` directly followed by `PH`
- [ ] The first and subsequent table rows render `phEmail` directly before `phName`
- [ ] `npm run test` passes with updated unit assertions
- [ ] `npm run e2e` passes with updated Playwright assertions
- [ ] No service, model, or routing behavior changes are required to deliver the ticket

### Open Questions
None.

---

## Implementation Plan

### Technical Approach
The process list is implemented as a standalone Angular component with an inline template and inline responsive styles in `src/app/features/processes/process-list/process-list.component.ts`. The safest implementation is to reorder only the two adjacent table columns in that template:

- swap the `<th>` elements for `PH` and `Email PH`
- swap the matching `<td>` render order for `item.phName` and `item.phEmail`

This keeps the change strictly in the presentation layer, which matches the Jira scope and the current codebase structure.

Relevant Confluence guidance found in the configured `Services` space:
- Process list feature context: https://apreel.atlassian.net/wiki/spaces/Services/pages/169213953/F02+-+Process+List+Filtering+ZRD
- Existing E2E/source-of-truth notes for process list behavior: https://apreel.atlassian.net/wiki/spaces/Services/pages/169410561/F02+Process+List+Filtering+ZRD+E2E+Test+Scenarios

These pages indicate the process list is the primary browsing view with filtering, sorting, and pagination, and that legacy behavior is the source of truth. This plan reuses that guidance by preserving all current list behaviors and limiting the change to column render order only.

No relevant DOMAIN guidance was found in the `SDLC` space.

Repository patterns to follow:
- keep feature UI logic inside the existing standalone component rather than introducing new abstractions
- preserve current `data-testid` usage and DOM-query testing style in `process-list.component.spec.ts`
- preserve current Playwright coverage style in `tests/e2e/process-list-column-order.spec.ts`

No external service/API integration is required for this task, and the existing in-repo `ProcessListService` should remain unchanged.

### Task Breakdown

#### Phase 1: Update process list column render order
| # | Task | Files | Description |
|---|------|-------|-------------|
| 1.1 | Reorder table headers | `src/app/features/processes/process-list/process-list.component.ts` | Move the `Email PH` header so it renders before `PH` in the process list table header. |
| 1.2 | Reorder row cells | `src/app/features/processes/process-list/process-list.component.ts` | Swap the render order of `item.phEmail` and `item.phName` in each `<tr>` so row values match the new header order. |
| 1.3 | Keep responsive styling aligned | `src/app/features/processes/process-list/process-list.component.ts` | Review inline responsive comments/width rules tied to `.col-phEmail` and `.col-phName`; keep existing widths but ensure no stale assumptions about the old order remain. |

#### Phase 2: Update regression coverage
| # | Task | Files | Description |
|---|------|-------|-------------|
| 2.1 | Update unit header-order assertion | `src/app/features/processes/process-list/process-list.component.spec.ts` | Change the spec to assert that `PH` is immediately after `Email PH`, not before it. |
| 2.2 | Update unit row-order assertion | `src/app/features/processes/process-list/process-list.component.spec.ts` | Change the row-cell assertion so `phName` is expected immediately after `phEmail`. |
| 2.3 | Update Playwright expected header sequence | `tests/e2e/process-list-column-order.spec.ts` | Adjust the expected header array to place `Email PH` before `PH`. |
| 2.4 | Update Playwright row-value assertion | `tests/e2e/process-list-column-order.spec.ts` | Adjust the index assertions so the email cell precedes the PH-name cell while keeping sorting/filtering/pagination/detail-navigation checks intact. |

### File Change Summary
| File | Action | Description |
|------|--------|-------------|
| `src/app/features/processes/process-list/process-list.component.ts` | MODIFY | Reorder `Email PH` and `PH` columns in the inline table template; keep change limited to the process list view. |
| `src/app/features/processes/process-list/process-list.component.spec.ts` | MODIFY | Update unit tests to verify the new header and row cell order. |
| `tests/e2e/process-list-column-order.spec.ts` | MODIFY | Update E2E assertions to validate the new column order while confirming existing table behaviors still work. |

### Verification Steps
1. [ ] `npm run build` succeeds
2. [ ] `npm run test -- --watch=false --browsers=ChromeHeadless` passes
3. [ ] `npm run e2e` passes
4. [ ] Manual check on `/processes` confirms `Email PH` is directly followed by `PH`
5. [ ] Manual check confirms filters, sorting, pagination, and detail navigation still behave as before